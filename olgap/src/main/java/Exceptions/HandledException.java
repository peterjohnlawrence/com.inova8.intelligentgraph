/*
 * inova8 2020
 */
package Exceptions;

import org.eclipse.rdf4j.RDF4JException;

/**
 * The Class HandledException.
 */
public class HandledException extends RDF4JException {
	  
  	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The code. */
	private String code;
	public HandledException(String code) {
        this.setCode(code);
    }
	    /**
    	 * Instantiates a new handled exception.
    	 *
    	 * @param code the code
    	 * @param e the message
    	 */
    	public HandledException(String code, Throwable e) {
	        super(e);
	        this.setCode(code);
	    }

    	
	    /**
	     * Instantiates a new handled exception.
	     *
	     * @param code the code
	     * @param parameterizedMessage the parameterized message
	     */
	    public HandledException(String code, String message) {
	        super(message);
	        this.setCode(code);
    	}
    	
	    /**
	     * Instantiates a new handled exception.
	     *
	     * @param code the code
	     * @param parameterizedMessage the parameterized message
	     * @param cause the cause
	     */
	    public HandledException(String code, String message, Throwable cause) {
	        super(message,cause);
	        this.setCode(code);
    	}
	    

		/**
    	 * Gets the code.
    	 *
    	 * @return the code
    	 */
    	public String getCode() {
	        return code;
	    }

	    /**
    	 * Sets the code.
    	 *
    	 * @param code the new code
    	 */
    	public void setCode(String code) {
	        this.code = code;
	    }
}
