/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.repositoryContext;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;

/**
 * The Class ReificationType.
 */
public class ReificationType {
	 
 	/** The reification type. */
 	private IRI reificationType;
	 
 	/** The reification subject. */
 	private IRI reificationSubject;
	 
 	/** The reification predicate. */
 	private IRI reificationPredicate;
	 
 	/** The reification object. */
 	private IRI reificationObject;	
	 
 	/** The reification is subject of. */
 	private IRI reificationIsSubjectOf;
	 
 	/** The reification is predicate of. */
 	private IRI reificationIsPredicateOf;
	 
 	/** The reification is object of. */
 	private IRI reificationIsObjectOf;	
	
	/**
	 * Instantiates a new reification type.
	 *
	 * @param reificationType the reification type
	 * @param reificationSubject the reification subject
	 * @param reificationPredicate the reification predicate
	 * @param reificationObject the reification object
	 */
	public ReificationType(IRI reificationType, IRI reificationSubject, IRI reificationPredicate, IRI reificationObject) {
		super();
		this.reificationType = reificationType;
		this.reificationSubject = reificationSubject;
		this.reificationPredicate = reificationPredicate;
		this.reificationObject = reificationObject;
	}	
	
	/**
	 * Instantiates a new reification type.
	 *
	 * @param reificationType the reification type
	 * @param reificationSubject the reification subject
	 * @param reificationPredicate the reification predicate
	 * @param reificationObject the reification object
	 * @param reificationIsSubjectOf the reification is subject of
	 * @param reificationIsPredicateOf the reification is predicate of
	 * @param reificationIsObjectOf the reification is object of
	 */
	public ReificationType(Resource reificationType, Resource reificationSubject, Resource reificationPredicate,
			Resource reificationObject , Resource reificationIsSubjectOf ,Resource reificationIsPredicateOf ,Resource reificationIsObjectOf   ) {
		this.reificationType = (IRI)reificationType;
		this.reificationSubject = (IRI)reificationSubject;
		this.reificationPredicate = (IRI)reificationPredicate;
		this.reificationObject =(IRI) reificationObject;
		
		this.reificationIsSubjectOf = (IRI)reificationIsSubjectOf;
		this.reificationIsPredicateOf = (IRI)reificationIsPredicateOf;
		this.reificationIsObjectOf =(IRI) reificationIsObjectOf;
	}
	
	/**
	 * Gets the reification type.
	 *
	 * @return the reification type
	 */
	public  IRI getReificationType() {
		return reificationType;
	}
	
	/**
	 * Gets the reification subject.
	 *
	 * @return the reification subject
	 */
	public  IRI getReificationSubject() {
		return reificationSubject;
	}
	
	/**
	 * Gets the reification predicate.
	 *
	 * @return the reification predicate
	 */
	public  IRI getReificationPredicate() {
		return reificationPredicate;
	}
	
	/**
	 * Gets the reification object.
	 *
	 * @return the reification object
	 */
	public  IRI getReificationObject() {
		return reificationObject;
	}
	
	/**
	 * Gets the reification is subject of.
	 *
	 * @return the reification is subject of
	 */
	public IRI getReificationIsSubjectOf() {
		return reificationIsSubjectOf;
	}
	
	/**
	 * Gets the reification is predicate of.
	 *
	 * @return the reification is predicate of
	 */
	public IRI getReificationIsPredicateOf() {
		return reificationIsPredicateOf;
	}
	
	/**
	 * Gets the reification is object of.
	 *
	 * @return the reification is object of
	 */
	public IRI getReificationIsObjectOf() {
		return reificationIsObjectOf;
	}


}
