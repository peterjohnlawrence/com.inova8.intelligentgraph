/*
 * inova8 2020
 */
package pathQLRepository;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.eclipse.rdf4j.model.Resource;

import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQL.PathParser;

/**
 * The Class PathQLRepository.
 */
public class PathQLRepository {

	/** The Constant logger. */
	 private final Logger logger = LogManager.getLogger(PathQLRepository.class);
	
	/** The cache rep. */
	 private org.eclipse.rdf4j.repository.Repository cacheRep;
	
	/** The cache service. */
	 private String cacheService;
	
	/** The repository. */
	private  org.eclipse.rdf4j.repository.Repository repository;
	
	/** The cache connection. */
	 private RepositoryConnection cacheConnection;
	
	/** The triple source. */
	 private TripleSource tripleSource; //Not unique per call using the same underlying triplestore
	
	/** The model builder. */
	 private ModelBuilder modelBuilder;
	
	/** The things. */
	 private HashMap<String, Thing> things = new HashMap<String, Thing>();
	
	/** The facts. */
//	 private HashMap<String, HashMap<String, pathQLModel.Resource>> facts = new HashMap<String, HashMap<String, pathQLModel.Resource>>();
	
	/** The compiled scripts. */
	 private HashMap<String, CompiledScript> compiledScripts = new HashMap<String, CompiledScript>();
	
	/** The seeq sources. */
	 private HashMap<String, SEEQSource> seeqSources = new HashMap<String, SEEQSource>();
	
	/** The is lazy loaded. */
	 private Boolean isLazyLoaded = false;
	
	public void setIsLazyLoaded(Boolean isLazyLoaded) {
		this.isLazyLoaded = isLazyLoaded;
	}
	/** The reification types. */
	 private HashMap<String, ReificationType> reificationTypes = new HashMap<String, ReificationType>();
	
	/** The predicate reification types. */
	 private HashMap<String, ReificationType> predicateReificationTypes = new HashMap<String, ReificationType>();
	
	/** The prefixes. */
	 private HashMap<String, IRI> prefixes = new HashMap<String, IRI>();

	private  HashSet<IRI>  publicContexts= new HashSet<IRI>();

	private  HashSet<IRI>  privateContexts= new HashSet<IRI>();

	/**
	 * Instantiates a new path QL repository.
	 *
	 * @param tripleSource the triple source
	 */
	public PathQLRepository(TripleSource tripleSource) {
		this.tripleSource = tripleSource;
		this.modelBuilder = new ModelBuilder();
	}

	/**
	 * Instantiates a new path QL repository.
	 *
	 * @param repository the repository
	 */
	public PathQLRepository(org.eclipse.rdf4j.repository.Repository repository,  Resource... contexts) {
		this.repository = repository;
		//this.contexts = contexts;
		ContextAwareConnection contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(contextAwareConnection);
		this.modelBuilder = new ModelBuilder();
	}

	/**
	 * Instantiates a new path QL repository.
	 *
	 * @param repository the repository
	 */
	public PathQLRepository(String serverURL, String repositoryId ,  Resource... contexts) {
		this.repository =  new HTTPRepository(serverURL, repositoryId);
		//this.contexts = contexts;
		ContextAwareConnection contextAwareConnection = publicContextAwareConnection();
		this.tripleSource = new RepositoryTripleSource(contextAwareConnection);
		this.modelBuilder = new ModelBuilder();

	}
	public HashMap<String, Thing> getThings() {
		return things;
	}

	/**
	 * Gets the reification types.
	 *
	 * @return the reification types
	 */
	 HashMap<String, ReificationType> getReificationTypes() {
		if (!isLazyLoaded && this.getTripleSource() != null)
			initializeReificationTypes();
		return reificationTypes;
	}

	/**
	 * Gets the predicate reification types.
	 *
	 * @return the predicate reification types
	 */
	 HashMap<String, ReificationType> getPredicateReificationTypes() {
		if (!isLazyLoaded && this.getTripleSource() != null)
			initializeReificationTypes();
		return predicateReificationTypes;
	}

