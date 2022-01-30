/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.dashjoin;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class Steps.
 */
public class Steps extends ArrayList<Step>{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
	String stepsString = "[\r\n" ; 
	for ( Step step:this) {
		stepsString += step.toString() + "\t,";
	};
	return StringUtils.chop(stepsString) +"]";	
}
}
