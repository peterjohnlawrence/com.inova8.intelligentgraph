/*
 * inova8 2020
 */
package remotePathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.intelligentgraph.vocabulary.XSD;

/**
 * The Class Remote_GettingStartedTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Remote_GettingStartedTests {
	
	/** The source. */
//	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	private static IntelligentGraphRepository source;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
//	private static Evaluator evaluator;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {


	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph1/>");
			source.prefix("rdfs","<http://www.w3.org/2000/01/rdf-schema#>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph1>");
			source.prefix("<http://inova8.com/calc2graph/testGraph1/>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			String averageSalesScript = "totalSales=0; count=0;for(sales in _this.getFacts(':sales')){totalSales +=  sales.doubleValue();count++}; if(count==0) return Double.POSITIVE_INFINITY else return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			Resource average = myCountry.getFact(":averageSales");
			assertEquals("3.0", average.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph1/>");
			Graph graph1 = source.openGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph1.getThing(":Country1");
			String performanceCalculation = "2*3";
			myCountry.addFact(":salesPerformance", performanceCalculation, SCRIPT.GROOVY) ;
			
			ResourceResults results = myCountry.getFacts(":salesPerformance") ;
			for(Resource result:results) {
				assertEquals("6", result.getValue().stringValue());
			}
	//		source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			source.closeGraph("<http://inova8.com/calc2graph/testGraph1>");
			
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Test 20.
	 */
	@Test
	@Order(20)
	void test_20() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph2/>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			myCountry.addFact(":sales", "60");
			String averageSalesScript = "totalSales=0; count=0;for(sales in _this.getFacts(\"<http://inova8.com/calc2graph/testGraph2/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			
			Double averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(12.5, averageCountrySales);
	//		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			source.closeGraph("<http://inova8.com/calc2graph/testGraph2>");
			
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Test 30.
	 */
	@Test
	@Order(30)
	void test_30() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph3/>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "10");
			myCountry.addFact(":sales", "20");
			myCountry.addFact(":sales", "30");
			myCountry.addFact(":sales", "40");
			myCountry.addFact(":sales", "50");
			String totalSalesScript = "return _this.getFacts(\":sales\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, SCRIPT.GROOVY) ;
			
			Double totalCountrySales = myCountry.getFact(":totalSales").doubleValue() ;
			assertEquals(150.0, totalCountrySales);
			source.closeGraph("<http://inova8.com/calc2graph/testGraph3>");
			
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Test 40.
	 */
	@Test
	@Order(40)
	void test_40() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph4/>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph4>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "100");
			myCountry.addFact(":sales", "200");
			myCountry.addFact(":sales", "300");
			myCountry.addFact(":sales", "400");
			myCountry.addFact(":sales", "500");
			String averageSalesScript = "return _this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			Double averageCountrySales;
			averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(300.0, averageCountrySales);
		    averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(300.0, averageCountrySales);
			myCountry.addFact(":sales", "600");
			 averageCountrySales = myCountry.getFact(":averageSales").doubleValue() ;
			assertEquals(350, averageCountrySales);
			source.closeGraph("<http://inova8.com/calc2graph/testGraph4>");
			
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

	/**
	 * Test 50.
	 */
	@Test
	@Order(50)
	void test_50() {
		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://localhost:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/calc2graph/testGraph5/>");
			Graph graph = source.openGraph("<http://inova8.com/calc2graph/testGraph5>");
			Thing Attribute = graph.getThing(":Attribute").addFact(RDFS.SUBCLASSOF, RDF.STATEMENT);
			Thing AttributeType = graph.getThing(":AttributeType");
			graph.getThing(":attributeOf").addFact(RDFS.SUB_PROPERTY_OF, RDF.SUBJECT).addFact(RDFS.DOMAIN, Attribute).addFact(RDFS.RANGE,RDFS.RESOURCE);
			graph.getThing(":attributeType").addFact(RDFS.SUB_PROPERTY_OF, RDF.PREDICATE).addFact(RDFS.DOMAIN, Attribute).addFact(RDFS.RANGE, AttributeType);
			graph.getThing(":attributeMeasurement").addFact(RDFS.SUB_PROPERTY_OF, RDF.OBJECT).addFact(RDFS.DOMAIN, Attribute).addFact(RDFS.RANGE, XSD.STRING);		

			Thing myCountry = graph.getThing(":Country1");
			String performanceCalculation = "2*3";
			myCountry.addFact(":Attribute@:salesPerformance", performanceCalculation, SCRIPT.GROOVY) ;
			
			ResourceResults results = myCountry.getFacts(":Attribute@:salesPerformance") ;
			//if(results.hasNext()) {
				for(Resource result:results) {
					assertEquals("6", result.getValue().stringValue());
					break;
				}
		//		source.removeGraph("<http://inova8.com/calc2graph/testGraph>");
				source.closeGraph("<http://inova8.com/calc2graph/testGraph5>");
			//}else {
			//	fail();
			//}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

}
