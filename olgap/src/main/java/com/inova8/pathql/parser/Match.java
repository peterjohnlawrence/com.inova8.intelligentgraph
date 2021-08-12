/*
 * inova8 2020
 */
package com.inova8.pathql.parser;

import org.eclipse.rdf4j.query.TupleQuery;

import static org.eclipse.rdf4j.model.util.Values.literal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLResults.MatchResults;
import com.inova8.pathql.parser.Match;

/**
 * The Class Match.
 */
@Deprecated
public class Match {
	
	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(Match.class);
	
	/**
	 * Instantiates a new match.
	 *
	 * @param source the source
	 */
	public Match(IntelligentGraphRepository source) {
		super();
		this.source = source;
	}

	/** The source. */
	IntelligentGraphRepository source;
	
	/** The match query. */
	private static String matchQuery = "select ?entity ?property ?value ?snippet  ?score\n" + "{ \n"
			+ "  ?entity <http://www.openrdf.org/contrib/lucenesail#matches> [\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#query>  ?matchString; \n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#property> ?property ;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#score> ?score;\n"
			+ "      <http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet] .\n" + "?entity ?property ?value }";

	/**
	 * Entity match.
	 *
	 * @param matchString the match string
	 * @return the match results
	 */
	public  MatchResults entityMatch(String matchString) {
		TupleQuery tupleMatchQuery = source.getContextAwareConnection().prepareTupleQuery(matchQuery);
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
