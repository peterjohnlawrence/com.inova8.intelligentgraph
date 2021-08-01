/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.algebra.Compare;
import org.eclipse.rdf4j.query.algebra.Compare.CompareOp;

import path.EdgeBinding;
import path.PathBinding;
import path.PathTupleExpr;
import pathCalc.CustomQueryOptions;
import pathCalc.Thing;

import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class PredicateElement.
 */
public class PredicateElement extends PathElement {
	
	/** The is inverse of. */
	private Boolean isInverseOf = false;
	
	/** The is reified. */
	private Boolean isReified = false;
	
	/** The is dereified. */
	private Boolean isDereified = false;
	
	/** The is negated. */
	private Boolean isNegated = false;
	
	/** The predicate. */
	private IRI predicate;
	
	/** The any predicate. */
	private Boolean anyPredicate;
	
	/** The reification. */
	private IRI reification;

	/** The object filter element. */
	private FactFilterElement objectFilterElement;
	
	/** The statement filter element. */
	private FactFilterElement statementFilterElement;
	
	/** The target object. */
	private Variable targetObject = new Variable();


	/**
	 * Instantiates a new predicate element.
	 *
	 * @param source the source
	 * @param isInverseOf the is inverse of
	 * @param isReified the is reified
	 * @param predicate the predicate
	 * @param reification the reification
	 */
	public PredicateElement(PathQLRepository source, Boolean isInverseOf, Boolean isReified, IRI predicate, IRI reification) {
		super(source);
		operator = PathConstants.Operator.PREDICATE;
		this.isInverseOf = isInverseOf;
		this.isReified = isReified;
		this.predicate = predicate;
		this.reification = reification;

	}

	/**
	 * Instantiates a new predicate element.
	 *
	 * @param source the source
	 */
	public PredicateElement(PathQLRepository source) {
		super(source);
		operator = PathConstants.Operator.PREDICATE;
	}

