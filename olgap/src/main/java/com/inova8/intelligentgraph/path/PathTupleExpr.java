/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

import com.inova8.pathql.element.Variable;

/**
 * The Class PathTupleExpr.
 */
public class PathTupleExpr {
	
	/** The tuple expr. */
	private TupleExpr tupleExpr;
	
	/** The path binding. */
	private PathBinding pathBinding= new PathBinding();
	
	/** The statement binding. */
	private StatementBinding statementBinding ;	
	
	private Variable boundVariable;

	/**
	 * Instantiates a new path tuple expr.
	 *
	 * @param tupleExpr the tuple expr
	 */
	public PathTupleExpr(TupleExpr tupleExpr) {
		super();
		this.tupleExpr = tupleExpr;
	}
	
	/**
	 * Gets the tuple expr.
	 *
	 * @return the tuple expr
	 */
	public TupleExpr getTupleExpr() {
		return tupleExpr;
	}
	
	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public PathBinding getPath() {
		return pathBinding;
	}
	
	/**
	 * Sets the path.
	 *
	 * @param pathBinding the new path
	 */
	public void  setPath(PathBinding pathBinding) {
		this.pathBinding= pathBinding;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString(){
		return getTupleExpr().toString();
	}
	
	/**
	 * Sets the tuple expr.
	 *
	 * @param tupleExpr the new tuple expr
	 */
	public void setTupleExpr(TupleExpr tupleExpr) {
		this.tupleExpr = tupleExpr;
	}
	
	/**
	 * Path to string.
	 *
	 * @return the object
	 */
	public Object pathToString() {
		return getPath().toString();
	}
	
	/**
	 * Sets the statement binding.
	 *
	 * @param statementBinding the new statement binding
	 */
	public void setStatementBinding(StatementBinding statementBinding) {
		this.statementBinding = statementBinding;
	}
	
	/**
	 * Gets the statement binding.
	 *
	 * @return the statement binding
	 */
	public StatementBinding getStatementBinding() {
		return statementBinding;
	}

	public Variable getBoundVariable() {
		return boundVariable;
	}

	public void setBoundVariable(Variable boundVariable) {
		this.boundVariable = boundVariable;
	}
	
}
