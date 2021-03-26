package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class SequencePathElement extends PathElement {

	private Boolean isNegated=false;

	public SequencePathElement() {
		super();
		operator=PathConstants.Operator.SEQUENCE;
	}

	@Override
	public String toSPARQL() {

		String sequenceString = getMinCardinalityString();
		sequenceString+=getLeftPathElement().toSPARQL();
		sequenceString += getRightPathElement().toSPARQL();
		sequenceString += getMaxCardinalityString();
		return sequenceString;
	}

	@Override
	public String toHTML() {
		String sequenceString = getLeftPathElement().toHTML() + " / " + getRightPathElement().toHTML();
		return addCardinality(sequenceString);
	}

	@Override
	public String toString() {
		String sequenceString = getLeftPathElement().toString() + " / " + getRightPathElement().toString();
		return addCardinality(sequenceString);
	}

	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable) ;
		getRightPathElement().setSourceVariable(getLeftPathElement().getTargetVariable());
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		Join joinPattern = new Join(leftPattern,rightPattern); 
		return joinPattern;
	}
	public Boolean getIsNegated() {
		return isNegated;
	}

	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}

	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		Integer leftExitIndex = getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);
		
		if (getLeftPathElement().getOperator().equals(PathConstants.Operator.PREDICATE)
				&& ((PredicateElement) getLeftPathElement()).getIsDereified()) {
			setEdgeCode(EdgeCode.DEREIFIED);
		}
		
		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, getEdgeCode());
		setExitIndex(rightExitIndex) ;
		return rightExitIndex;
	}
}
