/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.query.algebra.Extension;
import org.eclipse.rdf4j.query.algebra.ExtensionElem;
import org.eclipse.rdf4j.query.algebra.Join;
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
		// Extend(rightPatern(?s,?p,?o),
		Variable bindSourceVariable = new Variable("bind");
		Variable bindPredicateVariable = null;//new Variable("b0_b1");
		Variable bindTargetVariable =new Variable("b");
		
		PathTupleExpr bindPattern =  ((FactFilterElement)getLeftPathElement()).filterExpression( bindSourceVariable,bindPredicateVariable,bindTargetVariable, null,customQueryOptions);

		
//		PathTupleExpr bindPattern = getLeftPathElement().pathPatternQuery(bindSourceVariable,bindPredicateVariable,bindTargetVariable,customQueryOptions);		
		PathTupleExpr rightPattern ;
		
		if(bindPattern.getTupleExpr()==null ) {
			bindSourceVariable.setName("n0");
			rightPattern = getRightPathElement().pathPatternQuery(bindSourceVariable,predicateVariable,targetVariable,pathIteration,customQueryOptions);
			rightPattern.setBoundVariable(bindSourceVariable);
			return rightPattern;
		}else {
			rightPattern = getRightPathElement().pathPatternQuery(sourceVariable,predicateVariable,targetVariable,pathIteration, customQueryOptions);
			Extension leftPattern = new Extension(bindPattern.getTupleExpr(),new ExtensionElem(bindSourceVariable, "n0" ) );
			Join boundPattern = new Join(leftPattern,rightPattern.getTupleExpr() );		
			PathTupleExpr boundPatternTupleExpr = new PathTupleExpr( boundPattern);
			boundPatternTupleExpr.setStatementBinding(rightPattern.getStatementBinding());
			boundPatternTupleExpr.setBoundVariable(bindSourceVariable);
			return boundPatternTupleExpr;	
		}
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
		return  getRightPathElement().visitPathBinding(pathBinding,pathIteration);
		
	}



}
