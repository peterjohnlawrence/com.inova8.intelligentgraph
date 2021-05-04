/*
 * inova8 2020
 */
package pathPatternProcessor;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * The listener interface for receiving pathError events.
 * The class that is interested in processing a pathError
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPathErrorListener<code> method. When
 * the pathError event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PathErrorEvent
 */
public class PathErrorListener  extends BaseErrorListener {
	 
 	/** The syntax errors. */
 	private final List<String> syntaxErrors = new ArrayList<String>();
	
	/** The path pattern. */
	private String[] pathPattern;
	
	/**
	 * Instantiates a new path error listener.
	 *
	 * @param pathPattern the path pattern
	 */
	public PathErrorListener(String pathPattern) {
		 this.pathPattern = pathPattern.split(System.getProperty("line.separator"));
	}

	/**
	 * Syntax error.
	 *
	 * @param recognizer the recognizer
	 * @param offendingSymbol the offending symbol
	 * @param line the line
	 * @param charPositionInLine the char position in line
	 * @param msg the msg
	 * @param e the e
	 */
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, java.lang.Object offendingSymbol, int line,
			int charPositionInLine, String msg, RecognitionException e) {
		//syntaxErrors.add( "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg) ;
		syntaxErrors.add( "line " + line + ":" + charPositionInLine + " in \"" + this.pathPattern[line-1] + "\": " + msg) ;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<String> getErrors() {
		if (syntaxErrors.size() == 0)
			return null;
		else
			return syntaxErrors;
	}
	 
 	/**
 	 * To string.
 	 *
 	 * @return the string
 	 */
 	@Override
    public String toString()
    {
		 if(syntaxErrors.size()==0)
			 return null;
		 else
			 return syntaxErrors.toString();
    }
}
