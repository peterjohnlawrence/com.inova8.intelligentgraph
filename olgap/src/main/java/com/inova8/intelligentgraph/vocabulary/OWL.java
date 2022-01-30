/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class OWL.
 */
public class OWL extends org.eclipse.rdf4j.model.vocabulary.OWL{

	/** The Constant NAMESPACE. */
	public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";
	
	/** The Constant inverse_of. */
	public static final String inverse_of = NAMESPACE+"inverseOf";
	
	/** The Constant OWL_INVERSE_OF. */
	public static final IRI OWL_INVERSE_OF = iri(inverse_of);

}
