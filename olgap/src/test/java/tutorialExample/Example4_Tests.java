/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.pathQLResults.ResourceResults;

import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example4_Tests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example4_Tests/");
		Query.addFile(workingRep, "src/test/resources/example4.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example4/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	@Test
	@Order(1)
	void example4_1() {

		try {
			Thing person = source.getThing(":Person"); 
			ResourceResults persons = person.getFacts("^rdf:type[:hasLocation :Tideswell  ; :hasGender :Male ]");
			//ResourceResults persons = person.getFacts("^rdf:type[:hasLocation :Tideswell ]");
			ArrayList<String> personValues = new ArrayList<String>();
			for(Resource person1:persons) {
				personValues.add(person1.getValue().stringValue());
			}
			assertEquals("[http://inova8.com/intelligentgraph/example4/Another1, http://inova8.com/intelligentgraph/example4/Another11, http://inova8.com/intelligentgraph/example4/aPerson]", personValues.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void example4_2() {

		try {
			Double bmi = source.getThing(":Person").getFacts("^rdf:type[:hasLocation :Tideswell  ; :hasGender :Male ]/:hasBMI").average();
			assertEquals("22.83633221543103", bmi.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void example4_3() {

		try {
			Thing person = source.getThing(":Person"); 
			Thing male_Tideswell = source.getThing(":Male_Tideswell");
			Value gender = male_Tideswell.getFact(":hasGender");
			Value location = male_Tideswell.getFact(":hasLocation");
			Double bmi = person.getFacts("^rdf:type[:hasLocation %2 ; :hasGender %1 ]/:hasBMI",gender,location).average();
			assertEquals("22.83633221543103", bmi.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void example4_4() {

		try {

			Thing male_Tideswell = source.getThing(":Male_Tideswell");

			Double bmi = male_Tideswell.getFact(":averageBMI").doubleValue();
			assertEquals("22.83633221543103", bmi.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void example4_5() {

		try {

			Thing aPerson = source.getThing(":aPerson");

			Double genderLocationRelativeBMI = aPerson.getFact(":hasGenderLocationRelativeBMI").doubleValue();
			assertEquals("0.9394366395990403", genderLocationRelativeBMI.toString());
		} catch (Exception e) {
			assertEquals("",e.getMessage());
		}
	}
}
