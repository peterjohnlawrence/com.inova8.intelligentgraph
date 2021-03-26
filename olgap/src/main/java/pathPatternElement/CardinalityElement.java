package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class CardinalityElement extends PathElement {
	Integer minCardinality;
	Integer maxCardinality;
	Boolean unboundedCardinality;

	public CardinalityElement() {
		super();
		operator=PathConstants.Operator.CARDINALITY;
	}

	public String toString() {
		String cardinalityElement = "{" + minCardinality;
		if (maxCardinality != null) {
			cardinalityElement += "," + maxCardinality;
		} else if (unboundedCardinality) {
			cardinalityElement += ",*";
		}
		return cardinalityElement + "}";
	}
	public String toSPARQL() {

		String cardinalityElement = "{" + minCardinality;
		if (maxCardinality != null) {
			cardinalityElement += "," + maxCardinality;
		} else if (unboundedCardinality) {
			cardinalityElement += ",*";
		}
		return cardinalityElement + "}";
	}


	@Override
	public
	String toHTML() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Boolean getIsNegated() {
		return null;
	}

	@Override
	public void setIsNegated(Boolean isDereified) {
		
	}

	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setEntryIndex(entryIndex);
		Integer leftExitIndex = getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);
		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, edgeCode);
		setExitIndex(rightExitIndex) ;
		return rightExitIndex;
	}


}
