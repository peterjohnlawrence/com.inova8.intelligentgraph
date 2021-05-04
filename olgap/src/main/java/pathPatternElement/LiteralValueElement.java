/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.Literal;

import pathPatternProcessor.PathConstants;
import pathQLRepository.PathQLRepository;

/**
 * The Class LiteralValueElement.
 */
public class LiteralValueElement extends ObjectElement {
	
	/** The literal. */
	Literal literal;
	
	/**
	 * Instantiates a new literal value element.
	 */
	public LiteralValueElement(PathQLRepository source) {
		super(source);
		operator =PathConstants.Operator.LITERAL;
	}


	/**
	 * Gets the literal.
	 *
	 * @return the literal
	 */
	public Literal getLiteral() {
		return literal;
	}

	/**
	 * Sets the literal.
	 *
	 * @param literal the new literal
	 */
	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return literal.stringValue();
	}
	
	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {

		return  "'"+  literal.stringValue() + "'" ;
	}
	
	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {

		return literal.stringValue();
	}
}
