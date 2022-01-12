package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class OWL extends org.eclipse.rdf4j.model.vocabulary.OWL{

	/** The Constant OWL_INVERSE_OF. */
	public static final String NAMESPACE = "http://www.w3.org/2002/07/owl#";
	public static final String inverse_of = NAMESPACE+"inverseOf";
	public static final IRI OWL_INVERSE_OF = iri(inverse_of);

}
