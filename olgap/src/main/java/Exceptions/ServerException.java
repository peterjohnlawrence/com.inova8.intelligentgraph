/*
 * inova8 2020
 */
package Exceptions;

import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * The Class RepositoryException.
 */
public class ServerException extends HandledException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	public ServerException(String code, Exception e) {
		super(code, e);
	}


	public ServerException(String code, ParameterizedMessage parameterizedMessage, Exception e) {
		super(code, parameterizedMessage,e);
	}

	public ServerException(String code, ParameterizedMessage parameterizedMessage) {
		super(code, parameterizedMessage);
	}

}
