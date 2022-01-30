/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.intelligentGraphRepository;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.evaluator.Evaluator;
import com.inova8.intelligentgraph.evaluator.FactCache;
import com.inova8.intelligentgraph.exceptions.ServerException;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.sail.IntelligentGraphConnection;
import com.inova8.intelligentgraph.sail.IntelligentGraphSail;
import com.inova8.intelligentgraph.seeq.SEEQSource;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.context.Prefixes;
import com.inova8.pathql.context.Reifications;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathPatternException;
import com.inova8.pathql.utilities.Utilities;

import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.contextaware.ContextAwareConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.evaluation.SailTripleSource;
import org.eclipse.rdf4j.model.Resource;

/**
 * The Class IntelligentGraphRepository.
 */
public class IntelligentGraphRepository {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(IntelligentGraphRepository.class);
	
	/** The Constant FAILEDTOADDGRAPH_EXCEPTION. */
	private static final String FAILEDTOADDGRAPH_EXCEPTION = "Failed To Add Graph";
	
	/** The Constant FAILEDTOOPENGRAPH_EXCEPTION. */
	private static final String FAILEDTOOPENGRAPH_EXCEPTION = "Failed To Open Graph";
	
	/** The Constant FAILEDTOREMOVEGRAPH_EXCEPTION. */
	private static final String FAILEDTOREMOVEGRAPH_EXCEPTION = "Failed To Remove Graph";
	
	/** The Constant FAILEDTOCLOSEGRAPH_EXCEPTION. */
	private static final String FAILEDTOCLOSEGRAPH_EXCEPTION = "Failed To Close Graph";
	
	/** The cache service. */
	private String cacheService;
	
	/** The repository. */
	private org.eclipse.rdf4j.repository.Repository repository;
	
	/** The fact cache. */
	private final FactCache factCache = new FactCache();
	
	/** The cache connection. */
	private RepositoryConnection cacheConnection;
	
	/** The context aware connection. */
	private ContextAwareConnection contextAwareConnection;
	
	/** The triple source. */
	private TripleSource tripleSource; //Not unique per call using the same underlying triplestore
	
	/** The model builder. */
	private ModelBuilder modelBuilder;
	
	/** The things. */
	private static ConcurrentHashMap<String, Thing> things = new ConcurrentHashMap<String, Thing>();
	
	/** The compiled scripts. */
	private static ConcurrentHashMap<String, CompiledScript> compiledScripts = new ConcurrentHashMap<String, CompiledScript>();
	
	/** The seeq sources. */
	private static ConcurrentHashMap<String, SEEQSource> seeqSources = new ConcurrentHashMap<String, SEEQSource>();
	
	/** The repository context. */
	private RepositoryContext repositoryContext = new RepositoryContext();
	
	/** The public contexts. */
	private HashSet<IRI> publicContexts = new HashSet<IRI>();
	
	/** The private contexts. */
	@Deprecated
	private HashSet<IRI> privateContexts = new HashSet<IRI>();

	/** The graphs. */
	private ConcurrentHashMap<IRI, Graph> graphs = new ConcurrentHashMap<IRI, Graph>();

	/** The sail. */
	IntelligentGraphSail sail;
	
	/** The intelligent graph connection. */
	private IntelligentGraphConnection intelligentGraphConnection;
	
	/** The path QL repositories. */
	private static HashMap<Integer, IntelligentGraphRepository> pathQLRepositories = new HashMap<Integer, IntelligentGraphRepository>();
	
	/** The repository reification context is lazy loaded. */
	private  Boolean repositoryReificationContextIsLazyLoaded=false;
	
	/** The repository prefix context is lazy loaded. */
	private  Boolean repositoryPrefixContextIsLazyLoaded=false;
	
