/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.exceptions;

import com.inova8.intelligentgraph.constants.PathConstants.ErrorCode;

/**
 * The Class PathPatternException.
 */
public class PathPatternException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4065368840470751037L;
	
	/** The error code. */
	private final ErrorCode errorCode;
    
    /**
     * Instantiates a new path pattern exception.
     */
    public PathPatternException() {
		super();
		this.errorCode = null;

    }
    
    /**
     * Instantiates a new path pattern exception.
     *
     * @param message the message
     * @param errorCode the error code
     */
    public PathPatternException(String message,ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
    }
	
	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public ErrorCode getErrorCode() {
		return this.errorCode;
	}
}
