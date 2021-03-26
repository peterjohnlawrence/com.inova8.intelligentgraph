package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathQLModel.MatchFact;

public class MatchResults extends ResourceResults {
	public MatchResults(CloseableIteration<BindingSet, QueryEvaluationException> matchSet) {
		super(matchSet);
	}
	@Override
	public MatchFact nextResource() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return new MatchFact(next.getBinding("entity").getValue(), next.getBinding("property").getValue(), next.getBinding("value").getValue(), next.getBinding("snippet").getValue(), next.getBinding("score").getValue());


	}


}
