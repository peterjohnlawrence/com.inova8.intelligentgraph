/*
 * inova8 2020
 */
package tutorialExample;

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
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;

/**
 * The Class Example4_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example4_Tests {

	/** The source. */
	private static IntelligentGraphRepository source;
	
	/** The working rep. */
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example4_Tests/");
		Query.addFile(workingRep, "src/test/resources/example4.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example4/");
		conn.setNamespace(XSD.PREFIX, XSD.NAMESPACE);
		conn.setNamespace( RDF.PREFIX, RDF.NAMESPACE);
		conn.setNamespace(RDFS.PREFIX , RDFS.NAMESPACE);
		conn.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
		source = IntelligentGraphRepository.create(workingRep);

	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}
	
	/**
	 * Example 4 1.
	 */
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
	
	/**
	 * Example 4 2.
	 */
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
	
	/**
	 * Example 4 3.
	 */
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
	
	/**
	 * Example 4 4.
	 */
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
	
	/**
	 * Example 4 5.
	 */
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
	@Test
	@Order(6)
	void example4_6() {

		try {
			ResourceResults classes = source.getFacts("[ a owl:Class]/rdfs:label");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Person, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Person\"}; {s=http://inova8.com/intelligentgraph/example4/Gender, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Gender\"}; {s=http://inova8.com/intelligentgraph/example4/Gender_Location, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Gender Location\"}; {s=http://inova8.com/intelligentgraph/example4/Location, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Location\"};]", classes.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(7)
	void example4_7() {

		try {
			ResourceResults classes = source.getFacts("[ a owl:Class; eq :Person]/^a[:hasLocation :Maidstone]/:hasHeight");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Another2, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.7\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another3, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"2\"^^<http://www.w3.org/2001/XMLSchema#integer>}; {s=http://inova8.com/intelligentgraph/example4/Another4, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.8\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another5, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.5\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another6, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.5\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another7, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.7\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another8, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.6\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another9, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.7\"^^<http://www.w3.org/2001/XMLSchema#decimal>};]", classes.toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(8)
	void example4_8() {

		try {
			ResourceResults facts = source.getFacts("[ a owl:Class; eq :Person]/^a[:hasLocation :Maidstone ; :hasGender :Male]/(:hasHeight | :hasWeight | :hasLocation)");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Another2, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.7\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another2, p=http://inova8.com/intelligentgraph/example4/hasWeight, o=\"65\"^^<http://www.w3.org/2001/XMLSchema#integer>}; {s=http://inova8.com/intelligentgraph/example4/Another2, p=http://inova8.com/intelligentgraph/example4/hasLocation, o=http://inova8.com/intelligentgraph/example4/Maidstone}; {s=http://inova8.com/intelligentgraph/example4/Another4, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.8\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another4, p=http://inova8.com/intelligentgraph/example4/hasWeight, o=\"47\"^^<http://www.w3.org/2001/XMLSchema#integer>}; {s=http://inova8.com/intelligentgraph/example4/Another4, p=http://inova8.com/intelligentgraph/example4/hasLocation, o=http://inova8.com/intelligentgraph/example4/Maidstone}; {s=http://inova8.com/intelligentgraph/example4/Another6, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.5\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another6, p=http://inova8.com/intelligentgraph/example4/hasWeight, o=\"56\"^^<http://www.w3.org/2001/XMLSchema#integer>}; {s=http://inova8.com/intelligentgraph/example4/Another6, p=http://inova8.com/intelligentgraph/example4/hasLocation, o=http://inova8.com/intelligentgraph/example4/Maidstone}; {s=http://inova8.com/intelligentgraph/example4/Another8, p=http://inova8.com/intelligentgraph/example4/hasHeight, o=\"1.6\"^^<http://www.w3.org/2001/XMLSchema#decimal>}; {s=http://inova8.com/intelligentgraph/example4/Another8, p=http://inova8.com/intelligentgraph/example4/hasWeight, o=\"66\"^^<http://www.w3.org/2001/XMLSchema#integer>}; {s=http://inova8.com/intelligentgraph/example4/Another8, p=http://inova8.com/intelligentgraph/example4/hasLocation, o=http://inova8.com/intelligentgraph/example4/Maidstone};]", facts.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(9)
	void example4_9() {

		try {
			ResourceResults facts = source.getFacts("[ a owl:Class; eq :Gender]/^a/*");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Male, p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, o=http://inova8.com/intelligentgraph/example4/Gender}; {s=http://inova8.com/intelligentgraph/example4/Male, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Male\"}; {s=http://inova8.com/intelligentgraph/example4/Male, p=http://inova8.com/intelligentgraph/example4/averageBMI, o=\"22.31095112223672\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example4/Male, p=http://inova8.com/intelligentgraph/example4/total, o=\"9\"^^<http://www.w3.org/2001/XMLSchema#int>}; {s=http://inova8.com/intelligentgraph/example4/Female, p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, o=http://inova8.com/intelligentgraph/example4/Gender}; {s=http://inova8.com/intelligentgraph/example4/Female, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Female\"}; {s=http://inova8.com/intelligentgraph/example4/Female, p=http://inova8.com/intelligentgraph/example4/averageBMI, o=\"20.603838193374262\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example4/Female, p=http://inova8.com/intelligentgraph/example4/total, o=\"8\"^^<http://www.w3.org/2001/XMLSchema#int>};]", facts.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(9)
	void example4_91() {

		try {
			Resource fact = source.getFact("[ a owl:Class; eq :Gender]/^a/*");
			assertEquals(" {s=http://inova8.com/intelligentgraph/example4/Male, p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, o=http://inova8.com/intelligentgraph/example4/Gender}", fact.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(10)
	void example4_10() {

		try {
			ResourceResults facts = source.getFacts("[  eq :Another1]/:hasLocation/*");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, o=http://inova8.com/intelligentgraph/example4/Location}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Tideswell\"}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://inova8.com/intelligentgraph/example4/averageBMI, o=\"21.7109303439298\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://inova8.com/intelligentgraph/example4/total, o=\"7\"^^<http://www.w3.org/2001/XMLSchema#int>};]", facts.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	@Test
	@Order(11)
	void example4_11() {

		try {
			ResourceResults facts = source.getFacts("[ a owl:Class; eq :Person]/^a[:hasLocation :Tideswell ; :hasGender :Male ;eq :Another1]/:hasLocation/*");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://www.w3.org/1999/02/22-rdf-syntax-ns#type, o=http://inova8.com/intelligentgraph/example4/Location}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://www.w3.org/2000/01/rdf-schema#label, o=\"Tideswell\"}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://inova8.com/intelligentgraph/example4/averageBMI, o=\"21.7109303439298\"^^<http://www.w3.org/2001/XMLSchema#double>}; {s=http://inova8.com/intelligentgraph/example4/Tideswell, p=http://inova8.com/intelligentgraph/example4/total, o=\"7\"^^<http://www.w3.org/2001/XMLSchema#int>};]", facts.toString());

		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
}
