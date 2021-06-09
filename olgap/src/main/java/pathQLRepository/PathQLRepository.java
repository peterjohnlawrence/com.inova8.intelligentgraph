/*
 * inova8 2020
 */
package pathQLRepository;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.contextaware.ContextAwareConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.evaluation.SailTripleSource;

import Exceptions.ServerException;
import intelligentGraph.IntelligentGraphConnection;
import intelligentGraph.IntelligentGraphSail;

import org.eclipse.rdf4j.model.Resource;

import pathCalc.CustomQueryOptions;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Prefixes;
import pathCalc.Thing;
import pathQL.PathParser;

/**
 * The Class PathQLRepository.
 */
public class PathQLRepository {

	/** The Constant logger. */
	private static final Logger logger   = LoggerFactory.getLogger(PathQLRepository.class);
	private static final String FAILEDTOADDGRAPH_EXCEPTION = "Failed To Add Graph";
	private static final String FAILEDTOOPENGRAPH_EXCEPTION = "Failed To Open Graph";
	private static final String FAILEDTOREMOVEGRAPH_EXCEPTION = "Failed To Remove Graph";
	private static final String FAILEDTOCLOSEGRAPH_EXCEPTION = "Failed To Close Graph";
	/** The cache rep. */
	private org.eclipse.rdf4j.repository.Repository cacheRep;

	/** The cache service. */
	private String cacheService;

	/** The repository. */
	private org.eclipse.rdf4j.repository.Repository repository;
	
	private final  FactCache factCache=new FactCache();

	/** The cache connection. */
	private RepositoryConnection cacheConnection;
	private ContextAwareConnection contextAwareConnection ;
	/** The triple source. */
	private TripleSource tripleSource; //Not unique per call using the same underlying triplestore

	/** The model builder. */
	private ModelBuilder modelBuilder;

	/** The things. */
	private static  ConcurrentHashMap<String, Thing> things = new ConcurrentHashMap<String, Thing>();

	/** The facts. */
	//	 private HashMap<String, HashMap<String, pathQLModel.Resource>> facts = new HashMap<String, HashMap<String, pathQLModel.Resource>>();

	/** The compiled scripts. */
	private static ConcurrentHashMap<String, CompiledScript> compiledScripts = new ConcurrentHashMap<String, CompiledScript>();

	/** The seeq sources. */
	private static ConcurrentHashMap<String, SEEQSource> seeqSources = new ConcurrentHashMap<String, SEEQSource>();

	/** The is lazy loaded. */
	private Boolean isLazyLoaded = false;

	/**
	 * Sets the checks if is lazy loaded.
	 *
	 * @param isLazyLoaded
	 *            the new checks if is lazy loaded
	 */
	public void setIsLazyLoaded(Boolean isLazyLoaded) {
		this.isLazyLoaded = isLazyLoaded;
	}

	/** The reification types. */
	private ConcurrentHashMap<String, ReificationType> reificationTypes = new ConcurrentHashMap<String, ReificationType>();

	/** The predicate reification types. */
	private ConcurrentHashMap<String, ReificationType> predicateReificationTypes = new ConcurrentHashMap<String, ReificationType>();

	/** The prefixes. */
	private Prefixes prefixes = new Prefixes();

	private HashSet<IRI> publicContexts = new HashSet<IRI>();
	private HashSet<IRI> privateContexts = new HashSet<IRI>();
	
	private ConcurrentHashMap<IRI, Graph> graphs = new ConcurrentHashMap<IRI,Graph>();

	
	 IntelligentGraphSail sail ;
	private IntelligentGraphConnection intelligentGraphConnection;
	private static HashMap<Integer, PathQLRepository> pathQLRepositories  = new HashMap<Integer, PathQLRepository>();
	
//	public static PathQLRepository create(Repository workingRep) {
//		return create( (IntelligentGraphConnection)workingRep.getConnection());
//	}
	public static PathQLRepository create(org.eclipse.rdf4j.repository.Repository repository, Resource... contexts) {
		Integer key = repository.hashCode();
		if(pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		}else {
			PathQLRepository pathQLRepository = new PathQLRepository( repository, contexts);
			pathQLRepositories.put(key , pathQLRepository);
			return pathQLRepository;
		}	
	}
	public static PathQLRepository create(String serverURL, String repositoryId, Resource... contexts) {
		Integer key = (serverURL + repositoryId).hashCode();
		if(pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		}else {
			PathQLRepository pathQLRepository = new PathQLRepository( serverURL,  repositoryId, contexts);
			pathQLRepositories.put(key , pathQLRepository);
			return pathQLRepository;
		}	
	}

