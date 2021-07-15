/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.HashMap;

import path.PathBinding;
import path.PathTupleExpr;
import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.PathConstants.Operator;
import pathQLRepository.PathQLRepository;
import pathQLRepository.Reifications;
import pathPatternProcessor.PathConstants;

public abstract class PathElement {

	Operator operator;

	private PathElement rightPathElement;

	private PathElement leftPathElement;

	private String pathPattern;

	private Integer minCardinality = 1;
	private Integer maxCardinality = 1;
	private HashMap<Integer,Integer> iterationCardinality = new HashMap<Integer,Integer>();
	private Iterations iterations;
	//private Integer pathShare = 0;
	private Integer level;

	private Integer index;

	private EdgeCode edgeCode;

	protected Variable sourceVariable = new Variable();

	private Variable targetSubject = new Variable();

	private Variable targetPredicate = new Variable();

	protected Variable targetVariable = new Variable();

	private Integer entryIndex;

	private Integer exitIndex;

	private Integer baseIndex;

	private PathQLRepository source;

	private CustomQueryOptions customQueryOptions;

	Boolean isFirstIteration =true;
	public PathElement(PathQLRepository source) {
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

	public PathBinding visitPath(PathBinding path) {
		return path;
	};

	public abstract Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode);

	public abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable);

	public abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration);

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
	public void resetIteration() {
		setCardinality(0, getMinCardinality());
		if (getRightPathElement() != null) getRightPathElement().resetIteration();
		if (getLeftPathElement() != null) getLeftPathElement().resetIteration();
	}
	public Boolean hasNextCardinality(Integer iteration) {
		if (getLeftPathElement() != null) {
			if (getRightPathElement() != null) {
				if (getRightPathElement().hasNextCardinality(iteration)) {
					setCardinality(iteration, iterationCardinality.get(iteration-1));
					return true;
				} else {
					if (getLeftPathElement().hasNextCardinality(iteration)) {
						setCardinality(iteration, iterationCardinality.get(iteration-1));
						return true;
					} else {
						setCardinality(iteration, iterationCardinality.get(iteration-1));
						return false;
					}
				}
			}
		}
		return false;
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

	protected PathQLRepository getSource() {
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
