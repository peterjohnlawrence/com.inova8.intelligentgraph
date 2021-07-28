package pathPatternElement;

import org.eclipse.rdf4j.model.Literal;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathQLModel.Resource;

import static org.eclipse.rdf4j.model.util.Values.literal;
import pathQLRepository.PathQLRepository;

public class BindVariableElement extends LiteralValueElement {
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
	//	return literal(Evaluator.BINDVARIABLEPREFIX+bindVariableIndex.toString());
	}
	public String toString() {
		return Evaluator.BINDVARIABLEPREFIX+bindVariableIndex.toString();
	}
}
