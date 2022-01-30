/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.evaluator.Evaluator;
import com.inova8.intelligentgraph.evaluator.Trace;
import com.inova8.intelligentgraph.exceptions.NullValueReturnedException;
import com.inova8.intelligentgraph.exceptions.ScriptFailedException;
import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.NullPath;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.results.PathResults;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.results.ResourceStatementResults;
import com.inova8.intelligentgraph.seeq.SEEQSource;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.utilities.Utilities;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;

/**
 * The Class Thing.
 */
public class Thing extends Resource {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(Thing.class);
	
	/** The cached resources. */
	private HashMap<String, Resource> cachedResources;
	
	/** The graph name. */
	private  IRI graphName;
	
	/**
	 * Instantiates a new thing.
	 *
	 * @param superValue the super value
	 */
	public Thing(org.eclipse.rdf4j.model.Value superValue) {
		super(superValue);
	}
	
	/**
	 * Creates the.
	 *
	 * @param source the source
	 * @param superValue the super value
	 * @param evaluationContext the evaluation context
	 * @return the thing
	 */
	public static Thing create(IntelligentGraphRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		return Thing.create( source,  null, superValue,	 evaluationContext);
	}
	
	/**
	 * Creates the.
	 *
	 * @param source the source
	 * @param graphIri the graph iri
	 * @param superValue the super value
	 * @param evaluationContext the evaluation context
	 * @return the thing
	 */
	@SuppressWarnings("deprecation")
	public static Thing create(IntelligentGraphRepository source, IRI graphIri, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		Thing thing;

		String graphThingKey = superValue.stringValue();//graphIri.stringValue()+"~"+ superValue.stringValue();
		if (superValue != null && source != null && source.getThings().containsKey(graphThingKey)) {
			thing = source.getThings().get(graphThingKey);
			thing.setSource(source);
			if(evaluationContext!=null) {
				//if(thing.evaluationContext.getPrefixes()==null || thing.evaluationContext.getPrefixes().isEmpty())thing.evaluationContext.setPrefixes(evaluationContext.getPrefixes());
				if(evaluationContext.getCustomQueryOptions()!=null && !evaluationContext.getCustomQueryOptions().isEmpty())thing.evaluationContext.setCustomQueryOptions(evaluationContext.getCustomQueryOptions());
				if(evaluationContext.getTracer()!=null && evaluationContext.getTracer().isTracing())thing.evaluationContext.setTracer(evaluationContext.getTracer());
				if(evaluationContext.getDataset()!=null )thing.evaluationContext.setDataset(evaluationContext.getDataset());
				thing.evaluationContext.setContexts(evaluationContext.getContexts());
			}
			//Overwrite the graphName if not null
			if(graphIri!=null)thing.graphName= graphIri;
			return thing;
		} else {
			thing = new Thing(source, superValue, evaluationContext);
			if (source != null)
				source.getThings().put(graphThingKey, thing);
			if(graphIri==null)graphIri= Graph.DEFAULTGRAPH;
			thing.graphName = graphIri;
		}
		if (evaluationContext != null)
			thing.evaluationContext = evaluationContext;
		else if (thing.evaluationContext == null)
			thing.evaluationContext = new EvaluationContext();
		return thing;
	}
	
	/**
	 * Instantiates a new thing.
	 *
	 * @param source the source
	 * @param superValue the super value
	 * @param evaluationContext the evaluation context
	 */
	private Thing(IntelligentGraphRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		super(superValue, evaluationContext);
		this.setSource(source);
	}
	
	/**
	 * Checks if is iri.
	 *
	 * @return true, if is iri
	 */
	@Override
	public 	boolean isIRI(){
		return true;
	}
	
	/**
	 * Gets the graph name.
	 *
	 * @return the graph name
	 */
	public IRI getGraphName() {
		return graphName;
	}

	/**
	 * Generate cache context.
	 *
	 * @param predicate the predicate
	 * @return the iri
	 */
	@SuppressWarnings("deprecation")
	public  IRI generateCacheContext(IRI predicate) {
		String key;
		if (predicate != null) {
			key = getSuperValue().toString() + predicate.stringValue() + StringUtils.join(getCustomQueryOptions());
		} else {
			key = getSuperValue().toString() + StringUtils.join(getCustomQueryOptions());
		}
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI = iri(SCRIPT.DATA, cacheContext);
		return cacheContextIRI;
	}

