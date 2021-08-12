/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import java.util.ArrayList;

/**
 * The Class Path.
 */
public class PathBinding extends ArrayList<EdgeBinding>  {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String pathString = "";
		for ( EdgeBinding edge : this) {
			pathString += edge.toString() +"\r\n";
		};
		return pathString;
	}
} 
