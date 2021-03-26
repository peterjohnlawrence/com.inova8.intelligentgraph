package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathQLModel.Resource;

public class PathResults extends ResourceResults{
	public PathResults(CloseableIteration<BindingSet, QueryEvaluationException> pathSet,Thing thing, PathElement pathElement  ) {
		super(pathSet, thing);
		this.pathElement = pathElement;
	}

	@Override
	public Resource nextResource() {
		BindingSet next = getResourceSet().next();
		Value factValue = next.getValue(getPathElement().getTargetVariable().getName());
		if (factValue == null)
			return null;
		else {
			Value factPredicate = next.getValue(getPathElement().getTargetPredicate().getName());
			Value factThing = next.getValue(getPathElement().getTargetSubject().getName());
			Thing thing = getThing().getSource().thingFactory(this.getTracer(), factThing, this.getStack(),
					this.getCustomQueryOptions(), this.getPrefixes());
			return thing.processFactObjectValue((IRI) factPredicate, null, factValue);
		} 
	}


}
