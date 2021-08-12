/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;

/**
 * The Class StatementCollector.
 */
public class StatementCollector extends AbstractQueryModelVisitor<RuntimeException> {

	/**
	 * Process.
	 *
	 * @param node the node
	 * @return the statement collector
	 */
	public static StatementCollector process(QueryModelNode node) {
		StatementCollector collector = new StatementCollector();
		node.visit(collector);
		return collector;
	}

	/** The statement patterns. */
	private List<StatementPattern> statementPatterns = new ArrayList<>();
	
	/** The object vars. */
	private List<Var> objectVars = new ArrayList<>();

	/**
	 * Gets the statement patterns.
	 *
	 * @return the statement patterns
	 */
	public List<StatementPattern> getStatementPatterns() {
		return statementPatterns;
	}
	
	/**
	 * Gets the object vars.
	 *
	 * @return the object vars
	 */
	public List<Var> getObjectVars() {
		return objectVars;
	}

	/**
	 * Meet.
	 *
	 * @param node the node
	 */
	@Override
	public void meet(StatementPattern node) {
		objectVars.add(node.getObjectVar());
		statementPatterns.add(node);		 		
	}
}

