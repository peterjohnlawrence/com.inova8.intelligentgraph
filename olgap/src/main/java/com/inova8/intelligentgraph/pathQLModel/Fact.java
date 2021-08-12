/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathQLModel;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.pathCalc.Thing;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.processor.PathPatternException;

public class Fact extends Resource {
	private static final long serialVersionUID = 1L;

	Resource predicate =null;

	Resource subject;

	IRI reification;	

	Boolean isDereified;

	public Fact(Resource subject, Resource predicate,  Value value ) {
		super(value);
		this.predicate = predicate;
		this.subject = subject;
	}

	public Fact(Value subject, Value predicate, Value value) {
		super(value);
		if(predicate!=null)
			this.predicate =Thing.create(getSource(), predicate, getEvaluationContext() );	
		if(subject!=null)
			this.subject = Thing.create(getSource(), subject, getEvaluationContext());
	}

	public Resource getPredicate() {
		return predicate;
	}
	public IRI getPredicateIRI() {
		return (IRI) predicate.getSuperValue();
	}	

	public Resource getSubject() {
		return subject;
	}
	public IRI getSubjectIRI() {
		return (IRI) subject.getSuperValue();
	}

	@Override
	public String toString() {
		return "Fact [Resource[ object=" + super.toString() +  "], predicate=" + predicate + ", subject=" + subject +  "]";
	}

	public int getSubjectId() {
		return hashCode();
	}
	

	public URI getId() {
		try {
			return new URI("MatchFact" + "(" + getSubjectId() + ")");
		} catch (URISyntaxException e) {
		//	throw new Exception("Unable to create id for entity: " + "MatchFact", e);
			return null;
		}
	}
	

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResourceResults getFacts(String predicatePattern, Value... bindValues ) throws PathPatternException {
		return null;
	}
	
	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}

}