	/**
	 * Creates the.
	 *
	 * @param repository the repository
	 * @param contexts the contexts
	 * @return the intelligent graph repository
	 */
	public static IntelligentGraphRepository create(org.eclipse.rdf4j.repository.Repository repository, Resource... contexts) {
		Integer key = repository.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			IntelligentGraphRepository pathQLRepository = new IntelligentGraphRepository(repository, contexts);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	/**
	 * Creates the.
	 *
	 * @param serverURL the server URL
	 * @param repositoryId the repository id
	 * @param contexts the contexts
	 * @return the intelligent graph repository
	 */
	public static IntelligentGraphRepository create(String serverURL, String repositoryId, Resource... contexts) {
		Integer key = (serverURL + repositoryId).hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			IntelligentGraphRepository pathQLRepository = new IntelligentGraphRepository(serverURL, repositoryId, contexts);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	/**
	 * Creates the.
	 *
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @return the intelligent graph repository
	 */
	public static IntelligentGraphRepository create(IntelligentGraphConnection intelligentGraphConnection) {
		IntelligentGraphSail sail = intelligentGraphConnection.getIntelligentGraphSail();
		Integer key = sail.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			IntelligentGraphRepository pathQLRepository = new IntelligentGraphRepository(intelligentGraphConnection);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	/**
	 * Creates the.
	 *
	 * @param tripleSource the triple source
	 * @return the intelligent graph repository
	 */
	public static IntelligentGraphRepository create(TripleSource tripleSource) {
		Integer key = tripleSource.hashCode();
		if (pathQLRepositories.containsKey(key)) {
			return pathQLRepositories.get(key);
		} else {
			IntelligentGraphRepository pathQLRepository = new IntelligentGraphRepository(tripleSource);
			pathQLRepositories.put(key, pathQLRepository);
			return pathQLRepository;
		}
	}

	/**
	 * Instantiates a new intelligent graph repository.
	 *
	 * @param tripleSource the triple source
	 */
	//Used via SPARQL Functions
	private IntelligentGraphRepository(TripleSource tripleSource) {
		this.tripleSource = tripleSource;
		this.modelBuilder = new ModelBuilder();
	}

	/**
	 * Instantiates a new intelligent graph repository.
	 *
	 * @param intelligentGraphConnection the intelligent graph connection
	 */
	private IntelligentGraphRepository(IntelligentGraphConnection intelligentGraphConnection) {
		this.repository = new SailRepository(intelligentGraphConnection.getIntelligentGraphSail());/////////////////
		this.intelligentGraphConnection = intelligentGraphConnection;
		this.tripleSource = new SailTripleSource(intelligentGraphConnection, true, null);
		this.modelBuilder = new ModelBuilder();
	//	initializePrefixes(intelligentGraphConnection);
	}

	/**
	 * Instantiates a new intelligent graph repository.
	 *
	 * @param repository the repository
	 * @param contexts the contexts
	 */
	public IntelligentGraphRepository(org.eclipse.rdf4j.repository.Repository repository, Resource... contexts) {
		this.repository = repository;
		//((IntelligentGraphSail)repository).getConnection().getIntelligentGraphSail();
		this.contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(this.contextAwareConnection);
		this.modelBuilder = new ModelBuilder();
	//	initializePrefixes(contextAwareConnection);
	}

	/**
	 * Instantiates a new intelligent graph repository.
	 *
	 * @param serverURL the server URL
	 * @param repositoryId the repository id
	 * @param contexts the contexts
	 */
	private IntelligentGraphRepository(String serverURL, String repositoryId, Resource... contexts) {
		this.repository = new HTTPRepository(serverURL, repositoryId);
		this.contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(this.contextAwareConnection);
		this.modelBuilder = new ModelBuilder();
	//	initializePrefixes(contextAwareConnection);

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
	
	/**
	 * Clear caches.
	 */
	@Deprecated
	public static void clearCaches() {
		Iterator<Entry<Integer, IntelligentGraphRepository>> iter = pathQLRepositories.entrySet().iterator(); 
		while (iter.hasNext()) {
			IntelligentGraphRepository entry = iter.next().getValue();
			entry.clearCache();
		}
//		for (IntelligentGraphRepository pathRepository : pathQLRepositories.values()) {
//			pathRepository.clearCache();
//		}
	}

	/**
	 * Gets the things.
	 *
	 * @return the things
	 */
	public ConcurrentHashMap<String, Thing> getThings() {
		return things;
	}

//	@Deprecated
//	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
//		return this.getReifications().getReificationTypes();
//	}
//
//	@Deprecated
//	ConcurrentHashMap<String, ReificationType> getPredicateReificationTypes() {
//		return this.getReifications().getPredicateReificationTypes();
//	}

//	@Deprecated
//	private void initializeReificationTypes() {
//		this.getReifications().initializeReificationTypes();
//	}

//	@Deprecated
//	public org.eclipse.rdf4j.repository.Repository getCacheRep() {
//		return this.cacheRep;
//	}

//	@Deprecated
//	public void setCacheRep(org.eclipse.rdf4j.repository.Repository cacheRep) {
//		this.cacheRep = cacheRep;
//	}

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
	 * @param cacheService the new cache service
	 */
	public void setCacheService(String cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * Sets the triple source.
	 *
	 * @param tripleSource the new triple source
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
	 * @param customQueryOptionsArray the custom query options array
	 * @return the custom query options
	 */
	public CustomQueryOptions getCustomQueryOptions(Value[] customQueryOptionsArray) {
		CustomQueryOptions customQueryOptions = CustomQueryOptions.create(this, customQueryOptionsArray);
		if (customQueryOptions != null) {
			if (customQueryOptions.contains("service")) {
				com.inova8.intelligentgraph.model.Resource service = customQueryOptions.get("service");
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
	}

	/**
	 * Compiled script factory.
	 *
	 * @param scriptString the script string
	 * @return the compiled script
	 * @throws ScriptException the script exception
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
	 * @param seeqServer the seeq server
	 * @return the SEEQ source
	 * @throws ScriptException the script exception
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
				logger.error("Failed to connect to SEEQSource '{}'", seeqServer);
				throw e;
			}
		}
	}
	
	/**
	 * Clear repository reification context.
	 */
	public void clearRepositoryReificationContext() {
		this.repositoryReificationContextIsLazyLoaded=false;		
	}
	
	/**
	 * Clear repository namespace context.
	 */
	public void clearRepositoryNamespaceContext() {
		this.repositoryPrefixContextIsLazyLoaded=false;		
	}
	
	/**
	 * Clear cache.
	 *
	 * @param args the args
	 */
	public void clearCache(Value... args) {

		if (this.getRepository() != null) {
			CloseableIteration<Statement, RepositoryException> statementIterator = this.getRepository().getConnection()
					.getStatements(iri(PATHQL.NAMESPACE), PATHQL.CLEARCACHE,
							literal(args));
			while (statementIterator.hasNext())
				statementIterator.next();
		}
	}

	/**
	 * Clear service cache.
	 *
	 * @param customQueryOptions the custom query options
	 */
	@SuppressWarnings("unused")
	private void clearServiceCache(ConcurrentHashMap<String, com.inova8.intelligentgraph.model.Resource> customQueryOptions) {
		if (connected()) {
			RepositoryResult<Statement> cacheStatements = cacheConnection.getStatements(null, SCRIPT.CACHE_DATE_TIME,
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
	 * @param customQueryOptions the custom query options
	 * @return the before
	 */
	private String getBefore(ConcurrentHashMap<String, com.inova8.intelligentgraph.model.Resource> customQueryOptions) {
		if (customQueryOptions != null && customQueryOptions.containsKey("before")) {
			com.inova8.intelligentgraph.model.Resource beforeDateTime = customQueryOptions.get("before");
			return beforeDateTime.getValue().toString().substring(1, 20);
		}
		return null;
	}

	/**
	 * Adds the service.
	 *
	 * @param service the service
	 * @return the string
	 */
	@SuppressWarnings("unused")
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
	 * @param namespace the namespace
	 * @param localName the local name
	 * @return the iri
	 */
	public static IRI createIRI(String namespace, String localName) {
		return iri(namespace, localName);
	}

	/**
	 * Creates the IRI.
	 *
	 * @param iri the iri
	 * @return the iri
	 */
	public IRI createIRI(String iri) {
		return iri(iri);
	}

	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public Prefixes getPrefixes() {
		return getRepositoryContext().getPrefixes();
	}

	/**
	 * Prefix.
	 *
	 * @param prefix the prefix
	 * @param IRI the iri
	 * @return the intelligent graph repository
	 */
	public IntelligentGraphRepository prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = Utilities.trimAndCheckIRIString(IRI);
		if (iri != null) {
//			if (this.getRepository() == null) {
//				//Only used for testing when not actual repository available
//				getRepositoryContext().getPrefixes().put(prefix, iri);
//			} else {
				try {
					RepositoryConnection connection = this.getRepository().getConnection();
					connection.setNamespace(prefix, iri.stringValue());
					getRepositoryContext().getPrefixes().put(prefix, iri);
					logger.debug("Added prefix {} for namespace {} ", prefix, iri);
				} catch (Exception qe) {
					throw new ServerException(FAILEDTOREMOVEGRAPH_EXCEPTION,
							String.format("Failed to add prefix/namespace", prefix, iri), qe);
				}
//			}
		} else {
			logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);
		}
		return this;
	}
	
	/**
	 * Prefix.
	 *
	 * @param IRI the iri
	 * @return the intelligent graph repository
	 */
	public IntelligentGraphRepository prefix(String IRI) {
		return prefix("", IRI);
	}

	/**
	 * Gets the reifications.
	 *
	 * @return the reifications
	 */
	public Reifications getReifications() {
		return getRepositoryContext().getReifications();
	}

	/**
	 * Gets the repository context.
	 *
	 * @return the repository context
	 */
	public RepositoryContext getRepositoryContext() {
		if(!repositoryReificationContextIsLazyLoaded) {
			repositoryContext.initializeReifications(repository.getConnection());
			repositoryReificationContextIsLazyLoaded=true;
		}
		if(!repositoryPrefixContextIsLazyLoaded) {
			repositoryContext.initializePrefixes(repository.getConnection());
			repositoryPrefixContextIsLazyLoaded=true;
		}
		return repositoryContext;
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
	 * @throws RepositoryException the repository exception
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private ContextAwareConnection publicContextAwareConnection() throws RepositoryException, IllegalArgumentException {
		ContextAwareConnection contextAwareConnection = new PathQLContextAwareConnection(repository);
		RepositoryResult<Resource> contextIDs = contextAwareConnection.getContextIDs();
		for (Resource contextID : contextIDs) {
			if (contextAwareConnection.hasStatement(contextID, SCRIPT.ISPRIVATE,
					literal(true), contextID)) {
				privateContexts.add((IRI) contextID);
			} else {
				getPublicContexts().add((IRI) contextID);
			}

		}
		return contextAwareConnection;
	}

	/**
	 * Gets the thing.
	 *
	 * @param iri the iri
	 * @return the thing
	 */
	public Thing getThing(IRI iri) {
		return Thing.create(this, iri, null);
	}

	/**
	 * Gets the thing.
	 *
	 * @param iri the iri
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public Thing getThing(String iri) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(getRepositoryContext(),iri).getIri();
		return getThing(thingIri);
	}

	/**
	 * Gets the thing.
	 *
	 * @param iri the iri
	 * @param customQueryOptions the custom query options
	 * @return the thing
	 */
	public Thing getThing(IRI iri, CustomQueryOptions customQueryOptions) {
		return Thing.create(this, iri, new EvaluationContext(customQueryOptions));
	}

	/**
	 * Gets the thing.
	 *
	 * @param iri the iri
	 * @param customQueryOptions the custom query options
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public Thing getThing(String iri, CustomQueryOptions customQueryOptions) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(getRepositoryContext(),iri).getIri();
		return getThing(thingIri, customQueryOptions);
	}

	/**
	 * Write model to cache.
	 *
	 * @param thing the thing
	 * @param result the result
	 * @param cacheContext the cache context
	 */
	public void writeModelToCache(Thing thing, Object result, IRI cacheContext) {
		if (connected()) {
			try {
				cacheConnection.clear(cacheContext);
				((Model) result).add(cacheContext, SCRIPT.CACHE_DATE_TIME,
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

	/**
	 * Adds the graph.
	 *
	 * @param graphName the graph name
	 * @return the graph
	 */
	public Graph addGraph(String graphName) {
		ContextAwareConnection connection = this.getContextAwareConnection();
		Model result;
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(getRepositoryContext(), graphName).getIri();
			result = new LinkedHashModel();
			((Model) result).add(graphNameIri, SCRIPT.CACHE_DATE_TIME,
					literal(new Date()), graphNameIri);
			((Model) result).add(graphNameIri, SCRIPT.ISPRIVATE, literal(true),
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

	/**
	 * Open graph.
	 *
	 * @param graphName the graph name
	 * @return the graph
	 */
	public Graph openGraph(String graphName) {
		RepositoryConnection connection = this.getRepository().getConnection();//.getContextAwareConnection();

		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(getRepositoryContext(), graphName).getIri();
			@SuppressWarnings("deprecation")
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

	/**
	 * Removes the graph.
	 *
	 * @param graphName the graph name
	 * @return the iri
	 */
	public IRI removeGraph(String graphName) {
		//RepositoryConnection connection = this.getRepository().getConnection();//.getContextAwareConnection();
		IRI graphNameIri = null;
		try(RepositoryConnection connection = this.getRepository().getConnection())  {
			graphNameIri = PathParser.parseIriRef(getRepositoryContext(), graphName).getIri();
			@SuppressWarnings("deprecation")
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if (contextExists) {
				Resource[] contexts = {graphNameIri};
				//connection.begin(IsolationLevels.SERIALIZABLE);
				//connection.clear(graphNameIri);
				//connection.clear();
				connection.remove((IRI) null, (IRI) null, null, contexts);
				//connection.remove(graphNameIri, (IRI) null, null,contexts);//graphNameIri);
				//connection.remove((Resource) null, (IRI) null, null);//graphNameIri);
				//connection.commit();
				
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

	/**
	 * Close graph.
	 *
	 * @param graphName the graph name
	 * @return the boolean
	 */
	@SuppressWarnings("deprecation")
	public Boolean closeGraph(String graphName) {
		RepositoryConnection connection = this.getContextAwareConnection();
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(getRepositoryContext(), graphName).getIri();
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

	/**
	 * Initialize contexts.
	 */
	public void initializeContexts() {
		publicContexts.clear();
		privateContexts.clear();
		RepositoryConnection connection = this.repository.getConnection();
		CloseableIteration<Resource, RepositoryException> connectionIDs = connection.getContextIDs();
		while (connectionIDs.hasNext()) {
			Resource connectionID = connectionIDs.next();
			CloseableIteration<Statement, RepositoryException> contextPrivacies = connection.getStatements(connectionID,
					SCRIPT.ISPRIVATE, null, false);
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

	/**
	 * Gets the public contexts.
	 *
	 * @return the public contexts
	 */
	@Deprecated
	public HashSet<IRI> getPublicContexts() {
		return publicContexts;
	}

	/**
	 * Gets the private contexts.
	 *
	 * @return the private contexts
	 */
	@Deprecated
	public HashSet<IRI> getPrivateContexts() {
		return privateContexts;
	}

	/**
	 * Gets the intelligent graph connection.
	 *
	 * @return the intelligent graph connection
	 */
	public IntelligentGraphConnection getIntelligentGraphConnection() {
		if(intelligentGraphConnection.isOpen())
			return intelligentGraphConnection;
		else {
			intelligentGraphConnection = intelligentGraphConnection.getIntelligentGraphSail().getConnection();
			return intelligentGraphConnection;
		}
	}


}
