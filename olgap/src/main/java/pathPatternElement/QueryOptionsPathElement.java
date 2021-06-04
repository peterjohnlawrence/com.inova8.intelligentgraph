package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

public class QueryOptionsPathElement extends PathElement{

	public QueryOptionsPathElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.QUERYOPTIONS;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSPARQL() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toHTML() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		return null;
	}

	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		return null;
	}

	@Override
	public Boolean getIsNegated() {
		return null;
	}

	@Override
	public void setIsNegated(Boolean isDereified) {
	}



}
