/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class FactFilterElement.
 */
public class FactFilterElement extends ObjectElement{


	/** The property list not empty. */
	private ArrayList<VerbObjectList>  propertyListNotEmpty;
	
	/**
	 * Instantiates a new fact filter element.
	 */
	public FactFilterElement(PathQLRepository source) {
		super(source);
		operator = PathConstants.Operator.PROPERTYLIST;
	}


	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String filterElement="[";
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				filterElement += verbObjectList.toString() +" ;";			
			}	
		}
		return filterElement.substring(0,filterElement.length()-1)+ "]";
	}

	/**
	 * To SPARQL.
	 *
	 * @param sourceValue the source value
	 * @return the string
	 */
	public String toSPARQL( String sourceValue) {
		String filterElement="";
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				filterElement += verbObjectList.toSPARQL( sourceValue);			
			}	
		}
		return filterElement;
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
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.pathPatternQuery( thing,sourceVariable,targetVariable);		
				if(factFilterPattern == null) 
					factFilterPattern = verbObjectListPattern;
				else if(verbObjectListPattern == null){
					
				}else {
					TupleExpr factFilterPattern1 = factFilterPattern;
					factFilterPattern = new Join (factFilterPattern1,verbObjectListPattern  );			
				}
			}	
		}

		return factFilterPattern;
	}
	
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
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.boundPatternQuery( thing,sourceVariable,targetVariable);		
				if(factFilterPattern == null) 
					factFilterPattern = verbObjectListPattern;
				else if(verbObjectListPattern == null){
					
				}else {
					TupleExpr factFilterPattern1 = factFilterPattern;
					factFilterPattern = new Join (factFilterPattern1,verbObjectListPattern  );			
				}
			}	
		}

		return factFilterPattern;
	}
	
	/**
	 * Gets the property list not empty.
	 *
	 * @return the property list not empty
	 */
	public ArrayList<VerbObjectList> getPropertyListNotEmpty() {
		return propertyListNotEmpty;
	}

	/**
	 * Sets the property list not empty.
	 *
	 * @param propertyListNotEmpty the new property list not empty
	 */
	public void setPropertyListNotEmpty(ArrayList<VerbObjectList> propertyListNotEmpty) {
		this.propertyListNotEmpty = propertyListNotEmpty;
	}


	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public
	String toHTML() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public String toSPARQL() {

		return toSPARQL( "?n"+ getEntryIndex() ) ; //getIndex() ) ;
	}


	/**
	 * Bind target value.
	 *
	 * @param targetValue the target value
	 * @return the array list
	 */
	public ArrayList<String> bindTargetValue( String targetValue) {
		ArrayList<String>  targetValues = new ArrayList<String> ();
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				targetValues.addAll( verbObjectList.bindTargetValue(targetValue));			
			}	
		}
		return targetValues;
	}


	/**
	 * Bind target variable.
	 *
	 * @param targetVariable the target variable
	 * @return the array list
	 */
	public ArrayList<Variable> bindTargetVariable(Variable targetVariable) {
		ArrayList<Variable>  targetVariables = new ArrayList<Variable> ();
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				targetVariables.addAll( verbObjectList.bindTargetVariable(targetVariable));			
			}	
		}
		return targetVariables;
	};

	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
	public Integer indexVisitor(Integer baseIndex,Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		Integer exitIndex=null;
		if(propertyListNotEmpty!=null) {
			for(   VerbObjectList verbObjectList  : propertyListNotEmpty) {
				exitIndex = verbObjectList.indexVisitor(baseIndex, entryIndex,edgeCode) ;	
			}			
		}
		setExitIndex(exitIndex);
		return getExitIndex();
	}
}
