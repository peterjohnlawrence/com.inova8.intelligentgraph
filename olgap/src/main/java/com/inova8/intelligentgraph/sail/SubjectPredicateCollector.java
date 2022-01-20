/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;

/**
 * The Class SubjectPredicateCollector.
 */
public class SubjectPredicateCollector extends AbstractQueryModelVisitor<RuntimeException> {

	/**
	 * Process.
	 *
	 * @param node the node
	 * @param name the name
	 * @return the list
	 */
	public static List<StatementPattern> process(QueryModelNode node, String name) {
		SubjectPredicateCollector collector = new SubjectPredicateCollector();
		bindingValueName=name;
		node.visit(collector);
		return collector.getStatementPatterns();
	}

	/** The st patterns. */
	private List<StatementPattern> stPatterns = new ArrayList<>();
	
	/** The binding value name. */
	private static String bindingValueName;

	/**
	 * Gets the statement patterns.
	 *
	 * @return the statement patterns
	 */
	public List<StatementPattern> getStatementPatterns() {
		return stPatterns;
	}

	/**
	 * Meet.
	 *
	 * @param node the node
	 */
	@Override
	public void meet(Filter node) {
		// Skip boolean constraints
		node.getArg().visit(this);
	}

	/**
	 * Meet.
	 *
	 * @param node the node
	 */
	@Override
	public void meet(StatementPattern node) {
		 Var objectVar = node.getObjectVar();
		 if(objectVar!=null) 
			 if(objectVar.getName().equals(bindingValueName) )		
					stPatterns.add(node);		 		
	}
}

