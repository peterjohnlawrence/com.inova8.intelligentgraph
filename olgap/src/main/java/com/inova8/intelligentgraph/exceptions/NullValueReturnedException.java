/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class NullValueReturnedException.
 */
public class NullValueReturnedException extends HandledException{
	private static final String NULLVALUERETURNED_EXCEPTION = "NullValueReturned";
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new null value returned exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public NullValueReturnedException( String message) {
		super(NULLVALUERETURNED_EXCEPTION, message);
	}


}
