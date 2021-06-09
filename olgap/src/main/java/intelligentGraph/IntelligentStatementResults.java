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
	public IntelligentStatementResults(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
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
			return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement((Resource)subjBinding.getValue(), (IRI)predBinding.getValue(), objBinding.getValue(),null),null,thing.getEvaluationContext(), customQueryOptions);
		else
			return new IntelligentStatement(null, null, null,null);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		resultsIterator.remove();	
	}
	

}
