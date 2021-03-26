package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class FactFilterElement extends ObjectElement{


	private ArrayList<VerbObjectList>  propertyListNotEmpty;
	
	public FactFilterElement() {
		super();
		operator = PathConstants.Operator.PROPERTYLIST;
	}


	public String toString() {
		String filterElement="[";
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				filterElement += verbObjectList.toString() +" ;";			
			}	
		}
		return filterElement.substring(0,filterElement.length()-1)+ "]";
	}

	public String toSPARQL( String sourceValue) {
		String filterElement="";
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				filterElement += verbObjectList.toSPARQL( sourceValue);			
			}	
		}
		return filterElement;
	}
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
	public ArrayList<VerbObjectList> getPropertyListNotEmpty() {
		return propertyListNotEmpty;
	}

	public void setPropertyListNotEmpty(ArrayList<VerbObjectList> propertyListNotEmpty) {
		this.propertyListNotEmpty = propertyListNotEmpty;
	}


	@Override
	public
	String toHTML() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toSPARQL() {

		return toSPARQL( "?n"+ getEntryIndex() ) ; //getIndex() ) ;
	}


	public ArrayList<String> bindTargetValue( String targetValue) {
		ArrayList<String>  targetValues = new ArrayList<String> ();
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				targetValues.addAll( verbObjectList.bindTargetValue(targetValue));			
			}	
		}
		return targetValues;
	}


	public ArrayList<Variable> bindTargetVariable(Variable targetVariable) {
		ArrayList<Variable>  targetVariables = new ArrayList<Variable> ();
		if(propertyListNotEmpty!=null) {
			for ( VerbObjectList verbObjectList: propertyListNotEmpty) {
				targetVariables.addAll( verbObjectList.bindTargetVariable(targetVariable));			
			}	
		}
		return targetVariables;
	};

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
