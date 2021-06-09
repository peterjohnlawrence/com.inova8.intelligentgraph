/*
 * inova8 2020
 */
package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathQLModel.Resource;

/**
 * The Class PathResults.
 */
public class PathResults extends ResourceBindingSetResults{
	
	/**
	 * Instantiates a new path results.
	 *
	 * @param pathSet the path set
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public PathResults(CloseableIteration<BindingSet, QueryEvaluationException> pathSet,Thing thing, PathElement pathElement  ) {
		super(pathSet, thing);
		this.pathElement = pathElement;
	}

	/**
	 * Next resource.
	 *
	 * @return the resource
	 */
	@Override
	public Resource nextResource() {
		BindingSet next = getResourceSet().next();
		Value factValue = next.getValue(getPathElement().getTargetVariable().getName());
		if (factValue == null)
			return null;
		else {
			Value factPredicate = next.getValue(getPathElement().getTargetPredicate().getName());
			Value factThing = next.getValue(getPathElement().getTargetSubject().getName());
			Thing thing = Thing.create(getThing().getSource(), factThing,this.getEvaluationContext());
			return thing.processFactObjectValue((IRI) factPredicate,  factValue,this.getEvaluationContext().getCustomQueryOptions());
		} 
	}


}
