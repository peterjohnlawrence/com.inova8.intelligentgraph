/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.sail.SailException;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.Thing;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;

import static org.eclipse.rdf4j.model.util.Values.literal;


public  class IntelligentStatementResults extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	Thing thing;
	PathElement pathElement;
	Iterations sortedIterations;
	private Integer pathIteration=0;
	IntelligentGraphConnection intelligentGraphConnection;
	IntelligentGraphRepository source;
	private String pred;
	private String obj;
	private String subj;
	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private final Boolean trace;
	@Deprecated
	public IntelligentStatementResults(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace=false;
		subj = pathElement.getTargetSubject().toString();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	@Deprecated
	public IntelligentStatementResults(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=null;//resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		subj = pathElement.getTargetSubject().toString();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	public IntelligentStatementResults( IntelligentGraphRepository source, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=null;//intelligentGraphConnection.getResultsIterator(source, thing,pathElement, contexts);
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		subj = pathElement.getTargetSubject().toString();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	public IntelligentStatementResults( IntelligentGraphRepository source, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=null;//intelligentGraphConnection.getResultsIterator(source, thing,pathElement, contexts);
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = false;
		subj = pathElement.getTargetSubject().toString();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		boolean hasNext = getResultsIterator().hasNext();
		if(hasNext) {
			return true;
		}else {
			pathIteration ++;
			if(pathIteration < this.sortedIterations.size()) {
				this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathIteration, contexts);
				return getResultsIterator().hasNext();
			}else {
				return false;
			}
		}
		
	}

	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = getResultsIterator().next();
		Binding subjBinding = nextBindingset.getBinding(subj);
		Binding predBinding =nextBindingset.getBinding(pred);
		Binding objBinding =nextBindingset.getBinding(obj);
		if(subjBinding!=null && predBinding!=null && objBinding!=null )
			if(trace) {
				thing.getEvaluationContext().getTracer().traceFactReturnValue(thing, predBinding.getValue().stringValue(), objBinding.getValue());
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement((Resource)subjBinding.getValue(), (IRI)predBinding.getValue(), literal(thing.getEvaluationContext().getTrace()),null),null,thing.getEvaluationContext(), customQueryOptions);
			}else {
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement((Resource)subjBinding.getValue(), (IRI)predBinding.getValue(), objBinding.getValue(),null),null,thing.getEvaluationContext(), customQueryOptions);
			}
		else
			return new IntelligentStatement(null, null, null,null);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		resultsIterator.remove();	
	}
	public CloseableIteration<BindingSet, QueryEvaluationException>  getResultsIterator() {
		if(resultsIterator!=null)
			return resultsIterator;
		else {
			this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathIteration, contexts);
			return resultsIterator;
		}
	}

}
