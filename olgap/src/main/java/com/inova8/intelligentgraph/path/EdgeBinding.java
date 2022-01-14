package com.inova8.intelligentgraph.path;


import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;

import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.pathql.element.Variable;


public abstract class EdgeBinding {

	public abstract Variable getSourceVariable();

	public abstract Variable getPredicateVariable();

	public abstract Variable getTargetVariable();

	public abstract Direction getDirection();

	public abstract Boolean getIsDereified();

	public abstract IRI getReification();

	public abstract void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset);

}
