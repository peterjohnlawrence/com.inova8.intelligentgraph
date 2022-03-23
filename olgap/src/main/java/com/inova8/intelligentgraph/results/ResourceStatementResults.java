/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.results;

import java.net.URISyntaxException;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Predicate;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.element.PathElement;


/**
 * The Class ResourceStatementResults.
 */
public class ResourceStatementResults extends ResourceResults {
	
	/** The statement set. */
	CloseableIteration<Statement, RepositoryException> statementSet;
	
	/** The local statement iterator. */
	private CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator;
	
	/**
	 * Instantiates a new resource statement results.
	 *
	 * @param statementSet the statement set
	 * @param source the source
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet,
			IntelligentGraphRepository source, PathElement pathElement, CustomQueryOptions customQueryOptions) {

		super(source, pathElement, customQueryOptions);
		this.statementSet = statementSet;
	}
	

	/**
	 * Instantiates a new resource statement results.
	 *
	 * @param statementSet the statement set
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet, Thing thing,
			PathElement pathElement, CustomQueryOptions customQueryOptions) {
		super(thing, pathElement, customQueryOptions);
		this.statementSet = statementSet;
	}
	

	public ResourceStatementResults(
			CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator, IntelligentGraphRepository source,
			Object pathElement, CustomQueryOptions customQueryOptions) {
		super(source, (PathElement) pathElement, customQueryOptions);
		this.localStatementIterator = localStatementIterator;
	}


	/**
	 * Instantiates a new resource statement results.
	 *
	 * @param localStatementIterator the local statement iterator
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param customQueryOptions the custom query options
	 */
	public ResourceStatementResults(
			CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator, Thing thing,
			Object pathElement, CustomQueryOptions customQueryOptions) {
		super(thing, (PathElement) pathElement, customQueryOptions);
		this.localStatementIterator = localStatementIterator;
	}

	/**
	 * Next resource.
	 *
	 * @return the resource
	 */
	public Resource nextResource() {
		return thing;

	};

	/**
	 * Gets the statements.
	 *
	 * @return the statements
	 */
	protected CloseableIteration<Statement, RepositoryException> getStatements() {
		return (CloseableIteration<Statement, RepositoryException>) statementSet;
	}

	/**
	 * Gets the local statement iterator.
	 *
	 * @return the local statement iterator
	 */
	protected CloseableIteration<? extends Statement, QueryEvaluationException> getLocalStatementIterator() {
		return localStatementIterator;
	}

	/**
	 * Close.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void close() throws QueryEvaluationException {
		if (statementSet != null)
			statementSet.close();
		if (localStatementIterator != null)
			localStatementIterator.close();
	}

	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		if (statementSet != null)
			statementSet.remove();
		if (localStatementIterator != null)
			localStatementIterator.remove();
	}

	/**
	 * Gets the statement set.
	 *
	 * @return the statement set
	 */
	public CloseableIteration<Statement, RepositoryException> getStatementSet() {
		return statementSet;
	}

	/**
	 * Next statement.
	 *
	 * @return the statement
	 */
	public Statement nextStatement() {
		return getStatementSet().next();
	}

	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if (statementSet != null)
			return getStatementSet().hasNext();
		if (localStatementIterator != null)
			return localStatementIterator.hasNext();
		return false;
	}

	/**
	 * Next.
	 *
	 * @return the resource
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public Resource next() throws QueryEvaluationException {
		if (statementSet != null) {
			Statement next = getStatementSet().next();

			//return Resource.create(thing.getSource(), next.getObject(), getEvaluationContext());
			//Resource predicate, Boolean direction, IRI reification, Boolean isDereified,

			Resource subject = Resource.create(getSource(),next.getSubject(), getEvaluationContext());
			Predicate predicate;
			try {
				predicate = new Predicate(next.getPredicate());
			} catch (URISyntaxException e) {
				throw new QueryEvaluationException(e);
			}
			if(getEvaluationContext()!=null) getEvaluationContext().getTracer().traceFactNext(thing,predicate, next.getObject());
			return Resource.create(getSource(), subject, predicate, next.getObject(), getEvaluationContext());
		}
		if (localStatementIterator != null) {
			Statement next = localStatementIterator.next();
			return Resource.create(getSource(), next.getObject(), getEvaluationContext());
		}
		return null;
	}

//	@Override
//	public Fact nextFact() {
//		Statement next = getStatementSet().next();
//		return new Fact(next.getSubject(), next.getPredicate(), next.getObject());
//	}
//
//	@Override
//	public IRI nextReifiedValue() {
//		return null;
//	}

}
