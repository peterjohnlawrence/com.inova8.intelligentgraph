package olgapTest;

import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.spin.SpinSail;

public class ShortestPathTest {
	public static void main(String[] args) {

		Repository workingRep = new SailRepository(new SpinSail(new MemoryStore()));
		workingRep.init();
		try (RepositoryConnection conn = workingRep.getConnection();
				RepositoryConnection workingConn = workingRep.getConnection();) {

			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
			queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
			queryString += "PREFIX londontube: <http://in4mium.com/londontube/id/> \n";
			queryString += "PREFIX tfl: <http://in4mium.com/londontube/ref/> \n";
			//queryString += "SELECT   * \n";
			//queryString += "WHERE { \n";
			//queryString += "   BIND( \"http://localhost:8082/rdf4j-server/repositories/tfl\" as ?service)";
			//queryString += "   (?edge ?subject ?property ?direct ?object )  olgap:shortestPathProperty   (?service   "
			//		+ "<http://in4mium.com/londontube/id/Mornington_Crescent>  <http://in4mium.com/londontube/id/Burnt_Oak> "
			//		+ "<http://in4mium.com/londontube/id/Mornington_Crescent>  <http://in4mium.com/londontube/id/Baker_Street> "
			//		+ "\" (<http://in4mium.com/londontube/ref/connectsToxx>|<http://in4mium.com/londontube/ref/onLinexx>)!(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>|  <http://in4mium.com/londontube/ref/hasStationOnLinexx>)^!(rdf:type)\" "
			//		+  "\"!(rdf:type)(<http://in4mium.com/londontube/ref/connectsTo>)!^(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>| <http://in4mium.com/londontube/ref/hasStationInZone>)\""
			//		+ " 8 )  .";		
			queryString += "SELECT ?subject ?property ?object ?direct  ?edge \r\n" + "WHERE {\r\n"
					+ "     BIND( \"http://localhost:8082/rdf4j-server/repositories/tfl\" as ?service)\r\n"
					+ "	 BIND( <http://in4mium.com/londontube/id/Mornington_Crescent> as ?start)\r\n"
					+ "	 BIND( <http://in4mium.com/londontube/id/Baker_Street> as ?end)\r\n"
					//+ "	 BIND( \"(<http://in4mium.com/londontube/ref/connectsToxx>|<http://in4mium.com/londontube/ref/onLinexx>)!(rdf:type)^(<http://in4mium.com/londontube/ref/connectsFrom>|  <http://in4mium.com/londontube/ref/hasStationOnLinexx>)^!(rdf:type)\" as ?propertyPath)\r\n"
					//+ "	 BIND( \"^!(rdf:type)!(rdf:type)^(tfl:connectsFrom)\" as ?propertyPath)\r\n"
					+ "	 BIND( (tfl:connectsFrom tfl:hasStationOnLinexx) as ?propertyPath)\r\n"
					+ "	 BIND( 8 as ?maxPath)\r\n"
					+ "	(?edge ?subject ?property ?direct ?object )  <http://inova8.com/olgap/shortestPathProperty>   (?service  ?start  ?end ?propertyPath ?maxPath )  .\r\n"
					+ "}";
			queryString += "order by ?edge";

			TupleQuery query = conn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
					List<String> bindingNames = result.getBindingNames();
					while (result.hasNext()) {
						BindingSet solution = result.next();
						StringBuilder aResult = new StringBuilder();
						for (String bindingName : bindingNames) {
							aResult.append(bindingName).append(" = ")
									.append(solution.getValue(bindingName).stringValue()).append("; ");
						}
						System.out.println(aResult);
				}
				System.out.println("Finished!");
			}
		} finally {
			workingRep.shutDown();
		}
	}

}
