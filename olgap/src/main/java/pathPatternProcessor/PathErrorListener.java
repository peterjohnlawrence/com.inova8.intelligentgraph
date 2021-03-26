package pathPatternProcessor;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class PathErrorListener  extends BaseErrorListener {
	 private final List<String> syntaxErrors = new ArrayList<String>();
	private String[] pathPattern;
	public PathErrorListener(String pathPattern) {
		 this.pathPattern = pathPattern.split(System.getProperty("line.separator"));
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, java.lang.Object offendingSymbol, int line,
			int charPositionInLine, String msg, RecognitionException e) {
		//syntaxErrors.add( "line " + line + ":" + charPositionInLine + " at " + offendingSymbol + ": " + msg) ;
		syntaxErrors.add( "line " + line + ":" + charPositionInLine + " in \"" + this.pathPattern[line-1] + "\": " + msg) ;
	}

	public List<String> getErrors() {
		if (syntaxErrors.size() == 0)
			return null;
		else
			return syntaxErrors;
	}
	 @Override
    public String toString()
    {
		 if(syntaxErrors.size()==0)
			 return null;
		 else
			 return syntaxErrors.toString();
    }
}
