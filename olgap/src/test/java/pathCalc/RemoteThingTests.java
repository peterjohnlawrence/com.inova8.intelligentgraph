package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Stack;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.ClearCache;
import olgap.Evaluator;
import pathQL.Match;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;
import pathQLResults.ResourceResults;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RemoteThingTests {
	static RepositoryTripleSource repositoryTripleSource;
	private static PathQLRepository source;
	private static Evaluator evaluator;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		//Repository workingRep = new HTTPRepository("http://localhost:8080/rdf4j-server","calc2graph");
		org.eclipse.rdf4j.repository.Repository workingRep = new SPARQLRepository("http://localhost:8080/rdf4j-server/repositories/calc2graph");
		source = new PathQLRepository(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs","<http://www.w3.org/2000/01/rdf-schema#>");
		evaluator = new Evaluator();
	}
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
	@Test
	@Order(1)
	void search_1() {
		
		try {
			MatchResults searchResultsIterator = Match.entityMatch("Unit2");
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
	@Test
	@Order(1)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_1() {
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/volumeFlow>");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void test_2() {
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact(":volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	@Test
	@Order(21)
	void test_2_1() {
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":hasFeedBatteryLimit/:volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
		
	}
	@Test
	@Order(3)
	void test_3() {
		
		try {
			Thing $this = source.thingFactory( null, iri("http://inova8.com/calc2graph/id/Unit1"), new Stack<String>(),null);
			$this.prefix("http://inova8.com/calc2graph/def/");
			Double fact = 0.0;
			for( Resource batterylimit: $this.getFacts(":hasProductBatteryLimit")) {
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
	@Test
	@Order(4)
	void test_4() {
		
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":massThroughput");
			assertEquals("-0.06740710663143545", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(5)
	void test_5() {
		
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
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
	@Test
	@Order(6)
	void test_6() {
		
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit3"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massFlow>");
			assertEquals("-0.03370355331571773", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(7)
	void test_7() {
		
		try {
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massThroughput>");
			assertEquals("-0.06740710663143545", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	@Test
	@Order(8)
	void test_8() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Resource result = $this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput");

			assertEquals("-0.06740710663143545", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(9)
	void test_9() {		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact(":massFlow").floatValue()/$this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput").floatValue();
			
			assertEquals("0.5", result.toString());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(10)
	void test_10() {		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact("^:hasProductBatteryLimit/:massThroughput").floatValue();
			
			 assertEquals("-0.06740711",result.toString());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(11)
	void test_11() {		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact(":lat");
			
			 assertEquals("400",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(12)
	void test_12() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(13)
	void test_13() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#").getFact(":lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(14)
	void test_14() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph2]#");
				
			if(result!=null) 
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(15)
	void test_15() {		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/long>");

			 assertEquals("501",result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}

	@Test
	@Order(16)
	void test_16() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]#");
				
			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(17)
	void test_17() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]");
				
			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
			}catch(Exception e){
				fail();
				e.printStackTrace();
			}
	}
	@Test
	@Order(18)
	void test_18() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(19)
	void test_19() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#");
			
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(20)
	void test_20() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat");
			
			assertEquals("400", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(21)
	void test_21() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long");
			
			assertEquals("501", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(22)
	void test_22() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
			
			assertEquals("502", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(23)
	void test_23() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]");
			
			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(24)
	void test_24() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(25)
	void test_25() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("23.43999981880188", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(26)
	void test_26() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit3"), null);

			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");
			
			assertEquals("24.77999922633171", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	@Test
	@Order(27)
	void test_27() {
		
		try{
			Thing $this = new Thing(source, iri("http://inova8.com/calc2graph/id/Unit3"), null);
			$this.prefix("id","<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massFlowBalance");
			
			assertEquals("1.339999407529831", result.stringValue());
			}catch(Exception e){
				e.printStackTrace();
			}
	}
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
