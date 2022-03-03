/*
 * inova8 2020
 */
package tutorialExample;


import static org.junit.jupiter.api.Assertions.*;

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
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;

/**
 * The Class Example7_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example7_Tests {

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

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example7_Tests/");
		Query.addFile(workingRep, "src/test/resources/example7.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example7/");
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
	 * Example 7 1.
	 */
	@Test
	@Order(1)
	void example7_1() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			Resource femaleParent = aPerson.getFact(":hasParent{0,4}/:hasParent[:hasGender :Female]");
			assertEquals("http://inova8.com/intelligentgraph/example7/Another3", femaleParent.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	
	/**
	 * Example 7 2.
	 */
	@Test
	@Order(2)
	void example7_2() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			Resource maidstoneParent = aPerson.getFact(":hasParent[:hasLocation :Maidstone]");
			assertEquals("http://inova8.com/intelligentgraph/example7/Another3", maidstoneParent.stringValue());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	
	/**
	 * Example 7 3.
	 */
	@Test
	@Order(3)
	void example7_3() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example7/aPerson, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another3}; {s=http://inova8.com/intelligentgraph/example7/Another4, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another5};]",  aPerson.getFacts(":hasParent{0,4}/:hasParent[:hasGender :Female]").toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	
	/**
	 * Example 7 4.
	 */
	@Test
	@Order(4)
	void example7_4() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			assertEquals("[ {s=http://inova8.com/intelligentgraph/example7/aPerson, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another3}; {s=http://inova8.com/intelligentgraph/example7/Another1, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another2}; {s= {s=http://inova8.com/intelligentgraph/example7/aPerson, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another3}, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another4}; {s= {s= {s=http://inova8.com/intelligentgraph/example7/aPerson, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another3}, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another4}, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another5}; {s= {s= {s=http://inova8.com/intelligentgraph/example7/aPerson, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another3}, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another4}, p=http://inova8.com/intelligentgraph/example7/hasParent, o=http://inova8.com/intelligentgraph/example7/Another6};]",  aPerson.getFacts(":hasParent{0,4}/:hasParent[:hasLocation :Maidstone]").toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
	
	/**
	 * Example 7 5.
	 */
	@Test
	@Order(5)
	void example7_5() {
		try {
			Thing aPerson = source.getThing(":aPerson");
			assertEquals("Path=[[http://inova8.com/intelligentgraph/example7/aPerson,http://inova8.com/intelligentgraph/example7/hasParent,http://inova8.com/intelligentgraph/example7/Another3,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/intelligentgraph/example7/aPerson,http://inova8.com/intelligentgraph/example7/hasParent,http://inova8.com/intelligentgraph/example7/Another3,DIRECT]\r\n"
					+ "[http://inova8.com/intelligentgraph/example7/Another3,http://inova8.com/intelligentgraph/example7/hasParent,http://inova8.com/intelligentgraph/example7/Another4,DIRECT]\r\n"
					+ "[http://inova8.com/intelligentgraph/example7/Another4,http://inova8.com/intelligentgraph/example7/hasParent,http://inova8.com/intelligentgraph/example7/Another5,DIRECT]\r\n"
					+ "]\r\n"
					+ "",  aPerson.getPaths(":hasParent{0,4}/:hasParent[:hasGender :Female]").toString());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}

	/**
	 * Example 7 6.
	 */
	@Test
	@Order(6)
	void example7_6() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			Resource femaleParent = aPerson.getFact(":hasFamilialRelativeBMI");
			assertEquals(1.006889937409004, femaleParent.doubleValue());
		} catch (Exception e) {
			assertEquals("", e.getCause().getMessage());
		}
	}
}
