/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.And;
import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.ValueExpr;

import path.PathTupleExpr;
import pathCalc.CustomQueryOptions;
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
	 *
	 * @param source the source
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
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {		
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.pathPatternQuery( thing,sourceVariable,targetVariable,customQueryOptions).getTupleExpr();		
				if(factFilterPattern == null) 
					factFilterPattern = verbObjectListPattern;
				else if(verbObjectListPattern == null){
					
				}else {
					TupleExpr factFilterPattern1 = factFilterPattern;
					factFilterPattern = new Join (factFilterPattern1,verbObjectListPattern  );			
				}
			}	
		}

		return new PathTupleExpr(factFilterPattern);
	}
	public PathTupleExpr filterExpression(Thing thing, Variable sourceVariable, Variable targetVariable,TupleExpr filterExpression,CustomQueryOptions customQueryOptions) {		
		//QueryModelNode filterExpression = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				QueryModelNode verbObjectListExpression = verbObjectList.filterExpression( thing,sourceVariable,targetVariable,customQueryOptions);		
				if(filterExpression == null) 
					filterExpression = (TupleExpr) verbObjectListExpression;
				else if(verbObjectListExpression == null){
					//Ignore it for some unknown reason
				}else {
					if(filterExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.Compare") )	
						if(verbObjectListExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.Compare"))
							filterExpression = (TupleExpr) new And((ValueExpr) filterExpression,(ValueExpr) verbObjectListExpression  );	
						else
							filterExpression = new Filter( (TupleExpr) verbObjectListExpression ,(ValueExpr) filterExpression );	
					else
						if(verbObjectListExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.StatementPattern"))
							filterExpression = new Join(  (TupleExpr)filterExpression,(TupleExpr) verbObjectListExpression  );	
						else if(verbObjectListExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.Join"))
							filterExpression = new Join(  (TupleExpr)filterExpression,(TupleExpr) verbObjectListExpression  );	
						else if(verbObjectListExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.Compare"))
							filterExpression =  new Filter(  filterExpression,  (ValueExpr) verbObjectListExpression );	
						else if(verbObjectListExpression.getClass().getName().equals("org.eclipse.rdf4j.query.algebra.Filter")) {
							TupleExpr arg = ((Filter)verbObjectListExpression).getArg() ;
							filterExpression =  new Filter(   new Join(   filterExpression, arg),  ((Filter)verbObjectListExpression).getCondition() );	
						}
						else
							filterExpression = new Filter(  (TupleExpr) filterExpression  ,  (ValueExpr) verbObjectListExpression );
				}
			}	
		}

		return new PathTupleExpr(filterExpression);
	}
	
	/**
	 * Bound pattern query.
	 *
	 * @param thing the thing
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @return the tuple expr
	 */
	public PathTupleExpr boundPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {		
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.boundPatternQuery( thing,sourceVariable,targetVariable).getTupleExpr();		
				if(factFilterPattern == null) 
					factFilterPattern = verbObjectListPattern;
				else if(verbObjectListPattern == null){
					
				}else {
					TupleExpr factFilterPattern1 = factFilterPattern;
					factFilterPattern = new Join (factFilterPattern1,verbObjectListPattern  );			
				}
			}	
		}

		return new PathTupleExpr(factFilterPattern);
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

		return toSPARQL( "?n"+ getEntryIndex() ) ;
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
