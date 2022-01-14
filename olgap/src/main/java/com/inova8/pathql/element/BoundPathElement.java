/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class BoundPathElement.
 */
public class BoundPathElement extends PathElement{

	/**
	 * Instantiates a new bound path element.
	 *
	 * @param source the source
	 */
	public BoundPathElement(IntelligentGraphRepository source) {
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
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable,Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(thing,  sourceVariable,  predicateVariable,targetVariable,0,customQueryOptions);
	}
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable,Variable predicateVariable, Variable targetVariable,
			Integer pathIteration, CustomQueryOptions customQueryOptions) {
		PathTupleExpr rightPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,predicateVariable,targetVariable,customQueryOptions);
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
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		// TODO Auto-generated method stub
		return null;
	}



}
