package pathQLRepository;

import org.eclipse.rdf4j.model.IRI;
import org.antlr.v4.runtime.RecognitionException;

import pathCalc.Thing;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;

public class Graph {
 PathQLRepository source;
 IRI graphName;

public Graph(PathQLRepository source, IRI graphName) {
	super();
	this.source = source;
	this.graphName = graphName;
}

public IRI getGraphName() {
	return graphName;
}

public PathQLRepository getSource() {
	return source;
}
 public Thing getThing(IRI thing) {
	 return new Thing(this,  thing);
 }

public Thing getThing(String thing) throws RecognitionException, PathPatternException {
	IRI thingIri = PathParser.parseIriRef(source,thing).getIri();
	return getThing(thingIri);
}
public Boolean closeGraph() {
	return source.getPublicContexts().remove(graphName);
}
}
