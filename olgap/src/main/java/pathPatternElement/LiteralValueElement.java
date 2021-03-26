package pathPatternElement;

import org.eclipse.rdf4j.model.Literal;

import pathPatternProcessor.PathConstants;

public class LiteralValueElement extends ObjectElement {
	Literal literal;
	public LiteralValueElement() {
		super();
		operator =PathConstants.Operator.LITERAL;
	}


	public Literal getLiteral() {
		return literal;
	}

	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	public String toString() {
		return literal.stringValue();
	}
	public String toSPARQL() {

		return  "'"+  literal.stringValue() + "'" ;
	}
	@Override
	public String toHTML() {

		return literal.stringValue();
	}
}
