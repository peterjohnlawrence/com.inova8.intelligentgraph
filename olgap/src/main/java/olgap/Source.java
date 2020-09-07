package olgap;

import java.util.Date;
import java.util.HashMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public class Source {
	static private final Logger logger = LogManager.getLogger(Source.class);
	static private Repository cacheRep;
	static private String cacheService;
	static private RepositoryConnection cacheConnection;
	static private TripleSource tripleSource; //Not unique per call using the same underlying triplestore
	static private ModelBuilder modelBuilder;
	static private HashMap<String, Thing> things = new HashMap<String, Thing>();
	static private HashMap<String, CompiledScript> compiledScripts = new HashMap<String, CompiledScript>();

	public Source(TripleSource tripleSource) {
		Source.tripleSource = tripleSource;
		Source.modelBuilder = new ModelBuilder();
	}

	public static Repository getCacheRep() {
		return cacheRep;
	}

	public static void setCacheRep(Repository cacheRep) {
		Source.cacheRep = cacheRep;
	}

	public static String getCacheService() {
		return cacheService;
	}

	public static void setCacheService(String cacheService) {
		Source.cacheService = cacheService;
	}

	public static TripleSource getTripleSource() {
		return tripleSource;
	}

	public static void setTripleSource(TripleSource tripleSource) {
		Source.tripleSource = tripleSource;
	}

	public static ModelBuilder getModelBuilder() {
		return modelBuilder;
	}
	public Thing thingFactory(Resource subject) {
		return thingFactory(null, subject);
	}
	public Thing thingFactory(Tracer tracer, Resource subject) {
		if (things.containsKey(subject.toString())) {
			Thing thing = things.get(subject.toString());
			thing.setTracer(tracer);
			return thing;
		} else {
			olgap.Thing newThing = new Thing(this, subject);
			newThing.setTracer(tracer);
			things.put(subject.toString(), newThing);
			return newThing;
		}
	}

	public olgap.Value valueFactory(Tracer tracer, Value value) {
		switch (value.getClass().getSimpleName()) {
		case "SimpleLiteral":
		case "BooleanLiteral":
		case "BooleanLiteralImpl":
		case "CalendarLiteral":
		case "DecimalLiteral":
		case "IntegerLiteral":
		case "MemLiteral":
		case "BooleanMemLiteral":
		case "CalendarMemLiteral":
		case "DecimalMemLiteral":
		case "IntegerMemLiteral":
		case "NumericMemLiteral":
		case "NativeLiteral":
		case "NumericLiteral":
			return new olgap.Literal(value);
		default:
			//logger.error(new ParameterizedMessage("No handler found for objectvalue class {}",value.getClass().getSimpleName()));
			return thingFactory(tracer, (IRI) value);
		}
	}
	public HashMap<String, olgap.Value> getCustomQueryOptions(Value[] customQueryOptionsArray){
		if (customQueryOptionsArray.length == 0) {
			return null;
		} else {
			HashMap<String, olgap.Value> customQueryOptions = new HashMap<String,olgap.Value>();
			for ( int customQueryOptionsArrayIndex = 0;customQueryOptionsArrayIndex<customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2 ) {	
				
				customQueryOptions.put(customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue(), valueFactory(null, customQueryOptionsArray[customQueryOptionsArrayIndex+1]));
			}
			return customQueryOptions;
		}		
	}
	public CompiledScript compiledScriptFactory(SimpleLiteral scriptString) throws ScriptException {
		String scriptCode = scriptString.getLabel();
		if (compiledScripts.containsKey(scriptCode)) {
			return compiledScripts.get(scriptCode);
		} else {
			try {
				ScriptEngine scriptEngine = Evaluator.scriptEngines.get(scriptString.getDatatype().getLocalName());
				if(scriptEngine!=null ) {
					CompiledScript compiledScriptCode;
					compiledScriptCode = ((Compilable) scriptEngine).compile(scriptCode);
					compiledScripts.put(scriptCode.toString(), compiledScriptCode);
					return compiledScriptCode;
				}else {
					throw new  ScriptException("Unrecognized script language:" + scriptString.getDatatype().getLocalName());
				}
			} catch (ScriptException e) {
				//logger.error(String.format("Failed to compile '%s' language  script of  with contents:\n %s",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				logger.error(new ParameterizedMessage("Failed to compile '{}' language  script of  with contents:\n {}",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				throw e;
			}
		}
	}

	public static void clearCache() {
		things.clear();
	}

	public void writeModelToCache(Object result, IRI cacheContext) {
		if(connected()) {
			try {
				cacheConnection.clear(cacheContext);
				Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/olgap/cacheDateTime");
				((Model) result).add(cacheContext, Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/olgap/cacheDateTime"), Source.getTripleSource().getValueFactory().createLiteral(new Date()), cacheContext);
				cacheConnection.add((Model) result,cacheContext);
			}catch(Exception e) {
				//logger.error(String.format("Failed to write results to cache %s with context \n %s with exception %s", "Failed to write results to cache  {} with context \n {} with exception {}"));
				logger.error(new ParameterizedMessage("Failed to write results to cache  {} with context \n {} with exception {}",	result.toString(), cacheContext),e);
			}
		}
	}

	private boolean connected() {
		if(cacheConnection!=null && cacheConnection.isActive()) {
			return true;
		}else {
			try {
				String rdf4jServer = "http://localhost:8080/rdf4j-server/repositories/";
				String repositoryID = "olgap";
				Repository rep = new HTTPRepository(rdf4jServer+repositoryID);
				//cacheService ="SERVICE <" + rdf4jServer + "repositories/" + repositoryID + "?distinct=true&infer=false>";
				rep.init();
				cacheConnection = rep.getConnection();
				return true;
			}catch(Exception e) {
				logger.error("Failed to create connection to cache Repository");
				return false;
			}
		}
	}
}
