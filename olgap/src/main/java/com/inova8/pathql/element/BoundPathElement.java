/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class BoundPathElement.
 */
public class BoundPathElement extends PathElement{

	/**
	 * Instantiates a new bound path element.
	 *
	 * @param repositoryContext the repository context
	 */
	public BoundPathElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return pathPatternQuery( sourceVariable,  predicateVariable,targetVariable,0,customQueryOptions);
	}
	
	/**
	 * Path pattern query.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param pathIteration the path iteration
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable,
			Integer pathIteration, CustomQueryOptions customQueryOptions) {
		PathTupleExpr rightPattern = getRightPathElement().pathPatternQuery(sourceVariable,predicateVariable,targetVariable,customQueryOptions);
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
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr boundPatternQuery( Variable sourceVariable, Variable targetVariable) {
		if(sourceVariable==null)sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();
		TupleExpr boundPattern = getLeftPathElement().boundPatternQuery(sourceVariable,targetVariable).getTupleExpr() ;
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
	 * Visit path binding.
	 *
	 * @param pathBinding the path binding
	 * @param pathIteration the path iteration
	 * @return the path binding
	 */
	@Override
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		// TODO Auto-generated method stub
		return null;
	}



}
