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
	public static final String groovy = NAMESPACE + "groovy";
	public static final IRI GROOVY = iri(groovy);
	
	/** The Constant JS. */
	public static final String js = NAMESPACE + "javascript";
	public static final IRI JS = iri(js);
	
	/** The Constant PYTHON. */
	public static final String python = NAMESPACE + "python";
	public static final IRI PYTHON = iri(python);
	
	/** The Constant SCRIPTCODE. */
	public static final String scriptcode = NAMESPACE + "scriptCode";
	public static final IRI SCRIPTCODE = iri(scriptcode);
	
	/** The Constant DATA. */
	public static final String DATA = NAMESPACE +"data/";
	
	/** The Constant ANONTHING. */
	public static final String anonthing = NAMESPACE + "anonThing";
	public static final IRI ANONTHING = iri(anonthing);
	
	/** The Constant ANONPREDICATE. */
	public static final String anonpredicate = NAMESPACE + "anonPredicate";
	public static final IRI ANONPREDICATE = iri(anonpredicate);
	
	/** The Constant ISPRIVATE. */
	public static final String isprivate = NAMESPACE + "isPrivate";
	public static final IRI ISPRIVATE = iri(isprivate);

	public static final String cache_date_time = NAMESPACE + "cacheDateTime";
	public static final IRI CACHE_DATE_TIME = iri(cache_date_time);
}
