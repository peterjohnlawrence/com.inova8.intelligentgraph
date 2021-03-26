package pathQL;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.MalformedQueryException;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import org.eclipse.rdf4j.repository.RepositoryException;

import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathPatternException;
import pathQLQueryIterator.MatchJoinIterator;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import pathQLResults.ResourceResults;

public class PathQL {
	private final static Logger logger = LogManager.getLogger(PathQL.class);

	public static ResourceResults evaluate(PathQLRepository source,String pathQL) throws RecognitionException, PathPatternException {	
		PathElement element = PathParser.parsePathPattern(source, pathQL);		
		//pathQLResultsIterator.Resources resources = evaluate(element);
		return evaluate(source, element);
	}
	public  static pathQLResults.ResourceResults  evaluate(Thing thing, String pathQL) throws PathPatternException {
		PathElement pathElement = PathParser.parsePathPattern(thing, pathQL);
		return evaluate(thing, pathElement);
	}
	private static pathQLResults.ResourceResults evaluate(PathQLRepository source,PathElement element)
			throws RepositoryException, MalformedQueryException, QueryEvaluationException {
		PathQLResults pathqlResultsIterator = null ;
		if (element.getIsBound()) {
			String boundQuery = "SELECT * {"+ element.getLeftPathElement().toSPARQL()+"}";
			TupleQuery tupleBoundQuery = source.getRepository().getConnection().prepareTupleQuery(boundQuery);
			pathqlResultsIterator = new PathQLResults(tupleBoundQuery.evaluate());
			PathElement pathPattern = element.getRightPathElement();
			if(pathPattern!=null ) {
				EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(), null);
				TupleExpr rightArgTupleExpr = element.getRightPathElement().pathPatternQuery(null, null, null);
				MatchJoinIterator matchJoinIterator = new MatchJoinIterator(evaluationStrategy, pathqlResultsIterator.getResourceSet(),rightArgTupleExpr);
			
				//return new PathQLResultsIterator(matchJoinIterator);
				return new FactResults(matchJoinIterator,source,element.getRightPathElement());
			}else {
				return pathqlResultsIterator;
			}
		}else {
			//Cannot support an unbound PathQL query
			logger.error("Cannot support unbounded queries:"+ element.toString());
		}
		return pathqlResultsIterator;
	}

	public static pathQLResults.ResourceResults  evaluate(Thing thing, PathElement pathElement) {
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(thing.getSource().getTripleSource(), null);
		TupleExpr pathElementPattern = pathElement.pathPatternQuery(thing,null,null);
		pathElement.getSourceVariable().setValue( thing.getValue());
		BindingSet bindings = new QueryBindingSet();
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(pathElementPattern,bindings);
		logger.debug("Thing="+thing.toString());
		logger.debug( "pathQL="+ pathElement.toString());
		logger.debug("sourceVariable="+ pathElement.getTargetSubject().toString()+" targetPredicate="+ pathElement.getTargetPredicate().toString()+" targetVariable="+ pathElement.getTargetVariable().toString());
		logger.debug("pathPattern=\n"+ pathElementPattern.toString());
		return new FactResults( resultsIterator,thing, pathElement);
	}



}
