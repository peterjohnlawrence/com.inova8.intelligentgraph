package pathPatternElement;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathQLModel.Resource;

import org.eclipse.rdf4j.model.IRI;

import pathQLRepository.PathQLRepository;

public class BindVariableElement extends ObjectElement {
	Integer bindVariableIndex;
	public BindVariableElement(PathQLRepository source) {
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
		return Evaluator.BINDVARIABLEPREFIX+bindVariableIndex.toString();
	}
}
