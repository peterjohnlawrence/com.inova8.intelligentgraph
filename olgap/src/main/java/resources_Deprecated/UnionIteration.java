package resources_Deprecated;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathPatternElement.FactFilterElement;
import pathPatternElement.ObjectElement;
import pathPatternElement.VerbObjectList;
import pathPatternProcessor.PathConstants.FilterOperator;
@Deprecated
public class UnionIteration	extends AbstractCloseableIteration<Statement, QueryEvaluationException> {
	private final Logger logger = LogManager.getLogger(UnionIteration.class);
	CloseableIteration<? extends Statement, QueryEvaluationException> reificationStatements;
	CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseStatements;
	private CloseableIteration<? extends Statement, QueryEvaluationException> currentStatements;
	private boolean first=true;
	private FactFilterElement objectFilterPathElement;
	private FactFilterElement statementFilterPathElement;	
	private Statement nextStatement ;

	public UnionIteration(CloseableIteration<? extends Statement, QueryEvaluationException> reificationStatements,
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseStatements) {
		super();
		this.reificationStatements = reificationStatements;
		this.reificationInverseStatements = reificationInverseStatements;
		this.currentStatements  = this.reificationStatements;
	}
	public UnionIteration(CloseableIteration<? extends Statement, QueryEvaluationException> reificationStatements,
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseStatements, FactFilterElement objectFilterPathElement, FactFilterElement statementFilterPathElement) {
		super();
		this.reificationStatements = reificationStatements;
		this.reificationInverseStatements = reificationInverseStatements;
		this.currentStatements  = this.reificationStatements;
		this.objectFilterPathElement = objectFilterPathElement;
		this.statementFilterPathElement = statementFilterPathElement;
	}
	public boolean hasNext() {
		if(next()!=null) {
			return true;
		}else {
			return false;
		}
	}
	public org.eclipse.rdf4j.model.Value getNextSubjectValue() {
		Statement currentStatement = this.currentStatements.next();
		if(this.first) {
			return currentStatement.getSubject();
		}else {
			return currentStatement.getObject();
		}
	}
	public org.eclipse.rdf4j.model.Value getNextObjectValue() {
		Statement currentStatement = this.currentStatements.next();
		if(this.first) {
			return currentStatement.getObject();
		}else {
			return currentStatement.getSubject();
			
		}
	}
	@Override
	public Statement next() throws QueryEvaluationException {
		if(nextStatement!=null) {
			Statement returnStatement = nextStatement;
			nextStatement =null;
			return returnStatement;
		}else {
			nextStatement = getNext();
			return nextStatement;
		}
	}
	
	private Statement getNext() throws QueryEvaluationException {
		Statement nextStatement;
		if (this.currentStatements!=null && this.currentStatements.hasNext()) {
			nextStatement= this.currentStatements.next();
			if(objectFilterPathElement!=null ) {
				//if(!applyObjectFilter(nextStatement.getObject()) ) {
				if(!FilterProcessor.applyObjectFilter(objectFilterPathElement, nextStatement.getObject()) ) {
					if (this.hasNext())
						return next();
					else 
						return null;
				}
			}
			if(statementFilterPathElement!=null ) {
				//if(!applyStatementFilter(nextStatement.getSubject()) ) {
				if(!FilterProcessor.applyObjectFilter(statementFilterPathElement, nextStatement.getSubject()) ) {
					if (this.hasNext())
						return next();
					else 
						return null;
				}
			}
			return nextStatement;
		} else if (this.first && reificationInverseStatements!=null  && this.reificationInverseStatements.hasNext()) {
			this.currentStatements  = this.reificationInverseStatements;	
			this.first=false;
			nextStatement= this.currentStatements.next();
			if(objectFilterPathElement!=null ) {
				//if(!applyObjectFilter(nextStatement.getSubject()) ) {
				if(!FilterProcessor.applyObjectFilter(objectFilterPathElement, nextStatement.getSubject()) ) {
					if (this.hasNext())
						return next();
					else 
						return null;
				}
			}
			if(statementFilterPathElement!=null ) {
				//if(!applyStatementFilter((Resource) nextStatement.getObject()) ) {
				if(!FilterProcessor.applyObjectFilter(statementFilterPathElement, nextStatement.getSubject()) ) {
					if (this.hasNext())
						return next();
					else 
						return null;
				}
			}
			return nextStatement;
		}else {
			return null;
		}
		
	}
	@Override
	public void remove() throws QueryEvaluationException {
		if(reificationStatements!=null)  reificationStatements.remove();
		if(reificationInverseStatements!=null) reificationInverseStatements.remove();	
	}
	private Boolean applyObjectFilter(Value value) {
		//get only the first of propertyList and objectList
		if(objectFilterPathElement.getPropertyListNotEmpty().size()>1) {
			logger.error("Filter with multiple propertylist not supported " + objectFilterPathElement.toString());
		}else {
			VerbObjectList verbObjectList = objectFilterPathElement.getPropertyListNotEmpty().get(0);
			FilterOperator filterOperator = verbObjectList.getFilterOperator();
			IRI predicate;
			if(verbObjectList.getPredicate()!=null )
				predicate = verbObjectList.getPredicate().getPredicate();
			if(verbObjectList.getObjectList().size()>1 ) {
				logger.error("Filter verbObjectList with multiple objects not supported " + objectFilterPathElement.toString());				
			}else {
				ObjectElement object = verbObjectList.getObjectList().get(0);
				Literal literalOperand = object.getLiteral();
				IRI iriOperand = object.getIri();
				//switch (objectFilterPathElement.getFilterOperator()) {
				if(filterOperator==null) {
					// predicate clause
					
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
						//return value.equals(objectFilterPathElement.getFilterOperand());
						return value.equals(iriOperand);
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
	private Boolean applyStatementFilter(Value value) {
		//get only the first of propertyList and objectList
		if(statementFilterPathElement.getPropertyListNotEmpty().size()>1) {
			logger.error("Filter with multiple propertylist not supported " + statementFilterPathElement.toString());
		}else {
			VerbObjectList verbObjectList = statementFilterPathElement.getPropertyListNotEmpty().get(0);
			FilterOperator filterOperator = verbObjectList.getFilterOperator();
			IRI predicate;
			if(verbObjectList.getPredicate()!=null )
				predicate = verbObjectList.getPredicate().getPredicate();
			if(verbObjectList.getObjectList().size()>1 ) {
				logger.error("Filter verbObjectList with multiple objects not supported " + statementFilterPathElement.toString());				
			}else {
				ObjectElement object = verbObjectList.getObjectList().get(0);
				Literal literalOperand = object.getLiteral();
				IRI iriOperand = object.getIri();
				//SimpleResources iriOperands = new SimpleResources(null, null);
				//switch (statementFilterPathElement.getFilterOperator()) {
				if(filterOperator==null) {
					// predicate clause
					
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
						//return value.equals(objectFilterPathElement.getFilterOperand());
						return value.equals(iriOperand);
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
}
