/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.Thing;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetTraceFact_Tests {
		
	/** The source. */
	private static PathQLRepository source;
	
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_GetTraceFact_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");	
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("def", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = PathQLRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"));
			Resource result1 = $this.getFact(":volumeFlow");
			$this.traceFact(":massThroughput");
			$this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"));
			Resource result2 = $this.getFact(":massThroughput");
			
			
			assertEquals("40", result1.stringValue());
			assertEquals("37.99999952316284", result2.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}
	@Test
	@Order(2)
	void test_2() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"));
			Resource result1 = $this.getFact(":volumeFlow");
			
			assertEquals("59", result1.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}
}
