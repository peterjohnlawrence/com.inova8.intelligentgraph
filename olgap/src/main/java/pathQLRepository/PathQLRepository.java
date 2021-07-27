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
import org.antlr.v4.runtime.RecognitionException;
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
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.contextaware.ContextAwareConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.evaluation.SailTripleSource;

import Exceptions.ServerException;
import intelligentGraph.FactCache;
import intelligentGraph.IntelligentGraphConnection;
import intelligentGraph.IntelligentGraphSail;

import org.eclipse.rdf4j.model.Resource;

import pathCalc.CustomQueryOptions;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Prefixes;
import pathCalc.Thing;
import pathCalc.Trace;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQLResults.ResourceStatementResults;

/**
 * The Class PathQLRepository.
 */
public class PathQLRepository {

	private static final Logger logger = LoggerFactory.getLogger(PathQLRepository.class);
	private static final String FAILEDTOADDGRAPH_EXCEPTION = "Failed To Add Graph";
	private static final String FAILEDTOOPENGRAPH_EXCEPTION = "Failed To Open Graph";
	private static final String FAILEDTOREMOVEGRAPH_EXCEPTION = "Failed To Remove Graph";
	private static final String FAILEDTOCLOSEGRAPH_EXCEPTION = "Failed To Close Graph";
	private org.eclipse.rdf4j.repository.Repository cacheRep;
	private String cacheService;
	private org.eclipse.rdf4j.repository.Repository repository;
	private final FactCache factCache = new FactCache();
	private RepositoryConnection cacheConnection;
	private ContextAwareConnection contextAwareConnection;
	private TripleSource tripleSource; //Not unique per call using the same underlying triplestore
	private ModelBuilder modelBuilder;
	private static ConcurrentHashMap<String, Thing> things = new ConcurrentHashMap<String, Thing>();
	private static ConcurrentHashMap<String, CompiledScript> compiledScripts = new ConcurrentHashMap<String, CompiledScript>();
	private static ConcurrentHashMap<String, SEEQSource> seeqSources = new ConcurrentHashMap<String, SEEQSource>();

	private Reifications reifications = new Reifications(this);
	/** The prefixes. */
	@Deprecated
	private Prefixes prefixes = new Prefixes();
	@Deprecated
	private HashSet<IRI> publicContexts = new HashSet<IRI>();
	@Deprecated
	private HashSet<IRI> privateContexts = new HashSet<IRI>();

	private ConcurrentHashMap<IRI, Graph> graphs = new ConcurrentHashMap<IRI, Graph>();

	IntelligentGraphSail sail;
	private IntelligentGraphConnection intelligentGraphConnection;
	private static HashMap<Integer, PathQLRepository> pathQLRepositories = new HashMap<Integer, PathQLRepository>();

