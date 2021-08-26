/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Remote_TutorialTests {

	

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {


	}
	@Test
	@Order(1)
	void test_1() {
		try {
			//IntelligentGraphRepository source = IntelligentGraphRepository.create("http://host.docker.internal:8080/rdf4j-server","tutorial");
			IntelligentGraphRepository source = IntelligentGraphRepository.create("http://host.docker.internal:8080/rdf4j-server","tutorial");
			source.prefix("<http://inova8.com/intelligentgraph/example1/>");
			source.prefix("rdfs","<http://www.w3.org/2000/01/rdf-schema#>");
			source.removeGraph("<http://inova8.com/intelligentgraph/example1>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example1>");
			Thing aPerson = graph.getThing(":aPerson");
			Thing Person = graph.getThing(":Person");
			aPerson.addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7",XSD.DOUBLE).addFact(":hasWeight", "62",XSD.DOUBLE);
			assertEquals("http://inova8.com/intelligentgraph/example1/aPerson", aPerson.stringValue());
			aPerson.addFact(":hasBMI", "height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",SCRIPT.GROOVY);
			assertEquals("   1. Getting facts ':hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "   2. ...within contexts: [http://inova8.com/intelligentgraph/example1]\n"
					+ "         1. Evaluating predicate hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> , by invoking groovy script\n"
					+ "         2. height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)\n"
					+ "\n"
					+ "               1. Getting facts ':hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "               2. Next fact 'http://inova8.com/intelligentgraph/example1/hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 1.7\n"
					+ "               3. Returned fact ':hasHeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 1.7^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "\n"
					+ "               4. Getting facts ':hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "               5. Next fact 'http://inova8.com/intelligentgraph/example1/hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 62\n"
					+ "               6. Returned fact ':hasWeight' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 62^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "\n"
					+ "         3. Evaluated hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   3. Calculated hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   4. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   5. Returned fact 'http://inova8.com/intelligentgraph/example1/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>"
					, aPerson.traceFact(":hasBMI").asText());

			assertEquals("21.453287197231838", aPerson.getFact(":hasBMI").stringValue());
			assertEquals("   1. Getting facts ':hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
					+ "   2. ...within contexts: [http://inova8.com/intelligentgraph/example1]\n"
					+ "   3. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   4. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
					+ "   5. Returned fact 'http://inova8.com/intelligentgraph/example1/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>", aPerson.traceFact(":hasBMI").asText());
		} catch (Exception e) {
			fail();
		}
	}


}
