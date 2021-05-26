/*
 * inova8 2020
 */
package Exceptions;

/**
 * The Class NullValueReturnedException.
 */
public class NullValueReturnedException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new null value returned exception.
	 *
	 * @param code the code
	 * @param parameterizedMessage the parameterized message
	 */
	public NullValueReturnedException(String code, String message) {
		super(code, message);
	}


}
