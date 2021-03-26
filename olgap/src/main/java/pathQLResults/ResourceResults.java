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

import pathCalc.Thing;
import pathCalc.Tracer;
import pathPatternElement.PathElement;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

public abstract class ResourceResults implements CloseableIteration<Resource, QueryEvaluationException> , Iterable<Resource>{
	protected PathElement pathElement; 
	protected Thing thing;
	protected PathQLRepository source;

	CloseableIteration<BindingSet, QueryEvaluationException> resourceSet;

	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet) {
		this.resourceSet =resourceSet;
	}
	public ResourceResults( Thing thing ){

		this.thing = thing; 
	}
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing ){
		this.resourceSet =resourceSet;
		this.thing = thing; 
	}
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet,  Thing thing, PathElement pathElement  ){
		this.resourceSet =resourceSet;
		this.thing = thing; 
		this.pathElement = pathElement;
	}
	public ResourceResults(CloseableIteration<BindingSet, QueryEvaluationException> resourceSet, PathQLRepository source,  PathElement pathElement  ){
		this.resourceSet =resourceSet;
		this.source = source; 
		this.pathElement = pathElement;
	}
	@Deprecated
	public ResourceResults(Thing thing,IRI reificationType, IRI predicate 
	){

		this.thing = thing; 

	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return getResourceSet().hasNext();
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return thing.getSource().resourceFactory(getTracer(), next.getValue(getPathElement().getTargetVariable().getName()), getStack(), getCustomQueryOptions(),getPrefixes());
	}

	@Override
	public void remove() throws QueryEvaluationException {
		resourceSet.remove();	
	}

	@Override
	public void close() throws QueryEvaluationException {
		resourceSet.close();		
	}

	protected	CloseableIteration<BindingSet, QueryEvaluationException> getStatements() {
		return (CloseableIteration<BindingSet, QueryEvaluationException>) resourceSet;
	}
	public PathQLRepository getSource() {
		if(source!=null)
			return source;
		else if(thing!=null)
			return thing.getSource();
		else
			return null;
	}

	public Thing getThing() {
		return thing;
	}

	protected Tracer getTracer() {
		if(thing!=null) 
			return thing.getTracer();
		else 
			return null;
	}

	protected HashMap<String, IRI> getPrefixes() {
		if(thing!=null)
			return thing.getPrefixes();
		else 
			return getSource().getPrefixes();
	}

	protected Stack<String> getStack() {
		if(thing!=null)
			return thing.getStack();
		else
			return null;
	}

	protected HashMap<String, Resource> getCustomQueryOptions() {
		if(thing!=null)
			return thing.getCustomQueryOptions();
		else 
			return null;
	}
	@Deprecated
	public IRI getPredicate() {
		return null;
	}
	@Deprecated
	public IRI getReificationType() {
		return null;
	}
	public PathElement getPathElement() {
		return pathElement;
	}
	@Override
	public Iterator<Resource> iterator() {
		return new CloseableIterationIterator<>(this);
	}
	public BindingSet nextBindingSet() {
		return getResourceSet().next();
	}

	public CloseableIteration<BindingSet, QueryEvaluationException> getResourceSet() {
		return resourceSet;
	}
	public abstract Resource nextResource();

}