	/**
	 * Adds the reification type.
	 *
	 * @param reificationType the reification type
	 * @param reificationSubject the reification subject
	 * @param reificationPredicate the reification predicate
	 * @param reificationObject the reification object
	 * @param reificationIsSubjectOf the reification is subject of
	 * @param reificationIsPredicateOf the reification is predicate of
	 * @param reificationIsObjectOf the reification is object of
	 */
	public  void addReificationType(Resource reificationType, Resource reificationSubject,
			Resource reificationPredicate, Resource reificationObject, Resource reificationIsSubjectOf,
			Resource reificationIsPredicateOf, Resource reificationIsObjectOf) {
		ReificationType newReificationType = new ReificationType(reificationType, reificationSubject,
				reificationPredicate, reificationObject, reificationIsSubjectOf, reificationIsPredicateOf,
				reificationIsObjectOf);
		reificationTypes.put(((IRI) reificationType).stringValue(), newReificationType);
		predicateReificationTypes.put(((IRI) reificationPredicate).stringValue(), newReificationType);

	}

	/**
	 * Initialize reification types.
	 */
	private  void initializeReificationTypes() {
		StringBuilder initializedReifications = new StringBuilder(
				" reifications initialized: <" + Evaluator.RDF_STATEMENT + "> ");
		int initializedReification = 1;
		IRI RDFStatement = iri(Evaluator.RDF_STATEMENT);

		IRI RDFSsubPropertyOf = iri(Evaluator.RDFS_SUB_PROPERTY_OF);
		IRI RDFsubject = iri(Evaluator.RDF_SUBJECT);
		IRI RDFpredicate = iri(Evaluator.RDF_PREDICATE);
		IRI RDFobject = iri(Evaluator.RDF_OBJECT);
		IRI RDFSdomain = iri(Evaluator.RDFS_DOMAIN);
		IRI OWLinverseOf = iri(Evaluator.OWL_INVERSE_OF);

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
		this.logger.debug(initializedReification + initializedReifications.toString());
	}

	/**
	 * Gets the cache rep.
	 *
	 * @return the cache rep
	 */
	public  org.eclipse.rdf4j.repository.Repository getCacheRep() {
		return this.cacheRep;
	}

	/**
	 * Sets the cache rep.
	 *
	 * @param cacheRep the new cache rep
	 */
	public  void setCacheRep(org.eclipse.rdf4j.repository.Repository cacheRep) {
		this.cacheRep = cacheRep;
	}

	/**
	 * Gets the cache service.
	 *
	 * @return the cache service
	 */
	public  String getCacheService() {
		return this.cacheService;
	}

	/**
	 * Sets the cache service.
	 *
	 * @param cacheService the new cache service
	 */
	public  void setCacheService(String cacheService) {
		this.cacheService = cacheService;
	}

	/**
	 * Gets the triple source.
	 *
	 * @return the triple source
	 */
	public  TripleSource getTripleSource() {
		//PathQLRepository runs in two modes: when a triplesource is explicitly provided (as when invoked from SPARQL function) or when a repository is provided
		if(repository!=null) {
			return	new RepositoryTripleSource(getContextAwareConnection());
		}else if(tripleSource!=null) {
			return tripleSource;
		}else {
			return null;
		}
	}
	public   void setTripleSource(TripleSource tripleSource) {
		this.tripleSource = tripleSource;
	}


	/**
	 * Gets the model builder.
	 *
	 * @return the model builder
	 */
	public  ModelBuilder getModelBuilder() {
		return this.modelBuilder;
	}


