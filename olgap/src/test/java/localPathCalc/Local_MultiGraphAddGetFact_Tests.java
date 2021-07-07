/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathCalc.Trace;
import pathPatternProcessor.PathPatternException;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.ResourceResults;
import utilities.Query;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_MultiGraphAddGetFact_Tests {

	/** The source. */
	private static PathQLRepository source;
	
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
		//workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_MultiGraphAddGetFact_Tests/");
		workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/Local_MultiGraphAddGetFact_Tests/");
	//	Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
 		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source =  PathQLRepository.create(workingRep);

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
		//	Thing myCountry = addGraph2();
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");

			String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			CustomQueryOptions  customQueryOptions = new CustomQueryOptions();
			customQueryOptions.add("time",42);
		    customQueryOptions.add("name","Peter");
			Double averageCountrySales = myCountry.getFact(":averageSales",customQueryOptions).doubleValue() ;
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");

			assertEquals(3.0, averageCountrySales);
			
		} catch (Exception e) {
			fail();
		}
	}
	/**
	 * Test 20.
	 */
	@Test
	@Order(25)
	void test_25() {
		try {
		//	Thing myCountry = addGraph2();
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");

			String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			CustomQueryOptions  customQueryOptions = new CustomQueryOptions();
			customQueryOptions.add("time",42);
		    customQueryOptions.add("name","Peter");
			Trace averageCountrySalesTrace = myCountry.traceFact(":averageSales",customQueryOptions) ;
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			//Query.assertEqualsWOSpaces
			assertEquals
			("<ol style='list-style-type:none;'><li>Getting facts  ':averageSales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a></li></li><li>...using options: [name=&quot;Peter&quot;&amp;time=&quot;42&quot;^^&lt;http://www.w3.org/2001/XMLSchema#int&gt;]</li></li><li>...within contexts: [file://src/test/resources/calc2graph.def.ttl, http://inova8.com/calc2graph/testGraph2]</li></li><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/averageSales' target='_blank'>averageSales</a> of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a>, by invoking <b>groovy</b> script\n"
					+ "</li></li><li><div  style='border: 1px solid black;'> <pre><code >totalSales=0; count=0;for(sales in $this.getFacts(&quot;&lt;http://inova8.com/calc2graph/def/sales&gt;&quot;)){totalSales += &nbsp;sales.doubleValue();count++}; return totalSales/count;</code></pre></div></li><ol style='list-style-type:none;'><li>Getting facts '&lt;http://inova8.com/calc2graph/def/sales&gt;' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> </li></li><li>Returned fact 'http://inova8.com/calc2graph/def/sales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 1</li></li><li>Returned fact 'http://inova8.com/calc2graph/def/sales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 2</li></li><li>Returned fact 'http://inova8.com/calc2graph/def/sales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 3</li></li><li>Returned fact 'http://inova8.com/calc2graph/def/sales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 4</li></li><li>Returned fact 'http://inova8.com/calc2graph/def/sales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 5</li></li></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/averageSales' target='_blank'>averageSales</a> of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> =  3.0^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li></ol><li>Calculated <a href='http://inova8.com/calc2graph/def/averageSales' target='_blank'>averageSales</a> of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 3.0^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li><li>Retrieved cached value <a href='http://inova8.com/calc2graph/def/averageSales' target='_blank'>averageSales</a> of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 3.0^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li><li>Returned fact 'http://inova8.com/calc2graph/def/averageSales' of <a href='http://inova8.com/calc2graph/def/Country' target='_blank'>Country</a> = 3.0^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li><p></ol>", 
					averageCountrySalesTrace.asHTML());
			
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
			//Thing myCountry = addGraph3();
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
			Thing myCountry = graph.getThing(":Country");
			myCountry.addFact(":sales", "10");
			myCountry.addFact(":sales", "20");
			myCountry.addFact(":sales", "30");
			myCountry.addFact(":sales", "40");
			myCountry.addFact(":sales", "50");
			String totalSalesScript = "return $this.getFacts(\":sales\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, Evaluator.GROOVY) ;
			
			Double totalCountrySales = myCountry.getFact(":totalSales").doubleValue() ;
			assertEquals(150.0, totalCountrySales);
			myCountry.deleteFacts(":sales");
		//	source.closeGraph("<http://inova8.com/calc2graph/testGraph3>");
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
			//Thing myCountry = addGraph4();
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph4>");
			Thing myCountry = graph.getThing(":Country");
			myCountry.addFact(":sales", "100");
			myCountry.addFact(":sales", "200");
			myCountry.addFact(":sales", "300");
			myCountry.addFact(":sales", "400");
			myCountry.addFact(":sales", "500");
			String averageSalesScript = "return $this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			
			Double averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(300.0, averageCountrySales);
		    myCountry.addFact(":sales", "600");
		//	source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		    averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(350, averageCountrySales);
			source.closeGraph("<http://inova8.com/calc2graph/testGraph4>");
		//	source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
			
		} catch (Exception e) {
			fail();
		}
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
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
}
