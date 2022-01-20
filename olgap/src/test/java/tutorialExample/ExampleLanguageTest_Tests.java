/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.assertEquals;
import java.time.LocalDateTime;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;

import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExampleLanguageTest_Tests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	static RepositoryConnection conn; 
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		//workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/ExampleLanguageTest_Tests/");
		workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/ExampleLanguageTest_Tests/");
		Query.addFile(workingRep, "src/test/resources/ExampleLanguageTest.ttl");
		
		 conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/exampleLanguageTest/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2001/XMLSchema#");
		source = IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	@Test
	@Order(1)
	void example1_1() {

		try {
			Thing peter = source.getThing(":aPerson");
			Resource bmi = peter.getFact(":hasBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void example1_2() {

		try {
			Thing peter = source.getThing(":aPerson");
			Resource bmi = peter.getFact(":hasBMIjs");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void example1_3() {

		try {
			Thing peter = source.getThing(":aPerson");
			Resource bmi = peter.getFact(":hasBMIpytest");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void example1_4() {

		try {
			Thing peter = source.getThing(":aPerson");
			Resource bmi = peter.getFact(":hasBMIpy");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void example1_5() {

		try {
			Thing peter = source.getThing(":aPerson");
			CustomQueryOptions  customQueryOptions = new CustomQueryOptions();
			//customQueryOptions.add("aOption", LocalDate.of(2018,2,13));
			//customQueryOptions.add("aOption", LocalDateTime.of(2018,2,13,6,30));
			//customQueryOptions.add("aOption", LocalTime.of(6,30));
			customQueryOptions.add("aOption", LocalDateTime.parse("2018-02-14T06:30"));
			Resource bmi = peter.getFact(":hasCustomQueryOptionTest",customQueryOptions);
			assertEquals("\"2018-02-14T06:30:00.0\"^^<http://www.w3.org/2001/XMLSchema#dateTime>", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void example1_6() {

		try {
			
			String queryString1 = "PREFIX : <http://inova8.com/intelligentgraph/exampleLanguageTest/> select  ?o ?aOption\n"
					+ "{\n"
					+ "BIND (\"2018-02-14T06:30:00.0\"^^<http://www.w3.org/2001/XMLSchema#dateTime> as ?aOption) \n"
					+ "  :aPerson  :hasCustomQueryOptionTest  ?o } ";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("aOption=2018-02-14T06:30:00.0;o=\"2018-02-14T06:30:00.0\"^^<http://www.w3.org/2001/XMLSchema#dateTime>;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
