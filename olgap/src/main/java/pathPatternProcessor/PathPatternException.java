package pathPatternProcessor;

import pathPatternProcessor.PathConstants.ErrorCode;

public class PathPatternException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4065368840470751037L;
	private final ErrorCode errorCode;
    public PathPatternException() {
		super();
		this.errorCode = null;

    }
    public PathPatternException(String message,ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
    }
	public ErrorCode getErrorCode() {
		return this.errorCode;
	}
}
