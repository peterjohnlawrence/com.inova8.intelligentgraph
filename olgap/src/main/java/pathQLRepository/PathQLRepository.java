package pathQLRepository;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
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
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.model.Resource;
import olgap.Evaluator;
import pathCalc.Thing;
import pathCalc.Tracer;
import pathQLModel.Literal;

public class PathQLRepository {

	static private final Logger logger = LogManager.getLogger(PathQLRepository.class);
	static private org.eclipse.rdf4j.repository.Repository cacheRep;
	static private String cacheService;
	private static org.eclipse.rdf4j.repository.Repository repository;
	static private RepositoryConnection cacheConnection;
	static private TripleSource tripleSource; //Not unique per call using the same underlying triplestore
	static private ModelBuilder modelBuilder;
	static private HashMap<String, Thing> things = new HashMap<String, Thing>();
	static private HashMap<String, HashMap<String, pathQLModel.Resource>> facts = new HashMap<String, HashMap<String, pathQLModel.Resource>>();
	static private HashMap<String, CompiledScript> compiledScripts = new HashMap<String, CompiledScript>();
	static private HashMap<String, SEEQSource> seeqSources = new HashMap<String, SEEQSource>();
	static private Boolean isLazyLoaded = false;
	static private HashMap<String, ReificationType> reificationTypes = new HashMap<String, ReificationType>();
	static private HashMap<String, ReificationType> predicateReificationTypes = new HashMap<String, ReificationType>();
	static private HashMap<String,IRI> prefixes = new HashMap<String,IRI>();

