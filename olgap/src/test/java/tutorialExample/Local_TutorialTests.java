/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQLRepository.PathQLRepository;
import utilities.Query;
import pathQLRepository.Graph;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_TutorialTests {

	/** The repository triple source. */
	//	static RepositoryTripleSource repositoryTripleSource;

	/** The source. */
	private static PathQLRepository source;

	/**
	 * The evaluator.
	 *
	 * @throws Exception
	 *             the exception
	 */
	//	private static Evaluator evaluator;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	@Order(1)
	void test_1() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests1/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example1/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			PathQLRepository source = PathQLRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example1>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example1>");
			Thing aPerson = graph.getThing(":aPerson");
			Thing Person = graph.getThing(":Person");
			aPerson.addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "62",
					XSD.DOUBLE);
			assertEquals("http://inova8.com/intelligentgraph/example1/aPerson", aPerson.stringValue());
			aPerson.addFact(":hasBMI",
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					Evaluator.GROOVY);

			assertEquals(
					"   1. Getting facts ':hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
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
							+ "   5. Returned fact 'http://inova8.com/intelligentgraph/example1/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>",
					aPerson.traceFact(":hasBMI").asText());
			assertEquals("21.453287197231838", aPerson.getFact(":hasBMI").stringValue());
			assertEquals(
					"   1. Getting facts ':hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson>\n"
							+ "   2. ...within contexts: [http://inova8.com/intelligentgraph/example1]\n"
							+ "   3. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
							+ "   4. Retrieved cached value hasBMI <http://inova8.com/intelligentgraph/example1/hasBMI> of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>\n"
							+ "   5. Returned fact 'http://inova8.com/intelligentgraph/example1/hasBMI' of aPerson <http://inova8.com/intelligentgraph/example1/aPerson> = 21.453287197231838^^double <http://www.w3.org/2001/XMLSchema#double>",
					aPerson.traceFact(":hasBMI").asText());

		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	@Order(2)
	void test_2() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests2/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example2/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");

			PathQLRepository source = PathQLRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example2>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example2>");
			Thing person = graph.getThing(":Person");
			Thing BMI = graph.getThing(":BMI").addFact("<http://inova8.com/script/scriptCode>",
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					Evaluator.GROOVY);
			Thing relativeBMI = graph.getThing(":RelativeBMI").addFact("<http://inova8.com/script/scriptCode>",
					"double averageBMI = _this.getFact('rdf:type/:averageBMI').doubleValue(); _this.getFact(':hasBMI').doubleValue()/averageBMI;",
					Evaluator.GROOVY);

			graph.getThing(":aPerson").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "62", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another1").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "72", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another2").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "65", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another3").addFact(RDF.TYPE, person).addFact(":hasHeight", "2", XSD.DOUBLE)
					.addFact(":hasWeight", "59", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another4").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "47", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another5").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "70", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another6").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "56", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another7").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "63", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another8").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.6", XSD.DOUBLE)
					.addFact(":hasWeight", "66", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another9").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "46", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another10").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another11").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			graph.getThing(":Another12").addFact(RDF.TYPE, person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "75", XSD.DOUBLE).addFact(":hasBMI", "<:BMI>", Evaluator.GROOVY)
					.addFact(":hasRelativeBMI", "<:RelativeBMI>", Evaluator.GROOVY);
			person.addFact(":averageBMI", "_this.getFacts('^rdf:type/:hasBMI').average()", Evaluator.GROOVY);

			assertEquals("21.523052847377123", person.getFact(":averageBMI").stringValue());
			assertEquals("0.9967585615925397", graph.getThing(":aPerson").getFact(":hasRelativeBMI").stringValue());
			
			conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
			person.addFact(":average1.7-1.8BMI", "_this.getFacts(\"^rdf:type[:hasHeight [ ge '1.7'^^xsd:double  ; le '1.8'^^xsd:double  ]]/:hasBMI\").average()",
					Evaluator.GROOVY);
			assertEquals("19.885870106938924", person.getFact(":average1.7-1.8BMI").stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	@Order(3)
	void test_3() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests3/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example3/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			PathQLRepository source = PathQLRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example3>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example3>");
			Thing Person = graph.getThing(":Person");

			Thing BMI = graph.getThing(":BMI").addFact("<http://inova8.com/script/scriptCode>",
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					Evaluator.GROOVY);
			Thing relativeBMI = graph.getThing(":RelativeBMI").addFact("<http://inova8.com/script/scriptCode>",
					"double averageBMI = _this.getFact('rdf:type/:averageBMI').doubleValue(); _this.getFact(':hasBMI').doubleValue()/averageBMI;",
					Evaluator.GROOVY);
			Thing locationAverageBMI = graph.getThing(":LocationRelativeBMI").addFact(
					"<http://inova8.com/script/scriptCode>", "_this.getFacts('^:hasLocation/:hasBMI')average()",
					Evaluator.GROOVY);

			Thing Maidstone = graph.getThing(":Maidstone").addFact(":averageBMI", "<:LocationAverageBMI>",
					Evaluator.GROOVY);
			Thing Tideswell = graph.getThing(":Tideswell").addFact(":averageBMI", "<:LocationAverageBMI>",
					Evaluator.GROOVY);

			graph.getThing(":aPerson").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7",XSD.DOUBLE).addFact(":hasWeight", "62",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);

			graph.getThing(":Another1").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9",XSD.DOUBLE).addFact(":hasWeight", "72",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another2").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7",XSD.DOUBLE).addFact(":hasWeight", "65",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another3").addFact(RDF.TYPE, Person).addFact(":hasHeight", "2",XSD.DOUBLE).addFact(":hasWeight", "59",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another4").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8",XSD.DOUBLE).addFact(":hasWeight", "47",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another5").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5",XSD.DOUBLE).addFact(":hasWeight", "70",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another6").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5",XSD.DOUBLE).addFact(":hasWeight", "56",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another7").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7",XSD.DOUBLE).addFact(":hasWeight", "63",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another8").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.6",XSD.DOUBLE).addFact(":hasWeight", "66",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another9").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7",XSD.DOUBLE).addFact(":hasWeight", "46",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another10").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9",XSD.DOUBLE).addFact(":hasWeight", "61",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another11").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5",XSD.DOUBLE).addFact(":hasWeight", "61",XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);
			graph.getThing(":Another12").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8",XSD.DOUBLE).addFact(":hasWeight", "75",XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasBMI", "<:BMI>",Evaluator.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>",Evaluator.GROOVY);

			Person.addFact(":averageBMI", "_this.getFacts('^rdf:type/:hasBMI').average()", Evaluator.GROOVY);

			assertEquals("21.523052847377123", Person.getFact(":averageBMI").stringValue());
			assertEquals("0.9967585615925397", graph.getThing(":aPerson").getFact(":hasRelativeBMI").stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
}
