/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.Compare;
import org.eclipse.rdf4j.query.algebra.Compare.CompareOp;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.ValueConstant;
import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.PathConstants.FilterOperator;
import pathQLRepository.PathQLRepository;

/**
 * The Class VerbObjectList.
 */
public class VerbObjectList extends FactFilterElement {
	
	/**
	 * Instantiates a new verb object list.
	 *
	 * @param source the source
	 */
	public VerbObjectList(PathQLRepository source) {
		super(source);
		operator = PathConstants.Operator.VERBOBJECTLIST;
	}

	/** The filter operator. */
	FilterOperator filterOperator;
	
	/** The predicate. */
	PredicateElement predicate;
	
	/** The object list. */
	ArrayList<ObjectElement> objectList;

	/**
	 * Gets the filter operator.
	 *
	 * @return the filter operator
	 */
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	/**
	 * Sets the filter operator.
	 *
	 * @param filterOperator the new filter operator
	 */
	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public PredicateElement getPredicate() {
		return predicate;
	}

	/**
	 * Sets the predicate.
	 *
	 * @param predicate the new predicate
	 */
	public void setPredicate(PredicateElement predicate) {
		this.predicate = predicate;
	}

	/**
	 * Gets the object list.
	 *
	 * @return the object list
	 */
	public ArrayList<ObjectElement> getObjectList() {
		return objectList;
	}

