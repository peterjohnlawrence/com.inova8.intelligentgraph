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
 * The Class UnionBinding.
 */
public class UnionBinding extends EdgeBinding{
	
	/** The left edge binding. */
	EdgeBinding leftEdgeBinding;


	/** The right edge binding. */
	EdgeBinding rightEdgeBinding;

	/**
	 * Instantiates a new union binding.
	 *
	 * @param leftEdgeBinding the left edge binding
	 * @param rightEdgeBinding the right edge binding
	 */
	public UnionBinding(EdgeBinding leftEdgeBinding, EdgeBinding rightEdgeBinding) {
		super();
		this.leftEdgeBinding = leftEdgeBinding;
		this.rightEdgeBinding = rightEdgeBinding;
	}

	/**
	 * Gets the source variable.
	 *
	 * @return the source variable
	 */
	@Override
	public Variable getSourceVariable() {
		return null;
	}

	/**
	 * Gets the predicate variable.
	 *
	 * @return the predicate variable
	 */
	@Override
	public Variable getPredicateVariable() {
		return null;
	}

	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	@Override
	public Variable getTargetVariable() {
		return null;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	@Override
	public Direction getDirection() {
		return null;
	}

	/**
	 * Gets the checks if is dereified.
	 *
	 * @return the checks if is dereified
	 */
	@Override
	public Boolean getIsDereified() {
		return null;
	}

	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	@Override
	public IRI getReification() {
		return null;
	}
	
	/**
	 * Adds the edge to path model.
	 *
	 * @param builder the builder
	 * @param bindingset the bindingset
	 */
	@Override
	public void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset) {
		//TODO which one to use as binding, left or right?
		rightEdgeBinding.addEdgeToPathModel(builder, bindingset);
		
	}
}
