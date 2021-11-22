/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import java.net.URISyntaxException;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.intelligentGraphRepository.Reifications;
import com.inova8.intelligentgraph.path.StatementBinding;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;
import com.inova8.pathql.processor.PathConstants.Operator;

public abstract class PathElement {

	Operator operator;

	private PathElement rightPathElement;

	private PathElement leftPathElement;

	private String pathPattern;

	private Integer minCardinality = 1;
	private Integer maxCardinality = 1;
	private HashMap<Integer,Integer> iterationCardinality = new HashMap<Integer,Integer>();
	private Iterations iterations;
	private Integer level;

	private Integer index;

	private EdgeCode edgeCode;
//	protected PathBindings pathBindings;
	protected StatementBinding statementBinding ;
	protected Variable sourceVariable = new Variable();

	private Variable targetSubject = new Variable();

	private Variable targetPredicate = new Variable();

	protected Variable targetVariable = new Variable();

	private Integer entryIndex;

	private Integer exitIndex;

	private Integer baseIndex;

	private IntelligentGraphRepository source;

	private CustomQueryOptions customQueryOptions;

	Boolean isFirstIteration =true;
	public PathElement(IntelligentGraphRepository source) {
		this.source = source;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String toString() {
		return leftPathElement.toString() + rightPathElement.toString();
	};

	public abstract String toSPARQL();

	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		return pathBinding;
	};
	public  PathTupleExpr pathPatternQuery(Thing thing) {
		return pathPatternQuery(thing,(Variable)null,(Variable)null,null);
	};
	public  PathTupleExpr pathPatternQuery(Thing thing, Integer pathIteration,CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(thing,null,null,pathIteration,customQueryOptions);
	};
	public abstract Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode);

	protected abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions);

	protected abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration,CustomQueryOptions customQueryOptions);

	public PathTupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		if (getIsBound())
			return getLeftPathElement().boundPatternQuery(thing, sourceVariable, targetVariable);
		else
			return null;
	};

	public Boolean getIsBound() {
		if (getOperator().equals(Operator.BINDING)) {
			return true;
		} else if (getOperator().equals(Operator.PREDICATE)) {
			return false;
		} else {
			return getLeftPathElement().getIsBound();
		}
	};

	public abstract Boolean getIsNegated();

	public abstract void setIsNegated(Boolean isDereified);

	public void setLeftPathElement(PathElement leftPathElement) {
		this.leftPathElement = leftPathElement;
	}

	public PathElement getRightPathElement() {
		return rightPathElement;
	}

	public PathElement getLeftPathElement() {
		return leftPathElement;
	}

	public void setRightPathElement(PathElement rightPathElement) {
		this.rightPathElement = rightPathElement;
	}

	public abstract String toHTML();

	public void setPathPattern(String pathPattern) {
		this.pathPattern = pathPattern;

	}

	public String getPathPattern() {
		return "<a href='" + "' target='_blank'>" + pathPattern + "</a>";
	}

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

	public String getMinCardinalityString() {
		if (getMinCardinality() == 1 && (getMaxCardinality() != null && getMaxCardinality() == 1))
			return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			cardinality += "#{" + getMinCardinality() + "\r\n";
		}
		return cardinality;
	}

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

	public Integer getMinCardinality() {
		return minCardinality;
	}

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

	public void setCardinality(Integer iteration, Integer cardinality) {
		iterationCardinality.put(iteration,cardinality);
	}

	public void setMinCardinality(Integer min) {
		this.minCardinality = min;
	}

	public Integer getMaxCardinality() {
		if (maxCardinality!=null)
			return maxCardinality;
		else
			return PathConstants.MAXCARDINALITY;
	}

	public void setMaxCardinality(Integer max) {
		this.maxCardinality = max;
	}

	public Boolean getUnboundedCardinality() {
		if (getMaxCardinality() == null) {
			return true;
		} else {
			return false;
		}
	}

	public void setUnboundedCardinality(Boolean unboundedCardinality) {
		if (unboundedCardinality)
			this.setMaxCardinality(null);
	}

	public Integer getMaximumPathLength() {
		return (getLeftPathElement().getMaximumPathLength() + getRightPathElement().getMaximumPathLength())
				* getMaxCardinality();
	}

	public Integer getMinimumPathLength() {
		return (getLeftPathElement().getMinimumPathLength() + getRightPathElement().getMinimumPathLength())
				* getMinCardinality();
	}

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
	public void setIterations(Iterations iterations) {
		this.iterations=iterations;
	}
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

	public void resetIteration() {
		setCardinality(0, getMinCardinality());
		if (getRightPathElement() != null) getRightPathElement().resetIteration();
		if (getLeftPathElement() != null) getLeftPathElement().resetIteration();
	}
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
	@Deprecated
	public Integer getLevel() {
		return level;
	}
	@Deprecated
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Deprecated
	public Integer getIndex() {
		return index;
	}
	@Deprecated
	public void setIndex(Integer index) {
		this.index = index;
	}

	public EdgeCode getEdgeCode() {
		return edgeCode;
	}

	public void setEdgeCode(EdgeCode edgeCode) {
		this.edgeCode = edgeCode;
	}

	public Integer getEntryIndex() {
		return entryIndex;
	}

	public void setEntryIndex(Integer entryIndex) {
		this.entryIndex = entryIndex;
	}

	public Integer getExitIndex() {
		return exitIndex;
	}

	public void setExitIndex(Integer exitIndex) {
		this.exitIndex = exitIndex;
	}

	public Integer getBaseIndex() {
		return baseIndex;
	}

	public void setBaseIndex(Integer baseIndex) {
		this.baseIndex = baseIndex;
	}

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

	public void setSourceVariable(Variable sourceVariable) {
		if (getLeftPathElement() != null)
			getLeftPathElement().setSourceVariable(sourceVariable);
		else if (getRightPathElement() != null)
			getRightPathElement().setSourceVariable(sourceVariable);
		//TODO
		this.sourceVariable = sourceVariable;
	}
	public IRI  getParameterizedPredicate(IRI predicate ) throws URISyntaxException {
		if (getRightPathElement() != null)
			return getRightPathElement().getParameterizedPredicate(predicate);
		return null;
	}
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

	public void setTargetSubject(Variable targetSubject) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetSubject(targetSubject);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetSubject(targetSubject);
		//TODO
		this.targetSubject = targetSubject;
	}

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

	public void setTargetPredicate(Variable targetPredicate) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetPredicate(targetPredicate);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetPredicate(targetPredicate);
		//TODO
		this.targetPredicate = targetPredicate;
	}

	public Variable getTargetVariable() {
		if (getRightPathElement() != null)
			return getRightPathElement().getTargetVariable();
		else if (getLeftPathElement() != null)
			return getLeftPathElement().getTargetVariable();
		targetVariable.setName("n" + getExitIndex());
		return targetVariable;
	}

	public void setTargetVariable(Variable targetVariable) {
		if (getRightPathElement() != null)
			getRightPathElement().setTargetVariable(targetVariable);
		else if (getLeftPathElement() != null)
			getLeftPathElement().setTargetVariable(targetVariable);
		//TODO
		this.targetVariable = targetVariable;
	}

	protected IntelligentGraphRepository getSource() {
		return this.source;
	}

	public CustomQueryOptions getCustomQueryOptions() {
		return customQueryOptions;
	}

	public void setCustomQueryOptions(CustomQueryOptions customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}

	protected Reifications getReifications() {
		return getSource().getReifications();
	}

}
