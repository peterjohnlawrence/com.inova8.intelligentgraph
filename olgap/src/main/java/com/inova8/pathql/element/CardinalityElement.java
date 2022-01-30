/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class CardinalityElement.
 */
public class CardinalityElement extends PathElement {

	/**
	 * Instantiates a new cardinality element.
	 *
	 * @param repositoryContext the repository context
	 */
	public CardinalityElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.CARDINALITY;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String cardinalityElement = "{" + getMinCardinality();
		if (getMaxCardinality() != null) {
			cardinalityElement += "," + getMaxCardinality();
		} else if (getUnboundedCardinality()) {
			cardinalityElement += ",*";
		}
		return cardinalityElement + "}";
	}
	
	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {

		String cardinalityElement = "{" + getMinCardinality();
		if (getMaxCardinality() != null) {
			cardinalityElement += "," + getMaxCardinality();
		} else if (getUnboundedCardinality()) {
			cardinalityElement += ",*";
		}
		return cardinalityElement + "}";
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
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable,Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return null;
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
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable,Variable targetVariable,
			Integer pathIteration, CustomQueryOptions customQueryOptions) {
		return null;
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
		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, edgeCode);
		setExitIndex(rightExitIndex) ;
		return rightExitIndex;
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
