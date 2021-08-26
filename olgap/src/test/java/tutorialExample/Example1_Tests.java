/*
 * inova8 2020
 */
package tutorialExample;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.Trace;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;

import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Example1_Tests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Example1_Tests/");
		Query.addFile(workingRep, "src/test/resources/example1.ttl");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/example1/");
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
	void example1_1() {

		try {
			Thing aPerson = source.getThing(":aPerson");

			Trace trace = aPerson.traceFact(":hasBMI");
			assertEquals("   1. Getting facts ':hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "   2. ...within contexts: [http://inova8.com/intelligentgraph/example1, file://src/test/resources/example1.ttl]\n"
					+ "         1. Evaluating predicate hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> , by invoking groovy script\n"
					+ "         2. double height=_this.getFact(':hasHeight').doubleValue();  _this.getFact(':hasWeight').doubleValue()/(height*height)\n"
					+ "\n"
					+ "               1. Getting facts ':hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "               2. Next fact 'http://inova8.com/intelligentgraph/example1/hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 1.7\n"
					+ "               3. Returned fact ':hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 1.7^^decimal <http://www.w3.org/2001/XMLSchema#decimal>\n"
					+ "\n"
					+ "               4. Getting facts ':hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "               5. Next fact 'http://inova8.com/intelligentgraph/example1/hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 62\n"
					+ "               6. Returned fact ':hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 62^^decimal <http://www.w3.org/2001/XMLSchema#decimal>\n"
					+ "\n"
					+ "         3. Evaluated hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   3. Calculated hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   4. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   5. Returned fact 'http://inova8.com/intelligentgraph/example1/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>", trace.asText());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void example1_2() {

		try {
			Thing aPerson = source.getThing(":aPerson");
			Resource bmi = aPerson.getFact(":hasBMI");
			assertEquals("21.453287197231838", bmi.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

}
