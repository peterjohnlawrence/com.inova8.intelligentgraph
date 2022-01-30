/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.pathql.context.RepositoryContext;

import org.eclipse.rdf4j.model.IRI;

/**
 * The Class BindVariableElement.
 */
public class BindVariableElement extends ObjectElement {
	
	/** The bind variable index. */
	Integer bindVariableIndex;

	/**
	 * Instantiates a new bind variable element.
	 *
	 * @param repositoryContext the repository context
	 */
	public BindVariableElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
	}

	/**
	 * Gets the bind variable index.
	 *
	 * @return the bind variable index
	 */
	public Integer getBindVariableIndex() {
		return bindVariableIndex;
	}

	/**
	 * Sets the bind variable index.
	 *
	 * @param bindVariableIndex the new bind variable index
	 */
	public void setBindVariableIndex(Integer bindVariableIndex) {
		this.bindVariableIndex = bindVariableIndex;
	}

	/**
	 * Gets the literal.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the literal
	 */
	public Literal getLiteral(CustomQueryOptions customQueryOptions) {
		if (customQueryOptions != null) {
			Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
			if (rdfLiteral != null)
				return (Literal) rdfLiteral.getSuperValue();
			else
				return (Literal) null;
		} else
			return (Literal) null;
	}

	/**
	 * Gets the iri.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the iri
	 */
	public IRI getIri(CustomQueryOptions customQueryOptions) {
		if (customQueryOptions != null) {
			Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
			if (rdfLiteral != null)
				return (IRI) rdfLiteral.getSuperValue();
			else
				return (IRI) null;
		} else
			return (IRI) null;
	}

	/**
	 * Gets the value.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the value
	 */
	public Value getValue(CustomQueryOptions customQueryOptions) {
		if (customQueryOptions != null) {
			Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
			if (rdfLiteral != null)
				return rdfLiteral.getSuperValue();
			else
				return (Value) null;
		} else
			return (Value) null;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		return IntelligentGraphConstants.BINDVARIABLEPREFIX + bindVariableIndex.toString();
	}
}
