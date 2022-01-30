/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

/**
 * The Class ServerException.
 */
public class ServerException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/**
	 * Instantiates a new server exception.
	 *
	 * @param code the code
	 * @param e the e
	 */
	public ServerException(String code, Exception e) {
		super(code, e);
	}


	/**
	 * Instantiates a new server exception.
	 *
	 * @param code the code
	 * @param message the message
	 * @param e the e
	 */
	public ServerException(String code, String message, Exception e) {
		super(code, message,e);
	}

	/**
	 * Instantiates a new server exception.
	 *
	 * @param code the code
	 * @param message the message
	 */
	public ServerException(String code, String message) {
		super(code, message);
	}

}
