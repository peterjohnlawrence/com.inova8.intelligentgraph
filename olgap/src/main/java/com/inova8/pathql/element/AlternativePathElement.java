/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.path.UnionBinding;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class AlternativePathElement.
 */
public class AlternativePathElement extends PathElement{
	
	/** The is negated. */
	private Boolean isNegated=false;

	/**
	 * Instantiates a new alternative path element.
	 *
	 * @param repositoryContext the repository context
	 */
	public AlternativePathElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
		String alternateString = "{{" + getLeftPathElement().toSPARQL() +"}UNION{\r\n";
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
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery(Variable sourceVariable,Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(sourceVariable,predicateVariable,targetVariable,0,customQueryOptions) ;
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
//	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
//			Integer pathIteration, CustomQueryOptions customQueryOptions) {
//		PathTupleExpr leftPathPattern = getLeftPathElement().pathPatternQuery(thing,sourceVariable,targetVariable,customQueryOptions) ;
//		TupleExpr leftPattern = leftPathPattern.getTupleExpr() ;
//		getRightPathElement().setSourceVariable(getLeftPathElement().getSourceVariable());
//		getRightPathElement().setTargetVariable(getLeftPathElement().getTargetVariable());
//		PathTupleExpr rightPathPattern = getRightPathElement().pathPatternQuery(thing,sourceVariable,targetVariable,customQueryOptions);
//		TupleExpr rightPattern = rightPathPattern.getTupleExpr();
//	//	Union unionPattern =new TupleExprPath( new Union(leftPattern,rightPattern)); 
//		TupleExpr alternativePattern = new Union(leftPattern,rightPattern);
//		PathTupleExpr alternativePathPattern = new PathTupleExpr( alternativePattern);
//		alternativePathPattern.setPath(leftPathPattern.getPath());
//		return alternativePathPattern;
//	}	
	public PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable,
			Integer pathIteration,CustomQueryOptions customQueryOptions) {
		if (sourceVariable == null)	sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();	
		TupleExpr intermediateUnionPattern = null;
		PathTupleExpr alternativePathPattern = null;

		if (getCardinality(pathIteration) > 0) {
			Variable intermediateSourceVariable = null;
			Variable intermediateVariable = null;
			Variable intermediateTargetVariable = null;
			Variable priorIntermediateTargetVariable = null;
			for (int iteration = 1; iteration <= getCardinality(pathIteration); iteration++) {
				if (iteration == 1) {
					intermediateSourceVariable = sourceVariable;
					intermediateVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
					//intermediateVariable=  new Variable(getLeftPathElement().getTargetVariable().getName(),getLeftPathElement().getTargetVariable().getValue());   //getLeftPathElement().getTargetVariable();
					intermediateTargetVariable=targetVariable;
				}
				if (iteration < getCardinality(pathIteration)) {
					if (iteration > 1) 
						intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
					intermediateVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
					priorIntermediateTargetVariable = intermediateTargetVariable;
				}
				if (iteration == getCardinality(pathIteration)) {
					if (iteration > 1) {
						intermediateSourceVariable = priorIntermediateTargetVariable;
						intermediateVariable = targetVariable;//new Variable(sourceVariable.getName() + "_i" + iteration);
						intermediateTargetVariable = targetVariable;
					}
				}
				predicateVariable = deduceLeftPredicateVariable(predicateVariable);
				PathTupleExpr leftPattern = getLeftPathElement().pathPatternQuery( intermediateSourceVariable,predicateVariable, 
						intermediateTargetVariable, pathIteration,customQueryOptions);
				PathTupleExpr rightPattern;
				if (leftPattern==null){
					intermediateVariable.setValue(intermediateSourceVariable.getValue());
					predicateVariable = deduceRightPredicateVariable(predicateVariable);
					//intermediateVariable.setName(intermediateSourceVariable.getName());
					rightPattern = getRightPathElement().pathPatternQuery( intermediateVariable, predicateVariable,intermediateTargetVariable,
							pathIteration,customQueryOptions);
				}else {
					predicateVariable = deduceRightPredicateVariable(predicateVariable);
					rightPattern = getRightPathElement().pathPatternQuery( intermediateSourceVariable, predicateVariable, intermediateTargetVariable, 
							pathIteration,customQueryOptions);
				}
				
				if(leftPattern!=null)
					intermediateUnionPattern = new Union(leftPattern.getTupleExpr(), rightPattern.getTupleExpr());
				else {
					intermediateUnionPattern =rightPattern.getTupleExpr();
				}
				if (alternativePathPattern == null) {
					alternativePathPattern = new PathTupleExpr((TupleExpr)intermediateUnionPattern);
					UnionBinding alternativePathPatternBinding = new UnionBinding(leftPattern.getStatementBinding(), rightPattern.getStatementBinding());
					alternativePathPattern.getPath().add(alternativePathPatternBinding);
					//if(leftPattern!=null) alternativePathPattern.getPath().addAll( leftPattern.getPath());
					//alternativePathPattern.getPath().addAll( rightPattern.getPath());
				} else {
					alternativePathPattern.setTupleExpr(new Join(alternativePathPattern.getTupleExpr(), intermediateUnionPattern));
					
					UnionBinding alternativePathPatternBinding = new UnionBinding(leftPattern.getStatementBinding(), rightPattern.getStatementBinding());
					alternativePathPattern.getPath().add(alternativePathPatternBinding);
					
					//if(leftPattern!=null)alternativePathPattern.getPath().addAll( leftPattern.getPath());
					//alternativePathPattern.getPath().addAll( rightPattern.getPath());
				}
				alternativePathPattern.setStatementBinding(rightPattern.getStatementBinding());
				alternativePathPattern.setBoundVariable(alternativePathPattern.getStatementBinding().getSourceVariable());
			}
			return alternativePathPattern;
		} else {
			return null;
		}

	}
	
