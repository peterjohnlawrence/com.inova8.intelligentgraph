package pathQLModel;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.rdf4j.model.Value;

import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLResults.ResourceResults;
public class Fact extends Resource {
	Resource predicate;
	Resource subject;
	public Fact(Resource subject, Resource predicate,  Value value ) {
		super(value);
		this.predicate = predicate;
		this.subject = subject;
	}
	public Fact(Value subject, Value predicate, Value value) {
		super(value);
		this.predicate = new Thing(getSource(), predicate, getCustomQueryOptions());
		this.subject = new Thing(getSource(), subject, getCustomQueryOptions());
	}
	public Resource getPredicate() {
		return predicate;
	}
	public Resource getSubject() {
		return subject;
	}

	@Override
	public String toString() {
		return "Fact [Resource[ object=" + super.toString() +  "], predicate=" + predicate + ", subject=" + subject +  "]";
	}
	public int getSubjectId() {
		return hashCode();
	}
	public URI getId() {
		try {
			return new URI("MatchFact" + "(" + getSubjectId() + ")");
		} catch (URISyntaxException e) {
		//	throw new Exception("Unable to create id for entity: " + "MatchFact", e);
			return null;
		}
	}
	@Override
	public Resource getFact(String predicatePattern) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResourceResults getFacts(String predicatePattern) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}
}
