/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class PATHQL.
 */
public class PATHQL {

	/** The Constant PATH_EDGESTRING. */
	private static final String PATH_EDGESTRING = "path.Edge";

	/** The Constant NAMESPACE. */
	public static final String NAMESPACE = "http://inova8.com/pathql/";
	
	/** The Constant addFact. */
	public static final String addFact =NAMESPACE+"addFact";
	
	/** The Constant ADDFACT. */
	public static final IRI ADDFACT = iri(addFact);
	
	/** The Constant removeFact. */
	public static final String removeFact =NAMESPACE+"removeFact";
	
	/** The Constant REMOVEFACT. */
	public static final IRI REMOVEFACT = iri(removeFact);
	
	/** The Constant removeFacts. */
	public static final String removeFacts =NAMESPACE+"removeFacts";
	
	/** The Constant REMOVEFACTS. */
	public static final IRI REMOVEFACTS = iri(removeFacts);
	
	/** The Constant getFact. */
	public static final String getFact =NAMESPACE+"getFact";
	
	/** The Constant GETFACT. */
	public static final IRI GETFACT = iri(getFact);
	
	/** The Constant getFacts. */
	public static final String getFacts = NAMESPACE+"getFacts";
	
	/** The Constant GETFACTS. */
	public static final IRI GETFACTS = iri(getFacts);
	
	/** The Constant getPath. */
	public static final String getPath = NAMESPACE+"getPath";
	
	/** The Constant GETPATH. */
	public static final IRI GETPATH = iri(getPath);
	
	/** The Constant getPaths. */
	public static final String getPaths = NAMESPACE+"getPaths";
	
	/** The Constant GETPATHS. */
	public static final IRI GETPATHS = iri(getPaths);
	
	/** The Constant getScript. */
	public static final String getScript = NAMESPACE+"getScript";
	
	/** The Constant GETSCRIPT. */
	public static final IRI GETSCRIPT = iri(getScript);
	
	/** The Constant hasPath. */
	public static final String hasPath = NAMESPACE+"hasPath";
	
	/** The Constant HASPATH. */
	public static final IRI HASPATH = iri(hasPath);
	
	/** The Constant clearCache. */
	public static final String clearCache =NAMESPACE+"clearCache";
	
	/** The Constant CLEARCACHE. */
	public static final IRI CLEARCACHE = iri(clearCache);
	
	/** The Constant traceFact. */
	public static final String traceFact = NAMESPACE+"traceFact";
	
	/** The Constant TRACEFACT. */
	public static final IRI TRACEFACT = iri(traceFact);
	
	/** The Constant traceFacts. */
	public static final String traceFacts = NAMESPACE+"traceFacts";
	
	/** The Constant TRACEFACTS. */
	public static final IRI TRACEFACTS = iri(traceFacts);
	
	/** The Constant EDGESTRING. */
	private static final String EDGESTRING = "Edge";
	
	/** The Constant EDGE_TARGETSTRING. */
	private static final String EDGE_TARGETSTRING = "edge.Target";
	
	/** The Constant EDGE_SOURCESTRING. */
	private static final String EDGE_SOURCESTRING = "edge.Source";
	
	/** The Constant EDGE_REIFICATIONSTRING. */
	public static final String EDGE_REIFICATIONSTRING = "edge.Reification";
	
	/** The Constant EDGE_PREDICATESTRING. */
	public static final String EDGE_PREDICATESTRING = "edge.Predicate";
	
	/** The Constant EDGE_DEREIFIEDSTRING. */
	public static final String EDGE_DEREIFIEDSTRING = "edge.Dereified";
	
	/** The Constant EDGE_DIRECTIONSTRING. */
	public static final String EDGE_DIRECTIONSTRING = "edge.Direction";
	
	/** The Constant Edge. */
	public static final String Edge =NAMESPACE+EDGESTRING;
	
	/** The Constant EDGE. */
	public static final IRI EDGE = iri(Edge);
	
	/** The Constant edge_Dereified. */
	public static final String edge_Dereified =NAMESPACE+EDGE_DEREIFIEDSTRING;
	
	/** The Constant EDGE_DEREIFIED. */
	public static final IRI EDGE_DEREIFIED = iri(edge_Dereified);
	
	/** The Constant edge_Direction. */
	public static final String edge_Direction =NAMESPACE+EDGE_DIRECTIONSTRING;
	
	/** The Constant EDGE_DIRECTION. */
	public static final IRI EDGE_DIRECTION = iri(edge_Direction);
	
	/** The Constant edge_Predicate. */
	public static final String edge_Predicate =NAMESPACE+EDGE_PREDICATESTRING;
	
	/** The Constant EDGE_PREDICATE. */
	public static final IRI EDGE_PREDICATE = iri(edge_Predicate);
	
	/** The Constant edge_Reification. */
	public static final String edge_Reification =NAMESPACE+EDGE_REIFICATIONSTRING;
	
	/** The Constant EDGE_REIFICATION. */
	public static final IRI EDGE_REIFICATION = iri(edge_Reification);
	
	/** The Constant edge_Source. */
	public static final String edge_Source =NAMESPACE+EDGE_SOURCESTRING;
	
	/** The Constant EDGE_SOURCE. */
	public static final IRI EDGE_SOURCE = iri(edge_Source);
	
	/** The Constant edge_Target. */
	public static final String edge_Target =NAMESPACE+EDGE_TARGETSTRING;
	
	/** The Constant EDGE_TARGET. */
	public static final IRI EDGE_TARGET = iri(edge_Target);
	
	/** The Constant path_Edge. */
	public static final String path_Edge =NAMESPACE+PATH_EDGESTRING;
	
	/** The Constant PATH_EDGE. */
	public static final IRI PATH_EDGE = iri(path_Edge);

}
