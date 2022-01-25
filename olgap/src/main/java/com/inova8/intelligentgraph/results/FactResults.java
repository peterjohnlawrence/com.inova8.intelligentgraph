/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.results;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.IntelligentEvaluator;
import com.inova8.intelligentgraph.exceptions.HandledException;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Fact;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.pathql.element.PathElement;


/**
 * The Class FactResults.
 */
public class FactResults extends ResourceBindingSetResults {

	/**
	 * Instantiates a new fact results.
	 *
	 * @param factSet the fact set
	 * @param thing the thing
	 * @param pathElement the path element
	 */
	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, Thing thing,
			PathElement pathElement,CustomQueryOptions customQueryOptions) {
		super(factSet, thing, pathElement,customQueryOptions);
	}
	
	/**
	 * Instantiates a new fact results.
	 *
	 * @param factSet the fact set
	 * @param source the source
	 * @param pathElement the path element
	 */
	public FactResults(CloseableIteration<BindingSet, QueryEvaluationException> factSet, IntelligentGraphRepository source,
			PathElement pathElement,CustomQueryOptions customQueryOptions) {
		super(factSet, source, pathElement,customQueryOptions);
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
	 * @throws HandledException 
	 */
	private Resource calculateValue(BindingSet next) throws QueryEvaluationException {
		Value factValue = next.getValue(getPathElement().getTargetVariable().getName());
		if (factValue == null)
			return null;
		else if(factValue.isIRI()) {
			Thing thing = Thing.create(getSource(),factValue, this.getEvaluationContext());
			return thing;
		}
		else {
			//No script string should ever get here without having been processed first, so treat them all as literals
			//TODO somehow there are paths that get directly here:-(
//			return  Resource.create(getSource(), factValue, this.getEvaluationContext());
			Value factPredicate = next.getValue(getPathElement().getTargetPredicate().getName());
			Value factThing = next.getValue(getPathElement().getTargetSubject().getName());
			//Thing thing = Thing.create(getSource(),this.getThing().getGraphName(), factThing ,this.getEvaluationContext());
			Thing thing = Thing.create(getSource(),factThing ,this.getEvaluationContext());
			//TODO return thing.processFactObjectValue((IRI) factPredicate,factValue,this.customQueryOptions);
			return IntelligentEvaluator.processFactObjectValue(thing, (IRI) factPredicate,factValue,this.customQueryOptions);
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
		PathBinding path = new PathBinding();
		path = pathElement.visitPathBinding(path,0);
//		for(Edge edge:path) {
			
//TODO
//			Value sourceValue = next.getBinding(edge.getSourceVariable().getName()).getValue();
//			Value predicateValue = next.getBinding(edge.getPredicateVariable().getName()).getValue();
//			Value reificationValue = edge.getReification();
//			Value targetValue = next.getBinding(edge.getTargetVariable().getName()).getValue();
//			Boolean direction = edge.isInverse();
//			Boolean isDereified = edge.getIsDereified();
//			Fact edgeFact = new Fact(sourceValue, predicateValue, targetValue);
			
//		}
		Resource objectValue = calculateValue(next);
		return new Fact(next.getBinding(getPathElement().getTargetSubject().getName()).getValue(),
				next.getBinding(getPathElement().getTargetPredicate().getName()).getValue(),
				objectValue.getValue());

	}
}
