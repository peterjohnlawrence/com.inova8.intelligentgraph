/*
 * inova8 2020
 */
package intelligentGraph;

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

import pathCalc.Evaluator;
import pathCalc.Prefixes;


/**
 * The Class IntelligentGraphSail.
 */
public class IntelligentGraphSail extends NotifyingSailWrapper {
	
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
	
	/** The Constant SCRIPT. */
	public static final String SCRIPT = null;
	
	/** The closed. */
	private final AtomicBoolean closed = new AtomicBoolean(false);
	
	/** The parameters. */
	protected final Properties parameters = new Properties();
	
	/** The evaluator. */
	//protected final Evaluator evaluator = new Evaluator();
	private final Prefixes prefixes=new Prefixes();
	private final HashSet<IRI> publicContexts=new HashSet<IRI>();
	private final HashSet<IRI> privateContexts=new HashSet<IRI>();	
	private  Boolean isLazyLoaded=false;

	public static final String URN_CUSTOM_QUERY_OPTIONS = "urn:customQueryOptions";	
	/**
	 * Instantiates a new intelligent graph sail.
	 */
	public IntelligentGraphSail() {
		super();

	}
	public void initializeContexts() {
		publicContexts.clear();
		privateContexts.clear();
		IntelligentGraphConnection connection = this.getConnection();
		CloseableIteration<? extends Resource, SailException> connectionIDs = connection.getContextIDs();
		while(connectionIDs.hasNext()) {
			Resource connectionID = connectionIDs.next();
			CloseableIteration<? extends IntelligentStatement, SailException> contextPrivacies = connection.getStatements(connectionID, iri(Evaluator.SCRIPTNAMESPACE, Evaluator.ISPRIVATE), null, false);
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
	public void initializePrefixes() {
		IntelligentGraphConnection connection = this.getConnection();
		CloseableIteration<? extends Namespace, SailException> namespaces = connection.getNamespaces();
		while(namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}
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
		if(!isLazyLoaded) {
			initializeContexts();
			initializePrefixes();
			isLazyLoaded=true;
		}
		return privateContexts;
	}
	public HashSet<IRI>  getPublicContexts() {
		if(!isLazyLoaded) {
			initializeContexts();
			initializePrefixes();
			isLazyLoaded=true;
		}
		return publicContexts;
	}
	public Prefixes getPrefixes() {
		if(!isLazyLoaded) {
			initializeContexts();
			initializePrefixes();
			isLazyLoaded=true;
		}
		return prefixes;
	}
	/**
	 * Initialize.
	 *
	 * @throws SailException the sail exception
	 */
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

	
}
