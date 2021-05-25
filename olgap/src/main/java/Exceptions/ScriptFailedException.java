/*
 * inova8 2020
 */
package Exceptions;

import javax.script.ScriptException;

import org.apache.logging.log4j.message.ParameterizedMessage;

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
	public ScriptFailedException(String code, ParameterizedMessage parameterizedMessage, Exception e) {
		super(code, parameterizedMessage,e);
	}

	/**
	 * Instantiates a new script failed exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public ScriptFailedException(String code, ParameterizedMessage parameterizedMessage) {
		super(code, parameterizedMessage);
	}

}
