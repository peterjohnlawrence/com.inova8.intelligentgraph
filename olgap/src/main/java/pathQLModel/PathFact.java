/*
 * inova8 2020
 */
package pathQLModel;

import org.eclipse.rdf4j.model.Value;

@Deprecated
public class PathFact extends Fact {

	/**
	 * The Enum Direction.
	 */
	enum Direction{/** The forward. */
forward, /** The reverse. */
 reverse};
	
	/** The direction. */
	Direction direction ;
	
	/**
	 * Instantiates a new path fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 * @param direction the direction
	 */
	public PathFact(Resource subject, Resource predicate, Value value, Direction direction) {
		super(subject, predicate, value);
		this.direction=direction;
	}
}
