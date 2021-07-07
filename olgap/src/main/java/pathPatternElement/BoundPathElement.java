/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import path.Path;
import path.PathTupleExpr;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class BoundPathElement.
 */
public class BoundPathElement extends PathElement{

	/**
	 * Instantiates a new bound path element.
	 *
	 * @param source the source
	 */
	public BoundPathElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.BINDING ;
		setBaseIndex(0);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return  getLeftPathElement().toString() + " / " + getRightPathElement().toString()  ;
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public
	String toSPARQL() {
		String binding = getLeftPathElement().toSPARQL() ;
		String pattern = getRightPathElement().toSPARQL()  ;
		
		
		return  binding  + pattern  ;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public
	String toHTML() {
		return getLeftPathElement().toString() + " / " + getRightPathElement().toString();
	}
	
	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	@Override
	public Variable getTargetVariable() {
		targetVariable.setName("n"+ getBaseIndex() +"_"+ getExitIndex());
		return targetVariable;
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
		return pathPatternQuery(thing,  sourceVariable,  targetVariable,1);
	}
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration) {
		PathTupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		return rightPattern;
	}
	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	@Override
	public Boolean getIsNegated() {
		return null;
	}

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isDereified the new checks if is negated
	 */
	@Override
	public void setIsNegated(Boolean isDereified) {	
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
		if(getRightPathElement()!=null ) {
			Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, getEdgeCode());
			setExitIndex(rightExitIndex) ;
		}else {
			setExitIndex(leftExitIndex) ;
		}
		
		if (getLeftPathElement().getOperator().equals(PathConstants.Operator.PREDICATE)
				&& ((PredicateElement) getLeftPathElement()).getIsDereified()) {
			setEdgeCode(EdgeCode.DEREIFIED);
		}
		return getExitIndex();
	}

	/**
	 * Bound pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	@Override
	public PathTupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		if(sourceVariable==null)sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();
		TupleExpr boundPattern = getLeftPathElement().boundPatternQuery(thing,sourceVariable,targetVariable).getTupleExpr() ;
		return new PathTupleExpr(boundPattern);
	}

	/**
	 * Gets the checks if is bound.
	 *
	 * @return the checks if is bound
	 */
	@Override
	public Boolean getIsBound() {
		return true;
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
