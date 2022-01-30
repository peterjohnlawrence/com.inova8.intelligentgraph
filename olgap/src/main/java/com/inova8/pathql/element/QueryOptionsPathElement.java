/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class QueryOptionsPathElement.
 */
public class QueryOptionsPathElement extends PathElement{

	/**
	 * Instantiates a new query options path element.
	 *
	 * @param repositoryContext the repository context
	 */
	public QueryOptionsPathElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.QUERYOPTIONS;
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
	public String toSPARQL() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {
		// TODO Auto-generated method stub
		return null;
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
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
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



}
