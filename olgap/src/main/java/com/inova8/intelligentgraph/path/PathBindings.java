/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import java.util.LinkedHashMap;

import com.inova8.pathql.element.PathElement;

/**
 * The Class PathBindings.
 */
public class PathBindings extends LinkedHashMap<Integer,PathBinding>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates the.
	 *
	 * @param element the element
	 * @return the path bindings
	 */
	public static PathBindings create(PathElement element) {
		PathBindings pathBindings = new PathBindings();
		PathBinding pathBinding;
		for(Integer iteration=0;iteration<element.getIterations().size(); iteration++) {
			pathBinding = new PathBinding();
			pathBindings.put(iteration, element.visitPathBinding(pathBinding, iteration));
		}
		return pathBindings;
	}
}
