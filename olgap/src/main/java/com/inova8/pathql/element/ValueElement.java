/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class ValueElement.
 */
public class ValueElement extends PathElement {
	
	/**
	 * Instantiates a new value element.
	 *
	 * @param repositoryContext the repository context
	 */
	public ValueElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable,Variable targetVariable, CustomQueryOptions customQueryOptions) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
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
