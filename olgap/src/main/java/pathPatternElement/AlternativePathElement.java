/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;

import path.PathBinding;
import path.PathTupleExpr;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class AlternativePathElement.
 */
public class AlternativePathElement extends PathElement{
	
	/** The is negated. */
	private Boolean isNegated=false;

	/**
	 * Instantiates a new alternative path element.
	 *
	 * @param source the source
	 */
	public AlternativePathElement(PathQLRepository source) {
		super(source);
		operator= PathConstants.Operator.ALTERNATIVE;
	}


	@Override
	public String toString() {
		String toString="";
		if(isNegated) {
			toString="!";
		}
		return toString + "(" + getLeftPathElement().toString() + " | " + getRightPathElement().toString()  + ")";
	}


	@Override
	public
	String toSPARQL() {
		String alternateString = "{{" + getLeftPathElement().toSPARQL() +"}UNION{\r\n";
		alternateString += getRightPathElement().toSPARQL() + "}}" ;
		return alternateString;
	}


	@Override
	public
	String toHTML() {
		return  getLeftPathElement().toHTML() + " | " + getRightPathElement().toHTML() ;
	}


	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		return pathPatternQuery(thing,sourceVariable,targetVariable,1) ;
	}
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration) {
		TupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable).getTupleExpr() ;
		getRightPathElement().setSourceVariable(getLeftPathElement().getSourceVariable());
		getRightPathElement().setTargetVariable(getLeftPathElement().getTargetVariable());
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable).getTupleExpr();
	//	Union unionPattern =new TupleExprPath( new Union(leftPattern,rightPattern)); 
		return new PathTupleExpr( new Union(leftPattern,rightPattern));
	}	

	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode ) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);
		Integer rightPathIndex = getRightPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);
		setExitIndex(rightPathIndex);
		return rightPathIndex;
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
	 * Visit path.
	 *
	 * @param path the path
	 * @return the path
	 */
	@Override
	public PathBinding visitPath(PathBinding path) {
		// TODO Auto-generated method stub
		return null;
	}







}
