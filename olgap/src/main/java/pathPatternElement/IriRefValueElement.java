/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.IRI;

import pathPatternProcessor.PathConstants;
import pathQLRepository.PathQLRepository;

/**
 * The Class IriRefValueElement.
 */
public class IriRefValueElement extends ObjectElement {
	
	/** The iri. */
	IRI iri;
	
	/**
	 * Instantiates a new iri ref value element.
	 */
	public IriRefValueElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.IRIREF;
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
