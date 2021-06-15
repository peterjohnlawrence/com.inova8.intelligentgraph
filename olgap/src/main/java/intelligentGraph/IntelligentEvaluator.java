package intelligentGraph;

import static org.eclipse.rdf4j.model.util.Values.iri;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Exceptions.CircularReferenceException;
import Exceptions.NullValueReturnedException;
import Exceptions.ScriptFailedException;
import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;

public class IntelligentEvaluator {
	private static final String NULLVALUERETURNED_EXCEPTION = "NullValueReturned";
	private static final String SCRIPTFAILED_EXCEPTION = "ScriptFailed";
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	private static final String SCRIPTNOTFOUND_EXCEPTION = null;
	private static final String NULLVALUESCRIPT_EXCEPTION = null;
	private final  FactCache factCache=new FactCache();
	protected final Logger logger = LoggerFactory.getLogger(IntelligentEvaluator.class);
	protected final pathQLModel.Resource handleScript(Thing thing ,SimpleLiteral scriptString, IRI predicate, CustomQueryOptions customQueryOptions) {
		if (predicate.equals(iri(Evaluator.SCRIPTPROPERTY))) {
			return  pathQLModel.Resource.create(null, scriptString, thing.getEvaluationContext());
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
					return handleScript(scriptCodeliteral, predicate,customQueryOptions);
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
						
						CompiledScript compiledScriptCode = thing.getSource().compiledScriptFactory(scriptString);
						SimpleBindings scriptBindings = new SimpleBindings();
					//	scriptBindings.put("$tripleSource", getSource().getTripleSource());
						scriptBindings.put("$this", thing);
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
	 * Process fact object value.
	 *
	 * @param predicate
	 *            the predicate
	 * @param objectValue
	 *            the object value
	 * @return Processes the objectValue
	 */
	public pathQLModel.Resource processFactObjectValue(IRI thing, IRI predicate, Value objectValue, CustomQueryOptions customQueryOptions) {
		pathQLModel.Resource returnResult = null;
		SimpleLiteral literalValue;
		{
			literalValue = (SimpleLiteral) objectValue;
			if (Evaluator.isScriptEngine(literalValue.getDatatype())) {
			//	String key = getFactKey(predicate);
			//	if (notTracing() && getCachedResources().containsKey(key)) {
				org.eclipse.rdf4j.model.Resource customQueryOptionsContext;
				if(customQueryOptions!=null)
					customQueryOptionsContext = customQueryOptions.getContext();
				else
					 customQueryOptionsContext = CustomQueryOptions.getEmptyContext();
				//getSource().getRepository()
				//FactCache factCache = ((IntelligentGraphSail)getSource().getRepository()).getFactCache();
				FactCache factCache = getFactCache();
				if (notTracing() && factCache.contains(thing, predicate, customQueryOptionsContext)) {
					//Resource result = getCachedResources().get(key);
					Value resultValue =  factCache.getFacts(thing, predicate, customQueryOptionsContext);
					//logger.debug("Retrieved cache {} of {} = {}", predicate.stringValue(),addIRI(thing), getHTMLValue(resultValue));
					//addTrace(String.format("Retrieved cache %s of %s = %s", predicate.stringValue(),addIRI(thing), getHTMLValue(resultValue)));
					returnResult = pathQLModel.Resource.create(null, resultValue, getEvaluationContext());
				} else {
					pathQLModel.Resource result = this.handleScript(literalValue, predicate,customQueryOptions);//getFact(predicate, literalValue);
					if (result != null) {
	
						factCache.addFact(thing, predicate,result.getSuperValue() ,customQueryOptionsContext);
					//	addTrace(String.format("Calculated %s of %s = %s", addIRI(predicate),		addIRI(getSuperValue()), result.getHTMLValue()));
					}
					returnResult = result;
				}
			} else {
				//addTrace(String.format("Retrieved literal %s of %s = %s", addIRI(predicate),	addIRI(getSuperValue()), getHTMLValue(objectValue)));
				returnResult = pathQLModel.Resource.create(null, objectValue,getEvaluationContext());
			}
		}
		return returnResult;
	}

	public  void clearCache(Value... args) {
		factCache.clear();
	}

	public FactCache getFactCache() {
		return factCache;
	}

}