	/**
	 * Gets the custom query options.
	 *
	 * @param customQueryOptionsArray the custom query options array
	 * @return the custom query options
	 */
	public HashMap<String, pathQLModel.Resource> getCustomQueryOptions(Value[] customQueryOptionsArray) {
		if (customQueryOptionsArray.length == 0) {
			return null;
		} else if (customQueryOptionsArray.length % 2 != 0) {
			logger.error(new ParameterizedMessage("Must have matching args tag/value pairs '{}'",
					customQueryOptionsArray.length));
			return null;
		} else {
			HashMap<String, pathQLModel.Resource> customQueryOptions = new HashMap<String, pathQLModel.Resource>();
			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
				String customQueryOptionValue = customQueryOptionsArray[customQueryOptionsArrayIndex + 1].stringValue();
				if (customQueryOptionValue != null && !customQueryOptionValue.isEmpty())
					customQueryOptions.put(customQueryOptionParameter,
							pathQLModel.Resource.create (this,literal( customQueryOptionValue), null));//TODO
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
					compiledScripts.put(scriptCode.toString(), compiledScriptCode);
					return compiledScriptCode;
				} else {
					throw new ScriptException(
							"Unrecognized script language:" + scriptString.getDatatype().getLocalName());
				}
			} catch (ScriptException e) {
				logger.error(new ParameterizedMessage("Failed to compile '{}' language  script of  with contents:\n {}",
						scriptString.getDatatype().getLocalName(), scriptString.toString()));
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
				//logger.error(String.format("Failed to compile '%s' language  script of  with contents:\n %s",scriptString.getDatatype().getLocalName(), scriptString.toString()));
				logger.error(new ParameterizedMessage("Failed to connect to SEEQSource '{}'", seeqServer));
				throw e;
			}
		}
	}

	/**
	 * Clear cache.
	 *
	 * @param args the args
	 */
	public void clearCache(Value... args) {
		things.clear();
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

	/**
	 * Clear service cache.
	 *
	 * @param customQueryOptions the custom query options
	 */
	private void clearServiceCache(HashMap<String, pathQLModel.Resource> customQueryOptions) {
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
			logger.error(new ParameterizedMessage("Failed to connect to clear cache"));
		}

	}

	/**
	 * Gets the before.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the before
	 */
	private String getBefore(HashMap<String, pathQLModel.Resource> customQueryOptions) {
		if (customQueryOptions != null && customQueryOptions.containsKey("before")) {
			pathQLModel.Resource beforeDateTime = customQueryOptions.get("before");
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
	 * Gets the predicate reification type.
	 *
	 * @param predicate the predicate
	 * @return the predicate reification type
	 */
	public  ReificationType getPredicateReificationType(String predicate) {
		if (getPredicateReificationTypes().containsKey(predicate)) {
			return getPredicateReificationTypes().get(predicate);
		} else {
			return null;
		}
	}

	/**
	 * Creates the IRI.
	 *
	 * @param iri the iri
	 * @return the iri
	 */
	public  IRI createIRI(String iri) {
		//	return getTripleSource().getValueFactory().createIRI( iri);
		return iri(iri);
	}

	/**
	 * Gets the reification type.
	 *
	 * @param reificationType the reification type
	 * @return the reification type
	 */
	private ReificationType getReificationType(String reificationType) {
		return getReificationTypes().get(reificationType);
	}

	/**
	 * Gets the reification subject.
	 *
	 * @param reificationType the reification type
	 * @return the reification subject
	 */
	public  IRI getReificationSubject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationSubject();
		else
			return null;
	}

	/**
	 * Gets the reification is subject of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is subject of
	 */
	public  IRI getReificationIsSubjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsSubjectOf();
		else
			return null;
	}

	/**
	 * Gets the reification predicate.
	 *
	 * @param reificationType the reification type
	 * @return the reification predicate
	 */
	public  IRI getReificationPredicate(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationPredicate();
		else
			return null;
	}

	/**
	 * Gets the reification is predicate of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is predicate of
	 */
	public  IRI getReificationIsPredicateOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsPredicateOf();
		else
			return null;
	}

	/**
	 * Gets the reification object.
	 *
	 * @param reificationType the reification type
	 * @return the reification object
	 */
	public  IRI getReificationObject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationObject();
		else
			return null;
	}

	/**
	 * Gets the reification is object of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is object of
	 */
	public  IRI getReificationIsObjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsObjectOf();
		else
			return null;
	}

	/**
	 * Gets the reification object.
	 *
	 * @param reificationType the reification type
	 * @return the reification object
	 */
	public  IRI getReificationObject(IRI reificationType) {
		return getReificationObject(reificationType.stringValue());
	}

	/**
	 * Gets the reification is object of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is object of
	 */
	public  IRI getReificationIsObjectOf(IRI reificationType) {
		return getReificationIsObjectOf(reificationType.stringValue());
	}

	/**
	 * Gets the reification subject.
	 *
	 * @param reificationType the reification type
	 * @return the reification subject
	 */
	public  IRI getReificationSubject(IRI reificationType) {
		return getReificationSubject(reificationType.stringValue());
	}

	/**
	 * Gets the reification is subject of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is subject of
	 */
	public  IRI getReificationIsSubjectOf(IRI reificationType) {
		return getReificationIsSubjectOf(reificationType.stringValue());
	}

	/**
	 * Gets the reification predicate.
	 *
	 * @param reificationType the reification type
	 * @return the reification predicate
	 */
	public  IRI getReificationPredicate(IRI reificationType) {
		return getReificationPredicate(reificationType.stringValue());
	}

	/**
	 * Gets the reification is predicate of.
	 *
	 * @param reificationType the reification type
	 * @return the reification is predicate of
	 */
	public  IRI getReificationIsPredicateOf(IRI reificationType) {
		return getReificationIsPredicateOf(reificationType.stringValue());
	}

	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public  HashMap<String, IRI> getPrefixes() {
		return prefixes;
	}

	/**
	 * Sets the prefixes.
	 *
	 * @param prefixes the prefixes
	 */
	public  void setPrefixes(HashMap<String, IRI> prefixes) {
		this.prefixes = prefixes;
	}

	/**
	 * Prefix.
	 *
	 * @param prefix the prefix
	 * @param IRI the iri
	 * @return the path QL repository
	 */
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
	 * @param IRI the iri
	 * @return the path QL repository
	 */
	public PathQLRepository prefix(String IRI) {
		return this.prefix("", IRI);
	}

	/**
	 * Trim and check IRI string.
	 *
	 * @param IRI the iri
	 * @return the iri
	 */
	public static IRI trimAndCheckIRIString(String IRI) {
		return iri(trimIRIString(IRI));
	}

	/**
	 * Trim IRI string.
	 *
	 * @param IRI the iri
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
	 * @param predicateIRI the predicate IRI
	 * @param localPrefixes the local prefixes
	 * @return the iri
	 */
	public  IRI convertQName(String predicateIRI, HashMap<String, IRI> localPrefixes) {
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
				logger.error(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
			} else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}

	/**
	 * Gets the namespace.
	 *
	 * @param namespaceString the namespace string
	 * @param localPrefixes the local prefixes
	 * @return the namespace
	 */
	private  IRI getNamespace(String namespaceString, HashMap<String, IRI> localPrefixes) {
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
	public  org.eclipse.rdf4j.repository.Repository getRepository() {
		return repository;
	}
	public  ContextAwareConnection getContextAwareConnection() {
		if(repository!=null) {
			ContextAwareConnection contextAwareConnection =  new ContextAwareConnection(repository);
			if(getPublicContexts().size() >0) {
				IRI[] publicContextsArray = {} ;
				contextAwareConnection.setReadContexts( getPublicContexts().toArray(publicContextsArray));
			}
			return contextAwareConnection;
		}	else {
			return null;
		}
	}

	private  ContextAwareConnection publicContextAwareConnection()
			throws RepositoryException, IllegalArgumentException {
		ContextAwareConnection contextAwareConnection = new ContextAwareConnection(repository);
		 RepositoryResult<Resource> contextIDs = contextAwareConnection.getContextIDs();
		 for(Resource contextID: contextIDs ) {
			if(contextAwareConnection.hasStatement(contextID , iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE), literal(true ),  contextID)) {
				privateContexts.add((IRI)contextID);
			}else {
				getPublicContexts().add((IRI)contextID);
			}

		 }
		return contextAwareConnection;
	}
