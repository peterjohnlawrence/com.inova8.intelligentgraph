/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;

/**
 * The Class ExampleLanguageTest_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExampleLanguageTest_Tests {

	/** The source. */
	private static IntelligentGraphRepository source;
	
	/** The working rep. */
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	
	/** The conn. */
	static RepositoryConnection conn; 
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		//workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/ExampleLanguageTest_Tests/");
		workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/ExampleLanguageTest_Tests/");
		Query.addFile(workingRep, "src/test/resources/ExampleLanguageTest.ttl");
		
		 conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/exampleLanguageTest/");
		conn.setNamespace(XSD.PREFIX, XSD.NAMESPACE);
		conn.setNamespace( RDF.PREFIX, RDF.NAMESPACE);
		conn.setNamespace(RDFS.PREFIX , RDFS.NAMESPACE);
		conn.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
		source = IntelligentGraphRepository.create(workingRep);

	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	
	/**
	 * Example 1 1.
	 */
	@Test
	@Order(1)
	void example1_1() {

		try {
			Thing peter = source.getThing(":aPerson");
			Resource bmi = peter.getFact(":hasBMIgroovy");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 1 2.
	 */
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
	
	/**
	 * Example 1 3.
	 */
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
	
	/**
	 * Example 1 4.
	 */
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
	
	/**
	 * Example 1 5.
	 */
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
	
	/**
	 * Example 1 6.
	 */
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
