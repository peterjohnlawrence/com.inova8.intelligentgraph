/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.pathql.context.Prefixes;
import org.eclipse.rdf4j.query.Dataset;

public class EvaluationContext {
	
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(EvaluationContext.class);
	
	/** The tracer. */
	private Tracer tracer = new Tracer();
	
	/** The stack. */
	private volatile EvaluationStack stack = new EvaluationStack();
	
	/** The custom query options. */
	private CustomQueryOptions customQueryOptions = new   CustomQueryOptions();	
	
	
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
	//	this.prefixes = prefixes;
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
	 * @param prefixes the prefixes
	 * @param contexts the contexts
	 */
	public EvaluationContext(Prefixes prefixes, org.eclipse.rdf4j.model.Resource[] contexts) {
	//	this.prefixes = prefixes;
		this.contexts = contexts;
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

