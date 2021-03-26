package pathQL;

import org.eclipse.rdf4j.query.TupleQuery;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;

import static org.eclipse.rdf4j.model.util.Values.literal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Match {
	private final static Logger logger = LogManager.getLogger(Match.class);
	private static String matchQuery = "select ?entity ?property ?value ?snippet  ?score\n" + "{ \n"
			+ "  ?entity <http://www.openrdf.org/contrib/lucenesail#matches> [\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#query>  ?matchString; \n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#property> ?property ;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#score> ?score;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet] .\n" + "?entity ?property ?value }";

	public static MatchResults entityMatch(String matchString) {
		TupleQuery tupleMatchQuery = PathQLRepository.getRepository().getConnection().prepareTupleQuery(matchQuery);
		tupleMatchQuery.setBinding("matchString", literal(matchString));
		logger.debug("matchPattern=\n" + tupleMatchQuery.toString());
		return new MatchResults(tupleMatchQuery.evaluate());
	}

	/*	public static MatchResults entityDirectMatch(LuceneSail luceneSail, String matchString) {
			NotifyingSailConnection luceneConnection = luceneSail.getConnection();
			SearchIndex luceneIndex = luceneSail.getLuceneIndex();
	
			String matchesVarName = "anon";
			String propertyVarName = "property";
			String scoreVarName = "score";
			String snippetVarName = "snippet";
			//IRI subject = iri("entity");
			String queryString = "Unit1";
			IRI propertyURI = null;
			QuerySpec querySpec = new QuerySpec(matchesVarName, propertyVarName, scoreVarName, snippetVarName,
					null, queryString, propertyURI);
			Collection<BindingSet> queryResultSet = luceneIndex.evaluate(querySpec);
			return null;//new MatchResultsIterator(queryResultSet);
	
		}*/

}
