/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import utilities.Query;

/**
 * The Class SPARQL_To_TupleExpr.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SPARQL_To_TupleExpr {

	/** The conn. */
	private static RepositoryConnection conn;	
	
	/** The working rep. */
	private static org.eclipse.rdf4j.repository.Repository workingRep ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/SPARQL_To_TupleExpr/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
 		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		IntelligentGraphRepository.create(workingRep);

	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		conn.close();
	}


	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		
		try {
			//rdf:type[:hasHeight  [ lt '1.7' ]]/:hasBMI\r\n
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{\r\n"
					+ "?p rdf:type :Person . \r\n"
					+ "?p :hasHeight ?h .\r\n"
					+ "FILTER( ?h < '1.7') .\r\n"
					+ "?p :hasBMI ?bmi\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"p\"\r\n"
					+ "      ProjectionElem \"h\"\r\n"
					+ "      ProjectionElem \"bmi\"\r\n"
					+ "   Filter\r\n"
					+ "      Compare (<)\r\n"
					+ "         Var (name=h)\r\n"
					+ "         ValueConstant (value=\"1.7\")\r\n"
					+ "      Join\r\n"
					+ "         Join\r\n"
					+ "            StatementPattern\r\n"
					+ "               Var (name=p)\r\n"
					+ "               Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "               Var (name=_const_ed84ac24_uri, value=http://defaultPerson, anonymous)\r\n"
					+ "            StatementPattern\r\n"
					+ "               Var (name=p)\r\n"
					+ "               Var (name=_const_741a1d52_uri, value=http://defaulthasHeight, anonymous)\r\n"
					+ "               Var (name=h)\r\n"
					+ "         StatementPattern\r\n"
					+ "            Var (name=p)\r\n"
					+ "            Var (name=_const_16405533_uri, value=http://defaulthasBMI, anonymous)\r\n"
					+ "            Var (name=bmi)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		
		try {
			//rdf:type[ lt '1.7' ]\r\n
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{\r\n"
					+ "?p rdf:type :Person . \r\n"
					+ "FILTER( ?p < '1.7') .\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"p\"\r\n"
					+ "   Filter\r\n"
					+ "      Compare (<)\r\n"
					+ "         Var (name=p)\r\n"
					+ "         ValueConstant (value=\"1.7\")\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=p)\r\n"
					+ "         Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "         Var (name=_const_ed84ac24_uri, value=http://defaultPerson, anonymous)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void test_3() {
		
		try {
			// *[rdf:label [ like "Unit" ; a :Unit]/:hasProductBatteryLimit
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{?bind rdfs:label ?b1 . FILTER REGEX (?b1 , 'Unit1') .\r\n"
					+ "?bind a  :Unit  .\r\n"
					+ "BIND(?bind  as ?n0)\r\n"
				 	+ "?n0 :hasProductBatteryLimit ?n1\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"bind\"\r\n"
					+ "      ProjectionElem \"b1\"\r\n"
					+ "      ProjectionElem \"n0\"\r\n"
					+ "      ProjectionElem \"n1\"\r\n"
					+ "   Join\r\n"
					+ "      Filter\r\n"
					+ "         Regex\r\n"
					+ "            Var (name=b1)\r\n"
					+ "            ValueConstant (value=\"Unit1\")\r\n"
					+ "         Extension\r\n"
					+ "            Join\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_9285ccfc_uri, value=http://www.w3.org/2000/01/rdf-schema#label, anonymous)\r\n"
					+ "                  Var (name=b1)\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "                  Var (name=_const_3433d213_uri, value=http://defaultUnit, anonymous)\r\n"
					+ "            ExtensionElem (n0)\r\n"
					+ "               Var (name=bind)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=n0)\r\n"
					+ "         Var (name=_const_eec771f2_uri, value=http://defaulthasProductBatteryLimit, anonymous)\r\n"
					+ "         Var (name=n1)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void test_4() {
		
		try {
			// *[rdf:label [ like "Unit" ; a :Unit]/:hasProductBatteryLimit
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{?bind rdfs:label ?b1 . FILTER(?b1 = 'Unit1') .\r\n"
					+ "?bind a  :Unit  .\r\n"
					+ "BIND(?bind  as ?n0)\r\n"
				 	+ "?n0 :hasProductBatteryLimit ?n1\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"bind\"\r\n"
					+ "      ProjectionElem \"b1\"\r\n"
					+ "      ProjectionElem \"n0\"\r\n"
					+ "      ProjectionElem \"n1\"\r\n"
					+ "   Join\r\n"
					+ "      Filter\r\n"
					+ "         Compare (=)\r\n"
					+ "            Var (name=b1)\r\n"
					+ "            ValueConstant (value=\"Unit1\")\r\n"
					+ "         Extension\r\n"
					+ "            Join\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_9285ccfc_uri, value=http://www.w3.org/2000/01/rdf-schema#label, anonymous)\r\n"
					+ "                  Var (name=b1)\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "                  Var (name=_const_3433d213_uri, value=http://defaultUnit, anonymous)\r\n"
					+ "            ExtensionElem (n0)\r\n"
					+ "               Var (name=bind)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=n0)\r\n"
					+ "         Var (name=_const_eec771f2_uri, value=http://defaulthasProductBatteryLimit, anonymous)\r\n"
					+ "         Var (name=n1)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void test_5() {
		
		try {
			// *[rdf:label [ like "Unit" ; a :Unit]/:hasProductBatteryLimit
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{?bind rdfs:label ?b1 .\r\n"
					+ "?bind a  :Unit  .\r\n"
					+ "BIND(?bind  as ?n0)\r\n"
				 	+ "?n0 :hasProductBatteryLimit ?n1\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"bind\"\r\n"
					+ "      ProjectionElem \"b1\"\r\n"
					+ "      ProjectionElem \"n0\"\r\n"
					+ "      ProjectionElem \"n1\"\r\n"
					+ "   Join\r\n"
					+ "      Extension\r\n"
					+ "         Join\r\n"
					+ "            StatementPattern\r\n"
					+ "               Var (name=bind)\r\n"
					+ "               Var (name=_const_9285ccfc_uri, value=http://www.w3.org/2000/01/rdf-schema#label, anonymous)\r\n"
					+ "               Var (name=b1)\r\n"
					+ "            StatementPattern\r\n"
					+ "               Var (name=bind)\r\n"
					+ "               Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "               Var (name=_const_3433d213_uri, value=http://defaultUnit, anonymous)\r\n"
					+ "         ExtensionElem (n0)\r\n"
					+ "            Var (name=bind)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=n0)\r\n"
					+ "         Var (name=_const_eec771f2_uri, value=http://defaulthasProductBatteryLimit, anonymous)\r\n"
					+ "         Var (name=n1)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void test_6() {
		
		try {
			// *[rdf:label [ like "Unit" ; a :Unit]/:hasProductBatteryLimit
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ "{?bind a  :Unit  .\r\n"
					+ "?bind rdfs:label ?b1 . FILTER(?b1 = 'Unit1') .\r\n"
					+ "BIND(?bind  as ?n0)\r\n"
				 	+ "?n0 :hasProductBatteryLimit ?n1\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"bind\"\r\n"
					+ "      ProjectionElem \"b1\"\r\n"
					+ "      ProjectionElem \"n0\"\r\n"
					+ "      ProjectionElem \"n1\"\r\n"
					+ "   Join\r\n"
					+ "      Filter\r\n"
					+ "         Compare (=)\r\n"
					+ "            Var (name=b1)\r\n"
					+ "            ValueConstant (value=\"Unit1\")\r\n"
					+ "         Extension\r\n"
					+ "            Join\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "                  Var (name=_const_3433d213_uri, value=http://defaultUnit, anonymous)\r\n"
					+ "               StatementPattern\r\n"
					+ "                  Var (name=bind)\r\n"
					+ "                  Var (name=_const_9285ccfc_uri, value=http://www.w3.org/2000/01/rdf-schema#label, anonymous)\r\n"
					+ "                  Var (name=b1)\r\n"
					+ "            ExtensionElem (n0)\r\n"
					+ "               Var (name=bind)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=n0)\r\n"
					+ "         Var (name=_const_eec771f2_uri, value=http://defaulthasProductBatteryLimit, anonymous)\r\n"
					+ "         Var (name=n1)\r\n"
					+ "",result);
			String explanation = Query.explainSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "\u2560\u2550\u2550ProjectionElemList\r\n"
					+ "\u2551     ProjectionElem \"bind\"\r\n"
					+ "\u2551     ProjectionElem \"b1\"\r\n"
					+ "\u2551     ProjectionElem \"n0\"\r\n"
					+ "\u2551     ProjectionElem \"n1\"\r\n"
					+ "\u255A\u2550\u2550Join (JoinIterator)\r\n"
					+ "   \u251C\u2500\u2500Extension\r\n"
					+ "   \u2502  \u2560\u2550\u2550Join (JoinIterator)\r\n"
					+ "   \u2502  \u2551  \u251C\u2500\u2500StatementPattern (costEstimate=0, resultSizeEstimate=0)\r\n"
					+ "   \u2502  \u2551  \u2502     Var (name=bind)\r\n"
					+ "   \u2502  \u2551  \u2502     Var (name=_const_f5e5585a_uri, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, anonymous)\r\n"
					+ "   \u2502  \u2551  \u2502     Var (name=_const_3433d213_uri, value=http://defaultUnit, anonymous)\r\n"
					+ "   \u2502  \u2551  \u2514\u2500\u2500Filter (costEstimate=0, resultSizeEstimate=0)\r\n"
					+ "   \u2502  \u2551     \u2560\u2550\u2550Compare (=)\r\n"
					+ "   \u2502  \u2551     \u2551     Var (name=b1)\r\n"
					+ "   \u2502  \u2551     \u2551     ValueConstant (value=\"Unit1\")\r\n"
					+ "   \u2502  \u2551     \u255A\u2550\u2550StatementPattern (costEstimate=11, resultSizeEstimate=119)\r\n"
					+ "   \u2502  \u2551           Var (name=bind)\r\n"
					+ "   \u2502  \u2551           Var (name=_const_9285ccfc_uri, value=http://www.w3.org/2000/01/rdf-schema#label, anonymous)\r\n"
					+ "   \u2502  \u2551           Var (name=b1)\r\n"
					+ "   \u2502  \u255A\u2550\u2550ExtensionElem (n0)\r\n"
					+ "   \u2502        Var (name=bind)\r\n"
					+ "   \u2514\u2500\u2500StatementPattern (costEstimate=0, resultSizeEstimate=0)\r\n"
					+ "         Var (name=n0)\r\n"
					+ "         Var (name=_const_eec771f2_uri, value=http://defaulthasProductBatteryLimit, anonymous)\r\n"
					+ "         Var (name=n1)\r\n"
					+ "",explanation);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

}
