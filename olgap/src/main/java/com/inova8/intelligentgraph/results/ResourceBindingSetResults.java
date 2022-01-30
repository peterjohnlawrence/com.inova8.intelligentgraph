/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.results;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.element.PathElement;


/**
 * The Class ResourceBindingSetResults.
 */
public abstract class ResourceBindingSetResults extends ResourceResults {
	
	/** The resource set. */
	CloseableIteration<BindingSet, QueryEvaluationException> resourceSet;
	
	/**
	 * Instantiates a new resource binding set results.
	 *
	 * @param resourceSet the resource set
	 * @param source the source
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,
			IntelligentGraphRepository source, PathElement pathElement,CustomQueryOptions customQueryOptions) {

		super( source, pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	
	/**
	 * Instantiates a new resource binding set results.
	 *
	 * @param resourceSet the resource set
	 * @param thing the thing
	 */
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing ){
		super( thing);
		this.resourceSet =resourceSet;
	}
	
	/**
	 * Instantiates a new resource binding set results.
	 *
	 * @param resourceSet the resource set
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing, PathElement pathElement,CustomQueryOptions customQueryOptions  ){
		super( thing,pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	
	/**
	 * Instantiates a new resource binding set results.
	 *
	 * @param resourceSet the resource set
	 */
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet) {
		super();
		this.resourceSet =resourceSet;
	}
	
	/**
	 * Next resource.
	 *
	 * @return the resource
	 */
	public abstract Resource nextResource();

	/**
	 * Gets the statements.
	 *
	 * @return the statements
	 */
	protected	CloseableIteration<BindingSet, QueryEvaluationException> getStatements() {
		return (CloseableIteration<BindingSet, QueryEvaluationException>) resourceSet;
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
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		resourceSet.remove();	
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
	 * Next binding set.
	 *
	 * @return the binding set
	 */
	public BindingSet nextBindingSet() {
		return getResourceSet().next();
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
//	public Fact nextFact() {
//		BindingSet bindingSet = nextBindingSet();
//		PredicateElement predicateElement = (PredicateElement)getPathElement();
//		Variable subject = predicateElement.getTargetSubject();
//		Variable predicate = predicateElement.getTargetPredicate();
//		Variable target = predicateElement.getTargetVariable();
//		Value subjectValue = bindingSet.getValue(subject.getName());
//		Value predicateValue = bindingSet.getValue(predicate.getName());
//		Value targetValue = bindingSet.getValue(target.getName());
//		return new Fact(subjectValue, predicateValue,targetValue );
//	}
//	public IRI nextReifiedValue() {
//		BindingSet bindingSet = nextBindingSet();
//		PredicateElement predicateElement = (PredicateElement)getPathElement();
//		Variable reification = predicateElement.getReifiedVariable();
//		IRI reificationValue = (IRI) bindingSet.getValue(reification.getName());
//
//		return reificationValue;
//	}

}
