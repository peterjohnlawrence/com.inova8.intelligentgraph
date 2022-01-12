package com.inova8.intelligentgraph.results;

import java.net.URISyntaxException;
import java.util.Iterator;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;

import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.model.CustomQueryOptions;
import com.inova8.intelligentgraph.model.Predicate;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.reference.IntelligentGraphRepository;
import com.inova8.intelligentgraph.reference.PathElement;


public class FactResults
implements CloseableIteration<Resource, QueryEvaluationException> , Iterable<Resource>{
	protected CustomQueryOptions customQueryOptions; 
	protected PathElement pathElement; 
	protected Thing thing;
	protected IntelligentGraphRepository source;
	CloseableIteration<Statement, RepositoryException> statementSet;
	private CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator;
	@Deprecated
	public FactResults(CloseableIteration<Statement, RepositoryException> statementSet,
			IntelligentGraphRepository source, PathElement pathElement, CustomQueryOptions customQueryOptions) {

		//super(source, pathElement, customQueryOptions);
		this.source=source;
		this.pathElement=pathElement;
		this.customQueryOptions=customQueryOptions;
		this.statementSet = statementSet;
	}
	@Deprecated
	public FactResults(CloseableIteration<Statement, RepositoryException> statementSet, Thing thing) {
		//super(thing);
		this.thing=thing;
		this.statementSet = statementSet;
	}

	public FactResults(CloseableIteration<Statement, RepositoryException> statementSet, Thing thing,
			PathElement pathElement, CustomQueryOptions customQueryOptions) {
		//super(thing, pathElement, customQueryOptions);
		this.thing=thing;
		this.pathElement=pathElement;
		this.customQueryOptions=customQueryOptions;
		this.statementSet = statementSet;
	}
	@Deprecated
	public FactResults(CloseableIteration<Statement, RepositoryException> statementSet) {
		super();
		this.statementSet = statementSet;
	}

	public FactResults(
			CloseableIteration<? extends Statement, QueryEvaluationException> localStatementIterator, Thing thing,
			Object pathElement, CustomQueryOptions customQueryOptions) {
		//super(thing, (PathElement) pathElement, customQueryOptions);
		this.thing=thing;
		this.pathElement=(PathElement)pathElement;
		this.customQueryOptions=customQueryOptions;
		this.localStatementIterator = localStatementIterator;
	}
	@Deprecated
	public Resource nextResource() {
		return thing;

	};
	@Deprecated
	protected CloseableIteration<Statement, RepositoryException> getStatements() {
		return (CloseableIteration<Statement, RepositoryException>) statementSet;
	}
	@Deprecated
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
	@Deprecated
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
	@Override
	public Iterator<Resource> iterator() {
		return new CloseableIterationIterator<>(this);
	}

	protected EvaluationContext getEvaluationContext() {
		if(thing!=null)
			return thing.getEvaluationContext();
		else 
			return null;
	}
	public Integer count() {
		Integer count = 0;
		while( hasNext()) {
			next();
			count++;
		}
		return count; 
	}
	
	/**
	 * Average.
	 *
	 * @return the double
	 */
	public Double average() {
		Integer count = 0;
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
			count++;
		}
		return total/count; 
	}
	
	/**
	 * Total.
	 *
	 * @return the double
	 */
	public Double total() {
		Double total = 0.0;
		while( hasNext()) {
			total+=next().doubleValue();
		}
		return total;
	 
	}
	public String toString() {
		String toString="[";
		while( hasNext()) {
			toString +=next().toString()+";";
		}
		return toString+"]";
	}

}
