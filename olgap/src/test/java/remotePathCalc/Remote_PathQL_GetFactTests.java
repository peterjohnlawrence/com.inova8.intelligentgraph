/*
 * inova8 2020
 */
package remotePathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.ClearCache;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQL.Match;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;
import pathQLResults.ResourceResults;

/**
 * The Class RemoteThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Remote_PathQL_GetFactTests {
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	private static PathQLRepository source;
	
	/** The evaluator. */
	private static Evaluator evaluator;

	/** The match. */
	private static Match match;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//org.eclipse.rdf4j.repository.Repository workingRep = new SPARQLRepository("http://localhost:8080/rdf4j-server/repositories/calc2graph");
		SPARQLRepository  workingRep = new SPARQLRepository("http://localhost:8080/rdf4j-server/repositories/calc2graph");
		//org.eclipse.rdf4j.repository.Repository workingRep = new HTTPRepository("http://localhost:8080/rdf4j-server","calc2graph");
		//HTTPRepository workingRep = new HTTPRepository("http://localhost:8080/rdf4j-server","calc2graph");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "text/plain");
		workingRep.setAdditionalHttpHeaders(headers);

		source = PathQLRepository.create(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs","<http://www.w3.org/2000/01/rdf-schema#>");
		match = new Match(source);
	}
	
	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_0() {
		try {
			evaluator.clearCache();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Search 1.
	 */
	@Test
	@Order(1)
	void search_1() {
		
		try {
			MatchResults searchResultsIterator = match.entityMatch("Unit2");
			while(searchResultsIterator.hasNext()) {
				 BindingSet nextSearchResultBindingSet = searchResultsIterator.nextBindingSet();
				int i=1;
			}
			int i=1;
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_1() {
		try {
			//Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit1"),  null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/volumeFlow>");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
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
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit1"),  null);
			Resource result = $this.getFact(":volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
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
			Thing $this =  source.getThing( iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":hasFeedBatteryLimit/:volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
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
			Thing $this = Thing.create(source,  iri("http://inova8.com/calc2graph/id/Unit1"), null);
			$this.prefix("http://inova8.com/calc2graph/def/");
			Double fact = 0.0;
			for( Resource batterylimit: $this.getFacts(":hasProductBatteryLimit")) {
				Resource factValue = batterylimit.getFact(":massFlow");
				fact += batterylimit.getFact(":massFlow").doubleValue();
			}
			ResourceResults batterylimits = $this.getFacts(":hasProductBatteryLimit");
			Resource batterylimit; 
			while(batterylimits.hasNext() ) {
				batterylimit = batterylimits.next();
				fact -= batterylimit.getFact(":massFlow").doubleValue();
			}; 
			assertEquals(0.0, fact);
		} catch (Exception e) {
			fail();
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
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":massThroughput");
			assertEquals("-0.06740710663143545", result.stringValue());
		} catch (Exception e) {
			fail();
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
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("def","<http://inova8.com/calc2graph/def/>");
			Resource result = $this.getFact("def:Attribute@def:density");
			if(result!=null) 
					assertEquals(".42", result.stringValue());
			else
				fail();
		} catch (Exception e) {
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
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit3"),null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massFlow>");
			assertEquals("-0.03370355331571773", result.stringValue());
		} catch (Exception e) {
			fail();
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
			Thing $this = source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massThroughput>");
			assertEquals("-0.06740710663143545", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	
	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {
		
		try{
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Resource result = $this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput");

			assertEquals("-0.06740710663143545", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 9.
	 */
	@Test
	@Order(9)
	void test_9() {		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact(":massFlow").floatValue()/$this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput").floatValue();
			
			assertEquals("0.5", result.toString());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {		
		try{
			Thing $this =source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact("^:hasProductBatteryLimit/:massThroughput").floatValue();
			
			 assertEquals("-0.06740711",result.toString());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 11.
	 */
	@Test
	@Order(11)
	void test_11() {		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"),null);
			Resource result = $this.getFact(":lat");
			
			 assertEquals("400",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 12.
	 */
	@Test
	@Order(12)
	void test_12() {
		
		try{
			Thing $this =source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 13.
	 */
	@Test
	@Order(13)
	void test_13() {
		
		try{
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#").getFact(":lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 14.
	 */
	@Test
	@Order(14)
	void test_14() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph2]#");
				
			if(result!=null) 
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 15.
	 */
	@Test
	@Order(15)
	void test_15() {		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/long>");

			 assertEquals("501",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}

	/**
	 * Test 16.
	 */
	@Test
	@Order(16)
	void test_16() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]#");
				
			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 17.
	 */
	@Test
	@Order(17)
	void test_17() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]");
				
			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 18.
	 */
	@Test
	@Order(18)
	void test_18() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/Calc2Graph1"),null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 19.
	 */
	@Test
	@Order(19)
	void test_19() {
		
		try{
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#");
			
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 20.
	 */
	@Test
	@Order(20)
	void test_20() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 21.
	 */
	@Test
	@Order(21)
	void test_21() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"),null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long");
			
			assertEquals("501", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 22.
	 */
	@Test
	@Order(22)
	void test_22() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
			
			assertEquals("502", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 23.
	 */
	@Test
	@Order(23)
	void test_23() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/BatteryLimit1"),null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]");
			
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 24.
	 */
	@Test
	@Order(24)
	void test_24() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/Unit2"),null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 25.
	 */
	@Test
	@Order(25)
	void test_25() {
		
		try{
			Thing $this =source.getThing( iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 26.
	 */
	@Test
	@Order(26)
	void test_26() {
		
		try{
			Thing $this = source.getThing(  iri("http://inova8.com/calc2graph/id/Unit3"), null);

			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("24.77999922633171", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	/**
	 * Test 27.
	 */
	@Test
	@Order(27)
	void test_27() {
		
		try{
			Thing $this =source.getThing(  iri("http://inova8.com/calc2graph/id/Unit3"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massFlowBalance");
			
			assertEquals("1.339999407529831", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	/**
	 * Test 30.
	 */
	@Test
	@Order(30)
	void test_30() {
		try {
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			Graph graph1 = source.openGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph1.getThing(":Country1");
			String performanceCalculation = "2*3";
			myCountry.addFact(":salesPerformance", performanceCalculation, Evaluator.GROOVY) ;
			
			ResourceResults results = myCountry.getFacts(":salesPerformance") ;
			for(Resource result:results) {
				result.getValue();
				assertEquals("6", result.getValue().stringValue());
			}
			source.removeGraph("<http://inova8.com/calc2graph/testGraph>");
			
		} catch (Exception e) {
			fail();
		}
	}
	/**
	 * Test 31.
	 */
	@Test
	@Order(31)
	void test_31() {
		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\":sales\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			
			Value averageCountrySales = myCountry.getFact(":averageSales").getValue() ;
			assertEquals("3.0", averageCountrySales.stringValue());

			
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test 32.
	 */
	@Test
	@Order(32)
	void test_32() {
		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			String totalSalesScript = "return $this.getFacts(\":sales\").total();";
			myCountry.addFact(":totalSales", totalSalesScript, Evaluator.GROOVY) ;
			
			Value totalCountrySales = myCountry.getFact(":totalSales").getValue() ;
			assertEquals("15.0", totalCountrySales.stringValue());

			
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test 33.
	 */
	@Test
	@Order(33)
	void test_33() {
		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			String averageSalesScript = "return $this.getFacts(\":sales\").average();";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			
			Value averageCountrySales = myCountry.getFact(":averageSales").getValue() ;
			assertEquals("3.0", averageCountrySales.stringValue());

			
		} catch (Exception e) {
			fail();
		}
	}
	
	/**
	 * Test 34.
	 */
	@Test
	@Order(34)
	void test_34() {
		
		try {
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit3"),null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massYield>");
			assertEquals("-0.03370355331571773", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	/**
	 * Test 100.
	 */
	@Test
	@Order(100)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_100() {
		try {
			ClearCache clearCache = new ClearCache();
			org.eclipse.rdf4j.model.Value result = clearCache.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