	public static PathQLRepository create(IntelligentGraphConnection intelligentGraphConnection) {
		IntelligentGraphSail sail = intelligentGraphConnection.getIntelligentGraphSail();
		Integer key =sail.hashCode();
		if(pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		}else {
			PathQLRepository pathQLRepository = new PathQLRepository( intelligentGraphConnection);
			pathQLRepositories.put(key , pathQLRepository);
			return pathQLRepository;
		}
	}
	public static PathQLRepository create(TripleSource tripleSource) {
		Integer key =tripleSource.hashCode();
		if(pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		}else {
			PathQLRepository pathQLRepository = new PathQLRepository( tripleSource);
			pathQLRepositories.put(key , pathQLRepository);
			return pathQLRepository;
		}
	}
	/**
	 * Instantiates a new path QL repository.
	 */
	public PathQLRepository() {
		this.modelBuilder = new ModelBuilder();
	}

//Used via SPARQL Functions
	private PathQLRepository(TripleSource tripleSource) {
		this.tripleSource = tripleSource;
		this.modelBuilder = new ModelBuilder();
	}

	/**
	 * Instantiates a new path QL repository.
	 *
	 * @param intelligentGraphConnection
	 *            the intelligent graph connection
	 */
	private PathQLRepository(IntelligentGraphConnection intelligentGraphConnection) {
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.tripleSource = new SailTripleSource(intelligentGraphConnection, true, null);
		this.modelBuilder = new ModelBuilder();
		initializePrefixes(intelligentGraphConnection);
	}

	private PathQLRepository(org.eclipse.rdf4j.repository.Repository repository, Resource... contexts) {
		this.repository = repository; 
		//((IntelligentGraphSail)repository).getConnection().getIntelligentGraphSail();
		this.contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(this.contextAwareConnection);
		this.modelBuilder = new ModelBuilder();
		initializePrefixes(contextAwareConnection);
	}

	private PathQLRepository(String serverURL, String repositoryId, Resource... contexts) {
		this.repository = new HTTPRepository(serverURL, repositoryId);
		this.contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(this.contextAwareConnection);
		this.modelBuilder = new ModelBuilder();
		initializePrefixes(contextAwareConnection);

	}

	public  FactCache getFactCache() {
		return factCache;
	}
	/**
	 * Gets the triple source.
	 *
	 * @return the triple source
	 */
	public TripleSource getTripleSource() {
		//PathQLRepository runs in two modes: when a triplesource is explicitly provided (as when invoked from SPARQL function) or when a repository is provided
		if (repository != null) {
			return new RepositoryTripleSource(getContextAwareConnection());
		} else if (sail != null) {
			return new SailTripleSource(sail.getConnection(), true, null);
		} else if (tripleSource != null) {
			if(this.intelligentGraphConnection!=null ) {
				if(this.intelligentGraphConnection.isOpen()) {
					return tripleSource;
				}else {
					tripleSource = new SailTripleSource(this.intelligentGraphConnection.getIntelligentGraphSail().getConnection(), true, null);
					return tripleSource;
				}
			}else if(this.contextAwareConnection!=null) {
				if(this.contextAwareConnection.isOpen()) {
					return tripleSource;
				}else {
					tripleSource = new RepositoryTripleSource(this.contextAwareConnection.getRepository().getConnection());
					return tripleSource;
				}
			}
			else {
				return tripleSource;
			}
		} else {
			return null;
		}
	}
	public static void clearCaches() {
		for( PathQLRepository pathRepository:pathQLRepositories.values()) {
			pathRepository.clearCache();
		}
	}
	/**
	 * Gets the things.
	 *
	 * @return the things
	 */
	public ConcurrentHashMap<String, Thing> getThings() {
		return things;
	}

