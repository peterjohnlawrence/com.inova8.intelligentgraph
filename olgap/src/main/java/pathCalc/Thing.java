/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import Exceptions.CircularReferenceException;
import Exceptions.HandledException;
import Exceptions.NullValueReturnedException;
import Exceptions.ScriptFailedException;
import intelligentGraph.FactCache;
import intelligentGraph.IntelligentGraphConnection;
import intelligentGraph.IntelligentGraphSail;
import path.NullPath;
import path.Path;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQL.PathQL;
import pathQLModel.NullResource;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLRepository.ReificationType;
import pathQLRepository.SEEQSource;
import pathQLResults.PathResults;
import pathQLResults.ResourceResults;
import pathQLResults.ResourceStatementResults;

public class Thing extends Resource {

	@Deprecated
	private static final String NULLVALUERETURNED_EXCEPTION = "NullValueReturned";
	@Deprecated
	private static final String SCRIPTFAILED_EXCEPTION = "ScriptFailed";
	@Deprecated
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	@Deprecated
	private static final String SCRIPTNOTFOUND_EXCEPTION = null;
	@Deprecated
	private static final String NULLVALUESCRIPT_EXCEPTION = null;
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(Thing.class);
	/** The cached resources. */
	private HashMap<String, Resource> cachedResources;
	private  IRI graphName;
	boolean remoteHostProcessing = true;
	/**
	 * Creates the.
	 *
	 * @param source
	 *            the source
	 * @param superValue
	 *            the super value
	 * @param evaluationContext
	 *            the evaluation context
	 * @return the thing
	 */
	public static Thing create(PathQLRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		return Thing.create( source,  null, superValue,	 evaluationContext);
	}
	public static Thing create(PathQLRepository source, IRI graphIri, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		Thing thing;

		String graphThingKey = superValue.stringValue();//graphIri.stringValue()+"~"+ superValue.stringValue();
		if (superValue != null && source != null && source.getThings().containsKey(graphThingKey)) {
			thing = source.getThings().get(graphThingKey);
			thing.setSource(source);
			if(evaluationContext!=null) {
				if(thing.evaluationContext.getPrefixes()==null || thing.evaluationContext.getPrefixes().isEmpty())thing.evaluationContext.setPrefixes(evaluationContext.getPrefixes());
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
	 * @param source
	 *            the source
	 * @param superValue
	 *            the super value
	 * @param evaluationContext
	 *            the evaluation context
	 */
	private Thing(PathQLRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		super(superValue, evaluationContext);
		this.setSource(source);
	}

	public IRI getGraphName() {
		return graphName;
	}

	public final IRI generateCacheContext(IRI predicate) {
		String key;
		if (predicate != null) {
			key = getSuperValue().toString() + predicate.stringValue() + StringUtils.join(getCustomQueryOptions());
		} else {
			key = getSuperValue().toString() + StringUtils.join(getCustomQueryOptions());
		}
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI = iri(Evaluator.SCRIPT_DATA_GRAPH, cacheContext);
		return cacheContextIRI;
	}

	@Deprecated
	public HashMap<String, Resource> getCachedResources() {
		if( cachedResources==null) {
			cachedResources= new HashMap<String, Resource> ();
		}
		return cachedResources;
	}

	public final Resource getFact(IRI predicate, SimpleLiteral scriptString, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) {
		Resource fact = processFactObjectValue(predicate,scriptString,  customQueryOptions ,contexts);
		return fact;
	}

	public final Resource getFact(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException{
		logger.debug("getFact{}\n", predicatePattern);

		ResourceResults factValues =  getFacts( predicatePattern,customQueryOptions);
		if (factValues == null) {
			return new NullResource();
		} else if (factValues.hasNext()) {
			return factValues.next();
		} else {
			factValues.close();
			return new NullResource();
		}		
	}

	public final Resource getFact(String predicatePattern) throws PathPatternException {
		logger.debug("getFact{}\n", predicatePattern);
		//this.getEvaluationContext().getTracer().traceFact(this, predicatePattern);
		ResourceResults factValues =  getFacts( predicatePattern);
		if (factValues == null) {
			this.getEvaluationContext().getTracer().traceFactReturnNull(this, predicatePattern);
			return new NullResource();
		} else if (factValues.hasNext()) {
			Resource nextFact = factValues.next();
			this.getEvaluationContext().getTracer().traceFactReturnValue(this, predicatePattern,nextFact);
			return nextFact;
		} else {
			this.getEvaluationContext().getTracer().traceFactEmpty(this, predicatePattern);
			factValues.close();
			return new NullResource();
		}
	}

	public final ResourceResults getFacts(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {
		//This could be configured to process locally in a client or remotely in the server. Not sure both are required.
		boolean remoteHostProcessing = true;
		logger.debug("getFacts{}\n", predicatePattern);
		if(remoteHostProcessing) {
			this.getEvaluationContext().getTracer().traceFacts(this, predicatePattern);
			SimpleDataset dataset = getDataset( customQueryOptions);
			dataset.addDefaultGraph(this.graphName);
			org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
			ResourceStatementResults results = null;
			if(this.getSource().getRepository()==null ) {
				CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator = this.getSource()
						.getTripleSource()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern), contextArray);
				results = new ResourceStatementResults(localStatementIterator, this, null, customQueryOptions);
			}else {
				CloseableIteration<Statement, RepositoryException> statementIterator = this.getSource()
						.getRepository().getConnection()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern), contextArray);
				results = new ResourceStatementResults(statementIterator, this, null, customQueryOptions);
			}
			return results;
		}else {
			return PathQL.evaluate(this, predicatePattern,customQueryOptions);
		}
	}
	public final Path getPath(String predicatePattern) throws PathPatternException, RDFParseException, UnsupportedRDFormatException, IOException {
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
	public final PathResults getPaths(String predicatePattern) throws PathPatternException {
	 return  getPaths( predicatePattern, null);
	}
	public final PathResults getPaths(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {
		//This could be configured to process locally in a client or remotely in the server. Not sure both are required.
		boolean remoteHostProcessing = true;
		logger.debug("getPaths{}\n", predicatePattern);
		if(remoteHostProcessing) {
			this.getEvaluationContext().getTracer().tracePaths(this, predicatePattern);
			SimpleDataset dataset = getDataset( customQueryOptions);
			dataset.addDefaultGraph(this.graphName);
			org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
			PathResults results = null;
			if(this.getSource().getRepository()==null ) {
				CloseableIteration<? extends Statement, QueryEvaluationException> localPathIterator = this.getSource()
						.getTripleSource()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.GETPATHS), literal(predicatePattern), contextArray);
				results = new PathResults(localPathIterator, this, null);
			}else {
				CloseableIteration<Statement, RepositoryException> pathIterator = this.getSource()
						.getRepository().getConnection()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.GETPATHS), literal(predicatePattern), contextArray);
				results = new PathResults(pathIterator, this, null, customQueryOptions);
			}
			return results;
		}else {
		//	return PathQL.evaluate(this, predicatePattern,customQueryOptions);
			return null;
		}
		
	}

	private org.eclipse.rdf4j.model.Resource[] prepareDataset(CustomQueryOptions customQueryOptions) {
		SimpleDataset dataset = getDataset( customQueryOptions);
		if(dataset!=null) {
			Set<IRI> contexts = dataset.getDefaultGraphs();
			org.eclipse.rdf4j.model.Resource[] contextArray=contexts.toArray(new org.eclipse.rdf4j.model.Resource[0]);
			return contextArray;
		}else
			return new  org.eclipse.rdf4j.model.Resource[0];
	}
	@Deprecated
	public void supersedeCustomQueryOptions(CustomQueryOptions customQueryOptions) {
		if(customQueryOptions!=null && !customQueryOptions.isEmpty()) {
			customQueryOptions.addInherited(this.getEvaluationContext().getCustomQueryOptions());
			this.getEvaluationContext().setCustomQueryOptions(customQueryOptions);
		}
	}

	public final ResourceResults getFacts(String predicatePattern) throws PathPatternException {
		return getFacts(predicatePattern,null);
	}

	public Resource getSignal(String signal) {
		getEvaluationContext().getTracer().incrementLevel();
		//incrementTraceLevel();
		signal = PathQLRepository.trimIRIString(signal);
		String[] elements = signal.split("/");
		Object result;
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			SEEQSource seeqSource = null;
			try {
				getEvaluationContext().getTracer().traceSEEQ(elements[5],
						getCustomQueryOptions());
				//addTrace(String.format("Fetching SEEQ signal %s with customQueryOptions %s", elements[5],
				//		getCustomQueryOptions()));
				seeqSource = getSource().seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], getCustomQueryOptions());
				//decrementTraceLevel();
				getEvaluationContext().getTracer().decrementLevel();
				//return getSource().resourceFactory(getTracer(),PathQLRepository.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),customQueryOptions,this.prefixes);
				return Resource.create(getSource(), literal((Double) result), this.getEvaluationContext());
			} catch (ScriptException e) {
				return Resource.create(getSource(), literal("**SEEQ Source Error**"), this.getEvaluationContext());
			} catch (HandledException e) {
				return Resource.create(getSource(), literal(e.getCode()), this.getEvaluationContext());
			}
		case "HTTP:":
			getEvaluationContext().getTracer().traceSEEQHTTPError(signal);
			//String httpMessage = String.format("HTTP not supported signal source: %s", signal);
			logger.error(String.format("HTTP not supported signal source: %s", signal));
			//addTrace(httpMessage);
			return Resource.create(getSource(), literal("**HTTP Source Error**"), this.getEvaluationContext());
		default:
			getEvaluationContext().getTracer().traceSEEQError(signal);
			//String defaultMessage = String.format("Unsupported signal source: %s", signal);
			logger.error(String.format("Unsupported signal source: %s", signal));
			//addTrace(defaultMessage);
			return Resource.create(getSource(), literal("**Unsupported Source Error**"), this.getEvaluationContext());

		}
	}

	protected final Resource handleScript(SimpleLiteral scriptString, IRI predicate, CustomQueryOptions customQueryOptions,org.eclipse.rdf4j.model.Resource ... contexts) {
		if (predicate.equals(iri(Evaluator.SCRIPTPROPERTY))) {
			return Resource.create(getSource(), scriptString, this.getEvaluationContext());
		} else {
			String scriptCode = scriptString.getLabel();
			scriptCode = scriptCode.trim();
			if (scriptCode.startsWith("<")) {
				String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
				org.eclipse.rdf4j.model.Resource scriptResource = convertQName(scriptIRI);
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
					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
					throw new ScriptFailedException(SCRIPTNOTFOUND_EXCEPTION, String.format(
							"Reference script <%s> not found for  %s of  subject %s", scriptIRI, predicate, this), qe);
				}
				if (scriptCodeliteral != null) {
					getEvaluationContext().getTracer().traceRedirecting(this, predicate, scriptCode);
					return handleScript(scriptCodeliteral, predicate,customQueryOptions, contexts);
				} else {
					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
					throw new ScriptFailedException(NULLVALUESCRIPT_EXCEPTION, String.format(
							"Reference script null <%s> for %s of subject %s", scriptIRI, predicate, this));
				}
			} else {
				getEvaluationContext().getTracer().incrementLevel();
				IRI cacheContextIRI = generateCacheContext(predicate);				
				getEvaluationContext().getTracer().traceEvaluating(this, predicate, scriptString);

				Object scriptResult = null;
				try {
					//Test to see if the same 'call' is on the stack
					//If so report that circular reference encountered
					//If not push on stack
					String stackKey = generateStackKey(predicate);
					if (!searchStack(stackKey)) {
						
						CompiledScript compiledScriptCode = getSource().compiledScriptFactory(scriptString);
						SimpleBindings scriptBindings = new SimpleBindings();
						Object _result = null;
						scriptBindings.put("_result", _result);
						scriptBindings.put("_this", this);
						scriptBindings.put("$this", this);

						scriptBindings.put("$customQueryOptions",customQueryOptions);
						//	scriptBindings.put("$property",		Thing.create(getSource(), predicate, this.getEvaluationContext()));
					    //	scriptBindings.put("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI));
						//	scriptBindings.put("$tripleSource", getSource().getTripleSource());

						Resource result;
						try {
							pushStack(stackKey);
							scriptResult = compiledScriptCode.eval(scriptBindings);
							if(scriptResult==null) {
								//Could be Python which will not return a result by default
								scriptResult=scriptBindings.get("_result");
							}
							result = returnResult(scriptResult, cacheContextIRI);
						}
						finally {
							//Since script complete, pop from stack
							popStack();
						}
						//decrementTraceLevel();
						getEvaluationContext().getTracer().decrementLevel();
						if (result != null)
							getEvaluationContext().getTracer().traceEvaluated(this, predicate, result);
						else {
							throw new NullValueReturnedException(NULLVALUERETURNED_EXCEPTION,
									String.format("Evaluated null for %s of %s, using script %s", predicate,
											getSuperValue(), scriptString));
						}
						getEvaluationContext().getTracer().decrementLevel();
						return result;
					} else {
						getEvaluationContext().getTracer().traceCircularReference(this, predicate, getStack(), stackKey);
						logger.error(
								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(),
								getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
										.toString());
//						if(getEvaluationContext().getTracer().isTracing()) {
//							return returnResult("CircularReference", cacheContextIRI);
//						}else
						 throw new CircularReferenceException(CIRCULARREFERENCE_EXCEPTION, String.format(
								"Circular reference encountered when evaluating <%s> of <%s>.\r\n%s",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(), getStack()
										.subList(getStack().size() - getStack().search(stackKey), getStack().size())));
					}
				} catch (ScriptException e) {
					//decrementTraceLevel();
					getEvaluationContext().getTracer().decrementLevel();
					logger.error(String.format(
							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
							StringEscapeUtils.escapeHtml4(e.getMessage())));
					getEvaluationContext().getTracer().traceScriptError(e);
				//	addTrace(scriptFailedMesssage);
					throw new ScriptFailedException(SCRIPTFAILED_EXCEPTION, e);

				}
			}
		}

	}

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


	public Resource processFactObjectValue(IRI predicate, Value objectValue, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) {
		Resource returnResult = null;
		SimpleLiteral literalValue;
		{
			literalValue = (SimpleLiteral) objectValue;
			if (Evaluator.isScriptEngine(literalValue.getDatatype())) {
				org.eclipse.rdf4j.model.Resource customQueryOptionsContext;
				if(customQueryOptions!=null)
					customQueryOptionsContext = customQueryOptions.getContext();
				else
					 customQueryOptionsContext = CustomQueryOptions.getEmptyContext();
				FactCache factCache = getSource().getIntelligentGraphConnection().getIntelligentGraphSail().getFactCache();
				if (/*notTracing() && */factCache.contains(this.getIRI(), predicate, customQueryOptionsContext)) {
					Value resultValue =  factCache.getFacts(this.getIRI(), predicate, customQueryOptionsContext);
					logger.debug("Retrieved cache {} of {} = {}", predicate.stringValue(),
							getSuperValue().stringValue(), resultValue.stringValue());
					getEvaluationContext().getTracer().traceRetrievedCache(this,predicate, resultValue);
					returnResult = Resource.create(getSource(), resultValue, evaluationContext);
				} else {
					Resource result = this.handleScript(literalValue, predicate,customQueryOptions,contexts);//getFact(predicate, literalValue);
					if (result != null) {
						//TODO validate caching
						factCache.addFact(this.getIRI(), predicate,result.getSuperValue() ,customQueryOptionsContext);
						getEvaluationContext().getTracer().traceCalculated(this,predicate, result);
					}
					returnResult = result;
				}
			} else {
				getEvaluationContext().getTracer().traceRetrievedLiteral(this,predicate, objectValue);
				returnResult = Resource.create(getSource(), objectValue, this.getEvaluationContext());
			}
		}
		return returnResult;
	}

	public Resource returnResult(Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "NullValue":
				return Resource.create(getSource(), literal((String) "null"), this.getEvaluationContext());
			case "Integer":
				return Resource.create(getSource(), literal((Integer) result), this.getEvaluationContext());
			case "Double":
				return Resource.create(getSource(), literal((Double) result), this.getEvaluationContext());
			case "Float":
				return Resource.create(getSource(), literal((Float) result), this.getEvaluationContext());
			case "decimal":
				return Resource.create(getSource(), literal((BigDecimal) result), this.getEvaluationContext());
			case "BigDecimal":
				return Resource.create(getSource(), literal((BigDecimal) result), this.getEvaluationContext());
			case "BigInteger":
				return Resource.create(getSource(), literal((BigInteger) result), this.getEvaluationContext());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				getSource().writeModelToCache(this, result, cacheContextIRI);
				return Thing.create(getSource(), cacheContextIRI, this.getEvaluationContext());
			case "Literal":
				Value content = ((pathQLModel.Literal) result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
				case "int":
				case "integer":
					return Resource.create(getSource(),
							literal((BigInteger) ((pathQLModel.Literal) result).bigIntegerValue()),
							this.getEvaluationContext());
				case "decimal":
					return Resource.create(getSource(), literal(((pathQLModel.Literal) result).decimalValue()),
							this.getEvaluationContext());
				case "double":
					return Resource.create(getSource(), literal(((pathQLModel.Literal) result).doubleValue()),
							this.getEvaluationContext());
				case "string": 
					return Resource.create(getSource(), literal(content.stringValue()), this.getEvaluationContext());
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),
							((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName());
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

	@Deprecated
	public Thing prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = PathQLRepository.trimAndCheckIRIString(IRI);
		if (iri != null) {
			this.getEvaluationContext().getPrefixes().put(prefix, iri);
			return this;
		} else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);
			return null;
		}
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
	public Trace traceFact(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {

		if(remoteHostProcessing) {	
			SimpleDataset dataset = getDataset( customQueryOptions);
			dataset.addDefaultGraph(this.graphName);
			org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
			ResourceStatementResults results = null;
			CloseableIteration<Statement, RepositoryException> statementIterator = this.getSource()
						.getRepository().getConnection()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.TRACEFACTS), literal(predicatePattern), contextArray);
			results = new ResourceStatementResults(statementIterator, this, null, customQueryOptions);
			for(Resource result:results) {
				String resultString = result.stringValue();
				results.close();
				return new Trace(resultString);
			}
			results.close();
			return new Trace("results");
		}else {
//			if (this.getEvaluationContext() == null) {
//				this.evaluationContext = new EvaluationContext();
//			}
//			supersedeCustomQueryOptions(customQueryOptions);
//			this.getEvaluationContext().setTracing(true);
//			ResourceResults factValues = PathQL.evaluate(this, predicatePattern,customQueryOptions);
//			if (factValues.hasNext()) {
//				factValues.next();
//			}
//			return new Trace(this.getEvaluationContext().getTrace());
		}
		return null;
	}

	public Trace traceFact(String predicatePattern) throws PathPatternException {
		return traceFact(predicatePattern, null);
	}

	public void deleteFacts(String predicatePattern) throws Exception {		
		if(remoteHostProcessing) {
			org.eclipse.rdf4j.model.Resource[] contextArray = this.getEvaluationContext().getContexts(); 
			this.getSource().getRepository().getConnection().remove(this.getIRI(),
							iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern));
		}else {
//			try {
//				PredicateElement predicateElement = (PredicateElement) PathParser.parsePathPattern(this, predicatePattern);
//				pathQLResults.ResourceResults facts;
//				if (predicateElement != null) {
//					facts = PathQL.evaluate(this, predicateElement);
//				} else {
//					throw new PathPatternException();
//				}
//				if (predicateElement.getIsReified()) {
//					deleteReifiedFacts(facts);
//				} else if (predicateElement.getIsDereified()) {
//					throw new NotSupportedException("derifiedFact deletion");
//				} else {
//					deleteFacts(facts);
//				}
//				//Since changed the database, we need to clear any cache values.
//				PathQLRepository.clearCaches();
//	
//			} catch (Exception e) {
//				throw e;
//			}
			
		}
	}
