/*
 * inova8 2020
 */
package path;

import org.eclipse.rdf4j.model.IRI;

import pathPatternElement.Variable;


public class Edge {


	enum Direction {
		DIRECT, 
		 INVERSE
	};


	Variable sourceVariable;
	

	Variable predicateVariable;
	

	IRI reification;
	

	Variable targetVariable;
	
	Direction direction;
	Boolean isDereified;

	public Edge(Variable sourceVariable, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
	}

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

	public Variable getSourceVariable() {
		return sourceVariable;
	}

	public Variable getPredicateVariable() {
		return predicateVariable;
	}

	public IRI getReification() {
		return reification;
	}


	public Variable getTargetVariable() {
		return targetVariable;
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
			return "[" + sourceVariable.toString() +",<"+ reification.stringValue() +">@"+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + sourceVariable.toString() +","+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"]" ;
	};	
}
