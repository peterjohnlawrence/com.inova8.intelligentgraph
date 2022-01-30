/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.script.CompiledScript;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.exceptions.CircularReferenceException;
import com.inova8.intelligentgraph.exceptions.HandledException;
import com.inova8.intelligentgraph.exceptions.NullValueReturnedException;
import com.inova8.intelligentgraph.exceptions.ScriptFailedException;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.sail.IntelligentGraphSail;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;

/**
 * The Class IntelligentEvaluator.
 */
public class IntelligentEvaluator {
	
	/** The fact cache. */
	private final  FactCache factCache=new FactCache();
	
	/** The Constant logger. */
	protected final static Logger logger = LoggerFactory.getLogger(IntelligentEvaluator.class);

	/**
	 * Instantiates a new intelligent evaluator.
	 *
	 * @param intelligentGraphSail the intelligent graph sail
	 */
	public IntelligentEvaluator(IntelligentGraphSail intelligentGraphSail) {
	}

	/**
	 * Clear cache.
	 *
	 * @param args the args
	 */
	public  void clearCache(Value... args) {
		factCache.setDirty(true);
	}

	/**
	 * Gets the fact cache.
	 *
	 * @return the fact cache
	 */
	public FactCache getFactCache() {
		return factCache;
	}

	/**
	 * Handle script.
	 *
	 * @param thing the thing
	 * @param scriptString the script string
	 * @param predicate the predicate
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 * @return the resource
	 * @throws HandledException the handled exception
	 */
	protected static  Resource handleScript(Thing thing,SimpleLiteral scriptString, IRI predicate, CustomQueryOptions customQueryOptions,org.eclipse.rdf4j.model.Resource ... contexts) throws HandledException{
		if (predicate.equals(SCRIPT.SCRIPTCODE)) {
			return Resource.create(thing.getSource(), scriptString, thing.getEvaluationContext());
		} else {
			String scriptCode = scriptString.getLabel();
			scriptCode = scriptCode.trim();
			if (scriptCode.startsWith("<")) {
				String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
				org.eclipse.rdf4j.model.Resource scriptResource =  thing.getSource().getRepositoryContext().convertQName(scriptIRI,  thing.getSource().getPrefixes());//convertQName(scriptIRI);
				Statement scriptStatement;
				SimpleLiteral scriptCodeliteral = null;
				try {
					CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements =  thing.getSource()
							.getTripleSource().getStatements(scriptResource, SCRIPT.SCRIPTCODE, null);
	
					while (scriptStatements.hasNext()) {
						scriptStatement = scriptStatements.next();
						scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
					}
				} catch (QueryEvaluationException qe) {
					 thing.getEvaluationContext().getTracer().traceRedirectingFailed( thing, predicate, scriptCode);
					throw new ScriptFailedException( String.format(
							"Reference script <%s> not found for  %s of  subject %s", scriptIRI, predicate, thing), qe);
				}
				if (scriptCodeliteral != null) {
					 thing.getEvaluationContext().getTracer().traceRedirecting( thing, predicate, scriptCode);
					return handleScript(thing,scriptCodeliteral, predicate,customQueryOptions, contexts);
				} else {
					 thing.getEvaluationContext().getTracer().traceRedirectingFailed( thing, predicate, scriptCode);
					throw new ScriptFailedException( String.format(
							"Reference script null <%s> for %s of subject %s", scriptIRI, predicate, thing));
				}
			} else {
				 thing.getEvaluationContext().getTracer().incrementLevel();
				IRI cacheContextIRI =  thing.generateCacheContext(predicate);				
				 thing.getEvaluationContext().getTracer().traceEvaluating( thing, predicate, scriptString);
	
				Object scriptResult = null;
				try {
					//Test to see if the same 'call' is on the stack
					//If so report that circular reference encountered
					//If not push on stack
					String stackKey = generateStackKey(thing,predicate);
					if (! thing.searchStack(stackKey)) {
						
						CompiledScript compiledScriptCode =  thing.getSource().compiledScriptFactory(scriptString);
						SimpleBindings scriptBindings = new SimpleBindings();
						Object _result = null;
						scriptBindings.put("_result", _result);
						scriptBindings.put("_this", thing);
						scriptBindings.put("_customQueryOptions",customQueryOptions);
	
						Resource result;
						try {
							 thing.pushStack(stackKey);
							scriptResult = compiledScriptCode.eval(scriptBindings);
							if(scriptResult==null) {
								//Could be Python which will not return a result by default
								scriptResult=scriptBindings.get("_result");
							}
							result = returnResult(thing,scriptResult, cacheContextIRI);
						}
						finally {
							//Since script complete, pop from stack
							 thing.popStack();
						}
	
						 thing.getEvaluationContext().getTracer().decrementLevel();
						if (result != null)
							 thing.getEvaluationContext().getTracer().traceEvaluated( thing, predicate, result);
						else {
							throw new NullValueReturnedException(
									String.format("Evaluated null for %s of %s, using script %s", predicate,
											 thing.getSuperValue(), scriptString));
						}
						 thing.getEvaluationContext().getTracer().decrementLevel();
						return result;
					} else {
						 thing.getEvaluationContext().getTracer().traceCircularReference( thing, predicate,  thing.getStack(), stackKey);
						logger.error(
								"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
								predicate.stringValue(), ((IRI)  thing.getSuperValue()).stringValue(),
								 thing.getStack().subList( thing.getStack().size() -  thing.getStack().search(stackKey),  thing.getStack().size())
										.toString());
						 throw new CircularReferenceException(String.format(
								"Circular reference encountered when evaluating <%s> of <%s>.\r\n%s",
								predicate.stringValue(), ((IRI)  thing.getSuperValue()).stringValue(),  thing.getStack()
										.subList( thing.getStack().size() -  thing.getStack().search(stackKey),  thing.getStack().size())));
					}
				} catch (ScriptException e) {
					 thing.getEvaluationContext().getTracer().decrementLevel();
					logger.error(String.format(
							"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
							StringEscapeUtils.escapeHtml4(e.getMessage())));
					 thing.getEvaluationContext().getTracer().traceScriptError(e);
					throw new ScriptFailedException( e);
	
				}
			}
		}
	
	}

