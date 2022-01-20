/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;


/**
 * The Class MatchFact.
 */
public class MatchFact extends Fact {

	private static final long serialVersionUID = 1L;

 String snippet;
 

 Double score;
	

	public MatchFact(Resource subject, Predicate predicate, Value value, String snippet, Double score) {
		super(subject, predicate, value);
		this.snippet = snippet;
		this.score = score;

	}

	public MatchFact(IntelligentGraphRepository source, Value subject, Value predicate, Value value, Value snippet, Value score) {	
		super(subject, predicate, value);
		if(snippet!=null) this.snippet =  snippet.stringValue();
		if(score!=null)this.score = ((org.eclipse.rdf4j.model.Literal)score).doubleValue();
	}

	@Override
	public String toString() {
		return "MatchFact [" + super.toString() + ",snippet=" + snippet + ", score=" + score +  "]";
	}

	public String getSnippet() {
		return snippet;
	}
	

	public Double getScore() {
		return score;
	}

}
