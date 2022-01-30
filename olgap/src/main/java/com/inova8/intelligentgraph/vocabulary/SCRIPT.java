/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class SCRIPT.
 */
public class SCRIPT {

	/** The Constant NAMESPACE. */
	public static final String NAMESPACE = "http://inova8.com/script/";
	
	/** The Constant GROOVY. */
	public static final IRI GROOVY = iri(NAMESPACE + "groovy");
	
	/** The Constant JS. */
	public static final IRI JS = iri(NAMESPACE + "javascript");
	
	/** The Constant PYTHON. */
	public static final IRI PYTHON = iri(NAMESPACE + "python");
	
	/** The Constant SCRIPTCODE. */
	public static final IRI SCRIPTCODE = iri(NAMESPACE + "scriptCode");
	
	/** The Constant DATA. */
	public static final String DATA = NAMESPACE +"data/";
	
	/** The Constant ANONTHING. */
	public static final IRI ANONTHING = iri(NAMESPACE + "anonThing");
	
	/** The Constant ANONPREDICATE. */
	public static final IRI ANONPREDICATE = iri(NAMESPACE + "anonPredicate");
	
	/** The Constant ISPRIVATE. */
	public static final IRI ISPRIVATE = iri(NAMESPACE + "isPrivate");
//	public static final String ISPRIVATE = "isPrivate";
	/** The Constant CACHE_DATE_TIME. */
//	public static final String CACHE_DATE_TIME = "cacheDateTime";
	public static final IRI CACHE_DATE_TIME = iri(NAMESPACE + "cacheDateTime");
}
