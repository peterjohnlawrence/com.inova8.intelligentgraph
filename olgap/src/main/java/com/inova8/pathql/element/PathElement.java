/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import java.net.URISyntaxException;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;

import com.inova8.intelligentgraph.path.StatementBinding;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.Reifications;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;
import com.inova8.pathql.processor.PathConstants.Operator;

/**
 * The Class PathElement.
 */
public abstract class PathElement {

	/** The operator. */
	Operator operator;

	/** The right path element. */
	private PathElement rightPathElement;

	/** The left path element. */
	private PathElement leftPathElement;

	/** The path pattern. */
	private String pathPattern;

	/** The min cardinality. */
	private Integer minCardinality = 1;
	
	/** The max cardinality. */
	private Integer maxCardinality = 1;
	
	/** The iteration cardinality. */
	private HashMap<Integer,Integer> iterationCardinality = new HashMap<Integer,Integer>();
	
	/** The iterations. */
	private Iterations iterations;
	
	/** The level. */
	private Integer level;

	/** The index. */
	private Integer index;

	/** The edge code. */
	private EdgeCode edgeCode;

/** The statement binding. */
//	protected PathBindings pathBindings;
	protected StatementBinding statementBinding ;
	
	/** The source variable. */
	protected Variable sourceVariable = new Variable();

	/** The target subject. */
	private Variable targetSubject = new Variable();

	/** The target predicate. */
	private Variable targetPredicate = new Variable();

	/** The target variable. */
	protected Variable targetVariable = new Variable();

	/** The entry index. */
	private Integer entryIndex;

	/** The exit index. */
	private Integer exitIndex;

	/** The base index. */
	private Integer baseIndex;

//	private IntelligentGraphRepository source;

	/** The custom query options. */
private CustomQueryOptions customQueryOptions;

	/** The is first iteration. */
	Boolean isFirstIteration =true;

	/** The repository context. */
	private RepositoryContext repositoryContext;
//	public PathElement(IntelligentGraphRepository source) {
//		this.source = source;
/**
 * Instantiates a new path element.
 *
 * @param repositoryContext the repository context
 */
//	}
	public PathElement(RepositoryContext repositoryContext) {
		this.repositoryContext = repositoryContext;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return leftPathElement.toString() + rightPathElement.toString();
	};

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public abstract String toSPARQL();

