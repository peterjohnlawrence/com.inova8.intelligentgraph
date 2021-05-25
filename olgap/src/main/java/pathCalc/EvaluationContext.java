/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.Dataset;

import pathQLRepository.PathQLRepository;

/**
 * The Class EvaluationContext.
 */
public class EvaluationContext {
	
	/** The logger. */
	protected final Logger logger = LogManager.getLogger(EvaluationContext.class);
	
	/** The tracer. */
	private Tracer tracer = new Tracer();
	
	/** The stack. */
	private volatile EvaluationStack stack = new EvaluationStack();
	
	/** The custom query options. */
	private CustomQueryOptions customQueryOptions = new   CustomQueryOptions();	
	
	/** The prefixes. */
	private Prefixes prefixes = new Prefixes();
	
	/** The dataset. */
	private Dataset dataset;
	
	/** The contexts. */
	private org.eclipse.rdf4j.model.Resource[] contexts;

	/**
	 * Instantiates a new evaluation context.
	 *
	 * @param tracer the tracer
	 * @param stack the stack
	 * @param customQueryOptions the custom query options
	 * @param prefixes the prefixes
	 */
	//	private HashMap<String, IRI> defaultPrefixes = new HashMap<String, IRI>();
	public EvaluationContext(Tracer tracer, EvaluationStack stack,CustomQueryOptions customQueryOptions,
			Prefixes prefixes) {
		super();
		this.tracer = tracer;
		this.stack = stack;
		this.customQueryOptions = customQueryOptions;
		this.prefixes = prefixes;
	}
	
	/**
	 * Instantiates a new evaluation context.
	 *
	 * @param customQueryOptions the custom query options
	 */
	public EvaluationContext(CustomQueryOptions customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}
	
	/**
	 * Instantiates a new evaluation context.
	 */
	public EvaluationContext() {
	}
	
	/**
	 * Instantiates a new evaluation context.
	 *
	 * @param customQueryOptions the custom query options
	 * @param dataset the dataset
	 */
	public EvaluationContext(CustomQueryOptions customQueryOptions, Dataset dataset) {
		this.customQueryOptions = customQueryOptions;
		this.dataset =dataset;
	}
	
	/**
	 * Instantiates a new evaluation context.
	 *
	 * @param customQueryOptions the custom query options
	 * @param dataset the dataset
	 * @param prefixes the prefixes
	 */
	public EvaluationContext(CustomQueryOptions customQueryOptions, Dataset dataset, Prefixes prefixes) {
		this.customQueryOptions = customQueryOptions;
		this.dataset =dataset;
		this.prefixes = prefixes;
	}
	
	/**
	 * Instantiates a new evaluation context.
	 *
	 * @param prefixes the prefixes
	 * @param contexts the contexts
	 */
	public EvaluationContext(Prefixes prefixes, org.eclipse.rdf4j.model.Resource[] contexts) {
		this.prefixes = prefixes;
		this.contexts = contexts;
	}
	
	/**
	 * Sets the prefixes.
	 *
	 * @param prefixes the prefixes
	 */
	public void setPrefixes(Prefixes prefixes) {
		this.prefixes = prefixes;
	}
	public void setCustomQueryOptions(CustomQueryOptions customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}
	/**
	 * Gets the tracer.
	 *
	 * @return the tracer
	 */
	public Tracer getTracer() {
		return tracer;
	}
	
	/**
	 * Sets the tracer.
	 *
	 * @param tracer the new tracer
	 */
	public void  setTracer(Tracer tracer) {
		this.tracer = tracer;
	}
	
	/**
	 * Gets the stack.
	 *
	 * @return the stack
	 */
	public EvaluationStack getStack() {
		if(stack==null) {
			stack = new  EvaluationStack();
		}
		return stack;
	}
	
	/**
	 * Sets the stack.
	 *
	 * @param stack the new stack
	 */
	public void setStack(EvaluationStack stack) {
		this.stack = stack;
	}
	
	/**
	 * Gets the custom query options.
	 *
	 * @return the custom query options
	 */
	public  CustomQueryOptions getCustomQueryOptions() {
		return customQueryOptions;
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
	 * Convert Q name.
	 *
	 * @param predicateIRI the predicate IRI
	 * @return the iri
	 */
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
				logger.error(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
				addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
			}else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}
	
	/**
	 * Gets the namespace.
	 *
	 * @param namespaceString the namespace string
	 * @return the namespace
	 */
	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}

	
	/**
	 * Not tracing.
	 *
	 * @return true, if successful
	 */
	protected boolean notTracing() {
		if (tracer != null)
			return false;
		else
			return true;
	}

	/**
	 * Adds the trace.
	 *
	 * @param message the message
	 */
	public void addTrace(Message message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	/**
	 * Adds the trace.
	 *
	 * @param message the message
	 */
	protected void addTrace(String message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	/**
	 * Adds the script.
	 *
	 * @param script the script
	 */
	protected void addScript(String script) {
		if (tracer != null)
			tracer.addScript(script);
	}
	
	/**
	 * Decrement trace level.
	 */
	protected void decrementTraceLevel() {
		if (tracer != null)
			tracer.decrementLevel();
	}

	/**
	 * Increment trace level.
	 */
	protected void incrementTraceLevel() {
		if (tracer != null)
			tracer.incrementLevel();
	}
	
	/**
	 * Gets the trace.
	 *
	 * @return the trace
	 */
	public String getTrace() {
		return tracer.getTrace();

	}
	
	/**
	 * Indent script for trace.
	 *
	 * @param script the script
	 * @return the string
	 */
	protected String indentScriptForTrace(String script) {
		if (tracer != null)
			return tracer.indentScriptForTrace(script);
		else
			return null;
	}
	
	/**
	 * Sets the tracing.
	 *
	 * @param b the new tracing
	 */
	public void setTracing(boolean b) {
		if (tracer != null)
			tracer.setTracing(b);
		
	}
	
	/**
	 * Checks if is tracing.
	 *
	 * @return true, if is tracing
	 */
	public boolean isTracing() {
		if (tracer != null)
			return tracer.isTracing();
		else
			return false;
	}
	
	/**
	 * Gets the dataset.
	 *
	 * @return the dataset
	 */
	public Dataset getDataset() {
		return dataset;
	}
	
	/**
	 * Sets the dataset.
	 *
	 * @param dataset the new dataset
	 */
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	
	/**
	 * Gets the contexts.
	 *
	 * @return the contexts
	 */
	public org.eclipse.rdf4j.model.Resource[] getContexts() {
		return contexts;
	}

}