	/**
	 * Deduce left predicate variable.
	 *
	 * @param predicateVariable the predicate variable
	 * @return the variable
	 */
	private Variable deduceLeftPredicateVariable(Variable predicateVariable) {
		if(predicateVariable==null) {
			predicateVariable = new Variable("L");
		}else {
			predicateVariable = new Variable(predicateVariable.getName()+"L");
		}

		return predicateVariable;
	}
	
	/**
	 * Deduce right predicate variable.
	 *
	 * @param predicateVariable the predicate variable
	 * @return the variable
	 */
	private Variable deduceRightPredicateVariable(Variable predicateVariable) {
		if(predicateVariable==null) {
			predicateVariable = new Variable("R");
		}else {
			predicateVariable = new Variable(predicateVariable.getName()+"R");
		}

		return predicateVariable;
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
	
	/**
	 * Gets the path length.
	 *
	 * @param iteration the iteration
	 * @return the path length
	 */
	@Override
	public Integer getPathLength(Integer iteration) {
		Integer pathLength = null;
		if (getLeftPathElement() != null) {
			pathLength = getLeftPathElement().getPathLength(iteration);
			if (getRightPathElement() != null) {
				if ( pathLength != null ) { 
					if(getRightPathElement().getPathLength(iteration) > pathLength) {
						pathLength = getRightPathElement().getPathLength(iteration);
					}
				} else {
						pathLength= getRightPathElement().getPathLength(iteration);
				}
			}
		}
		if (pathLength != null) {
			pathLength *= getCardinality(iteration);
		} else {
			pathLength = getCardinality(iteration);
		}
		return pathLength;
	}
//	@Override
//	public void setPathBindings(PathBindings pathBindings) {
//		this.pathBindings = pathBindings;
//		if (getRightPathElement() != null)	getRightPathElement().setPathBindings(pathBindings);
//		else if (getLeftPathElement() != null) getLeftPathElement().setPathBindings(pathBindings);	
//	}
}
