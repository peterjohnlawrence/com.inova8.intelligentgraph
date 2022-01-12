/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.results;

import java.io.IOException;
import java.util.Iterator;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import com.inova8.intelligentgraph.model.CustomQueryOptions;
import com.inova8.intelligentgraph.model.Path;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.reference.IntelligentGraphRepository;
import com.inova8.intelligentgraph.reference.PathElement;

/**
 * The Class PathResults.
 */
public class PathResults implements CloseableIteration<Path, QueryEvaluationException> , Iterable<Path>{
	protected CustomQueryOptions customQueryOptions; 
	protected PathElement pathElement; 
	protected Thing thing;
	protected IntelligentGraphRepository source;
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
