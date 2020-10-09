package olgap;

import java.util.Date;
import java.util.HashMap;
import java.util.Stack;

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
	static private HashMap<String, HashMap<String, olgap.Value>> facts = new HashMap<String, HashMap<String, olgap.Value>>();
	static private HashMap<String, CompiledScript> compiledScripts = new HashMap<String, CompiledScript>();
	static private HashMap<String, SEEQSource> seeqSources = new HashMap<String, SEEQSource>();
	static private IRI attributeOfItem;
	static private IRI attributeProperty;
	static private IRI attributeProducedBySignal;

	public Source(TripleSource tripleSource) {
		Source.tripleSource = tripleSource;
		Source.modelBuilder = new ModelBuilder();
		attributeOfItem = Source.getTripleSource().getValueFactory()
				.createIRI("http://inova8.com/calc2graph/def/attribute.of.Item");
		attributeProperty = Source.getTripleSource().getValueFactory()
				.createIRI("http://inova8.com/calc2graph/def/attribute.Property");
		attributeProducedBySignal = Source.getTripleSource().getValueFactory()
				.createIRI("http://inova8.com/calc2graph/def/attribute.producedBy.Signal");
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

	public Thing thingFactory(Resource subject, Stack<String> stack) {
		return thingFactory(null, subject, stack);
	}

	public Thing thingFactory(Tracer tracer, Resource subject, Stack<String> stack) {
		HashMap<String, olgap.Value> values;
		if (facts.containsKey(subject.toString())) {
			values = facts.get(subject.toString());
			//	thing.setTracer(tracer);
			//	return thing;
		} else {
			values = new HashMap<String, olgap.Value>();
			facts.put(subject.toString(), values);
		}
		olgap.Thing newThing = new Thing(this, subject);
		newThing.setTracer(tracer);
		newThing.setStack(stack);
		newThing.setValues(values);
		return newThing;

	}
	public olgap.Value valueFactory(Tracer tracer, String value, Stack<String> stack) {
		return this.valueFactory(tracer, Source.getTripleSource().getValueFactory().createLiteral(value),stack);
	}

	public olgap.Value valueFactory(Tracer tracer, Value value, Stack<String> stack) {
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
			return thingFactory(tracer, (IRI) value, stack);
		}
	}

	public HashMap<String, olgap.Value> getCustomQueryOptions(Value[] customQueryOptionsArray) {
		if (customQueryOptionsArray.length == 0) {
			return null;
		} else {
			HashMap<String, olgap.Value> customQueryOptions = new HashMap<String, olgap.Value>();
			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
				customQueryOptions.put(customQueryOptionParameter,
						valueFactory(null, customQueryOptionsArray[customQueryOptionsArrayIndex + 1], null));//TODO
				if (customQueryOptionParameter.equals("service")) {
					String service = customQueryOptions.get("service").getValue().toString();
					service = service.substring(0, service.indexOf('?'));
					setCacheService(service);
				}
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
				if (scriptEngine != null) {
					CompiledScript compiledScriptCode;
					compiledScriptCode = ((Compilable) scriptEngine).compile(scriptCode);
					compiledScripts.put(scriptCode.toString(), compiledScriptCode);
					return compiledScriptCode;
				} else {
					throw new ScriptException(
							"Unrecognized script language:" + scriptString.getDatatype().getLocalName());
				}
			} catch (ScriptException e) {
				//logger.error(String.format("Failed to compile '%s' language  script of  with contents:\n %s",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				logger.error(new ParameterizedMessage("Failed to compile '{}' language  script of  with contents:\n {}",
						scriptString.getDatatype().getLocalName(), scriptString.toString()));
				throw e;
			}
		}
	}
	public SEEQSource seeqSourceFactory(String seeqServer) throws ScriptException {
		if (seeqSources.containsKey(seeqServer)) {
			return seeqSources.get(seeqServer);
		} else {
			try {
				SEEQSource seeqSource = new SEEQSource("http://" + seeqServer + "/api","peter.lawrence@inova8.com","lusterthief");
				seeqSources.put(seeqServer, seeqSource);
		        return seeqSource;
			} catch (Exception e) {
				//logger.error(String.format("Failed to compile '%s' language  script of  with contents:\n %s",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				logger.error(new ParameterizedMessage("Failed to connect to SEEQSource '{}'",
						seeqServer));
				throw e;
			}
		}
	}
	public static void clearCache() {
		things.clear();
	}

	public void writeModelToCache(Thing thing, Object result, IRI cacheContext) {
		if (connected()) {
			try {
				cacheConnection.clear(cacheContext);
				Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/olgap/cacheDateTime");
				((Model) result).add(cacheContext,
						Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/olgap/cacheDateTime"),
						Source.getTripleSource().getValueFactory().createLiteral(new Date()), cacheContext);
				cacheConnection.add((Model) result, cacheContext);
				thing.addTrace(new ParameterizedMessage("Results cached to service {} in graph {}",
						addService(Source.getCacheService()), addService(cacheContext.stringValue())));
			} catch (Exception e) {
				logger.error(new ParameterizedMessage(
						"Failed to write results to cache  {} with context \n {} with exception {}", result.toString(),
						cacheContext), e);
				thing.addTrace(new ParameterizedMessage("Results NOT cached to service:{}",
						addService(Source.getCacheService())));
			}
		} else {
			thing.addTrace(new ParameterizedMessage("No service to cached results"));
		}
	}

	private String addService(String service) {
		return "<a href='" + service + "' target='_blank'>" + service + "</a>";
	}

	private boolean connected() {
		if (cacheConnection != null && cacheConnection.isActive()) {
			return true;
		} else {
			try {
				Repository rep = new HTTPRepository(cacheService);
				rep.init();
				cacheConnection = rep.getConnection();
				return true;
			} catch (Exception e) {
				logger.error("Failed to create connection to cache:" + cacheService);
				return false;
			}
		}
	}

	public static IRI getAttributeOfItem() {
		return attributeOfItem;
	}

	public static IRI getAttributeProperty() {
		return attributeProperty;
	}

	public static IRI getAttributeProducedBySignal() {
		return attributeProducedBySignal;
	}
}
