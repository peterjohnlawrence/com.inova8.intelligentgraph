/*
 * inova8 2020
 */
package Exceptions;

import javax.script.ScriptException;

/**
 * The Class ScriptFailedException.
 */
public class ScriptFailedException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param e the e
	 */
	public ScriptFailedException(String code, ScriptException e) {
		super(code, e);
	}

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 * @param e the e
	 */
	public ScriptFailedException(String code, String message, Exception e) {
		super(code, message,e);
	}

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public ScriptFailedException(String code, String message) {
		super(code, message);
	}

}
