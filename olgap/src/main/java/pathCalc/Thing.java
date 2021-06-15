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
import Exceptions.CircularReferenceException;
import Exceptions.HandledException;
import Exceptions.NotSupportedException;
import Exceptions.NullValueReturnedException;
import Exceptions.ScriptFailedException;
import intelligentGraph.FactCache;
import intelligentGraph.IntelligentGraphConnection;
import intelligentGraph.IntelligentGraphSail;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQL.PathQL;
import pathQLModel.Fact;
import pathQLModel.NullValue;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLRepository.ReificationType;
import pathQLRepository.SEEQSource;
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

	private final IRI generateCacheContext(IRI predicate) {
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

	@Deprecated
	private String getFactKey(PredicateElement predicateElement) {
		return getFactKey(predicateElement.getPredicate());
	}

	@Deprecated
	private String getFactKey(IRI predicate) {
		String key = predicate.stringValue() + "." + StringUtils.join(this.getCustomQueryOptions()).hashCode() + "."
				+ StringUtils.join(getSource().getPublicContexts()).hashCode();
		return key;
	}


	public final Resource getFact(IRI predicate, SimpleLiteral scriptString, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) {
		Resource fact = processFactObjectValue(predicate,scriptString,  customQueryOptions ,contexts);//this.handleScript(scriptString, predicate);
		return fact;
	}

	public final Resource getFact(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException{
		logger.debug("getFact{}\n", predicatePattern);

		ResourceResults factValues =  getFacts( predicatePattern,customQueryOptions);
		if (factValues == null) {
			return new NullValue();
		} else if (factValues.hasNext()) {
			return factValues.next();
		} else {
			factValues.close();
			return new NullValue();
		}
		
	}


	public final Resource getFact(String predicatePattern) throws PathPatternException {
		logger.debug("getFact{}\n", predicatePattern);
		ResourceResults factValues =  getFacts( predicatePattern);
		if (factValues == null) {
			return new NullValue();
		} else if (factValues.hasNext()) {
			return factValues.next();
		} else {
			factValues.close();
			return new NullValue();
		}
	}

	public final ResourceResults getFacts(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {
		//This could be configured to process locally in a client or remotely in the server. Not sure both are required.
		boolean remoteHostProcessing = true;
		if(remoteHostProcessing) {
			//this.supersedeCustomQueryOptions(customQueryOptions);
			SimpleDataset dataset = getDataset( customQueryOptions);
			dataset.addDefaultGraph(this.graphName);
			org.eclipse.rdf4j.model.Resource[] contextArray = dataset.getDefaultGraphs().toArray(new org.eclipse.rdf4j.model.Resource[0] );
			ResourceStatementResults results = null;
				CloseableIteration<Statement, RepositoryException> statementIterator = this.getSource()
						.getRepository().getConnection()
						.getStatements(this.getIRI(),
								iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern), contextArray);
				results = new ResourceStatementResults(statementIterator, this, null, customQueryOptions);
			return results;
		}else {
			return PathQL.evaluate(this, predicatePattern,customQueryOptions);
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

	/**
	 * Gets the signal.
	 *
	 * @param signal
	 *            the signal
	 * @return the signal
	 */
	public Resource getSignal(String signal) {
		incrementTraceLevel();
		signal = PathQLRepository.trimIRIString(signal);
		String[] elements = signal.split("/");
		Object result;
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			SEEQSource seeqSource = null;
			try {
				addTrace(String.format("Fetching SEEQ signal %s with customQueryOptions %s", elements[5],
						getCustomQueryOptions()));
				seeqSource = getSource().seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], getCustomQueryOptions());
				decrementTraceLevel();
				//return getSource().resourceFactory(getTracer(),PathQLRepository.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),customQueryOptions,this.prefixes);
				return Resource.create(getSource(), literal((Double) result), this.getEvaluationContext());
			} catch (ScriptException e) {
				return Resource.create(getSource(), literal("**SEEQ Source Error**"), this.getEvaluationContext());
			} catch (HandledException e) {
				return Resource.create(getSource(), literal(e.getCode()), this.getEvaluationContext());
			}
		case "HTTP:":
			String httpMessage = String.format("HTTP not supported signal source: %s", signal);
			logger.error(httpMessage);
			addTrace(httpMessage);
			return Resource.create(getSource(), literal("**HTTP Source Error**"), this.getEvaluationContext());
		default:
			String defaultMessage = String.format("Unsupported signal source: %s", signal);
			logger.error(defaultMessage);
			addTrace(defaultMessage);
			return Resource.create(getSource(), literal("**Unsupported Source Error**"), this.getEvaluationContext());

		}
	}


	/**
	 * Handle script.
	 *
	 * @param scriptString
	 *            the script string
	 * @param predicate
	 *            the predicate
	 * @param customQueryOptions 
	 * @param contexts 
	 * @return the resource
	 */
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
					throw new ScriptFailedException(SCRIPTNOTFOUND_EXCEPTION, String.format(
							"Reference script <%s> not found for  %s of  subject %s", scriptIRI, predicate, this), qe);
				}
				if (scriptCodeliteral != null) {
					return handleScript(scriptCodeliteral, predicate,customQueryOptions, contexts);
				} else {
					throw new ScriptFailedException(NULLVALUESCRIPT_EXCEPTION, String.format(
							"Reference script null <%s> for %s of subject %s", scriptIRI, predicate, this));
				}
			} else {
				incrementTraceLevel();
				IRI cacheContextIRI = generateCacheContext(predicate);
				String scripttype = scriptString.getDatatype().getLocalName();
				addTrace(String.format("Evaluating predicate %s of %s, by invoking <b>%s</b> script\n",
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
						
						CompiledScript compiledScriptCode = getSource().compiledScriptFactory(scriptString);
						SimpleBindings scriptBindings = new SimpleBindings();
					//	scriptBindings.put("$tripleSource", getSource().getTripleSource());
						scriptBindings.put("$this", this);
					//	scriptBindings.put("$property",		Thing.create(getSource(), predicate, this.getEvaluationContext()));
						scriptBindings.put("$customQueryOptions",customQueryOptions);// getCustomQueryOptions());
					//	scriptBindings.put("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI));
						Resource result;
						try {
							pushStack(stackKey);
							scriptResult = compiledScriptCode.eval(scriptBindings);
							result = returnResult(scriptResult, cacheContextIRI);
						}
						finally {
							//Since script complete, pop from stack
							popStack();
						}
						decrementTraceLevel();
						if (result != null)
							addTrace(String.format("Evaluated %s of %s =  %s", addIRI(predicate),
									addIRI(getSuperValue()), result.getHTMLValue()));
						else {
							throw new NullValueReturnedException(NULLVALUERETURNED_EXCEPTION,
									String.format("Evaluated null for %s of %s, using script %s", predicate,
											getSuperValue(), scriptString));
						}
						decrementTraceLevel();
						return result;
					} else {
						String circularReferenceMessage = String.format(
								"Circular reference encountered when evaluating %s of %s:", addIRI(predicate),
								addIRI(getSuperValue()));
						addTrace(circularReferenceMessage);
						addScript(getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
								.toString());
						logger.error(
								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(),
								getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
										.toString());
						throw new CircularReferenceException(CIRCULARREFERENCE_EXCEPTION, String.format(
								"Circular reference encountered when evaluating <%s> of <%s>.\r\n%s",
								predicate.stringValue(), ((IRI) getSuperValue()).stringValue(), getStack()
										.subList(getStack().size() - getStack().search(stackKey), getStack().size())));
					}
				} catch (ScriptException e) {
					decrementTraceLevel();
					String scriptFailedMesssage = String.format(
							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
							StringEscapeUtils.escapeHtml4(e.getMessage()));
					logger.error(scriptFailedMesssage);
					addTrace(scriptFailedMesssage);
					throw new ScriptFailedException(SCRIPTFAILED_EXCEPTION, e);

				}
			}
		}

	}

	/**
	 * Generate stack key.
	 *
	 * @param predicate
	 *            the predicate
	 * @return the string
	 */
	private String generateStackKey(IRI predicate) {
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

	/**
	 * Process fact object value.
	 *
	 * @param predicate
	 *            the predicate
	 * @param objectValue
	 *            the object value
	 * @param contexts 
	 * @return Processes the objectValue
	 */
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
				if (notTracing() && factCache.contains(this.getIRI(), predicate, customQueryOptionsContext)) {
					//Resource result = getCachedResources().get(key);
					Value resultValue =  factCache.getFacts(this.getIRI(), predicate, customQueryOptionsContext);
					logger.debug("Retrieved cache {} of {} = {}", predicate.stringValue(),
							addIRI(getSuperValue()), getHTMLValue(resultValue));
					addTrace(String.format("Retrieved cache %s of %s = %s", predicate.stringValue(),
							addIRI(getSuperValue()), getHTMLValue(resultValue)));
					returnResult = Resource.create(getSource(), resultValue, evaluationContext);
				} else {
					Resource result = this.handleScript(literalValue, predicate,customQueryOptions,contexts);//getFact(predicate, literalValue);
					if (result != null) {
						//TODO validate caching
						factCache.addFact(this.getIRI(), predicate,result.getSuperValue() ,customQueryOptionsContext);
						addTrace(String.format("Calculated %s of %s = %s", addIRI(predicate),
								addIRI(getSuperValue()), result.getHTMLValue()));
					}
					returnResult = result;
				}
			} else {
				addTrace(String.format("Retrieved literal %s of %s = %s", addIRI(predicate),
						addIRI(getSuperValue()), getHTMLValue(objectValue)));
				returnResult = Resource.create(getSource(), objectValue, this.getEvaluationContext());
			}
		}
		return returnResult;
	}



	/**
	 * Return result.
	 *
	 * @param result
	 *            the result
	 * @param cacheContextIRI
	 *            the cache context IRI
	 * @return the olgap. value
	 */
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

