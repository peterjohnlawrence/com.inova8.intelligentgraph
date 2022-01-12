/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.processor.PathPatternException;

import utilities.Query;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SPARQL_To_TupleExpr {

	/** The source. */
	private static IntelligentGraphRepository source;
	
	/** The conn. */
	private static RepositoryConnection conn;	
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
		source =  IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		conn.close();
	}


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
			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
					+ " {\r\n"
					+ "FILTER(?n1 < \"4\")\r\n"
					+ "FILTER(?n1 >= \"2\")\r\n"
					+ "{{ ?r1 <http://inova8.com/calc2graph/def/attribute.of.Item> <http://inova8.com/calc2graph/def/Country2>}\r\n"
					+ "UNION\r\n"
					+ "{ <http://inova8.com/calc2graph/def/Country2> <http://inova8.com/calc2graph/def/item.attribute>  ?r1}}\r\n"
					+ "{{ ?r1 <http://inova8.com/calc2graph/def/attribute.Property> <http://inova8.com/calc2graph/def/sales>}\r\n"
					+ "UNION\r\n"
					+ "{ <http://inova8.com/calc2graph/def/sales> <http://inova8.com/calc2graph/def/property.Attribute>  ?r1}}\r\n"
					+ "{ ?r1 <http://inova8.com/calc2graph/def/attribute.value> ?n1}\r\n"
					+ "}";
			String result = Query.parseSPARQL(conn, queryString1);
			assertEquals("Projection\r\n"
					+ "   ProjectionElemList\r\n"
					+ "      ProjectionElem \"s\"\r\n"
					+ "      ProjectionElem \"o\"\r\n"
					+ "   Union\r\n"
					+ "      StatementPattern\r\n"
					+ "         Var (name=s)\r\n"
					+ "         Var (name=_const_29d7f744_uri, value=http://defaultspouse, anonymous)\r\n"
					+ "         Var (name=o)\r\n"
					+ "      Union\r\n"
					+ "         StatementPattern\r\n"
					+ "            Var (name=s)\r\n"
					+ "            Var (name=_const_23e72d59_uri, value=http://defaultparent, anonymous)\r\n"
					+ "            Var (name=o)\r\n"
					+ "         Filter\r\n"
					+ "            Compare (!=)\r\n"
					+ "               Var (name=_anon_3a1af08a_0a4f_4289_8bb0_946b60c755ca, anonymous)\r\n"
					+ "               ValueConstant (value=http://defaultchild)\r\n"
					+ "            StatementPattern\r\n"
					+ "               Var (name=s)\r\n"
					+ "               Var (name=_anon_3a1af08a_0a4f_4289_8bb0_946b60c755ca, anonymous)\r\n"
					+ "               Var (name=o)\r\n"
					+ "",result);

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
//	@Test
//	@Order(3)
//	void test_3() {
//		
//		try {
//			String queryString1 = "PREFIX : <http://default> SELECT * \r\n"
//					+ "{\r\n"
//					+ "?s (:spouse|:parent|!:child) ?o . \r\n"
//					+ "}";
//			String result = Query.parseSPARQL(conn, queryString1);
//			assertEquals("Projection\r\n"
//					+ "   ProjectionElemList\r\n"
//					+ "      ProjectionElem \"s\"\r\n"
//					+ "      ProjectionElem \"o\"\r\n"
//					+ "   Union\r\n"
//					+ "      StatementPattern\r\n"
//					+ "         Var (name=s)\r\n"
//					+ "         Var (name=_const_29d7f744_uri, value=http://defaultspouse, anonymous)\r\n"
//					+ "         Var (name=o)\r\n"
//					+ "      Union\r\n"
//					+ "         StatementPattern\r\n"
//					+ "            Var (name=s)\r\n"
//					+ "            Var (name=_const_23e72d59_uri, value=http://defaultparent, anonymous)\r\n"
//					+ "            Var (name=o)\r\n"
//					+ "         Filter\r\n"
//					+ "            Compare (!=)\r\n"
//					+ "               Var (name=_anon_3a1af08a_0a4f_4289_8bb0_946b60c755ca, anonymous)\r\n"
//					+ "               ValueConstant (value=http://defaultchild)\r\n"
//					+ "            StatementPattern\r\n"
//					+ "               Var (name=s)\r\n"
//					+ "               Var (name=_anon_3a1af08a_0a4f_4289_8bb0_946b60c755ca, anonymous)\r\n"
//					+ "               Var (name=o)\r\n"
//					+ "",result);
//
//		} catch (Exception e) {
//			assertEquals("", e.getMessage());
//			e.printStackTrace();
//		}
//	}
}
