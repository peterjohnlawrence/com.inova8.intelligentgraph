/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.algebra.ProjectionElemList;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;

/**
 * The Class ProjectionElemListCollector.
 */
public class ProjectionElemListCollector extends AbstractQueryModelVisitor<RuntimeException> {

	/**
	 * Process.
	 *
	 * @param node the node
	 * @return the list
	 */
	public static List<ProjectionElemList> process(QueryModelNode node) {
		ProjectionElemListCollector collector = new ProjectionElemListCollector();
		node.visit(collector);
		return collector.getProjections();
	}

	/** The projection elem lists. */
	private List<ProjectionElemList> projectionElemLists = new ArrayList<>();

	/**
	 * Gets the projections.
	 *
	 * @return the projections
	 */
	public List<ProjectionElemList> getProjections() {
		return projectionElemLists;
	}

	/**
	 * Meet.
	 *
	 * @param node the node
	 */
	@Override
	public void meet(ProjectionElemList node) {
		projectionElemLists.add(node);		 		
	}
}

