/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;

import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.pathql.element.Variable;


/**
 * The Class EdgeBinding.
 */
public abstract class EdgeBinding {

	/**
	 * Gets the source variable.
	 *
	 * @return the source variable
	 */
	public abstract Variable getSourceVariable();

	/**
	 * Gets the predicate variable.
	 *
	 * @return the predicate variable
	 */
	public abstract Variable getPredicateVariable();

	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	public abstract Variable getTargetVariable();

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public abstract Direction getDirection();

	/**
	 * Gets the checks if is dereified.
	 *
	 * @return the checks if is dereified
	 */
	public abstract Boolean getIsDereified();

	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	public abstract IRI getReification();

	/**
	 * Adds the edge to path model.
	 *
	 * @param builder the builder
	 * @param bindingset the bindingset
	 */
	public abstract void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset);

}