//	@Deprecated
//	private void deleteFacts(pathQLResults.ResourceResults facts) throws QueryEvaluationException, RepositoryException {
//		RepositoryConnection connection = this.getSource().getRepository().getConnection();//getContextAwareConnection();
//		while (facts.hasNext()) {
//			Fact nextFact = facts.nextFact();		
//			connection.remove(nextFact.getSubjectIRI(), nextFact.getPredicateIRI(), nextFact.getValue(), this.getGraphName());
//		}
//	}
//	@Deprecated
//	private void deleteReifiedFacts(pathQLResults.ResourceResults facts)
//			throws QueryEvaluationException, RepositoryException {
//		RepositoryConnection connection = this.getSource().getRepository().getConnection();//getContextAwareConnection();
//		PredicateElement predicateElement = (PredicateElement) facts.getPathElement();
//
//		ReificationType reificationType = this.getSource().getReificationTypes()
//				.get(predicateElement.getReification().stringValue());
//		if (reificationType != null) {
//
//			while (facts.hasNext()) {
//				IRI reificationValue = facts.nextReifiedValue();		
//				connection.remove(reificationValue, null, null, this.getGraphName());
//			}
//		} else {
//			logger.error("Reified type not supported:" + predicateElement.toString());
//		}
//	}

	public Thing addFact(String property, String value, IRI dataType) {

		try {
			Literal literal = literal(value, dataType);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);

			addFact(literal, predicateElement);

		} catch (Exception e) {

		}
		return this;

	}

	public Thing addFact(String property, String value) {

		try {
			Literal literal = literal(value);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			addFact(literal, predicateElement);
		} catch (Exception e) {

		}
		return this;

	}

	private void addFact(Literal literal, PredicateElement predicateElement) throws RepositoryException {
		RepositoryConnection connection = this.getSource().getContextAwareConnection();
		if (predicateElement.getIsReified()) {
			ReificationType reificationType = this.getSource().getReificationTypes()
					.get(predicateElement.getReification().stringValue());
			if (reificationType != null) {
				IRI reification = iri(
						reificationType.getReificationType().stringValue() + "/" + this.getIRI().hashCode() + "."
								+ predicateElement.getPredicate().hashCode() + "." + literal.hashCode());
				connection.add(reification, Evaluator.RDF_TYPE_IRI, predicateElement.getReification(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationSubject(), this.getIRI(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationPredicate(), predicateElement.getPredicate(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationObject(), literal,
						this.getGraphName());
			} else {
				logger.error("Reified type not supported:" + predicateElement.toString());
			}

		} else {
			IRI propertyIri = predicateElement.getPredicate();
			
			connection.add(this.getIRI(), propertyIri, literal, this.getGraphName());
		}
		//Since changed the database, we need to xclear any cache values.
		PathQLRepository.clearCaches();
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
	 * @return the dataset
	 */
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
	public SimpleDataset getDataset(CustomQueryOptions customQueryOptions) {
		SimpleDataset dataset = new SimpleDataset();//getDataset();
		if(customQueryOptions!=null) {
			if(dataset==null)dataset=new SimpleDataset();
			dataset.addDefaultGraph(iri(IntelligentGraphSail.URN_CUSTOM_QUERY_OPTIONS+"?"+customQueryOptions.toURIEncodedString()));
		}
		return dataset;
	}
	/**
	 * Gets the thing.
	 *
	 * @param thing the thing
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public Thing getThing(String thing) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(this.getSource(),thing).getIri();
		return create(this.getSource(), thingIri,this.getEvaluationContext());
	}

//	@Deprecated
//	public final Resource getFact(PredicateElement predicateElement) {
//		String key = getFactKey(predicateElement);
//		getEvaluationContext().getTracer().traceSeeking(this,predicateElement,getCustomQueryOptions());
//		//addTrace(String.format("Seeking value %s of %s using customQueryOptions {}",
//		//		addIRI(predicateElement.getPredicate()), addIRI(getSuperValue()), getCustomQueryOptions()));
//		if (notTracing() && getCachedResources().containsKey(key)) {
//			Resource result = getCachedResources().get(key);
//			getEvaluationContext().getTracer().traceRetrieved(this,predicateElement, result);
//			//addTrace(String.format("Retrieved cache %s of %s = %s", predicateElement.toString(),
//			//		addIRI(getSuperValue()), getHTMLValue(result.getValue())));
//			return result;
//		} else {
//			return retrieveFact(predicateElement.getPredicate(), key);
//		}
//	}

//	@Deprecated
//	private Resource retrieveFact(IRI predicate, String key) throws QueryEvaluationException {
//		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = getSource()
//				.getTripleSource().getStatements((IRI) getSuperValue(), predicate, null);
//		Resource returnResult = null;
//		while (objectStatements.hasNext()) {
//			Statement objectStatement = objectStatements.next();
//			Value objectValue = objectStatement.getObject();
//			returnResult = processFactObjectValue(predicate, objectValue,this.getCustomQueryOptions());
//		}
//		if (returnResult != null)
//			return returnResult;
//		getEvaluationContext().getTracer().traceNoPredicate(this,predicate);
//		//addTrace(String.format("Error: No predicate %s found for subject %s", addIRI(predicate),
//		//		addThisIRI()));
//		// It could be there are reified attributes associated with scripts or signals
//		Resource reifiedValue = getReifiedValue(getSource().createIRI(Evaluator.RDF_STATEMENT), predicate);
//		if (reifiedValue != null) {
//			return reifiedValue;//source.valueFactory(getTracer(), reifiedValue, getStack(),this.customQueryOptions,this.prefixes);
//		}
//		decrementTraceLevel();
//		return new pathQLModel.Literal(null);
//	}

//	@Deprecated
//	private Resource getReifiedValue(IRI createIRI, IRI predicate) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
