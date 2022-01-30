/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class RDF.
 */
public class RDF extends org.eclipse.rdf4j.model.vocabulary.RDF{
	
	/** The Constant NAMESPACE. */
	public static final String NAMESPACE= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	/** The Constant object. */
	public static final String object =  NAMESPACE+ "object";
	
	/** The Constant OBJECT. */
	public static final IRI OBJECT = iri( object);
	
	/** The Constant predicate. */
	public static final String predicate =  NAMESPACE+"predicate";
	
	/** The Constant PREDICATE. */
	public static final IRI PREDICATE=  iri( predicate);
	
	/** The Constant Statement. */
	public static final String Statement =  NAMESPACE+"Statement";
	
	/** The Constant STATEMENT. */
	public static final IRI STATEMENT = iri( Statement);
	
	/** The Constant subject. */
	public static final String subject =  NAMESPACE+"subject";
	
	/** The Constant SUBJECT. */
	public static final IRI SUBJECT = iri( subject);
	
	/** The Constant type. */
	public static final String type =  NAMESPACE+"type";
	
	/** The Constant TYPE. */
	public static final IRI TYPE = iri( type);

}
