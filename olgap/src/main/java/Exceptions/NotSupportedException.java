/*
 * inova8 2020
 */
package Exceptions;

/**
 * The Class RepositoryException.
 */
public class NotSupportedException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	public NotSupportedException(String code) {
		super(code);
	}

	public NotSupportedException(String code, Exception e) {
		super(code, e);
	}


	public NotSupportedException(String code,String message, Exception e) {
		super(code, message,e);
	}

	public NotSupportedException(String code, String message) {
		super(code, message);
	}

}
