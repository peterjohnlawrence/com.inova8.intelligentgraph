/*
 * inova8 2020
 */
package resources_Deprecated;

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
import org.apache.commons.lang3.Validate;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.FactCache;
import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.exceptions.CircularReferenceException;
import com.inova8.intelligentgraph.exceptions.HandledException;
import com.inova8.intelligentgraph.exceptions.NullValueReturnedException;
import com.inova8.intelligentgraph.exceptions.ScriptFailedException;
import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.intelligentGraphRepository.ReificationType;
import com.inova8.intelligentgraph.intelligentGraphRepository.SEEQSource;
import com.inova8.intelligentgraph.path.NullPath;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathCalc.Trace;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathPatternException;

import resources_Deprecated.DeprecatedThing;

import com.inova8.intelligentgraph.pathQLModel.NullResource;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLResults.PathResults;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;
import com.inova8.intelligentgraph.pathQLResults.ResourceStatementResults;

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

public class DeprecatedThing extends Resource {

	private static final long serialVersionUID = 1L;
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(DeprecatedThing.class);
	/** The cached resources. */
	private HashMap<String, Resource> cachedResources;
	private  IRI graphName;
	protected DeprecatedThing(org.eclipse.rdf4j.model.Value superValue) {
		super(superValue);
	}
	public static DeprecatedThing create(IntelligentGraphRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		return DeprecatedThing.create( source,  null, superValue,	 evaluationContext);
	}
	public static DeprecatedThing create(IntelligentGraphRepository source, IRI graphIri, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		com.inova8.intelligentgraph.pathQLModel.Thing thing;

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
			thing = new DeprecatedThing(source, superValue, evaluationContext);
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
	private DeprecatedThing(IntelligentGraphRepository source, org.eclipse.rdf4j.model.Value superValue,
			EvaluationContext evaluationContext) {
		super(superValue, evaluationContext);
		this.setSource(source);
	}
	@Override
	public 	boolean isIRI(){
		return true;
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
		IRI cacheContextIRI = iri(SCRIPT.DATA, cacheContext);
		return cacheContextIRI;
	}

	@Deprecated
	public HashMap<String, Resource> getCachedResources() {
		if( cachedResources==null) {
			cachedResources= new HashMap<String, Resource> ();
		}
		return cachedResources;
	}

	public final Resource getFact(IRI predicate, SimpleLiteral scriptString, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) throws HandledException {
		Resource fact = processFactObjectValue(predicate,scriptString,  customQueryOptions ,contexts);
		return fact;
	}

	public final Resource getFact(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException{
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
	public final Resource getFact(String predicatePattern, Value...bindValues ) throws PathPatternException  {
		logger.debug("getFact{}\n", predicatePattern);
		//this.getEvaluationContext().getTracer().traceFact(this, predicatePattern);
		ResourceResults factValues =  getFacts( predicatePattern,bindValues);
		if (factValues == null) {
			this.getEvaluationContext().getTracer().traceFactReturnNull(this, predicatePattern);
			//return new NullResource();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s", predicatePattern, this));
		} else if (factValues.hasNext()) {
			Resource nextFact = factValues.next();
			this.getEvaluationContext().getTracer().traceFactReturnValue(this, predicatePattern,nextFact);
			return nextFact;
		} else {
			this.getEvaluationContext().getTracer().traceFactEmpty(this, predicatePattern);
			factValues.close();
		//	return new NullResource();
			throw new NullValueReturnedException( String.format(
					"No values found for pattern %s with subject %s", predicatePattern, this));
		}	
	}

	public final ResourceResults getFacts(String predicatePattern, Value... bindValues ) throws PathPatternException {
		return getFacts(predicatePattern,CustomQueryOptions.create(bindValues));
	}
	public final ResourceResults getFacts(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {
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

	public Resource getSignal(String signal) {
		getEvaluationContext().getTracer().incrementLevel();
		//incrementTraceLevel();
		signal = IntelligentGraphRepository.trimIRIString(signal);
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

	protected final Resource handleScript(SimpleLiteral scriptString, IRI predicate, CustomQueryOptions customQueryOptions,org.eclipse.rdf4j.model.Resource ... contexts) throws HandledException{
		if (predicate.equals(SCRIPT.SCRIPTCODE)) {
			return Resource.create(getSource(), scriptString, this.getEvaluationContext());
		} else {
			String scriptCode = scriptString.getLabel();
			scriptCode = scriptCode.trim();
			if (scriptCode.startsWith("<")) {
				String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
				org.eclipse.rdf4j.model.Resource scriptResource = convertQName(scriptIRI);
				//IRI scriptPropertyIRI = iri(SCRIPT.SCRIPTCODE);
				Statement scriptStatement;
				SimpleLiteral scriptCodeliteral = null;
				try {
					CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = getSource()
							.getTripleSource().getStatements(scriptResource, SCRIPT.SCRIPTCODE, null);

					while (scriptStatements.hasNext()) {
						scriptStatement = scriptStatements.next();
						scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
					}
				} catch (QueryEvaluationException qe) {
					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
					throw new ScriptFailedException( String.format(
							"Reference script <%s> not found for  %s of  subject %s", scriptIRI, predicate, this), qe);
				}
				if (scriptCodeliteral != null) {
					getEvaluationContext().getTracer().traceRedirecting(this, predicate, scriptCode);
					return handleScript(scriptCodeliteral, predicate,customQueryOptions, contexts);
				} else {
					getEvaluationContext().getTracer().traceRedirectingFailed(this, predicate, scriptCode);
					throw new ScriptFailedException( String.format(
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
						scriptBindings.put("_customQueryOptions",customQueryOptions);

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

						getEvaluationContext().getTracer().decrementLevel();
						if (result != null)
							getEvaluationContext().getTracer().traceEvaluated(this, predicate, result);
						else {
							throw new NullValueReturnedException(
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
						 throw new CircularReferenceException(String.format(
								"Circular reference encountered when evaluating <%s> of <%s>.\r\n%s",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(), getStack()
										.subList(getStack().size() - getStack().search(stackKey), getStack().size())));
					}
				} catch (ScriptException e) {
					getEvaluationContext().getTracer().decrementLevel();
					logger.error(String.format(
							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
							StringEscapeUtils.escapeHtml4(e.getMessage())));
					getEvaluationContext().getTracer().traceScriptError(e);
					throw new ScriptFailedException( e);

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


	public Resource processFactObjectValue(IRI predicate, Value objectValue, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) throws QueryEvaluationException {
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
					Resource result = this.handleScript(literalValue, predicate,customQueryOptions,contexts);
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
				return (DeprecatedThing) result;
			case "LinkedHashModel":
				getSource().writeModelToCache(this, result, cacheContextIRI);
				return DeprecatedThing.create(getSource(), cacheContextIRI, this.getEvaluationContext());
			case "Literal":
				Value content = ((com.inova8.intelligentgraph.pathQLModel.Literal) result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
				case "int":
				case "integer":
					return Resource.create(getSource(),
							literal((BigInteger) ((com.inova8.intelligentgraph.pathQLModel.Literal) result).bigIntegerValue()),
							this.getEvaluationContext());
				case "decimal":
					return Resource.create(getSource(), literal(((com.inova8.intelligentgraph.pathQLModel.Literal) result).decimalValue()),
							this.getEvaluationContext());
				case "double":
					return Resource.create(getSource(), literal(((com.inova8.intelligentgraph.pathQLModel.Literal) result).doubleValue()),
							this.getEvaluationContext());
				case "string": 
					return Resource.create(getSource(), literal(content.stringValue()), this.getEvaluationContext());
				case "time": 
					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
				case "date": 
					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
				case "dateTime": 
					return Resource.create(getSource(), literal(content), this.getEvaluationContext());
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
	public DeprecatedThing prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = IntelligentGraphRepository.trimAndCheckIRIString(IRI);
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

	public Trace traceFact(String predicatePattern, Value...  bindValues) throws PathPatternException {
		return traceFact(predicatePattern, CustomQueryOptions.create(bindValues));
	}

	public void deleteFacts(String predicatePattern) throws Exception {		
		org.eclipse.rdf4j.model.Resource[] contextArray = this.getEvaluationContext().getContexts(); 
		this.getSource().getRepository().getConnection().remove(this.getIRI(),
							PATHQL.GETFACTS, literal(predicatePattern));
	}


	public DeprecatedThing addFact(String property, String value, IRI dataType) {
		try {
			Literal literal = literal(value, dataType);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			addFact(predicateElement,literal );
		} catch (Exception e) {
		}
		return this;
	}
	public DeprecatedThing addFact(IRI property, String value, IRI dataType ) {
		Literal literal = literal(value, dataType);
		addFact(property,literal);
		return this;
	}
	public DeprecatedThing addFact(IRI property, Value value) {
		Validate.notNull(property);
		Validate.notNull(value);
		RepositoryConnection connection = this.getSource().getContextAwareConnection();
		switch(value.getClass().getName() ) {
			case "com.inova8.intelligentgraph.pathCalc.Thing":
				connection.add(this.getIRI(), property, ((DeprecatedThing) value).getIRI(), this.getGraphName());
				break;
			default:
				connection.add(this.getIRI(), property, value, this.getGraphName());
		}		
		//IntelligentGraphRepository.clearCaches();
		return this;
	}
	public DeprecatedThing addFact(String property, Value value) {
		Validate.notNull(property);
		Validate.notNull(value);
		try {
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			switch(value.getClass().getName() ) {
			case "com.inova8.intelligentgraph.pathCalc.Thing":
				addFact(predicateElement,((DeprecatedThing) value).getIRI() );
				break;
			default:
				addFact(predicateElement,value );
		}
			
		} catch (Exception e) {

		}		
		
		return this;
	}
	public DeprecatedThing addFact(String property, String value) {
		Validate.notNull(property);
		Validate.notNull(value);
		try {
			Literal literal = literal(value);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			addFact(predicateElement,literal );
		} catch (Exception e) {

		}
		return this;

	}

	private void addFact( PredicateElement predicateElement,Value value) throws RepositoryException {
		RepositoryConnection connection = this.getSource().getContextAwareConnection();
		if (predicateElement.getIsReified()) {
			ReificationType reificationType = this.getSource().getReificationTypes()
					.get(predicateElement.getReification().stringValue());
			if (reificationType != null) {
				IRI reification = iri(
						reificationType.getReificationType().stringValue() + "/" + this.getIRI().hashCode() + "."
								+ predicateElement.getPredicate().hashCode() + "." + value.hashCode());
				connection.add(reification, RDF.TYPE, predicateElement.getReification(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationSubject(), this.getIRI(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationPredicate(), predicateElement.getPredicate(),
						this.getGraphName());
				connection.add(reification, reificationType.getReificationObject(), value,
						this.getGraphName());
			} else {
				logger.error("Reified type not supported:" + predicateElement.toString());
			}

		} else {
			IRI propertyIri = predicateElement.getPredicate();
			
			connection.add(this.getIRI(), propertyIri, value, this.getGraphName());
		}
		//Since changed the database, we need to xclear any cache values.
		//IntelligentGraphRepository.clearCaches();
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
			dataset.addDefaultGraph(iri(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS+"?"+customQueryOptions.toURIEncodedString()));
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
	public DeprecatedThing getThing(String thing) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(this.getSource(),thing).getIri();
		return create(this.getSource(), thingIri,this.getEvaluationContext());
	}



}
