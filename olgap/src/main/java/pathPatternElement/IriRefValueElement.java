/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.IRI;

import pathCalc.CustomQueryOptions;
import pathPatternProcessor.PathConstants;
import pathQLRepository.PathQLRepository;

/**
 * The Class IriRefValueElement.
 */
public class IriRefValueElement extends ObjectElement {
	
	/** The iri. */
	IRI iri;
	

	public IriRefValueElement(PathQLRepository source) {
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
