/*
 * inova8 2020
 */
package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;

import path.Edge;
import path.Path;
import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathQLModel.Fact;
import pathQLModel.Resource;

/**
 * The Class PathResults.
 */
public class PathResults extends ResourceResults{

	private CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator;
	private CloseableIteration<Statement, RepositoryException> pathSet;
	public PathResults(CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator,Thing thing, PathElement pathElement  ) {
		super( thing);
		this.pathElement = pathElement;
		this.pathIterator=pathIterator;
	}


	public PathResults(CloseableIteration<Statement, RepositoryException> pathSet, Thing thing,	PathElement pathElement, CustomQueryOptions customQueryOptions) {
		this.pathElement = pathElement;
		this.pathSet=pathSet;
	}


	public Path nextPath() {
		//PathSet next = getPathSet().next();
		Edge edge = new Edge(null, null, null, true);
		return null;
	}


	@Override
	public Resource nextResource() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void close() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if (pathSet != null)
			return pathSet.hasNext();
		if (pathIterator != null)
			return pathIterator.hasNext();
		return false;
	}


	@Override
	public Resource next() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void remove() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Fact nextFact() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IRI nextReifiedValue() {
		// TODO Auto-generated method stub
		return null;
	}
}
