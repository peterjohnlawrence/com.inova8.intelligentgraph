package olgapTest;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class RDF4J {
	public static void main(String[] args) {
		Repository workingRep = new SailRepository(new MemoryStore());
		workingRep.init();
		Repository rep = new HTTPRepository("http://localhost:8082/rdf4j-server/", "tfl");
		rep.init();

		try (RepositoryConnection conn = rep.getConnection(); RepositoryConnection workingConn =  workingRep.getConnection();) {
			
//			DegreeDistributionTest degreeDistribution = new DegreeDistributionTest(conn);
//			degreeDistribution.evaluate();
			
			TrianglesTest triangles = new TrianglesTest(conn, workingRep);
			triangles.evaluate();
			
		
		}finally {
			// Before our program exits, make sure the database is properly shut down.
			rep.shutDown();
		}
	}

}
