package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.pathql.context.RepositoryContext;

import org.eclipse.rdf4j.model.IRI;

public class BindVariableElement extends ObjectElement {
	Integer bindVariableIndex;

	public BindVariableElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
	}

	public Integer getBindVariableIndex() {
		return bindVariableIndex;
	}

	public void setBindVariableIndex(Integer bindVariableIndex) {
		this.bindVariableIndex = bindVariableIndex;
	}

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

	public String toString() {
		return IntelligentGraphConstants.BINDVARIABLEPREFIX + bindVariableIndex.toString();
	}
}
