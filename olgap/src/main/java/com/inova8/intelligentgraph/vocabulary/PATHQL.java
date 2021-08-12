package com.inova8.intelligentgraph.vocabulary;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class PATHQL {

	public static final String NAMESPACE = "http://inova8.com/pathql/";
	public static final String getFact =NAMESPACE+"getFact";
	public static final IRI GETFACT = iri(getFact);
	public static final String getFacts = NAMESPACE+"getFacts";
	public static final IRI GETFACTS = iri(getFacts);
	public static final String getPath = NAMESPACE+"getPath";
	public static final IRI GETPATH = iri(getPath);
	public static final String getPaths = NAMESPACE+"getPaths";
	public static final IRI GETPATHS = iri(getPaths);
	public static final String getScript = NAMESPACE+"getScript";
	public static final IRI GETSCRIPT = iri(getScript);
	public static final String hasPath = NAMESPACE+"hasPath";
	public static final IRI HASPATH = iri(hasPath);
	public static final String clearCache =NAMESPACE+"clearCache";
	public static final IRI CLEARCACHE = iri(clearCache);
	public static final String traceFact = NAMESPACE+"traceFact";
	public static final IRI TRACEFACT = iri(traceFact);
	public static final String traceFacts = NAMESPACE+"traceFacts";
	public static final IRI TRACEFACTS = iri(traceFacts);
	
	
	public static final String Edge =NAMESPACE+"Edge";
	public static final IRI EDGE = iri(Edge);
	public static final String edge_Dereified =NAMESPACE+"edge.Dereified";
	public static final IRI EDGE_DEREIFIED = iri(edge_Dereified);
	public static final String edge_Direct =NAMESPACE+"edge.Direct";
	public static final IRI EDGE_DIRECT = iri(edge_Direct);
	public static final String edge_Predicate =NAMESPACE+"edge.Predicate";
	public static final IRI EDGE_PREDICATE = iri(edge_Predicate);
	public static final String edge_Reification =NAMESPACE+"edge.Reification";
	public static final IRI EDGE_REIFICATION = iri(edge_Reification);
	public static final String edge_Source =NAMESPACE+"edge.Source";
	public static final IRI EDGE_SOURCE = iri(edge_Source);
	public static final String edge_Target =NAMESPACE+"edge.Target";
	public static final IRI EDGE_TARGET = iri(edge_Target);
	public static final String path_Edge =NAMESPACE+"path.Edge";
	public static final IRI PATH_EDGE = iri(path_Edge);

}
