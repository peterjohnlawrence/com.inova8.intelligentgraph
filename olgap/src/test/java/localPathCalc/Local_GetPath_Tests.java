/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import path.Path;
import path.PathBinding;
import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQL.Match;
import pathQLModel.MatchFact;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;
import pathQLResults.PathResults;
import pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetPath_Tests {
	

	
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
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_getPath_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("def", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = PathQLRepository.create(workingRep);


	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}	
	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	
	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	

	@Test
	@Order(1)
	void test_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Equipment_1"), null);
			PathResults paths =  $this.getPaths(":connectedTo/:connectedTo/:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_3,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_4,INVERSE]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_3,INVERSE]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
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
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Continuant_1"), null);
			PathResults paths =  $this.getPaths(":Connection@:connectedTo/:Connection@:connectedTo/:Connection@:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_3,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_4,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}


	@Test
	@Order(3)
	void test_3() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Continuant_1"), null);
			PathResults paths =  $this.getPaths(":Connection@:connectedTo/:Connection@:connectedTo/:Connection@:connectedTo#");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_3,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Connection_3_4,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
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
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Continuant_1"), null);
			PathResults paths =  $this.getPaths(":Connection@:connectedTo/:Connection@:connectedTo#/:connection.to.Continuant/:Connection@:connectedTo");
			assertEquals("Path=[[http://inova8.com/calc2graph/id/Continuant_1,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_2,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_2,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Connection_2_3,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Connection_2_3,http://inova8.com/calc2graph/def/connection.to.Continuant,http://inova8.com/calc2graph/id/Continuant_3,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Continuant_3,<http://inova8.com/calc2graph/def/Connection>@http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Continuant_4,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}


	@Test
	@Order(5)
	void test_5() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Path path = $this.getPath(":Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("Path=[[http://inova8.com/calc2graph/id/BatteryLimit1,<http://inova8.com/calc2graph/def/Location>@http://inova8.com/calc2graph/def/appearsOn,http://inova8.com/calc2graph/id/Location_BL1,INVERSE,false]\r\n"
					+ "[http://inova8.com/calc2graph/id/Location_BL1,http://inova8.com/calc2graph/def/lat,\"400\"^^<http://www.w3.org/2001/XMLSchema#integer>,INVERSE]\r\n"
					+ "]\r\n"
					+ "", path.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	@Order(6)
	void test_6() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Path path  = $this.getPath(":Location@:appearsOn[eq id:Calc2Graph2]#"); //""

			if (path != null)
				assertEquals("Path=[[http://inova8.com/calc2graph/id/BatteryLimit1,<http://inova8.com/calc2graph/def/Location>@http://inova8.com/calc2graph/def/appearsOn,http://inova8.com/calc2graph/id/Location_BL1_2,INVERSE,false]\r\n"
						+ "]\r\n"
						+ "", path.toString());

		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(7)
	void test_7() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Equipment_1"), null);
			
			ResourceResults results = $this.getFacts(":connectedTo{1,3}");
			ArrayList<String> resultsArrayList = new ArrayList<String>();
			for (Resource result : results) {
				resultsArrayList.add(result.stringValue());
			}
			assertEquals("[http://inova8.com/calc2graph/id/Equipment_2, http://inova8.com/calc2graph/id/Equipment_2_1, http://inova8.com/calc2graph/id/Equipment_3, http://inova8.com/calc2graph/id/Equipment_2_2, http://inova8.com/calc2graph/id/Equipment_4, http://inova8.com/calc2graph/id/Equipment_2_3]", resultsArrayList.toString());
			PathResults paths =  $this.getPaths(":connectedTo{1,3}");
			ArrayList<String> pathArrayList = new ArrayList<String>();
			for (Path path : paths) {
				pathArrayList.add(path.toString());
			}
			assertEquals("[Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,INVERSE]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,INVERSE]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,INVERSE]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,INVERSE]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_3,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_3,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_4,INVERSE]\r\n"
					+ "]\r\n"
					+ ", Path=[[http://inova8.com/calc2graph/id/Equipment_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_1,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_1,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_2,INVERSE]\r\n"
					+ "[http://inova8.com/calc2graph/id/Equipment_2_2,http://inova8.com/calc2graph/def/connectedTo,http://inova8.com/calc2graph/id/Equipment_2_3,INVERSE]\r\n"
					+ "]\r\n"
					+ "]", pathArrayList.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}
}
