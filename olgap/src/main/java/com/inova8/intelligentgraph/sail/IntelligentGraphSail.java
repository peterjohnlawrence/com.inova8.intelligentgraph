/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.evaluator.FactCache;
import com.inova8.intelligentgraph.evaluator.IntelligentEvaluator;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;


/**
 * The Class IntelligentGraphSail.
 */
public class IntelligentGraphSail extends NotifyingSailWrapper {
	
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(Thing.class);	
	
	/**
	 * The Enum ResponseType.
	 */
	public enum ResponseType { 
 
 /** The script. */
 SCRIPT, 
 
 /** The trace. */
 TRACE, 
 
 /** The value. */
 VALUE};

	/** The Constant INTELLIGENTGRAPH_DEBUG. */
	public static final String INTELLIGENTGRAPH_DEBUG = "DEBUG";
	
	/** The Constant SCRIPT_POSTFIX. */
	public static final String SCRIPT_POSTFIX = "_SCRIPT";
	
	/** The Constant TRACE_POSTFIX. */
	public static final String TRACE_POSTFIX = "_TRACE";

	/** The closed. */
	private final AtomicBoolean closed = new AtomicBoolean(false);

	/** The parameters. */
	protected final Properties parameters = new Properties();
	
	/** The public contexts. */
	private final HashSet<IRI> publicContexts=new HashSet<IRI>();
	
	/** The private contexts. */
	private final HashSet<IRI> privateContexts=new HashSet<IRI>();	
	
	/** The contexts are lazy loaded. */
	private  Boolean contextsAreLazyLoaded=false;
	
	/** The intelligent evaluator. */
	private final  IntelligentEvaluator intelligentEvaluator;

	/**
	 * Instantiates a new intelligent graph sail.
	 */
	public IntelligentGraphSail() {
		super();
		intelligentEvaluator=new IntelligentEvaluator(this);
	}
	
	/**
	 * Initialize contexts.
	 */
	public void initializeContexts() {
		publicContexts.clear();
		privateContexts.clear();
		IntelligentGraphConnection connection = this.getConnection();
		CloseableIteration<? extends Resource, SailException> connectionIDs = connection.getContextIDs();
		while(connectionIDs.hasNext()) {
			Resource connectionID = connectionIDs.next();
			if(connectionID.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS)) {
				//Ignore any urn:customQueryOptions that might have been added to contexts
				
			}else {
				CloseableIteration<? extends IntelligentStatement, SailException> contextPrivacies = connection.getStatements(connectionID, SCRIPT.ISPRIVATE, null, false);
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
	//	IntelligentGraphRepository.create(this).clearRepositoryNamespaceContext();
	//	IntelligentGraphRepository.create(this).clearRepositoryNamespaceContext();
	}
//	public void initializePrefixes() {
//		IntelligentGraphConnection connection = this.getConnection();
//		CloseableIteration<? extends Namespace, SailException> namespaces = connection.getNamespaces();
//		while(namespaces.hasNext()) {
//			Namespace namespace = namespaces.next();
//			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
//		}
//		prefixesAreLazyLoaded=true;
//	}
	/**
 * Sets the parameter.
 *
 * @param key the key
 * @param value the value
 */
	public void setParameter(String key, String value) {
		parameters.setProperty(key, value);
	}
	
	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public Properties getParameters() {
		return parameters;
	}
	
	/**
	 * Gets the private contexts.
	 *
	 * @return the private contexts
	 */
	public HashSet<IRI>  getPrivateContexts() {
		if(!contextsAreLazyLoaded) {
			initializeContexts();
		}
		return privateContexts;
	}
	
	/**
	 * Gets the public contexts.
	 *
	 * @return the public contexts
	 */
	public HashSet<IRI>  getPublicContexts() {
		if(!contextsAreLazyLoaded) {
			initializeContexts();
		}
		return publicContexts;
	}
//	public Prefixes getPrefixes() {
//		if(!prefixesAreLazyLoaded) {
//			initializePrefixes();
//		}
//		return prefixes;
/**
 * Initialize.
 *
 * @throws SailException the sail exception
 */
//	}
	@Override
	public void initialize() throws SailException {
		super.initialize();
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 * @throws SailException the sail exception
	 */
	@Override
	public IntelligentGraphConnection getConnection() throws SailException {
		if (!closed.get()) {
			return new IntelligentGraphConnection(super.getConnection(),this);
		} else {
			throw new SailException("Sail is shut down or not initialized");
		}
	}
	
	/**
	 * Gets the value factory.
	 *
	 * @return the value factory
	 */
	@Override
	public ValueFactory getValueFactory() {
		return getBaseSail().getValueFactory();
	}
	
	/**
	 * Clear cache.
	 */
	public void clearCache() {
		intelligentEvaluator.clearCache();
		
	}
	
	/**
	 * Clear cache.
	 *
	 * @param obj the obj
	 */
	public void clearCache(Value obj) {
		intelligentEvaluator.clearCache(obj);
	}
	
	/**
	 * Gets the fact cache.
	 *
	 * @return the fact cache
	 */
	public FactCache getFactCache() {
		return intelligentEvaluator.getFactCache();
	}
//	public IRI convertQName(String predicateIRI) {
//		predicateIRI=Utilities.trimIRIString( predicateIRI);
//		String[] predicateIRIParts = predicateIRI.split(":");
//		IRI predicate = null;
//		if(predicateIRIParts[0].equals("a")) {
//				predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
//			}
//		else if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
//			predicate = iri(predicateIRI);
//		}else {
//			IRI namespace = getNamespace(predicateIRIParts[0]);
//			if(namespace==null) {
//				String message = String.format("Error identifying namespace of qName %s", predicateIRI);			
//				logger.error(message);
//			//TODO	addTrace(message);
//			}else {
//				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
//			}
//		}
//		return predicate;
//	}

//	private IRI getNamespace(String namespaceString) {
//		IRI namespace = getPrefixes().get(namespaceString);
//		return namespace;
//	}
//	public void setContextsAreLazyLoaded(Boolean contextsAreLazyLoaded) {
//		this.contextsAreLazyLoaded = contextsAreLazyLoaded;
//	}
//	public void setPrefixesAreLazyLoaded(Boolean prefixesAreLazyLoaded) {
//	}
	
}
