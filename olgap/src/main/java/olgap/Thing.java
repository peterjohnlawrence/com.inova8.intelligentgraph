package olgap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;

public class Thing extends olgap.Value
{
	Source source;
	private HashMap<String, olgap.Value> values = new HashMap<String,olgap.Value>();
	

	public Thing(Source source,  org.eclipse.rdf4j.model.Value superValue ) {
		this.superValue = superValue;
		this.source = source;
	}
	public String toString() {
		return superValue.toString();
	}
	@SafeVarargs
	public final HashSet<olgap.Value> getFacts(IRI predicate, HashMap<String,olgap.Value>... customQueryOptions) {
		addTrace(new ParameterizedMessage("Seeking values {} of subject {}", addIRI(predicate),addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements((IRI) superValue, predicate, null);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(), objectStatement.getObject()));
			//objects.add(new  olgap.Value(tripleSource, shell, objectStatement.getObject()));
		}
		addTrace(new ParameterizedMessage("Retrieved objects {} of {} = {}",addIRI(predicate),addIRI(superValue),  values));
		return values;
	}
	@SafeVarargs
	public final HashSet<olgap.Value> getFacts(String predicateIRI, HashMap<String,olgap.Value>... customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);	
		return this.getFacts(predicate,customQueryOptions);
	}
	@SafeVarargs
	public final HashSet<olgap.Value> getIsFactsOf(IRI predicate, HashMap<String,olgap.Value>... customQueryOptions) {
		addTrace(new ParameterizedMessage("Seeking subjects {} of {}",addIRI(predicate),addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements(null, predicate, (IRI) superValue);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(),objectStatement.getSubject()));
		}
		addTrace(new ParameterizedMessage("Retrieved subjects {} of {} = {}",addIRI(predicate),addIRI(superValue),  values));
		return values;
	}
	@SafeVarargs
	public final HashSet<olgap.Value> getIsFactsOf(String predicateIRI,  HashMap<String,olgap.Value>... customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);	
		return this.getIsFactsOf(predicate,customQueryOptions);
	}
	@SafeVarargs
	public final olgap.Value getFact(IRI predicate, SimpleLiteral scriptString, HashMap<String, olgap.Value>... customQueryOptions) {
		olgap.Value result = this.handleScript( scriptString, predicate, customQueryOptions);
		return result;		
		
	}
	@SafeVarargs
	public final olgap.Value getFact(IRI predicate, HashMap<String,olgap.Value>... customQueryOptions) {
		String key =predicate.toString() +  StringUtils.join(customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}",addIRI(predicate),addIRI(superValue),customQueryOptions));
		if(notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}",addIRI(predicate),addIRI(superValue),result.getValue()));
			return result;			
		}else {
			CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
					.getStatements((IRI) superValue, predicate, null);
			while (objectStatements.hasNext()) {
				Statement objectStatement = objectStatements.next();
				Value objectValue = objectStatement.getObject();
				SimpleLiteral literalValue;
				try{
					literalValue = (SimpleLiteral)objectValue;
					if( Evaluator.scriptEngines.containsKey(literalValue.getDatatype().getLocalName()) ) {

						olgap.Value  result =  getFact(predicate,literalValue, customQueryOptions) ;
						if(result!=null) {
							
							values.put(key, result);
							addTrace(new ParameterizedMessage("Calculated {} of {} = {}",addIRI(predicate),addIRI(superValue),result.getValue()));
						}
						return result;					
					}else {
						addTrace(new ParameterizedMessage("Retrieved literal {} of {} = {}",addIRI(predicate),addIRI(superValue), objectValue));
						return source.valueFactory( getTracer(),objectValue);
					}
				}catch(Exception e) {
					addTrace(new ParameterizedMessage("Retrieved resource {} of {} = {}",addIRI(predicate),addIRI(superValue), objectValue));
					return source.valueFactory(getTracer(), objectValue);
				}
			}
			addTrace(new ParameterizedMessage("Error: No predicate {} found for subject {}", addIRI(predicate), addThisIRI()));
			decrementTraceLevel();
			return null;
		}
	}
	@SafeVarargs
	public final olgap.Value getFact(String predicateIRI, HashMap<String,olgap.Value>...  customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);	
		return this.getFact(predicate,customQueryOptions);
	}
	@SafeVarargs
	protected final olgap.Value handleScript(SimpleLiteral scriptString, IRI predicate, HashMap<String, olgap.Value>...  customQueryOptions) {
		String scriptCode = scriptString.getLabel();
		scriptCode=scriptCode.trim();
		if(scriptCode.startsWith("$$")) {
			String scriptIRI = scriptCode.substring(2);
			Resource scriptResource = Source.getTripleSource().getValueFactory().createIRI(scriptIRI);
			IRI scriptPropertyIRI = Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/calc2graph/def/scriptCode");
			CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = Source.getTripleSource()
					.getStatements(scriptResource, scriptPropertyIRI, null);
			while (scriptStatements.hasNext()) {
				Statement scriptStatement = scriptStatements.next();
				SimpleLiteral scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
				return handleScript(scriptCodeliteral, predicate, customQueryOptions);
			}
			logger.error(new ParameterizedMessage("Reference script <{}> not found for subject {}", scriptIRI, this));
		}else {
			incrementTraceLevel();
			IRI cacheContextIRI = generateCacheContext(predicate, customQueryOptions);
			
			ScriptContext scriptContext = new SimpleScriptContext();
			scriptContext.setAttribute("$tripleSource", Source.getTripleSource(),ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("$this", this,ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("$property", source.thingFactory(getTracer(), predicate),ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("$args", customQueryOptions,ScriptContext.ENGINE_SCOPE);
			scriptContext.setAttribute("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI),ScriptContext.ENGINE_SCOPE);
			String scripttype = scriptString.getDatatype().getLocalName();
			//addTrace(String.format("Evaluating predicate %s of %s, by invoking <b>%s</b> script\n",addIRI(predicate),addIRI(superValue),scripttype));
			addTrace(new ParameterizedMessage("Evaluating predicate {} of {}, by invoking <b>{}</b> script\n",addIRI(predicate),addIRI(superValue),scripttype));
			addScript(scriptString.getLabel());
			incrementTraceLevel();
			Object scriptResult = null;
			try {
				CompiledScript compiledScriptCode= source.compiledScriptFactory(scriptString);
				scriptResult = compiledScriptCode.eval(scriptContext);
				olgap.Value result = returnResult(scriptResult, cacheContextIRI);
				decrementTraceLevel();
				//addTrace(String.format("Evaluated %s of %s =  %s",addIRI(predicate),addIRI(superValue),result.getHTMLValue()));
				addTrace(new ParameterizedMessage("Evaluated {} of {} =  {}",addIRI(predicate),addIRI(superValue),result.getHTMLValue()));
				decrementTraceLevel();
				return result;
			} catch (ScriptException e) {
				decrementTraceLevel();
				//addTrace(String.format("Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >", StringEscapeUtils.escapeHtml4(e.getMessage())));
				addTrace(new ParameterizedMessage("Script failed with <br/><code ><span style=\"white-space: pre-wrap\">{}</span></code >",StringEscapeUtils.escapeHtml4(e.getMessage())));				
				return null;
			}
		}
		return null;
	}


	@SafeVarargs
	private final IRI generateCacheContext(IRI predicate, HashMap<String, olgap.Value>... customQueryOptions) {
		String key = superValue.toString() + predicate.toString() + StringUtils.join(customQueryOptions);
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI = Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/cat2graph/data/", cacheContext);
		return cacheContextIRI;
	}

	public  olgap.Value returnResult(Object result, IRI cacheContextIRI) {
		switch (result.getClass().getSimpleName()) {
		case "Integer":
			return source.valueFactory(getTracer(),Source.getTripleSource().getValueFactory().createLiteral((Integer)result));
		case "Double":
			return source.valueFactory(getTracer(),Source.getTripleSource().getValueFactory().createLiteral((Double)result));
		case "BigDecimal":
			return source.valueFactory(getTracer(),Source.getTripleSource().getValueFactory().createLiteral((BigDecimal)result));
		case "Thing":
			return (Thing)result;
		case "LinkedHashModel":
			source.writeModelToCache(result, cacheContextIRI);
			return source.thingFactory(getTracer(),cacheContextIRI);		
		default:
			logger.error("No handler found for result {} of class {}", result.toString(),result.getClass().getSimpleName());
			break;
		}
		return null;		 
	}
	public void writeResultToCache(){
//		source.writeModelToCache(result, cacheContext);
		
	}

	public String getLabel() {
		return superValue.stringValue();
	}
	public Value getValue() {
		return superValue;
	}
}
