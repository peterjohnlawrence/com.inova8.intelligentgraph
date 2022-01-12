/*
 * inova8 2020
 */
package com.inova8.pathql.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import com.inova8.intelligentgraph.constants.PathConstants.ErrorCode;
import com.inova8.intelligentgraph.exceptions.PathPatternException;
import com.inova8.intelligentgraph.repositoryContext.Prefixes;
import com.inova8.intelligentgraph.repositoryContext.RepositoryContext;
import com.inova8.pathql.element.IriRefValueElement;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.processor.PathErrorListener;
import com.inova8.pathql.processor.PathPatternVisitor;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.IriRefContext;
import PathPattern.PathPatternParser.PathEltOrInverseContext;
import PathPattern.PathPatternParser.QueryStringContext;

/**
 * The Class PathParser.
 */
public class PathParser {
	
	/** The path pattern visitor. */
	//private final static Logger logger = LogManager.getLogger(PathParser.class);
	static PathPatternVisitor pathPatternVisitor ;
	static PathPatternVisitor queryStringVisitor ;

//	public static PathElement parsePathPattern(Prefixes prefixes, String pathPattern)
//			throws RecognitionException, PathPatternException {
//		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(prefixes);
//		PathElement pathElement = pathPatternParser(pathPattern, pathPatternVisitor);	
//		return pathElement;
//	}
	public static PathElement parsePathPattern(RepositoryContext repositoryContext, String pathPattern)
			throws RecognitionException, PathPatternException {
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(repositoryContext);
		PathElement pathElement = pathPatternParser(pathPattern, pathPatternVisitor);	
		return pathElement;
	}
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
	 * @param source the source
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
	public static IriRefValueElement parseIriRef(Prefixes prefixes,String uriPattern)
			throws RecognitionException, PathPatternException {
		try {
			pathPatternVisitor = new PathPatternVisitor(prefixes);
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
	 * @param source the source
	 * @param uriPattern the uri pattern
	 * @return the predicate element
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	public static PredicateElement parsePredicate(Prefixes prefixes,String uriPattern)
			throws RecognitionException, PathPatternException {
		pathPatternVisitor = new PathPatternVisitor(prefixes);
		CharStream input = CharStreams.fromString( uriPattern);
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathEltOrInverseContext pathPatternTree = parser.pathEltOrInverse();
		PathElement iriRefValueElement = pathPatternVisitor.visit(pathPatternTree);
		return (PredicateElement) iriRefValueElement;
	}
}
