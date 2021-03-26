package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class BoundPathElement extends PathElement{

	public BoundPathElement() {
		super();
		operator=PathConstants.Operator.BINDING ;
		setBaseIndex(0);
	}

	@Override
	public String toString() {
		return  getLeftPathElement().toString() + " / " + getRightPathElement().toString()  ;
	}

	@Override
	public
	String toSPARQL() {
		String binding = getLeftPathElement().toSPARQL() ;
		String pattern = getRightPathElement().toSPARQL()  ;
		
		
		return  binding  + pattern  ;
	}

	@Override
	public
	String toHTML() {
		return getLeftPathElement().toString() + " / " + getRightPathElement().toString();
	}
	@Override
	public Variable getTargetVariable() {
		targetVariable.setName("?n"+ getBaseIndex() +"_"+ getExitIndex());
		return targetVariable;
	}
	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable);
		return rightPattern;
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

	@Override
	public TupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		if(sourceVariable==null)sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();
		TupleExpr boundPattern = getLeftPathElement().boundPatternQuery(thing,sourceVariable,targetVariable) ;
		return boundPattern;
	}

	@Override
	public Boolean getIsBound() {
		return true;
	}

}
