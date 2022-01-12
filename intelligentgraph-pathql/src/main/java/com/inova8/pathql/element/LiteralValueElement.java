/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.Literal;

import com.inova8.intelligentgraph.constants.PathConstants;
import com.inova8.intelligentgraph.model.CustomQueryOptions;
import com.inova8.intelligentgraph.repositoryContext.RepositoryContext;

/**
 * The Class LiteralValueElement.
 */
public class LiteralValueElement extends ObjectElement {
	
	Literal literal;


//	public LiteralValueElement(IntelligentGraphRepository source) {
//		super(source);
//		operator =PathConstants.Operator.LITERAL;
//	}
	public LiteralValueElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.LITERAL ;
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
