/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import static org.eclipse.rdf4j.model.util.Values.literal;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.evaluator.Trace;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;

/**
 * The Class Example6_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example6_Tests {

	/** The source. */
	private static IntelligentGraphRepository source;
	
	/** The working rep. */
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example6_Tests/");
		Query.addFile(workingRep, "src/test/resources/example6.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example6/");
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
	 * Example 6 1.
	 */
	@Test
	@Order(1)
	void example6_1() {

		try {
			Thing aPerson_Measurement = source.getThing(":Measurement_1");
			Resource bmi = aPerson_Measurement.getFact(":hasBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 6 2.
	 */
	@Test
	@Order(2)
	void example6_2() {

		try {
			Thing aPerson_Measurement = source.getThing(":Measurement_2");
			Resource bmi = aPerson_Measurement.getFact(":hasBMI");
			assertEquals("22.03856749311295", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 6 3.
	 */
	@Test
	@Order(3)
	void example6_3() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			ResourceResults bmis = aPerson.getFacts(":Observation@:BMIObservation/:hasBMI");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example6/Measurement_1, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_2, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_3, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"22.49134948096886\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_4, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"19.723183391003463\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_5, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"19.918367346938776\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_6, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"20.571428571428573\"^^<http://www.w3.org/2001/XMLSchema#double>};]", bmis.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 6 5.
	 */
	@Test
	@Order(5)
	void example6_5() {

		try {
			Resource bmi = source.getThing(":aPerson").getFact(":Observation@:BMIObservation[:hasDate %1]/:hasBMI",literal(LocalDate.parse("2021-08-02")));
			assertEquals("22.03856749311295", bmi.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 6 6.
	 */
	@Test
	@Order(6)
	void example6_6() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			ResourceResults bmis = aPerson.getFacts(":Observation@:BMIObservation[:hasDate [lt %1]]/:hasBMI",literal(LocalDate.parse("2021-08-03")));
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example6/Measurement_1, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example6/Measurement_2, p=http://inova8.com/intelligentgraph/example6/hasBMI, o=\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>};]", bmis.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Example 6 7.
	 */
	@Test
	@Order(7)
	void example6_7() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			Trace trace = aPerson.traceFact(":Observation@:BMIObservation[:hasDate [lt %1]]/:hasBMI",literal(LocalDate.parse("2021-08-03")));
			Query.assertEqualsWOSpaces("   1. Getting facts ':Observation@:BMIObservation[:hasDate [lt %1]]/:hasBMI' of aPerson <http://inova8.com/intelligentgraph/example6/aPerson>\r\n"
					+ "   2. ...using options: [1=\"2021-08-03\"^^<http://www.w3.org/2001/XMLSchema#date>]\r\n"
					+ "   3. ...within contexts: [http://default, file://src/test/resources/example6.ttl]\r\n"
					+ "   4. Returned fact 'http://inova8.com/intelligentgraph/example6/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example6/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>", trace.asText());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
