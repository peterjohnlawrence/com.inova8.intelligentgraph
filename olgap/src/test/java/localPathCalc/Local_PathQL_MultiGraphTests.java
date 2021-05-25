/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import intelligentGraph.IntelligentGraphConfig;
import intelligentGraph.IntelligentGraphConnection;
import intelligentGraph.IntelligentGraphFactory;
import intelligentGraph.IntelligentGraphSail;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternProcessor.PathPatternException;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_PathQL_MultiGraphTests {

	/** The source. */
	private static PathQLRepository source;
	
	/** The conn. */
	private static RepositoryConnection conn;	

	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		File dataDir = new File("src/test/resources/datadir/Local_PathQL_MultiGraphTests/");
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
		
		LuceneSail  lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");

		Sail baseSail = new NativeStore(dataDir);		
		lucenesail.setBaseSail(baseSail);
		intelligentGraphSail.setBaseSail(lucenesail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		
//		Sail baseSail = new NativeStore(dataDir);		
//		intelligentGraphSail.setBaseSail(baseSail);		
//		lucenesail.setBaseSail(intelligentGraphSail);	
//		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(lucenesail);
//		
		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null));

		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null),iri("http://default"));
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source =  PathQLRepository.create(workingRep);
		//source = PathQLRepository.create(workingRep);
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
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		try {

			Graph graph1 = source.openGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph1.getThing(":Country");
			String performanceCalculation = "2*3";
			myCountry.addFact(":salesPerformance", performanceCalculation, Evaluator.GROOVY) ;
			
			ResourceResults results = myCountry.getFacts(":salesPerformance") ;
			for(Resource result:results) {
				assertEquals("6", result.getValue().stringValue());
			}
			source.closeGraph("<http://inova8.com/calc2graph/testGraph1>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
		} catch (Exception e) {
			fail();
		}
	}
	/**
	 * Test 20.
	 */
	@Test
	@Order(20)
	void test_20() {
		try {
			Thing myCountry = addGraph2();
			String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			
			Double averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			
			source.closeGraph("<http://inova8.com/calc2graph/testGraph2>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals(3.0, averageCountrySales);
			
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test 30.
	 */
	@Test
	@Order(30)
	void test_30() {
		try {
			Thing myCountry = addGraph3();
			String totalSalesScript = "return $this.getFacts(\":sales\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, Evaluator.GROOVY) ;
			
			Double totalCountrySales = myCountry.getFact(":totalSales").doubleValue() ;
			assertEquals(150.0, totalCountrySales);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
			
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test 40.
	 */
	@Test
	@Order(40)
	void test_40() {
		try {
			Thing myCountry = addGraph4();
			String averageSalesScript = "return $this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			
			Double averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(300.0, averageCountrySales);
			myCountry.addFact(":sales", "600");
			 averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(350, averageCountrySales);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
			
		} catch (Exception e) {
			fail();
		}
	}


	/**
	 * Test 50.
	 */
	@Test
	@Order(50)
	void test_50() {
		try {
			Graph graph = source.openGraph("<http://inova8.com/calc2graph/testGraph5>");
			Thing myCountry = graph.getThing(":Country");
			String performanceCalculation = "2*3";
			myCountry.addFact(":Attribute@:salesPerformance", performanceCalculation, Evaluator.GROOVY) ;
			
			ResourceResults results = myCountry.getFacts(":Attribute@:salesPerformance") ;
			for(Resource result:results) {
				assertEquals("6", result.getValue().stringValue());
				break;
			}
			source.removeGraph("<http://inova8.com/calc2graph/testGraph5>");
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Ig 60.
	 */
	@Test
	@Order(60)
	void ig_60() {
		
		try {
			Thing myCountry= addGraph2();
			String averageSalesScript = "return $this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o } limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=javax.script.ScriptException: Exceptions.ScriptFailedException: Error identifying namespace of qName :sales;",result);

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
			Thing myCountry=addGraph3();
			String totalSalesScript = "return $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, Evaluator.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\r\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
					+ "{\r\n"
					+ "  ?s  :totalSales  ?o} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/def/Country;o=150.0;",result);
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
			String averageSalesScript = "return $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			conn.setNamespace("", "http://inova8.com/calc2graph/def/");
			conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?s ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\r\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
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
			String averageSalesScript = "return $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> CONSTRUCT{\r\n"
					+ "  ?s  :averageSales  ?o.\r\n"
					+ "} "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\r\n"
					+ "FROM <file://calc2graph.data.ttl>\r\n"
					+ "FROM <file://calc2graph.def.ttl>\r\n"
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
