package com.inova8.pathql.element;

import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

public class QueryOptionsPathElement extends PathElement{

	public QueryOptionsPathElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
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
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return null;
	}

	@Override
	public Boolean getIsNegated() {
		return null;
	}

	@Override
	public void setIsNegated(Boolean isDereified) {
	}

	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable, Variable predicateVariable,Variable targetVariable,
			Integer pathIteration, CustomQueryOptions customQueryOptions) {
		return null;
	}



}
