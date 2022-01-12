/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

import static org.eclipse.rdf4j.model.util.Values.iri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.model.CustomQueryOptions;
import com.inova8.intelligentgraph.repositoryContext.Prefixes;

import utilities.Utilities;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.Dataset;

public class EvaluationContext {
	
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(EvaluationContext.class);
	
	/** The tracer. */
	private Tracer tracer = new Tracer();
	
	/** The stack. */
	private volatile EvaluationStack stack = new EvaluationStack();
	
	/** The custom query options. */
	private CustomQueryOptions customQueryOptions ;

	
	/** The prefixes. */
	@Deprecated
	private Prefixes prefixes = new Prefixes();
	
	/** The dataset. */
	@Deprecated
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
	@Deprecated
	public void setPrefixes(Prefixes prefixes) {
		this.prefixes = prefixes;
	}
	@Deprecated
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
	
	@Deprecated
	public  CustomQueryOptions getCustomQueryOptions() {
		return customQueryOptions;
	}
	
	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	@Deprecated
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
		predicateIRI=Utilities.trimIRIString( predicateIRI);
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
				logger.error("Error identifying namespace of qName {}", predicateIRI);
				getTracer().traceQNameError( predicateIRI);
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

	@Deprecated
	protected boolean notTracing() {
		if (tracer != null)
			return false;
		else
			return true;
	}


	
	@Deprecated
	public String getTrace() {
		return tracer.getTraceHTML();

	}
	
	
	@Deprecated
	public void setTracing(boolean b) {
		if (tracer != null)
			tracer.setTracing(b);
		
	}

	@Deprecated
	public boolean isTracing() {
		if (tracer != null)
			return tracer.isTracing();
		else
			return false;
	}

	@Deprecated
	public Dataset getDataset() {
		return dataset;
	}

	@Deprecated
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	

	public org.eclipse.rdf4j.model.Resource[] getContexts() {
		if(contexts!=null)
			return contexts;
		else
			return new  org.eclipse.rdf4j.model.Resource[0];
	}

	public void setContexts(org.eclipse.rdf4j.model.Resource[] contexts) {
		this.contexts = contexts;
	}

}

