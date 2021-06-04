/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import utilities.Query;

/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_Evaluate_Tests {
	
	/** The lucenesail. */
//	static LuceneSail lucenesail ;
	

	
	/** The source. */
	private static PathQLRepository source;
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

		source = PathQLRepository.create(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
	}
	/**
	 * Pathql 1.
	 */
	@Test
	@Order(1)
	void pathql_1() {

		try {
			PathQLResults pathqlResultsIterator = (PathQLResults) PathQL.evaluate(source,"[ eq :Unit1]");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=null, subject=http://inova8.com/calc2graph/def/Unit1],snippet=null, score=null]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	/**
	 * Pathql 2.
	 */
	@Test
	@Order(2)
	void pathql_2() {

		try {
			PathQLResults pathqlResultsIterator = (PathQLResults) PathQL.evaluate(source,"[ like 'Unit1']");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=http://www.w3.org/2000/01/rdf-schema#label, subject=http://inova8.com/calc2graph/id/Location_Unit1],snippet=Location <B>Unit1</B>, score=2.309943199157715]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Pathql 3.
	 */
	@Test
	@Order(3)
	void pathql_3() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ eq :Unit1]/:hasProductBatteryLimit");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Pathql 4.
	 */
	@Test
	@Order(4)
	void pathql_4() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ eq :Unit1]/:hasProductBatteryLimit/:volumeFlow");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=\"40\"^^<http://www.w3.org/2001/XMLSchema#int>], predicate=http://inova8.com/calc2graph/def/volumeFlow, subject=http://inova8.com/calc2graph/id/BatteryLimit2]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Pathql 5.
	 */
	@Test
	@Order(5)
	void pathql_5() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ like \"Unit1\"]>:hasProductBatteryLimit");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

}