	public static PathQLRepository create(org.eclipse.rdf4j.repository.Repository repository, Resource... contexts) {
		Integer key = repository.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			PathQLRepository pathQLRepository = new PathQLRepository(repository, contexts);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	public static PathQLRepository create(String serverURL, String repositoryId, Resource... contexts) {
		Integer key = (serverURL + repositoryId).hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			PathQLRepository pathQLRepository = new PathQLRepository(serverURL, repositoryId, contexts);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	public static PathQLRepository create(IntelligentGraphConnection intelligentGraphConnection) {
		IntelligentGraphSail sail = intelligentGraphConnection.getIntelligentGraphSail();
		Integer key = sail.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			PathQLRepository pathQLRepository = new PathQLRepository(intelligentGraphConnection);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	public static PathQLRepository create(TripleSource tripleSource) {
		Integer key = tripleSource.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			PathQLRepository pathQLRepository = new PathQLRepository(tripleSource);
			pathQLRepositories.put(key, pathQLRepository);
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
		this.repository = new SailRepository(intelligentGraphConnection.getIntelligentGraphSail());/////////////////
		this.intelligentGraphConnection = intelligentGraphConnection;
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

	public FactCache getFactCache() {
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
			if (this.getIntelligentGraphConnection() != null) {
				if (this.getIntelligentGraphConnection().isOpen()) {
					return tripleSource;
				} else {
					tripleSource = new SailTripleSource(
							this.getIntelligentGraphConnection().getIntelligentGraphSail().getConnection(), true, null);
					return tripleSource;
				}
			} else if (this.contextAwareConnection != null) {
				if (this.contextAwareConnection.isOpen()) {
					return tripleSource;
				} else {
					tripleSource = new RepositoryTripleSource(
							this.contextAwareConnection.getRepository().getConnection());
					return tripleSource;
				}
			} else {
				return tripleSource;
			}
		} else {
			return null;
		}
	}

	public static void clearCaches() {
		for (PathQLRepository pathRepository : pathQLRepositories.values()) {
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

	@Deprecated
	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
		return this.getReifications().getReificationTypes();
	}

	@Deprecated
	ConcurrentHashMap<String, ReificationType> getPredicateReificationTypes() {
		return this.getReifications().getPredicateReificationTypes();
	}

	@Deprecated
	private void initializeReificationTypes() {
		this.getReifications().initializeReificationTypes();
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
		if (customQueryOptions != null) {
			if (customQueryOptions.contains("service")) {
				pathQLModel.Resource service = customQueryOptions.get("service");
				String serviceURL = service.toString();
				String serviceIRI = null;
				if (serviceURL.indexOf('?') > 0) {
					serviceIRI = serviceURL.substring(0, serviceURL.indexOf('?'));
				}
				if (serviceIRI != null)
					setCacheService(serviceIRI);
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
	public void clearCache(Value... args) {

		if (this.getRepository() != null) {
			CloseableIteration<Statement, RepositoryException> statementIterator = this.getRepository().getConnection()
					.getStatements(iri(IntelligentGraphConnection.PATHQL), iri(IntelligentGraphConnection.CLEARCACHE),
							literal(args));

			Statement statement;
			while (statementIterator.hasNext())
				statement = statementIterator.next();
		}

		//	factCache.clear();
		//	compiledScripts.clear();
		//	seeqSources.clear();
	}

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

	public static IRI createIRI(String namespace, String localName) {
		return iri(namespace, localName);
	}

	public IRI createIRI(String iri) {
		//	return getTripleSource().getValueFactory().createIRI( iri);
		return iri(iri);
	}

	public Prefixes getPrefixes() {
		return prefixes;
	}

	private void initializePrefixes(IntelligentGraphConnection connection) {
		CloseableIteration<? extends Namespace, SailException> namespaces = connection.getNamespaces();
		while (namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}

	}

	private void initializePrefixes(ContextAwareConnection connection)
			throws RepositoryException, IllegalArgumentException {

		//ContextAwareConnection connection = this.getContextAwareConnection();
		RepositoryResult<Namespace> namespaces = connection.getNamespaces();
		while (namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}

	}

	public PathQLRepository prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = trimAndCheckIRIString(IRI);
		if (iri != null) {
			if (this.getRepository() == null) {
				//Onlyu used for testing when not actual repository available
				getPrefixes().put(prefix, iri);
			} else {
				try {
					RepositoryConnection connection = this.getRepository().getConnection();
					connection.setNamespace(prefix, IRI);
					logger.debug("Added prefix {} for namespace {} ", prefix, iri);
				} catch (Exception qe) {
					throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,
							String.format("Failed to add prefix/namespace", prefix, iri), qe);
				}
			}
		} else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);
		}
		return this;
	}
	public PathQLRepository prefix(String IRI) {
		return this.prefix("", IRI);

	}

	public static IRI trimAndCheckIRIString(String IRI) {
		return iri(trimIRIString(IRI));
	}

	public static String trimIRIString(String IRI) {
		IRI = IRI.trim();
		if (IRI.startsWith("<") && IRI.endsWith(">")) {
			IRI = IRI.substring(1, IRI.length() - 1);
			return IRI;
		}
		return IRI;
	}

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

	public org.eclipse.rdf4j.repository.Repository getRepository() {
		return repository;
	}

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

	public Thing getThing(String iri) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(this,iri).getIri();
		return getThing(thingIri);
	}

	public Thing getThing(IRI iri, CustomQueryOptions customQueryOptions) {
		return Thing.create(this, iri, new EvaluationContext(customQueryOptions));
	}

	public Thing getThing(String iri, CustomQueryOptions customQueryOptions) {
		return getThing(iri(iri), customQueryOptions);
	}

	public void writeModelToCache(Thing thing, Object result, IRI cacheContext) {
		if (connected()) {
			try {
				cacheConnection.clear(cacheContext);
				((Model) result).add(cacheContext, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),
						literal(new Date()), cacheContext);
				cacheConnection.add((Model) result, cacheContext);
				thing.getEvaluationContext().getTracer().traceResultsCached(this.getCacheService(),
						cacheContext.stringValue());
			} catch (Exception e) {
				logger.error("Failed to write results to cache  {} with context \n {} with exception {}",
						result.toString(), cacheContext, e);
				thing.getEvaluationContext().getTracer().traceResultsCachedError(this.getCacheService());
			}
		} else {
			thing.getEvaluationContext().getTracer().traceResultsCachedNoService();
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
			Graph addedGraph = new Graph(this, graphNameIri);
			graphs.put(graphNameIri, addedGraph);
			getPublicContexts().add(graphNameIri);
			logger.debug("Added new graph {} ", graphNameIri.stringValue());
		} catch (Exception qe) {
			throw new ServerException(FAILEDTOADDGRAPH_EXCEPTION,
					String.format("Failed to add graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
		return new Graph(this, graphNameIri);
	}

	public Graph openGraph(String graphName) {
		RepositoryConnection connection = this.getRepository().getConnection();//.getContextAwareConnection();

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
			throw new ServerException(FAILEDTOOPENGRAPH_EXCEPTION,
					String.format("Failed to get graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
		return new Graph(this, graphNameIri);
	}

	public IRI removeGraph(String graphName) {
		RepositoryConnection connection = this.getRepository().getConnection();//.getContextAwareConnection();
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this, graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if (contextExists) {
				connection.clear(graphNameIri);
				connection.remove((Resource) null, (IRI) null, null, graphNameIri);
				logger.debug("Removed graph {} ", graphNameIri.stringValue());
			} else {
				logger.error("Failed to remove graph {} ", graphNameIri.stringValue());
				return null;
				//	throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,  String.format("Failed to remove graph %s. Does not exist", graphName)); 
			}

		} catch (Exception qe) {
			throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,
					String.format("Failed to remove graph %s", graphName, qe.getMessage()), qe);
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
			} else {
				return false;
			}

		} catch (Exception qe) {
			throw new ServerException(FAILEDTOCLOSEGRAPH_EXCEPTION,
					String.format("Failed to close graph %s with exception %s", graphName, qe.getMessage()), qe);
		}
	}

	public void initializeContexts() {
		publicContexts.clear();
		privateContexts.clear();
		RepositoryConnection connection = this.repository.getConnection();
		CloseableIteration<Resource, RepositoryException> connectionIDs = connection.getContextIDs();
		while (connectionIDs.hasNext()) {
			Resource connectionID = connectionIDs.next();
			CloseableIteration<Statement, RepositoryException> contextPrivacies = connection.getStatements(connectionID,
					iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE), null, false);
			publicContexts.add((IRI) connectionID);
			while (contextPrivacies.hasNext()) {
				Statement contextPrivacy = contextPrivacies.next();
				Value privacy = contextPrivacy.getObject();
				if (privacy.stringValue() == "true") {
					privateContexts.add((IRI) connectionID);
					publicContexts.remove((IRI) connectionID);
				}
			}
		}

	}

	@Deprecated
	public HashSet<IRI> getPublicContexts() {
		return publicContexts;
	}

	@Deprecated
	public HashSet<IRI> getPrivateContexts() {
		return privateContexts;
	}

	public IntelligentGraphConnection getIntelligentGraphConnection() {
		return intelligentGraphConnection;
	}

	public Reifications getReifications() {
		return reifications;
	}
}
