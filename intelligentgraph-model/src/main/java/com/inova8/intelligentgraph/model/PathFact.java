/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.eclipse.rdf4j.model.Value;


@Deprecated
public class PathFact extends Fact {

	private static final long serialVersionUID = 1L;

	enum Direction {
		forward, reverse
	};

	Direction direction;

	public PathFact(Resource subject, Predicate predicate, Value value, Direction direction) {
		super(subject, predicate, value);
		this.direction = direction;
	}
}
