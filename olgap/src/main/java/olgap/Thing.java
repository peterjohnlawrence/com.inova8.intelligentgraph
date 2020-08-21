package olgap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.logging.log4j.message.FormattedMessage;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class Thing extends olgap.Value
{
	TripleSource tripleSource;
	private Evaluator host;
	private HashMap<String, olgap.Value> values = new HashMap<String,olgap.Value>();
	
	public Thing(Evaluator host,TripleSource tripleSource,  org.eclipse.rdf4j.model.Value superValue ) {
		this.host = host;
		this.superValue = superValue;
		this.tripleSource = tripleSource;
	}

	public String toString() {
		return superValue.toString();
	}

	public HashSet<olgap.Value> getFacts(IRI predicate, Value ... arguments) {
		//logger.traceEntry(new FormattedMessage("GetObjects <{}> of subject <{}>", predicate,superValue));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = tripleSource
				.getStatements((IRI) superValue, predicate, null);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(this.host.valueFactory(objectStatement.getObject()));
			//objects.add(new  olgap.Value(tripleSource, shell, objectStatement.getObject()));
		}
		logger.traceExit(new FormattedMessage("Retrieving objects <{}> of <{}> = {}",predicate,superValue,  values));
		return values;
	}
	public HashSet<olgap.Value> getFacts(String predicateIRI, Value ... args) {
		IRI predicate = tripleSource.getValueFactory().createIRI(predicateIRI);	
		return this.getFacts(predicate,args);
	}
	
	public HashSet<olgap.Value> getIsFactsOf(IRI predicate, Value ... arguments) {
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = tripleSource
				.getStatements(null, predicate, (IRI) superValue);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(this.host.valueFactory(objectStatement.getSubject()));

		}
		logger.traceExit(new FormattedMessage("Retrieving subjects <{}> of <{}> = {}",predicate,superValue,  values));
		return values;
	}
	public HashSet<olgap.Value> getIsFactsOf(String predicateIRI, Value ... args) {
		IRI predicate = tripleSource.getValueFactory().createIRI(predicateIRI);	
		return this.getIsFactsOf(predicate,args);
	}
	public olgap.Value getFact(IRI predicate, SimpleLiteral scriptString, Value ... arguments) {
		olgap.Value result = this.handleScript( scriptString, predicate, arguments);
		return result;		
		
	}
	public olgap.Value getFact(IRI predicate, Value ... arguments) {
		String key =predicate.toString() + Arrays.toString(arguments);
		if( values.containsKey(key)) {
			olgap.Value result = values.get(key);
			logger.traceExit(new FormattedMessage("Cache <{}> of <{}> = {}",predicate,superValue,result.getValue()));
			return result;			
		}else {
			CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = tripleSource
					.getStatements((IRI) superValue, predicate, null);
			while (objectStatements.hasNext()) {
				Statement objectStatement = objectStatements.next();
				Value objectValue = objectStatement.getObject();
				SimpleLiteral literalValue;
				try{
					literalValue = (SimpleLiteral)objectValue;
					if( host.scriptEngines.containsKey(literalValue.getDatatype().getLocalName()) ) {
						olgap.Value  result =  getFact(predicate,literalValue, arguments) ;
						if(result!=null) {
							logger.traceExit(new FormattedMessage("Evaluating <{}> of <{}> = {}",predicate,superValue, result.getValue()));
							values.put(key, result);
						}
						return result;					
					}else {
						logger.traceExit(new FormattedMessage("Literal <{}> of <{}> = {}",predicate,superValue, objectValue));
						return this.host.valueFactory( objectValue);
					}
				}catch(Exception e) {
					logger.traceExit(new FormattedMessage("Resource <{}> of <{}> = {}",predicate,superValue, objectValue));
					return this.host.valueFactory( objectValue);
				}
			}
			logger.error(new FormattedMessage("No predicate {} found for subject {}", predicate, this));
			return null;
		}
	}
	public olgap.Value getFact(String predicateIRI, Value ... arguments) {
		IRI predicate = tripleSource.getValueFactory().createIRI(predicateIRI);	
		return this.getFact(predicate,arguments);
	}

	protected olgap.Value handleScript(SimpleLiteral scriptString, IRI predicate, Value ...  arguments) {
		String scriptCode = scriptString.getLabel();
		scriptCode=scriptCode.trim();
		if(scriptCode.startsWith("$$")) {
			String scriptIRI = scriptCode.substring(2);
			Resource scriptResource = tripleSource.getValueFactory().createIRI(scriptIRI);
			IRI scriptPropertyIRI = tripleSource.getValueFactory().createIRI("http://inova8.com/calc2graph/def/scriptCode");
			CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = tripleSource
					.getStatements(scriptResource, scriptPropertyIRI, null);
			while (scriptStatements.hasNext()) {
				Statement scriptStatement = scriptStatements.next();
				SimpleLiteral scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
				return handleScript(scriptCodeliteral, predicate, arguments);
			}
			logger.error(new FormattedMessage("Reference script <{}> not found for subject {}", scriptIRI, this));
		}
		IRI cacheContextIRI = generateCacheContext(predicate, arguments);
		
		ScriptEngine groovyScriptEngine = 	Evaluator.scriptEngines.get(scriptString.getDatatype().getLocalName());
		groovyScriptEngine.put("$tripleSource", tripleSource);
		groovyScriptEngine.put("$this", this);
		groovyScriptEngine.put("$property", this.host.thingFactory( predicate));
		groovyScriptEngine.put("$args", arguments);
		groovyScriptEngine.put("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI));
		Object scriptResult = null;
		try {
			scriptResult = groovyScriptEngine.eval(scriptCode);
			olgap.Value result = returnResult(scriptResult, cacheContextIRI);
			logger.traceExit(new FormattedMessage("Script Result {}",result.getValue()));
			return result;
		} catch (ScriptException e) {
			logger.traceExit(new FormattedMessage("Script failed with {}",e.getMessage()));
			return null;
		}
/* Use GroovyShell directly instead of via Javax JSR223
		
		Script script = shell.parse(scriptCode);
		Binding binding = new Binding();
		binding.setVariable("$tripleSource", tripleSource);
		binding.setVariable("$this", this);
		binding.setVariable("$property", this.host.thingFactory( predicate));
		binding.setVariable("$args", arguments);

		binding.setVariable("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI));
		script.setBinding(binding);
		logger.traceEntry(new FormattedMessage("Evaluating predicate <{}> of <{}>, by invoking script\n\n{}\n", predicate,superValue, scriptString));

		try {
			Object scriptResult = script.run();
			olgap.Value result = returnResult(scriptResult, cacheContextIRI);
			logger.traceExit(new FormattedMessage("Script Result {}",result.getValue()));
			return result;
		}catch(Exception e) {
			logger.traceExit(new FormattedMessage("Script failed with {}",e.getMessage()));
			return null;
		}
*/
	}

	private IRI generateCacheContext(IRI predicate, Value... arguments) {
		String key = superValue.toString() + predicate.toString() + Arrays.toString(arguments);
		String cacheContext = Evaluator.bytesToHex(Evaluator.digest.digest(key.getBytes()));
		IRI cacheContextIRI = this.tripleSource.getValueFactory().createIRI("http://inova8.com/cat2graph/data/", cacheContext);
		return cacheContextIRI;
	}

	public  olgap.Value returnResult(Object result, IRI cacheContext) {
		switch (result.getClass().getSimpleName()) {
		case "Integer":
			return this.host.valueFactory(this.tripleSource.getValueFactory().createLiteral((Integer)result));
		case "Double":
			return this.host.valueFactory(this.tripleSource.getValueFactory().createLiteral((Double)result));
		case "Thing":
			return (Thing)result;
		case "LinkedHashModel":
			writeModelToCache(result, cacheContext);
			return this.host.thingFactory(cacheContext);		
		default:
			logger.error(new FormattedMessage("No handler found for result {} of class {}", result,result.getClass().getSimpleName()));
			break;
		}
		
		return null;		 
	}

	private void writeModelToCache(Object result, IRI cacheContext) {
		this.host.cacheConn.add((Model) result);
	}

	public String getLabel() {
		return superValue.stringValue();
	}
	public Value getValue() {
		return superValue;
	}
}