	/**
	 * Visit path binding.
	 *
	 * @param pathBinding the path binding
	 * @param pathIteration the path iteration
	 * @return the path binding
	 */
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		return pathBinding;
	};
	
	/**
	 * Path pattern query.
	 *
	 * @return the path tuple expr
	 */
	public  PathTupleExpr pathPatternQuery() {
		return pathPatternQuery((Variable)null,(Variable)null,(Variable)null,null);
	};
	
	/**
	 * Path pattern query.
	 *
	 * @param pathIteration the path iteration
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	public  PathTupleExpr pathPatternQuery( Integer pathIteration,CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(null,null,null,pathIteration,customQueryOptions);
	};
	
	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
	public abstract Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode);

	/**
	 * Path pattern query.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	protected abstract PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions);

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
	protected abstract PathTupleExpr pathPatternQuery(Variable sourceVariable, Variable predicateVariable,Variable targetVariable,
			Integer pathIteration,CustomQueryOptions customQueryOptions);

	/**
	 * Bound pattern query.
	 *
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the path tuple expr
	 */
	public PathTupleExpr boundPatternQuery( Variable sourceVariable, Variable targetVariable) {
		if (getIsBound())
			return getLeftPathElement().boundPatternQuery( sourceVariable, targetVariable);
		else
			return null;
	};

	/**
	 * Gets the checks if is bound.
	 *
	 * @return the checks if is bound
	 */
	public Boolean getIsBound() {
		if (getOperator().equals(Operator.BINDING)) {
			return true;
		} else if (getOperator().equals(Operator.PREDICATE)) {
			return false;
		} else {
			return getLeftPathElement().getIsBound();
		}
	};

	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	public abstract Boolean getIsNegated();

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isDereified the new checks if is negated
	 */
	public abstract void setIsNegated(Boolean isDereified);

	/**
	 * Sets the left path element.
	 *
	 * @param leftPathElement the new left path element
	 */
	public void setLeftPathElement(PathElement leftPathElement) {
		this.leftPathElement = leftPathElement;
	}

	/**
	 * Gets the right path element.
	 *
	 * @return the right path element
	 */
	public PathElement getRightPathElement() {
		return rightPathElement;
	}

	/**
	 * Gets the left path element.
	 *
	 * @return the left path element
	 */
	public PathElement getLeftPathElement() {
		return leftPathElement;
	}

	/**
	 * Sets the right path element.
	 *
	 * @param rightPathElement the new right path element
	 */
	public void setRightPathElement(PathElement rightPathElement) {
		this.rightPathElement = rightPathElement;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	public abstract String toHTML();

	/**
	 * Sets the path pattern.
	 *
	 * @param pathPattern the new path pattern
	 */
	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;

	}

	/**
	 * Gets the path pattern.
	 *
	 * @return the path pattern
	 */
	public String getPathPattern() {
		return "<a href='" + "' target='_blank'>" + pathPattern + "</a>";
	}

	/**
	 * Gets the cardinality string.
	 *
	 * @return the cardinality string
	 */
	public String getCardinalityString() {
		if (getMinCardinality() == 1 && (getMaxCardinality() != null && getMaxCardinality() == 1))
			return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			cardinality += "{" + getMinCardinality();
			if (getMaxCardinality() != null) {
				cardinality += "," + getMaxCardinality();
			} else {
				cardinality += ",*";
			}
			cardinality += "}";
		}
		return cardinality;
	}

	/**
	 * Gets the min cardinality string.
	 *
	 * @return the min cardinality string
	 */
	public String getMinCardinalityString() {
		if (getMinCardinality() == 1 && (getMaxCardinality() != null && getMaxCardinality() == 1))
			return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			cardinality += "#{" + getMinCardinality() + "\r\n";
		}
		return cardinality;
	}

	/**
	 * Gets the max cardinality string.
	 *
	 * @return the max cardinality string
	 */
	public String getMaxCardinalityString() {
		if (getMinCardinality() == 1 && (getMaxCardinality() != null && getMaxCardinality() == 1))
			return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			if (getMaxCardinality() != null) {
				cardinality += "#," + getMaxCardinality();
			} else {
				cardinality += "#,*";
			}
			cardinality += "}\r\n";
		}
		return cardinality;
	}

	/**
	 * Adds the cardinality.
	 *
	 * @param predicateString the predicate string
	 * @return the string
	 */
	protected String addCardinality(String predicateString) {
		if (getMinCardinality() == 1 && (getMaxCardinality() != null && getMaxCardinality() == 1))
			return predicateString;
		if (getMinCardinality() != null) {
			predicateString = "(" + predicateString + "){" + getMinCardinality();
			if (getMaxCardinality() != null) {
				predicateString += "," + getMaxCardinality();
			} else if (getUnboundedCardinality() != null) {
				predicateString += ",*";
			}
			predicateString += "}";
		}
		return predicateString;
	}

	/**
	 * Gets the min cardinality.
	 *
	 * @return the min cardinality
	 */
	public Integer getMinCardinality() {
		return minCardinality;
	}

	/**
	 * Gets the cardinality.
	 *
	 * @param iteration the iteration
	 * @return the cardinality
	 */
	public Integer getCardinality(Integer iteration) {
		Integer cardinality = iterationCardinality.get(iteration);
		if(cardinality !=null) 
			return cardinality;
		else {
			if(iteration==0) {
				cardinality = getMinCardinality();
				return cardinality;
			}else {
				cardinality = getCardinality(iteration-1);
				return cardinality;
			}
		}
			
	}

	/**
	 * Sets the cardinality.
	 *
	 * @param iteration the iteration
	 * @param cardinality the cardinality
	 */
	public void setCardinality(Integer iteration, Integer cardinality) {
		iterationCardinality.put(iteration,cardinality);
	}

	/**
	 * Sets the min cardinality.
	 *
	 * @param min the new min cardinality
	 */
	public void setMinCardinality(Integer min) {
		this.minCardinality = min;
	}

	/**
	 * Gets the max cardinality.
	 *
	 * @return the max cardinality
	 */
	public Integer getMaxCardinality() {
		if (maxCardinality!=null)
			return maxCardinality;
		else
			return PathConstants.MAXCARDINALITY;
	}

	/**
	 * Sets the max cardinality.
	 *
	 * @param max the new max cardinality
	 */
	public void setMaxCardinality(Integer max) {
		this.maxCardinality = max;
	}

	/**
	 * Gets the unbounded cardinality.
	 *
	 * @return the unbounded cardinality
	 */
	public Boolean getUnboundedCardinality() {
		if (getMaxCardinality() == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the unbounded cardinality.
	 *
	 * @param unboundedCardinality the new unbounded cardinality
	 */
	public void setUnboundedCardinality(Boolean unboundedCardinality) {
		if (unboundedCardinality)
			this.setMaxCardinality(null);
	}

	/**
	 * Gets the maximum path length.
	 *
	 * @return the maximum path length
	 */
	public Integer getMaximumPathLength() {
		return (getLeftPathElement().getMaximumPathLength() + getRightPathElement().getMaximumPathLength())
				* getMaxCardinality();
	}

	/**
	 * Gets the minimum path length.
	 *
	 * @return the minimum path length
	 */
	public Integer getMinimumPathLength() {
		return (getLeftPathElement().getMinimumPathLength() + getRightPathElement().getMinimumPathLength())
				* getMinCardinality();
	}

	/**
	 * Gets the path length.
	 *
	 * @param iteration the iteration
	 * @return the path length
	 */
	public Integer getPathLength(Integer iteration) {
		Integer pathLength = null;
		if (getLeftPathElement() != null) {
			pathLength = getLeftPathElement().getPathLength(iteration);
			if (getRightPathElement() != null) {
				if (pathLength != null) {
						pathLength += getRightPathElement().getPathLength(iteration);
				}else {
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
	
	/**
	 * Sets the iterations.
	 *
	 * @param iterations the new iterations
	 */
	public void setIterations(Iterations iterations) {
		this.iterations=iterations;
	}
	
	/**
	 * Gets the iterations.
	 *
	 * @return the iterations
	 */
	public Iterations getIterations() {
		return this.iterations;
	}
	
//	public PathBindings getPathBindings() {
//		return pathBindings;
//	}
//
//	public void setPathBindings(PathBindings pathBindings) {
//		this.pathBindings = pathBindings;
//		if (getRightPathElement() != null) getRightPathElement().setPathBindings(pathBindings);
//		if (getLeftPathElement() != null) getLeftPathElement().setPathBindings(pathBindings);	
//	}

	/**
 * Reset iteration.
 */
public void resetIteration() {
		setCardinality(0, getMinCardinality());
		if (getRightPathElement() != null) getRightPathElement().resetIteration();
		if (getLeftPathElement() != null) getLeftPathElement().resetIteration();
	}
	
	/**
	 * Checks for next cardinality.
	 *
	 * @param iteration the iteration
	 * @return the boolean
	 */
	public Boolean hasNextCardinality(Integer iteration) {
		if (getLeftPathElement() != null) {
			if (getRightPathElement() != null) {
				if (getRightPathElement().hasNextCardinality(iteration)) {
					setCardinality(iteration,getCardinality(iteration-1));
					return true;
				} else {
					if (getLeftPathElement().hasNextCardinality(iteration)) {
						setCardinality(iteration,getCardinality(iteration-1));
						return true;
					} else {
						 Integer cardinality = getCardinality(iteration-1);
						 if( cardinality < getMaxCardinality()) {
							cardinality++;
							setCardinality(iteration,cardinality);
							return true;
						 }else {
							setCardinality(iteration,getMinCardinality());
							return false;
						 }
					}
				}
			}
		}
		return false;
//		if (getLeftPathElement() != null) {
//			if (getRightPathElement() != null) {
//				if (getRightPathElement().hasNextCardinality(iteration)) {
//					setCardinality(iteration, iterationCardinality.get(iteration-1));
//					return true;
//				} else {
//					if (getLeftPathElement().hasNextCardinality(iteration)) {
//						setCardinality(iteration, iterationCardinality.get(iteration-1));
//						return true;
//					} else {
//						setCardinality(iteration, iterationCardinality.get(iteration-1));
//						return false;
//					}
//				}
//			}
//		}
//		return false;
	}
	
	/**
	 * Gets the path share string.
	 *
	 * @param iteration the iteration
	 * @return the path share string
	 */
	public String getPathShareString(Integer iteration) {
		StringBuilder pathShareString = new StringBuilder("");
		return pathShareString.append("(")
				.append(getLeftPathElement().getPathShareString(iteration))
				.append("/")
				.append(getRightPathElement().getPathShareString(iteration))
				.append(")")
				.append("{").append(getMinCardinality()).append(",")
				.append(getCardinality(iteration))
				.append(",").append(getMaxCardinality()).append("}")
				.toString();
	}
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	@Deprecated
	public Integer getLevel() {
		return level;
	}
	
	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	@Deprecated
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	@Deprecated
	public Integer getIndex() {
		return index;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	@Deprecated
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Gets the edge code.
	 *
	 * @return the edge code
	 */
	public EdgeCode getEdgeCode() {
		return edgeCode;
	}

	/**
	 * Sets the edge code.
	 *
	 * @param edgeCode the new edge code
	 */
	public void setEdgeCode(EdgeCode edgeCode) {
		this.edgeCode = edgeCode;
	}

	/**
	 * Gets the entry index.
	 *
	 * @return the entry index
	 */
	public Integer getEntryIndex() {
		return entryIndex;
	}

	/**
	 * Sets the entry index.
	 *
	 * @param entryIndex the new entry index
	 */
	public void setEntryIndex(Integer entryIndex) {
		this.entryIndex = entryIndex;
	}

	/**
	 * Gets the exit index.
	 *
	 * @return the exit index
	 */
	public Integer getExitIndex() {
		return exitIndex;
	}

	/**
	 * Sets the exit index.
	 *
	 * @param exitIndex the new exit index
	 */
	public void setExitIndex(Integer exitIndex) {
		this.exitIndex = exitIndex;
	}

	/**
	 * Gets the base index.
	 *
	 * @return the base index
	 */
	public Integer getBaseIndex() {
		return baseIndex;
	}

	/**
	 * Sets the base index.
	 *
	 * @param baseIndex the new base index
	 */
	public void setBaseIndex(Integer baseIndex) {
		this.baseIndex = baseIndex;
	}

	/**
	 * Gets the source variable.
	 *
	 * @return the source variable
	 */
	public Variable getSourceVariable() {
		if (getLeftPathElement() != null)
			return getLeftPathElement().getSourceVariable();
		else if (getRightPathElement() != null)
			return getRightPathElement().getSourceVariable();

		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			sourceVariable.setName("r" + getEntryIndex());
		} else {
			sourceVariable.setName("n" + getEntryIndex());// (getIndex());
		}

		return sourceVariable;
	}

	/**
	 * Sets the source variable.
	 *
	 * @param sourceVariable the new source variable
	 */
	public void setSourceVariable(Variable sourceVariable) {
		if (getLeftPathElement() != null)
			getLeftPathElement().setSourceVariable(sourceVariable);
		else if (getRightPathElement() != null)
			getRightPathElement().setSourceVariable(sourceVariable);
		//TODO
		this.sourceVariable = sourceVariable;
	}
	
	/**
	 * Gets the parameterized predicate.
	 *
	 * @param predicate the predicate
	 * @return the parameterized predicate
	 * @throws URISyntaxException the URI syntax exception
	 */
	public IRI  getParameterizedPredicate(IRI predicate ) throws URISyntaxException {
		if (getRightPathElement() != null)
			return getRightPathElement().getParameterizedPredicate(predicate);
		return null;
	}
	
	/**
	 * Gets the target subject.
	 *
	 * @return the target subject
	 */
	public Variable getTargetSubject() {
		if (getRightPathElement() != null)
			return getRightPathElement().getTargetSubject();
		else if (getLeftPathElement() != null)
			return getLeftPathElement().getTargetSubject();
		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			targetSubject.setName("r" + getEntryIndex());
		} else {
			targetSubject.setName("n" + getEntryIndex());// (getIndex());
		}
		//targetSubject.setName("n"+ getEntryIndex());
		return targetSubject;
	}

	/**
	 * Sets the target subject.
	 *
	 * @param targetSubject the new target subject
	 */
	public void setTargetSubject(Variable targetSubject) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetSubject(targetSubject);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetSubject(targetSubject);
		//TODO
		this.targetSubject = targetSubject;
	}

	/**
	 * Gets the target predicate.
	 *
	 * @return the target predicate
	 */
	public Variable getTargetPredicate() {
		if (getRightPathElement() != null)
			return getRightPathElement().getTargetPredicate();
		else if (getLeftPathElement() != null)
			return getLeftPathElement().getTargetPredicate();
		targetPredicate.setName("p" + getEntryIndex() + "_" + getExitIndex());
		//TODO
		targetPredicate.setName("p_" + getTargetSubject().getName() + "_" + getTargetVariable().getName());
		return targetPredicate;
	}

	/**
	 * Sets the target predicate.
	 *
	 * @param targetPredicate the new target predicate
	 */
	public void setTargetPredicate(Variable targetPredicate) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetPredicate(targetPredicate);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetPredicate(targetPredicate);
		//TODO
		this.targetPredicate = targetPredicate;
	}

	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	public Variable getTargetVariable() {
		if (getRightPathElement() != null)
			return getRightPathElement().getTargetVariable();
		else if (getLeftPathElement() != null)
			return getLeftPathElement().getTargetVariable();
		targetVariable.setName("n" + getExitIndex());
		return targetVariable;
	}

	/**
	 * Sets the target variable.
	 *
	 * @param targetVariable the new target variable
	 */
	public void setTargetVariable(Variable targetVariable) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetVariable(targetVariable);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetVariable(targetVariable);
		//TODO
		this.targetVariable = targetVariable;
	}

//	protected IntelligentGraphRepository getSource() {
//		return this.source;
//	}

	/**
 * Gets the custom query options.
 *
 * @return the custom query options
 */
public CustomQueryOptions getCustomQueryOptions() {
		return customQueryOptions;
	}

	/**
	 * Sets the custom query options.
	 *
	 * @param customQueryOptions the new custom query options
	 */
	public void setCustomQueryOptions(CustomQueryOptions customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}

	/**
	 * Gets the reifications.
	 *
	 * @return the reifications
	 */
	protected Reifications getReifications() {
		return repositoryContext.getReifications();
	}

}
