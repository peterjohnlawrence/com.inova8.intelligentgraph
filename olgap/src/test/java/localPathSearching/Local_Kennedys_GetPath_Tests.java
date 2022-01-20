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
class Local_Kennedys_GetPath_Tests {
	

	
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
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/kennedys/");
		Query.addFile(workingRep, "src/test/resources/kennedys/kennedys.ttl");
		Query.addFile(workingRep, "src/test/resources/kennedys/inverses.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("kennedys", "http://topbraid.org/examples/kennedys#");
		conn.setNamespace("", "http://topbraid.org/examples/kennedys#");
		
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
			Thing _this =source.getThing(":ArnoldSchwarzenegger");
			PathResults paths =  _this.getPaths(":spouse/:parent/:parent/:almaMater");
			assertEquals("Path=[[http://topbraid.org/examples/kennedys#ArnoldSchwarzenegger,http://topbraid.org/examples/kennedys#spouse,http://topbraid.org/examples/kennedys#MariaShriver,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#MariaShriver,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#EuniceKennedy,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#EuniceKennedy,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#JosephKennedy,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#JosephKennedy,http://topbraid.org/examples/kennedys#almaMater,http://topbraid.org/examples/kennedys#Harvard,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://topbraid.org/examples/kennedys#ArnoldSchwarzenegger,http://topbraid.org/examples/kennedys#spouse,http://topbraid.org/examples/kennedys#MariaShriver,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#MariaShriver,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#EuniceKennedy,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#EuniceKennedy,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#RoseFitzgerald,DIRECT]\r\n"
					+ "[http://topbraid.org/examples/kennedys#RoseFitzgerald,http://topbraid.org/examples/kennedys#almaMater,http://topbraid.org/examples/kennedys#SacredHeartConvent,DIRECT]\r\n"
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
			Thing _this =source.getThing(":ArnoldSchwarzenegger");
			PathResults paths =  _this.getPaths("(:spouse|:parent|:child){0,5}/:almaMater[eq :Harvard]");
			for(Path path:paths) {
				assertEquals("Path=[[http://topbraid.org/examples/kennedys#ArnoldSchwarzenegger,http://topbraid.org/examples/kennedys#spouse,http://topbraid.org/examples/kennedys#MariaShriver,DIRECT]\r\n"
						+ "[http://topbraid.org/examples/kennedys#MariaShriver,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#EuniceKennedy,DIRECT]\r\n"
						+ "[http://topbraid.org/examples/kennedys#EuniceKennedy,http://topbraid.org/examples/kennedys#parent,http://topbraid.org/examples/kennedys#JosephKennedy,DIRECT]\r\n"
						+ "[http://topbraid.org/examples/kennedys#JosephKennedy,http://topbraid.org/examples/kennedys#almaMater,http://topbraid.org/examples/kennedys#Harvard,DIRECT]\r\n"
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
