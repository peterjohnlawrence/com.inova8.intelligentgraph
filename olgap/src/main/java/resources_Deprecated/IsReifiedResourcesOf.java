package resources_Deprecated;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Resource;
import pathCalc.Source;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.Resources;
import pathPatternProcessor.Thing;
import pathPatternProcessor.Resources.Selector;
@Deprecated
public class IsReifiedResourcesOf extends Resources{
	PredicateElement predicateElement;
	UnionIteration unionReificationObjectStatements;
	UnionIteration unionReificationSubjectStatements ;
	UnionIteration unionReificationPredicateStatements;
	org.eclipse.rdf4j.model.Value currentReification;
	Statement currentSubjectStatement;
	Statement currentPredicateStatement;
	Statement currentObjectStatement;
	
	
	public IsReifiedResourcesOf(DeprecatedThing thing, PredicateElement predicateElement) {
		super( Resources.Selector.OBJECT, thing, predicateElement.getReification(), predicateElement.getPredicate());
		this.predicateElement = predicateElement;
		//Initialize 
		unionReificationObjectStatements = prepareObjectStatements(predicateElement);
//		if( unionReificationObjectStatements.hasNext()) {
//			currentObjectStatement = unionReificationObjectStatements.next();//getNextSubjectValue();
//			currentReification = currentObjectStatement.getSubject();
//			unionReificationPredicateStatements = preparePredicateStatements(currentReification,predicateElement);
//			if( unionReificationPredicateStatements.hasNext()) {
//				currentPredicateStatement = unionReificationPredicateStatements.next();//getNextSubjectValue();
//				unionReificationSubjectStatements = prepareSubjectStatements(currentReification, predicateElement);	
//			}
//		}
	}
	
	@Override
	public boolean hasNext() throws QueryEvaluationException {
//		if(unionReificationSubjectStatements.hasNext()) {	
//			currentSubjectStatement = unionReificationSubjectStatements.next();
//			return true;
//		}else {
			if( unionReificationObjectStatements.hasNext()) {
				currentObjectStatement = unionReificationObjectStatements.next();
				currentReification = currentObjectStatement.getSubject();
				unionReificationPredicateStatements = preparePredicateStatements(currentReification,predicateElement);
				if( unionReificationPredicateStatements.hasNext()) {
					currentPredicateStatement = unionReificationPredicateStatements.next();
					unionReificationSubjectStatements = prepareSubjectStatements(currentReification, predicateElement);	
					if(unionReificationSubjectStatements.hasNext() ) {
						currentSubjectStatement = unionReificationSubjectStatements.next();
						return true;
					}else {
						return hasNext();
					}
				}else {
					return hasNext();
				}
			}else {
				return false;	
			}	
//		}
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		if (currentReification==null) {
			return null;
		}else if( predicateElement.getIsDereified()) {
			return thing.getSource().resourceFactory(getTracer(), currentSubjectStatement.getSubject(), getStack(), getCustomQueryOptions(),getPrefixes());
		}else if (predicateElement.getIsInverseOf()){
			return thing.getSource().resourceFactory(getTracer(), currentSubjectStatement.getObject(), getStack(), getCustomQueryOptions(),getPrefixes());
		}else {
			return thing.getSource().resourceFactory(getTracer(), currentSubjectStatement.getObject(), getStack(), getCustomQueryOptions(),getPrefixes());
		}
	}
	private UnionIteration prepareObjectStatements(PredicateElement predicateElement)
			throws QueryEvaluationException {
		// ?reification :reificationSubject  :thing .  :thing   :reificationIsSubjectOf  ?reification .
		IRI reificationType= predicateElement.getReification();
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationObjectStatements = null;
		if(Source.getReificationObject(reificationType)!=null) 
			reificationObjectStatements = Source.getTripleSource().getStatements(null, Source.getReificationObject(reificationType), (IRI) thing.getSuperValue());
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsObjectOfStatements = null ;
		if(Source.getReificationIsObjectOf(reificationType)!=null) 
			reificationIsObjectOfStatements = Source.getTripleSource().getStatements((IRI) thing.getSuperValue(), Source.getReificationIsObjectOf(reificationType), null );
		UnionIteration unionReificationObjectStatements = new UnionIteration(reificationObjectStatements, reificationIsObjectOfStatements,null,predicateElement.getStatementFilterElement());
		return unionReificationObjectStatements;
	}
	private UnionIteration prepareSubjectStatements( org.eclipse.rdf4j.model.Value reification,PredicateElement predicateElement)
			throws QueryEvaluationException {
		//?reification :reificationObject ?reifiedValue .  ?reifiedValue   :reificationIsObjectOf  ?reification .
		IRI reificationType= predicateElement.getReification();
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = null;
		if(Source.getReificationSubject(reificationType)!=null) 
			reificationSubjectStatements = Source.getTripleSource().getStatements((IRI) reification, Source.getReificationSubject(reificationType), null);
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsSubjectOfStatements = null;
		if(Source.getReificationIsSubjectOf(reificationType)!=null) 
			reificationIsSubjectOfStatements= Source.getTripleSource().getStatements(null , Source.getReificationIsSubjectOf(reificationType), (IRI) reification);
		UnionIteration unionReificationSubjectStatements = new UnionIteration(reificationSubjectStatements, reificationIsSubjectOfStatements,predicateElement.getObjectFilterElement(),null);
		return unionReificationSubjectStatements;
	}
	private UnionIteration preparePredicateStatements(org.eclipse.rdf4j.model.Value reification,PredicateElement predicateElement)
			throws QueryEvaluationException {
		// ?reification :reificationPredicate :predicate . :predicate   :reificationIsPredicateOf ?reification .
		IRI predicate=   predicateElement.getPredicate();
		IRI reificationType= predicateElement.getReification();
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = null;
		if(Source.getReificationPredicate(reificationType)!=null) 
			reificationPredicateStatements =  Source.getTripleSource().getStatements((IRI) reification, Source.getReificationPredicate(reificationType), predicate);
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsPredicateOfStatements = null;
		if(Source.getReificationIsPredicateOf(reificationType) !=null) 
			reificationIsPredicateOfStatements =  Source.getTripleSource().getStatements( predicate , Source.getReificationIsPredicateOf(reificationType),(IRI) reification);
		UnionIteration unionReificationPredicateStatements = new UnionIteration(reificationPredicateStatements, reificationIsPredicateOfStatements);
		return unionReificationPredicateStatements;
	}

	@Override
	public void remove() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() throws QueryEvaluationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	CloseableIteration<? extends Statement, QueryEvaluationException> getStatements() {
		// TODO Auto-generated method stub
		return null;
	}
}
