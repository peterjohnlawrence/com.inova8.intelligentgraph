/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class SequencePathElement.
 */
public class SequencePathElement extends PathElement {

	/** The is negated. */
	private Boolean isNegated=false;

	/**
	 * Instantiates a new sequence path element.
	 *
	 * @param source the source
	 */
	public SequencePathElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.SEQUENCE;
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public String toSPARQL() {

		String sequenceString = getMinCardinalityString();
		sequenceString+=getLeftPathElement().toSPARQL();
		sequenceString += getRightPathElement().toSPARQL();
		sequenceString += getMaxCardinalityString();
		return sequenceString;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {
		String sequenceString = getLeftPathElement().toHTML() + " / " + getRightPathElement().toHTML();
		return addCardinality(sequenceString);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String sequenceString = getLeftPathElement().toString() + " / " + getRightPathElement().toString();
		return addCardinality(sequenceString);
	}

	/**
	 * Path pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable) ;
		getRightPathElement().setSourceVariable(getLeftPathElement().getTargetVariable());
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		Join joinPattern = new Join(leftPattern,rightPattern); 
		return joinPattern;
	}
	
	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	public Boolean getIsNegated() {
		return isNegated;
	}

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isNegated the new checks if is negated
	 */
	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}

	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
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

	/**
	 * Visit path.
	 *
	 * @param path the path
	 * @return the path
	 */
	@Override
	public Path visitPath(Path path) {
		path = getLeftPathElement().visitPath(path);
		path = getRightPathElement().visitPath(path);
		return path;
	}
}
