/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import com.inova8.intelligentgraph.pathCalc.Thing;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.processor.PathPatternException;

/**
 * The Class Path.
 */
//public class Path extends ArrayList<Edge>  {
public class Path extends Resource  {

	ArrayList<Edge> path = new ArrayList<Edge>();
	public static Path create(Thing thing, Value pathValue) throws RDFParseException, UnsupportedRDFormatException, IOException {
		String pathString = pathValue.stringValue();
		Model pathModel = Rio.parse(new ByteArrayInputStream(pathString.getBytes(StandardCharsets.UTF_8)), PATHQL.NAMESPACE, RDFFormat.TURTLE);
		Path path = new Path(thing.getValue());
		for (org.eclipse.rdf4j.model.Resource edge: pathModel.filter(null, RDF.TYPE, PATHQL.EDGE).subjects()) {
			Edge pathEdge;
			Optional<org.eclipse.rdf4j.model.Resource> source =Models.getPropertyResource( pathModel , edge, PATHQL.EDGE_SOURCE);    
			Optional<org.eclipse.rdf4j.model.Resource> predicate =Models.getPropertyResource( pathModel , edge, PATHQL.EDGE_PREDICATE);   
			Optional<org.eclipse.rdf4j.model.Value> target =Models.getProperty( pathModel , edge, PATHQL.EDGE_TARGET);    
			Models.getPropertyLiteral( pathModel , edge, PATHQL.EDGE_DIRECT);
			Models.getPropertyLiteral( pathModel , edge, PATHQL.EDGE_DEREIFIED);  
			Optional<IRI> reification =Models.getPropertyIRI( pathModel , edge, PATHQL.EDGE_REIFICATION); 
			if(reification.isPresent()) {
				pathEdge = new Edge(source.get(), reification.get(), predicate.get(), target.get(), true, false);//direct.get(), dereified.get());
			}else {
				pathEdge = new Edge(source.get(), predicate.get(), target.get(), true);//direct.get());
			}
			path.add(pathEdge)	;
		}		
		return path;	
		
	}
	protected Path(Value value) {
		super(value);
	}

	public String toSPARQL() {
	
		return null;
	}

	public String toString() {
		String pathString = "Path=[";
		for ( Edge edge : path) {
			pathString += edge.toString() +"\r\n";
		};
		return pathString+"]\r\n";
	}

	public void add(Edge pathEdge) {
		path.add(pathEdge);
	}

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues ) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(String predicatePattern, Value...bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}
	
} 
