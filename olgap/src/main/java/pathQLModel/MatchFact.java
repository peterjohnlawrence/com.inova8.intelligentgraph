/*
 * inova8 2020
 */
package pathQLModel;

import org.eclipse.rdf4j.model.Value;

import pathQLRepository.PathQLRepository;

/**
 * The Class MatchFact.
 */
public class MatchFact extends Fact {
 
 /** The snippet. */
 String snippet;
 
 /** The score. */
 Double score;
	
	/**
	 * Instantiates a new match fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 * @param snippet the snippet
	 * @param score the score
	 */
	public MatchFact(Resource subject, Resource predicate, Value value, String snippet, Double score) {
		super(subject, predicate, value);
		this.snippet = snippet;
		this.score = score;

	}
	
	/**
	 * Instantiates a new match fact.
	 *
	 * @param source the source
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 * @param snippet the snippet
	 * @param score the score
	 */
	public MatchFact(PathQLRepository source, Value subject, Value predicate, Value value, Value snippet, Value score) {	
		super(subject, predicate, value);
		if(snippet!=null) this.snippet =  snippet.stringValue();
		if(score!=null)this.score = ((org.eclipse.rdf4j.model.Literal)score).doubleValue();
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "MatchFact [" + super.toString() + ",snippet=" + snippet + ", score=" + score +  "]";
	}
	
	/**
	 * Gets the snippet.
	 *
	 * @return the snippet
	 */
	public String getSnippet() {
		return snippet;
	}
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

}
