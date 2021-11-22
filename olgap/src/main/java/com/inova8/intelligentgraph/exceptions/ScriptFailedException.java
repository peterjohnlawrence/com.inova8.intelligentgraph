/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

import javax.script.ScriptException;

/**
 * The Class ScriptFailedException.
 */
public class ScriptFailedException extends HandledException{
	private static final String SCRIPTFAILED_EXCEPTION = "ScriptFailed";
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param e the e
	 */
	public ScriptFailedException( HandledException e) {
		super(SCRIPTFAILED_EXCEPTION, e);
	}

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 * @param e the e
	 */
	public ScriptFailedException(String message, Exception e) {
		super(SCRIPTFAILED_EXCEPTION, message,e);
	}

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public ScriptFailedException( String message) {
		super(SCRIPTFAILED_EXCEPTION, message);
	}

	public ScriptFailedException(ScriptException e) {
		super(SCRIPTFAILED_EXCEPTION, e);
	}

}
