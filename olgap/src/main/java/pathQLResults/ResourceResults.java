/*
 * inova8 2020
 */
package pathQLResults;

import java.util.Iterator;
import java.lang.Iterable;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import pathCalc.EvaluationContext;
import pathCalc.Thing;
import pathCalc.Tracer;
import pathPatternElement.PathElement;
import pathQLModel.Fact;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

/**
 * The Class ResourceResults.
 */
public abstract class ResourceResults implements CloseableIteration<Resource, QueryEvaluationException> , Iterable<Resource>{
	
	/** The path element. */
	protected PathElement pathElement; 
	
	/** The thing. */
	protected Thing thing;
	
	/** The source. */
	protected PathQLRepository source;


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
	public ResourceResults( Thing thing, PathElement pathElement  ){

		this.thing = thing; 
		this.pathElement = pathElement;
	}
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 * @param source the source
	 * @param pathElement the path element
	 */
	public ResourceResults( PathQLRepository source,  PathElement pathElement  ){
		this.source = source; 
		this.pathElement = pathElement;
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
	public PathQLRepository getSource() {
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

	/**
	 * Gets the tracer.
	 *
	 * @return the tracer
	 */
	protected Tracer getTracer() {
		if(thing!=null) 
			return thing.getTracer();
		else 
			return null;
	}

	protected EvaluationContext getEvaluationContext() {
		if(thing!=null)
			return thing.getEvaluationContext();
		else 
			return null;
	}
	/**
	 * Gets the stack.
	 *
	 * @return the stack
	 */
	protected Stack<String> getStack() {
		if(thing!=null)
			return thing.getStack();
		else
			return null;
	}

	/**
	 * Gets the custom query options.
	 *
	 * @return the custom query options
	 */
	protected ConcurrentHashMap<String, Resource> getCustomQueryOptions() {
		if(thing!=null)
			return thing.getCustomQueryOptions();
		else 
			return null;
	}
	
	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	@Deprecated
	public IRI getPredicate() {
		return null;
	}
	
	/**
	 * Gets the reification type.
	 *
	 * @return the reification type
	 */
	@Deprecated
	public IRI getReificationType() {
		return null;
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
	public abstract Fact nextFact();	
	public abstract IRI nextReifiedValue();
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
}
