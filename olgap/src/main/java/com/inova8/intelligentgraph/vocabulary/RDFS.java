/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class RDFS.
 */
public class RDFS extends org.eclipse.rdf4j.model.vocabulary.RDFS{


	/** The Constant NAMESPACE. */
	public static final String NAMESPACE ="http://www.w3.org/2000/01/rdf-schema#";
	
	/** The Constant domain. */
	public static final String domain=NAMESPACE+"domain";
	
	/** The Constant DOMAIN. */
	public static final IRI DOMAIN = iri(domain);

	/** The Constant range. */
	public static final String range=NAMESPACE+"range";
	
	/** The Constant RANGE. */
	public static final IRI RANGE = iri(range);

	/** The Constant subClassOf. */
	public static final String subClassOf=NAMESPACE+"subClassOf";
	
	/** The Constant SUBCLASSOF. */
	public static final IRI SUBCLASSOF = iri(subClassOf);
	
	/** The Constant subPropertyOf. */
	public static final String subPropertyOf=NAMESPACE+"subPropertyOf";
	
	/** The Constant SUB_PROPERTY_OF. */
	public static final IRI SUB_PROPERTY_OF = iri(subPropertyOf);

}
