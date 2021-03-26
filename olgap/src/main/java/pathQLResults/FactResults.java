package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathQLModel.Fact;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

public class FactResults extends ResourceResults {

	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, Thing thing,
			PathElement pathElement) {
		super(factSet, thing, pathElement);
	}
	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, PathQLRepository source,
			PathElement pathElement) {
		super(factSet, source, pathElement);
	}
	@Override
	public Resource next() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		return calculateValue(next);
	}

	private Resource calculateValue(BindingSet next) {
		Value factValue = next.getValue(getPathElement().getTargetVariable().getName());
		if (factValue == null)
			return null;
		else if(factValue.isIRI()) {
			return new Thing(getThing().getSource(),factValue, getThing().getCustomQueryOptions());
		}
		else {
			Value factPredicate = next.getValue(getPathElement().getTargetPredicate().getName());
			Value factThing = next.getValue(getPathElement().getTargetSubject().getName());
			Thing thing = getSource().thingFactory(this.getTracer(), factThing, this.getStack(),
					this.getCustomQueryOptions(), this.getPrefixes());
			return thing.processFactObjectValue((IRI) factPredicate, null, factValue);
		}
	}
	@Override
	public Resource nextResource() throws QueryEvaluationException {
		BindingSet next = getResourceSet().next();
		Resource objectValue = calculateValue(next);
		return new Fact(next.getBinding(getPathElement().getTargetSubject().getName()).getValue(),
				next.getBinding(getPathElement().getTargetPredicate().getName()).getValue(),
				objectValue.getValue());

	}
}
