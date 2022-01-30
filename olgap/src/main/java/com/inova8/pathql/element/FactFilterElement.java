/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.And;
import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.ValueExpr;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class FactFilterElement.
 */
public class FactFilterElement extends ObjectElement{


	/** The property list not empty. */
	private ArrayList<VerbObjectList>  propertyListNotEmpty;
	
	/**
	 * Instantiates a new fact filter element.
	 *
	 * @param repositoryContext the repository context
	 */
	public FactFilterElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {		
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.pathPatternQuery( sourceVariable,predicateVariable,targetVariable,customQueryOptions).getTupleExpr();		
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
	 * Filter expression.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param filterExpression the filter expression
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	public PathTupleExpr filterExpression( Variable sourceVariable, Variable predicateVariable,  Variable targetVariable,TupleExpr filterExpression,CustomQueryOptions customQueryOptions) {		
		//QueryModelNode filterExpression = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				QueryModelNode verbObjectListExpression = verbObjectList.filterExpression( sourceVariable,predicateVariable,targetVariable,customQueryOptions);		
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
	 * @param sourceVariable the source variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	public PathTupleExpr boundPatternQuery( Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {		
		TupleExpr factFilterPattern = null;
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				TupleExpr verbObjectListPattern = verbObjectList.boundPatternQuery( sourceVariable,targetVariable).getTupleExpr();		
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
	 * @param customQueryOptions the custom query options
	 * @return the array list
	 */
	public ArrayList<Variable> bindTargetVariable(Variable targetVariable, CustomQueryOptions customQueryOptions) {
		ArrayList<Variable>  targetVariables = new ArrayList<Variable> ();
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				targetVariables.addAll( verbObjectList.bindTargetVariable(targetVariable,customQueryOptions));			
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
