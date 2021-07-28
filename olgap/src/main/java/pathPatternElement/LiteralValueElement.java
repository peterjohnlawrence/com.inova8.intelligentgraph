/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.Literal;

import pathCalc.CustomQueryOptions;
import pathPatternProcessor.PathConstants;
import pathQLRepository.PathQLRepository;

/**
 * The Class LiteralValueElement.
 */
public class LiteralValueElement extends ObjectElement {
	
	Literal literal;


	public LiteralValueElement(PathQLRepository source) {
		super(source);
		operator =PathConstants.Operator.LITERAL;
	}
	public Literal getLiteral() {
		return literal;
	}
	public Literal getLiteral(CustomQueryOptions customQueryOptions) {
		return literal;
	}
	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	
	public String toString() {
		return getLiteral().stringValue();
	}
	
	public String toSPARQL() {

		return  "'"+  getLiteral().stringValue() + "'" ;
	}
	
	@Override
	public String toHTML() {

		return getLiteral().stringValue();
	}
}
