/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.pathql.processor.PathPatternException;

/**
 * The Class NullResource.
 */
@Deprecated
public class NullResource extends Resource {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new null resource.
	 */
	public NullResource() {
		super(null);
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	@Override
	public ResourceResults getFacts(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
