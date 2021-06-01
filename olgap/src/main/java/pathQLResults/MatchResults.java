/*
 * inova8 2020
 */
package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathQLModel.MatchFact;

/**
 * The Class MatchResults.
 */
@Deprecated
public class MatchResults extends ResourceBindingSetResults {
	
	/**
	 * Instantiates a new match results.
	 *
	 * @param matchSet the match set
	 */
	public MatchResults(CloseableIteration<BindingSet, QueryEvaluationException> matchSet) {
		super(matchSet);
	}
	
	/**
	 * Next resource.
	 *
	 * @return the match fact
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public MatchFact nextResource() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return new MatchFact(getSource(), next.getBinding("entity").getValue(), next.getBinding("property").getValue(), next.getBinding("value").getValue(), next.getBinding("snippet").getValue(), next.getBinding("score").getValue());
	}


}