	/**
	 * Gets the cached resources.
	 *
	 * @return the cached resources
	 */
	@Deprecated
	public HashMap<String, Resource> getCachedResources() {
		if( cachedResources==null) {
			cachedResources= new HashMap<String, Resource> ();
		}
		return cachedResources;
	}

//	public  Resource getFact(IRI predicate, SimpleLiteral scriptString, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) throws HandledException {
//		Resource fact = processFactObjectValue(predicate,scriptString,  customQueryOptions ,contexts);
//		return fact;
/**
 * Gets the fact.
 *
 * @param predicatePattern the predicate pattern
 * @param customQueryOptions the custom query options
 * @param bindValues the bind values
 * @return the fact
 */
//	}
	public  Resource getFact(String predicatePattern, CustomQueryOptions customQueryOptions, Value...bindValues) {
		logger.debug("getFact{}\n", predicatePattern);

		ResourceResults factValues =  getFacts( predicatePattern,customQueryOptions,bindValues);
		if (factValues == null) {
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s, customQueryOptions %s, and bind variables %s", predicatePattern, this,customQueryOptions,bindValues));
		} else if (factValues.hasNext()) {
			return factValues.next();
		} else {
			factValues.close();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s, customQueryOptions %s, and bind variables %s", predicatePattern, this,customQueryOptions,bindValues));
		}
		
	}
	
	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param customQueryOptions the custom query options
	 * @return the fact
	 */
	public  Resource getFact(String predicatePattern, CustomQueryOptions customQueryOptions) {
		logger.debug("getFact{}\n", predicatePattern);

		ResourceResults factValues =  getFacts( predicatePattern,customQueryOptions);
		if (factValues == null) {
			//return new NullResource();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s and customQueryOptions %s", predicatePattern, this,customQueryOptions));
		} else if (factValues.hasNext()) {
			return factValues.next();
		} else {
			factValues.close();
			//return new NullResource();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s and customQueryOptions %s", predicatePattern, this,customQueryOptions));
		}		
	}
	
	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 */
	public  Resource getFact(String predicatePattern, Value...bindValues )  {
		logger.debug("getFact{}\n", predicatePattern);
		//this.getEvaluationContext().getTracer().traceFact(this, predicatePattern);
		ResourceResults factValues =  getFacts( predicatePattern,bindValues);
		if (factValues == null) {
			this.getEvaluationContext().getTracer().traceFactReturnNull(this, predicatePattern);
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s", predicatePattern, this));
		} else if (factValues.hasNext()) {
			Resource nextFact = factValues.next();
			this.getEvaluationContext().getTracer().traceFactReturnValue(this, predicatePattern,nextFact);
			return nextFact;
		} else {
			this.getEvaluationContext().getTracer().traceFactEmpty(this, predicatePattern);
			factValues.close();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s", predicatePattern, this));
		}	
	}

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 */
	public  ResourceResults getFacts(String predicatePattern, Value... bindValues )  {
		return getFacts(predicatePattern,CustomQueryOptions.create(bindValues));
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param customQueryOptions the custom query options
	 * @param bindValues the bind values
	 * @return the facts
	 */
	public  ResourceResults getFacts(String predicatePattern, CustomQueryOptions customQueryOptions, Value... bindValues ){
		return getFacts(predicatePattern,customQueryOptions.addAll(bindValues));
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param customQueryOptions the custom query options
	 * @return the facts
	 */
	public  ResourceResults getFacts(String predicatePattern, CustomQueryOptions customQueryOptions)  {
		logger.debug("getFacts{}\n", predicatePattern);
		this.getEvaluationContext().getTracer().traceFacts(this, predicatePattern);
		SimpleDataset dataset = getDataset( customQueryOptions);
		dataset.addDefaultGraph(this.graphName);
		org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
		ResourceStatementResults results = null;
		if(this.getSource().getRepository()==null ) {
			CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator = this.getSource()
					.getTripleSource()
					.getStatements(this.getIRI(),
							PATHQL.GETFACTS, literal(predicatePattern), contextArray);
			results = new ResourceStatementResults(localStatementIterator, this, null, customQueryOptions);
		}else {
			CloseableIteration<Statement, RepositoryException> statementIterator = this.getSource()
					.getRepository().getConnection()
					.getStatements(this.getIRI(),
							PATHQL.GETFACTS, literal(predicatePattern), contextArray);
			results = new ResourceStatementResults(statementIterator, this, null, customQueryOptions);
		}
		return results;

	}
	
	/**
	 * Gets the path.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the path
	 */
	public  Path getPath(String predicatePattern) {
		logger.debug("getPath{}\n", predicatePattern);
		PathResults pathValues =  getPaths( predicatePattern,null);
		if (pathValues == null) {
			this.getEvaluationContext().getTracer().tracePathReturnNull(this, predicatePattern);
			return new NullPath();
		} else if (pathValues.hasNext()) {
			Path path = (Path) pathValues.next();
			this.getEvaluationContext().getTracer().tracePathReturn(this, predicatePattern,path);
			return path;
		} else {
			this.getEvaluationContext().getTracer().tracePathEmpty(this, predicatePattern);
			pathValues.close();
			return new NullPath();
		}
	}
	
	/**
	 * Gets the paths.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the paths
	 */
	public  PathResults getPaths(String predicatePattern) {
	 return  getPaths( predicatePattern, null);
	}
	
	/**
	 * Gets the paths.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param customQueryOptions the custom query options
	 * @return the paths
	 */
	public  PathResults getPaths(String predicatePattern, CustomQueryOptions customQueryOptions)  {

		logger.debug("getPaths{}\n", predicatePattern);

		this.getEvaluationContext().getTracer().tracePaths(this, predicatePattern);
		SimpleDataset dataset = getDataset( customQueryOptions);
		dataset.addDefaultGraph(this.graphName);
		org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
		PathResults results = null;
		if(this.getSource().getRepository()==null ) {
			CloseableIteration<? extends Statement, QueryEvaluationException> localPathIterator = this.getSource()
					.getTripleSource()
					.getStatements(this.getIRI(),
							PATHQL.GETPATHS, literal(predicatePattern), contextArray);
			results = new PathResults(localPathIterator, this, null);
		}else {
			CloseableIteration<Statement, RepositoryException> pathIterator = this.getSource()
					.getRepository().getConnection()
					.getStatements(this.getIRI(),
							PATHQL.GETPATHS, literal(predicatePattern), contextArray);
			results = new PathResults(pathIterator, this, null, customQueryOptions);
		}
		return results;
	}

//	private org.eclipse.rdf4j.model.Resource[] prepareDataset(CustomQueryOptions customQueryOptions) {
//		SimpleDataset dataset = getDataset( customQueryOptions);
//		if(dataset!=null) {
//			Set<IRI> contexts = dataset.getDefaultGraphs();
//			org.eclipse.rdf4j.model.Resource[] contextArray=contexts.toArray(new org.eclipse.rdf4j.model.Resource[0]);
//			return contextArray;
//		}else
//			return new  org.eclipse.rdf4j.model.Resource[0];
//	}


	/**
 * Gets the signal.
 *
 * @param signal the signal
 * @return the signal
 */
@SuppressWarnings("deprecation")
	public Resource getSignal(String signal) {		
		return getSignal(signal, getCustomQueryOptions());	
	}
	
	
	/**
	 * Gets the signal.
	 *
	 * @param signal the signal
	 * @param customQueryOptions the custom query options
	 * @return the signal
	 */
	public Resource getSignal(String signal, CustomQueryOptions customQueryOptions) {
		getEvaluationContext().getTracer().incrementLevel();
		//incrementTraceLevel();
		signal = Utilities.trimIRIString(signal);
		String[] elements = signal.split("/");
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			return SEEQSource.getSEEQSignal(this,signal, customQueryOptions);
		case "HTTP:":
			getEvaluationContext().getTracer().traceSEEQHTTPError(signal);
			logger.error(String.format("HTTP not supported signal source: %s", signal));
			//return Resource.create(getSource(), literal("**HTTP Source Error**"), this.getEvaluationContext());
			throw new ScriptFailedException( String.format("HTTP not supported signal source: %s", signal));
		default:
			getEvaluationContext().getTracer().traceSEEQError(signal);
			logger.error(String.format("Unsupported signal source: %s", signal));
			//return Resource.create(getSource(), literal("**Unsupported Source Error**"), this.getEvaluationContext());
			throw new ScriptFailedException( String.format("Unsupported signal source: %s", signal));

		}
	}
//	private Resource getSEEQSignal(String signal, CustomQueryOptions customQueryOptions)
//			throws ScriptFailedException {
//		signal = Utilities.trimIRIString(signal);
//		String[] elements = signal.split("/");
//		Object result;
//		SEEQSource seeqSource = null;
//		try {
//			if(elements.length<6) {
//				getEvaluationContext().getTracer().decrementLevel();
//				String error = String.format("Unsupported signal source: %s", signal);
//				logger.error(error);
//				getEvaluationContext().getTracer().traceSignalError(error);
//				throw new ScriptFailedException( error);
//			}else {
//				getEvaluationContext().getTracer().traceSEEQ(elements[5],
//						customQueryOptions);
//				seeqSource = getSource().seeqSourceFactory(elements[2]);
//				result = seeqSource.getSignal(elements[5], customQueryOptions);
//				getEvaluationContext().getTracer().decrementLevel();
//				return Resource.create(getSource(), literal((Double) result), this.getEvaluationContext());
//			}
//		} catch (ScriptException e) {
//			//return Resource.create(getSource(), literal("**SEEQ Source Error**"), this.getEvaluationContext());
//			throw new ScriptFailedException( e);
//			
//		} catch (HandledException e) {
//			//return Resource.create(getSource(), literal(e.getCode()), this.getEvaluationContext());
//			throw new ScriptFailedException( e);
//		}
//	}

//	protected  Resource handleScript(SimpleLiteral scriptString, IRI predicate, CustomQueryOptions customQueryOptions,org.eclipse.rdf4j.model.Resource ... contexts) throws HandledException{
//		if (predicate.equals(SCRIPT.SCRIPTCODE)) {
//			return Resource.create(getSource(), scriptString, this.getEvaluationContext());
//		} else {
//			String scriptCode = scriptString.getLabel();
//			scriptCode = scriptCode.trim();
//			if (scriptCode.startsWith("<")) {
//				String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
//				org.eclipse.rdf4j.model.Resource scriptResource = getSource().getRepositoryContext().convertQName(scriptIRI, getSource().getPrefixes());//convertQName(scriptIRI);
//				Statement scriptStatement;
//				SimpleLiteral scriptCodeliteral = null;
//				try {
//					CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = getSource()
//							.getTripleSource().getStatements(scriptResource, SCRIPT.SCRIPTCODE, null);
//
//					while (scriptStatements.hasNext()) {
//						scriptStatement = scriptStatements.next();
//						scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
//					}
//				} catch (QueryEvaluationException qe) {
//					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
//					throw new ScriptFailedException( String.format(
//							"Reference script <%s> not found for  %s of  subject %s", scriptIRI, predicate, this), qe);
//				}
//				if (scriptCodeliteral != null) {
//					getEvaluationContext().getTracer().traceRedirecting(this, predicate, scriptCode);
//					return handleScript(scriptCodeliteral, predicate,customQueryOptions, contexts);
//				} else {
//					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
//					throw new ScriptFailedException( String.format(
//							"Reference script null <%s> for %s of subject %s", scriptIRI, predicate, this));
//				}
//			} else {
//				getEvaluationContext().getTracer().incrementLevel();
//				IRI cacheContextIRI = generateCacheContext(predicate);				
//				getEvaluationContext().getTracer().traceEvaluating(this, predicate, scriptString);
//
//				Object scriptResult = null;
//				try {
//					//Test to see if the same 'call' is on the stack
//					//If so report that circular reference encountered
//					//If not push on stack
//					String stackKey = generateStackKey(predicate);
//					if (!searchStack(stackKey)) {
//						
//						CompiledScript compiledScriptCode = getSource().compiledScriptFactory(scriptString);
//						SimpleBindings scriptBindings = new SimpleBindings();
//						Object _result = null;
//						scriptBindings.put("_result", _result);
//						scriptBindings.put("_this", this);
//						scriptBindings.put("_customQueryOptions",customQueryOptions);
//
//						Resource result;
//						try {
//							pushStack(stackKey);
//							scriptResult = compiledScriptCode.eval(scriptBindings);
//							if(scriptResult==null) {
//								//Could be Python which will not return a result by default
//								scriptResult=scriptBindings.get("_result");
//							}
//							result = returnResult(scriptResult, cacheContextIRI);
//						}
//						finally {
//							//Since script complete, pop from stack
//							popStack();
//						}
//
//						getEvaluationContext().getTracer().decrementLevel();
//						if (result != null)
//							getEvaluationContext().getTracer().traceEvaluated(this, predicate, result);
//						else {
//							throw new NullValueReturnedException(
//									String.format("Evaluated null for %s of %s, using script %s", predicate,
//											getSuperValue(), scriptString));
//						}
//						getEvaluationContext().getTracer().decrementLevel();
//						return result;
//					} else {
//						getEvaluationContext().getTracer().traceCircularReference(this, predicate, getStack(), stackKey);
//						logger.error(
//								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
//								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(),
//								getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
//										.toString());
//						 throw new CircularReferenceException(String.format(
//								"Circular reference encountered when evaluating <%s> of <%s>.\r\n%s",
//								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(), getStack()
//										.subList(getStack().size() - getStack().search(stackKey), getStack().size())));
//					}
//				} catch (ScriptException e) {
//					getEvaluationContext().getTracer().decrementLevel();
//					logger.error(String.format(
//							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
//							StringEscapeUtils.escapeHtml4(e.getMessage())));
//					getEvaluationContext().getTracer().traceScriptError(e);
//					throw new ScriptFailedException( e);
//
//				}
//			}
//		}
//
//	}

	/**
 * Generate stack key.
 *
 * @param predicate the predicate
 * @return the string
 */
@SuppressWarnings("deprecation")
	public String generateStackKey(IRI predicate) {
		String stackKey;
		if (predicate != null) {
			stackKey = "<" + predicate.stringValue() + "> <" + this.toString() + ">; queryOptions="
					+ (this.getCustomQueryOptions() == null ? "" : this.getCustomQueryOptions().toString()) + "\r\n";
		} else {
			stackKey = "<NULL> <" + this.toString() + ">; queryOptions="
					+ (this.getCustomQueryOptions() == null ? "" : this.getCustomQueryOptions().toString()) + "\r\n";
		}
		return stackKey;
	}


//	public Resource processFactObjectValue(IRI predicate, Value objectValue, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) throws QueryEvaluationException {
//		Resource returnResult = null;
//		SimpleLiteral literalValue;
//		{
//			literalValue = (SimpleLiteral) objectValue;
//			if (Evaluator.isScriptEngine(literalValue.getDatatype())) {
//				org.eclipse.rdf4j.model.Resource customQueryOptionsContext;
//				if(customQueryOptions!=null)
//					customQueryOptionsContext = customQueryOptions.getContext();
//				else
//					 customQueryOptionsContext = CustomQueryOptions.getEmptyContext();
//				FactCache factCache = getSource().getIntelligentGraphConnection().getIntelligentGraphSail().getFactCache();
//				if (/*notTracing() && */factCache.contains(this.getIRI(), predicate, customQueryOptionsContext)) {
//					Value resultValue =  factCache.getFacts(this.getIRI(), predicate, customQueryOptionsContext);
//					logger.debug("Retrieved cache {} of {} = {}", predicate.stringValue(),
//							getSuperValue().stringValue(), resultValue.stringValue());
//					getEvaluationContext().getTracer().traceRetrievedCache(this,predicate, resultValue);
//					returnResult = Resource.create(getSource(), resultValue, evaluationContext);
//				} else {
//					Resource result = this.handleScript(literalValue, predicate,customQueryOptions,contexts);
//					if (result != null) {
//						//TODO validate caching
//						factCache.addFact(this.getIRI(), predicate,result.getSuperValue() ,customQueryOptionsContext);
//						getEvaluationContext().getTracer().traceCalculated(this,predicate, result);
//					}
//					returnResult = result;
//				}
//			} else {
//				getEvaluationContext().getTracer().traceRetrievedLiteral(this,predicate, objectValue);
//				returnResult = Resource.create(getSource(), objectValue, this.getEvaluationContext());
//			}
//		}
//		return returnResult;
//	}

//	public Resource returnResult(Object result, IRI cacheContextIRI) {
//		if (result != null) {
//			switch (result.getClass().getSimpleName()) {
//			case "NullValue":
//				return Resource.create(getSource(), literal((String) "null"), this.getEvaluationContext());
//			case "Integer":
//				return Resource.create(getSource(), literal((Integer) result), this.getEvaluationContext());
//			case "Double":
//				return Resource.create(getSource(), literal((Double) result), this.getEvaluationContext());
//			case "Float":
//				return Resource.create(getSource(), literal((Float) result), this.getEvaluationContext());
//			case "decimal":
//				return Resource.create(getSource(), literal((BigDecimal) result), this.getEvaluationContext());
//			case "BigDecimal":
//				return Resource.create(getSource(), literal((BigDecimal) result), this.getEvaluationContext());
//			case "BigInteger":
//				return Resource.create(getSource(), literal((BigInteger) result), this.getEvaluationContext());
//			case "Thing":
//				return (Thing) result;
//			case "LinkedHashModel":
//				getSource().writeModelToCache(this, result, cacheContextIRI);
//				return Thing.create(getSource(), cacheContextIRI, this.getEvaluationContext());
//			case "Literal":
//				Value content = ((com.inova8.intelligentgraph.model.Literal) result).getValue();
//				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
//				case "int":
//				case "integer":
//					return Resource.create(getSource(),
//							literal((BigInteger) ((com.inova8.intelligentgraph.model.Literal) result).bigIntegerValue()),
//							this.getEvaluationContext());
//				case "decimal":
//					return Resource.create(getSource(), literal(((com.inova8.intelligentgraph.model.Literal) result).decimalValue()),
//							this.getEvaluationContext());
//				case "double":
//					return Resource.create(getSource(), literal(((com.inova8.intelligentgraph.model.Literal) result).doubleValue()),
//							this.getEvaluationContext());
//				case "string": 
//					return Resource.create(getSource(), literal(content.stringValue()), this.getEvaluationContext());
//				case "time": 
//					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
//				case "date": 
//					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
//				case "dateTime": 
//					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
//				default:
//					logger.error("No literal handler found for result {} of class {}", result.toString(),
//							((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName());
//					return Resource.create(getSource(), literal("**Handler Error**"), this.getEvaluationContext());
//				}
//			default:
//				logger.error("No handler found for result {} of class {}", result.toString(),
//						result.getClass().getSimpleName());
//				return Resource.create(getSource(), literal("**Handler Error**"), this.getEvaluationContext());
//			}
//		} else {
//			return null;
//		}
//	}

	/**
 * Trace fact.
 *
 * @param predicatePattern the predicate pattern
 * @param customQueryOptions the custom query options
 * @return the trace
 */
public Trace traceFact(String predicatePattern, CustomQueryOptions customQueryOptions)  {
		SimpleDataset dataset = getDataset( customQueryOptions);
		dataset.addDefaultGraph(this.graphName);
		org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
		ResourceStatementResults results = null;
		CloseableIteration<Statement, RepositoryException> statementIterator = this.getSource()
					.getRepository().getConnection()
					.getStatements(this.getIRI(),
							PATHQL.TRACEFACTS, literal(predicatePattern), contextArray);
		results = new ResourceStatementResults(statementIterator, this, null, customQueryOptions);
		for(Resource result:results) {
			String resultString = result.stringValue();
			results.close();
			return new Trace(resultString);
		}
		results.close();
		return new Trace("results");

	}

	/**
	 * Trace fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the trace
	 */
	public Trace traceFact(String predicatePattern, Value...  bindValues) {
		return traceFact(predicatePattern, CustomQueryOptions.create(bindValues));
	}

	/**
	 * Delete facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @throws Exception the exception
	 */
	public void deleteFacts(String predicatePattern) throws Exception {		
		this.getSource().getRepository().getConnection().remove(this.getIRI(),
							PATHQL.REMOVEFACTS, literal(predicatePattern), this.getGraphName());
	}



	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the thing
	 */
	public Thing addFact(IRI property, String value, IRI dataType ) {
		Literal literal = literal(value, dataType);
		addFact(property,literal);
		return this;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the thing
	 */
	public Thing addFact(IRI property, Value value) {
		Validate.notNull(property);
		Validate.notNull(value);
		RepositoryConnection connection = this.getSource().getContextAwareConnection();
		switch(value.getClass().getSimpleName() ) {
			case "Thing":
				connection.add(this.getIRI(), property, ((Thing) value).getIRI(), this.getGraphName());
				break;
			default:
				connection.add(this.getIRI(), property, value, this.getGraphName());
		}		
		return this;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param pathql the pathql
	 * @param value the value
	 * @param dataType the data type
	 * @return the thing
	 */
	public Thing addFact(String pathql, String value, IRI dataType) {		
		addFact(pathql,literal(value, dataType));
		return this;
	}

	/**
	 * Adds the fact.
	 *
	 * @param pathql the pathql
	 * @param value the value
	 * @return the thing
	 */
	public Thing addFact(String pathql, Value value) {

		try {

			IRI predicate = iri(
					PATHQL.addFact + "?pathQL=" + URLEncoder.encode(pathql, StandardCharsets.UTF_8.toString()));
			switch (value.getClass().getSimpleName()) {
			case "Thing":

				this.getSource().getRepository().getConnection().add(this.getIRI(), predicate, ((Thing) value).getIRI(),
						this.getGraphName());
				break;
			default:
				this.getSource().getRepository().getConnection().add(this.getIRI(), predicate, value,
						this.getGraphName());
			}
		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return this;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param pathql the pathql
	 * @param value the value
	 * @return the thing
	 */
	public Thing addFact(String pathql, String value) {		
		addFact(pathql,value,XSD.STRING);
		return this;
	}


	/**
	 * Gets the iri.
	 *
	 * @return the iri
	 */
	public IRI getIRI() {
		return (IRI) getSuperValue();
	}

	/**
	 * Gets the dataset.
	 *
	 * @param declaredContexts the declared contexts
	 * @return the dataset
	 */
	@SuppressWarnings("deprecation")
	public SimpleDataset getDataset(org.eclipse.rdf4j.model.Resource... declaredContexts) {
		//The graphs can be defined in three ways: as the dataset of a tuplequery, as contexts in getStatements query, or as explicitly defined graphs in a PathCalc query
		SimpleDataset dataset = (SimpleDataset) getEvaluationContext().getDataset();
		if (dataset != null)
			return dataset;
		else { 
			HashSet<IRI> publicContexts;
			if(this.getSource().getIntelligentGraphConnection()!=null) {
				publicContexts = this.getSource().getIntelligentGraphConnection().getIntelligentGraphSail().getPublicContexts();
			}else {
				publicContexts = getSource().getPublicContexts();
			}
			if (publicContexts == null || publicContexts.isEmpty()) {
				org.eclipse.rdf4j.model.Resource[] contexts = getEvaluationContext().getContexts();
				if (contexts == null || contexts.length == 0) {
					
					return null;
				} else {
					dataset = new SimpleDataset();
					for (org.eclipse.rdf4j.model.Resource resource : contexts) {
						dataset.addDefaultGraph((IRI) resource);
					}
					return dataset;
				}
			} else {
				dataset = new SimpleDataset();
				for (IRI graph : publicContexts) {
					dataset.addDefaultGraph(graph);
				}
				return dataset;
			}
		}
	}
	
	/**
	 * Gets the dataset.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the dataset
	 */
	public SimpleDataset getDataset(CustomQueryOptions customQueryOptions) {
		SimpleDataset dataset = new SimpleDataset();//getDataset();
		if(customQueryOptions!=null) {
			dataset.addDefaultGraph(iri(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS+"?"+customQueryOptions.toURIEncodedString()));
		}
		return dataset;
	}
	
	/**
	 * Gets the thing.
	 *
	 * @param thing the thing
	 * @return the thing
	 */
	public Thing getThing(String thing){
		//IRI thingIri = PathParser.parseIriRef( this.getSource().getRepositoryContext(),thing).getIri();
		IRI thingIri = getSource().getRepositoryContext().convertQName(thing);
		return create(this.getSource(), thingIri,this.getEvaluationContext());
	}




}
