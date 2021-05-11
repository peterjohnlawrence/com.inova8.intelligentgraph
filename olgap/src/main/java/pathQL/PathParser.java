/*
 * inova8 2020
 */
package pathQL;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.IriRefContext;
import PathPattern.PathPatternParser.PathEltOrInverseContext;
import PathPattern.PathPatternParser.PathPatternContext;
import pathCalc.Thing;
import pathPatternElement.IriRefValueElement;
import pathPatternElement.PathElement;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternException;
import pathPatternProcessor.PathPatternVisitor;
import pathQLRepository.PathQLRepository;
import pathPatternProcessor.PathConstants.ErrorCode;

/**
 * The Class PathParser.
 */
public class PathParser {
	
	//private final static Logger logger = LogManager.getLogger(PathParser.class);
	static PathPatternVisitor pathPatternVisitor ;//= new PathPatternVisitor();
	
	/**
	 * Parses the path pattern.
	 *
	 * @param thing the thing
	 * @param pathPattern the path pattern
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static PathElement parsePathPattern(Thing thing, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);		
		PathElement pathElement = parser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}

	/**
	 * Parses the path pattern.
	 *
	 * @param source the source
	 * @param pathPattern the path pattern
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static PathElement parsePathPattern(PathQLRepository source, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(source);
		PathElement pathElement = parser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}
	
	/**
	 * Parser.
	 *
	 * @param pathPattern the path pattern
	 * @param pathPatternVisitor the path pattern visitor
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
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

		if( errorListener.toString()!=null) {
			if(parser.getNumberOfSyntaxErrors()==0) {
				//Lexer only error
				throw new PathPatternException(errorListener.toString(),ErrorCode.LEXER);
			}else {
				//Parser error
				throw new PathPatternException(errorListener.toString(),ErrorCode.PARSER);
			}
		}
		pathElement.setPathPattern(pathPattern);
		pathElement.indexVisitor(null,0,null);
		return pathElement;

	}
	public static IriRefValueElement parseIriRef(PathQLRepository source,String uriPattern)
			throws RecognitionException, PathPatternException {
		pathPatternVisitor = new PathPatternVisitor(source);
		CharStream input = CharStreams.fromString( uriPattern);
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		IriRefContext pathPatternTree = parser.iriRef();
		PathElement iriRefValueElement = pathPatternVisitor.visit(pathPatternTree);
		return (IriRefValueElement) iriRefValueElement;
	}
	public static PredicateElement parsePredicate(PathQLRepository source,String uriPattern)
			throws RecognitionException, PathPatternException {
		pathPatternVisitor = new PathPatternVisitor(source);
		CharStream input = CharStreams.fromString( uriPattern);
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathEltOrInverseContext pathPatternTree = parser.pathEltOrInverse();
		PathElement iriRefValueElement = pathPatternVisitor.visit(pathPatternTree);
		return (PredicateElement) iriRefValueElement;
	}
}
