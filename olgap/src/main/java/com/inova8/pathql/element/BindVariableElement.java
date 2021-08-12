package com.inova8.pathql.element;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Resource;

import org.eclipse.rdf4j.model.IRI;

public class BindVariableElement extends ObjectElement {
	Integer bindVariableIndex;
	public BindVariableElement(IntelligentGraphRepository source) {
		super(source);
	}
	public Integer getBindVariableIndex() {
		return bindVariableIndex;
	}
	public void setBindVariableIndex(Integer bindVariableIndex) {
		this.bindVariableIndex = bindVariableIndex;
	}
	public Literal getLiteral(CustomQueryOptions customQueryOptions) {
		Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
		return (Literal) rdfLiteral.getSuperValue();
	}
	public IRI getIri(CustomQueryOptions customQueryOptions) {
		Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
		return (IRI) rdfLiteral.getSuperValue();
	}
	public Value getValue(CustomQueryOptions customQueryOptions) {
		Resource rdfLiteral = customQueryOptions.get(bindVariableIndex.toString());
		return  rdfLiteral.getSuperValue();
	}
	public String toString() {
		return IntelligentGraphConstants.BINDVARIABLEPREFIX+bindVariableIndex.toString();
	}
}
