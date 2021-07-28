/*
 * inova8 2020
 */
package pathQLModel;

import org.eclipse.rdf4j.model.Value;

import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLResults.ResourceResults;

/**
 * The Class Literal.
 */
public class Literal extends Resource {
	
	/**
	 * Instantiates a new literal.
	 *
	 * @param value the value
	 */
	public Literal(Value value) {
		super(value);
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
	public ResourceResults getFacts(String predicatePattern,org.eclipse.rdf4j.model.Literal... bindValues) throws PathPatternException {
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
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	@Override
	public Resource getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	@Override
	public Resource getPredicate() {
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
