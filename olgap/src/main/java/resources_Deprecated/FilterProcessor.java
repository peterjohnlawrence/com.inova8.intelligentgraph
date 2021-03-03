package resources_Deprecated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import pathPatternElement.FactFilterElement;
import pathPatternElement.ObjectElement;
import pathPatternElement.VerbObjectList;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.Resources;
import pathPatternProcessor.PathConstants.FilterOperator;
@Deprecated
public class FilterProcessor {
	private final static Logger logger = LogManager.getLogger(FilterProcessor.class);
	
	public static Boolean applyObjectFilter(FactFilterElement filterElement, Value value) {
		//get only the first of propertyList and objectList
		if(filterElement.getPropertyListNotEmpty().size()>1) {
			logger.error("Filter with multiple propertylist not supported " + filterElement.toString());
		}else {
			VerbObjectList verbObjectList = filterElement.getPropertyListNotEmpty().get(0);
			FilterOperator filterOperator = verbObjectList.getFilterOperator();
			IRI predicate;
			if(verbObjectList.getPredicate()!=null )
				predicate = verbObjectList.getPredicate().getPredicate();
			if(verbObjectList.getObjectList().size()>1 ) {
				logger.error("Filter verbObjectList with multiple objects not supported " + filterElement.toString());				
			}else {
				ObjectElement object = verbObjectList.getObjectList().get(0);
				//Literal literalOperand = object.getLiteral();
				IRI iriOperand = object.getIri();
				//SimpleResources iriOperands = new SimpleResources(null, filterElement);
				//switch (statementFilterPathElement.getFilterOperator()) {
				if(filterOperator==null) {
					// predicate clause
					return evaluateFilterElement(value, filterElement);
				}else {
					switch (filterOperator) {
					case LT:
						break;
					case GT:
						break;
					case LE:
						break;
					case GE:
						break;
					case EQ:
						if(object.getBlankNodeVerbObjectList()!=null ) {
							//TODO nest into filter element and iterate to find results
							return evaluateFilterElement(value, object.getBlankNodeVerbObjectList().get(0));
						}else if(object.getLiteral()!=null) {
							return value.equals(object.getLiteral());
						}else if(object.getIri()!=null ) {
							return value.equals(object.getIri());
						}
						break;
					case NE:
						//return !value.equals(objectFilterPathElement.getFilterOperand());
						return !value.equals(iriOperand);
					default:
						logger.error("Filter operator not supported " + filterOperator.toString());
						break;
					}	
				}
			}
		}
		return true;
	}
	private  static Boolean evaluateFilterElement(Value value, FactFilterElement filterElement){
		Resources filterValues = processFilterElement(value, filterElement) ;
		if(filterValues!=null ) {
			return filterValues.hasNext();
		}else {
			return false;
		}
	}
	private  static Resources processFilterElement(Value value, FactFilterElement filterElement) {
		if(filterElement.getPropertyListNotEmpty()!=null) {
			SimpleResources resources = null ;
			for ( VerbObjectList verbObjectList: filterElement.getPropertyListNotEmpty() ) {	
				 IRI predicate = verbObjectList.getPredicate().getPredicate();
				for(ObjectElement object: verbObjectList.getObjectList()) {
					//   object.getIri();
					resources = new SimpleResources(value, predicate, object.getIri());
					return (Resources)resources;
				}
				
			}
			return (Resources)resources;
		}else {
			return (Resources)null;
		}

		
		
		
		
		
//		if(filterElement.getIsInverseOf()) {
//			if( filterElement.getIsReified()) {
//				return  ((Resource) thing).getIsReifiedFactsOf(filterElement);
//			}else {
//				return ((Resource) thing).getIsFactsOf(filterElement);
//			}
//		}else {
//			if( filterElement.getIsReified()) {
//				return  ((Resource) thing).getReifiedFacts(filterElement);
//			}else {
//				return ((Resource) thing).getFacts(filterElement);
//			}
//		}
	}
}
