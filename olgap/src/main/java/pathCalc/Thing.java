/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.Dataset;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

import Exceptions.CircularReferenceException;
import Exceptions.HandledException;
import Exceptions.NullValueReturnedException;
import Exceptions.ScriptFailedException;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQL.PathQL;
import pathQLModel.NullValue;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLRepository.SEEQSource;
import pathQLResults.ResourceResults;

/**
 * The Class Thing.
 */
public class Thing extends Resource {

	private static final String NULLVALUERETURNED_EXCEPTION = "NullValueReturned";
	private static final String SCRIPTFAILED_EXCEPTION = "ScriptFailed";
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	private static final String SCRIPTNOTFOUND_EXCEPTION = null;
	private static final String NULLVALUESCRIPT_EXCEPTION = null;
	/** The logger. */
	protected final Logger logger = LogManager.getLogger(Thing.class);
	private Graph graph;
	public static Thing create(PathQLRepository source, org.eclipse.rdf4j.model.Value superValue, EvaluationContext evaluationContext) {
		Thing thing ;
		if(superValue!=null && source!=null &&  source.getThings().containsKey(superValue.stringValue()) ) {
			thing = source.getThings().get(superValue.stringValue());				
		}else {
			thing = new Thing(source, superValue, evaluationContext);
			if(source!=null)source.getThings().put(superValue.stringValue(), thing);	
		}
		if(evaluationContext!=null)
			thing.evaluationContext=evaluationContext;
		else if(thing.evaluationContext==null) 
			thing.evaluationContext=new EvaluationContext();	
		return thing;
	}
	private Thing(PathQLRepository source, org.eclipse.rdf4j.model.Value superValue, EvaluationContext evaluationContext ) {
		super(superValue,evaluationContext);
		this.setSource(source);
	}	
	public Thing(PathQLRepository source, Value superValue ) {
		super(superValue);
		this.setSource(source);
	}
	public Thing(Graph graph, String iri) {
		super(iri(iri));
		this.setGraph(graph);
	}
	public Thing(Graph graph, IRI iri) {
		super(iri);
		
		this.setGraph(graph);
	}	


	public Graph getGraph() {
		return graph;
	}

	private void setGraph(Graph graph) {
		this.graph=graph;
		this.setSource(graph.getSource());
	}
	
