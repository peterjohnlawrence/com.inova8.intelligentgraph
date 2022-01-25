package olgap;

import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.spin.SpinSail;

@SuppressWarnings("deprecation")

public class Test {
	public static void main(String[] args) {
		
		Repository workingRep = new SailRepository(new SpinSail(new MemoryStore()));
		//LuceneSail lucenesail = new LuceneSail();
		//lucenesail.setBaseSail(new MemoryStore());
		//lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
		//Repository workingRep = new SailRepository(lucenesail);
		//Repository workingRep = new SailRepository(new MemoryStore()));
		workingRep.init();
		//Repository rep = new HTTPRepository("http://localhost:8082/rdf4j-server/", "tfl");
		//rep.init();
		String namespace = "http://inova8.com/olgap/";
		ValueFactory f = workingRep.getValueFactory();
		IRI exa = f.createIRI(namespace, "a");
		IRI exb = f.createIRI(namespace, "b");

		try (RepositoryConnection conn = workingRep.getConnection();
				RepositoryConnection workingConn = workingRep.getConnection();) {
			conn.add(exa, RDFS.LABEL,  f.createLiteral("step on no pets")); 
			conn.add(exb, RDFS.LABEL, f.createLiteral("go on, try it"));
			
			
			String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  \n";
			queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
			//queryString += "SELECT ?x ?label  ?degreeDistribution \n";
			queryString += "SELECT   * \n";
			queryString += "WHERE { \n";

//			queryString += "   BIND( olgap:triangles(\"http://localhost:8082/rdf4j-server/\", \"tfl\") as ?triangles)";
			
			//queryString += "    ?x rdfs:label ?label . \n";
			
//			queryString += "   { select ?degree ?count  {";
//			queryString += "   BIND( \"http://localhost:8082/rdf4j-server/\" as ?service)";
//			queryString += "   BIND( \"tfl\" as ?repository)";
//			//queryString += "   (?service ?repository)  olgap:degreeDistributionProperty (?degree  ?count )  .";
//			queryString += "   (?degree  ?count ) olgap:degreeDistributionProperty   (?service ?repository)  .";
//			queryString += "   }} ";
	
			
//			queryString += "   { select ?degree ?count  {";
			queryString += "   BIND( \"http://localhost:8082/rdf4j-server/\" as ?service)";
			queryString += "   BIND( \"tfl\" as ?repository)";
//			//queryString += "   (?service ?repository)  olgap:degreeDistributionProperty (?degree  ?count )  .";
			queryString += "   (?edge ?subject ?property ?direct ?object )  olgap:shortestPathProperty   (?service ?repository  <http://in4mium.com/londontube/id/Mornington_Crescent>  <http://in4mium.com/londontube/id/Burnt_Oak> \"!(rdf:type)\"   \"(<http://in4mium.com/londontube/ref/connectsTo>)\" \"!^(rdf:type)\" \"^(<http://in4mium.com/londontube/ref/connectsFrom>| <http://in4mium.com/londontube/ref/hasStationInZone>)\"  8 )  .";
//			queryString += "   }} ";
			
			
			//queryString += "    FILTER(cfn:palindrome(str(?label)))";
			//queryString += "   BIND( olgap:degreeDistribution(\"http://localhost:8082/rdf4j-server/\", \"tfl\") as ?degreeDistribution)";
			
			queryString += "}";
			queryString += "order by ?edge";
			
			TupleQuery query = conn.prepareTupleQuery(queryString);
			
			// A QueryResult is also an AutoCloseable resource, so make sure it gets 
			// closed when done.
			try (TupleQueryResult result = query.evaluate()) {
				List<String> bindingNames = result.getBindingNames();
				while (result.hasNext()) {
					BindingSet solution = result.next();
					StringBuilder aResult = new StringBuilder();
					for (String bindingName : bindingNames) {
						aResult.append(bindingName).append(" = ").append(solution.getValue(bindingName).stringValue()).append("; ");
					}
					System.out.println(aResult);
				}
				System.out.println("Finished!");
			}
			

		} finally {
			// Before our program exits, make sure the database is properly shut down.
			workingRep.shutDown();
		}
	}

}
