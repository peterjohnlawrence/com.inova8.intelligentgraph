package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class RDF extends org.eclipse.rdf4j.model.vocabulary.RDF{
	public static final String NAMESPACE= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String object =  NAMESPACE+ "object";
	public static final IRI OBJECT = iri( object);
	public static final String predicate =  NAMESPACE+"predicate";
	public static final IRI PREDICATE=  iri( predicate);
	public static final String Statement =  NAMESPACE+"Statement";
	public static final IRI STATEMENT = iri( Statement);
	public static final String subject =  NAMESPACE+"subject";
	public static final IRI SUBJECT = iri( subject);
	public static final String type =  NAMESPACE+"type";
	public static final IRI TYPE = iri( type);

}
