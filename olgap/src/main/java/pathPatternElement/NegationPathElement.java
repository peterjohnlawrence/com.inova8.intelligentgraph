/*
 * inova8 2020
 */
package pathPatternElement;

import path.Path;
import path.PathTupleExpr;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class NegationPathElement.
 */
public class NegationPathElement extends PathElement{

	/**
	 * Instantiates a new negation path element.
	 *
	 * @param source the source
	 */
	public NegationPathElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.NEGATION;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public
	String toSPARQL() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public
	String toHTML() {
		// TODO Auto-generated method stub
		return null;
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
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	@Override
	public Boolean getIsNegated() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isDereified the new checks if is negated
	 */
	@Override
	public void setIsNegated(Boolean isDereified) {
		// TODO Auto-generated method stub
		
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
		setEntryIndex(entryIndex);
		Integer leftExitIndex = getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);
		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, edgeCode);
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
		// TODO Auto-generated method stub
		return null;
	}



}
