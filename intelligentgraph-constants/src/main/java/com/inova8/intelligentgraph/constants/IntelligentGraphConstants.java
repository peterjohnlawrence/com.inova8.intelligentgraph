package com.inova8.intelligentgraph.constants;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class IntelligentGraphConstants {
	public static final String BINDVARIABLEPREFIX ="%";
	/** The Constant CACHE_HASH. */
	public static final String CACHE_HASH = "cacheHash";
	public static final String CUSTOMQUERYOPTIONS ="_customQueryOptions";
	public static final String PROPERTY ="_property";
	
	public static final String THIS ="_this";
	
	public static final String URN_CUSTOM_QUERY_OPTIONS = "urn:customQueryOptions";
	
	public static final String DATASET = "dataset";

	public static final String PREFIXES = "prefixes";
	public static final String QUERYTYPE = "queryType";
	public static final String SAIL = "sail";
	public static final String TUPLEEXPR = "tupleExpr";
	public static final String INTELLIGENTGRAPHCONNECTION = "intelligentGraphConnection";	
	public static final IRI DEFAULTGRAPH = iri("http://default");
}
