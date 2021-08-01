/*
 * inova8 2020
 */
package pathQLModel;

import org.eclipse.rdf4j.model.Value;

@Deprecated
public class PathFact extends Fact {

	private static final long serialVersionUID = 1L;

	enum Direction {
		forward, reverse
	};

	Direction direction;

	public PathFact(Resource subject, Resource predicate, Value value, Direction direction) {
		super(subject, predicate, value);
		this.direction = direction;
	}
}
