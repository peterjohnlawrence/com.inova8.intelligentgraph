package com.inova8.intelligentgraph.pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLModel.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.PathElement;

@Deprecated
public abstract class ResourceBindingSetResults extends BindingSetResults {
	CloseableIteration<BindingSet, QueryEvaluationException> resourceSet;
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,
			IntelligentGraphRepository source, PathElement pathElement,ICustomQueryOptions customQueryOptions) {

		super( source, pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  IThing thing ){
		super( thing);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  IThing thing, PathElement pathElement,ICustomQueryOptions customQueryOptions  ){
		super( thing,pathElement,customQueryOptions);
		this.resourceSet =resourceSet;
	}
	public ResourceBindingSetResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet) {
		super();
		this.resourceSet =resourceSet;
	}
	public abstract IResource nextResource();

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
	public IResource next() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return IResource.create(thing.getSource(), next.getValue(getPathElement().getTargetVariable().getName()), getEvaluationContext());
	}


}
