package pathQL;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternException;
import pathPatternProcessor.PathPatternVisitor;
import pathQLRepository.PathQLRepository;
import pathPatternProcessor.PathConstants.ErrorCode;

public class PathParser {
	private final static Logger logger = LogManager.getLogger(PathParser.class);
	public static PathElement parsePathPattern(Thing thing, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);		
		PathElement pathElement = parser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}

	public static PathElement parsePathPattern(PathQLRepository source, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor();//source);
		PathElement pathElement = parser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}
	
	private static PathElement parser(String pathPattern, PathPatternVisitor pathPatternVisitor)
			throws RecognitionException, PathPatternException {
		PathErrorListener errorListener = new PathErrorListener(pathPattern);
		CharStream input = CharStreams.fromString( pathPattern);
		PathPatternLexer lexer = new PathPatternLexer(input);
		lexer.removeErrorListeners(); 
		lexer.addErrorListener(errorListener); 
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		parser.removeErrorListeners(); 
		parser.addErrorListener(errorListener); 
		PathPatternContext pathPatternTree = parser.pathPattern();
		PathElement pathElement = pathPatternVisitor.visit(pathPatternTree);
		pathElement.setPathPattern(pathPattern);
		if( errorListener.toString()!=null) {
			if(parser.getNumberOfSyntaxErrors()==0) {
				//Lexer only error
				throw new PathPatternException(errorListener.toString(),ErrorCode.LEXER);
			}else {
				//Parser error
				throw new PathPatternException(errorListener.toString(),ErrorCode.PARSER);
			}
		}
		pathElement.indexVisitor(null,0,null);
		return pathElement;
	}
}
