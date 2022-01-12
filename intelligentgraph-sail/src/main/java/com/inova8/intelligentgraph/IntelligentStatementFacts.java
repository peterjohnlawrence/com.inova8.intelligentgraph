/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.sail.SailException;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.URNCustomQueryOptionsDecode;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.path.StatementBinding;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.Variable;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.net.URISyntaxException;
import java.util.Set;


public  class IntelligentStatementFacts extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	Thing thing;
	PathElement pathElement;
	Iterations sortedIterations;
	private Integer pathIteration=0;
	IntelligentGraphConnection intelligentGraphConnection;
	IntelligentGraphRepository source;
	private Variable predicateVariable;
	private String pred;
	private String obj;
	private String subj;
	private SimpleValueFactory simpleValueFactory;
	private ICustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private final Boolean trace;
	private PathTupleExpr pathTupleExpr;
	@Deprecated
	public IntelligentStatementFacts(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, ICustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace=false;
		subj = pathElement.getTargetSubject().toString();
		//TODO
		predicateVariable =pathElement.getTargetPredicate();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	@Deprecated
	public IntelligentStatementFacts(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, ICustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=null;//resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		subj = pathElement.getTargetSubject().toString();
		//TODO
		predicateVariable =pathElement.getTargetPredicate();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	public IntelligentStatementFacts( IntelligentGraphRepository source, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, ICustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=null;//intelligentGraphConnection.getResultsIterator(source, thing,pathElement, contexts);
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	public IntelligentStatementFacts( IntelligentGraphRepository source, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, ICustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=null;
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = false;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if(resultsIterator!=null && resultsIterator.hasNext()) {
			return true;
		}else {
			while(pathIteration < this.sortedIterations.size() ) {
				ICustomQueryOptions customQueryOptions= URNCustomQueryOptionsDecode.getCustomQueryOptions(contexts,source.getIntelligentGraphConnection().getPrefixes());
				pathTupleExpr = pathElement.pathPatternQuery(thing,pathIteration,customQueryOptions);
				pathIteration ++;
				this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathTupleExpr,contexts);
				boolean hasNext = resultsIterator.hasNext();
				if(hasNext) {
					predicateVariable =pathTupleExpr.getStatementBinding().getPredicateVariable();
					subj = pathTupleExpr.getStatementBinding().getSourceVariable().getName();
					pred = pathTupleExpr.getStatementBinding().getPredicateVariable().getName();
					obj = pathTupleExpr.getStatementBinding().getTargetVariable().getName();
					return true;
				}

			}
			return false;
		}	
	}

	@Override 
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = getResultsIterator().next();
		Binding subjBinding = nextBindingset.getBinding(subj);
		//Binding predBinding =nextBindingset.getBinding(	getAlternatePredicateBindingName( nextBindingset.getBindingNames(), pred));
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
//			this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathIteration, contexts);
			return resultsIterator;
		}
	}
//	public  String getAlternatePredicateBindingName(Set<String> bindingsetNames, String predicateVariableName ) {
//		String predicateVariableNameRoot = predicateVariableName.split("_alt")[0];
//		if( bindingsetNames.contains(predicateVariableNameRoot)) {
//			return predicateVariableNameRoot;
//		}else {
//			for(String bindingsetname:bindingsetNames) {
//				String bindingNameRoot = bindingsetname.split("_alt")[0];
//				if(bindingNameRoot.equals(predicateVariableNameRoot)) {
//					return bindingsetname;
//				}
//			}
//		}	
//		return null;
//	}
}
