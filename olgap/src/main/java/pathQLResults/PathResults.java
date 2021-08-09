/*
 * inova8 2020
 */
package pathQLResults;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
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
import pathQLRepository.PathQLRepository;

/**
 * The Class PathResults.
 */
public class PathResults implements CloseableIteration<Path, QueryEvaluationException> , Iterable<Path>{//extends ResourceResults{
	protected CustomQueryOptions customQueryOptions; 
	protected PathElement pathElement; 
	protected Thing thing;
	protected PathQLRepository source;
	private CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator;
	private CloseableIteration<Statement, RepositoryException> pathSet;
	public PathResults(CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator,Thing thing, PathElement pathElement ) {
		this.thing=thing;
		this.pathElement = pathElement;
		this.pathIterator=pathIterator;
	}


	public PathResults(CloseableIteration<Statement, RepositoryException> pathSet, Thing thing,	PathElement pathElement, CustomQueryOptions customQueryOptions) {
		this.thing=thing;
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
			thing.getEvaluationContext().getTracer().traceFactNext(thing,next.getPredicate(), next.getObject());
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


	public String toString() {
		String toString="";
		while( hasNext()) {
			toString +=next().toString();
		}
		return toString;
	}


	@Override
	public Iterator<Path> iterator() {
		return new CloseableIterationIterator<>(this);
	}

}
