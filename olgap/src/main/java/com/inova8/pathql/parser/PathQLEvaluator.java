/*
 * inova8 2020
 */
package com.inova8.pathql.parser;

import org.antlr.v4.runtime.RecognitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.exceptions.PathPatternException;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLQueryIterator.MatchJoinIterator;
import com.inova8.intelligentgraph.pathQLResults.FactBindingsetResults;
import com.inova8.intelligentgraph.pathQLResults.PathQLBindingSetResults;
import com.inova8.intelligentgraph.pathQLResults.BindingSetResults;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathQLEvaluator;

import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import org.eclipse.rdf4j.repository.RepositoryException;


@Deprecated
public class PathQLEvaluator {
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(PathQLEvaluator.class);


	@Deprecated
	public static BindingSetResults evaluate(IntelligentGraphRepository source,String pathQL) throws RecognitionException, PathPatternException {	
		PathElement element = PathParser.parsePathPattern(source, pathQL);		
		if(element!=null ) {
			return evaluate(source, element,null);
		}else {
			throw new  PathPatternException();
		}
	}
//	@Deprecated
//	private  static com.inova8.intelligentgraph.pathQLResults.ResourceResults  evaluate(Thing thing, String pathQL,CustomQueryOptions customQueryOptions) throws PathPatternException {
//		PathElement pathElement =  PathParser.parsePathPattern(thing, pathQL);
//		if(pathElement!=null ) {
//			CustomQueryOptions pathCustomQueryOptions = pathElement.getCustomQueryOptions();
//			if(pathCustomQueryOptions!=null) {
//				pathCustomQueryOptions.addInherited(customQueryOptions);
//				return evaluate(thing, pathElement,pathCustomQueryOptions);
//			}else {
//				return evaluate(thing, pathElement,customQueryOptions);
//			}
//		}else {
//			throw new  PathPatternException();
//		}
//	}
	@Deprecated
	private static com.inova8.intelligentgraph.pathQLResults.BindingSetResults evaluate(IntelligentGraphRepository source,PathElement element,CustomQueryOptions customQueryOptions )
			throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		PathQLBindingSetResults boundResultsIterator = null ;
		if (element.getIsBound()) {
			PathElement pathPattern = element.getRightPathElement();
			String boundQuery;
			if(pathPattern!=null) {
				boundQuery = "SELECT DISTINCT ?n0 {"+ element.getLeftPathElement().toSPARQL()+"}";
			}else {
				boundQuery = "SELECT * {"+ element.getLeftPathElement().toSPARQL()+"}";
			}
			logger.debug("boundPattern=\n"+ boundQuery);
			TupleQuery tupleBoundQuery = source.getContextAwareConnection().prepareTupleQuery(boundQuery);
			boundResultsIterator = new PathQLBindingSetResults(tupleBoundQuery.evaluate());
			
			if(pathPattern!=null ) {
				EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(), null);
				TupleExpr rightArgTupleExpr = element.getRightPathElement().pathPatternQuery(null).getTupleExpr();
				MatchJoinIterator matchJoinIterator = new MatchJoinIterator(evaluationStrategy, boundResultsIterator.getResourceSet(),rightArgTupleExpr);
			
				//return new PathQLResultsIterator(matchJoinIterator);
				return new FactBindingsetResults(matchJoinIterator,source,element.getRightPathElement(), customQueryOptions);
			}else {
				return boundResultsIterator;
			}
		}else {
			//Cannot support an unbound PathQL query
			logger.error("Cannot support unbounded queries:"+ element.toString());
		}
		return boundResultsIterator;
	}





}
