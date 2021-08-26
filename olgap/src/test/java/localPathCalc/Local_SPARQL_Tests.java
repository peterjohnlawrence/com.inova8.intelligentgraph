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
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.processor.PathPatternException;

import utilities.Query;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_SPARQL_Tests {

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
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_SPARQL_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
 		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source =  IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		conn.close();
	}
/**
 * Adds the graph 2.
 *
 * @return the thing
 * @throws RecognitionException the recognition exception
 * @throws PathPatternException the path pattern exception
 */
//	}
	private Thing addGraph2() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
		Thing myCountry = graph.getThing(":Country");
		myCountry.addFact(":sales", "1");
		myCountry.addFact(":sales", "2");
		myCountry.addFact(":sales", "3");
		myCountry.addFact(":sales", "4");
		myCountry.addFact(":sales", "5");
	//	myCountry.addFact(":sales", "60");
		return myCountry;
	}
	
	/**
	 * Adds the graph 3.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph3() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
		Thing myCountry = graph.getThing(":Country");
		myCountry.addFact(":sales", "10");
		myCountry.addFact(":sales", "20");
		myCountry.addFact(":sales", "30");
		myCountry.addFact(":sales", "40");
		myCountry.addFact(":sales", "50");
		return myCountry;
	}
	
	/**
	 * Adds the graph 4.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph4() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph4>");
		Thing myCountry = graph.getThing(":Country");
		myCountry.addFact(":sales", "100");
		myCountry.addFact(":sales", "200");
		myCountry.addFact(":sales", "300");
		myCountry.addFact(":sales", "400");
		myCountry.addFact(":sales", "500");
		return myCountry;
	}
	
	/**
	 * Ig 60.
	 */
	@Test
	@Order(60)
	void ig_60() {
		
		try {
			Thing myCountry= addGraph2();
			String averageSalesScript = "return _this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o } limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=3.0;",result);

		} catch (Exception e) {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(65)
	void ig_65() {
		
		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
			Thing myCountry= addGraph2();
			String averageSalesScript = "return _this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
				//	+ "FROM <http://default>\n"
				//	+ "FROM <file://calc2graph.data.ttl>\n"
				//	+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+"VALUES(?s){(<http://inova8.com/calc2graph/def/Country>)} "
					+ "  ?s  :averageSales  ?o } limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=3.0;",result);

		} catch (Exception e) {
			fail();
			e.printStackTrace();
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		}

		try {
			Thing myCountry=addGraph3();
			String totalSalesScript = "return _this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, SCRIPT.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\r\n"
					+ "FROM <http://default>\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :totalSales  ?o} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=150.0;",result);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
			 result = Query.runSPARQL(conn, queryString1);
			assertEquals("",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	/**
	 * Ig 70.
	 */
	@Test
	@Order(70)
	void ig_70() {

		try {
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "100");
			myCountry.addFact(":sales", "200");
			myCountry.addFact(":sales", "300");
			myCountry.addFact(":sales", "400");
			myCountry.addFact(":sales", "500");
			String totalSalesScript = "return _this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, SCRIPT.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\r\n"
					+ "FROM <http://default>\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :totalSales  ?o} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/def/Country1;o=1500.0;",result);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
			 result = Query.runSPARQL(conn, queryString1);
			assertEquals("",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 80.
	 */
	@Test
	@Order(80)
	void ig_80() {

		try {
			Thing myCountry=addGraph4();
			String averageSalesScript = "return _this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			conn.setNamespace("", "http://inova8.com/calc2graph/def/");
			conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\r\n"
					+ "FROM <http://default>\n"
//					+ "FROM <file://calc2graph.data.ttl>\r\n"
//					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :averageSales  ?o } limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=300.0;",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 90.
	 */
	@Test
	@Order(90)
	void ig_90() {

		try {
			Thing myCountry = addGraph4();
			String averageSalesScript = "return _this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> CONSTRUCT{\r\n"
					+ "  ?s  :averageSales  ?o.\r\n"
					+ "} "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\r\n"
					+ "FROM <http://default>\n"
//					+ "FROM <file://calc2graph.data.ttl>\r\n"
//					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :averageSales  ?o } limit 10";


			String result = Query.runCONSTRUCT(conn, queryString1);
			assertEquals("(http://inova8.com/calc2graph/def/Country, http://inova8.com/calc2graph/def/averageSales, \"300.0\"^^<http://www.w3.org/2001/XMLSchema#double>)\n",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
