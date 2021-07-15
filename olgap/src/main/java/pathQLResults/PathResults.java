/*
 * inova8 2020
 */
package pathQLResults;

import java.io.IOException;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import path.EdgeBinding;
import path.Path;
import path.PathBinding;
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
	public PathResults(CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator,Thing thing, PathElement pathElement ) {
		super( thing);
		this.pathElement = pathElement;
		this.pathIterator=pathIterator;
	}


	public PathResults(CloseableIteration<Statement, RepositoryException> pathSet, Thing thing,	PathElement pathElement, CustomQueryOptions customQueryOptions) {
		super( thing);
		this.pathElement = pathElement;
		this.pathSet=pathSet;
	}

	public CloseableIteration<Statement, RepositoryException> getPathSet() {
		return pathSet;
	}
	public PathBinding nextPath() {
		Statement next = getPathSet().next();
		
		
		//PathSet next = getPathSet().next();
		EdgeBinding edge = new EdgeBinding(null, null, null, true);
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
	public Path next() throws QueryEvaluationException {
		if (pathSet != null) {
			Statement next = getPathSet().next();
			thing.getEvaluationContext().getTracer().traceFactReturnStatement(thing,next.getPredicate(), next.getObject());
			try {
				return Path.create(thing, next.getObject());
			} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
				e.printStackTrace();
			}
		}
		if (pathIterator != null) {
			Statement next = pathIterator.next();
			try {
				return Path.create(thing, next.getObject());
			} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
				e.printStackTrace();
			}
		}
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
	public String toString() {
		String toString="";
		while( hasNext()) {
			toString +=next().toString();
		}
		return toString;
	}

}