	/**
	 * Instantiates a new predicate element.
	 *
	 * @param source the source
	 * @param predicate the predicate
	 */
	public PredicateElement(PathQLRepository source,IRI predicate) {
		super(source);
		operator = PathConstants.Operator.PREDICATE;
		setPredicate(predicate);
	}
	 public Integer getMaximumPathLength() {
		 return getMaxCardinality();
	 }
	 public Integer getMinimumPathLength() {
		 return getMinCardinality();
	 }
	/**
	 * Gets the checks if is inverse of.
	 *
	 * @return the checks if is inverse of
	 */
	public Boolean getIsInverseOf() {
		return isInverseOf;
	}
	public Boolean hasNextCardinality(Integer iteration) {
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
	public String getPathShareString(Integer iteration) {
		StringBuilder pathShareString = new StringBuilder("");
		return  pathShareString.append("{").append(getMinCardinality()).append(",")
				.append(getCardinality(iteration))//cardinality)
				.append(",").append(getMaxCardinality()).append("}").toString();
	}
//	public Boolean hasNext() {
//		if((getPathShare() + getMinimumPathLength()) <= getMaximumPathLength())
//			return true;
//		else
//			return false;	
//	}
//	public Boolean canIncrementMore(){
//		return ((getPathShare() + getMinCardinality()) < getMaxCardinality());
//	}
//	public Boolean canDecrementMore(){
//		return ( getPathShare() > 0 );
//	}

	/**
	 * Sets the checks if is inverse of.
	 *
	 * @param isInverseOf the new checks if is inverse of
	 */
	public void setIsInverseOf(Boolean isInverseOf) {
		this.isInverseOf = isInverseOf;
	}

	/**
	 * Gets the checks if is reified.
	 *
	 * @return the checks if is reified
	 */
	public Boolean getIsReified() {
		return isReified;
	}

	/**
	 * Sets the checks if is reified.
	 *
	 * @param isReified the new checks if is reified
	 */
	public void setIsReified(Boolean isReified) {
		this.isReified = isReified;
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public IRI getPredicate() {
		return predicate;
	}

	/**
	 * Gets the predicate SPARQL.
	 *
	 * @return the predicate SPARQL
	 */
	public String getPredicateSPARQL() {
		if (getAnyPredicate()) {
			return "?"+ getPredicateSPARQLVariable();
		} else {
			return "<" + predicate.stringValue() + ">";
		}

	}

	/**
	 * Gets the predicate SPARQL variable.
	 *
	 * @return the predicate SPARQL variable
	 */
	public String getPredicateSPARQLVariable() {
	//	return "?p" + getIndex() + "_" + (getIndex()+1);
		if(getBaseIndex() == null)
			return "p" + getEntryIndex() + "_" + getExitIndex();
		else 
			return "p" + getBaseIndex() + "_" + getEntryIndex() + "_" + getExitIndex();
//		else
//			return "?p" + getIndex();
	}

	/**
	 * Sets the predicate.
	 *
	 * @param predicate the new predicate
	 */
	public void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	public IRI getReification() {
		return reification;
	}

	/**
	 * Sets the reification.
	 *
	 * @param reification the new reification
	 */
	public void setReification(IRI reification) {
		this.reification = reification;
	}
	
	/**
	 * Gets the reified variable.
	 *
	 * @return the reified variable
	 */
	public Variable getReifiedVariable() {
		String reificationValue = "r" + getExitIndex();
		Variable reificationVariable = new Variable(reificationValue);
		return reificationVariable;
	}
	
	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	public Variable getTargetVariable() {
		if(getRightPathElement()!=null)
			return getRightPathElement().getTargetVariable();
		else
		{
			if (getIsDereified()) {
				return (Variable) getReifiedVariable();
			}else {
				targetVariable.setName("n"+ getExitIndex());
				return targetVariable;
			}
		}
	}
	
	/**
	 * Gets the checks if is dereified.
	 *
	 * @return the checks if is dereified
	 */
	public Boolean getIsDereified() {
		return isDereified;
	}

	/**
	 * Sets the checks if is dereified.
	 *
	 * @param isDereified the new checks if is dereified
	 */
	public void setIsDereified(Boolean isDereified) {
		this.isDereified = isDereified;
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
	 * @param isDereified the new checks if is negated
	 */
	public void setIsNegated(Boolean isDereified) {
		this.isNegated = isDereified;
	}

	/**
	 * Gets the object filter element.
	 *
	 * @return the object filter element
	 */
	public FactFilterElement getObjectFilterElement() {
		return objectFilterElement;
	}

	/**
	 * Sets the object filter element.
	 *
	 * @param objectFilterElement the new object filter element
	 */
	public void setObjectFilterElement(FactFilterElement objectFilterElement) {
		this.objectFilterElement = objectFilterElement;
	}

	/**
	 * Gets the statement filter element.
	 *
	 * @return the statement filter element
	 */
	public FactFilterElement getStatementFilterElement() {
		return statementFilterElement;
	}

	/**
	 * Sets the statement filter element.
	 *
	 * @param statementFilterElement the new statement filter element
	 */
	public void setStatementFilterElement(FactFilterElement statementFilterElement) {
		this.statementFilterElement = statementFilterElement;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	public String toHTML() {

		String predicateString = "";
		if (isNegated)
			predicateString += "!";
		if (isInverseOf)
			predicateString += "^";
		if (getIsReified()) {
			if (reification != null)
				predicateString += "<" + reification.stringValue() + ">";
			predicateString += "@";
			predicateString += "<" + predicate.stringValue() + ">";
			if (objectFilterElement != null)
				predicateString += objectFilterElement.toString();
			if (isDereified)
				predicateString += "#";
			if (statementFilterElement != null)
				predicateString += statementFilterElement.toString();
		} else {
			if (predicate != null)
				predicateString += "<" + predicate.stringValue() + ">";
			else
				predicateString += "<NULL>";
			if (objectFilterElement != null)
				predicateString += objectFilterElement.toString();
		}
		predicateString += getCardinalityString();
		return predicateString;

	}

	public String toString() {

		String predicateString = "";
		if (isNegated)
			predicateString += "!";
		if (isInverseOf)
			predicateString += "^";
		if (getIsReified()) {
			if (reification != null)
				predicateString += "<" + reification.stringValue() + ">";
			predicateString += "@";
			predicateString += "<" + predicate.stringValue() + ">";
			if (objectFilterElement != null)
				predicateString += objectFilterElement.toString();
			if (isDereified)
				predicateString += "#";
			if (statementFilterElement != null)
				predicateString += statementFilterElement.toString();
		} else {
			if (predicate != null)
				predicateString += "<" + predicate.stringValue() + ">";
			else if (getAnyPredicate())
				predicateString += "*";
			else
				predicateString += "<NULL>";
			if (objectFilterElement != null)
				predicateString += objectFilterElement.toString();
		}
		predicateString += getCardinalityString();
		return predicateString;

	}

	public String toSPARQL() {
		return unboundPredicateToSPARQL();
	}


	private Variable getTargetObject() {
		targetObject.setName("n" + getExitIndex());
		return targetObject;
	}

	protected String boundPredicateToSPARQL(String sourceValue, String targetValue) {
		String predicateString = "";

		predicateString += getMinCardinalityString();

		ArrayList<String> targetValues = new ArrayList<String>();

		if (objectFilterElement != null)
			targetValues = objectFilterElement.bindTargetValue(targetValue);
		if (targetValues.size() == 0)
			targetValues.add(targetValue);

		if (getIsReified()) {
			String reificationValue = "?r" + getExitIndex(); 
			IRI subject = getReifications().getReificationSubject(reification);
			IRI isSubjectOf = getReifications().getReificationIsSubjectOf(reification);
			IRI property = getReifications().getReificationPredicate(reification);
			IRI isPropertyOf = getReifications().getReificationIsPredicateOf(reification);
			IRI object = getReifications().getReificationObject(reification);
			IRI isObjectOf = getReifications().getReificationIsObjectOf(reification);

			if (isInverseOf) {
				// ?reification :reificationObject  :thing .  :thing   :reificationIsObjectOf  ?reification .
				if (object != null && isObjectOf != null) {
					predicateString += "{{" + reificationValue + " <" + object.stringValue() + "> " + sourceValue;
					predicateString += " }UNION{ ";
					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
							+ " }}\r\n";
				} else if (object != null) {
					predicateString += reificationValue + " <" + object.stringValue() + "> " + sourceValue + "\r\n";
				} else if (isObjectOf != null) {
					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue + "\r\n";
				} else {
				}
			} else {
				// ?reification :reificationSubject  :thing .  :thing   :reificationIsSubjectOf  ?reification .
				if (subject != null && isSubjectOf != null) {
					predicateString += "{{" + reificationValue + " <" + subject.stringValue() + "> " + sourceValue;
					predicateString += " }UNION{ ";
					predicateString += sourceValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
							+ " }}\r\n";
				} else if (subject != null) {
					predicateString += reificationValue + " <" + subject.stringValue() + "> " + sourceValue + "\r\n";
				} else if (isSubjectOf != null) {
					predicateString += sourceValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue + "\r\n";
				} else {
				}
			}
			// ?reification :reificationPredicate :predicate . :predicate   :reificationIsPredicateOf ?reification .
			if (property != null && isPropertyOf != null) {
				predicateString += "{{" + reificationValue + " <" + property.stringValue() + "> "
						+ getPredicateSPARQL();
				predicateString += " }UNION{ ";
				predicateString += getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue
						+ " }}\r\n";
			} else if (property != null) {
				predicateString += reificationValue + " <" + property.stringValue() + "> " + getPredicateSPARQL()
						+ "\r\n";
			} else if (isPropertyOf != null) {
				predicateString += getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue
						+ "\r\n";
			} else {
			}
			for (String aTargetValue : targetValues) {
				if (isInverseOf) {
					//?reification :reificationSubject ?reifiedValue .  ?reifiedValue   :reificationIsSubjectOf  ?reification .
					if (subject != null && isSubjectOf != null) {
						predicateString += "{{" + reificationValue + " <" + subject.stringValue() + "> " + aTargetValue;
						predicateString += " }UNION{ ";
						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
								+ " }}\r\n";
					} else if (subject != null) {
						predicateString += reificationValue + " <" + subject.stringValue() + "> " + aTargetValue + "\r\n";
					} else if (isSubjectOf != null) {
						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
								+ "\r\n";
					} else {
					}
				} else {
					//?reification :reificationObject ?reifiedValue .  ?reifiedValue   :reificationIsObjectOf  ?reification .
					if (object != null && isObjectOf != null) {
						predicateString += "{{" + reificationValue + " <" + object.stringValue() + "> " + aTargetValue;
						predicateString += " }UNION{ ";
						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
								+ " }}\r\n";
					} else if (object != null) {
						predicateString += reificationValue + " <" + object.stringValue() + "> " + aTargetValue + "\r\n";
					} else if (isObjectOf != null) {
						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
								+ "\r\n";
					} else {
					}
				}
				//indices.set(level,index+1);
				if (objectFilterElement != null)
					predicateString += objectFilterElement.toSPARQL(aTargetValue);
			}

			if (statementFilterElement != null)
				predicateString += statementFilterElement.toSPARQL(reificationValue);
		} else {
			for (String aTargetValue : targetValues) {
				if (isNegated) {
					if (isInverseOf) {
						predicateString += aTargetValue + "?"+ getPredicateSPARQLVariable() + " " + sourceValue + ". FILTER("
								+ "?"+ getPredicateSPARQLVariable() + "!=" + getPredicateSPARQL() + ").\r\n";
					} else {
						predicateString += sourceValue + " ?" + getPredicateSPARQLVariable() + " " + aTargetValue
								+ ". FILTER(?" + getPredicateSPARQLVariable() + "!=" + getPredicateSPARQL() + ")\r\n";
					}
				} else {
					if (isInverseOf) {
						predicateString += aTargetValue + " " + getPredicateSPARQL() + " " + sourceValue + " .\r\n";
					} else {
						predicateString += sourceValue + " " + getPredicateSPARQL() + " " + aTargetValue + " .\r\n";
					}
				}
				if (objectFilterElement != null)
					predicateString += objectFilterElement.toSPARQL(aTargetValue);
			}
		}
		predicateString += getMaxCardinalityString();
		return predicateString;
	}


	/**
	 * Unbound predicate to SPARQL.
	 *
	 * @return the string
	 */
	protected String unboundPredicateToSPARQL() {
		String targetValue = "?n" + getExitIndex();
		String sourceValue;
		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			sourceValue = "?r" +  getEntryIndex();
		} else {
			sourceValue = "?n" + getEntryIndex();
		}

		return boundPredicateToSPARQL(sourceValue, targetValue);
	}

	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, Integer pathIteration, CustomQueryOptions customQueryOptions) {
		if(sourceVariable==null)sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();	
		if (getIsReified()) {
			return pathReifiedPredicatePatternQuery(thing, sourceVariable, targetVariable,this.getReifiedVariable(),pathIteration,customQueryOptions);
		} else {
			return pathPredicatePatternQuery(thing, sourceVariable, targetVariable,pathIteration,customQueryOptions);
		}
	}

	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(thing, sourceVariable, targetVariable,0,customQueryOptions);
	}

	private PathTupleExpr pathReifiedPredicatePatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, Variable reificationVariable,
			Integer pathIteration,CustomQueryOptions customQueryOptions) {
		PathTupleExpr predicatePattern = null;
		
		if(getCardinality(pathIteration)>0) {
			Variable intermediateSourceVariable = null ;
			Variable intermediateTargetVariable = null;
			Variable priorIntermediateTargetVariable = null ;
			Variable intermediateReificationVariable  = null ;
			for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
				if( iteration==1) {
					intermediateSourceVariable = sourceVariable;
				}
				if(iteration<getCardinality(pathIteration)) {	
					 if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = new Variable(targetVariable.getName()+"_i"+iteration);
					priorIntermediateTargetVariable = intermediateTargetVariable;				
				}
				if( iteration==getCardinality(pathIteration)) {
					if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = targetVariable;
					intermediateReificationVariable  = reificationVariable;
				}else {
					intermediateReificationVariable = new Variable(reificationVariable.getName()+"_i"+iteration);
				}
				predicatePattern = pathReifiedPredicatePatternTupleExpr(thing, predicatePattern,intermediateSourceVariable, intermediateTargetVariable, intermediateReificationVariable,customQueryOptions);
			}
			//predicatePattern.getPath().addAll(getPathBindings().get(pathIteration));
			return predicatePattern;
		}else {
			return null;
		}
	}
	private PathTupleExpr pathReifiedPredicatePatternTupleExpr(Thing thing, PathTupleExpr predicatePattern, Variable sourceVariable, Variable targetVariable,Variable reificationVariable,CustomQueryOptions customQueryOptions) {
		ArrayList<Variable> targetVariables = new ArrayList<Variable>();
		if (objectFilterElement != null)
			targetVariables = objectFilterElement.bindTargetVariable(targetVariable);
		if (targetVariables.size() == 0)
			targetVariables.add(getTargetObject());
		TupleExpr reifiedPredicatePattern = null;
		Variable predicateVariable = getPredicateVariable();
		IRI subject = getReifications().getReificationSubject(reification);
		IRI isSubjectOf = getReifications().getReificationIsSubjectOf(reification);
		IRI property = getReifications().getReificationPredicate(reification);
		IRI isPropertyOf = getReifications().getReificationIsPredicateOf(reification);
		IRI object = getReifications().getReificationObject(reification);
		IRI isObjectOf = getReifications().getReificationIsObjectOf(reification);
		Variable subjectVariable = new Variable("subject" + getExitIndex(),subject);
		Variable isSubjectOfVariable = new Variable("isSubjectOf" +  getExitIndex(),isSubjectOf);
		Variable propertyVariable = new Variable("property" +   getExitIndex(),property);
		Variable isPropertyOfVariable = new Variable("isPropertyOf" +   getExitIndex(),isPropertyOf);
		Variable objectVariable = new Variable("object" + getExitIndex(),object);
		Variable isObjectOfVariable = new Variable("isObjectOf" + getExitIndex(),isObjectOf);
		//Part1
		TupleExpr part1Pattern = null;
		if (isInverseOf) {
			if (object != null && isObjectOf != null) {
				StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
						sourceVariable);
				StatementPattern isObjectOfPattern = new StatementPattern(sourceVariable, isObjectOfVariable,
						reificationVariable);
				part1Pattern = new Union(objectPattern, isObjectOfPattern);
			} else if (object != null) {
				StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
						sourceVariable);
				part1Pattern = objectPattern;
			} else if (isObjectOf != null) {
				StatementPattern isObjectOfPattern = new StatementPattern(targetVariable, isObjectOfVariable,
						sourceVariable);
				part1Pattern = isObjectOfPattern;
			} else {
			}
		} else {
			if (subject != null && isSubjectOf != null) {
				StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
						sourceVariable);
				StatementPattern isSubjectOfPattern = new StatementPattern(sourceVariable, isSubjectOfVariable,
						reificationVariable);
				part1Pattern = new Union(subjectPattern, isSubjectOfPattern);
			} else if (subject != null) {
				StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
						sourceVariable);
				part1Pattern = subjectPattern;
			} else if (isSubjectOf != null) {
				StatementPattern isSubjectOfPattern = new StatementPattern(sourceVariable, isSubjectOfVariable,
						reificationVariable);
				part1Pattern = isSubjectOfPattern;
			} else {
			}
		}
		//Part2
		TupleExpr part2Pattern = null;
		if (property != null && isPropertyOf != null) {
			StatementPattern propertyPattern = new StatementPattern(reificationVariable, propertyVariable,
					predicateVariable);
			StatementPattern isPropertyOfPattern = new StatementPattern(predicateVariable, isPropertyOfVariable,
					reificationVariable);
			part2Pattern = new Union(propertyPattern, isPropertyOfPattern);
		} else if (property != null) {
			StatementPattern propertyPattern = new StatementPattern(reificationVariable, propertyVariable,
					predicateVariable);
			part2Pattern = propertyPattern;
		} else if (isPropertyOf != null) {
			StatementPattern isPropertyOfPattern = new StatementPattern(predicateVariable, isPropertyOfVariable,
					reificationVariable);
			part2Pattern = isPropertyOfPattern;
		} else {
		}

		TupleExpr part12Pattern = null;
		if (part1Pattern != null && part2Pattern != null)
			part12Pattern = new Join(part1Pattern, part2Pattern);
		else if (part1Pattern != null)
			part12Pattern = part1Pattern;
		else if (part2Pattern != null)
			part12Pattern = part2Pattern;
		//Part3	
		TupleExpr part3Pattern = null;
		for (Variable aTargetVariable : targetVariables) {
			TupleExpr aTargetPattern = null;
			if (isInverseOf) {
				if (subject != null && isSubjectOf != null) {
					StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
							aTargetVariable);
					StatementPattern isSubjectOfPattern = new StatementPattern(aTargetVariable, isSubjectOfVariable,
							reificationVariable);
					aTargetPattern = new Union(subjectPattern, isSubjectOfPattern);
				} else if (subject != null) {
					StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
							aTargetVariable);
					aTargetPattern = subjectPattern;
				} else if (isSubjectOf != null) {
					StatementPattern isSubjectOfPattern = new StatementPattern(aTargetVariable, isSubjectOfVariable,
							reificationVariable);
					aTargetPattern = isSubjectOfPattern;
				} else {
				}
			} else {
				if (object != null && isObjectOf != null) {
					StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
							aTargetVariable);
					StatementPattern isObjectOfPattern = new StatementPattern(aTargetVariable, isObjectOfVariable,
							reificationVariable);
					aTargetPattern = new Union(objectPattern, isObjectOfPattern);
				} else if (object != null) {
					StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
							aTargetVariable);
					aTargetPattern = objectPattern;
				} else if (isObjectOf != null) {
					StatementPattern isObjectOfPattern = new StatementPattern(aTargetVariable, isObjectOfVariable,
							reificationVariable);
					aTargetPattern = isObjectOfPattern;
				} else {
				}
			}

			if (part3Pattern != null) {
				Join newPart3Pattern = new Join(part3Pattern, aTargetPattern);
				part3Pattern = newPart3Pattern;
			} else {
				part3Pattern = aTargetPattern;
			}

		}

		if (part12Pattern != null && part3Pattern != null)
			reifiedPredicatePattern = new Join(part12Pattern, part3Pattern);
		else if (part12Pattern != null)
			reifiedPredicatePattern = part12Pattern;
		else if (part3Pattern != null)
			reifiedPredicatePattern = part3Pattern;
		
		if (objectFilterElement != null) {
			reifiedPredicatePattern =  objectFilterElement.filterExpression( thing,getTargetObject(),null, reifiedPredicatePattern,customQueryOptions).getTupleExpr();
		}			
		if (statementFilterElement != null) {
			reifiedPredicatePattern =   statementFilterElement.filterExpression(  thing,reificationVariable,null,reifiedPredicatePattern,customQueryOptions).getTupleExpr(); 
		}
		EdgeBinding edge = new EdgeBinding(sourceVariable,getReification(), predicateVariable, targetVariable, getIsInverseOf(), getIsDereified());	
		if(predicatePattern==null) {
			predicatePattern= new PathTupleExpr(reifiedPredicatePattern); 
		}else{
			predicatePattern.setTupleExpr(new Join(predicatePattern.getTupleExpr(), reifiedPredicatePattern));
		}
		predicatePattern.getPath().add(edge);
		return predicatePattern;
	}

	private PathTupleExpr pathPredicatePatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, Integer pathIteration,CustomQueryOptions customQueryOptions) {
		PathTupleExpr predicatePattern = null;
		if(getCardinality(pathIteration)>0) {
			Variable intermediateSourceVariable = null ;
			Variable intermediateTargetVariable = null;
			Variable priorIntermediateTargetVariable = null ;
			for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
				if( iteration==1) {
					intermediateSourceVariable = sourceVariable;
				}
				if(iteration<getCardinality(pathIteration)) {
					
					 if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
					
					intermediateTargetVariable = new Variable(targetVariable.getName()+"_i"+iteration);
					priorIntermediateTargetVariable = intermediateTargetVariable;
					
				}
				if( iteration==getCardinality(pathIteration)) {
					if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = targetVariable;
				}
				predicatePattern = pathPredicatePatternTupleExpr(thing, predicatePattern, intermediateSourceVariable,
						intermediateTargetVariable,customQueryOptions);
			}
		//	predicatePattern.setPath(getPathBindings().get(pathIteration));
			return predicatePattern;
		}else {
			return null;
			//return new SameTerm(sourceVariable,targetVariable);
		}
	}

	private PathTupleExpr pathPredicatePatternTupleExpr(Thing thing, PathTupleExpr predicatePattern,
			Variable intermediateSourceVariable, Variable intermediateTargetVariable,CustomQueryOptions customQueryOptions) {
		TupleExpr intermediatePredicatePattern;
		Variable predicateVariable ;
		if (isNegated) {
			 predicateVariable = new Variable(getPredicateSPARQLVariable());
			//TODO
			 Variable variable = new Variable("p2", predicate);
			if (isInverseOf) {
				StatementPattern inverseOfPattern = new StatementPattern(intermediateTargetVariable, predicateVariable,
						intermediateSourceVariable);
				Compare filterExpression = new Compare(predicateVariable, variable, CompareOp.NE);
				intermediatePredicatePattern = new Filter(inverseOfPattern, filterExpression);
			} else {
				intermediatePredicatePattern = new StatementPattern(intermediateSourceVariable, predicateVariable,
						intermediateTargetVariable);
			}
		} else {
			predicateVariable = new Variable(getPredicateSPARQLVariable(), predicate);

			if (isInverseOf) {
				intermediatePredicatePattern = new StatementPattern(intermediateTargetVariable, predicateVariable,
						intermediateSourceVariable);
			} else {
				intermediatePredicatePattern = new StatementPattern(intermediateSourceVariable, predicateVariable,
						intermediateTargetVariable);
			}
		}
		
		if (objectFilterElement != null) {
			intermediatePredicatePattern = (TupleExpr) objectFilterElement.filterExpression( thing,intermediateTargetVariable,null,intermediatePredicatePattern,customQueryOptions).getTupleExpr();
		}	
		EdgeBinding edge = new EdgeBinding(intermediateSourceVariable, predicateVariable, intermediateTargetVariable, getIsInverseOf());	
		
		if(predicatePattern==null) {
			predicatePattern = new PathTupleExpr(intermediatePredicatePattern);
		}else{
			predicatePattern.setTupleExpr(new Join(predicatePattern.getTupleExpr(), intermediatePredicatePattern));
		}
		predicatePattern.getPath().add(edge);
		return predicatePattern;
	}
	/**
	 * Gets the predicate variable.
	 *
	 * @return the predicate variable
	 */
	private Variable getPredicateVariable() {
		Variable predicateVariable;
		if (getAnyPredicate()) {
			predicateVariable = new Variable(getPredicateSPARQLVariable());
		} else {
			predicateVariable = new Variable(getPredicateSPARQLVariable(), getPredicate());
		}
		return predicateVariable;
	}


	public Boolean getAnyPredicate() {
		if (anyPredicate != null) {
			return anyPredicate;
		} else if (predicate != null) {
			return false;
		} else {
			return true;
		}
	}

	public void setAnyPredicate(Boolean anyPredicate) {
		this.anyPredicate = anyPredicate;
	}

	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);

		setExitIndex(entryIndex+1) ;
		Integer filterBaseIndex;
		if(getIsInverseOf()) {
			filterBaseIndex = entryIndex;
		}else {
			filterBaseIndex = entryIndex+1;
		}
		if(objectFilterElement!=null) objectFilterElement.indexVisitor(filterBaseIndex, 0,edgeCode);
		
		if(statementFilterElement!=null) statementFilterElement.indexVisitor(entryIndex, 0,edgeCode);
		setExitIndex(entryIndex+1) ; 
		
		
		if (edgeCode != null && getIsReified() && isDereified)
			setEdgeCode(EdgeCode.DEREIFIED);
		else
			setEdgeCode(edgeCode);
		return getExitIndex();
	}

	@Override
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		if( getIsReified())
			pathBinding = visitReifiedPredicatePathBinding( pathBinding,pathIteration );
		else
			pathBinding = visitPredicatePathBinding( pathBinding,pathIteration );
		return pathBinding;
	}
	private PathBinding visitReifiedPredicatePathBinding(PathBinding pathBinding, Integer pathIteration) {
		EdgeBinding predicateEdge;
		Variable sourceVariable = this.getSourceVariable();
		Variable targetVariable = this.getTargetVariable();	
		Variable reificationVariable= this.getReifiedVariable();
		
		Variable intermediateSourceVariable = null ;
		Variable intermediateTargetVariable = null;
		Variable priorIntermediateTargetVariable = null ;
		Variable intermediateReificationVariable  = null ;
		for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
			if( iteration==1) {
				intermediateSourceVariable = sourceVariable;
			}
			if(iteration<getCardinality(pathIteration)) {	
				 if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
				intermediateTargetVariable = new Variable(targetVariable.getName()+"_i"+iteration);
				priorIntermediateTargetVariable = intermediateTargetVariable;				
			}
			if( iteration==getCardinality(pathIteration)) {
				if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
				intermediateTargetVariable = targetVariable;
				intermediateReificationVariable  = reificationVariable;
			}else {
				intermediateReificationVariable = new Variable(reificationVariable.getName()+"_i"+iteration);
			}
			predicateEdge = new EdgeBinding( intermediateSourceVariable, this.reification, getPredicateVariable(), intermediateTargetVariable ,getIsInverseOf(),getIsDereified());
			pathBinding.add(predicateEdge);
		}
		return pathBinding;
	}
	private PathBinding visitPredicatePathBinding(PathBinding pathBinding, Integer pathIteration) {
		EdgeBinding predicateEdge;
		Variable sourceVariable = this.getSourceVariable();
		Variable targetVariable = this.getTargetVariable();		
		
		Variable intermediateSourceVariable = null ;
		Variable intermediateTargetVariable = null;
		Variable priorIntermediateTargetVariable = null ;
		for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
			if( iteration==1) {
				intermediateSourceVariable = sourceVariable;
			}
			if(iteration<getCardinality(pathIteration)) {
				
				 if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
				
				intermediateTargetVariable = new Variable(targetVariable.getName()+"_i"+iteration);
				priorIntermediateTargetVariable = intermediateTargetVariable;
				
			}
			if( iteration==getCardinality(pathIteration)) {
				if( iteration>1)intermediateSourceVariable = priorIntermediateTargetVariable;
				intermediateTargetVariable = targetVariable;
			}
			predicateEdge = new EdgeBinding( intermediateSourceVariable,getPredicateVariable(), intermediateTargetVariable,getIsInverseOf());
			pathBinding.add(predicateEdge);
		}
		return pathBinding;
	}
}