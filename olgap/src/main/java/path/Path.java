/*
 * inova8 2020
 */
package path;

import java.util.ArrayList;

/**
 * The Class Path.
 */
public class Path extends ArrayList<Edge>  {

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
		for ( Edge edge : this) {
			pathString += edge.toString() +"\r\n";
		};
		return pathString;
	}
} 
