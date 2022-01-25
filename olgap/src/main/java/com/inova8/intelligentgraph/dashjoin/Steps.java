package com.inova8.intelligentgraph.dashjoin;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Steps extends ArrayList<Step>{
	private static final long serialVersionUID = 1L;

	public String toString() {
	String stepsString = "[\r\n" ; 
	for ( Step step:this) {
		stepsString += step.toString() + "\t,";
	};
	return StringUtils.chop(stepsString) +"]";	
}
}