	/**
	 * Process fact object value.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param objectValue the object value
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 * @return the resource
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	public static Resource processFactObjectValue(Thing thing,IRI predicate, Value objectValue, CustomQueryOptions customQueryOptions, org.eclipse.rdf4j.model.Resource ... contexts) throws QueryEvaluationException {
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
				FactCache factCache = thing.getSource().getIntelligentGraphConnection().getIntelligentGraphSail().getFactCache();
				if (/*notTracing() && */factCache.contains(thing.getIRI(), predicate, customQueryOptionsContext)) {
					Value resultValue =  factCache.getFacts(thing.getIRI(), predicate, customQueryOptionsContext);
					logger.debug("Retrieved cache {} of {} = {}", predicate.stringValue(),
							thing.getSuperValue().stringValue(), resultValue.stringValue());
					thing.getEvaluationContext().getTracer().traceRetrievedCache(thing,predicate, resultValue);
					returnResult = Resource.create(thing.getSource(), resultValue, thing.getEvaluationContext());
				} else {
					Resource result = handleScript(thing,literalValue, predicate,customQueryOptions,contexts);
					if (result != null) {
						//TODO validate caching
						factCache.addFact(thing.getIRI(), predicate,result.getSuperValue() ,customQueryOptionsContext);
						thing.getEvaluationContext().getTracer().traceCalculated(thing,predicate, result);
					}
					returnResult = result;
				}
			} else {
				thing.getEvaluationContext().getTracer().traceRetrievedLiteral(thing,predicate, objectValue);
				returnResult = Resource.create(thing.getSource(), objectValue, thing.getEvaluationContext());
			}
		}
		return returnResult;
	}

	/**
	 * Generate stack key.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @return the string
	 */
	@SuppressWarnings("deprecation")
	public static String generateStackKey(Thing thing,IRI predicate) {
		String stackKey;
		if (predicate != null) {
			stackKey = "<" + predicate.stringValue() + "> <" + thing.toString() + ">; queryOptions="
					+ (thing.getCustomQueryOptions() == null ? "" : thing.getCustomQueryOptions().toString()) + "\r\n";
		} else {
			stackKey = "<NULL> <" + thing.toString() + ">; queryOptions="
					+ (thing.getCustomQueryOptions() == null ? "" : thing.getCustomQueryOptions().toString()) + "\r\n";
		}
		return stackKey;
	}

	/**
	 * Return result.
	 *
	 * @param thing the thing
	 * @param result the result
	 * @param cacheContextIRI the cache context IRI
	 * @return the resource
	 */
	public static Resource returnResult(Thing thing,Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "NullValue":
				return Resource.create(thing.getSource(), literal((String) "null"), thing.getEvaluationContext());
			case "Integer":
				return Resource.create(thing.getSource(), literal((Integer) result), thing.getEvaluationContext());
			case "Double":
				return Resource.create(thing.getSource(), literal((Double) result), thing.getEvaluationContext());
			case "Float":
				return Resource.create(thing.getSource(), literal((Float) result), thing.getEvaluationContext());
			case "decimal":
				return Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigDecimal":
				return Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigInteger":
				return Resource.create(thing.getSource(), literal((BigInteger) result), thing.getEvaluationContext());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				thing.getSource().writeModelToCache(thing, result, cacheContextIRI);
				return Thing.create(thing.getSource(), cacheContextIRI, thing.getEvaluationContext());
			case "Literal":
				Value content = ((com.inova8.intelligentgraph.model.Literal) result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
				case "int":
				case "integer":
					return Resource.create(thing.getSource(),
							literal((BigInteger) ((com.inova8.intelligentgraph.model.Literal) result).bigIntegerValue()),
							thing.getEvaluationContext());
				case "decimal":
					return Resource.create(thing.getSource(), literal(((com.inova8.intelligentgraph.model.Literal) result).decimalValue()),
							thing.getEvaluationContext());
				case "double":
					return Resource.create(thing.getSource(), literal(((com.inova8.intelligentgraph.model.Literal) result).doubleValue()),
							thing.getEvaluationContext());
				case "string": 
					return Resource.create(thing.getSource(), literal(content.stringValue()), thing.getEvaluationContext());
				case "time": 
					return Resource.create(thing.getSource(), literal(content), thing.getEvaluationContext());
				case "date": 
					return Resource.create(thing.getSource(), literal(content), thing.getEvaluationContext());
				case "dateTime": 
					return Resource.create(thing.getSource(), literal(content), thing.getEvaluationContext());
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),
							((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName());
					return Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
				}
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
			}
		} else {
			return null;
		}
	}

}
