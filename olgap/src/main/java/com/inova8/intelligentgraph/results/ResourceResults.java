/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.results;

import java.util.Iterator;
import java.lang.Iterable;
import java.util.Stack;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.element.PathElement;

/**
 * The Class ResourceResults.
 */
public abstract class ResourceResults implements CloseableIteration<Resource, QueryEvaluationException> , Iterable<Resource>{
	protected CustomQueryOptions customQueryOptions; 
	
	/** The path element. */
	protected PathElement pathElement; 
	
	/** The thing. */
	protected Thing thing;
	
	/** The source. */
	protected IntelligentGraphRepository source;


	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 */
	public ResourceResults() {
	}
	
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 * @param thing the thing
	 */
	public ResourceResults( Thing thing ){
		this.thing = thing; 
	}
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public ResourceResults( Thing thing, PathElement pathElement, CustomQueryOptions customQueryOptions ){

		this.thing = thing; 
		this.pathElement = pathElement;
		this.customQueryOptions = customQueryOptions;
	}
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 * @param source the source
	 * @param pathElement the path element
	 */
	public ResourceResults( IntelligentGraphRepository source,  PathElement pathElement ,CustomQueryOptions customQueryOptions ){
		this.source = source; 
		this.pathElement = pathElement;
		this.customQueryOptions = customQueryOptions;
	}
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param thing the thing
	 * @param reificationType the reification type
	 * @param predicate the predicate
	 */
	@Deprecated
	public ResourceResults(Thing thing,IRI reificationType, IRI predicate 
	){

		this.thing = thing; 

	}
	public IntelligentGraphRepository getSource() {
		if(source!=null)
			return source;
		else if(thing!=null)
			return thing.getSource();
		else
			return null;
	}

	/**
	 * Gets the thing.
	 *
	 * @return the thing
	 */
	public Thing getThing() {
		return thing;
	}

//	protected Tracer getTracer() {
//		if(thing!=null) 
//			return thing.getTracer();
//		else 
//			return null;
//	}

	protected EvaluationContext getEvaluationContext() {
		if(thing!=null)
			return thing.getEvaluationContext();
		else 
			return null;
	}

	protected Stack<String> getStack() {
		if(thing!=null)
			return thing.getStack();
		else
			return null;
	}

	protected CustomQueryOptions getCustomQueryOptions() {
		return this.customQueryOptions;
	}

	
	/**
	 * Gets the path element.
	 *
	 * @return the path element
	 */
	public PathElement getPathElement() {
		return pathElement;
	}
	
	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	@Override
	public Iterator<Resource> iterator() {
		return new CloseableIterationIterator<>(this);
	}

	public abstract Resource nextResource();
//	public abstract Fact nextFact();	
//	public abstract IRI nextReifiedValue();
	/**
	 * Count.
	 *
	 * @return the integer
	 */
	public Integer count() {
		Integer count = 0;
		while( hasNext()) {
			next();
			count++;
		}
		return count; 
	}
	
	/**
	 * Average.
	 *
	 * @return the double
	 */
	public Double average() {
		Integer count = 0;
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
			count++;
		}
		return total/count; 
	}
	
	/**
	 * Total.
	 *
	 * @return the double
	 */
	public Double total() {
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
		}
		return total;
	 
	}
	public String toString() {
		String toString="[";
		while( hasNext()) {
			toString +=next().toString()+";";
		}
		return toString+"]";
	}
}
