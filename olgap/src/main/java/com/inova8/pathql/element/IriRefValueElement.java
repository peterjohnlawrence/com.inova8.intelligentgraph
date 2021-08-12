/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.IRI;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.pathql.processor.PathConstants;

/**
 * The Class IriRefValueElement.
 */
public class IriRefValueElement extends ObjectElement {
	
	/** The iri. */
	IRI iri;
	

	public IriRefValueElement(IntelligentGraphRepository source) {
		super(source);
		operator=PathConstants.Operator.IRIREF;
	}
	public IRI getIri(CustomQueryOptions customQueryOptions) {
		return iri;
	}

	public IRI getIri() {
		return iri;
	}
	public void setIri(IRI iri) {
		this.iri = iri;
	}
	

	public String toString() {
		return  "<"+ iri.stringValue() +">";
	}
	

	public String toSPARQL() {

		return "<"+ iri.stringValue() +">";
	}
	

	@Override
	public String toHTML() {

		return "<"+ iri.stringValue() +">";
	}
}
