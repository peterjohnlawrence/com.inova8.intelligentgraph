/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class QueryException.
 */
public class QueryException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new query exception.
	 *
	 * @param code the code
	 */
	public QueryException(String code) {
		super(code);
	}
	
	/**
	 * Instantiates a new query exception.
	 *
	 * @param code the code
	 * @param message the message
	 */
	public QueryException(String code,String message) {
		super(code,message);
	}

}
