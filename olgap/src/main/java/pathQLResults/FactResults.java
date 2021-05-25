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
import pathPatternElement.Edge;
import pathPatternElement.Path;
import pathPatternElement.PathElement;
import pathQLModel.Fact;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

/**
 * The Class FactResults.
 */
public class FactResults extends ResourceResults {

	/**
	 * Instantiates a new fact results.
	 *
	 * @param factSet the fact set
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, Thing thing,
			PathElement pathElement) {
		super(factSet, thing, pathElement);
	}
	
	/**
	 * Instantiates a new fact results.
	 *
	 * @param factSet the fact set
	 * @param source the source
	 * @param pathElement the path element
	 */
	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, PathQLRepository source,
			PathElement pathElement) {
		super(factSet, source, pathElement);
	}
	
	/**
	 * Next.  
	 *
	 * @return the resource
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public Resource next() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return calculateValue(next);
	}

	/**
	 * Calculate value.
	 *
	 * @param next the next
	 * @return the resource
	 */
	private Resource calculateValue(BindingSet next) {
		Value factValue = next.getValue(getPathElement().getTargetVariable().getName());
		if (factValue == null)
			return null;
		else if(factValue.isIRI()) {
			Thing thing = Thing.create(getSource(),factValue, this.getEvaluationContext());
			return thing;
		}
		else {
			Value factPredicate = next.getValue(getPathElement().getTargetPredicate().getName());
			Value factThing = next.getValue(getPathElement().getTargetSubject().getName());
			Thing thing = Thing.create(getSource(),this.getThing().getGraphName(), factThing ,this.getEvaluationContext());
			return thing.processFactObjectValue((IRI) factPredicate,factValue);
		}
	}
	
	/**
	 * Next resource.
	 *
	 * @return the resource
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public Resource nextResource() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		Path path = new Path();
		path = pathElement.visitPath(path);
		for(Edge edge:path) {
			
//TODO
//			Value sourceValue = next.getBinding(edge.getSourceVariable().getName()).getValue();
//			Value predicateValue = next.getBinding(edge.getPredicateVariable().getName()).getValue();
//			Value reificationValue = edge.getReification();
//			Value targetValue = next.getBinding(edge.getTargetVariable().getName()).getValue();
//			Boolean direction = edge.isInverse();
//			Boolean isDereified = edge.getIsDereified();
//			Fact edgeFact = new Fact(sourceValue, predicateValue, targetValue);
			
		}
		Resource objectValue = calculateValue(next);
		return new Fact(next.getBinding(getPathElement().getTargetSubject().getName()).getValue(),
				next.getBinding(getPathElement().getTargetPredicate().getName()).getValue(),
				objectValue.getValue());

	}
}
