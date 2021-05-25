/*
 * inova8 2020
 */
package pathQLModel;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLResults.ResourceResults;

/**
 * The Class Fact.
 */
public class Fact extends Resource {
	
	/** The predicate. */
	Resource predicate =null;
	
	/** The subject. */
	Resource subject;
	/** The reification. */
	IRI reification;	
	/** IsDereified. */
	Boolean isDereified;
	/**
	 * Instantiates a new fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 */
	public Fact(Resource subject, Resource predicate,  Value value ) {
		super(value);
		this.predicate = predicate;
		this.subject = subject;
	}
	
	/**
	 * Instantiates a new fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 */
	public Fact(Value subject, Value predicate, Value value) {
		super(value);
		if(predicate!=null)
			this.predicate =Thing.create(getSource(), predicate, getEvaluationContext() );	
		if(subject!=null)
			this.subject = Thing.create(getSource(), subject, getEvaluationContext());
	}
	
	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public Resource getPredicate() {
		return predicate;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public Resource getSubject() {
		return subject;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Fact [Resource[ object=" + super.toString() +  "], predicate=" + predicate + ", subject=" + subject +  "]";
	}
	
	/**
	 * Gets the subject id.
	 *
	 * @return the subject id
	 */
	public int getSubjectId() {
		return hashCode();
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public URI getId() {
		try {
			return new URI("MatchFact" + "(" + getSubjectId() + ")");
		} catch (URISyntaxException e) {
		//	throw new Exception("Unable to create id for entity: " + "MatchFact", e);
			return null;
		}
	}
	
	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public Resource getFact(String predicatePattern) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public ResourceResults getFacts(String predicatePattern) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param path the path
	 * @return the facts
	 */
	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the snippet.
	 *
	 * @return the snippet
	 */
	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}
}
