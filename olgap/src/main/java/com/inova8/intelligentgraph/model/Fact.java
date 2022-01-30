/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.pathql.processor.PathPatternException;

/**
 * The Class Fact.
 */
public class Fact extends Resource {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 */
	public Fact(Resource subject, Predicate predicate,  Value value ) {
		super(value);
		this.predicate = predicate;
		this.subject = subject;
	}

	/**
	 * Instantiates a new fact.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 */
	public Fact(Value subject, Value predicate, Value value) {
		super(value);
		if(predicate!=null)
			try {
				this.predicate =new Predicate((IRI)predicate) ;
			} catch (URISyntaxException e) {
				//TODO
				this.predicate=null;
			} 
		if(subject!=null)
			this.subject = Thing.create(getSource(), subject, getEvaluationContext());
	}

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public Predicate getPredicate() {
		return predicate;
	}
	
	/**
	 * Gets the predicate IRI.
	 *
	 * @return the predicate IRI
	 */
	public IRI getPredicateIRI() {
		return (IRI) predicate.getSuperValue();
	}	

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public Resource getSubject() {
		return subject;
	}
	
	/**
	 * Gets the subject IRI.
	 *
	 * @return the subject IRI
	 */
	public IRI getSubjectIRI() {
		return (IRI) subject.getSuperValue();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Fact [Resource[ object=" + super.toString() +  "], predicate=" + predicate + ", subject=" + subject +  "]";
	}

	/**
	 * Gets the subject id.
	 *
	 * @return the subject id
	 */
	public int getSubjectId() {
		return hashCode();
	}
	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public URI getId() {
		try {
			return new URI("MatchFact" + "(" + getSubjectId() + ")");
		} catch (URISyntaxException e) {
		//	throw new Exception("Unable to create id for entity: " + "MatchFact", e);
			return null;
		}
	}
	

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public ResourceResults getFacts(String predicatePattern, Value... bindValues ) throws PathPatternException {
		return null;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}
	

//	@Override
//	public Object getSnippet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//
//	@Override
//	public Object getScore() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
