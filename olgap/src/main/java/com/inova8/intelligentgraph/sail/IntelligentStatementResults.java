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
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.path.StatementBinding;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.Variable;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.net.URISyntaxException;


/**
 * The Class IntelligentStatementResults.
 */
public  class IntelligentStatementResults extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	
	/** The results iterator. */
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	
	/** The thing. */
	Thing thing;
	
	/** The path element. */
	PathElement pathElement;
	
	/** The sorted iterations. */
	Iterations sortedIterations;
	
	/** The path iteration. */
	private Integer pathIteration=0;
	
	/** The intelligent graph connection. */
	IntelligentGraphConnection intelligentGraphConnection;
	
	/** The source. */
	IntelligentGraphRepository source;
	
	/** The predicate variable. */
	private Variable predicateVariable;
	
	/** The obj. */
	private String obj;
	
	/** The subj. */
	private String subj;
	
	/** The simple value factory. */
	private SimpleValueFactory simpleValueFactory;
	
	/** The custom query options. */
	private CustomQueryOptions customQueryOptions;
	
	/** The contexts. */
	private Resource[] contexts;
	
	/** The trace. */
	private final Boolean trace;
	
	/** The path tuple expr. */
	private PathTupleExpr pathTupleExpr;

	/**
	 * Instantiates a new intelligent statement results.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @param customQueryOptions the custom query options
	 * @param trace the trace
	 * @param contexts the contexts
	 */
	public IntelligentStatementResults( IntelligentGraphRepository source, Thing thing,
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

	/**
	 * Instantiates a new intelligent statement results.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 */
	public IntelligentStatementResults( IntelligentGraphRepository source, Thing thing,
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

	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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

	/**
	 * Next.
	 *
	 * @return the intelligent statement
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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

	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		getResultsIterator().remove();	
	}
	
	/**
	 * Gets the results iterator.
	 *
	 * @return the results iterator
	 */
	public CloseableIteration<BindingSet, QueryEvaluationException>  getResultsIterator() {
		if(resultsIterator!=null)
			return resultsIterator;
		else {
	//		this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathIteration, contexts);
			return resultsIterator;
		}
	}

}
