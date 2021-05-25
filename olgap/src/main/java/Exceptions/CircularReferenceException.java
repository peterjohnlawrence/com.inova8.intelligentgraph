/*
 * inova8 2020
 */
package Exceptions;

import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * The Class CircularReferenceException.
 */
public class CircularReferenceException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new circular reference exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public CircularReferenceException(String code, ParameterizedMessage parameterizedMessage) {
		super(code, parameterizedMessage);
	}


}
