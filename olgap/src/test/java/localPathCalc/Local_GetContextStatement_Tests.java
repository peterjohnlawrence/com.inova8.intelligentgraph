/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import intelligentGraph.IntelligentGraphSail;
import utilities.Query;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetContextStatement_Tests {
	
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	//private static PathQLRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_GetContextStatement_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");

		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
	}

	@Test
	@Order(2)
	void ig_2() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(0.7, ((Literal) object).doubleValue(),0);
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}	
	@Test
	@Order(3)
	void ig_3() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				statement.getObject();
			}
			statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(0.7, ((Literal) object).doubleValue(),0);
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void ig_4() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements((org.eclipse.rdf4j.model.Resource)iri("http://inova8.com/calc2graph/id/Unit1"), iri("http://inova8.com/pathql/getFacts"), literal("<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>"));
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals("http://inova8.com/calc2graph/id/BatteryLimit1",  object.stringValue());
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void ig_5() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements((org.eclipse.rdf4j.model.Resource)iri("http://inova8.com/calc2graph/id/Unit1"), iri("http://inova8.com/pathql/getFacts"), literal("<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>/<http://inova8.com/calc2graph/def/density>"));
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(".42",  object.stringValue());
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}

	@Test
	@Order(6)
	void ig_6() {

		try {
			//RepositoryResult<Statement> results = conn.getStatements(null, iri("http://inova8.com/calc2graph/def/volumeFlow"), null);
			//   RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null, null);
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/volumeFlow"), null, iri("file://src/test/resources/calc2graph.data.ttl"));
			Statement result;
			if( results.hasNext()) {
				result=results.next();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/volumeFlow, \"59\"^^<http://www.w3.org/2001/XMLSchema#int>, file://src/test/resources/calc2graph.data.ttl) [file://src/test/resources/calc2graph.data.ttl]",result.toString());
				 return;
				
			}else
				fail();
		//	
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(7)
	void ig_7() {

		try {

			//&time='42'^^xsd:int
			String param1 = URLEncoder.encode("43^^xsd:int", StandardCharsets.UTF_8.toString());
			String param2 = URLEncoder.encode("2019^^xsd:double", StandardCharsets.UTF_8.toString());
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/pathql/getFacts"), literal(":testProperty6"),iri(IntelligentGraphSail.URN_CUSTOM_QUERY_OPTIONS+"?time="+param1+"&date="+param2));
			Statement result;
			if( results.hasNext()) {
				result=results.next();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/testProperty6, \"43\"^^<http://www.w3.org/2001/XMLSchema#integer>) [null]",result.toString());
				 return;
				
			}else
				fail();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(8)
	void ig_8() {

		try {

			//&time='42'^^xsd:int
			String param1 = URLEncoder.encode("43.0", StandardCharsets.UTF_8.toString());
			String param2 = URLEncoder.encode("", StandardCharsets.UTF_8.toString());
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/pathql/getFacts"), literal(":testProperty6"),iri(IntelligentGraphSail.URN_CUSTOM_QUERY_OPTIONS+"?time="+param1+"&date="+param2));
			Statement result;
			if( results.hasNext()) {
				result=results.next();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/testProperty6, \"43.0\") [null]",result.toString());
				 return;
				
			}else
				fail();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
