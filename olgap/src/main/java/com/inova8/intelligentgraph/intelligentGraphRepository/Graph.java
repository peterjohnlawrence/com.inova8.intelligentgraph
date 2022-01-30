/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.intelligentGraphRepository;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathPatternException;

import org.antlr.v4.runtime.RecognitionException;
import static org.eclipse.rdf4j.model.util.Values.iri;


/**
 * The Class Graph.
 */
public class Graph {
	
	/** The Constant DEFAULTGRAPH. */
	public static final IRI DEFAULTGRAPH = iri("http://default");

 /** The source. */
 IntelligentGraphRepository source;
 

 /** The graph name. */
 IRI graphName;
 
 /** The is private. */
 Boolean isPrivate=true;

/**
 * Instantiates a new graph.
 *
 * @param source the source
 * @param graphName the graph name
 */
public Graph(IntelligentGraphRepository source, IRI graphName) {
	super();
	this.source = source;
	this.graphName = graphName;
}


/**
 * Gets the graph name.
 *
 * @return the graph name
 */
public IRI getGraphName() {
	return graphName;
}

/**
 * Gets the source.
 *
 * @return the source
 */
public IntelligentGraphRepository getSource() {
	return source;
}
 
/**
 * Gets the thing.
 *
 * @param thing the thing
 * @return the thing
 * @throws RecognitionException the recognition exception
 * @throws PathPatternException the path pattern exception
 */
public Thing getThing(String thing) throws RecognitionException, PathPatternException {
	IRI thingIri = PathParser.parseIriRef(source.getRepositoryContext(),thing).getIri();
	return getThing(thingIri);
}
 
 /**
  * Gets the thing.
  *
  * @param thing the thing
  * @return the thing
  */
 public Thing getThing(IRI thing) {
	 return Thing.create(this.getSource(),this.getGraphName(),  (Value)thing, new EvaluationContext());
 }
 
 /**
  * Gets the thing.
  *
  * @param thing the thing
  * @param customQueryOptions the custom query options
  * @return the thing
  */
 @Deprecated
 public Thing getThing(IRI thing,CustomQueryOptions customQueryOptions) {
	 return Thing.create(this.getSource(),this.getGraphName(),  (Value)thing, new EvaluationContext(customQueryOptions));
 }
 
 /**
  * Gets the thing.
  *
  * @param thing the thing
  * @param customQueryOptions the custom query options
  * @return the thing
  * @throws RecognitionException the recognition exception
  * @throws PathPatternException the path pattern exception
  */
 @Deprecated
 public Thing getThing(String thing, CustomQueryOptions customQueryOptions) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(source.getRepositoryContext(),thing).getIri();
		return getThing(thingIri, customQueryOptions);
	}

/**
 * Close graph.
 *
 * @return the boolean
 */
@SuppressWarnings("deprecation")
public Boolean closeGraph() {
	return source.getPublicContexts().remove(graphName);
}
}
