package pathPatternElement;

import org.eclipse.rdf4j.model.IRI;

import pathPatternProcessor.PathConstants;

public class IriRefValueElement extends ObjectElement {
	IRI iri;
	public IriRefValueElement() {
		super();
		operator=PathConstants.Operator.IRIREF;
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
