/*
 * inova8 2020
 */
package com.inova8.pathql.processor;

import static org.eclipse.rdf4j.model.util.Values.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.constants.PathConstants;
import com.inova8.intelligentgraph.constants.PathConstants.FilterOperator;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.FactFilterElement;
import com.inova8.pathql.element.ObjectElement;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.element.VerbObjectList;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class PathParser.
 */
@Deprecated
public class PathParser {

/** The thing. */
Thing thing;

	/**
	 * Instantiates a new path parser.
	 *
	 * @param thing the thing
	 */
	public PathParser(Thing thing) {
		this.thing = thing;
	}

	/**
	 * Parses the path.
	 *
	 * @param path the path
	 * @return the predicate element
	 */
	public PredicateElement parsePath(String path) {
		String pathAlternatives[] = path.split("[|]");
		PredicateElement pathElement = null;
		for(String pathAlternative: pathAlternatives) { 
			pathElement  = parsePathAlternative(pathAlternative);
		}
		pathElement.toString();
		return pathElement;	
	}

	/**
	 * Parses the path alternative.
	 *
	 * @param pathAlternative the path alternative
	 * @return the predicate element
	 */
	private PredicateElement parsePathAlternative (String pathAlternative) {
		String pathSequences[] = pathAlternative.split("[/]");
		PredicateElement firstPathSequenceElement = parsePathSequence(pathSequences[0]);
		if(pathSequences.length>1 ) {
			PredicateElement rootPathSequenceElement = new PredicateElement(thing.getSource());
			rootPathSequenceElement.setOperator(PathConstants.Operator.SEQUENCE);
			rootPathSequenceElement.setLeftPathElement(firstPathSequenceElement);
			PredicateElement nestedPathSequenceElement = rootPathSequenceElement;
			for(int i = 1; i< pathSequences.length-1; i++) { 
				PredicateElement newPathSequenceElement = new PredicateElement(thing.getSource());
				newPathSequenceElement.setOperator(PathConstants.Operator.SEQUENCE);
				nestedPathSequenceElement.setLeftPathElement(parsePathSequence(pathSequences[i]));
				nestedPathSequenceElement.setLeftPathElement(newPathSequenceElement);
				nestedPathSequenceElement = newPathSequenceElement;
			}
			nestedPathSequenceElement.setRightPathElement(parsePathSequence(pathSequences[pathSequences.length-1]));
			return rootPathSequenceElement;			
		}else {
			return firstPathSequenceElement;			
		}	
	}

	/**
	 * Parses the path sequence.
	 *
	 * @param pathSequence the path sequence
	 * @return the predicate element
	 */
	private PredicateElement parsePathSequence(String pathSequence) {
		String pathEltOrInverse = pathSequence.trim();	
		return parsePathEltOrInverse(pathEltOrInverse);	
	}
	
	/**
	 * Parses the path elt or inverse.
	 *
	 * @param predicatePath the predicate path
	 * @return the predicate element
	 */
	private PredicateElement parsePathEltOrInverse(String predicatePath) {
		PredicateElement pathElement = new PredicateElement(thing.getSource());	
		//pathElement.toString();
		pathElement.setOperator(PathConstants.Operator.PREDICATE);
		if(predicatePath.startsWith("^")) {
			pathElement.setIsInverseOf(true);
			predicatePath = predicatePath.substring(1);
		}
		String[] predicatePathParts = predicatePath.split("[@]");
		if(predicatePathParts.length>1) {
			pathElement.setIsReified(true);
			String[]  statementParts = predicatePathParts[1].split("[#]");
			if(  predicatePathParts[1].contains("#")) {
				pathElement.setIsDereified(true);
				//Only if filter AFTER #
				if(statementParts.length>1) {
					String[] StatementFilterParts = statementParts[1].split("[\\[\\]]");
					if( StatementFilterParts.length>1) {
						pathElement.setStatementFilterElement(  parseFilter(statementParts[1].substring(1,statementParts[1].length()-1)));
					}

				}
			}
			pathElement.setReification(thing.convertQName(predicatePathParts[0]));
			String[] objectFilterParts = statementParts[0].split("[\\[\\]]");
			pathElement.setPredicate(thing.convertQName(objectFilterParts[0]));
			if(objectFilterParts.length>1)pathElement.setObjectFilterElement(parseFilter(objectFilterParts[1]));
		}else {
			String[] objectFilterParts = predicatePath.split("[\\[\\]]");
			pathElement.setPredicate(thing.convertQName(objectFilterParts[0])); 
			if(objectFilterParts.length>1)pathElement.setObjectFilterElement(parseFilter(objectFilterParts[1]));
		}
		return pathElement;
	}
	
