/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathQLModel;

import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.pathQLResults.ResourceResults;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.processor.PathPatternException;
@Deprecated
public class NullResource extends Resource {

	private static final long serialVersionUID = 1L;

	public NullResource() {
		super(null);
	}

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}

}