//	public Thing getThing(IRI iri,HashMap<String, pathQLModel.Resource> customQueryOptions, HashMap<String,IRI> prefixes) {
//		return new Thing(this, iri, customQueryOptions,prefixes);
//	}
	public Thing getThing(IRI iri,HashMap<String, pathQLModel.Resource> customQueryOptions) {
		return Thing.create(this, iri,null );
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
				((Model) result).add(cacheContext, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),
						literal(new Date()), cacheContext);
				cacheConnection.add((Model) result, cacheContext);
				thing.addTrace(new ParameterizedMessage("Results cached to service {} in graph {}",
						addService(this.getCacheService()), addService(cacheContext.stringValue())));
			} catch (Exception e) {
				logger.error(new ParameterizedMessage(
						"Failed to write results to cache  {} with context \n {} with exception {}", result.toString(),
						cacheContext), e);
				thing.addTrace(new ParameterizedMessage("Results NOT cached to service:{}",
						addService(this.getCacheService())));
			}
		} else {
			thing.addTrace(new ParameterizedMessage("No service to cached results"));
		}
	}
	public Graph addGraph(String graphName ) {
		ContextAwareConnection connection = this.getContextAwareConnection();
		Model result;
		IRI graphNameIri = null;
		try {
			graphNameIri = 	PathParser.parseIriRef(this, graphName).getIri();
			result = new LinkedHashModel();
			((Model) result).add(graphNameIri, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.CACHE_DATE_TIME),	literal(new Date()), graphNameIri);
			((Model) result).add(graphNameIri, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE),	literal(true), graphNameIri);
			connection.add((Model) result, graphNameIri);
			getPublicContexts().add(graphNameIri);
			 
			logger.debug(new ParameterizedMessage("Added new graph {} ", graphNameIri.stringValue()));
		} catch (Exception e) {
			logger.error(new ParameterizedMessage("Failed to add graph {} with exception {}", graphNameIri.stringValue(),e.getMessage()));
		}
		return new Graph(this, graphNameIri);
	}
	public Graph openGraph(String graphName ) {
		RepositoryConnection connection = this.getContextAwareConnection();
		Model result;
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this,graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if(!contextExists) {
				addGraph( graphName );
			}
			logger.debug(new ParameterizedMessage("Got graph {} ", graphNameIri.stringValue()));
		} catch (Exception e) {
			logger.error(new ParameterizedMessage("Failed to get graph {} with exception {}", graphNameIri.stringValue(),e.getMessage()));
		}
		return new Graph(this, graphNameIri);
	}
	public IRI removeGraph(String graphName ) {
		RepositoryConnection connection = this.getContextAwareConnection();
		IRI graphNameIri = null;
		try {
			graphNameIri = PathParser.parseIriRef(this,graphName).getIri();
			Boolean contextExists = connection.getContextIDs().asList().contains(graphNameIri);
			if(contextExists) {
				connection.clear(graphNameIri);
				logger.debug(new ParameterizedMessage("Removed graph {} ", graphNameIri.stringValue()));
			}

		} catch (Exception e) {
			logger.error(new ParameterizedMessage("Failed to remove graph {} with exception {}", graphNameIri.stringValue(),e.getMessage()));
		}
		return graphNameIri;
	}
	public Boolean closeGraph(String graphName ) {
		IRI graphNameIri = null;
		try {

			graphNameIri = PathParser.parseIriRef(this,graphName).getIri();
			getPublicContexts().remove(graphNameIri);
			return true;
		} catch (Exception e) {
			logger.error(new ParameterizedMessage("Failed to close graph {} with exception {}", graphNameIri.stringValue(),e.getMessage()));
		}
		return false;
	}

	public HashSet<IRI> getPublicContexts() {
		return publicContexts;
	}
}
