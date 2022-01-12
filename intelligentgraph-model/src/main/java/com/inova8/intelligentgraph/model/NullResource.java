/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.reference.PredicateElement;
import com.inova8.intelligentgraph.results.FactResults;
@Deprecated
public class NullResource extends Resource {

	private static final long serialVersionUID = 1L;

	public NullResource() {
		super(null);
	}

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactResults getFacts(String predicatePattern, Value... bindValues)  {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
