/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.IRI;


public class Edge{


//	public enum Direction {
//		DIRECT, 
//		 INVERSE;
//		  @Override
//		  public String toString() {
//		    switch(this) {
//		      case DIRECT: return "DIRECT";
//		      case INVERSE: return "INVERSE";
//		      default: throw new IllegalArgumentException();
//		    }
//		  }
//	};
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
//	public Step toStep() {
//		Step step = new Step();
//		step.end = getTarget();
//		
//		step.edge  = new HashMap<String,Object>();
//		step.edge.put(PATHQL.EDGE_PREDICATESTRING , getPredicate());
//		if(getDirection()!=null)step.edge.put(PATHQL.EDGE_DIRECTIONSTRING , getDirection());
//		if(getIsDereified()!=null)step.edge.put(PATHQL.EDGE_DEREIFIEDSTRING , getIsDereified());
//		if(getReification()!=null)step.edge.put(PATHQL.EDGE_REIFICATIONSTRING , getReification());
//		
//		
//		return step;
//		
//		
//	}
}
