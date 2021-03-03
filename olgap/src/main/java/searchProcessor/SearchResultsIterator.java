package searchProcessor;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Resource;
import pathPatternProcessor.Resources;

public class SearchResultsIterator extends Resources {
	
	CloseableIteration<BindingSet, QueryEvaluationException> searchSet;


	public SearchResultsIterator(CloseableIteration<BindingSet, QueryEvaluationException> searchSet) {
		super();
		this.searchSet=searchSet;
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return searchSet.hasNext();
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		BindingSet next = searchSet.next();
		return thing.getSource().resourceFactory(getTracer(), next.getValue(getPathElement().getTargetVariable().getName()), getStack(), getCustomQueryOptions(),getPrefixes());

	}

	@Override
	public void remove() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}

	protected CloseableIteration<BindingSet, QueryEvaluationException> getStatements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BindingSet nextBindingSet() {
		return searchSet.next();
	}

}