	/**
	 * Generate cache context.
	 *
	 * @param predicate the predicate
	 * @return the iri
	 */
	private final IRI generateCacheContext(IRI predicate) {
		String key;
		if(predicate!=null) {
			 key = getSuperValue().toString() + predicate.stringValue() + StringUtils.join(getCustomQueryOptions());
		}else {
			 key = getSuperValue().toString() + StringUtils.join(getCustomQueryOptions());
		}
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI =	iri(Evaluator.SCRIPT_DATA_GRAPH,cacheContext);
		return cacheContextIRI;
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicateElement the predicate element
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue }
	 */
	public final Resource getFact(PredicateElement predicateElement) { 
		String key = getFactKey(predicateElement);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicateElement.getPredicate()),
				addIRI(getSuperValue()), getCustomQueryOptions()));
		if (notTracing() && getCachedResources().containsKey(key)) {
			Resource result = getCachedResources().get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", predicateElement.toString(), addIRI(getSuperValue()),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicateElement.getPredicate(), key);
		}
	}
	private String getFactKey(PredicateElement predicateElement) {
		return  getFactKey(predicateElement.getPredicate()) ;
	}

	private String getFactKey(IRI predicate) {
		String key = predicate.stringValue() + "."+ StringUtils.join(this.getCustomQueryOptions()).hashCode() + "."+StringUtils.join(getSource().getPublicContexts()).hashCode();
		return key;
	}
	/**
	 * Gets the fact.
	 *
	 * @param predicateElement the predicate element
	 * @param scriptString the script string
	 * @return the fact
	 */
	public final Resource getFact(PredicateElement predicateElement, SimpleLiteral scriptString) {
		Resource fact = this.handleScript(scriptString, predicateElement.getPredicate());
		return fact;
	}
	
	/**
	 * Gets the fact.
	 *
	 * @param predicate the predicate
	 * @param scriptString the script string
	 * @return the fact
	 */
	private final Resource getFact(IRI predicate, SimpleLiteral scriptString) {
		Resource fact = this.handleScript(scriptString, predicate);
		return fact;
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	public final Resource getFact(String predicatePattern) throws PathPatternException {
		logger.debug(new ParameterizedMessage("getFact{}\n",predicatePattern));
		ResourceResults factValues = PathQL.evaluate(this, predicatePattern);
		if(factValues==null) {
			//throw new NullValueReturnedException(NULLVALUERETURNED_EXCEPTION, new ParameterizedMessage("getFact evaluated to null for {} of {}", predicatePattern, getSuperValue()));
			return new NullValue();
		}else if(factValues.hasNext()) {
			return factValues.next();
		}else {
			factValues.close();
			//TODO Is this the best way of handling errors
			//throw new NullValueReturnedException(NULLVALUERETURNED_EXCEPTION, new ParameterizedMessage("getFact evaluated to empty for {} of {}", predicatePattern, getSuperValue()));
			return new NullValue();
		}
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	public final ResourceResults getFacts(String predicatePattern) throws PathPatternException {

		return PathQL.evaluate(this, predicatePattern);
	}

	/**
	 * Gets the signal.
	 *
	 * @param signal the signal
	 * @return the signal
	 */
	public Resource getSignal(String signal) {
		incrementTraceLevel();
		signal=PathQLRepository.trimIRIString(signal); 
		String[] elements = signal.split("/");
		Object result;
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			SEEQSource seeqSource = null;
			try {
				addTrace(new ParameterizedMessage("Fetching SEEQ signal {} with customQueryOptions {}", elements[5],
						getCustomQueryOptions()));
				seeqSource = getSource().seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], getCustomQueryOptions());
				decrementTraceLevel();
				//return getSource().resourceFactory(getTracer(),PathQLRepository.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),customQueryOptions,this.prefixes);
				return Resource.create (getSource(),literal((Double) result), this.getEvaluationContext());
			} catch (ScriptException e) {
				return Resource.create (getSource(), literal("**SEEQ Source Error**"), this.getEvaluationContext());
			} catch (HandledException e) {
				return Resource.create (getSource(),literal( e.getCode()), this.getEvaluationContext());
			}
		case "HTTP:":
			ParameterizedMessage httpMessage = new ParameterizedMessage("HTTP not supported signal source: {}", signal);
			logger.error(httpMessage.getFormattedMessage());
			addTrace(httpMessage);
			return Resource.create (getSource(),literal("**HTTP Source Error**"), this.getEvaluationContext());
		default:
			ParameterizedMessage defaultMessage = new ParameterizedMessage("Unsupported signal source: {}", signal);
			logger.error(defaultMessage.getFormattedMessage());
			addTrace(defaultMessage);
			return Resource.create (getSource(), literal("**Unsupported Source Error**"), this.getEvaluationContext());

		}
	}
	
