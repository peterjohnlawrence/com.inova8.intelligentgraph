/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.sail.SailException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.EvaluatorThing;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.path.StatementBinding;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.Variable;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.net.URISyntaxException;


public  class IntelligentStatementResults extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	EvaluatorThing thing;
	PathElement pathElement;
	Iterations sortedIterations;
	private Integer pathIteration=0;
	IntelligentGraphConnection intelligentGraphConnection;
	IntelligentGraphRepository source;
	private Variable predicateVariable;
	private String obj;
	private String subj;
	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private final Boolean trace;
	private PathTupleExpr pathTupleExpr;

	public IntelligentStatementResults( IntelligentGraphRepository source, EvaluatorThing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=null;
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		subj = pathElement.getTargetSubject().toString();
		pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	public IntelligentStatementResults( IntelligentGraphRepository source, EvaluatorThing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=null;
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = false;
		subj = pathElement.getTargetSubject().toString();
		pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if(resultsIterator!=null && resultsIterator.hasNext()) {
			return true;
		}else {
			while(pathIteration < this.sortedIterations.size() ) {
				CustomQueryOptions customQueryOptions= CustomQueryOption.getCustomQueryOptions(contexts,source.getRepositoryContext().getPrefixes());//source.getIntelligentGraphConnection().getPrefixes());
				pathTupleExpr = pathElement.pathPatternQuery(pathIteration,customQueryOptions);
				pathIteration ++;
				this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathTupleExpr, contexts);
				boolean hasNext = resultsIterator.hasNext();
				if(hasNext){
					predicateVariable =pathTupleExpr.getStatementBinding().getPredicateVariable();
					subj = pathTupleExpr.getStatementBinding().getSourceVariable().getName();
					pathTupleExpr.getStatementBinding().getPredicateVariable().getName();
					obj = pathTupleExpr.getStatementBinding().getTargetVariable().getName();
					return true;
				}
			}
			return false;
		}	
	}

	@SuppressWarnings("deprecation")
	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = getResultsIterator().next();
		Binding subjBinding = nextBindingset.getBinding(subj);
		Binding predBinding =StatementBinding.getAlternatePredicateBinding( nextBindingset,   predicateVariable );
		Binding objBinding =nextBindingset.getBinding(obj);
		if(subjBinding!=null && predBinding!=null && objBinding!=null ) {
			IRI parameterizedPredicate ;
			try {
				 parameterizedPredicate = this.pathElement.getParameterizedPredicate((IRI)predBinding.getValue());
			} catch (URISyntaxException e) {
				 parameterizedPredicate =(IRI)predBinding.getValue();
			}	
			if(trace) {
				thing.getEvaluationContext().getTracer().traceFactReturnValue(thing, predBinding.getValue().stringValue(), objBinding.getValue());
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement((Resource)subjBinding.getValue(), parameterizedPredicate, literal(thing.getEvaluationContext().getTrace()),null),null,thing.getEvaluationContext(), customQueryOptions);
			}else {
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement((Resource)subjBinding.getValue(), parameterizedPredicate, objBinding.getValue(),null),null,thing.getEvaluationContext(), customQueryOptions);
			}
		}
		else
			return new IntelligentStatement(null, null, null);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		getResultsIterator().remove();	
	}
	public CloseableIteration<BindingSet, QueryEvaluationException>  getResultsIterator() {
		if(resultsIterator!=null)
			return resultsIterator;
		else {
	//		this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathIteration, contexts);
			return resultsIterator;
		}
	}

}
