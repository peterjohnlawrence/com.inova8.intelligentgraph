/*
 * inova8 2020
 */
package pathCalc;

/**
 * The Class HandledException.
 */
public class HandledException extends Exception {
	  
  	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The code. */
	private String code;

	    /**
    	 * Instantiates a new handled exception.
    	 *
    	 * @param code the code
    	 * @param message the message
    	 */
    	public HandledException(String code, String message) {
	        super(message);
	        this.setCode(code);
	    }

	    /**
    	 * Instantiates a new handled exception.
    	 *
    	 * @param code the code
    	 * @param message the message
    	 * @param cause the cause
    	 */
    	public HandledException(String code, String message, Throwable cause) {
	        super(message, cause);
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
