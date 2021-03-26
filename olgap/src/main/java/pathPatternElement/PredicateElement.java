package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.algebra.Compare;
import org.eclipse.rdf4j.query.algebra.Compare.CompareOp;

import pathCalc.Thing;

import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.Union;
import org.eclipse.rdf4j.query.algebra.Var;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

public class PredicateElement extends PathElement {
	private Boolean isInverseOf = false;
	private Boolean isReified = false;
	private Boolean isDereified = false;
	private Boolean isNegated = false;
	private IRI predicate;
	private Boolean anyPredicate;
	private IRI reification;

	private FactFilterElement objectFilterElement;
	private FactFilterElement statementFilterElement;
	private Variable targetObject = new Variable();

	public PredicateElement(Boolean isInverseOf, Boolean isReified, IRI predicate, IRI reification) {
		super();
		operator = PathConstants.Operator.PREDICATE;
		this.isInverseOf = isInverseOf;
		this.isReified = isReified;
		this.predicate = predicate;
		this.reification = reification;

	}

	public PredicateElement() {
		super();
		operator = PathConstants.Operator.PREDICATE;
	}

	public PredicateElement(IRI predicate) {
		super();
		operator = PathConstants.Operator.PREDICATE;
		setPredicate(predicate);
	}

	public Boolean getIsInverseOf() {
		return isInverseOf;
	}

	public void setIsInverseOf(Boolean isInverseOf) {
		this.isInverseOf = isInverseOf;
	}

	public Boolean getIsReified() {
		return isReified;
	}

	public void setIsReified(Boolean isReified) {
		this.isReified = isReified;
	}

	public IRI getPredicate() {
		return predicate;
	}

	public String getPredicateSPARQL() {
		if (getAnyPredicate()) {
			return getPredicateSPARQLVariable();
		} else {
			return "<" + predicate.stringValue() + ">";
		}

	}

	public String getPredicateSPARQLVariable() {
	//	return "?p" + getIndex() + "_" + (getIndex()+1);
		if(getBaseIndex() == null)
			return "?p" + getEntryIndex() + "_" + getExitIndex();
		else 
			return "?p" + getBaseIndex() + "_" + getEntryIndex() + "_" + getExitIndex();
//		else
//			return "?p" + getIndex();
	}

	public void setPredicate(IRI predicate) {
		this.predicate = predicate;
	}

	public IRI getReification() {
		return reification;
	}

	public void setReification(IRI reification) {
		this.reification = reification;
	}
	private Variable getReifiedVariable() {
		String reificationValue = "?r" + getExitIndex();//(getIndex() + 1);
		Variable reificationVariable = new Variable(reificationValue);
		return reificationVariable;
	}
	public Variable getTargetVariable() {
		if(getRightPathElement()!=null)
			return getRightPathElement().getTargetVariable();
		else
		{
			if (getIsDereified()) {
				return (Variable) getReifiedVariable();
			}else {
				targetVariable.setName("?n"+ getExitIndex());
				return targetVariable;
			}
		}
	}
	public Boolean getIsDereified() {
		return isDereified;
	}

	public void setIsDereified(Boolean isDereified) {
		this.isDereified = isDereified;
	}

	public Boolean getIsNegated() {
		return isNegated;
	}

	public void setIsNegated(Boolean isDereified) {
		this.isNegated = isDereified;
	}

	public FactFilterElement getObjectFilterElement() {
		return objectFilterElement;
	}

	public void setObjectFilterElement(FactFilterElement objectFilterElement) {
		this.objectFilterElement = objectFilterElement;
	}

	public FactFilterElement getStatementFilterElement() {
		return statementFilterElement;
	}

	public void setStatementFilterElement(FactFilterElement statementFilterElement) {
		this.statementFilterElement = statementFilterElement;
	}

