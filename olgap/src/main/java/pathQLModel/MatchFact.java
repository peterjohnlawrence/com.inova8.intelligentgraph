package pathQLModel;

import org.eclipse.rdf4j.model.Value;

import pathCalc.Thing;

public class MatchFact extends Fact {
 String snippet;
 Double score;
	public MatchFact(Resource subject, Resource predicate, Value value, String snippet, Double score) {
		super(subject, predicate, value);
		this.snippet = snippet;
		this.score = score;

	}
	public MatchFact(Value subject, Value predicate, Value value, Value snippet, Value score) {		
		super(new Thing(getSource(), subject, getCustomQueryOptions()), new Thing(getSource(), predicate, getCustomQueryOptions()), value);
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
