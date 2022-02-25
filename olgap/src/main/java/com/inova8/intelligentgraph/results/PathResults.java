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

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.pathql.element.PathElement;

/**
 * The Class PathResults.
 */
public class PathResults implements CloseableIteration<Path, QueryEvaluationException> , Iterable<Path>{
	
	/** The custom query options. */
	protected CustomQueryOptions customQueryOptions; 
	
	/** The path element. */
	protected PathElement pathElement; 
	
	/** The thing. */
	protected Thing thing;
	
	/** The source. */
	protected IntelligentGraphRepository source;
	
	/** The path iterator. */
	private CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator;
	
	/** The path set. */
	private CloseableIteration<Statement, RepositoryException> pathSet;
	public PathResults(CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator,IntelligentGraphRepository source, PathElement pathElement ) {
		this.source=source;
		this.pathElement = pathElement;
		this.pathIterator=pathIterator;
	}
	public PathResults(CloseableIteration<Statement, RepositoryException> pathSet, IntelligentGraphRepository source,	PathElement pathElement, CustomQueryOptions customQueryOptions) {
		this.source=source;
		this.pathElement = pathElement;
		this.pathSet=pathSet;
	}
	
	/**
	 * Instantiates a new path results.
	 *
	 * @param pathIterator the path iterator
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public PathResults(CloseableIteration<? extends Statement, QueryEvaluationException> pathIterator,Thing thing, PathElement pathElement ) {
		this.thing=thing;
		this.pathElement = pathElement;
		this.pathIterator=pathIterator;
	}


	/**
	 * Instantiates a new path results.
	 *
	 * @param pathSet the path set
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public PathResults(CloseableIteration<Statement, RepositoryException> pathSet, Thing thing,	PathElement pathElement, CustomQueryOptions customQueryOptions) {
		this.thing=thing;
		this.pathElement = pathElement;
		this.pathSet=pathSet;
	}

	/**
	 * Gets the path set.
	 *
	 * @return the path set
	 */
	public CloseableIteration<Statement, RepositoryException> getPathSet() {
		return pathSet;
	}
//	public PathBinding nextPath() {
//		getPathSet().next();
//		//EdgeBinding edge = new EdgeBinding(null, null, null, true);
//		return null;
//	}

	protected EvaluationContext getEvaluationContext() {
		if(thing!=null)
			return thing.getEvaluationContext();
		else 
			return null;
	}

	/**
 * Close.
 *
 * @throws QueryEvaluationException the query evaluation exception
 */
@Override
	public void close() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if (pathSet != null)
			return pathSet.hasNext();
		if (pathIterator != null)
			
			return pathIterator.hasNext();
		return false;
	}


	/**
	 * Next.
	 *
	 * @return the path
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public Path next() throws QueryEvaluationException {
		if (pathSet != null) {
			Statement next = getPathSet().next();
			if(getEvaluationContext() !=null) getEvaluationContext().getTracer().traceFactNext(thing,next.getPredicate(), next.getObject());
			try {
				return Path.create(next.getSubject(), next.getObject());
			} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
				e.printStackTrace();
			}
		}
		if (pathIterator != null) {
			Statement next = pathIterator.next();
			try {
				return Path.create(next.getSubject(), next.getObject());
			} catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String toString="";
		while( hasNext()) {
			toString +=next().toString();
		}
		return toString;
	}


	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	@Override
	public Iterator<Path> iterator() {
		return new CloseableIterationIterator<>(this);
	}

}