	public String toHTML() {

		String predicateString = "";
		if (isNegated)
			predicateString += "!";
		if (isInverseOf)
			predicateString += "^";
		if (isReified) {
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
		if (isReified) {
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
		targetObject.setName("?n" + getExitIndex());
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

		if (isReified) {
			String reificationValue = "?r" + getExitIndex(); //(getIndex() + 1);
			IRI subject = PathQLRepository.getReificationSubject(reification);
			IRI isSubjectOf = PathQLRepository.getReificationIsSubjectOf(reification);
			IRI property = PathQLRepository.getReificationPredicate(reification);
			IRI isPropertyOf = PathQLRepository.getReificationIsPredicateOf(reification);
			IRI object = PathQLRepository.getReificationObject(reification);
			IRI isObjectOf = PathQLRepository.getReificationIsObjectOf(reification);

			if (isInverseOf) {
				// ?reification :reificationObject  :thing .  :thing   :reificationIsObjectOf  ?reification .
				if (object != null && isObjectOf != null) {
					predicateString += "{{" + reificationValue + " <" + object.stringValue() + "> " + sourceValue;
					predicateString += " }UNION{ ";
					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
							+ " }}\n";
				} else if (object != null) {
					predicateString += reificationValue + " <" + object.stringValue() + "> " + sourceValue + "\n";
				} else if (isObjectOf != null) {
					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue + "\n";
				} else {
				}
			} else {
				// ?reification :reificationSubject  :thing .  :thing   :reificationIsSubjectOf  ?reification .
				if (subject != null && isSubjectOf != null) {
					predicateString += "{{" + reificationValue + " <" + subject.stringValue() + "> " + sourceValue;
					predicateString += " }UNION{ ";
					predicateString += sourceValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
							+ " }}\n";
				} else if (subject != null) {
					predicateString += reificationValue + " <" + subject.stringValue() + "> " + sourceValue + "\n";
				} else if (isSubjectOf != null) {
					predicateString += sourceValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue + "\n";
				} else {
				}
			}
			// ?reification :reificationPredicate :predicate . :predicate   :reificationIsPredicateOf ?reification .
			if (property != null && isPropertyOf != null) {
				predicateString += "{{" + reificationValue + " <" + property.stringValue() + "> "
						+ getPredicateSPARQL();
				predicateString += " }UNION{ ";
				predicateString += getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue
						+ " }}\n";
			} else if (property != null) {
				predicateString += reificationValue + " <" + property.stringValue() + "> " + getPredicateSPARQL()
						+ "\n";
			} else if (isPropertyOf != null) {
				predicateString += getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue
						+ "\n";
			} else {
			}
			for (String aTargetValue : targetValues) {
				if (isInverseOf) {
					//?reification :reificationSubject ?reifiedValue .  ?reifiedValue   :reificationIsSubjectOf  ?reification .
					if (subject != null && isSubjectOf != null) {
						predicateString += "{{" + reificationValue + " <" + subject.stringValue() + "> " + aTargetValue;
						predicateString += " }UNION{ ";
						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
								+ " }}\n";
					} else if (subject != null) {
						predicateString += reificationValue + " <" + subject.stringValue() + "> " + aTargetValue + "\n";
					} else if (isSubjectOf != null) {
						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue
								+ "\n";
					} else {
					}
				} else {
					//?reification :reificationObject ?reifiedValue .  ?reifiedValue   :reificationIsObjectOf  ?reification .
					if (object != null && isObjectOf != null) {
						predicateString += "{{" + reificationValue + " <" + object.stringValue() + "> " + aTargetValue;
						predicateString += " }UNION{ ";
						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
								+ " }}\n";
					} else if (object != null) {
						predicateString += reificationValue + " <" + object.stringValue() + "> " + aTargetValue + "\n";
					} else if (isObjectOf != null) {
						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue
								+ "\n";
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
						predicateString += aTargetValue + getPredicateSPARQLVariable() + " " + sourceValue + ". FILTER("
								+ getPredicateSPARQLVariable() + "!=" + getPredicateSPARQL() + ").\n";
					} else {
						predicateString += sourceValue + " " + getPredicateSPARQLVariable() + " " + aTargetValue
								+ ". FILTER(" + getPredicateSPARQLVariable() + "!=" + getPredicateSPARQL() + ")\n";
					}
				} else {
					if (isInverseOf) {
						predicateString += aTargetValue + " " + getPredicateSPARQL() + " " + sourceValue + " .\n";
					} else {
						predicateString += sourceValue + " " + getPredicateSPARQL() + " " + aTargetValue + " .\n";
					}
				}
				if (objectFilterElement != null)
					predicateString += objectFilterElement.toSPARQL(aTargetValue);
			}
		}
		predicateString += getMaxCardinalityString();
		return predicateString;
	}

	protected String unboundPredicateToSPARQL() {
		String targetValue = "?n" + getExitIndex();// (getIndex() + 1);
		String sourceValue;
		if (getEdgeCode() != null && getEdgeCode().equals(PathConstants.EdgeCode.DEREIFIED)) {
			sourceValue = "?r" +  getEntryIndex();// (getIndex());
		} else {
			sourceValue = "?n" + getEntryIndex();// (getIndex());
		}

		return boundPredicateToSPARQL(sourceValue, targetValue);
	}
	@Deprecated
	public TupleExpr unboundPathPatternQuery(Thing thing) {
		return pathPatternQuery(thing, this.sourceVariable,this.targetVariable);
	}

	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		if(sourceVariable==null)sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();
		 
		ArrayList<Variable> targetVariables = new ArrayList<Variable>();
		if (objectFilterElement != null)
			targetVariables = objectFilterElement.bindTargetVariable(targetVariable);
		if (targetVariables.size() == 0)
			targetVariables.add(getTargetObject());
		if (isReified) {
			TupleExpr reifiedPredicatePattern = null;
			Variable reificationVariable = getReifiedVariable();
			Variable predicateVariable;
			if (getAnyPredicate()) {
				predicateVariable = new Variable(getPredicateSPARQLVariable());
			} else {
				predicateVariable = new Variable(getPredicateSPARQLVariable(), getPredicate());
			}
			IRI subject = PathQLRepository.getReificationSubject(reification);
			IRI isSubjectOf = PathQLRepository.getReificationIsSubjectOf(reification);
			IRI property = PathQLRepository.getReificationPredicate(reification);
			IRI isPropertyOf = PathQLRepository.getReificationIsPredicateOf(reification);
			IRI object = PathQLRepository.getReificationObject(reification);
			IRI isObjectOf = PathQLRepository.getReificationIsObjectOf(reification);
			Var subjectVariable = new Var("?subject" + getExitIndex(),subject);//(getIndex() + 1), subject);
			Var isSubjectOfVariable = new Var("?isSubjectOf" +  getExitIndex(),isSubjectOf);// (getIndex() + 1), isSubjectOf);
			Var propertyVariable = new Var("?property" +   getExitIndex(),property);//  (getIndex() + 1), property);
			Var isPropertyOfVariable = new Var("?isPropertyOf" +   getExitIndex(),isPropertyOf);//  (getIndex() + 1), isPropertyOf);
			Var objectVariable = new Var("?object" + getExitIndex(),object);// (getIndex() + 1), object);
			Var isObjectOfVariable = new Var("?isObjectOf" + getExitIndex(),isObjectOf);// (getIndex() + 1), isObjectOf);
			//Part1
			TupleExpr part1Pattern = null;
			if (isInverseOf) {
				if (object != null && isObjectOf != null) {
					//					predicateString += "{{"+reificationValue  + " <" + object.stringValue() + "> "  + sourceValue;
					//					predicateString += " }UNION{ ";
					//					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue + " }}\n";
					StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
							sourceVariable);
					StatementPattern isObjectOfPattern = new StatementPattern(sourceVariable, isObjectOfVariable,
							reificationVariable);
					part1Pattern = new Union(objectPattern, isObjectOfPattern);
				} else if (object != null) {
					//					predicateString += reificationValue  + " <" + object.stringValue() + "> "  + sourceValue+ "\n";
					StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
							sourceVariable);
					part1Pattern = objectPattern;
				} else if (isObjectOf != null) {
					//					predicateString += sourceValue + " <" + isObjectOf.stringValue() + "> " + reificationValue + "\n";
					StatementPattern isObjectOfPattern = new StatementPattern(targetVariable, isObjectOfVariable,
							sourceVariable);
					part1Pattern = isObjectOfPattern;
				} else {
				}
			} else {
				if (subject != null && isSubjectOf != null) {
					//					predicateString += "{{"+reificationValue + " <" + subject.stringValue() + "> "  + sourceValue;
					//					predicateString += " }UNION{ ";
					//					predicateString += sourceValue + " <" + isSubjectOf.stringValue()+ "> " + reificationValue + " }}\n";
					StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
							sourceVariable);
					StatementPattern isSubjectOfPattern = new StatementPattern(sourceVariable, isSubjectOfVariable,
							reificationVariable);
					part1Pattern = new Union(subjectPattern, isSubjectOfPattern);
				} else if (subject != null) {
					//					predicateString += reificationValue  + " <" + subject.stringValue() + "> "  + sourceValue+ "\n";
					StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
							sourceVariable);
					part1Pattern = subjectPattern;
				} else if (isSubjectOf != null) {
					//					predicateString += sourceValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue + "\n";
					StatementPattern isSubjectOfPattern = new StatementPattern(sourceVariable, isSubjectOfVariable,
							reificationVariable);
					part1Pattern = isSubjectOfPattern;
				} else {
				}
			}
			//Part2
			TupleExpr part2Pattern = null;
			if (property != null && isPropertyOf != null) {
				//				predicateString += "{{"+ reificationValue + " <" + property.stringValue()+ "> "  + getPredicateSPARQL();
				//				predicateString += " }UNION{ ";
				//				predicateString += getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue	+ " }}\n";
				StatementPattern propertyPattern = new StatementPattern(reificationVariable, propertyVariable,
						predicateVariable);
				StatementPattern isPropertyOfPattern = new StatementPattern(predicateVariable, isPropertyOfVariable,
						reificationVariable);
				part2Pattern = new Union(propertyPattern, isPropertyOfPattern);
			} else if (property != null) {
				//				predicateString += reificationValue + " <" + property.stringValue()+ "> "  + getPredicateSPARQL()+ "\n";
				StatementPattern propertyPattern = new StatementPattern(reificationVariable, propertyVariable,
						predicateVariable);
				part2Pattern = propertyPattern;
			} else if (isPropertyOf != null) {
				//				predicateString +=  getPredicateSPARQL() + " <" + isPropertyOf.stringValue() + "> " + reificationValue	+ "\n";
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
			for (Var aTargetVariable : targetVariables) {
				TupleExpr aTargetPattern = null;
				if (isInverseOf) {
					if (subject != null && isSubjectOf != null) {
						//						predicateString += "{{"+reificationValue + " <" + subject.stringValue() + "> " + aTargetValue ;
						//						predicateString += " }UNION{ ";
						//						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue+ " }}\n";
						StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
								aTargetVariable);
						StatementPattern isSubjectOfPattern = new StatementPattern(aTargetVariable, isSubjectOfVariable,
								reificationVariable);
						aTargetPattern = new Union(subjectPattern, isSubjectOfPattern);
					} else if (subject != null) {
						//						predicateString +=  reificationValue + " <" +subject.stringValue()+ "> " + aTargetValue + "\n";
						StatementPattern subjectPattern = new StatementPattern(reificationVariable, subjectVariable,
								aTargetVariable);
						aTargetPattern = subjectPattern;
					} else if (isSubjectOf != null) {
						//						predicateString += aTargetValue + " <" + isSubjectOf.stringValue() + "> " + reificationValue+ "\n";
						StatementPattern isSubjectOfPattern = new StatementPattern(aTargetVariable, isSubjectOfVariable,
								reificationVariable);
						aTargetPattern = isSubjectOfPattern;
					} else {
					}
				} else {
					if (object != null && isObjectOf != null) {
						//						predicateString += "{{"+reificationValue + " <" + object.stringValue() + "> " + aTargetValue ;
						//						predicateString += " }UNION{ ";
						//						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue+ " }}\n";
						StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
								aTargetVariable);
						StatementPattern isObjectOfPattern = new StatementPattern(aTargetVariable, isObjectOfVariable,
								reificationVariable);
						aTargetPattern = new Union(objectPattern, isObjectOfPattern);
					} else if (object != null) {
						//						predicateString += reificationValue + " <" +object.stringValue()+ "> " + aTargetValue + "\n";
						StatementPattern objectPattern = new StatementPattern(reificationVariable, objectVariable,
								aTargetVariable);
						aTargetPattern = objectPattern;
					} else if (isObjectOf != null) {
						//						predicateString += aTargetValue + " <" + isObjectOf.stringValue() + "> " + reificationValue+ "\n";
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
				TupleExpr objectFilterPattern = objectFilterElement.pathPatternQuery( thing,getTargetObject(),null); 
				if(objectFilterPattern!=null) {
					Join newObjectFilterPattern = new Join(objectFilterPattern, reifiedPredicatePattern);
					reifiedPredicatePattern = newObjectFilterPattern;
				}
			}			
			if (statementFilterElement != null) {
				TupleExpr statementFilterPattern =  statementFilterElement.pathPatternQuery(  thing,reificationVariable,null); 
				if(statementFilterPattern!=null) {
					Join newStatementFilterPattern = new Join(statementFilterPattern, reifiedPredicatePattern);
					reifiedPredicatePattern = newStatementFilterPattern;
				}
			}			
			return reifiedPredicatePattern;
		} else {
			TupleExpr predicatePattern = null;
			if (isNegated) {
				Var predicateVariable = new Var(getPredicateSPARQLVariable());
				//TODO
				Var variable = new Var("p2", predicate);
				if (isInverseOf) {
					StatementPattern inverseOfPattern = new StatementPattern(targetVariable, predicateVariable,
							sourceVariable);
					Compare filterExpression = new Compare(predicateVariable, variable, CompareOp.NE);
					predicatePattern = new Filter(inverseOfPattern, filterExpression);
				} else {
					predicatePattern = new StatementPattern(sourceVariable, predicateVariable,
							targetVariable);
				}
			} else {
				Var predicateVariable = new Var(getPredicateSPARQLVariable(), predicate);

				if (isInverseOf) {
					predicatePattern = new StatementPattern(targetVariable, predicateVariable,
							sourceVariable);
				} else {
					predicatePattern = new StatementPattern(sourceVariable, predicateVariable,
							targetVariable);
				}
			}
			
			if (objectFilterElement != null) {
				TupleExpr objectFilterPattern = objectFilterElement.pathPatternQuery( thing,getTargetObject(),null); //, aTargetValue
				if(objectFilterPattern!=null) {
					Join newObjectFilterPattern = new Join(objectFilterPattern, predicatePattern);
					predicatePattern = newObjectFilterPattern;
				}
			}	

			return predicatePattern;
		}
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
		
		if (edgeCode != null && isReified && isDereified)
			setEdgeCode(EdgeCode.DEREIFIED);
		else
			setEdgeCode(edgeCode);
		return getExitIndex();
	}

}