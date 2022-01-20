/*
 * inova8 2020
 */
package localPathSearching;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.pathQLResults.PathResults;

import utilities.Query;

/**
 * The Class ThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_TFL_GetPath_Tests {
	

	
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
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/tfl/");
		Query.addFile(workingRep, "src/test/resources/tfl/22-rdf-syntax-ns.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/extraconnections.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/lines.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/londontubeModel.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/rdf-schema.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/stations.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/tfl.ttl");
		Query.addFile(workingRep, "src/test/resources/tfl/wgs84_pos.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		//conn.setNamespace("tfl", "http://in4mium.com/tfl#");
		conn.setNamespace("londontubeModel", "http://in4mium.com/londontube/schema/londontubeModel#");
		conn.setNamespace("londontube", "http://in4mium.com/londontube/id/");
		conn.setNamespace("", "http://in4mium.com/londontube/ref/");
		conn.setNamespace("geo", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		conn.setNamespace("owl", "http://www.w3.org/2002/07/owl#");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("dc", "http://purl.org/dc/elements/1.1/");
		source = IntelligentGraphRepository.create(workingRep);


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
			//Thing _this =source.getThing(iri("http://in4mium.com/londontube/id/","Mornington_Crescent"), null);
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":connectsTo");
			assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void test_2() {
		try {
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":connectsFrom|:connectsTo");
			assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsFrom,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsFrom,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
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
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":connectsTo{1,2}");
			assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Kings_Cross_St._Pancras,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/South_Hampstead,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Mornington_Crescent,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Wembley_Central,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Warren_Street,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Mornington_Crescent,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Chalk_Farm,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Kentish_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void test_4() {
		try {
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":connectsTo{1,2}/:connectsTo[eq londontube:Kentish_Town]");
			assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Kentish_Town,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Euston,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Euston,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Camden_Town,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Camden_Town,http://in4mium.com/londontube/ref/connectsTo,http://in4mium.com/londontube/id/Kentish_Town,DIRECT]\r\n"
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
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":online{1,3}/:hasStationOnLine[eq londontube:Oakleigh_Park]");
			assertEquals("", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(6)
	void test_6() {
		try {
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths("(:onLine|:hasStationOnLine){1,4}/:hasStationOnLine[eq londontube:Oakleigh_Park]");
			assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Northern,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Old_Street,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Old_Street,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Great_Northern,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Great_Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Oakleigh_Park,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Northern,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Moorgate,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Moorgate,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Great_Northern,DIRECT]\r\n"
					+ "[http://in4mium.com/londontube/id/Great_Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Oakleigh_Park,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(7)
	void test_7() {
		try {
			Thing _this =source.getThing("londontube:Mornington_Crescent");
			PathResults paths =  _this.getPaths(":onLine/(:hasStationOnLine/:onLine){0,4}/:hasStationOnLine[eq londontube:Oakleigh_Park]");
			for(Path path: paths) {
				assertEquals("Path=[[http://in4mium.com/londontube/id/Mornington_Crescent,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Northern,DIRECT]\r\n"
						+ "[http://in4mium.com/londontube/id/Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Old_Street,DIRECT]\r\n"
						+ "[http://in4mium.com/londontube/id/Old_Street,http://in4mium.com/londontube/ref/onLine,http://in4mium.com/londontube/id/Great_Northern,DIRECT]\r\n"
						+ "[http://in4mium.com/londontube/id/Great_Northern,http://in4mium.com/londontube/ref/hasStationOnLine,http://in4mium.com/londontube/id/Oakleigh_Park,DIRECT]\r\n"
						+ "]\r\n"
						+ "", path.toString());
				break;
			}
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
