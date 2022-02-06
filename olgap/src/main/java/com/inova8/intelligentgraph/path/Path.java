/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.pathql.processor.PathPatternException;

/**
 * The Class Path.
 */
public class Path extends Resource  implements Iterable<Edge>{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The path. */
	ArrayList<Edge> path = new ArrayList<Edge>();
	
	/**
	 * Creates the.
	 *
	 * @param thing the thing
	 * @param pathValue the path value
	 * @return the path
	 * @throws RDFParseException the RDF parse exception
	 * @throws UnsupportedRDFormatException the unsupported RD format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
				Optional<org.eclipse.rdf4j.model.IRI> reified = Models.getPropertyIRI( pathModel , edge, PATHQL.EDGE_REIFIED);  
				pathEdge = new Edge(source.get(),reified.get(), reification.get(),  predicate.get(), target.get(), isDirect,isDereified);
			}else {
				pathEdge = new Edge(source.get(), predicate.get(), target.get(), isDirect);
			}
			path.add(pathEdge)	;
		}		
		return path;	
		
	}
	
	/**
	 * Instantiates a new path.
	 *
	 * @param value the value
	 */
	protected Path(Value value) {
		super(value);
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {
	
		return null;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String pathString = "Path=[";
		for ( Edge edge : path) {
			pathString += edge.toString() +"\r\n";
		};
		return pathString+"]\r\n";
	}

	/**
	 * Adds the.
	 *
	 * @param pathEdge the path edge
	 */
	public void add(Edge pathEdge) {
		path.add(pathEdge);
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public Resource getFact(String predicatePattern, Value... bindValues ) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public ResourceResults getFacts(String predicatePattern, Value...bindValues) throws PathPatternException {
		return null;
	}

	/**
	 * Iterator.
	 *
	 * @return the iterator
	 */
	@Override
	public Iterator<Edge> iterator() {
		return path.iterator();
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}	
} 
