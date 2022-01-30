/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.Literal;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;

/**
 * The Class LiteralValueElement.
 */
public class LiteralValueElement extends ObjectElement {
	
	/** The literal. */
	Literal literal;


	/**
	 * Instantiates a new literal value element.
	 *
	 * @param repositoryContext the repository context
	 */
	public LiteralValueElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
	 * Gets the literal.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the literal
	 */
	public Literal getLiteral(CustomQueryOptions customQueryOptions) {
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
		return getLiteral().stringValue();
	}
	
	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {

		return  "'"+  getLiteral().stringValue() + "'" ;
	}
	
	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {

		return getLiteral().stringValue();
	}
}