//	/**
//	 * Sets the cached values.
//	 *
//	 * @param cachedValues
//	 *            the cached values
//	 */
//	public void setCachedValues(ConcurrentHashMap<String, Resource> cachedValues) {
//		this.setCachedResources(cachedValues);
//	}

	/**
	 * Prefix.
	 *
	 * @param prefix
	 *            the prefix
	 * @param IRI
	 *            the iri
	 * @return the thing
	 */
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

	/**
	 * Prefix.
	 *
	 * @param IRI
	 *            the iri
	 * @return the thing
	 */
	@Deprecated
	public Thing prefix(String IRI) {
		return this.prefix("", IRI);
	}

	/**
	 * Gets the facts.
	 *
	 * @param path
	 *            the path
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
	public String traceFact(String predicatePattern, CustomQueryOptions customQueryOptions) throws PathPatternException {
		if (this.getEvaluationContext() == null) {
			this.evaluationContext = new EvaluationContext();
		}
		supersedeCustomQueryOptions(customQueryOptions);
		this.getEvaluationContext().setTracing(true);
		ResourceResults factValues = PathQL.evaluate(this, predicatePattern,customQueryOptions);
		if (factValues.hasNext()) {
			factValues.next();
		}
		return this.getEvaluationContext().getTrace();
	}
	public String traceFact(String predicatePattern) throws PathPatternException {
		return traceFact(predicatePattern, null);
	}

	public void deleteFacts(String predicatePattern) throws Exception {		
		boolean remoteHostProcessing = true;
		if(remoteHostProcessing) {
			org.eclipse.rdf4j.model.Resource[] contextArray = this.getEvaluationContext().getContexts(); 
//			if (this.getSource().getTripleSource() != null) { 
//				 this.getSource().getTripleSource().removeStatements(this.getIRI(),
//								iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern), contextArray);
//				
//			} else if (this.getSource().getContextAwareConnection() != null) {
				 this.getSource().getRepository().getConnection().remove(this.getIRI(),
							iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern));
						//.getIntelligentGraphConnection().removeStatements(this.getIRI(),
						//		iri(IntelligentGraphConnection.GETFACTS), literal(predicatePattern), contextArray);
//			}

		}else {
			try {
				PredicateElement predicateElement = (PredicateElement) PathParser.parsePathPattern(this, predicatePattern);
				pathQLResults.ResourceResults facts;
				if (predicateElement != null) {
					facts = PathQL.evaluate(this, predicateElement);
				} else {
					throw new PathPatternException();
				}
				if (predicateElement.getIsReified()) {
					deleteReifiedFacts(facts);
				} else if (predicateElement.getIsDereified()) {
					throw new NotSupportedException("derifiedFact deletion");
				} else {
					deleteFacts(facts);
				}
				//getSource();
				//Since changed the database, we need to xclear any cache values.
				PathQLRepository.clearCaches();
	
			} catch (Exception e) {
				throw e;
			}
			
		}
	}
	@Deprecated
	private void deleteFacts(pathQLResults.ResourceResults facts) throws QueryEvaluationException, RepositoryException {
		RepositoryConnection connection = this.getSource().getRepository().getConnection();//getContextAwareConnection();
		while (facts.hasNext()) {
			Fact nextFact = facts.nextFact();		
			connection.remove(nextFact.getSubjectIRI(), nextFact.getPredicateIRI(), nextFact.getValue(), this.getGraphName());
		}
	}
	@Deprecated
	private void deleteReifiedFacts(pathQLResults.ResourceResults facts)
			throws QueryEvaluationException, RepositoryException {
		RepositoryConnection connection = this.getSource().getRepository().getConnection();//getContextAwareConnection();
		PredicateElement predicateElement = (PredicateElement) facts.getPathElement();

		ReificationType reificationType = this.getSource().getReificationTypes()
				.get(predicateElement.getReification().stringValue());
		if (reificationType != null) {

			while (facts.hasNext()) {
				IRI reificationValue = facts.nextReifiedValue();		
				connection.remove(reificationValue, null, null, this.getGraphName());
			}
		} else {
			logger.error("Reified type not supported:" + predicateElement.toString());
		}
	}

	/**
	 * Adds the fact.
	 *
	 * @param property
	 *            the property
	 * @param value
	 *            the value
	 * @param dataType
	 *            the data type
	 * @return the thing
	 */
	public Thing addFact(String property, String value, IRI dataType) {

		try {
			Literal literal = literal(value, dataType);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);

			addFact(literal, predicateElement);

		} catch (Exception e) {

		}
		return this;

	}

	/**
	 * Adds the fact.
	 *
	 * @param property
	 *            the property
	 * @param value
	 *            the value
	 * @return the thing
	 */
	public Thing addFact(String property, String value) {

		try {
			Literal literal = literal(value);
			PredicateElement predicateElement = PathParser.parsePredicate(getSource(), property);
			addFact(literal, predicateElement);
		} catch (Exception e) {

		}
		return this;

	}

	/**
	 * Adds the fact.
	 *
	 * @param literal
	 *            the literal
	 * @param predicateElement
	 *            the predicate element
	 * @throws RepositoryException
	 *             the repository exception
	 */
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
		//	HashSet<IRI> publicContexts = this.getSource().getIntelligentGraphConnection().getIntelligentGraphSail().getPublicContexts();
		//	HashSet<IRI> publicContexts = getSource().getPublicContexts();
			if (publicContexts == null || publicContexts.isEmpty()) {
				org.eclipse.rdf4j.model.Resource[] contexts = getEvaluationContext().getContexts();
				if (contexts == null || contexts.length == 0) {
					//TODO temp
//					dataset = new SimpleDataset();
//					for (org.eclipse.rdf4j.model.Resource resource : declaredContexts) {
//						dataset.addDefaultGraph((IRI) resource);
//					}
//					return dataset;
					
					return null;
				} else {
					dataset = new SimpleDataset();
					for (org.eclipse.rdf4j.model.Resource resource : contexts) {
						dataset.addDefaultGraph((IRI) resource);
					}
				//	getEvaluationContext().setDataset(dataset);
					return dataset;
				}
			} else {
				dataset = new SimpleDataset();
				for (IRI graph : publicContexts) {
					dataset.addDefaultGraph(graph);
				}
			//	getEvaluationContext().setDataset(dataset);
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

	@Deprecated
	public final Resource getFact(PredicateElement predicateElement) {
		String key = getFactKey(predicateElement);
		addTrace(String.format("Seeking value %s of %s using customQueryOptions {}",
				addIRI(predicateElement.getPredicate()), addIRI(getSuperValue()), getCustomQueryOptions()));
		if (notTracing() && getCachedResources().containsKey(key)) {
			Resource result = getCachedResources().get(key);
			addTrace(String.format("Retrieved cache %s of %s = %s", predicateElement.toString(),
					addIRI(getSuperValue()), getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicateElement.getPredicate(), key);
		}
	}

	@Deprecated
	private Resource retrieveFact(IRI predicate, String key) throws QueryEvaluationException {
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = getSource()
				.getTripleSource().getStatements((IRI) getSuperValue(), predicate, null);
		Resource returnResult = null;
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			Value objectValue = objectStatement.getObject();
			returnResult = processFactObjectValue(predicate, objectValue,this.getCustomQueryOptions());
		}
		if (returnResult != null)
			return returnResult;
		addTrace(String.format("Error: No predicate %s found for subject %s", addIRI(predicate),
				addThisIRI()));
		// It could be there are reified attributes associated with scripts or signals
		Resource reifiedValue = getReifiedValue(getSource().createIRI(Evaluator.RDF_STATEMENT), predicate);
		if (reifiedValue != null) {
			return reifiedValue;//source.valueFactory(getTracer(), reifiedValue, getStack(),this.customQueryOptions,this.prefixes);
		}
		decrementTraceLevel();
		return new pathQLModel.Literal(null);
	}

	/**
	 * Gets the reified value.
	 *
	 * @param createIRI
	 *            the create IRI
	 * @param predicate
	 *            the predicate
	 * @return the reified value
	 */
	@Deprecated
	private Resource getReifiedValue(IRI createIRI, IRI predicate) {
		// TODO Auto-generated method stub
		return null;
	}

}
