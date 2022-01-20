/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.pathql.processor.PathPatternException;

/**
 * The Class Literal.
 */
public class Literal extends Resource {

	private static final long serialVersionUID = 1L;

	public Literal(Value value) {
		super(value);
	}
	@Override
	public 	boolean isLiteral(){
		return true;
	}

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(String predicatePattern,Value... bindValues) throws PathPatternException {
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
