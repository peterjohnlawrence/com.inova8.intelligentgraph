/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.eclipse.rdf4j.model.util.Values.literal;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.Thing;
import com.inova8.intelligentgraph.pathCalc.Trace;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;

import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example5_Tests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example5_Tests/");
		Query.addFile(workingRep, "src/test/resources/example5.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example5/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("xsd", "http://www.w3.org/2000/01/rdf-schema#");
		source = IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	@Test
	@Order(1)
	void example5_1() {

		try {
			Thing aPerson_Measurement = source.getThing(":aPerson_Measurement_1");
			Resource bmi = aPerson_Measurement.getFact(":hasBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void example5_2() {

		try {
			Thing aPerson_Measurement = source.getThing(":aPerson_Measurement_2");
			Resource bmi = aPerson_Measurement.getFact(":hasBMI");
			assertEquals("22.03856749311295", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void example5_3() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			ResourceResults bmis = aPerson.getFacts("^:measurementOf/:hasBMI");
			assertEquals("\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.49134948096886\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.723183391003463\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.918367346938776\"^^<http://www.w3.org/2001/XMLSchema#double>\"20.571428571428573\"^^<http://www.w3.org/2001/XMLSchema#double>", bmis.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void example5_4() {

		try {
			Resource bmi = source.getThing(":aPerson").getFact("^:measurementOf[:hasOrdinal %1]/:hasBMI",literal(2));
			assertEquals("22.03856749311295", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void example5_5() {

		try {
			Resource bmi = source.getThing(":aPerson").getFact("^:measurementOf[:hasDate %1]/:hasBMI",literal(LocalDate.parse("2021-08-02")));
			assertEquals("22.03856749311295", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void example5_6() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			ResourceResults bmis = aPerson.getFacts("^:measurementOf[:hasDate [lt %1]]/:hasBMI",literal(LocalDate.parse("2021-08-03")));
			assertEquals("\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>", bmis.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(7)
	void example5_7() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			Trace trace = aPerson.traceFact("^:measurementOf[:hasDate [lt %1]]/:hasBMI",literal(LocalDate.parse("2021-08-03")));
			assertEquals("   1. Getting facts '^:measurementOf[:hasDate [lt %1]]/:hasBMI' of aPerson <http://inova8.com/intelligentgraph/example5/aPerson>\r\n"
					+ "   2. ...using options: [1=\"2021-08-03\"^^<http://www.w3.org/2001/XMLSchema#date>]\r\n"
					+ "   3. ...within contexts: [file://src/test/resources/example5.ttl, http://default]\r\n"
					+ "   4. Returned fact 'http://inova8.com/intelligentgraph/example5/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example5/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>", trace.asText());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
