/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.IRI;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;

/**
 * The Class IriRefValueElement.
 */
public class IriRefValueElement extends ObjectElement {
	
	/** The iri. */
	IRI iri;
	

	/**
	 * Instantiates a new iri ref value element.
	 *
	 * @param repositoryContext the repository context
	 */
	public IriRefValueElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.IRIREF;
	}
	
	/**
	 * Gets the iri.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the iri
	 */
	public IRI getIri(CustomQueryOptions customQueryOptions) {
		return iri;
	}

	/**
	 * Gets the iri.
	 *
	 * @return the iri
	 */
	public IRI getIri() {
		return iri;
	}
	
	/**
	 * Sets the iri.
	 *
	 * @param iri the new iri
	 */
	public void setIri(IRI iri) {
		this.iri = iri;
	}
	

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return  "<"+ iri.stringValue() +">";
	}
	

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {

		return "<"+ iri.stringValue() +">";
	}
	

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {

		return "<"+ iri.stringValue() +">";
	}
}
