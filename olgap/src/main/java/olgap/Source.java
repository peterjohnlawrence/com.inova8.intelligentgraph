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
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
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
	static private HashMap<String, ReificationType> reificationTypes = new HashMap<String, ReificationType>();
	static private HashMap<String, ReificationType> predicateReificationTypes = new HashMap<String, ReificationType>();
	//	static private IRI reificationSubject;
	//	static private IRI reificationPredicate;
	//	static private IRI reificationObject;

	public Source(TripleSource tripleSource) {
		Source.tripleSource = tripleSource;
		Source.modelBuilder = new ModelBuilder();
		initializeReificationTypes();

		//		reificationSubject = Source.getTripleSource().getValueFactory()
		//				.createIRI("http://inova8.com/calc2graph/def/attribute.of.Item");
		//		reificationPredicate = Source.getTripleSource().getValueFactory()
		//				.createIRI("http://inova8.com/calc2graph/def/attribute.Property");
		//		reificationObject = Source.getTripleSource().getValueFactory()
		//				.createIRI("http://inova8.com/calc2graph/def/attribute.producedBy.Signal");
	}

	private void initializeReificationTypes() {
		StringBuilder initializedReifications = new StringBuilder(" reifications initialized: <" + Evaluator.RDF_STATEMENT+"> ");
		int initializedReification = 1;
		IRI RDFStatement =  Source.getTripleSource().getValueFactory().createIRI(Evaluator.RDF_STATEMENT);
		
		IRI RDFSsubPropertyOf = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.RDFS_SUB_PROPERTY_OF);
		IRI RDFsubject = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.RDF_SUBJECT);
		IRI RDFpredicate = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.RDF_PREDICATE);
		IRI RDFobject = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.RDF_OBJECT);
		IRI RDFSdomain = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.RDFS_DOMAIN);
		IRI OWLinverseOf = Source.getTripleSource().getValueFactory()
				.createIRI(Evaluator.OWL_INVERSE_OF);
		
		reificationTypes.put(Evaluator.RDF_STATEMENT,
				new ReificationType(RDFStatement,RDFsubject,RDFpredicate,RDFobject));
		
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = Source
				.getTripleSource().getStatements(null, RDFSsubPropertyOf, RDFpredicate);
		while (reificationPredicateStatements.hasNext()) {
			Value reificationType = null;
			
			Resource reificationPredicate;
			Resource reificationIsPredicateOf = null;
			Resource reificationSubject = null;
			Resource reificationIsSubjectOf = null;
			Resource reificationObject = null;
			Resource reificationIsObjectOf = null;
			Statement reificationPredicateStatement = reificationPredicateStatements.next();
			reificationPredicate = reificationPredicateStatement.getSubject();
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationTypeStatements = Source
					.getTripleSource().getStatements(reificationPredicate, RDFSdomain, null);
			while (reificationTypeStatements.hasNext()) {
				Statement reificationTypeStatement = reificationTypeStatements.next();
				reificationType = reificationTypeStatement.getObject();
				break;
			}
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = Source
					.getTripleSource().getStatements(null, RDFSdomain, reificationType);
			while (reificationSubjectStatements.hasNext()) {
				Statement reificationSubjectStatement = reificationSubjectStatements.next();
				Resource reificationProperty = reificationSubjectStatement.getSubject();
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubPropertyOfStatements = Source
						.getTripleSource().getStatements(reificationProperty, RDFSsubPropertyOf, null);
				while (reificationSubPropertyOfStatements.hasNext()) {
					Statement reificationSubPropertyOfStatement = reificationSubPropertyOfStatements.next();
					CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseOfStatements = Source
							.getTripleSource().getStatements(reificationProperty, OWLinverseOf, null);
					Resource reificationInverseProperty = null;
					while (reificationInverseOfStatements.hasNext()) {
						Statement reificationInverseOfStatement = reificationInverseOfStatements.next();
						reificationInverseProperty = (Resource) reificationInverseOfStatement.getObject();
						break;
					}
					Resource subPropertyOf = (Resource) reificationSubPropertyOfStatement.getObject();
					switch (subPropertyOf.stringValue()) {
					case Evaluator.RDF_SUBJECT:
						reificationSubject = reificationProperty;
						reificationIsSubjectOf =reificationInverseProperty;
						break;
					case Evaluator.RDF_OBJECT :
						reificationObject = reificationProperty;
						reificationIsObjectOf =reificationInverseProperty;
						break;
					case Evaluator.RDF_PREDICATE:
						reificationPredicate = reificationProperty;
						reificationIsPredicateOf =reificationInverseProperty;
						break;
					default:
					}
				}
			}
			initializedReification++;
			initializedReifications.append("<").append(((IRI)reificationType).stringValue()).append("> ");
			ReificationType newReificationType = new ReificationType(reificationType,reificationSubject,reificationPredicate ,reificationObject, reificationIsSubjectOf,reificationIsPredicateOf,reificationIsObjectOf  );
			reificationTypes.put(((IRI)reificationType).stringValue(), newReificationType );
			predicateReificationTypes.put(((IRI)reificationPredicate).stringValue(), newReificationType );
		}
		Source.logger.debug(initializedReification + initializedReifications.toString());
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

