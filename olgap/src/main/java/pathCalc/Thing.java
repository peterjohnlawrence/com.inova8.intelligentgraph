/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.HashMap;
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
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import olgap.Evaluator;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQL.PathQL;
import pathQLModel.NullValue;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLRepository.SEEQSource;
import pathQLResults.ResourceResults;

public class Thing extends Resource {

	protected final Logger logger = LogManager.getLogger(Thing.class);
	public Thing(PathQLRepository source, String iri, HashMap<String, Resource> customQueryOptions ) {
		super(iri(iri));
		this.setSource(source);
		this.customQueryOptions = customQueryOptions;
	}
	public Thing(PathQLRepository source, org.eclipse.rdf4j.model.Value superValue, HashMap<String, Resource> customQueryOptions ) {
		super(superValue);
		this.setSource(source);
		this.customQueryOptions = customQueryOptions;
	}
	public Thing(PathQLRepository source, Value superValue, HashMap<String, Resource> customQueryOptions, HashMap<String,IRI> prefixes ) {
		super(superValue);
		this.setSource(source);
		this.customQueryOptions = customQueryOptions;
		mergePrexfixes(prefixes);
	}
	private void mergePrexfixes(HashMap<String, IRI> prefixes) {
		if(prefixes!=null) {
			prefixes.forEach(
				    (key, value) -> this.prefixes.merge( key, value, (v1, v2) -> ( v1.stringValue()).equalsIgnoreCase(v2.stringValue()) ? v1 : v2)
				);
		}
	}
	private final IRI generateCacheContext(IRI predicate) {
		String key;
		if(predicate!=null) {
			 key = getSuperValue().toString() + predicate.stringValue() + StringUtils.join(customQueryOptions);
		}else {
			 key = getSuperValue().toString() + StringUtils.join(customQueryOptions);
		}
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI =	iri(Evaluator.SCRIPT_DATA_GRAPH,cacheContext);
		return cacheContextIRI;
	}

