/*
 * inova8 2020
 */
package com.inova8.pathql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.element.IriRefValueElement;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.pathPattern.PathPatternLexer;
import com.inova8.pathql.pathPattern.PathPatternParser;
import com.inova8.pathql.pathPattern.PathPatternParser.IriRefContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathEltOrInverseContext;
import com.inova8.pathql.pathPattern.PathPatternParser.QueryStringContext;
import com.inova8.pathql.processor.PathErrorListener;
import com.inova8.pathql.processor.PathPatternException;
import com.inova8.pathql.processor.PathPatternVisitor;
import com.inova8.pathql.processor.PathConstants.ErrorCode;

/**
 * The Class PathParser.
 */
public class PathParser {
	
	/** The path pattern visitor. */
	//private final static Logger logger = LogManager.getLogger(PathParser.class);
	static PathPatternVisitor pathPatternVisitor ;
	
	/** The query string visitor. */
	static PathPatternVisitor queryStringVisitor ;
	
//	@Deprecated
//	public static PathElement parsePathPattern(Thing thing, String pathPattern)
//			throws RecognitionException, PathPatternException {
//		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);		
//		PathElement pathElement = pathPatternParser(pathPattern, pathPatternVisitor);	
//		return pathElement;
//	}
//	public static PathElement parsePathPattern(String pathPattern)
//			throws RecognitionException, PathPatternException {
//		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor();		
//		PathElement pathElement = pathPatternParser(pathPattern, pathPatternVisitor);	
//		return pathElement;
//	}
	/**
 * Parses the path pattern.
 *
 * @param repositoryContext the repository context
 * @param pathPattern the path pattern
 * @return the path element
 * @throws RecognitionException the recognition exception
 * @throws PathPatternException the path pattern exception
 */
	public static PathElement parsePathPattern(RepositoryContext repositoryContext, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(repositoryContext);
		PathElement pathElement = pathPatternParser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}
	
	/**
	 * Path pattern parser.
	 *
	 * @param pathPattern the path pattern
	 * @param pathPatternVisitor the path pattern visitor
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private static PathElement pathPatternParser(String pathPattern, PathPatternVisitor pathPatternVisitor)
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
		QueryStringContext queryStringTree = parser.queryString();
		PathElement pathElement = pathPatternVisitor.visit(queryStringTree);

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
		pathElement.setIterations(Iterations.create(pathElement));
	//	pathElement.setPathBindings(PathBindings.create(pathElement));
		return pathElement;

	}
	
	/**
	 * Parses the iri ref.
	 *
	 * @param repositoryContext the repository context
	 * @param uriPattern the uri pattern
	 * @return the iri ref value element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static IriRefValueElement parseIriRef(RepositoryContext repositoryContext,String uriPattern)
			throws RecognitionException, PathPatternException {
		try {
			pathPatternVisitor = new PathPatternVisitor(repositoryContext);
			CharStream input = CharStreams.fromString( uriPattern);
			PathPatternLexer lexer = new PathPatternLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PathPatternParser parser = new PathPatternParser(tokens);
			IriRefContext pathPatternTree = parser.iriRef();
			PathElement iriRefValueElement = pathPatternVisitor.visit(pathPatternTree);
			return (IriRefValueElement) iriRefValueElement;
		}catch (Exception qe) {
			throw new PathPatternException(qe.getMessage(),null);
		}
	}
	
	/**
	 * Parses the predicate.
	 *
	 * @param repositoryContext the repository context
	 * @param uriPattern the uri pattern
	 * @return the predicate element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static PredicateElement parsePredicate(RepositoryContext repositoryContext,String uriPattern)
			throws RecognitionException, PathPatternException {
		pathPatternVisitor = new PathPatternVisitor(repositoryContext);
		CharStream input = CharStreams.fromString( uriPattern);
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathEltOrInverseContext pathPatternTree = parser.pathEltOrInverse();
		PathElement iriRefValueElement = pathPatternVisitor.visit(pathPatternTree);
		return (PredicateElement) iriRefValueElement;
	}
}