//	public Thing thingFactory(Resource subject, Stack<String> stack, HashMap<String, olgap.Value> customQueryOptions ) {
//		return thingFactory(null, subject, stack, customQueryOptions);
//	}

	public Thing thingFactory(Tracer tracer, Resource subject, Stack<String> stack, HashMap<String, olgap.Value> customQueryOptions, HashMap<String,String> prefixes ) {
		HashMap<String, olgap.Value> values;
		if (facts.containsKey(subject.toString())) {
			values = facts.get(subject.toString());
			//	thing.setTracer(tracer);
			//	return thing;
		} else {
			values = new HashMap<String, olgap.Value>();
			facts.put(subject.toString(), values);
		}
		olgap.Thing newThing = new Thing(this, subject,customQueryOptions,prefixes);
		newThing.setTracer(tracer);
		newThing.setStack(stack);
		newThing.setValues(values);
		return newThing;

	}
	public Thing thingFactory(Tracer tracer, Resource subject, Stack<String> stack, HashMap<String, olgap.Value> customQueryOptions  ) {
		return thingFactory(tracer, subject, stack,  customQueryOptions,null );
		

	}
	public olgap.Value valueFactory(Tracer tracer, Stack<String> stack) {
		return new olgap.Literal(null);
	}
	public olgap.Value valueFactory(Tracer tracer, String value, Stack<String> stack, HashMap<String, olgap.Value> customQueryOptions, HashMap<String,String> prefixes) {
		return this.valueFactory(tracer, Source.getTripleSource().getValueFactory().createLiteral(value), stack,customQueryOptions,prefixes);
	}

	public olgap.Value valueFactory(Tracer tracer, Value value, Stack<String> stack, HashMap<String, olgap.Value> customQueryOptions, HashMap<String,String> prefixes) {
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
			return thingFactory(tracer, (IRI) value, stack,customQueryOptions, prefixes);
		}
	}

	public HashMap<String, olgap.Value> getCustomQueryOptions(Value[] customQueryOptionsArray) {
		if (customQueryOptionsArray.length == 0) {
			return null;
		} else {
			HashMap<String, olgap.Value> customQueryOptions = new HashMap<String, olgap.Value>();
			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
				String customQueryOptionValue = customQueryOptionsArray[customQueryOptionsArrayIndex + 1].stringValue();
				if (customQueryOptionValue != null && !customQueryOptionValue.isEmpty())
					customQueryOptions.put(customQueryOptionParameter,
							valueFactory(null, customQueryOptionValue, null,null,null));//TODO
				if (customQueryOptionParameter.equals("service")) {
					String service = customQueryOptionValue;
					if (customQueryOptionValue.indexOf('?') > 0) {
						service = customQueryOptionValue.substring(0, customQueryOptionValue.indexOf('?'));
					}
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
				SEEQSource seeqSource = new SEEQSource("http://" + seeqServer + "/api", "peter.lawrence@inova8.com",
						"lusterthief");
				seeqSources.put(seeqServer, seeqSource);
				return seeqSource;
			} catch (Exception e) {
				//logger.error(String.format("Failed to compile '%s' language  script of  with contents:\n %s",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				logger.error(new ParameterizedMessage("Failed to connect to SEEQSource '{}'", seeqServer));
				throw e;
			}
		}
	}

	public void clearCache(Value... args) {
		things.clear();
		facts.clear();
		compiledScripts.clear();
		seeqSources.clear();
		if (args.length > 0) {
			//clear the service if provided
			HashMap<String, olgap.Value> customQueryOptions = getCustomQueryOptions(args);
			if (customQueryOptions.containsKey("service"))
				clearServiceCache(customQueryOptions);
		}
	}

	private void clearServiceCache(HashMap<String, olgap.Value> customQueryOptions) {
		if (connected()) {
			IRI cacheDateTimePredicate = Source.getTripleSource().getValueFactory().createIRI(Evaluator.NAMESPACE,
					Evaluator.CACHE_DATE_TIME);
			RepositoryResult<Statement> cacheStatements = cacheConnection.getStatements(null, cacheDateTimePredicate,
					null);
			String before = "";
			if (customQueryOptions.containsKey("before")) {
				before = getBefore(customQueryOptions);
			}
			while (cacheStatements.hasNext()) {
				Statement cacheStatement = cacheStatements.next();
				SimpleLiteral cacheDateTime = (org.eclipse.rdf4j.model.impl.SimpleLiteral) cacheStatement.getObject();
				String cacheDateTimeString = cacheDateTime.stringValue().substring(0, 19);
				if (before == "" || cacheDateTimeString.compareTo(before) <= 0) {
					IRI cacheContext = (IRI) cacheStatement.getSubject();
					cacheConnection.clear(cacheContext);
				}
			}
		} else {
			logger.error(new ParameterizedMessage("Failed to connect to clear cache"));
		}

	}

	private String getBefore(HashMap<String, olgap.Value> customQueryOptions) {
		if (customQueryOptions != null && customQueryOptions.containsKey("before")) {
			olgap.Value beforeDateTime = customQueryOptions.get("before");
			return beforeDateTime.getValue().toString().substring(1, 20);
		}
		return null;
	}

	public void writeModelToCache(Thing thing, Object result, IRI cacheContext) {
		if (connected()) {
			try {
				cacheConnection.clear(cacheContext);
				//Source.getTripleSource().getValueFactory().createIRI(Evaluator.NAMESPACE, "cacheDateTime");
				((Model) result).add(cacheContext,
						Source.getTripleSource().getValueFactory().createIRI(Evaluator.NAMESPACE, Evaluator.CACHE_DATE_TIME),
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
	public static ReificationType getPredicateReificationType(String predicate) {
		if(predicateReificationTypes.containsKey(predicate) ) {
			return predicateReificationTypes.get(predicate);
		}else {
			return null;
		}
	}

	public static IRI getReificationSubject(String reificationType) {
		return reificationTypes.get(reificationType).getReificationSubject();
	}
	public static IRI getReificationIsSubjectOf(String reificationType) {
		return reificationTypes.get(reificationType).getReificationIsSubjectOf();
	}
	public static IRI getReificationPredicate(String reificationType) {
		return reificationTypes.get(reificationType).getReificationPredicate();
	}
	public static IRI getReificationIsPredicateOf(String reificationType) {
		return reificationTypes.get(reificationType).getReificationIsPredicateOf();
	}

	public static IRI getReificationObject(String reificationType) {
		return reificationTypes.get(reificationType).getReificationObject();
	}
	public static IRI getReificationIsObjectOf(String reificationType) {
		return reificationTypes.get(reificationType).getReificationIsObjectOf();
	}
	public static IRI createIRI(String iri) {
		return getTripleSource().getValueFactory().createIRI( iri);
	}
	public static IRI createIRI(String namespace, String localName) {
		return getTripleSource().getValueFactory().createIRI(namespace, localName);
	}
}
