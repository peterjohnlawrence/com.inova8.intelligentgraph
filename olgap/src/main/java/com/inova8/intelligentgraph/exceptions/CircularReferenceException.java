/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class CircularReferenceException.
 */
public class CircularReferenceException extends HandledException{
	
	/** The Constant CIRCULARREFERENCE_EXCEPTION. */
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new circular reference exception.
	 *
	 * @param message the message
	 */
	public CircularReferenceException( String message) {
		super(CIRCULARREFERENCE_EXCEPTION, message);
	}


}
