/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.PathConstants.Operator;
import pathQLRepository.PathQLRepository;
import pathPatternProcessor.PathConstants;

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
	private Integer minCardinality = null;
	
	/** The max cardinality. */
	private Integer maxCardinality = null;
	
	/** The unbounded cardinality. */
	private Boolean unboundedCardinality = null;
	
	/** The level. */
	private Integer level;
	
	/** The index. */
	private Integer index ;
	
	/** The edge code. */
	private EdgeCode edgeCode ;
	
	/** The source variable. */
	protected Variable sourceVariable = new Variable();
	
	/** The target subject. */
	private Variable targetSubject= new Variable();
	
	/** The target predicate. */
	private Variable targetPredicate= new Variable();
	
	/** The target variable. */
	protected Variable targetVariable= new Variable();
	
	/** The entry index. */
	private Integer entryIndex;
	
	/** The exit index. */
	private Integer exitIndex;
	
	/** The base index. */
	private Integer baseIndex;

	/** The source. */
	private PathQLRepository source;

	private CustomQueryOptions customQueryOptions;
	
	/**
	 * Instantiates a new path element.
	 *
	 * @param source the source
	 */
	public PathElement(PathQLRepository source) {
		this.source= source;
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
	 * Visit path.
	 *
	 * @param path the path
	 * @return the path
	 */
	public  Path visitPath( Path path){
		return path;
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
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	public abstract TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable);
	
	/**
	 * Bound pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	public TupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		if( getIsBound())
			return getLeftPathElement().boundPatternQuery(thing,sourceVariable,targetVariable);
		else
			return null;
	};
	
	/**
	 * Gets the checks if is bound.
	 *
	 * @return the checks if is bound
	 */
	public Boolean getIsBound() {
		if ( getOperator().equals(Operator.BINDING)) {
			return true;
		}else if ( getOperator().equals(Operator.PREDICATE)) {
			return false;
		}else {
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
	public abstract void setIsNegated(Boolean isDereified) ;
	
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
		String cardinality ="";
		if (getMinCardinality() != null) {
			cardinality += "{" + getMinCardinality() ;
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
		String cardinality ="";
		if (getMinCardinality() != null) {
			cardinality += "#{" + getMinCardinality() +"\n";
		}
		return cardinality;
	}
	
	/**
	 * Gets the max cardinality string.
	 *
	 * @return the max cardinality string
	 */
	public String getMaxCardinalityString() {
		String cardinality ="";
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
	
	/**
	 * Adds the cardinality.
	 *
	 * @param predicateString the predicate string
	 * @return the string
	 */
	protected String addCardinality(String predicateString) {
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
		return maxCardinality;
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
		return unboundedCardinality;
	}

	/**
	 * Sets the unbounded cardinality.
	 *
	 * @param unboundedCardinality the new unbounded cardinality
	 */
	public void setUnboundedCardinality(Boolean unboundedCardinality) {
		this.unboundedCardinality = unboundedCardinality;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the new level
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
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
		if(getLeftPathElement()!=null)
			return getLeftPathElement().getSourceVariable();
		else if(getRightPathElement()!=null )
			return getRightPathElement().getSourceVariable();
		
		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			sourceVariable.setName("r" +  getEntryIndex());
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
		if(getLeftPathElement()!=null)
			getLeftPathElement().setSourceVariable(sourceVariable);
		else if(getRightPathElement()!=null )
			getRightPathElement().setSourceVariable(sourceVariable);
		//TODO
		this.sourceVariable = sourceVariable;
	}

	/**
	 * Gets the target subject.
	 *
	 * @return the target subject
	 */
	public Variable getTargetSubject() {
		if(getRightPathElement()!=null)
			return getRightPathElement().getTargetSubject();
		else if(getLeftPathElement()!=null )
			return getLeftPathElement().getTargetSubject();	
		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			targetSubject.setName("r" +  getEntryIndex());
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
		if(getRightPathElement()!=null)
			getRightPathElement().setTargetSubject(targetSubject);
		else if(getLeftPathElement()!=null )
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
		if(getRightPathElement()!=null)
			return getRightPathElement().getTargetPredicate();
		else if(getLeftPathElement()!=null )
			return getLeftPathElement().getTargetPredicate();	
		targetPredicate.setName("p"+ getEntryIndex()+"_"+getExitIndex() );
		//TODO
		return targetPredicate;
	}

	/**
	 * Sets the target predicate.
	 *
	 * @param targetPredicate the new target predicate
	 */
	public void setTargetPredicate(Variable targetPredicate) {
		if(getRightPathElement()!=null)
			getRightPathElement().setTargetPredicate(targetPredicate);
		else if(getLeftPathElement()!=null )
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
		if(getRightPathElement()!=null)
			return getRightPathElement().getTargetVariable();
		else if(getLeftPathElement()!=null )
			return getLeftPathElement().getTargetVariable();
		targetVariable.setName("n"+ getExitIndex());
		return targetVariable;
	}

	/**
	 * Sets the target variable.
	 *
	 * @param targetVariable the new target variable
	 */
	public void setTargetVariable(Variable targetVariable) {
		if(getRightPathElement()!=null)
			getRightPathElement().setTargetVariable(targetVariable);
		else if(getLeftPathElement()!=null )
			getLeftPathElement().setTargetVariable(targetVariable);
		//TODO
		this.targetVariable = targetVariable;
	}

//	public  void setSource(PathQLRepository source) {
//		this.source = source;
//	};
	

	/**
 * Gets the source.
 *
 * @return the source
 */
protected PathQLRepository getSource() {
		return this.source ;
	}

	public CustomQueryOptions getCustomQueryOptions() {
		return customQueryOptions;
	}

	public void setCustomQueryOptions(CustomQueryOptions customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}

}