	/**
	 * @param predicate
	 * @param customQueryOptions
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue }
	 */
	public final Resource getFact(PredicateElement predicateElement) { 
		String key = predicateElement.toString() + StringUtils.join(this.customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicateElement.getPredicate()),
				addIRI(getSuperValue()), customQueryOptions));
		if (notTracing() && getCachedResources().containsKey(key)) {
			Resource result = getCachedResources().get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", predicateElement.toString(), addIRI(getSuperValue()),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicateElement.getPredicate(), key);
		}
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicate the predicate
	 * @param scriptString the script string
	 * @return the fact
	 */
	public final Resource getFact(PredicateElement predicateElement, SimpleLiteral scriptString) {
		Resource fact = this.handleScript(scriptString, (IRI) predicateElement.getTargetPredicate().getValue());
		return fact;
	}
	private final Resource getFact(IRI predicate, SimpleLiteral scriptString) {
		Resource fact = this.handleScript(scriptString, predicate);
		return fact;
	}

	public final Resource getFact(String predicatePattern) throws PathPatternException {
		logger.debug(new ParameterizedMessage("getFact{}\n",predicatePattern));
		ResourceResults factValues = PathQL.evaluate(this, predicatePattern);
		if(factValues==null) {
			return new NullValue();
		}else if(factValues.hasNext()) {
			return factValues.next();
		}else {
			factValues.close();
			return new NullValue();
		}
	}
	public final ResourceResults getFacts(String predicatePattern) throws PathPatternException {

		return PathQL.evaluate(this, predicatePattern);
	}

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
						customQueryOptions));
				seeqSource = getSource().seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], customQueryOptions);
				decrementTraceLevel();
				//return getSource().resourceFactory(getTracer(),PathQLRepository.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),customQueryOptions,this.prefixes);
				return getSource().resourceFactory(getTracer(),	literal((Double) result), getStack(),customQueryOptions,this.prefixes);
			} catch (ScriptException e) {
				return getSource().resourceFactory(getTracer(), "**SEEQ Source Error**", getStack(),customQueryOptions,this.prefixes);
			} catch (HandledException e) {
				return getSource().resourceFactory(getTracer(), e.getCode(), getStack(),customQueryOptions,this.prefixes);
			}
		case "HTTP:":
			ParameterizedMessage httpMessage = new ParameterizedMessage("HTTP not supported signal source: {}", signal);
			logger.error(httpMessage.getFormattedMessage());
			addTrace(httpMessage);
			return getSource().resourceFactory(getTracer(), "**HTTP Source Error**", getStack(),customQueryOptions,this.prefixes);
		default:
			ParameterizedMessage defaultMessage = new ParameterizedMessage("Unsupported signal source: {}", signal);
			logger.error(defaultMessage.getFormattedMessage());
			addTrace(defaultMessage);
			return getSource().resourceFactory(getTracer(), "**Unsupported Source Error**", getStack(),customQueryOptions,this.prefixes);

		}
	}
	public final pathCalc.Thing getThing(String subjectIRI) {
		return getSource().thingFactory(getTracer(),convertQName( subjectIRI), this.getStack(),this.customQueryOptions,this.prefixes);
	}

	protected final Resource handleScript(SimpleLiteral scriptString, IRI predicate) {
		String scriptCode = scriptString.getLabel();
		scriptCode = scriptCode.trim();
		if (scriptCode.startsWith("<")) {
			String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
			org.eclipse.rdf4j.model.Resource scriptResource =convertQName( scriptIRI);
			IRI scriptPropertyIRI = iri(Evaluator.SCRIPTPROPERTY);
			Statement scriptStatement;
			SimpleLiteral scriptCodeliteral = null;
			try {
				CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = PathQLRepository
						.getTripleSource().getStatements(scriptResource, scriptPropertyIRI, null);

				while (scriptStatements.hasNext()) {
					scriptStatement = scriptStatements.next();
					scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
				}
			} catch (QueryEvaluationException qe) {
				logger.error("Script query fails");
			}
			if (scriptCodeliteral != null) {
				return handleScript(scriptCodeliteral, predicate);
			} else {
				logger.error(
						new ParameterizedMessage("Reference script <{}> not found for subject {}", scriptIRI, this));
				return getSource().resourceFactory(getTracer(), "**Script Reference Error**", getStack(),customQueryOptions,this.prefixes);
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
				if (getStack().search(stackKey) < 1) {
					getStack().push(stackKey);
					CompiledScript compiledScriptCode = getSource().compiledScriptFactory(scriptString);
					ScriptContext scriptContext = new SimpleScriptContext();
					scriptContext.setAttribute("$tripleSource", PathQLRepository.getTripleSource(), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$this", this, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$property",
							getSource().thingFactory(getTracer(), predicate, this.getStack(),customQueryOptions), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$customQueryOptions", customQueryOptions, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI),
							ScriptContext.ENGINE_SCOPE);
					scriptResult = compiledScriptCode.eval(scriptContext);
					Resource result = returnResult(scriptResult, cacheContextIRI);
					//Since script complete, pop from stack
					getStack().pop();
					decrementTraceLevel();
					if(result!=null )
						addTrace(new ParameterizedMessage("Evaluated {} of {} =  {}", addIRI(predicate), addIRI(getSuperValue()),
							result.getHTMLValue()));
					else
						addTrace(new ParameterizedMessage("Evaluated null {} of {}", addIRI(predicate), addIRI(getSuperValue())));
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
					return getSource().resourceFactory(getTracer(), "**Circular Reference**", getStack(),customQueryOptions,this.prefixes);
				}
			} catch (ScriptException e) {
				decrementTraceLevel();
				ParameterizedMessage scriptFailedMesssage = new ParameterizedMessage(
						"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">{}</span></code >",
						StringEscapeUtils.escapeHtml4(e.getMessage()));
				logger.error(scriptFailedMesssage.getFormattedMessage());
				addTrace(scriptFailedMesssage);
				return getSource().resourceFactory(getTracer(), scriptFailedMesssage.getFormattedMessage(), getStack(),customQueryOptions,this.prefixes);
			}
		}
	}
	private String generateStackKey(IRI predicate) {
		String stackKey ;
		if(predicate!=null) {
			 stackKey = "<" + predicate.stringValue() + "> <" + this.toString() + ">; queryOptions="
					+ (customQueryOptions == null ? "" : customQueryOptions.toString()) + "\r\n";
		}else {
			 stackKey = "<NULL> <" + this.toString() + ">; queryOptions="
					+ (customQueryOptions == null ? "" : customQueryOptions.toString()) + "\r\n";
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
	public Resource processFactObjectValue(IRI predicate, String key, Value objectValue) {
		Resource returnResult;
		SimpleLiteral literalValue;
		try {
			literalValue = (SimpleLiteral) objectValue;
			if (Evaluator.isScriptEngine(literalValue.getDatatype().getLocalName())) {

				Resource result = getFact(predicate, literalValue);
				if (result != null) {

					getCachedResources().put(key, result);
					addTrace(new ParameterizedMessage("Calculated {} of {} = {}", addIRI(predicate),
							addIRI(getSuperValue()), result.getHTMLValue()));
				}
				returnResult = result;
			} else {
				addTrace(new ParameterizedMessage("Retrieved literal {} of {} = {}", addIRI(predicate),
						addIRI(getSuperValue()), getHTMLValue(objectValue)));
				returnResult = getSource().resourceFactory(getTracer(), objectValue, getStack(),customQueryOptions,this.prefixes);
			}
		} catch (Exception e) {
			addTrace(new ParameterizedMessage("Retrieved resource {} of {} = {}", addIRI(predicate),
					addIRI(getSuperValue()), getHTMLValue(objectValue)));
			returnResult = getSource().resourceFactory(getTracer(), objectValue, getStack(),customQueryOptions,this.prefixes);
		}
		return returnResult;
	}

	/**
	 * @param predicate
	 * @param key
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue } unless key can be found in cache
	 * @throws QueryEvaluationException
	 */
	@Deprecated
	private Resource retrieveFact(IRI predicate,  String key)
			throws QueryEvaluationException {
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = PathQLRepository
				.getTripleSource().getStatements((IRI) getSuperValue(), predicate, null);
		Resource returnResult = null;
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			Value objectValue = objectStatement.getObject();		
			returnResult = processFactObjectValue(predicate,  key, objectValue);
		}
		if (returnResult != null)
			return returnResult;
		addTrace(new ParameterizedMessage("Error: No predicate {} found for subject {}", addIRI(predicate),
				addThisIRI()));
		// It could be there are reified attributes associated with scripts or signals
		Resource reifiedValue = getReifiedValue(PathQLRepository.createIRI(Evaluator.RDF_STATEMENT),predicate);
		if (reifiedValue != null) {
			return reifiedValue;//source.valueFactory(getTracer(), reifiedValue, getStack(),this.customQueryOptions,this.prefixes);
		}
		decrementTraceLevel();
		return getSource().resourceFactory(getTracer(),getStack());
	}
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
				return getSource().resourceFactory(getTracer(),
						literal((String) "null"), getStack(),this.customQueryOptions,this.prefixes);
			case "Integer":
				return getSource().resourceFactory(getTracer(),
						literal((Integer) result), getStack(),this.customQueryOptions,this.prefixes);
			case "Double":
				return getSource().resourceFactory(getTracer(),
						literal((Double) result), getStack(),this.customQueryOptions,this.prefixes);
			case "Float":
				return getSource().resourceFactory(getTracer(),
						literal((Float) result), getStack(),this.customQueryOptions,this.prefixes);
			case "decimal":
				return getSource().resourceFactory(getTracer(),
						literal((BigDecimal) result), getStack(),this.customQueryOptions,this.prefixes);
			case "BigDecimal":
				return getSource().resourceFactory(getTracer(),
						literal((BigDecimal) result), getStack(),this.customQueryOptions,this.prefixes);
			case "BigInteger":
				return getSource().resourceFactory(getTracer(),
						literal((BigInteger) result), getStack(),this.customQueryOptions,this.prefixes);
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				getSource().writeModelToCache(this, result, cacheContextIRI);
				return getSource().thingFactory(getTracer(), cacheContextIRI, getStack(),this.customQueryOptions,this.prefixes);
			case "Literal":
				Value content = ((pathQLModel.Literal)result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal)content).getDatatype().getLocalName()) {			
				case "integer":
					return getSource().resourceFactory(getTracer(),
							literal((BigInteger)((pathQLModel.Literal)result).bigIntegerValue() ), getStack(),this.customQueryOptions,this.prefixes);
				case "decimal":
					return getSource().resourceFactory(getTracer(),
							literal(((pathQLModel.Literal)result).decimalValue() ), getStack(),this.customQueryOptions,this.prefixes);
				case "double":
					return getSource().resourceFactory(getTracer(),
							literal(((pathQLModel.Literal)result).doubleValue() ), getStack(),this.customQueryOptions,this.prefixes);
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),((org.eclipse.rdf4j.model.Literal)content).getDatatype().getLocalName());
					return getSource().resourceFactory(getTracer(), "**Handler Error**", getStack(),this.customQueryOptions,this.prefixes);
				}
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return getSource().resourceFactory(getTracer(), "**Handler Error**", getStack(),this.customQueryOptions,this.prefixes);
			}
		} else {
			return null;
		}
	}

	public void setCachedValues(HashMap<String, Resource> cachedValues) {
		this.setCachedResources(cachedValues);
	}


	public Thing prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = PathQLRepository.trimAndCheckIRIString(IRI);
		if(iri!=null )	{	
			getPrefixes().put(prefix,iri);	
			return this;
		}else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);	
			return null;
		}
	}

	public Thing prefix(String IRI) {
		return this.prefix("",IRI);
	}
	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource getSubject() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public URI getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
