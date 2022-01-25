package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class RDFS extends org.eclipse.rdf4j.model.vocabulary.RDFS{


	public static final String NAMESPACE ="http://www.w3.org/2000/01/rdf-schema#";
	
	public static final String domain=NAMESPACE+"domain";
	public static final IRI DOMAIN = iri(domain);

	public static final String range=NAMESPACE+"range";
	public static final IRI RANGE = iri(range);

	public static final String subClassOf=NAMESPACE+"subClassOf";
	public static final IRI SUBCLASSOF = iri(subClassOf);
	
	public static final String subPropertyOf=NAMESPACE+"subPropertyOf";
	public static final IRI SUB_PROPERTY_OF = iri(subPropertyOf);

}
