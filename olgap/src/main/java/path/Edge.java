/*
 * inova8 2020
 */
package path;

import org.eclipse.rdf4j.model.IRI;


public class Edge {


	enum Direction {
		DIRECT, 
		 INVERSE
	};
	org.eclipse.rdf4j.model.Resource source;
	org.eclipse.rdf4j.model.Resource predicate;
	IRI reification;
	org.eclipse.rdf4j.model.Value target;
	Direction direction;
	Boolean isDereified;

	public Edge(org.eclipse.rdf4j.model.Resource source, org.eclipse.rdf4j.model.Resource predicate, org.eclipse.rdf4j.model.Value target, Boolean isInverseOf) {
		this.source = source;
		this.predicate = predicate;
		this.target = target;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
	}

	public Edge(org.eclipse.rdf4j.model.Resource source, IRI reification, org.eclipse.rdf4j.model.Resource predicate, org.eclipse.rdf4j.model.Value target, Boolean isInverseOf, Boolean isDereified) {
		this.source = source;
		this.predicate = predicate;
		this.reification = reification;
		this.target = target;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
		this.isDereified=isDereified;
	}

	public org.eclipse.rdf4j.model.Resource getSource() {
		return source;
	}

	public org.eclipse.rdf4j.model.Resource getPredicate() {
		return predicate;
	}

	public IRI getReification() {
		return reification;
	}


	public org.eclipse.rdf4j.model.Value getTarget() {
		return target;
	}


	public Direction getDirection() {
		return direction;
	}
	

	public Boolean isInverse() {
		if (direction == Direction.INVERSE)
			return true;
		else
			return false;
	}
	

	public Boolean getIsDereified() {
		return isDereified;
	}


	String toSPARQL() {
		return null;
	};
	

	public String toString() {
		if(reification!=null)
			return "[" + source.toString() +",<"+ reification.stringValue() +">@"+ predicate.toString() +","+ target.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + source.toString() +","+ predicate.toString() +","+ target.toString() +"," + direction +"]" ;
	};	
}
