package resources_Deprecated;

import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import pathPatternElement.PathElement;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternException;
import pathPatternProcessor.PathPatternVisitor;
import pathPatternProcessor.Resources;
import pathPatternProcessor.Thing;
import pathPatternProcessor.PathConstants.ErrorCode;
@Deprecated
public class PathProcessorOld {
	private final static Logger logger = LogManager.getLogger(PathProcessorOld.class);
	public  static pathPatternProcessor.Resources parseProcessPath(DeprecatedThing thing, String pathPattern) throws PathPatternException {
		PathElement pathElement = parsePathPattern(thing, pathPattern);
		ArrayList<Integer> indices = new ArrayList<Integer>();
		indices.add(0, 0);
		pathElement.buildIndices(indices, null);
		return processPathElement(thing, (PathElement)pathElement);
	}
	public static PathElement parsePathPattern(DeprecatedThing thing, String pathPattern)
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
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
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
		return pathElement;
	}
	private  static Resources processPathElement(DeprecatedThing thing, PathElement pathElement){
		switch (pathElement.getOperator()) {
		case PREDICATE: 
			return processPredicateElement(thing, (PredicateElement)pathElement) ;
		case SEQUENCE: 
			pathPatternProcessor.Resources leftValues = processPredicateElement(thing, (PredicateElement)pathElement.getLeftPathElement());
			while (leftValues.hasNext()) {
				return processPathElement((DeprecatedThing) leftValues.next(), pathElement.getRightPathElement());
			}
			return null;
		case ALTERNATIVE: 
			logger.error("ALTERNATIVE not supported " + pathElement.getOperator().toString());
			return null;
		case NEGATION: 
			logger.error("NEGATION not supported " + pathElement.getOperator().toString());
			return null;		
		default:
			return null;
		}
	}
	private  static pathPatternProcessor.Resources processPredicateElement(DeprecatedThing thing, PredicateElement predicateElement) {
		if(predicateElement.getIsInverseOf()) {
			if( predicateElement.getIsReified()) {
				return  getIsReifiedFactsOf(thing,predicateElement);
			}else {
				return getIsFactsOf(thing,predicateElement);
			}
		}else {
			if( predicateElement.getIsReified()) {
				return  getReifiedFacts(thing,predicateElement);
			}else {
				return getFacts(thing,predicateElement);
			}
		}
	}
	public final static Resources getFacts(DeprecatedThing thing,PredicateElement predicateElement) {
		thing.addTrace(new ParameterizedMessage("Seeking values {} of subject {}", predicateElement.getPathPattern(), thing.addIRI(thing.getSuperValue())));
		Resources facts = new SimpleResources( thing,predicateElement);		
		return facts;
	}
	public final static Resources getIsFactsOf(DeprecatedThing thing,PredicateElement predicateElement) {
		thing.addTrace(new ParameterizedMessage("Seeking subjects {} of {}", predicateElement.getPathPattern(), thing.addIRI(thing.getSuperValue())));
		Resources isFactsOf = new IsSimpleResourcesOf( thing,predicateElement);
		return isFactsOf;
	}
	public final static Resources getReifiedFacts(DeprecatedThing thing, PredicateElement predicateElement) {
		IRI reificationType= predicateElement.getReification();
		IRI predicate=   predicateElement.getPredicate();
		thing.addTrace(new ParameterizedMessage("Seeking values {} of subject {}", predicateElement.getPathPattern(), thing.addIRI(thing.getSuperValue())));
		Resources reifiedFacts = new ReifiedResources( thing,predicateElement);
		return  reifiedFacts;
	}

	public final static Resources getIsReifiedFactsOf(DeprecatedThing thing, PredicateElement predicateElement) {
		thing.addTrace(new ParameterizedMessage("Seeking values {} of subject {}",  predicateElement.getPathPattern() , thing.addIRI(thing.getSuperValue())));
		Resources isReifiedFactsOf = new IsReifiedResourcesOf( thing,predicateElement);
		return  isReifiedFactsOf;
	}
}
