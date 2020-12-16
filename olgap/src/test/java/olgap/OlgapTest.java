package olgap;

import java.util.Set;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.iterator.QueryContextIteration;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class OlgapTest {
	@SuppressWarnings("deprecation")
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
				aResult.append("\r\n");
			}
		}
		contextResults.close();
		return aResult.toString();
	}
}
