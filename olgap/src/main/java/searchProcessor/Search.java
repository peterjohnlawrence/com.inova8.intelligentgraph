package searchProcessor;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import pathCalc.Source;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;

public class Search {
	private final static Logger logger = LogManager.getLogger(Search.class);
	private static String searchQuery = "select ?entity ?property ?snippet  ?score\n" + "{ \n"
			+ "  ?entity <http://www.openrdf.org/contrib/lucenesail#matches> [\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#query>  ?searchString; \n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#property> ?property ;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#score> ?score;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet] .\n" + "}";
	
	public static SearchResultsIterator entitySearch( String searchString) {
		TupleQuery tupleSearchQuery = Source.getRepository().getConnection().prepareTupleQuery(searchQuery);
		tupleSearchQuery.setBinding("searchString", literal(searchString));
		logger.debug("searchPattern=\n" + tupleSearchQuery.toString());
		return new SearchResultsIterator(tupleSearchQuery.evaluate());
	}
	
	@Deprecated
	public static SearchResultsIterator entitySearch3(String searchString) {
		//	  ?entity <http://www.openrdf.org/contrib/lucenesail#matches> [
		//	      <http://www.openrdf.org/contrib/lucenesail#query> searchString ; 
		//	      <http://www.openrdf.org/contrib/lucenesail#property> ?property ;
		//	      <http://www.openrdf.org/contrib/lucenesail#score> ?score;
		//	      <http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet] .
		//	  OPTIONAL{?property  rdfs:label ?labelProperty}. FILTER(!ISBLANK(?entity))

		Var entityVariable = new Var("?entity");
		Var matchesPredicate = new Var("?matchesPredicate", iri("http://www.openrdf.org/contrib/lucenesail#matches"));
		Var matchNodeVariable = new Var("?matchNode");
		StatementPattern searchPattern1 = new StatementPattern(entityVariable, matchesPredicate, matchNodeVariable);

		Var queryPredicate = new Var("?queryPredicate", iri("http://www.openrdf.org/contrib/lucenesail#query"));
		Var searchVariable = new Var("?search", literal(searchString));
		StatementPattern searchPattern2 = new StatementPattern(matchNodeVariable, queryPredicate, searchVariable);

		Var propertyPredicate = new Var("?propertyPredicate",
				iri("http://www.openrdf.org/contrib/lucenesail#property"));
		Var propertyVariable = new Var("?property");
		StatementPattern searchPattern3 = new StatementPattern(matchNodeVariable, propertyPredicate, propertyVariable);

		Var scorePredicate = new Var("?scorePredicate", iri("http://www.openrdf.org/contrib/lucenesail#score"));
		Var scoreVariable = new Var("?score");
		StatementPattern searchPattern4 = new StatementPattern(matchNodeVariable, scorePredicate, scoreVariable);

		Var snippetPredicate = new Var("?snippetPredicate", iri("http://www.openrdf.org/contrib/lucenesail#snippet"));
		Var snippetVariable = new Var("?snippet");
		StatementPattern searchPattern5 = new StatementPattern(matchNodeVariable, snippetPredicate, snippetVariable);

	//	Var labelPredicate = new Var("?labelPredicate", iri("http://www.w3.org/2000/01/rdf-schema#label"));
	//	Var labelVariable = new Var("?label");
	//	StatementPattern searchPattern6 = new StatementPattern(propertyVariable, labelPredicate, labelVariable);

		Join join1 = new Join(searchPattern4, searchPattern5);
		Join join2 = new Join(searchPattern3, join1);
		Join join3 = new Join(searchPattern2, join2);
		Join entitySearch = new Join(searchPattern1, join3);

		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(Source.getTripleSource(), null);

		BindingSet bindings = new QueryBindingSet();
		CloseableIteration<BindingSet, QueryEvaluationException> searchResult = evaluationStrategy
				.evaluate(entitySearch, bindings);

		logger.debug("pathPattern=\n" + entitySearch.toString());
		return new SearchResultsIterator(searchResult);

	}


}
