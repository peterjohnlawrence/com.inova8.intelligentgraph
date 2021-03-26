package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathQLModel.MatchFact;
import pathQLModel.Resource;

public class PathQLResults extends ResourceResults {
	public PathQLResults(CloseableIteration<BindingSet, QueryEvaluationException> matchSet) {
		super(matchSet);
	}

	public MatchFact nextMatch() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return new MatchFact(getValue(next,"n0"), getValue(next,"property_0"), getValue(next,"value_0"), getValue(next,"snippet_0"), getValue(next,"score_0"));


	}
	private Value getValue(BindingSet next, String binding) {
		Value value=null;
		if(next.getBinding(binding)!=null) value = next.getBinding(binding).getValue();
		return value;
	}

	@Override
	public Resource nextResource() {
		return nextMatch();
	}

}
