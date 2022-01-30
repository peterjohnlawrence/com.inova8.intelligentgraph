/*
 * inova8 2020
 */
package remotePathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import utilities.Query;

import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class Remote_Sparql_IntelligentGraphTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Remote_Sparql_IntelligentGraphTests {
	
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		org.eclipse.rdf4j.repository.Repository workingRep = new HTTPRepository("http://localhost:8080/rdf4j-server","calc2graph");
		IntelligentGraphRepository.create(workingRep);
		conn = workingRep.getConnection();
	}

	/**
	 * Ig 1.
	 */
	@Test
	@Order(1)
	void ig_1() {

		try {
			String queryString1 = "select * WHERE {VALUES(?time){(41)} ?s <http://inova8.com/calc2graph/def/testProperty6> $o } limit 1";

			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/id/BatteryLimit1;o=41;time=41;\r\n"
					+ ""
					,result);
//			String result = runSPARQL(conn, queryString1);
//			assertEquals("time=41;o=41;"
//					,result); 
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Ig 2.
	 */
	@Test
	@Order(2)
	void ig_2() {

		try {
			//RepositoryResult<Statement> results = conn.getStatements(null, iri("http://inova8.com/calc2graph/def/volumeFlow"), null);
			//   RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null, null);
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/volumeFlow"), null, iri("http://default"));
			Statement result;
			while( results.hasNext()) {
				result=results.next();
				result.getSubject();
				 result.getObject();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/volumeFlow, \"59\"^^<http://www.w3.org/2001/XMLSchema#int>, http://default) [http://default]",result.toString());
				 break;
				
			}
		//	
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Ig 3.
	 */
	@Test
	@Order(3)
	void ig_3() {

		try {
			String queryString1 = "select ?time $o $o_SCRIPT $o_TRACE  WHERE {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/volumeFlow> $o,$o_SCRIPT, $o_TRACE } limit 1";

			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("o_SCRIPT=59;;o_TRACE=<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a>, by invoking <b>javascript</b> script\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >59;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a> =  59^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol></ol>;time=41;o=59;"
					+ "",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 4.
	 */
	@Test
	@Order(4)
	void ig_4() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/massFlow> $o} limit 1";

			String result =  Query.runQuery(conn, queryString1);
			assertEquals("time=41;o=24.77999922633171;"
					,result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 5.
	 */
	@Test
	@Order(5)
	void ig_5() {

		try {
			String queryString1 = "select * {BIND(\"42*3;\"^^<http://inova8.com/script/groovy> as ?result) } ";

			String result =  Query.runQuery(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 6.
	 */
	@Test
	@Order(6)
	void ig_6() {

		try {
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 7.
	 */
	@Test
	@Order(7)
	void ig_7() {

		try {
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select * "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\r\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :totalSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 8.
	 */
	@Test
	@Order(8)
	void ig_8() {

		try {
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select * "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\r\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 9.
	 */
	@Test
	@Order(9)
	void ig_9() {

		try {

			String queryString1 = "select   ?o   { ?s  <http://inova8.com/script/scriptCode> ?o } limit 1";
			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/id/deduceDensity;o=$this.prefix(\"<http://inova8.com/calc2graph/def/>\");\r\n"
					+ "var result= $this.getFact(\":density\").floatValue();  \r\n"
					+ "result;;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 10.
	 */
	@Test
	@Order(10)
	void ig_10() {
		try {
			String queryString1 = "select   ?o "
					+ "{\r\n"
					+ "  <http://inova8.com/calc2graph/id/Attribute_3>  <http://inova8.com/calc2graph/def/attribute.value> ?o }";
			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("o=Evaluated null for http://inova8.com/calc2graph/def/attribute.value of http://inova8.com/calc2graph/id/Attribute_3, using script \"$this.prefix(\"<http://inova8.com/calc2graph/def/>\");\r\n"
					+ "var result= $this.getFact(\":density\").floatValue();  \r\n"
					+ "result;\"^^<http://inova8.com/script/groovy>;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 11.
	 */
	@Test
	@Order(11)
	void ig_11() {
		try {
			String queryString1 = "select   ?o "
					+ "{\r\n"
					+ "  <http://inova8.com/calc2graph/id/BatteryLimit1>  <http://inova8.com/calc2graph/def/testProperty2> ?o }";
			String result =  Query.runSPARQL(conn, queryString1);
			assertEquals("o=javax.script.ScriptException: Exceptions.ScriptFailedException: javax.script.ScriptException: Exceptions.CircularReferenceException: Circular reference encountered when evaluating <http://inova8.com/calc2graph/def/testProperty2> of <http://inova8.com/calc2graph/id/BatteryLimit1>.\r\n"
					+ "[<http://inova8.com/calc2graph/def/testProperty2> <http://inova8.com/calc2graph/id/BatteryLimit1>; queryOptions={o=\"$this.prefix(\"<http://inova8.com/calc2graph/def/>\"); $this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>}\r\n"
					+ ", <http://inova8.com/calc2graph/def/testProperty3> <http://inova8.com/calc2graph/id/BatteryLimit1>; queryOptions={o=\"$this.prefix(\"<http://inova8.com/calc2graph/def/>\"); $this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>}\r\n"
					+ "];",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