	/**
	 * Sets the object list.
	 *
	 * @param objectList the new object list
	 */
	public void setObjectList(ArrayList<ObjectElement> objectList) {
		this.objectList = objectList;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String verbObjectList = "";
		if (filterOperator != null) {
			verbObjectList += PathConstants.getFilterLookup(filterOperator) + " ";

		} else if (predicate != null) {
			verbObjectList += predicate.toString() + " ";
		}
		if (objectList != null) {
			if (objectList.size() == 1) {
				verbObjectList += objectList.get(0).toString();
			} else {
				verbObjectList += "(" + objectList.get(0).toString();
				objectList.get(0).toString();
				for (int objectIndex = 1; objectIndex < objectList.size(); objectIndex++) {
					verbObjectList += " , " + objectList.get(objectIndex).toString() + " ";
				}
				verbObjectList += ")";
			}
		}
		return verbObjectList;

	}

	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		if (predicate != null)
			 predicate.indexVisitor(baseIndex, entryIndex, edgeCode);
		if (objectList != null) {
			for (ObjectElement objectElement : objectList) {
				objectElement.indexVisitor(baseIndex, entryIndex, edgeCode);
				entryIndex++;
			}
			entryIndex--;
		}
		setExitIndex(entryIndex);
		return getExitIndex();
	}

	/**
	 * Bind target value.
	 *
	 * @param targetValue the target value
	 * @return the array list
	 */
	public ArrayList<String> bindTargetValue(String targetValue) {
		ArrayList<String> targetValues = new ArrayList<String>();
		if (filterOperator != null) {
			if (filterOperator.equals(FilterOperator.EQ)) {
				for (ObjectElement objectElement : objectList) {
					switch (objectElement.getOperator()) {
					case IRIREF:
						targetValues.add(objectElement.toSPARQL());
						break;
					case LITERAL:
						targetValues.add(objectElement.toSPARQL());
						break;
					default:
					}
				}
			}
		}
		return targetValues;
	};

	/**
	 * Bind target variable.
	 *
	 * @param targetVariable the target variable
	 * @return the array list
	 */
	public ArrayList<Variable> bindTargetVariable(Variable targetVariable) {
		ArrayList<Variable> targetVariables = new ArrayList<Variable>();
		if (filterOperator != null) {
			if (filterOperator.equals(FilterOperator.EQ)) {
				for (ObjectElement objectElement : objectList) {
					switch (objectElement.getOperator()) {
					case IRIREF:
						targetVariables.add(new Variable(targetVariable.getName(), objectElement.getIri()));
						break;
					case LITERAL:
						targetVariables.add(new Variable(targetVariable.getName(), objectElement.getLiteral()));
						break;
					default:
					}
				}
			}
		}
		return targetVariables;
	};

	/**
	 * To SPARQL.
	 *
	 * @param sourceValue the source value
	 * @return the string
	 */
	public String toSPARQL(String sourceValue) {

		String verbObjectList = "";
		if (filterOperator != null) {
			//if(filterOperator.equals(FilterOperator.EQ)) {
			switch (filterOperator) {
			case EQ:
				switch (objectList.get(0).getOperator()) {
				case PROPERTYLIST:
					verbObjectList += ((FactFilterElement) objectList.get(0)).toSPARQL(sourceValue);
					break;
				case IRIREF:
					verbObjectList += "BIND(<" + objectList.get(0).getIri().stringValue() + "> as " + sourceValue
							+ ")\n";
					break;
				case LITERAL:
					//	verbObjectList +="FILTER("+sourceValue +  " " + PathConstants.getFilterLookup(filterOperator) + " " + objectList.get(0).toSPARQL(indices,edgeCode)+")\n";	
					break;
				default:
					break;
				}
				break;
			case LIKE:			
				verbObjectList += sourceValue + " <http://www.openrdf.org/contrib/lucenesail#matches> ["
				      +"<http://www.openrdf.org/contrib/lucenesail#query> " + objectList.get(0).toSPARQL() +"; "
				      +"<http://www.openrdf.org/contrib/lucenesail#property> ?property_" + getEntryIndex() +";"
				      +"<http://www.openrdf.org/contrib/lucenesail#score> ?score_" + getEntryIndex() +";"
				      +"<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_" + getEntryIndex() +"].";
				break;

			default:
				verbObjectList += "FILTER(" + sourceValue + " " + PathConstants.getFilterLookup(filterOperator) + " "
						+ objectList.get(0).toSPARQL() + ")\n";
				break;
			}
			//}else {
			//	verbObjectList += "FILTER("+sourceValue +  " " + PathConstants.getFilterLookup(filterOperator) + " " + objectList.get(0).toSPARQL()+")\n";		
			//}

		} else if (predicate != null) {
			if (objectList != null) {
				if (objectList.size() == 1) {
					switch (objectList.get(0).getOperator()) {
					case PROPERTYLIST:
						String targetValue = "?n" + getBaseIndex() + "_" + predicate.getExitIndex();
						verbObjectList += sourceValue + " " + predicate.toString() + " " + targetValue + " .\n";//sourceValue+"_0" + " .\n";        			
						verbObjectList += ((FactFilterElement) objectList.get(0)).toSPARQL(targetValue);
						break;
					case IRIREF:
						verbObjectList += predicate.boundPredicateToSPARQL(sourceValue, objectList.get(0).toSPARQL());
						break;
					case LITERAL:
						verbObjectList += predicate.boundPredicateToSPARQL(sourceValue, objectList.get(0).toSPARQL());
						break;
					default:
					}
				} else {
					verbObjectList += "(" + objectList.get(0).toSPARQL();
					objectList.get(0).toString();
					for (int objectIndex = 1; objectIndex < objectList.size(); objectIndex++) {
						verbObjectList += " , " + objectList.get(objectIndex).toSPARQL() + " ";
					}
					verbObjectList += ")";
				}
			}
		}
		return verbObjectList;
	};

	/**
	 * Bound target variable.
	 *
	 * @param predicate the predicate
	 * @return the variable
	 */
	private Variable boundTargetVariable(PredicateElement predicate) {
		//predicate.getExitIndex()
		Variable boundTargetVariable = new Variable("n" + getBaseIndex() + "_" + predicate.getExitIndex());
		return boundTargetVariable;

	}

	/**
	 * Path pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr verbObjectListPattern = null;
		if (filterOperator != null) {
			if (filterOperator.equals(FilterOperator.EQ)) {
				switch (objectList.get(0).getOperator()) {
				case PROPERTYLIST:
					//verbObjectList += ((FactFilterElement)objectList.get(0)).pathPatternQuery(thing);//( sourceValue);
					break;
				case IRIREF:
					sourceVariable.setValue(objectList.get(0).getIri());
					break;
				case LITERAL:
					sourceVariable.setValue(objectList.get(0).getLiteral());
					break;
				default:
				}
			} else {

			}

		} else if (predicate != null) {
			if (objectList != null) {

				if (objectList.size() == 1) {

					Variable boundTargetVariable;
					switch (objectList.get(0).getOperator()) {
					case PROPERTYLIST:
						break;
					case IRIREF:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getIri());
						verbObjectListPattern = predicate.pathPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					case LITERAL:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getLiteral());
						verbObjectListPattern = predicate.pathPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					default:
					}
				} else {

				}
			}
		}
		return verbObjectListPattern;

	};
	public QueryModelNode filterExpression(Thing thing, Variable sourceVariable, Variable targetVariable) {
		QueryModelNode verbObjectListExpression = null;
		if (filterOperator != null) {
			if (filterOperator.equals(FilterOperator.EQ)) {
				switch (objectList.get(0).getOperator()) {
				case PROPERTYLIST:
					//verbObjectList += ((FactFilterElement)objectList.get(0)).pathPatternQuery(thing);//( sourceValue);
					break;
				case IRIREF:
					sourceVariable.setValue(objectList.get(0).getIri());
					break;
				case LITERAL:
					sourceVariable.setValue(objectList.get(0).getLiteral());
					break;
				default:
				}
			} else {
			//	verbObjectListPattern;
				CompareOp compareOperator = PathConstants.compareOperators.get(filterOperator);
				if(compareOperator!=null){
					Compare filterExpression = new Compare(sourceVariable, new ValueConstant(objectList.get(0).getLiteral()), compareOperator);
					return filterExpression;
				}

			}

		} else if (predicate != null) {
			if (objectList != null) {

				if (objectList.size() == 1) {
					Variable boundTargetVariable;
					switch (objectList.get(0).getOperator()) {
					case PROPERTYLIST:
						//objectList.get(0);
						predicate.setObjectFilterElement((FactFilterElement) objectList.get(0));
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getIri());
						verbObjectListExpression = predicate.pathPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					case IRIREF:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getIri());
						verbObjectListExpression = predicate.pathPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					case LITERAL:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getLiteral());
						verbObjectListExpression = predicate.pathPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					default:
					}
				} else {

				}
			}
		}
		return verbObjectListExpression;

	};
	/**
	 * Bound pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	@Override
	public TupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		TupleExpr verbObjectListPattern = null;
		if (filterOperator != null) {
			if (filterOperator.equals(FilterOperator.EQ)) {
				switch (objectList.get(0).getOperator()) {
				case PROPERTYLIST:
					//verbObjectList += ((FactFilterElement)objectList.get(0)).pathPatternQuery(thing);//( sourceValue);
					break;
				case IRIREF:
					sourceVariable.setValue(objectList.get(0).getIri());
					break;
				case LITERAL:
					sourceVariable.setValue(objectList.get(0).getLiteral());
					break;
				default:
				}
			} else {
				//verbObjectList += "FILTER("+sourceVariable +  " " + PathConstants.getFilterLookup(filterOperator) + " " + objectList.get(0).toSPARQL()+")\n";		
			}

		} else if (predicate != null) {
			if (objectList != null) {

				if (objectList.size() == 1) {

					Variable boundTargetVariable;
					switch (objectList.get(0).getOperator()) {
					case PROPERTYLIST:
						break;
					case IRIREF:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getIri());
						verbObjectListPattern = predicate.boundPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					case LITERAL:
						boundTargetVariable = boundTargetVariable(predicate);
						boundTargetVariable.setValue(objectList.get(0).getLiteral());
						verbObjectListPattern = predicate.boundPatternQuery(thing, sourceVariable, boundTargetVariable);
						break;
					default:
					}
				} else {

				}
			}
		}
		return verbObjectListPattern;
	};
}
