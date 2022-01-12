/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import com.inova8.intelligentgraph.exceptions.PathPatternException;
import com.inova8.intelligentgraph.reference.PredicateElement;
import com.inova8.intelligentgraph.results.FactResults;
import com.inova8.intelligentgraph.vocabulary.PATHQL;


public class Path extends Resource  implements Iterable<Edge>{
	private static final long serialVersionUID = 1L;
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
			Optional<org.eclipse.rdf4j.model.Literal> direction =Models.getPropertyLiteral( pathModel , edge, PATHQL.EDGE_DIRECTION);
			boolean isDirect = direction.get().getLabel().equals(Direction.DIRECT.toString())?false:true;
			Optional<IRI> reification =Models.getPropertyIRI( pathModel , edge, PATHQL.EDGE_REIFICATION); 
			if(reification.isPresent()) {
				Optional<org.eclipse.rdf4j.model.Literal> dereified = Models.getPropertyLiteral( pathModel , edge, PATHQL.EDGE_DEREIFIED);  
				boolean isDereified= dereified.get().booleanValue();
				pathEdge = new Edge(source.get(), reification.get(), predicate.get(), target.get(), isDirect,isDereified);
			}else {
				pathEdge = new Edge(source.get(), predicate.get(), target.get(), isDirect);
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
//	public  PathSteps toPathSteps(){
//		PathSteps map = new PathSteps();
//		map.put("start", path.get(0).source);
//		Steps steps = new Steps();
//		for (Edge edge : path) {
//			steps.add(edge.toStep());
//		}
//		map.put("steps", steps);
//		return map;	
//	}
	@Override
	public Resource getFact(String predicatePattern, Value... bindValues ) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactResults getFacts(String predicatePattern, Value...bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Iterator<Edge> iterator() {
		return path.iterator();
	}
	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}
	
} 