	/**
	 * Parses the filter.
	 *
	 * @param filter the filter
	 * @return the fact filter element
	 */
	private FactFilterElement  parseFilter(String filter) {
		filter = filter.trim();
		return parseFactFilterPattern(filter);	
	}
	
	/**
	 * Parses the fact filter pattern.
	 *
	 * @param factFilterPattern the fact filter pattern
	 * @return the fact filter element
	 */
	private FactFilterElement parseFactFilterPattern(String factFilterPattern) {
		FactFilterElement pathElement = new FactFilterElement(thing.getSource());		

		String[] factFilterPatternParts = factFilterPattern.trim().split("\\s+");
		if(factFilterPatternParts[0].matches("^.*?(lt|gt|le|ge|eq|ne).*$")) {	
			pathElement.setOperator(PathConstants.Operator.FILTER);
			
			parsePropertyListNotEmpty(pathElement, factFilterPatternParts);
			
		//	pathElement.setFilterOperator(PathConstants.filterOperators.get(factFilterPatternParts[0]));
		//	pathElement.setFilterOperand(parseFilterOperand(factFilterPatternParts[1]));
		}else{
			pathElement.setOperator(PathConstants.Operator.PREDICATEOBJECT);
			
			parsePropertyListNotEmpty(pathElement, factFilterPatternParts);
			
			
			
			
			
		//	pathElement.setFilterPredicate(thing.convertQName(factFilterPatternParts[0]));
		//	pathElement.setFilterOperand(parseFilterOperand(factFilterPatternParts[1]));			
		};
		return pathElement	;
	}

	/**
	 * Parses the property list not empty.
	 *
	 * @param pathElement the path element
	 * @param factFilterPatternParts the fact filter pattern parts
	 */
	private void parsePropertyListNotEmpty(FactFilterElement pathElement, String[] factFilterPatternParts) {
		FilterOperator filterOperator = PathConstants.filterOperators.get(factFilterPatternParts[0]);
		IRI filterPredicate = thing.convertQName(factFilterPatternParts[0]);
		Value filterOperand = parseFilterOperand(factFilterPatternParts[1]);
		ArrayList<VerbObjectList> propertyListNotEmpty = new ArrayList<VerbObjectList>();
		VerbObjectList verbObjectList = new VerbObjectList(thing.getSource());
		verbObjectList.setFilterOperator(filterOperator);
		PredicateElement filterPredicateElement = new PredicateElement(thing.getSource());
		filterPredicateElement.setPredicate(filterPredicate);
		verbObjectList.setPredicate(filterPredicateElement);
		ArrayList<ObjectElement> objectList = new  ArrayList<ObjectElement>();
		ObjectElement object = new ObjectElement(thing.getSource());
		if(filterOperand.isLiteral()) {
			object.setLiteral((Literal)filterOperand);
		}else {
			object.setIri((IRI)filterOperand);
		}
		objectList.add(object);
		verbObjectList.setObjectList(objectList);	
		propertyListNotEmpty.add(verbObjectList);
		pathElement.setPropertyListNotEmpty(propertyListNotEmpty);
	}
	
	/**
	 * Parses the filter operand.
	 *
	 * @param value the value
	 * @return the org.eclipse.rdf 4 j.model. value
	 */
	private  org.eclipse.rdf4j.model.Value parseFilterOperand(String value){
		org.eclipse.rdf4j.model.Value operand = thing.convertQName(value); 
		if(operand==null) {
			if(value.startsWith("\"") || value.startsWith("'"))value= value.substring(1,value.length()-1);
			operand = literal( value);
		}
		return  operand;
		
	}
}
