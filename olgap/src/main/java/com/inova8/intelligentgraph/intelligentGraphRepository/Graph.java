/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.intelligentGraphRepository;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathPatternException;

import org.antlr.v4.runtime.RecognitionException;
import static org.eclipse.rdf4j.model.util.Values.iri;


public class Graph {
	public static final IRI DEFAULTGRAPH = iri("http://default");

 IntelligentGraphRepository source;
 

 IRI graphName;
 Boolean isPrivate=true;

public Graph(IntelligentGraphRepository source, IRI graphName) {
	super();
	this.source = source;
	this.graphName = graphName;
}


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
 
public Thing getThing(String thing) throws RecognitionException, PathPatternException {
	IRI thingIri = PathParser.parseIriRef(source,thing).getIri();
	return getThing(thingIri);
}
 public Thing getThing(IRI thing) {
	 return Thing.create(this.getSource(),this.getGraphName(),  (Value)thing, new EvaluationContext());
 }
 @Deprecated
 public Thing getThing(IRI thing,CustomQueryOptions customQueryOptions) {
	 return Thing.create(this.getSource(),this.getGraphName(),  (Value)thing, new EvaluationContext(customQueryOptions));
 }
 @Deprecated
 public Thing getThing(String thing, CustomQueryOptions customQueryOptions) throws RecognitionException, PathPatternException {
		IRI thingIri = PathParser.parseIriRef(source,thing).getIri();
		return getThing(thingIri, customQueryOptions);
	}

@SuppressWarnings("deprecation")
public Boolean closeGraph() {
	return source.getPublicContexts().remove(graphName);
}
}
