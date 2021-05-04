/*
 * inova8 2020
 */
package pathQLResults;

import java.util.HashMap;
import java.util.Iterator;
import java.lang.Iterable;
import java.util.Stack;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.BindingSet;

import pathCalc.EvaluationContext;
import pathCalc.Thing;
import pathCalc.Tracer;
import pathPatternElement.PathElement;
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

	/** The resource set. */
	CloseableIteration<BindingSet, QueryEvaluationException> resourceSet;

	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 */
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet) {
		this.resourceSet =resourceSet;
	}
	
	/**
	 * Instantiates a new resource results.
	 *
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
	 */
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing ){
		this.resourceSet =resourceSet;
		this.thing = thing; 
	}
	
	/**
	 * Instantiates a new resource results.
	 *
	 * @param resourceSet the resource set
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing, PathElement pathElement  ){
		this.resourceSet =resourceSet;
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
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet, PathQLRepository source,  PathElement pathElement  ){
		this.resourceSet =resourceSet;
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
	
	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return getResourceSet().hasNext();
	}

	/**
	 * Next.
	 *
	 * @return the resource
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public Resource next() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		//return thing.getSource().resourceFactory(getTracer(), next.getValue(getPathElement().getTargetVariable().getName()), getStack(), getCustomQueryOptions(),getPrefixes());
		return Resource.create(thing.getSource(), next.getValue(getPathElement().getTargetVariable().getName()), getEvaluationContext());
	}

	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		resourceSet.remove();	
	}

	/**
	 * Close.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void close() throws QueryEvaluationException {
		resourceSet.close();		
	}

	/**
	 * Gets the statements.
	 *
	 * @return the statements
	 */
	protected	CloseableIteration<BindingSet, QueryEvaluationException> getStatements() {
		return (CloseableIteration<BindingSet, QueryEvaluationException>) resourceSet;
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
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

	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
//	protected HashMap<String, IRI> getPrefixes() {
//		if(thing!=null)
//			return thing.getPrefixes();
//		else 
//			return getSource().getPrefixes();
//	}
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
	protected HashMap<String, Resource> getCustomQueryOptions() {
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
	
	/**
	 * Next binding set.
	 *
	 * @return the binding set
	 */
	public BindingSet nextBindingSet() {
		return getResourceSet().next();
	}

	/**
	 * Gets the resource set.
	 *
	 * @return the resource set
	 */
	public CloseableIteration<BindingSet, QueryEvaluationException> getResourceSet() {
		return resourceSet;
	}
	
	/**
	 * Next resource.
	 *
	 * @return the resource
	 */
	public abstract Resource nextResource();
	
	public Integer count() {
		Integer count = 0;
		while( hasNext()) {
			count++;
		}
		return count; 
	}
	public Double average() {
		Integer count = 0;
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
			count++;
		}
		return total/count; 
	}
	public Double total() {
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
		}
		return total;
	 
	}
}
