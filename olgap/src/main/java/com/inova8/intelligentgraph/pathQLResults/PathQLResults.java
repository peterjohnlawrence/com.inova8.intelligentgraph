/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.pathQLModel.MatchFact;
import com.inova8.intelligentgraph.pathQLModel.Resource;


public class PathQLResults extends ResourceBindingSetResults {
	
	/**
	 * Instantiates a new path QL results.
	 *
	 * @param matchSet the match set
	 */
	public PathQLResults(CloseableIteration<BindingSet, QueryEvaluationException> matchSet) {
		super(matchSet);
	}

	/**
	 * Next match.
	 *
	 * @return the match fact
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	public MatchFact nextMatch() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return new MatchFact(getSource(), getValue(next,"n0"), getValue(next,"property_0"), getValue(next,"value_0"), getValue(next,"snippet_0"), getValue(next,"score_0"));


	}
	
	/**
	 * Gets the value.
	 *
	 * @param next the next
	 * @param binding the binding
	 * @return the value
	 */
	private Value getValue(BindingSet next, String binding) {
		Value value=null;
		if(next.getBinding(binding)!=null) value = next.getBinding(binding).getValue();
		return value;
	}

	/**
	 * Next resource.
	 *
	 * @return the resource
	 */
	@Override
	public Resource nextResource() {
		return nextMatch();
	}

//	@Override
//	public Fact nextFact() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public IRI nextReifiedValue() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