	/**
	 * Gets the reification types.
	 *
	 * @return the reification types
	 */
	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
		if (!isLazyLoaded && this.getTripleSource() != null)
			initializeReificationTypes();
		return reificationTypes;
	}

	/**
	 * Gets the predicate reification types.
	 *
	 * @return the predicate reification types
	 */
	ConcurrentHashMap<String, ReificationType> getPredicateReificationTypes() {
		if (!isLazyLoaded && this.getTripleSource() != null)
			initializeReificationTypes();
		return predicateReificationTypes;
	}

	/**
	 * Adds the reification type.
	 *
	 * @param reificationType
	 *            the reification type
	 * @param reificationSubject
	 *            the reification subject
	 * @param reificationPredicate
	 *            the reification predicate
	 * @param reificationObject
	 *            the reification object
	 * @param reificationIsSubjectOf
	 *            the reification is subject of
	 * @param reificationIsPredicateOf
	 *            the reification is predicate of
	 * @param reificationIsObjectOf
	 *            the reification is object of
	 */
	public void addReificationType(Resource reificationType, Resource reificationSubject, Resource reificationPredicate,
			Resource reificationObject, Resource reificationIsSubjectOf, Resource reificationIsPredicateOf,
			Resource reificationIsObjectOf) {
		ReificationType newReificationType = new ReificationType(reificationType, reificationSubject,
				reificationPredicate, reificationObject, reificationIsSubjectOf, reificationIsPredicateOf,
				reificationIsObjectOf);
		reificationTypes.put(((IRI) reificationType).stringValue(), newReificationType);
		predicateReificationTypes.put(((IRI) reificationPredicate).stringValue(), newReificationType);

	}

	/**
	 * Initialize reification types.
	 */
	private void initializeReificationTypes() {
		StringBuilder initializedReifications = new StringBuilder(
				" reifications initialized: <" + Evaluator.RDF_STATEMENT + "> ");
		int initializedReification = 1;
		IRI RDFStatement = Evaluator.RDF_STATEMENT_IRI;

		IRI RDFSsubPropertyOf = Evaluator.RDFS_SUB_PROPERTY_OF_IRI;
		IRI RDFsubject = Evaluator.RDF_SUBJECT_IRI;
		IRI RDFpredicate = Evaluator.RDF_PREDICATE_IRI;
		IRI RDFobject = Evaluator.RDF_OBJECT_IRI;
		IRI RDFSdomain = Evaluator.RDFS_DOMAIN_IRI;
		IRI OWLinverseOf = Evaluator.OWL_INVERSE_OF_IRI;

		reificationTypes.put(Evaluator.RDF_STATEMENT,
				new ReificationType(RDFStatement, RDFsubject, RDFpredicate, RDFobject));

		CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = this
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
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationTypeStatements = this
					.getTripleSource().getStatements(reificationPredicate, RDFSdomain, null);
			while (reificationTypeStatements.hasNext()) {
				Statement reificationTypeStatement = reificationTypeStatements.next();
				reificationType = reificationTypeStatement.getObject();
				break;
			}
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = this
					.getTripleSource().getStatements(null, RDFSdomain, reificationType);
			while (reificationSubjectStatements.hasNext()) {
				Statement reificationSubjectStatement = reificationSubjectStatements.next();
				org.eclipse.rdf4j.model.Resource reificationProperty = reificationSubjectStatement.getSubject();
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubPropertyOfStatements = this
						.getTripleSource().getStatements(reificationProperty, RDFSsubPropertyOf, null);
				while (reificationSubPropertyOfStatements.hasNext()) {
					Statement reificationSubPropertyOfStatement = reificationSubPropertyOfStatements.next();
					CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseOfStatements = this
							.getTripleSource().getStatements(reificationProperty, OWLinverseOf, null);
					org.eclipse.rdf4j.model.Resource reificationInverseProperty = null;
					while (reificationInverseOfStatements.hasNext()) {
						Statement reificationInverseOfStatement = reificationInverseOfStatements.next();
						reificationInverseProperty = (org.eclipse.rdf4j.model.Resource) reificationInverseOfStatement
								.getObject();
						break;
					}
					org.eclipse.rdf4j.model.Resource subPropertyOf = (org.eclipse.rdf4j.model.Resource) reificationSubPropertyOfStatement
							.getObject();
					switch (subPropertyOf.stringValue()) {
					case Evaluator.RDF_SUBJECT:
						reificationSubject = reificationProperty;
						reificationIsSubjectOf = reificationInverseProperty;
						break;
					case Evaluator.RDF_OBJECT:
						reificationObject = reificationProperty;
						reificationIsObjectOf = reificationInverseProperty;
						break;
					case Evaluator.RDF_PREDICATE:
						reificationPredicate = reificationProperty;
						reificationIsPredicateOf = reificationInverseProperty;
						break;
					default:
					}
				}
			}
			initializedReification++;
			initializedReifications.append("<").append(((IRI) reificationType).stringValue()).append("> ");
			ReificationType newReificationType = new ReificationType((org.eclipse.rdf4j.model.Resource) reificationType,
					reificationSubject, reificationPredicate, reificationObject, reificationIsSubjectOf,
					reificationIsPredicateOf, reificationIsObjectOf);
			reificationTypes.put(((IRI) reificationType).stringValue(), newReificationType);
			predicateReificationTypes.put(((IRI) reificationPredicate).stringValue(), newReificationType);
		}
		isLazyLoaded = true;
		logger.debug(initializedReification + initializedReifications.toString());
	}

	/**
	 * Gets the cache rep.
	 *
	 * @return the cache rep
	 */
	@Deprecated
	public org.eclipse.rdf4j.repository.Repository getCacheRep() {
		return this.cacheRep;
	}

	/**
	 * Sets the cache rep.
	 *
	 * @param cacheRep
	 *            the new cache rep
	 */
	@Deprecated
	public void setCacheRep(org.eclipse.rdf4j.repository.Repository cacheRep) {
		this.cacheRep = cacheRep;
	}

	/**
	 * Gets the cache service.
	 *
	 * @return the cache service
	 */
	public String getCacheService() {
		return this.cacheService;
	}

	/**
	 * Sets the cache service.
	 *
	 * @param cacheService
	 *            the new cache service
	 */
	public void setCacheService(String cacheService) {
		this.cacheService = cacheService;
	}


	/**
	 * Sets the triple source.
	 *
	 * @param tripleSource
	 *            the new triple source
	 */
	public void setTripleSource(TripleSource tripleSource) {
		this.tripleSource = tripleSource;
	}

	/**
	 * Gets the model builder.
	 *
	 * @return the model builder
	 */
	public ModelBuilder getModelBuilder() {
		return this.modelBuilder;
	}

	/**
	 * Gets the custom query options.
	 *
	 * @param customQueryOptionsArray
	 *            the custom query options array
	 * @return the custom query options
	 */
	public CustomQueryOptions getCustomQueryOptions(Value[] customQueryOptionsArray) {
		CustomQueryOptions customQueryOptions = CustomQueryOptions.create(this, customQueryOptionsArray);
		if(customQueryOptions!=null) {
			if (customQueryOptions.contains("service")) {
				pathQLModel.Resource service = customQueryOptions.get("service");
				String serviceURL = service.toString();
				String serviceIRI = null;
				if (serviceURL.indexOf('?') > 0) {
					serviceIRI = serviceURL.substring(0, serviceURL.indexOf('?'));
				}
				if(serviceIRI!=null) setCacheService(serviceIRI);
			}
		}
		return customQueryOptions;
//		if (customQueryOptionsArray.length == 0) {
//			return null;
//		} else if (customQueryOptionsArray.length % 2 != 0) {
//			logger.error("Must have matching args tag/value pairs '{}'",
//					customQueryOptionsArray.length);
//			return null;
//		} else {
//			CustomQueryOptions customQueryOptions = new CustomQueryOptions();
//			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
//				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
//				String customQueryOptionValue = customQueryOptionsArray[customQueryOptionsArrayIndex + 1].stringValue();
//				if (customQueryOptionValue != null && !customQueryOptionValue.isEmpty())
//					customQueryOptions.put(customQueryOptionParameter,
//							pathQLModel.Resource.create(this, literal(customQueryOptionValue), null));
//				if (customQueryOptionParameter.equals("service")) {
//					String service = customQueryOptionValue;
//					if (customQueryOptionValue.indexOf('?') > 0) {
//						service = customQueryOptionValue.substring(0, customQueryOptionValue.indexOf('?'));
//					}
//					setCacheService(service);
//				}
//			}
//			return customQueryOptions;
//		}
	}

	/**
	 * Compiled script factory.
	 *
	 * @param scriptString
	 *            the script string
	 * @return the compiled script
	 * @throws ScriptException
	 *             the script exception
	 */
	public CompiledScript compiledScriptFactory(SimpleLiteral scriptString) throws ScriptException {
		String scriptCode = scriptString.getLabel();
		if (compiledScripts.containsKey(scriptCode)) {
			return compiledScripts.get(scriptCode);
		} else {
			try {
				ScriptEngine scriptEngine = Evaluator.getScriptEngine(scriptString.getDatatype());
				if (scriptEngine != null) {
					CompiledScript compiledScriptCode;
					compiledScriptCode = ((Compilable) scriptEngine).compile(scriptCode);
					compiledScripts.put(scriptCode, compiledScriptCode);
					return compiledScriptCode;
				} else {
					throw new ScriptException(
							"Unrecognized script language:" + scriptString.getDatatype().getLocalName());
				}
			} catch (ScriptException e) {
				logger.error("Failed to compile '{}' language  script of  with contents:\n {}",
						scriptString.getDatatype().getLocalName(), scriptString.toString());
				throw e;
			}
		}
	}

	/**
	 * Seeq source factory.
	 *
	 * @param seeqServer
	 *            the seeq server
	 * @return the SEEQ source
	 * @throws ScriptException
	 *             the script exception
	 */
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
				logger.error("Failed to connect to SEEQSource '{}'", seeqServer);
				throw e;
			}
		}
	}

	/**
	 * Clear cache.
	 *
	 * @param args
	 *            the args
	 */
	public  void clearCache(Value... args) {
		factCache.clear();
//		for( Thing thing:things.values()) {
//			thing.getCachedResources().clear();
//		}
//		things.clear();
		compiledScripts.clear();
		seeqSources.clear();
//		if (args.length > 0) {
//			//clear the service if provided
//			ConcurrentHashMap<String, pathQLModel.Resource> customQueryOptions = getCustomQueryOptions(args);
//			if (customQueryOptions.containsKey("service"))
//				clearServiceCache(customQueryOptions);
//		}
		//logger.debug("Caches cleared {}");
	}

	/**
	 * Clear service cache.
	 *
	 * @param customQueryOptions
	 *            the custom query options
	 */

	@SuppressWarnings("unused")
	private void clearServiceCache(ConcurrentHashMap<String, pathQLModel.Resource> customQueryOptions) {
		if (connected()) {
			IRI cacheDateTimePredicate = iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME);
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
			logger.error("Failed to connect to clear cache");
		}

	}

	/**
	 * Gets the before.
	 *
	 * @param customQueryOptions
	 *            the custom query options
	 * @return the before
	 */
	private String getBefore(ConcurrentHashMap<String, pathQLModel.Resource> customQueryOptions) {
		if (customQueryOptions != null && customQueryOptions.containsKey("before")) {
			pathQLModel.Resource beforeDateTime = customQueryOptions.get("before");
			return beforeDateTime.getValue().toString().substring(1, 20);
		}
		return null;
	}

	/**
	 * Adds the service.
	 *
	 * @param service
	 *            the service
	 * @return the string
	 */
	private String addService(String service) {
		return "<a href='" + service + "' target='_blank'>" + service + "</a>";
	}

	/**
	 * Connected.
	 *
	 * @return true, if successful
	 */
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

	/**
	 * Creates the IRI.
	 *
	 * @param namespace
	 *            the namespace
	 * @param localName
	 *            the local name
	 * @return the iri
	 */
	public static IRI createIRI(String namespace, String localName) {
		return iri(namespace, localName);
	}

	/**
	 * Gets the predicate reification type.
	 *
	 * @param predicate
	 *            the predicate
	 * @return the predicate reification type
	 */
	public ReificationType getPredicateReificationType(String predicate) {
		if (getPredicateReificationTypes().containsKey(predicate)) {
			return getPredicateReificationTypes().get(predicate);
		} else {
			return null;
		}
	}

	/**
	 * Creates the IRI.
	 *
	 * @param iri
	 *            the iri
	 * @return the iri
	 */
	public IRI createIRI(String iri) {
		//	return getTripleSource().getValueFactory().createIRI( iri);
		return iri(iri);
	}

	/**
	 * Gets the reification type.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification type
	 */
	private ReificationType getReificationType(String reificationType) {
		return getReificationTypes().get(reificationType);
	}

	/**
	 * Gets the reification subject.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification subject
	 */
	public IRI getReificationSubject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationSubject();
		else
			return null;
	}

	/**
	 * Gets the reification is subject of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is subject of
	 */
	public IRI getReificationIsSubjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsSubjectOf();
		else
			return null;
	}

	/**
	 * Gets the reification predicate.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification predicate
	 */
	public IRI getReificationPredicate(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationPredicate();
		else
			return null;
	}

	/**
	 * Gets the reification is predicate of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is predicate of
	 */
	public IRI getReificationIsPredicateOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsPredicateOf();
		else
			return null;
	}

	/**
	 * Gets the reification object.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification object
	 */
	public IRI getReificationObject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationObject();
		else
			return null;
	}

	/**
	 * Gets the reification is object of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is object of
	 */
	public IRI getReificationIsObjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsObjectOf();
		else
			return null;
	}

	/**
	 * Gets the reification object.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification object
	 */
	public IRI getReificationObject(IRI reificationType) {
		return getReificationObject(reificationType.stringValue());
	}

	/**
	 * Gets the reification is object of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is object of
	 */
	public IRI getReificationIsObjectOf(IRI reificationType) {
		return getReificationIsObjectOf(reificationType.stringValue());
	}

	/**
	 * Gets the reification subject.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification subject
	 */
	public IRI getReificationSubject(IRI reificationType) {
		return getReificationSubject(reificationType.stringValue());
	}

	/**
	 * Gets the reification is subject of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is subject of
	 */
	public IRI getReificationIsSubjectOf(IRI reificationType) {
		return getReificationIsSubjectOf(reificationType.stringValue());
	}

	/**
	 * Gets the reification predicate.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification predicate
	 */
	public IRI getReificationPredicate(IRI reificationType) {
		return getReificationPredicate(reificationType.stringValue());
	}

	/**
	 * Gets the reification is predicate of.
	 *
	 * @param reificationType
	 *            the reification type
	 * @return the reification is predicate of
	 */
	public IRI getReificationIsPredicateOf(IRI reificationType) {
		return getReificationIsPredicateOf(reificationType.stringValue());
	}

	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public Prefixes getPrefixes() {
		return prefixes;
	}

	/**
	 * Initialize prefixes.
	 *
	 * @param connection
	 *            the connection
	 */
	private void initializePrefixes(IntelligentGraphConnection connection) {
		CloseableIteration<? extends Namespace, SailException> namespaces = connection.getNamespaces();
		while (namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}

	}
	/**
	 * Initialize prefixes.
	 *
	 * @param connection
	 *            the connection
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private void initializePrefixes(ContextAwareConnection connection)
			throws RepositoryException, IllegalArgumentException {

		//ContextAwareConnection connection = this.getContextAwareConnection();
		RepositoryResult<Namespace> namespaces = connection.getNamespaces();
		while (namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}

	}

	/**
	 * Prefix.
	 *
	 * @param prefix
	 *            the prefix
	 * @param IRI
	 *            the iri
	 * @return the path QL repository
	 */
	
	@Deprecated
	public PathQLRepository prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = trimAndCheckIRIString(IRI);
		if (iri != null) {
			getPrefixes().put(prefix, iri);
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
	 * @return the path QL repository
	 */
	@Deprecated
	public PathQLRepository prefix(String IRI) {
		return this.prefix("", IRI);
	}

	/**
	 * Trim and check IRI string.
	 *
	 * @param IRI
	 *            the iri
	 * @return the iri
	 */
	public static IRI trimAndCheckIRIString(String IRI) {
		return iri(trimIRIString(IRI));
	}

	/**
	 * Trim IRI string.
	 *
	 * @param IRI
	 *            the iri
	 * @return the string
	 */
	public static String trimIRIString(String IRI) {
		IRI = IRI.trim();
		if (IRI.startsWith("<") && IRI.endsWith(">")) {
			IRI = IRI.substring(1, IRI.length() - 1);
			return IRI;
		}
		return IRI;
	}

	/**
	 * Convert Q name.
	 *
	 * @param predicateIRI
	 *            the predicate IRI
	 * @param localPrefixes
	 *            the local prefixes
	 * @return the iri
	 */
	public IRI convertQName(String predicateIRI, ConcurrentHashMap<String, IRI> localPrefixes) {
		predicateIRI = PathQLRepository.trimIRIString(predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":|~");
		IRI predicate = null;
		if (predicateIRIParts[0].equals("a")) {
			predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		} else if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		} else {
			IRI namespace = getNamespace(predicateIRIParts[0], localPrefixes);
			if (namespace == null) {
				logger.error("Error identifying namespace of qName {}", predicateIRI);
			} else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}

	/**
	 * Gets the namespace.
	 *
	 * @param namespaceString
	 *            the namespace string
	 * @param localPrefixes
	 *            the local prefixes
	 * @return the namespace
	 */
	private IRI getNamespace(String namespaceString, ConcurrentHashMap<String, IRI> localPrefixes) {
		IRI namespace;
		if (localPrefixes != null) {
			namespace = localPrefixes.get(namespaceString);
			if (namespace != null)
				return namespace;
		}
		namespace = getPrefixes().get(namespaceString);
		return namespace;
	}

	/**
	 * Gets the repository.
	 *
	 * @return the repository
	 */
	public org.eclipse.rdf4j.repository.Repository getRepository() {
		return repository;
	}

	/**
	 * Gets the context aware connection.
	 *
	 * @return the context aware connection
	 */
	public ContextAwareConnection getContextAwareConnection() {
		if (repository != null) {
			ContextAwareConnection contextAwareConnection = new PathQLContextAwareConnection(repository);
			if (getPublicContexts().size() > 0) {
				IRI[] publicContextsArray = {};
				contextAwareConnection.setReadContexts(getPublicContexts().toArray(publicContextsArray));
			}
			return contextAwareConnection;
		} else {
			return null;
		}
	}

	/**
	 * Public context aware connection.
	 *
	 * @return the context aware connection
	 * @throws RepositoryException
	 *             the repository exception
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private ContextAwareConnection publicContextAwareConnection() throws RepositoryException, IllegalArgumentException {
		ContextAwareConnection contextAwareConnection = new PathQLContextAwareConnection(repository);
		RepositoryResult<Resource> contextIDs = contextAwareConnection.getContextIDs();
		for (Resource contextID : contextIDs) {
			if (contextAwareConnection.hasStatement(contextID, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE),
					literal(true), contextID)) {
				privateContexts.add((IRI) contextID);
			} else {
				getPublicContexts().add((IRI) contextID);
			}

		}
		return contextAwareConnection;
	}

	public Thing getThing(IRI iri) {
		return Thing.create(this, iri, null);
	}
	public Thing getThing(String iri) {
		return getThing( iri(iri));
	}
	public Thing getThing(IRI iri, CustomQueryOptions customQueryOptions) {
		return Thing.create(this, iri, new EvaluationContext(customQueryOptions));
	}
	public Thing getThing(String iri, CustomQueryOptions customQueryOptions) {
		return getThing( iri(iri), customQueryOptions);
	}

	public void writeModelToCache(Thing thing, Object result, IRI cacheContext) {
		if (connected()) {
			try {
				cacheConnection.clear(cacheContext);
				((Model) result).add(cacheContext, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),
						literal(new Date()), cacheContext);
				cacheConnection.add((Model) result, cacheContext);
				thing.addTrace(String.format("Results cached to service %s in graph %s",
						addService(this.getCacheService()), addService(cacheContext.stringValue())));
			} catch (Exception e) {
				logger.error(
						"Failed to write results to cache  {} with context \n {} with exception {}", result.toString(),
						cacheContext, e);
				thing.addTrace(String.format("Results NOT cached to service:%s",
						addService(this.getCacheService())));
			}
		} else {
			thing.addTrace("No service to cached results");
		}
	}

	public Graph addGraph(String graphName) {
		ContextAwareConnection connection = this.getContextAwareConnection();
		Model result;
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this, graphName).getIri();
			result = new LinkedHashModel();
			((Model) result).add(graphNameIri, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),
					literal(new Date()), graphNameIri);
			((Model) result).add(graphNameIri, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE), literal(true),
					graphNameIri);
			connection.add((Model) result, graphNameIri);
			Graph addedGraph = new Graph(this,graphNameIri);
			graphs.put(graphNameIri, addedGraph);
			getPublicContexts().add(graphNameIri);
			logger.debug("Added new graph {} ", graphNameIri.stringValue());
		} catch (Exception qe) {
			throw new ServerException(FAILEDTOADDGRAPH_EXCEPTION, String.format(
					"Failed to add graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
		return new Graph(this, graphNameIri);
	}

	public Graph openGraph(String graphName) {
		RepositoryConnection connection = this.getContextAwareConnection();

		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this, graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if (!contextExists) {
				addGraph(graphName);
			} else {
				getPublicContexts().add(graphNameIri);
			}
			logger.debug("Got graph {} ", graphNameIri.stringValue());
		} catch (Exception qe) {
			throw new ServerException(FAILEDTOOPENGRAPH_EXCEPTION, String.format(
					"Failed to get graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
		return new Graph(this, graphNameIri);
	}

	public IRI removeGraph(String graphName){
		RepositoryConnection connection = this.getContextAwareConnection();
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this, graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if (contextExists) {
				connection.clear(graphNameIri);
				logger.debug("Removed graph {} ", graphNameIri.stringValue());
			}else {
				logger.error("Failed to remove graph {} ", graphNameIri.stringValue());
				return null;
			//	throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,  String.format("Failed to remove graph %s. Does not exist", graphName)); 
			}

		} catch (Exception qe) {
			throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,  String.format("Failed to remove graph %s", graphName, qe.getMessage()), qe); 
		}
		return graphNameIri;
	}

	@SuppressWarnings("deprecation")
	public Boolean closeGraph(String graphName) {
		RepositoryConnection connection = this.getContextAwareConnection();
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this, graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if (contextExists) {
				getPublicContexts().remove(graphNameIri);
				return true;
			}else {
				return false;
			}
			
		} catch (Exception qe) {
			throw new ServerException(FAILEDTOCLOSEGRAPH_EXCEPTION, String.format(
					"Failed to close graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
	}
	public void initializeContexts() {
		publicContexts.clear();
		privateContexts.clear();
		RepositoryConnection connection = this.repository.getConnection();
		CloseableIteration<Resource, RepositoryException> connectionIDs = connection.getContextIDs();
		while(connectionIDs.hasNext()) {
			Resource connectionID = connectionIDs.next();
			CloseableIteration<Statement, RepositoryException> contextPrivacies = connection.getStatements(connectionID, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE), null, false);
			publicContexts.add((IRI) connectionID);
			while(contextPrivacies.hasNext()) {
				Statement contextPrivacy = contextPrivacies.next();
				Value privacy = contextPrivacy.getObject();
				if(privacy.stringValue()=="true") {
					privateContexts.add((IRI) connectionID);
					publicContexts.remove((IRI) connectionID);
				}
			}
		}

	}
	public HashSet<IRI> getPublicContexts() {
		return publicContexts;
	}
	public HashSet<IRI> getPrivateContexts() {
		return privateContexts;
	}
}
