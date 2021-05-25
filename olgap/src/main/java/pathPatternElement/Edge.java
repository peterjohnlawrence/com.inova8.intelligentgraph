/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class Edge.
 */
public class Edge {

	/**
	 * The Enum Direction.
	 */
	enum Direction {
		
		/** The direct. */
		DIRECT, 
		 /** The inverse. */
		 INVERSE
	};

	/** The source variable. */
	Variable sourceVariable;
	
	/** The predicate variable. */
	Variable predicateVariable;
	
	/** The reification. */
	IRI reification;
	
	/** The target variable. */
	Variable targetVariable;
	
	/** The direction. */
	Direction direction;
	/** IsDereified. */
	Boolean isDereified;

	/**
	 * Instantiates a new edge.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param isInverseOf the is inverse of
	 */
	public Edge(Variable sourceVariable, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
	}
	
	/**
	 * Instantiates a new edge.
	 *
	 * @param sourceVariable the source variable
	 * @param reification the reification
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param isInverseOf the is inverse of
	 * @param isDereified the is dereified
	 */
	public Edge(Variable sourceVariable, IRI reification, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf, Boolean isDereified) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.reification = reification;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
		this.isDereified=isDereified;
	}
	
	/**
	 * Gets the source variable.
	 *
	 * @return the source variable
	 */
	public Variable getSourceVariable() {
		return sourceVariable;
	}

	/**
	 * Gets the predicate variable.
	 *
	 * @return the predicate variable
	 */
	public Variable getPredicateVariable() {
		return predicateVariable;
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
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	public Variable getTargetVariable() {
		return targetVariable;
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
			return "[" + sourceVariable.toString() +",<"+ reification.stringValue() +">@"+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + sourceVariable.toString() +","+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"]" ;
	};	
}
