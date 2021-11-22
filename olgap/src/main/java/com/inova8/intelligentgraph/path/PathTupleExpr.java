package com.inova8.intelligentgraph.path;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

public class PathTupleExpr {
	private TupleExpr tupleExpr;
	private PathBinding pathBinding= new PathBinding();
	private StatementBinding statementBinding ;	

	public PathTupleExpr(TupleExpr tupleExpr) {
		super();
		this.tupleExpr = tupleExpr;
	}
	public TupleExpr getTupleExpr() {
		return tupleExpr;
	}
	public PathBinding getPath() {
		return pathBinding;
	}
	public void  setPath(PathBinding pathBinding) {
		this.pathBinding= pathBinding;
	}
	public String toString(){
		return getTupleExpr().toString();
	}
	public void setTupleExpr(TupleExpr tupleExpr) {
		this.tupleExpr = tupleExpr;
	}
	public Object pathToString() {
		return getPath().toString();
	}
	public void setStatementBinding(StatementBinding statementBinding) {
		this.statementBinding = statementBinding;
	}
	public StatementBinding getStatementBinding() {
		return statementBinding;
	}
}
