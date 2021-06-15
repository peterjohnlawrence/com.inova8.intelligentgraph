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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Prefixes;
import pathCalc.Thing;
import pathQLRepository.PathQLRepository;


public class IntelligentGraphSail extends NotifyingSailWrapper {
	protected final Logger logger = LoggerFactory.getLogger(Thing.class);	
	public enum ResponseType { 
 SCRIPT, 
 TRACE, 
 VALUE};

	public static final String INTELLIGENTGRAPH_DEBUG = "DEBUG";
	
	public static final String SCRIPT_POSTFIX = "_SCRIPT";
	
	public static final String TRACE_POSTFIX = "_TRACE";
	
	public static final String SCRIPT = null;

	private final AtomicBoolean closed = new AtomicBoolean(false);

	protected final Properties parameters = new Properties();
	
	private final Prefixes prefixes=new Prefixes();
	private final HashSet<IRI> publicContexts=new HashSet<IRI>();
	private final HashSet<IRI> privateContexts=new HashSet<IRI>();	
	private  Boolean contextsAreLazyLoaded=false;
	private  Boolean prefixesAreLazyLoaded=false;
	public static final String URN_CUSTOM_QUERY_OPTIONS = "urn:customQueryOptions";	
	private final  IntelligentEvaluator intelligentEvaluator=new IntelligentEvaluator();

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
			if(connectionID.stringValue().startsWith(URN_CUSTOM_QUERY_OPTIONS)) {
				//Ignore any urn:customQueryOptions that might have been added to contexts
				
			}else {
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
	private boolean notTracing() {
		return false;
	}
	@Deprecated
	private EvaluationContext getEvaluationContext() {
		// TODO Auto-generated method stub
		return null;
	}
	public void clearCache() {
		intelligentEvaluator.clearCache(null);
		
	}
	public FactCache getFactCache() {
		return intelligentEvaluator.getFactCache();
	}
	public IRI convertQName(String predicateIRI) {
		predicateIRI=PathQLRepository.trimIRIString( predicateIRI);
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
	
}
