package com.inova8.intelligentgraph.path;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;

import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.pathql.element.Variable;


public class UnionBinding extends EdgeBinding{
	EdgeBinding leftEdgeBinding;


	EdgeBinding rightEdgeBinding;

	public UnionBinding(EdgeBinding leftEdgeBinding, EdgeBinding rightEdgeBinding) {
		super();
		this.leftEdgeBinding = leftEdgeBinding;
		this.rightEdgeBinding = rightEdgeBinding;
	}

	@Override
	public Variable getSourceVariable() {
		return null;
	}

	@Override
	public Variable getPredicateVariable() {
		return null;
	}

	@Override
	public Variable getTargetVariable() {
		return null;
	}

	@Override
	public Direction getDirection() {
		return null;
	}

	@Override
	public Boolean getIsDereified() {
		return null;
	}

	@Override
	public IRI getReification() {
		return null;
	}
	@Override
	public void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset) {
		//TODO which one to use as binding, left or right?
		rightEdgeBinding.addEdgeToPathModel(builder, bindingset);
		
	}
}
