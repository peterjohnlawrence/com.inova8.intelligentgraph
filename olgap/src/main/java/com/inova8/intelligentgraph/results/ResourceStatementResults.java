package com.inova8.intelligentgraph.results;

import java.net.URISyntaxException;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.model.Predicate;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.pathql.element.PathElement;


public class ResourceStatementResults extends ResourceResults {
	CloseableIteration<Statement, RepositoryException> statementSet;
	private CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator;
	@Deprecated
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet,
			IntelligentGraphRepository source, PathElement pathElement, CustomQueryOptions customQueryOptions) {

		super(source, pathElement, customQueryOptions);
		this.statementSet = statementSet;
	}
	@Deprecated
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet, Thing thing) {
		super(thing);
		this.statementSet = statementSet;
	}

	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet, Thing thing,
			PathElement pathElement, CustomQueryOptions customQueryOptions) {
		super(thing, pathElement, customQueryOptions);
		this.statementSet = statementSet;
	}
	@Deprecated
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet) {
		super();
		this.statementSet = statementSet;
	}

	public ResourceStatementResults(
			CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator, Thing thing,
			Object pathElement, CustomQueryOptions customQueryOptions) {
		super(thing, (PathElement) pathElement, customQueryOptions);
		this.localStatementIterator = localStatementIterator;
	}

	public Resource nextResource() {
		return thing;

	};

	protected CloseableIteration<Statement, RepositoryException> getStatements() {
		return (CloseableIteration<Statement, RepositoryException>) statementSet;
	}

	protected CloseableIteration<? extends Statement, QueryEvaluationException> getLocalStatementIterator() {
		return localStatementIterator;
	}

	@Override
	public void close() throws QueryEvaluationException {
		if (statementSet != null)
			statementSet.close();
		if (localStatementIterator != null)
			localStatementIterator.close();
	}

	@Override
	public void remove() throws QueryEvaluationException {
		if (statementSet != null)
			statementSet.remove();
		if (localStatementIterator != null)
			localStatementIterator.remove();
	}

	public CloseableIteration<Statement, RepositoryException> getStatementSet() {
		return statementSet;
	}

	public Statement nextStatement() {
		return getStatementSet().next();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if (statementSet != null)
			return getStatementSet().hasNext();
		if (localStatementIterator != null)
			return localStatementIterator.hasNext();
		return false;
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		if (statementSet != null) {
			Statement next = getStatementSet().next();

			//return Resource.create(thing.getSource(), next.getObject(), getEvaluationContext());
			//Resource predicate, Boolean direction, IRI reification, Boolean isDereified,
			Resource subject = Resource.create(thing.getSource(),next.getSubject(), getEvaluationContext());
			Predicate predicate;
			try {
				predicate = new Predicate(next.getPredicate());
			} catch (URISyntaxException e) {
				throw new QueryEvaluationException(e);
			}
			thing.getEvaluationContext().getTracer().traceFactNext(thing,predicate, next.getObject());
			return Resource.create(thing.getSource(), subject, predicate, next.getObject(), getEvaluationContext());
		}
		if (localStatementIterator != null) {
			Statement next = localStatementIterator.next();
			return Resource.create(thing.getSource(), next.getObject(), getEvaluationContext());
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
