package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.Thing;

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
	public void buildIndices(ArrayList<Integer> indices, EdgeCode edgeCode) {
		setLevel(indices.size() - 1);
		setIndex(indices.get(getLevel()));	
	}

	@Override
	public Boolean getIsNegated() {
		return null;
	}

	@Override
	public void setIsNegated(Boolean isDereified) {
		
	}


}
