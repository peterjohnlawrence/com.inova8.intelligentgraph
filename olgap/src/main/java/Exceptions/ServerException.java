/*
 * inova8 2020
 */
package Exceptions;

/**
 * The Class RepositoryException.
 */
public class ServerException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	public ServerException(String code, Exception e) {
		super(code, e);
	}


	public ServerException(String code, String message, Exception e) {
		super(code, message,e);
	}

	public ServerException(String code, String message) {
		super(code, message);
	}

}
