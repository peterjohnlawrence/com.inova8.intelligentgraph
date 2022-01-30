/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.dashjoin;

import java.util.HashMap;

import com.inova8.intelligentgraph.path.Edge;
import com.inova8.intelligentgraph.path.Path;

/**
 * The Class PathSteps.
 */
public class PathSteps extends HashMap<String, Object>{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new path steps.
	 *
	 * @param path the path
	 */
	public  PathSteps(Path path){
		Steps steps = new Steps();
		Boolean first =true;
		for (Edge edge : path) {
			if(first)this.put("start", new Resource(edge.getSource()));
			first = false;
			steps.add(new Step(edge));
		}
		this.put("steps", steps);

	}

/**
 * To string.
 *
 * @return the string
 */
public String toString() {
	String pathStepsString = "[\r\n";
	for ( String key : this.keySet()) {
		pathStepsString += key.toString() +":" + this.get(key).toString()+";\r\n";
	};
	return pathStepsString+"]\r\n";
	
}

}
