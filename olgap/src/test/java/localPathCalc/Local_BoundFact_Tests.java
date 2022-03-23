/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
import utilities.Query;

/**
 * The Class Local_GetFact_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_BoundFact_Tests {

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
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_BoundFact_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("def", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
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
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	

	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	



	/**
 * Test 1.
 */
@Test
	@Order(1)
	void test_1() {
		try {
			ResourceResults results = source.getFacts("[ rdfs:label [ like \"Unit1\"]; a :Unit]/:hasProductBatteryLimit");
			assertEquals("[ {s=http://inova8.com/calc2graph/id/Unit1, p=http://inova8.com/calc2graph/def/hasProductBatteryLimit, o=http://inova8.com/calc2graph/id/BatteryLimit2}; {s=http://inova8.com/calc2graph/id/Unit1, p=http://inova8.com/calc2graph/def/hasProductBatteryLimit, o=http://inova8.com/calc2graph/id/BatteryLimit3};]", results.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
@Test
@Order(1_1)
	void test_1_1() {
		try {
			ResourceResults results = source.getFacts(":hasProductBatteryLimit");
			assertEquals("[ {s=http://inova8.com/calc2graph/id/Unit1, p=http://inova8.com/calc2graph/def/hasProductBatteryLimit, o=http://inova8.com/calc2graph/id/BatteryLimit2}; {s=http://inova8.com/calc2graph/id/Unit1, p=http://inova8.com/calc2graph/def/hasProductBatteryLimit, o=http://inova8.com/calc2graph/id/BatteryLimit3};]", results.toString());
		} catch (Exception e) {
			assertEquals("Facts query unbound", e.getMessage());
		}
	}
@Test
@Order(1_2)
	void test_1_2() {
		try {
			ResourceResults results = source.getFacts("[eq *]/:hasProductBatteryLimit");
			assertEquals("[http://inova8.com/calc2graph/id/BatteryLimit2;http://inova8.com/calc2graph/id/BatteryLimit3;]", results.toString());
		} catch (Exception e) {
			assertEquals("[line 1:4 in \"[eq *]/:hasProductBatteryLimit\": no viable alternative at input '[eq*']", e.getCause().getMessage());
		}
	}
	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		try {
			Resource result = source.getFact("[eq id:BatteryLimit2]/:volumeFlow");
			assertEquals("40", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}


	/**
	 * Test 2 1.
	 */
	@Test
	@Order(21)
	void test_2_1() {
		try {

			Resource result = source.getFact("[eq id:Unit1]/:hasFeedBatteryLimit/:volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Test 2 2.
	 */
	@Test
	@Order(22)
	void test_2_2() {
		try {
			Resource result = source.getFact("[eq id:BatteryLimit1]/:long");
			assertEquals("501", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}
	
	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {

		try {
			Thing _this = Thing.create(source, iri("http://inova8.com/calc2graph/id/Unit1"), 	null);
			Double fact = 0.0;
			for (Resource batterylimit : source.getFacts("[eq id:Unit1]/:hasProductBatteryLimit")) {
				fact += batterylimit.getFact(":massFlow").doubleValue();
			}
			ResourceResults batterylimits = _this.getFacts(":hasProductBatteryLimit");
			Resource batterylimit;
			while (batterylimits.hasNext()) {
				batterylimit = batterylimits.next();
				fact -= batterylimit.getFact(":massFlow").doubleValue();
			}
			;
			assertEquals(0.0, fact);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {

		try {
			Resource result = source.getFact("[eq id:Unit1]/:massThroughput");
			assertEquals("37.99999952316284", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	

	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {

		try {
			Resource result =  source.getFact("[eq id:BatteryLimit1]/def:Attribute@def:density");
			if (result != null)
				assertEquals(".42", result.stringValue());
			else
				fail();
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {

		try {
			Resource result = source.getFact("[eq id:BatteryLimit3]/<http://inova8.com/calc2graph/def/massFlow>");
			assertEquals("10.0", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {

		try {
			Resource result = source.getFact("[eq id:Unit1]/<http://inova8.com/calc2graph/def/massThroughput>");
			assertEquals("37.99999952316284", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {

		try {

			Resource result = source.getFact("[eq id:BatteryLimit2]/^:hasProductBatteryLimit").getFact(":massThroughput");

			assertEquals("37.99999952316284", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}



	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		try {
			Float result = source.getFact("[eq id:BatteryLimit2]/^:hasProductBatteryLimit/:massThroughput").floatValue();

			assertEquals("38.0", result.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 11.
	 */
	@Test
	@Order(11)
	void test_11() {
		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 12.
	 */
	@Test
	@Order(12)
	void test_12() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 13.
	 */
	@Test
	@Order(13)
	void test_13() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq id:Calc2Graph1]#").getFact(":lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 14.
	 */
	@Test
	@Order(14)
	void test_14() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq id:Calc2Graph2]#"); //""

			if (result != null)
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 15.
	 */
	@Test
	@Order(15)
	void test_15() {
		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/<http://inova8.com/calc2graph/def/long>");

			assertEquals("501", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 16.
	 */
	@Test
	@Order(16)
	void test_16() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Calc2Graph1>]/^:Location@:appearsOn[eq id:BatteryLimit2]#");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 17.
	 */
	@Test
	@Order(17)
	void test_17() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Calc2Graph1>]/^:Location@:appearsOn[eq id:BatteryLimit2]");

			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 18.
	 */
	@Test
	@Order(18)
	void test_18() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Calc2Graph1>]/^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 19.
	 */
	@Test
	@Order(19)
	void test_19() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[ rdfs:label 'Calc2Graph2']#");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 20.
	 */
	@Test
	@Order(20)
	void test_20() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 21.
	 */
	@Test
	@Order(21)
	void test_21() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long");

			assertEquals("501", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 22.
	 */
	@Test
	@Order(22)
	void test_22() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");

			assertEquals("502", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 23.
	 */
	@Test
	@Order(23)
	void test_23() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/BatteryLimit1>]/:Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 24.
	 */
	@Test
	@Order(24)
	void test_24() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Unit2>]/:massThroughput");

			assertEquals("37.99999952316284", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 25.
	 */
	@Test
	@Order(25)
	void test_25() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Unit2>]/:massThroughput");

			assertEquals("37.99999952316284", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 26.
	 */
	@Test
	@Order(26)
	void test_26() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Unit3>]/:massThroughput");

			assertEquals("24.77999922633171", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 27.
	 */
	@Test
	@Order(27)
	void test_27() {

		try {
			Resource result = source.getFact("[eq <http://inova8.com/calc2graph/id/Unit3>]/:massFlowBalance");

			assertEquals("-13.220000296831131", result.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}


}
