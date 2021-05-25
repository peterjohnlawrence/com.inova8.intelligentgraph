/*
 * inova8 2020
 */
package utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.iterator.QueryContextIteration;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 * The Class Query.
 */
public class Query {

	/**
	 * Run boolean.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the boolean
	 */
	public static Boolean runBoolean(RepositoryConnection conn, String queryString) {
		BooleanQuery query = conn.prepareBooleanQuery(queryString);
		Boolean result = query.evaluate();
		return result;
	}

	/**
	 * Run query.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runQuery(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		QueryContext queryContext = new QueryContext();
		queryContext.setAttribute("test", conn);
		QueryContextIteration contextResults = new QueryContextIteration(query.evaluate(), queryContext);
		StringBuilder aResult = new StringBuilder();
		while (contextResults.hasNext()) {
	
			BindingSet solution = contextResults.next();
			Set<String> bindingNames = solution.getBindingNames();
			if(bindingNames.size()==1) {
				for (String bindingName : bindingNames) {
					if (solution.getValue(bindingName) != null)
						aResult.append(solution.getValue(bindingName).stringValue());
				}
			}else {
			for (String bindingName : bindingNames) {
				if (solution.getValue(bindingName) != null)
					aResult.append(bindingName).append("=").append(solution.getValue(bindingName).stringValue())
							.append(";");
			}
			}
		}
		contextResults.close();
		return aResult.toString();
	}

	/**
	 * Run SPARQL.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runSPARQL(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		StringBuilder aResult = new StringBuilder();
		try(TupleQueryResult result = query.evaluate()){
			for (BindingSet bindingSet: result) {
				Set<String> bindingNames = bindingSet.getBindingNames();
				for (String bindingName : bindingNames) {
					if (bindingSet.getValue(bindingName) != null)
						aResult.append(bindingName).append("=").append(bindingSet.getValue(bindingName).stringValue())
								.append(";");
				}
			}
			
		}
		return aResult.toString();
	}

	/**
	 * Run CONSTRUCT.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runCONSTRUCT(RepositoryConnection conn, String queryString) {
		GraphQuery query = conn.prepareGraphQuery(queryString);
		StringBuilder aResult = new StringBuilder();
		try(GraphQueryResult result = query.evaluate()){
			while(result.hasNext()) {
				Statement nextResult = result.next();
				aResult.append(nextResult.toString()).append("\n");
			}
//			for (Statement statement: result) {
//				aResult.append(statement.toString());
//			}
			
		}catch(Exception e){
			return aResult.toString();
		}
		return aResult.toString();
	}

	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	 static String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}

	/**
		 * Assert equals WO spaces.
		 *
		 * @param actual the actual
		 * @param expected the expected
		 */
	 public static  void assertEqualsWOSpaces(String actual, String expected){
			assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
	}

}
