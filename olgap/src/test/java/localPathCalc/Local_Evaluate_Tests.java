/*
 * inova8 2020
 */
package localPathCalc;

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
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLResults.FactBindingsetResults;
import com.inova8.intelligentgraph.pathQLResults.PathQLBindingSetResults;
import com.inova8.pathql.parser.PathQLEvaluator;

import utilities.Query;

/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Deprecated
class Local_Evaluate_Tests {
	
	/** The lucenesail. */
//	static LuceneSail lucenesail ;
	

	
	/** The source. */
	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_Evaluate_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");	
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");

		source = IntelligentGraphRepository.create(workingRep);
//		source.prefix("<http://inova8.com/calc2graph/def/>");
//		source.prefix("id","<http://inova8.com/calc2graph/id/>");
//		source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	@Test
	@Order(1)
	void pathql_1() {

		try {
			PathQLBindingSetResults pathqlResultsIterator = (PathQLBindingSetResults) PathQLEvaluator.evaluate(source,"[ eq id:Unit1]");
			if (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=null, subject=http://inova8.com/calc2graph/id/Unit1],snippet=null, score=null]",
						nextMatch.toString());
				return;
			}else {
				fail();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	/**
	 * Pathql 2.
	 */
	@Test
	@Order(2)
	void pathql_2() {

		try {
			PathQLBindingSetResults pathqlResultsIterator = (PathQLBindingSetResults) PathQLEvaluator.evaluate(source,"[ like 'Unit1']");
			if (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=http://www.w3.org/2000/01/rdf-schema#label, subject=http://inova8.com/calc2graph/id/Location_Unit1],snippet=Location <B>Unit1</B>, score=2.4626340866088867]",
						nextMatch.toString());
				return;
			}else {
				fail();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Pathql 3.
	 */
	@Test
	@Order(3)
	void pathql_3() {
		
		try {
			FactBindingsetResults pathqlResultsIterator = (FactBindingsetResults) PathQLEvaluator.evaluate(source,"[ eq id:Unit1]/:hasProductBatteryLimit");
			if (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				return;
			}else {
							fail();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Pathql 4.
	 */
	@Test
	@Order(4)
	void pathql_4() {
		
		try {
			FactBindingsetResults pathqlResultsIterator = (FactBindingsetResults) PathQLEvaluator.evaluate(source,"[ eq id:Unit1]/:hasProductBatteryLimit/:volumeFlow");
			if (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=\"40\"^^<http://www.w3.org/2001/XMLSchema#int>], predicate=http://inova8.com/calc2graph/def/volumeFlow, subject=http://inova8.com/calc2graph/id/BatteryLimit2]",
						nextMatch.toString());
				return;
			}else {
				fail();
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	
	/**
	 * Pathql 5.
	 */
	@Test
	@Order(5)
	void pathql_5() {
		
		try {
			FactBindingsetResults pathqlResultsIterator = (FactBindingsetResults) PathQLEvaluator.evaluate(source,"[ like \"Unit1\"]>:hasProductBatteryLimit");
			if (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				return;
			}else {
				fail();
			}
		} catch (Exception e) {
						assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

}
