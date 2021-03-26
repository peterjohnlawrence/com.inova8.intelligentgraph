package pathQLModel;

import org.eclipse.rdf4j.model.Value;

public class PathFact extends Fact {

	enum Direction{forward, reverse};
	Direction direction ;
	public PathFact(Resource subject, Resource predicate, Value value, Direction direction) {
		super(subject, predicate, value);
		this.direction=direction;
	}
}
