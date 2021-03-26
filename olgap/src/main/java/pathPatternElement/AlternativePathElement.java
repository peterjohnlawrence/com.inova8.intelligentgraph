package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class AlternativePathElement extends PathElement{
	private Boolean isNegated=false;

	public AlternativePathElement() {
		super();
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
		String alternateString = "{{" + getLeftPathElement().toSPARQL() +"}UNION{\n";
		alternateString += getRightPathElement().toSPARQL() + "}}" ;
		return alternateString;
	}

	@Override
	public
	String toHTML() {
		return  getLeftPathElement().toHTML() + " | " + getRightPathElement().toHTML() ;
	}

	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable) ;
		getRightPathElement().setSourceVariable(getLeftPathElement().getSourceVariable());
		getRightPathElement().setTargetVariable(getLeftPathElement().getTargetVariable());
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		Union unionPattern = new Union(leftPattern,rightPattern); 
		return unionPattern;
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
	public Boolean getIsNegated() {
		return isNegated;
	}

	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}





}
