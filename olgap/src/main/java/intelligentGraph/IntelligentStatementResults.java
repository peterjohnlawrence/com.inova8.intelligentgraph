/*
 * inova8 2020
 */
package intelligentGraph;

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
import static org.eclipse.rdf4j.model.util.Values.literal;
import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternElement.PathElement;


public  class IntelligentStatementResults extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	Thing thing;
	PathElement pathElement;
	IntelligentGraphConnection intelligentGraphConnection;
	private String pred;
	private String obj;
	private String subj;
	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private final Boolean trace;
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
	public IntelligentStatementResults(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Boolean trace, Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		this.trace = trace;
		subj = pathElement.getTargetSubject().toString();
		pred = pathElement.getTargetPredicate().toString();
		obj= pathElement.getTargetVariable().toString();
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return resultsIterator.hasNext();
	}

	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = resultsIterator.next();
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
	

}
