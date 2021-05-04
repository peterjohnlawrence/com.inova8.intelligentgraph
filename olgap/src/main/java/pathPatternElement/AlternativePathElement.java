/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;

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
	 */
	public AlternativePathElement(PathQLRepository source) {
		super(source);
		operator= PathConstants.Operator.ALTERNATIVE;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String toString="";
		if(isNegated) {
			toString="!";
		}
		return toString + "(" + getLeftPathElement().toString() + " | " + getRightPathElement().toString()  + ")";
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public
	String toSPARQL() {
		String alternateString = "{{" + getLeftPathElement().toSPARQL() +"}UNION{\n";
		alternateString += getRightPathElement().toSPARQL() + "}}" ;
		return alternateString;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public
	String toHTML() {
		return  getLeftPathElement().toHTML() + " | " + getRightPathElement().toHTML() ;
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
		getRightPathElement().setSourceVariable(getLeftPathElement().getSourceVariable());
		getRightPathElement().setTargetVariable(getLeftPathElement().getTargetVariable());
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		Union unionPattern = new Union(leftPattern,rightPattern); 
		return unionPattern;
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
	public Path visitPath(Path path) {
		// TODO Auto-generated method stub
		return null;
	}





}
