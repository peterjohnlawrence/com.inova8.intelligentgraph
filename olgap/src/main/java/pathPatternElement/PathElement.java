/*
 * inova8 2020
 */
package pathPatternElement;

import path.Path;
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

	public Integer minCardinality = 1;

	public Integer maxCardinality = 1;
	
	private Integer pathShare =0;

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

	private boolean imIt;

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

	public Path visitPath(Path path) {
		return path;
	};

	public abstract Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode);

	public abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable);
	public abstract PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, Integer pathIteration);
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
		if(getMinCardinality()==1 && ( getMaxCardinality()!=null && getMaxCardinality()== 1) ) return "";
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
		if(getMinCardinality()==1 &&  ( getMaxCardinality()!=null && getMaxCardinality()== 1) ) return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			cardinality += "#{" + getMinCardinality() + "\n";
		}
		return cardinality;
	}

	public String getMaxCardinalityString() {
		if(getMinCardinality()==1 && ( getMaxCardinality()!=null && getMaxCardinality()== 1 )) return "";
		String cardinality = "";
		if (getMinCardinality() != null) {
			if (getMaxCardinality() != null) {
				cardinality += "#," + getMaxCardinality();
			} else {
				cardinality += "#,*";
			}
			cardinality += "}\n";
		}
		return cardinality;
	}

	protected String addCardinality(String predicateString) {
		if(getMinCardinality()==1 &&  ( getMaxCardinality()!=null && getMaxCardinality()== 1 ) ) return predicateString;
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

	public void setMinCardinality(Integer min) {
		this.minCardinality = min;
	}

	public Integer getMaxCardinality() {
		return maxCardinality;
	}

	public void setMaxCardinality(Integer max) {
		this.maxCardinality = max;
	}

	public Boolean getUnboundedCardinality() {
		if(getMaxCardinality() == null ) {
			return true;
		}else {
			return false;
		}
	}

	public void setUnboundedCardinality(Boolean unboundedCardinality) {
		if(unboundedCardinality) this.setMaxCardinality( null);
	}
	 public Integer getMaximumPathLength() {
		 return (getLeftPathElement().getMaximumPathLength() + getRightPathElement().getMaximumPathLength())*getMaxCardinality();
	 }
	 public Integer getMinimumPathLength() {
		 return (getLeftPathElement().getMinimumPathLength() + getRightPathElement().getMinimumPathLength())*getMinCardinality();
	 }
	 public void setPathShare(Integer pathShare) {
		 this.pathShare=pathShare;
		 if( this.getLeftPathElement()!=null)
			 this.getLeftPathElement().setPathShare(pathShare);
	 }
	 public Integer getPathShare () {
		 return pathShare;
	 }
	 public Boolean hasNext() {
		while( passOnPathShare()) {// &&  getLeftPathElement().hasNext() && getRightPathElement().hasNext()) {
			return true;
		}
		return false;
	 }
	 private Boolean incrementRightPathElement(PathElement parentPathElement) {
		 if(getRightPathElement()!=null) {
			return  getRightPathElement().incrementRightPathElement(this);
		 }else {
			 if(canIncrementMore()) {
				 pathShare++;
				 imIt=true;
				 return true;
			 }else {
				 return parentPathElement.incrementNextLeftPathElement(parentPathElement);
			 }
		 }
	 }
	 
	private Boolean incrementNextLeftPathElement(PathElement parentPathElement) {
		 if(getLeftPathElement()!=null) {
			 if(getLeftPathElement().getRightPathElement()!=null && getLeftPathElement().getRightPathElement().canIncrementMore()) {
				 return  getLeftPathElement().getRightPathElement().incrementRightPathElement(this);
			 }else {
				 if(getLeftPathElement().canIncrementMore()) {
					 getLeftPathElement().setPathShare(getLeftPathElement().getPathShare()+1);
					 imIt=true;
					 return true;
				 }else {
					 return parentPathElement.incrementNextLeftPathElement(parentPathElement);
				 }
			 }
		 }
		 return false;
	}

	private Boolean passOnPathShare() {
		//First increment the rightmost if we can
		 if(getLeftPathElement().canIncrementMore()) {
			 if(getRightPathElement().canDecrementMore() ) {
				getLeftPathElement().setPathShare(getLeftPathElement().getPathShare()+1); 
				//Then set the left most to the remainder
				getRightPathElement().setPathShare(getRightPathElement().getPathShare()-1);	
				return true;
			 }else {
				 if(pathShare==0) {
					 return false;
				 }else {
					// pathShare --;
					 getRightPathElement().setPathShare(pathShare);
					 getLeftPathElement().setPathShare(0);
					 return true;
				 }
			 }			 
		 }else {
			 if(pathShare==0) {
				 return false;
			 }else {
				// pathShare --;
				 getRightPathElement().setPathShare(pathShare);
				 getLeftPathElement().setPathShare(0);
				 return true;
			 }		 
		 }
	}
	public Boolean canIncrementMore() {
		if(( getRightPathElement()!=null && getRightPathElement().canIncrementMore() )||( getLeftPathElement()!=null && getLeftPathElement().canIncrementMore()))
			return true;
		else 
			return false;
	}
	public Boolean canDecrementMore(){
		if(getPathShare()==0)
			return false;
		else
			return true;
	}
	public String getPathShareString() {
		StringBuilder pathShareString = new StringBuilder("");
		return  pathShareString.append("{").append(getMinCardinality()).append(",").append(getMinCardinality()+pathShare).append(",").append(getMaxCardinality()).append(",left").append(getLeftPathElement().getPathShareString()).append(",right").append(getRightPathElement().getPathShareString()).append("}").toString();
	}
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIndex() {
		return index;
	}

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
