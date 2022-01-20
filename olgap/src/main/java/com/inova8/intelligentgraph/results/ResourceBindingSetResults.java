package com.inova8.intelligentgraph.results;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.element.PathElement;


public abstract class ResourceBindingSetResults extends ResourceResults {
	CloseableIteration<BindingSet, QueryEvaluationException> resourceSet;
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,
			IntelligentGraphRepository source, PathElement pathElement,CustomQueryOptions customQueryOptions) {

		super( source, pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing ){
		super( thing);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing, PathElement pathElement,CustomQueryOptions customQueryOptions  ){
		super( thing,pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet) {
		super();
		this.resourceSet =resourceSet;
	}
	public abstract Resource nextResource();

	protected	CloseableIteration<BindingSet, QueryEvaluationException> getStatements() {
		return (CloseableIteration<BindingSet, QueryEvaluationException>) resourceSet;
	}
	@Override
	public void close() throws QueryEvaluationException {
		resourceSet.close();		
	}
	@Override
	public void remove() throws QueryEvaluationException {
		resourceSet.remove();	
	}
	public CloseableIteration<BindingSet, QueryEvaluationException> getResourceSet() {
		return resourceSet;
	}
	public BindingSet nextBindingSet() {
		return getResourceSet().next();
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return getResourceSet().hasNext();
	}

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
