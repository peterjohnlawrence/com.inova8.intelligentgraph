/*
 * inova8 2020
 */
package remotePathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Remote_CRUD_Test {
	
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	private static IntelligentGraphRepository source;
	


	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		org.eclipse.rdf4j.repository.Repository workingRep = new HTTPRepository("http://localhost:8080/rdf4j-server","calc2graph");
		//org.eclipse.rdf4j.repository.Repository workingRep = new SPARQLRepository("http://localhost:8080/rdf4j-server/repositories/calc2graph");
		source =IntelligentGraphRepository.create(workingRep);
		workingRep.getConnection();
	}

	
	/**
	 * Ig 0.
	 */
	@Test
	@Order(0)
	void ig_0() {

		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			ResourceResults facts = myCountry.getFacts(":sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			myCountry.deleteFacts(":sales[eq '3']");
			factsinrange = myCountry.getFacts(":sales[ge '2';lt '4']").count();
			assertEquals(1, factsinrange);
			Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph1>");
			assertEquals(true, closed);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(1)
	void ig_1() {

		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country2");
			myCountry.addFact(":Attribute@:sales", "1");
			myCountry.addFact(":Attribute@:sales", "2");
			myCountry.addFact(":Attribute@:sales", "3");
			myCountry.addFact(":Attribute@:sales", "4");
			ResourceResults facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			myCountry.deleteFacts(":Attribute@:sales[eq '3']");
			facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			factsinrange = facts.count();
			assertEquals(1, factsinrange);
			Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals(true, closed);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}	
}
