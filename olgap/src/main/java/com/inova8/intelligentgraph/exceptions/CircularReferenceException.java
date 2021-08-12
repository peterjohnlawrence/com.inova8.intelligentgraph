/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class CircularReferenceException.
 */
public class CircularReferenceException extends HandledException{
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new circular reference exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public CircularReferenceException( String message) {
		super(CIRCULARREFERENCE_EXCEPTION, message);
	}


}
