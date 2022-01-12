/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.model.IGSail;


public class IntelligentGraphSail extends NotifyingSailWrapper implements IGSail {
	protected final Logger logger = LoggerFactory.getLogger(IntelligentGraphSail.class);	
	public enum ResponseType { 
 SCRIPT, 
 TRACE, 
 VALUE};

	public static final String INTELLIGENTGRAPH_DEBUG = "DEBUG";
	
	public static final String SCRIPT_POSTFIX = "_SCRIPT";
	
	public static final String TRACE_POSTFIX = "_TRACE";

	private final AtomicBoolean closed = new AtomicBoolean(false);

	protected final Properties parameters = new Properties();
	
	private final Prefixes prefixes=new Prefixes();
	private final HashSet<IRI> publicContexts=new HashSet<IRI>();
	private final HashSet<IRI> privateContexts=new HashSet<IRI>();	
	private  Boolean contextsAreLazyLoaded=false;
	private  Boolean prefixesAreLazyLoaded=false;

	private final  IntelligentEvaluator intelligentEvaluator;

	public IntelligentGraphSail() {
		super();
		intelligentEvaluator=new IntelligentEvaluator(this);
	}
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
		setContextsAreLazyLoaded(true);
	}
	public void initializePrefixes() {
		IntelligentGraphConnection connection = this.getConnection();
		CloseableIteration<? extends Namespace, SailException> namespaces = connection.getNamespaces();
		while(namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}
		prefixesAreLazyLoaded=true;
	}
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
	public HashSet<IRI>  getPrivateContexts() {
		if(!contextsAreLazyLoaded) {
			initializeContexts();
		}
		return privateContexts;
	}
	public HashSet<IRI>  getPublicContexts() {
		if(!contextsAreLazyLoaded) {
			initializeContexts();
		}
		return publicContexts;
	}
	public Prefixes getPrefixes() {
		if(!prefixesAreLazyLoaded) {
			initializePrefixes();
		}
		return prefixes;
	}
	@Override
	public void initialize() throws SailException {
		super.initialize();
	}

	@Override
	public IntelligentGraphConnection getConnection() throws SailException {
		if (!closed.get()) {
			return new IntelligentGraphConnection(super.getConnection(),this);
		} else {
			throw new SailException("Sail is shut down or not initialized");
		}
	}
	
	@Override
	public ValueFactory getValueFactory() {
		return getBaseSail().getValueFactory();
	}
	public void clearCache() {
		intelligentEvaluator.clearCache();
		
	}
	public void clearCache(Value obj) {
		intelligentEvaluator.clearCache(obj);
	}
	public FactCache getFactCache() {
		return intelligentEvaluator.getFactCache();
	}
	public IRI convertQName(String predicateIRI) {
		predicateIRI=IntelligentGraphRepository.trimIRIString( predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":");
		IRI predicate = null;
		if(predicateIRIParts[0].equals("a")) {
				predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			}
		else if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		}else {
			IRI namespace = getNamespace(predicateIRIParts[0]);
			if(namespace==null) {
				String message = String.format("Error identifying namespace of qName %s", predicateIRI);			
				logger.error(message);
			//TODO	addTrace(message);
			}else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}

	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}
	public void setContextsAreLazyLoaded(Boolean contextsAreLazyLoaded) {
		this.contextsAreLazyLoaded = contextsAreLazyLoaded;
	}
	public void setPrefixesAreLazyLoaded(Boolean prefixesAreLazyLoaded) {
		this.prefixesAreLazyLoaded = prefixesAreLazyLoaded;
	}
	
}