//	/**
//	 * Gets the thing.
//	 *
//	 * @param subjectIRI the subject IRI
//	 * @return the thing
//	 */
//	public final pathCalc.Thing getThing(String subjectIRI) {
//		return getSource().thingFactory(getTracer(),convertQName( subjectIRI), this.getStack(),this.getCustomQueryOptions(),this.prefixes);
//	}

	protected final Resource handleScript(SimpleLiteral scriptString, IRI predicate) {
		if (predicate.equals(iri(Evaluator.SCRIPTPROPERTY))) {
			return  Resource.create(getSource(),scriptString, this.getEvaluationContext());
		}else {
			String scriptCode = scriptString.getLabel();
			scriptCode = scriptCode.trim();
			if (scriptCode.startsWith("<")) {
				String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
				org.eclipse.rdf4j.model.Resource scriptResource =convertQName( scriptIRI);
				IRI scriptPropertyIRI = iri(Evaluator.SCRIPTPROPERTY);
				Statement scriptStatement;
				SimpleLiteral scriptCodeliteral = null;
				try {
					CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = getSource()
							.getTripleSource().getStatements(scriptResource, scriptPropertyIRI, null);
	
					while (scriptStatements.hasNext()) {
						scriptStatement = scriptStatements.next();
						scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
					}
				} catch (QueryEvaluationException qe) {
					throw new ScriptFailedException(SCRIPTNOTFOUND_EXCEPTION,new ParameterizedMessage("Reference script <{}> not found for  {} of  subject {}", scriptIRI, predicate,  this),qe);
				}
				if (scriptCodeliteral != null) {
					return handleScript(scriptCodeliteral, predicate);
				} else {
					throw new ScriptFailedException(NULLVALUESCRIPT_EXCEPTION,new ParameterizedMessage("Reference script null <{}> for {} of subject {}", scriptIRI, predicate, this ));
				}
			} else {
				incrementTraceLevel();
				IRI cacheContextIRI = generateCacheContext(predicate);
				String scripttype = scriptString.getDatatype().getLocalName();
				addTrace(new ParameterizedMessage("Evaluating predicate {} of {}, by invoking <b>{}</b> script\n",
						addIRI(predicate), addIRI(getSuperValue()), scripttype));
	
				addScript(scriptString.getLabel());
				incrementTraceLevel();
				Object scriptResult = null;
				try {
					//Test to see if the same 'call' is on the stack
					//If so report that circular reference encountered
					//If not push on stack
					String stackKey = generateStackKey(predicate);
					if (!searchStack(stackKey)) {
						pushStack(stackKey);
						CompiledScript compiledScriptCode = getSource().compiledScriptFactory(scriptString);
						ScriptContext scriptContext = new SimpleScriptContext();
						scriptContext.setAttribute("$tripleSource", getSource().getTripleSource(), ScriptContext.ENGINE_SCOPE);
						scriptContext.setAttribute("$this", this, ScriptContext.ENGINE_SCOPE);
						scriptContext.setAttribute("$property",
								Thing.create(getSource(), predicate, this.getEvaluationContext()), ScriptContext.ENGINE_SCOPE);
						scriptContext.setAttribute("$customQueryOptions", getCustomQueryOptions(), ScriptContext.ENGINE_SCOPE);
						scriptContext.setAttribute("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI),
								ScriptContext.ENGINE_SCOPE);
						scriptResult = compiledScriptCode.eval(scriptContext);
						Resource result = returnResult(scriptResult, cacheContextIRI);
						//Since script complete, pop from stack
						popStack();
						decrementTraceLevel();
						if(result!=null )
							addTrace(new ParameterizedMessage("Evaluated {} of {} =  {}", addIRI(predicate), addIRI(getSuperValue()),
								result.getHTMLValue()));
						else {
							throw new NullValueReturnedException(NULLVALUERETURNED_EXCEPTION, new ParameterizedMessage("Evaluated null for {} of {}, using script {}", predicate, getSuperValue(),scriptString));
     					//		addTrace(new ParameterizedMessage("Evaluated null {} of {}", addIRI(predicate), addIRI(getSuperValue())));
						}
						decrementTraceLevel();
						return result;
					} else {
						ParameterizedMessage circularReferenceMessage = new ParameterizedMessage(
								"Circular reference encountered when evaluating {} of {}:", addIRI(predicate),
								addIRI(getSuperValue()));
						addTrace(circularReferenceMessage);
						addScript(getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
								.toString());
						logger.error(new ParameterizedMessage(
								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(),
								getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
										.toString()));
						throw new CircularReferenceException(CIRCULARREFERENCE_EXCEPTION, new ParameterizedMessage(
								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(),
								getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())));

					//	return Resource.create(getSource(), literal("**Circular Reference**"), this.getEvaluationContext());
					}
				} 
				catch (ScriptException e) {
					decrementTraceLevel();
					ParameterizedMessage scriptFailedMesssage = new ParameterizedMessage(
							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">{}</span></code >",
							StringEscapeUtils.escapeHtml4(e.getMessage()));
					logger.error(scriptFailedMesssage.getFormattedMessage());
					addTrace(scriptFailedMesssage);
					//throw e;
					throw new ScriptFailedException(SCRIPTFAILED_EXCEPTION,e);
					//return Resource.create(getSource(),literal(scriptFailedMesssage.getFormattedMessage()), this.getEvaluationContext());
				}
			}
		}

	}
	
	/**
	 * Generate stack key.
	 *
	 * @param predicate the predicate
	 * @return the string
	 */
	private String generateStackKey(IRI predicate) {
		String stackKey ;
//		if(predicate!=null) {
//			 stackKey = "<" + predicate.stringValue() + "> <" + this.toString() + ">; queryOptions="
//					+ (this.getCustomQueryOptions() == null ? "" : this.getCustomQueryOptions().toString()) + "\r\n";
//		}else {
//			 stackKey = "<NULL> <" + this.toString() + ">; queryOptions="
//					+ (this.getCustomQueryOptions() == null ? "" : this.getCustomQueryOptions().toString()) + "\r\n";
//		}
		//TODO Debug only
		if(predicate!=null) {
		 stackKey = "<" + predicate.stringValue() + ">";
	}else {
		 stackKey = "<NULL>" ;
	}
		return stackKey;
	}

	/**
	 * Process fact object value.
	 *
	 * @param predicate the predicate
	 * @param key the key
	 * @param objectValue the object value
	 * @return Processes the objectValue
	 */
	public Resource processFactObjectValue(IRI predicate,  Value objectValue) {
		Resource returnResult = null;
		SimpleLiteral literalValue;
		try {
			literalValue = (SimpleLiteral) objectValue;
			if (Evaluator.isScriptEngine(literalValue.getDatatype())) {
				String key= getFactKey(predicate );
				if (notTracing() && getCachedResources().containsKey(key)) {
					Resource result = getCachedResources().get(key);
					logger.debug(new ParameterizedMessage("Retrieved cache {} of {} = {}", predicate.stringValue(), addIRI(getSuperValue()),
							getHTMLValue(result.getValue())));
					addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", predicate.stringValue(), addIRI(getSuperValue()),
							getHTMLValue(result.getValue())));
					returnResult = result;
				}else {
					Resource result = getFact(predicate, literalValue);
					if (result != null) {
						
						getCachedResources().put(key, result);
						addTrace(new ParameterizedMessage("Calculated {} of {} = {}", addIRI(predicate),
								addIRI(getSuperValue()), result.getHTMLValue()));
					}
					returnResult = result;
				}
			} else {
				addTrace(new ParameterizedMessage("Retrieved literal {} of {} = {}", addIRI(predicate),
						addIRI(getSuperValue()), getHTMLValue(objectValue)));
				returnResult = Resource.create(getSource(), objectValue,  this.getEvaluationContext());
			}
		} catch (Exception e) {
			throw e;
		//	addTrace(new ParameterizedMessage("Retrieved resource {} of {} = {}", addIRI(predicate),
		//			addIRI(getSuperValue()), getHTMLValue(objectValue)));
		//	returnResult = Resource.create(getSource(), objectValue,  this.getEvaluationContext());
		}
		finally {
			
		}
		//return returnResult;
		return returnResult;
	}

	/**
	 * Retrieve fact.
	 *
	 * @param predicate the predicate
	 * @param key the key
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue } unless key can be found in cache
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Deprecated
	private Resource retrieveFact(IRI predicate,  String key)
			throws QueryEvaluationException {
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = getSource()
				.getTripleSource().getStatements((IRI) getSuperValue(), predicate, null);
		Resource returnResult = null;
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			Value objectValue = objectStatement.getObject();		
			returnResult = processFactObjectValue(predicate, objectValue);
		}
		if (returnResult != null)
			return returnResult;
		addTrace(new ParameterizedMessage("Error: No predicate {} found for subject {}", addIRI(predicate),
				addThisIRI()));
		// It could be there are reified attributes associated with scripts or signals
		Resource reifiedValue = getReifiedValue(getSource().createIRI(Evaluator.RDF_STATEMENT),predicate);
		if (reifiedValue != null) {
			return reifiedValue;//source.valueFactory(getTracer(), reifiedValue, getStack(),this.customQueryOptions,this.prefixes);
		}
		decrementTraceLevel();
		return new pathQLModel.Literal(null);
	}
	
	/**
	 * Gets the reified value.
	 *
	 * @param createIRI the create IRI
	 * @param predicate the predicate
	 * @return the reified value
	 */
	@Deprecated
	private Resource getReifiedValue(IRI createIRI, IRI predicate) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Return result.
	 *
	 * @param result the result
	 * @param cacheContextIRI the cache context IRI
	 * @return the olgap. value
	 */
	public Resource returnResult(Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "NullValue":
				return Resource.create(getSource(),	literal((String) "null"), this.getEvaluationContext());
			case "Integer":
				return Resource.create(getSource(),	literal((Integer) result), this.getEvaluationContext());
			case "Double":
				return Resource.create(getSource(),literal((Double) result), this.getEvaluationContext());
			case "Float":
				return Resource.create(getSource(),literal((Float) result), this.getEvaluationContext());
			case "decimal":
				return Resource.create(getSource(),literal((BigDecimal) result), this.getEvaluationContext());
			case "BigDecimal":
				return Resource.create(getSource(),literal((BigDecimal) result), this.getEvaluationContext());
			case "BigInteger":
				return Resource.create(getSource(),literal((BigInteger) result), this.getEvaluationContext());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				getSource().writeModelToCache(this, result, cacheContextIRI);
				return Thing.create(getSource(),cacheContextIRI,  this.getEvaluationContext());
			case "Literal":
				Value content = ((pathQLModel.Literal)result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal)content).getDatatype().getLocalName()) {			
				case "integer":
					return Resource.create(getSource(),
							literal((BigInteger)((pathQLModel.Literal)result).bigIntegerValue() ), this.getEvaluationContext());
				case "decimal":
					return Resource.create(getSource(),
							literal(((pathQLModel.Literal)result).decimalValue() ), this.getEvaluationContext());
				case "double":
					return Resource.create(getSource(),
							literal(((pathQLModel.Literal)result).doubleValue() ), this.getEvaluationContext());
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),((org.eclipse.rdf4j.model.Literal)content).getDatatype().getLocalName());
					return Resource.create(getSource(), literal("**Handler Error**"), this.getEvaluationContext());
				}
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return Resource.create(getSource(), literal("**Handler Error**"), this.getEvaluationContext());
			}
		} else {
			return null;
		}
	}

	/**
	 * Sets the cached values.
	 *
	 * @param cachedValues the cached values
	 */
	public void setCachedValues(HashMap<String, Resource> cachedValues) {
		this.setCachedResources(cachedValues);
	}


	/**
	 * Prefix.
	 *
	 * @param prefix the prefix
	 * @param IRI the iri
	 * @return the thing
	 */
	public Thing prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = PathQLRepository.trimAndCheckIRIString(IRI);
		if(iri!=null )	{	
			this.getEvaluationContext().getPrefixes().put(prefix,iri);
			return this;
		}else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);	
			return null;
		}
	}

	/**
	 * Prefix.
	 *
	 * @param IRI the iri
	 * @return the thing
	 */
	public Thing prefix(String IRI) {
		return this.prefix("",IRI);
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param path the path
	 * @return the facts
	 */
	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	@Override
	public Resource getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	@Override
	public Resource getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the snippet.
	 *
	 * @return the snippet
	 */
	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String traceFact(String predicatePattern) throws PathPatternException{
		if(this.getEvaluationContext()==null) {
			this.evaluationContext=new EvaluationContext();
		}
		this.getEvaluationContext().setTracing(true);
		ResourceResults factValues = PathQL.evaluate(this, predicatePattern);	
		if(factValues.hasNext()) {
			factValues.next();
		}
		return this.getEvaluationContext().getTrace();
	}
	public Thing addFact(String property, String value, IRI dataType) {

		try {
			Literal literal = literal(value,dataType);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			
			addFact(literal, predicateElement);

		} catch (Exception e) {

		}
		return this;
		
	}

	public Thing addFact(String property, String value) {

		try {
			Literal literal = literal(value);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(),property);		
			addFact(literal, predicateElement);
		} catch (Exception e) {

		}
		return this;
		
	}
	private void addFact(Literal literal, PredicateElement predicateElement) throws RepositoryException {
		RepositoryConnection connection = this.getSource().getContextAwareConnection();
		if(predicateElement.getIsReified()) {
			//TODO
			logger.error("Reified statement insert not yet supported:" + predicateElement.toString());
			//IRI propertyIri = PathParser.parsePredicate(property).getIri();
			//connection.add(this.getIRI(), propertyIri,literal, this.getGraph().getGraphName());
		}else {
			IRI propertyIri = predicateElement.getPredicate();
			connection.add(this.getIRI(), propertyIri,literal, this.getGraph().getGraphName());
		}
		//Since changed the database, we need to xclear any cache values.
		getSource().clearCache();
	}

	public IRI getIRI() {
		return (IRI)getSuperValue();
	}

	public Dataset getDataset() {
		//The graphs can be defined in three ways: as the dataset of a tuplequery, as contexts in getStatements query, or as explicitly defined graphs in a PathCalc query
		SimpleDataset dataset = (SimpleDataset) getEvaluationContext().getDataset();
		if(dataset!=null)
				return dataset;
		else {
			HashSet<IRI> publicContexts = getSource().getPublicContexts();
			if (publicContexts==null || publicContexts.isEmpty()) {
				org.eclipse.rdf4j.model.Resource[] contexts = getEvaluationContext().getContexts();
				if(contexts==null ||contexts.length==0 ) {
					return null;
				}else {
					dataset = new SimpleDataset();
					for( org.eclipse.rdf4j.model.Resource resource: contexts) {
						dataset.addDefaultGraph((IRI)resource);
					}
					getEvaluationContext().setDataset(dataset);
					return dataset;	
				}
			}else {
				dataset = new SimpleDataset();
				for( IRI graph: publicContexts) {
					dataset.addDefaultGraph(graph);
				}
				getEvaluationContext().setDataset(dataset);
				return dataset;
			}		
		}
	}
}
