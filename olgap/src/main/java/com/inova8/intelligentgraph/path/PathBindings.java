package com.inova8.intelligentgraph.path;

import java.util.LinkedHashMap;

import com.inova8.pathql.element.PathElement;

public class PathBindings extends LinkedHashMap<Integer,PathBinding>{

	private static final long serialVersionUID = 1L;
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
