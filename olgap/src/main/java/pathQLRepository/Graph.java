/*
 * inova8 2020
 */
package pathQLRepository;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.antlr.v4.runtime.RecognitionException;
import static org.eclipse.rdf4j.model.util.Values.iri;
import pathCalc.CustomQueryOptions;
import pathCalc.EvaluationContext;
import pathCalc.Thing;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;


public class Graph {
	public static final IRI DEFAULTGRAPH = iri("http://default");

 PathQLRepository source;
 

 IRI graphName;
 Boolean isPrivate=true;

public Graph(PathQLRepository source, IRI graphName) {
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
public PathQLRepository getSource() {
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

public Boolean closeGraph() {
	return source.getPublicContexts().remove(graphName);
}
}
