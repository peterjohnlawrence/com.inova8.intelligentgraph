package pathPatternElement;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants.EdgeCode;

public class ValueElement extends PathElement {
	public ValueElement() {
		super();
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public
	String toSPARQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public
	String toHTML() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getIsNegated() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setIsNegated(Boolean isDereified) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		// TODO Auto-generated method stub
		return null;
	}



}
