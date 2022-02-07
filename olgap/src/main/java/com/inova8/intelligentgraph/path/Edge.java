/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import org.eclipse.rdf4j.model.IRI;


/**
 * The Class Edge.
 */
public class Edge {


	/**
	 * The Enum Direction.
	 */
	public enum Direction {
		
		/** The direct. */
		DIRECT, 
		 
 		/** The inverse. */
 		INVERSE;
		  
  		/**
		   * To string.
		   *
		   * @return the string
		   */
  		@Override
		  public String toString() {
		    switch(this) {
		      case DIRECT: return "DIRECT";
		      case INVERSE: return "INVERSE";
		      default: throw new IllegalArgumentException();
		    }
		  }
	};
	
	/** The source. */
	org.eclipse.rdf4j.model.Resource source;
	
	/** The predicate. */
	org.eclipse.rdf4j.model.Resource predicate;
	
	/** The reification. */
	IRI reification;
	/** The reified. */
	org.eclipse.rdf4j.model.Resource reified;	
	/** The target. */
	org.eclipse.rdf4j.model.Value target;
	
	/** The direction. */
	Direction direction;
	
	/** The is dereified. */
	Boolean isDereified;

	

	/**
	 * Instantiates a new edge.
	 *
	 * @param source the source
	 * @param predicate the predicate
	 * @param target the target
	 * @param isInverseOf the is inverse of
	 */
	public Edge(org.eclipse.rdf4j.model.Resource source, org.eclipse.rdf4j.model.Resource predicate, org.eclipse.rdf4j.model.Value target, Boolean isInverseOf) {
		this.source = source;
		this.predicate = predicate;
		this.target = target;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
	}

	/**
	 * Instantiates a new edge.
	 *
	 * @param source the source
	 * @param reification the reification
	 * @param predicate the predicate
	 * @param target the target
	 * @param isInverseOf the is inverse of
	 * @param isDereified the is dereified
	 */
	public Edge(org.eclipse.rdf4j.model.Resource source, org.eclipse.rdf4j.model.Resource reified, IRI reification,  org.eclipse.rdf4j.model.Resource predicate, org.eclipse.rdf4j.model.Value target, Boolean isInverseOf, Boolean isDereified) {
		this.source = source;
		this.predicate = predicate;
		this.reification = reification;
		this.reified = reified;
		this.target = target;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
		this.isDereified=isDereified;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public org.eclipse.rdf4j.model.Resource getSource() {
		return source;
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public org.eclipse.rdf4j.model.Resource getPredicate() {
		return predicate;
	}

	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	public IRI getReification() {
		return reification;
	}
	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	public org.eclipse.rdf4j.model.Resource getReified() {
		return reified;
	}

	/**
	 * Gets the target.
	 *
	 * @return the target
	 */
	public org.eclipse.rdf4j.model.Value getTarget() {
		return target;
	}


	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public Direction getDirection() {
		return direction;
	}
	

	/**
	 * Checks if is inverse.
	 *
	 * @return the boolean
	 */
	public Boolean isInverse() {
		if (direction == Direction.INVERSE)
			return true;
		else
			return false;
	}
	

	/**
	 * Gets the checks if is dereified.
	 *
	 * @return the checks if is dereified
	 */
	public Boolean getIsDereified() {
		return isDereified;
	}


	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	String toSPARQL() {
		return null;
	};
	

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		if(reification!=null)
			return "[" + source.toString() +",<"+ reified.stringValue() + ">:<"+ reification.stringValue() +">@"+ predicate.toString() +","+ target.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + source.toString() +","+ predicate.toString() +","+ target.toString() +"," + direction +"]" ;
	};	
}