	public PathQLRepository() {
		PathQLRepository.modelBuilder = new ModelBuilder();
	}
	public PathQLRepository(TripleSource tripleSource) {
		PathQLRepository.tripleSource = tripleSource;
		PathQLRepository.modelBuilder = new ModelBuilder();
	}
	public PathQLRepository(org.eclipse.rdf4j.repository.Repository repository) {
		PathQLRepository.repository = repository;
		PathQLRepository.tripleSource = new RepositoryTripleSource(repository.getConnection());
		PathQLRepository.modelBuilder = new ModelBuilder();
	}
	static HashMap<String, ReificationType> getReificationTypes(){
		if(!isLazyLoaded &&  PathQLRepository.getTripleSource()!=null) initializeReificationTypes();
		return reificationTypes;
	}
	static HashMap<String, ReificationType> getPredicateReificationTypes(){
		if(!isLazyLoaded) initializeReificationTypes();
		return predicateReificationTypes;
	}
	public static void addReificationType(Resource reificationType,Resource reificationSubject,Resource reificationPredicate ,Resource reificationObject, Resource reificationIsSubjectOf,Resource reificationIsPredicateOf,Resource reificationIsObjectOf ) {
		ReificationType newReificationType = new ReificationType(reificationType,reificationSubject,reificationPredicate ,reificationObject, reificationIsSubjectOf,reificationIsPredicateOf,reificationIsObjectOf  );
		reificationTypes.put(((IRI)reificationType).stringValue(), newReificationType );
		predicateReificationTypes.put(((IRI)reificationPredicate).stringValue(), newReificationType );		
		
		
	}
	private static void initializeReificationTypes() {
		StringBuilder initializedReifications = new StringBuilder(" reifications initialized: <" + Evaluator.RDF_STATEMENT+"> ");
		int initializedReification = 1;
		IRI RDFStatement =  iri(Evaluator.RDF_STATEMENT);
		
		IRI RDFSsubPropertyOf = iri(Evaluator.RDFS_SUB_PROPERTY_OF);
		IRI RDFsubject = iri(Evaluator.RDF_SUBJECT);
		IRI RDFpredicate = iri(Evaluator.RDF_PREDICATE);
		IRI RDFobject = iri(Evaluator.RDF_OBJECT);
		IRI RDFSdomain = iri(Evaluator.RDFS_DOMAIN);
		IRI OWLinverseOf = iri(Evaluator.OWL_INVERSE_OF);
		
		reificationTypes.put(Evaluator.RDF_STATEMENT,
				new ReificationType(RDFStatement,RDFsubject,RDFpredicate,RDFobject));
		
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = PathQLRepository
				.getTripleSource().getStatements(null, RDFSsubPropertyOf, RDFpredicate);
		while (reificationPredicateStatements.hasNext()) {
			Value reificationType = null;
			
			org.eclipse.rdf4j.model.Resource reificationPredicate;
			org.eclipse.rdf4j.model.Resource reificationIsPredicateOf = null;
			org.eclipse.rdf4j.model.Resource reificationSubject = null;
			org.eclipse.rdf4j.model.Resource reificationIsSubjectOf = null;
			org.eclipse.rdf4j.model.Resource reificationObject = null;
			org.eclipse.rdf4j.model.Resource reificationIsObjectOf = null;
			Statement reificationPredicateStatement = reificationPredicateStatements.next();
			reificationPredicate = reificationPredicateStatement.getSubject();
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationTypeStatements = PathQLRepository
					.getTripleSource().getStatements(reificationPredicate, RDFSdomain, null);
			while (reificationTypeStatements.hasNext()) {
				Statement reificationTypeStatement = reificationTypeStatements.next();
				reificationType = reificationTypeStatement.getObject();
				break;
			}
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = PathQLRepository
					.getTripleSource().getStatements(null, RDFSdomain, reificationType);
			while (reificationSubjectStatements.hasNext()) {
				Statement reificationSubjectStatement = reificationSubjectStatements.next();
				org.eclipse.rdf4j.model.Resource reificationProperty = reificationSubjectStatement.getSubject();
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubPropertyOfStatements = PathQLRepository
						.getTripleSource().getStatements(reificationProperty, RDFSsubPropertyOf, null);
				while (reificationSubPropertyOfStatements.hasNext()) {
					Statement reificationSubPropertyOfStatement = reificationSubPropertyOfStatements.next();
					CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseOfStatements = PathQLRepository
							.getTripleSource().getStatements(reificationProperty, OWLinverseOf, null);
					org.eclipse.rdf4j.model.Resource reificationInverseProperty = null;
					while (reificationInverseOfStatements.hasNext()) {
						Statement reificationInverseOfStatement = reificationInverseOfStatements.next();
						reificationInverseProperty = (org.eclipse.rdf4j.model.Resource) reificationInverseOfStatement.getObject();
						break;
					}
					org.eclipse.rdf4j.model.Resource subPropertyOf = (org.eclipse.rdf4j.model.Resource) reificationSubPropertyOfStatement.getObject();
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
			ReificationType newReificationType = new ReificationType((org.eclipse.rdf4j.model.Resource)reificationType,reificationSubject,reificationPredicate ,reificationObject, reificationIsSubjectOf,reificationIsPredicateOf,reificationIsObjectOf  );
			reificationTypes.put(((IRI)reificationType).stringValue(), newReificationType );
			predicateReificationTypes.put(((IRI)reificationPredicate).stringValue(), newReificationType );
		}
		isLazyLoaded=true;
		PathQLRepository.logger.debug(initializedReification + initializedReifications.toString());
	}

	public static org.eclipse.rdf4j.repository.Repository getCacheRep() {
		return cacheRep;
	}

	public static void setCacheRep(org.eclipse.rdf4j.repository.Repository cacheRep) {
		PathQLRepository.cacheRep = cacheRep;
	}

	public static String getCacheService() {
		return cacheService;
	}

	public static void setCacheService(String cacheService) {
		PathQLRepository.cacheService = cacheService;
	}

	public static TripleSource getTripleSource() {
		return tripleSource;
	}

	public void setTripleSource(TripleSource tripleSource) {
		PathQLRepository.tripleSource = tripleSource;
	}

	public static ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public Thing thingFactory(Tracer tracer, Value subject, Stack<String> stack, HashMap<String, pathQLModel.Resource> customQueryOptions, HashMap<String,IRI> prefixes ) {
		HashMap<String, pathQLModel.Resource> values = null;
		if(subject!=null ) {
			if (facts.containsKey(subject.toString())) {
				values = facts.get(subject.toString());
			} else {
				values = new HashMap<String, pathQLModel.Resource>();
				facts.put(subject.toString(), values);
			}
		}
		pathCalc.Thing newThing = new Thing(this, subject,customQueryOptions,prefixes);
		newThing.setTracer(tracer);
		newThing.setStack(stack);
		newThing.setCachedValues(values);
		return newThing;

	}
	public Thing thingFactory(Tracer tracer, Value subject, Stack<String> stack, HashMap<String, pathQLModel.Resource> customQueryOptions  ) {
		return thingFactory(tracer, subject, stack,  customQueryOptions,null );
	}
	@Deprecated
	public pathQLModel.Resource resourceFactory(Tracer tracer, Stack<String> stack) {
		return new Literal(null);
	}
	public pathQLModel.Resource resourceFactory(Tracer tracer, String value, Stack<String> stack, HashMap<String, pathQLModel.Resource> customQueryOptions, HashMap<String,IRI> prefixes) {
		return this.resourceFactory(tracer, literal(value), stack,customQueryOptions,prefixes);
	}

	public pathQLModel.Resource resourceFactory(Tracer tracer, Value value, Stack<String> stack, HashMap<String, pathQLModel.Resource> customQueryOptions, HashMap<String,IRI> prefixes) {
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
			return new Literal(value);
		default:
			//logger.error(new ParameterizedMessage("No handler found for objectvalue class {}",value.getClass().getSimpleName()));
			return thingFactory(tracer, (IRI) value, stack,customQueryOptions, prefixes);
		}
	}
	
	public HashMap<String, pathQLModel.Resource> getCustomQueryOptions(Value[] customQueryOptionsArray) {
		if (customQueryOptionsArray.length == 0) {
			return null;
		} else if(customQueryOptionsArray.length % 2 != 0 ){
			logger.error(new ParameterizedMessage("Must have matching args tag/value pairs '{}'",	 customQueryOptionsArray.length));
			return null;
		}else {
			HashMap<String, pathQLModel.Resource> customQueryOptions = new HashMap<String, pathQLModel.Resource>();
			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
				String customQueryOptionValue = customQueryOptionsArray[customQueryOptionsArrayIndex + 1].stringValue();
				if (customQueryOptionValue != null && !customQueryOptionValue.isEmpty())
					customQueryOptions.put(customQueryOptionParameter,
							resourceFactory(null, customQueryOptionValue, null,null,null));//TODO
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
				ScriptEngine scriptEngine = Evaluator.getScriptEngine(scriptString.getDatatype().getLocalName());
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
			HashMap<String, pathQLModel.Resource> customQueryOptions = getCustomQueryOptions(args);
			if (customQueryOptions.containsKey("service"))
				clearServiceCache(customQueryOptions);
		}
		logger.debug(new ParameterizedMessage("Caches cleared {}"));
	}

	private void clearServiceCache(HashMap<String, pathQLModel.Resource> customQueryOptions) {
		if (connected()) {
			IRI cacheDateTimePredicate = iri(Evaluator.SCRIPTNAMESPACE,
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

	private String getBefore(HashMap<String, pathQLModel.Resource> customQueryOptions) {
		if (customQueryOptions != null && customQueryOptions.containsKey("before")) {
			pathQLModel.Resource beforeDateTime = customQueryOptions.get("before");
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
						iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),
						literal(new Date()), cacheContext);
				cacheConnection.add((Model) result, cacheContext);
				thing.addTrace(new ParameterizedMessage("Results cached to service {} in graph {}",
						addService(PathQLRepository.getCacheService()), addService(cacheContext.stringValue())));
			} catch (Exception e) {
				logger.error(new ParameterizedMessage(
						"Failed to write results to cache  {} with context \n {} with exception {}", result.toString(),
						cacheContext), e);
				thing.addTrace(new ParameterizedMessage("Results NOT cached to service:{}",
						addService(PathQLRepository.getCacheService())));
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
				org.eclipse.rdf4j.repository.Repository rep = new HTTPRepository(cacheService);
				rep.init();
				cacheConnection = rep.getConnection();
				return true;
			} catch (Exception e) {
				logger.error("Failed to create connection to cache:" + cacheService);
				return false;
			}
		}
	}
	public static IRI createIRI(String namespace, String localName) {
		return iri(namespace, localName);
	}
	public static ReificationType getPredicateReificationType(String predicate) {
		if(getPredicateReificationTypes().containsKey(predicate) ) {
			return getPredicateReificationTypes().get(predicate);
		}else {
			return null;
		}
	}
	public static IRI createIRI(String iri) {
	//	return getTripleSource().getValueFactory().createIRI( iri);
		return iri( iri);
	}

	private static ReificationType getReificationType(String reificationType) {
		return getReificationTypes().get(reificationType);
	}
	public static IRI getReificationSubject(String reificationType) {
		if(getReificationType(reificationType)!=null)
			return getReificationType(reificationType).getReificationSubject();
		else return null;
	}
	public static IRI getReificationIsSubjectOf(String reificationType) {
		if(getReificationType(reificationType)!=null)
		return getReificationType(reificationType).getReificationIsSubjectOf();
		else return null;
	}
	public static IRI getReificationPredicate(String reificationType) {
		if(getReificationType(reificationType)!=null)
		return getReificationType(reificationType).getReificationPredicate();
		else return null;
	}
	public static IRI getReificationIsPredicateOf(String reificationType) {
		if(getReificationType(reificationType)!=null)
		return getReificationType(reificationType).getReificationIsPredicateOf();
		else return null;
	}
	public static IRI getReificationObject(String reificationType) {
		if(getReificationType(reificationType)!=null)
		return getReificationType(reificationType).getReificationObject();
		else return null;
	}
	public static IRI getReificationIsObjectOf(String reificationType) {
		if(getReificationType(reificationType)!=null)
		return getReificationType(reificationType).getReificationIsObjectOf();
		else return null;
	}

	public static IRI getReificationObject(IRI reificationType) {
		return getReificationObject(reificationType.stringValue());
	}
	public static IRI getReificationIsObjectOf(IRI reificationType) {
		return getReificationIsObjectOf(reificationType.stringValue());
	}
	public static IRI getReificationSubject(IRI reificationType) {
		return getReificationSubject(reificationType.stringValue());
	}
	public static IRI getReificationIsSubjectOf(IRI reificationType) {
		return getReificationIsSubjectOf(reificationType.stringValue());
	}
	public static IRI getReificationPredicate(IRI reificationType) {
		return getReificationPredicate(reificationType.stringValue());
	}
	public static IRI getReificationIsPredicateOf(IRI reificationType) {
		return getReificationIsPredicateOf(reificationType.stringValue());
	}
	public static HashMap<String, IRI> getPrefixes() {
		return prefixes;
	}
	public static void setPrefixes(HashMap<String, IRI> prefixes) {
		PathQLRepository.prefixes=prefixes;
	}
	public PathQLRepository prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = trimAndCheckIRIString(IRI);
		if(iri!=null )	{	
			getPrefixes().put(prefix,iri);	
			return this;
		}else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);	
			return null;
		}
	}

	public PathQLRepository prefix(String IRI) {
		return this.prefix("",IRI);
	}
	public static  IRI trimAndCheckIRIString(String IRI) {	
		return iri(trimIRIString(IRI) );
	}
	public static String trimIRIString(String IRI) {
		IRI = IRI.trim();
		if( IRI.startsWith("<") && IRI.endsWith(">")) {	
			IRI=IRI.substring(1,IRI.length()-1);			
			return IRI ;	
		}
		return IRI;
	}
	public static IRI convertQName(String predicateIRI, HashMap<String,IRI> localPrefixes) {
		predicateIRI=PathQLRepository.trimIRIString( predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":|~");
		IRI predicate = null;
		if(predicateIRIParts[0].equals("a")) {
				predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			}
		else if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		}else {
			IRI namespace = getNamespace(predicateIRIParts[0],localPrefixes);
			if(namespace==null) {
				logger.error(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
			}else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}
	private static IRI getNamespace(String namespaceString, HashMap<String,IRI> localPrefixes) {
		IRI namespace;
		if(localPrefixes!= null ) {
			namespace = localPrefixes.get(namespaceString);
			if(namespace!=null)
				return namespace;
		}
		namespace = getPrefixes().get(namespaceString);
		return namespace;
	}
	public static org.eclipse.rdf4j.repository.Repository getRepository() {
		return repository;
	}
}
