/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;


import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.Thing;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import java.time.LocalDate;
import static org.eclipse.rdf4j.model.util.Values.literal;

import utilities.Query;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_TutorialTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	@Order(1)
	void test_1() {
		try {
			Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests1/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example1/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example1>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example1>");
			Thing aPerson = graph.getThing(":aPerson");
			Thing Person = graph.getThing(":Person");
			aPerson.addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "62",
					XSD.DOUBLE);
			assertEquals("http://inova8.com/intelligentgraph/example1/aPerson", aPerson.stringValue());
			aPerson.addFact(":hasBMI",
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);

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
			Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests2/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example2/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");

			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example2>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example2>");
			Thing Person = graph.getThing(":Person");


			graph.getThing(":aPerson").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "62", XSD.DOUBLE);
			graph.getThing(":Another1").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "72", XSD.DOUBLE);
			graph.getThing(":Another2").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "65", XSD.DOUBLE);
			graph.getThing(":Another3").addFact(RDF.TYPE, Person).addFact(":hasHeight", "2", XSD.DOUBLE)
					.addFact(":hasWeight", "59", XSD.DOUBLE);
			graph.getThing(":Another4").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "47", XSD.DOUBLE);
			graph.getThing(":Another5").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "70", XSD.DOUBLE);
			graph.getThing(":Another6").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "56", XSD.DOUBLE);
			graph.getThing(":Another7").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "63", XSD.DOUBLE);
			graph.getThing(":Another8").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.6", XSD.DOUBLE)
					.addFact(":hasWeight", "66", XSD.DOUBLE);
			graph.getThing(":Another9").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "46", XSD.DOUBLE);
			graph.getThing(":Another10").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE);
			graph.getThing(":Another11").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE);
			graph.getThing(":Another12").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "75", XSD.DOUBLE);

			Thing BMI = graph.getThing(":BMI").addFact(SCRIPT.SCRIPTCODE,
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);
			Thing relativeBMI = graph.getThing(":RelativeBMI").addFact(SCRIPT.SCRIPTCODE,
					"double averageBMI = _this.getFact('rdf:type/:averageBMI').doubleValue(); _this.getFact(':hasBMI').doubleValue()/averageBMI;",
					SCRIPT.GROOVY);
			
			for (Resource person : graph.getThing(":Person").getFacts("^rdf:type")) {
				((Thing) person).addFact(":hasBMI", "<:BMI>", SCRIPT.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>", SCRIPT.GROOVY);
			}
			
			Person.addFact(":averageBMI", "_this.getFacts('^rdf:type/:hasBMI').average()", SCRIPT.GROOVY);
							
			assertEquals("21.523052847377123", Person.getFact(":averageBMI").stringValue());
			assertEquals("0.9967585615925397", graph.getThing(":aPerson").getFact(":hasRelativeBMI").stringValue());

			conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
			Person.addFact(":average1.7-1.8BMI",
					"_this.getFacts(\"^rdf:type[:hasHeight [ ge '1.7'^^xsd:double  ; le '1.8'^^xsd:double  ]]/:hasBMI\").average()",
					SCRIPT.GROOVY);
			assertEquals("19.885870106938924", Person.getFact(":average1.7-1.8BMI").stringValue());
			
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
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example3>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example3>");
			Thing Person = graph.getThing(":Person");

			Thing BMI = graph.getThing(":BMI").addFact(SCRIPT.SCRIPTCODE,
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);
			Thing relativeBMI = graph.getThing(":RelativeBMI").addFact("<http://inova8.com/script/scriptCode>",
					"double averageBMI = _this.getFact('rdf:type/:averageBMI').doubleValue(); _this.getFact(':hasBMI').doubleValue()/averageBMI;",
					SCRIPT.GROOVY);
			Thing locationAverageBMI = graph.getThing(":LocationAverageBMI").addFact(SCRIPT.SCRIPTCODE,
					"_this.getFacts('^:hasLocation/:hasBMI')average()", SCRIPT.GROOVY);

			Thing Maidstone = graph.getThing(":Maidstone").addFact(":averageBMI", "<:LocationAverageBMI>",
					SCRIPT.GROOVY);
			Thing Tideswell = graph.getThing(":Tideswell").addFact(":averageBMI", "<:LocationAverageBMI>",
					SCRIPT.GROOVY);

			graph.getThing(":aPerson").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "62", XSD.DOUBLE).addFact(":hasLocation", Tideswell);

			graph.getThing(":Another1").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "72", XSD.DOUBLE).addFact(":hasLocation", Tideswell);
			graph.getThing(":Another2").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "65", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another3").addFact(RDF.TYPE, Person).addFact(":hasHeight", "2", XSD.DOUBLE)
					.addFact(":hasWeight", "59", XSD.DOUBLE).addFact(":hasLocation", Tideswell);
			graph.getThing(":Another4").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "47", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another5").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "70", XSD.DOUBLE).addFact(":hasLocation", Tideswell);
			graph.getThing(":Another6").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "56", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another7").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "63", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another8").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.6", XSD.DOUBLE)
					.addFact(":hasWeight", "66", XSD.DOUBLE).addFact(":hasLocation", Tideswell);
			graph.getThing(":Another9").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "46", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another10").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasLocation", Tideswell);
			graph.getThing(":Another11").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasLocation", Maidstone);
			graph.getThing(":Another12").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "75", XSD.DOUBLE).addFact(":hasLocation", Tideswell);

			Person.addFact(":averageBMI", "_this.getFacts('^rdf:type/:hasBMI').average()", SCRIPT.GROOVY);

			for (Resource person : graph.getThing(":Person").getFacts("^rdf:type")) {
				((Thing) person).addFact(":hasBMI", "<:BMI>", SCRIPT.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>", SCRIPT.GROOVY);
			}
			
			assertEquals("21.86941453137843", Tideswell.getFact(":averageBMI").stringValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	@Order(4)
	void test_4() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests4/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example4/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example4>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example4>");
			Thing Person = graph.getThing(":Person");

			Thing locationAverageBMI = graph.getThing(":LocationAverageBMI").addFact(SCRIPT.SCRIPTCODE,
					"_this.getFacts('^:hasLocation/:hasBMI')average()", SCRIPT.GROOVY);
			Thing genderLocationAverageBMI = graph.getThing(":Gender_LocationAverageBMI").addFact(SCRIPT.SCRIPTCODE,
					"gender=_this.getFact(':hasGender');\n" + "location=_this.getFact(':hasLocation');\n"
							+ "location.getFacts('^:hasLocation[:hasGender %1]/:hasBMI',gender).average()",
					SCRIPT.GROOVY);
			
			Thing Location = graph.getThing(":Location");
			Thing Maidstone = graph.getThing(":Maidstone").addFact(RDF.TYPE, Location).addFact(":averageBMI", "<:LocationAverageBMI>",
					SCRIPT.GROOVY);
			Thing Tideswell = graph.getThing(":Tideswell").addFact(RDF.TYPE, Location).addFact(":averageBMI", "<:LocationAverageBMI>",
					SCRIPT.GROOVY);

			Thing Gender = graph.getThing(":Gender");
			Thing Male = graph.getThing(":Male").addFact(RDF.TYPE, Gender);
			Thing Female = graph.getThing(":Female").addFact(RDF.TYPE, Gender);
			
			graph.getThing(":aPerson").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "62", XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male);
			graph.getThing(":Another1").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "72", XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male);
			graph.getThing(":Another2").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "65", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			graph.getThing(":Another3").addFact(RDF.TYPE, Person).addFact(":hasHeight", "2", XSD.DOUBLE)
					.addFact(":hasWeight", "59", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female);
			graph.getThing(":Another4").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "47", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			graph.getThing(":Another5").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "70", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female);
			graph.getThing(":Another6").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "56", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			graph.getThing(":Another7").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "63", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female);
			graph.getThing(":Another8").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.6", XSD.DOUBLE)
					.addFact(":hasWeight", "66", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			graph.getThing(":Another9").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.7", XSD.DOUBLE)
					.addFact(":hasWeight", "46", XSD.DOUBLE).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female);
			graph.getThing(":Another10").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.9", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasGender", Female);
			graph.getThing(":Another11").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.5", XSD.DOUBLE)
					.addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male);
			graph.getThing(":Another12").addFact(RDF.TYPE, Person).addFact(":hasHeight", "1.8", XSD.DOUBLE)
					.addFact(":hasWeight", "75", XSD.DOUBLE).addFact(":hasLocation", Tideswell).addFact(":hasGender", Female);	

			Thing BMI = graph.getThing(":BMI").addFact(SCRIPT.SCRIPTCODE,
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);
			Thing relativeBMI = graph.getThing(":RelativeBMI").addFact("<http://inova8.com/script/scriptCode>",
					"double averageBMI = _this.getFact('rdf:type/:averageBMI').doubleValue(); _this.getFact(':hasBMI').doubleValue()/averageBMI;",
					SCRIPT.GROOVY);
			
			for (Resource person : graph.getThing(":Person").getFacts("^rdf:type")) {
				((Thing) person).addFact(":hasBMI", "<:BMI>", SCRIPT.GROOVY).addFact(":hasRelativeBMI", "<:RelativeBMI>", SCRIPT.GROOVY);
			}
			
			Person.addFact(":averageBMI", "_this.getFacts('^rdf:type/:hasBMI').average()", SCRIPT.GROOVY);
			
			Thing Gender_Location = graph.getThing(":Gender_Location");
			graph.getThing(":Male_Tideswell").addFact(RDF.TYPE, Gender_Location).addFact(":hasGender", Male)
					.addFact(":hasLocation", Tideswell);
			graph.getThing(":Female_Tideswell").addFact(RDF.TYPE, Gender_Location).addFact(":hasGender", Female)
					.addFact(":hasLocation", Tideswell);
			graph.getThing(":Male_Maidstone").addFact(RDF.TYPE, Gender_Location).addFact(":hasGender", Male)
					.addFact(":hasLocation", Maidstone);
			graph.getThing(":Female_Maidstone").addFact(RDF.TYPE, Gender_Location).addFact(":hasGender", Female)
					.addFact(":hasLocation", Maidstone);

			for (Resource person : graph.getThing(":Gender_Location").getFacts("^rdf:type")) {
				((Thing) person).addFact(":averageBMI", "<:Gender_LocationAverageBMI>",
						SCRIPT.GROOVY);
			}

			assertEquals("22.836332215431028", graph.getThing(":Male_Tideswell").getFact(":averageBMI").stringValue());

			Thing genderLocationRelativeBMI = graph.getThing(":GenderLocationRelativeBMI").addFact(SCRIPT.SCRIPTCODE,
					"BMI=_this.getFact(':hasBMI').doubleValue();\n" + "gender=_this.getFact(':hasGender');\n"
							+ "location=_this.getFact(':hasLocation');\n"
							+ "genderLocationAverageBMI = _this.getThing(':Gender_Location').getFact('^rdf:type[:hasGender %1; :hasLocation %2]/:averageBMI',gender,location).doubleValue();\n"
							+ "BMI/genderLocationAverageBMI;",
					SCRIPT.GROOVY);

			for (Resource person : graph.getThing(":Person").getFacts("^rdf:type")) {
				((Thing) person).addFact(":hasGenderLocationRelativeBMI", "<:GenderLocationRelativeBMI>",
						SCRIPT.GROOVY);
			}
			assertEquals("0.9394366395990406",
					graph.getThing(":aPerson").getFact(":hasGenderLocationRelativeBMI").stringValue());

		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	@Test
	@Order(5)
	void test_5() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests5/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example5/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example5>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example5>");
			Thing Person = graph.getThing(":Person");
			Thing aPerson = graph.getThing(":aPerson");
			Thing Measurement = graph.getThing(":Measurement");
			
			graph.getThing(":aPerson_Measurement_1").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "62", XSD.DOUBLE).addFact(":hasDate", "2021-08-01", XSD.DATE).addFact(":measurementOf", aPerson);
			graph.getThing(":aPerson_Measurement_2").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.65", XSD.DOUBLE).addFact(":hasWeight", "60", XSD.DOUBLE).addFact(":hasDate", "2021-08-02", XSD.DATE).addFact(":measurementOf", aPerson);
			graph.getThing(":aPerson_Measurement_3").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "65", XSD.DOUBLE).addFact(":hasDate", "2021-08-03", XSD.DATE).addFact(":measurementOf", aPerson);
			graph.getThing(":aPerson_Measurement_4").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "57", XSD.DOUBLE).addFact(":hasDate", "2021-08-04", XSD.DATE).addFact(":measurementOf", aPerson);
			graph.getThing(":aPerson_Measurement_5").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.75", XSD.DOUBLE).addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasDate", "2021-08-05", XSD.DATE).addFact(":measurementOf", aPerson);
			graph.getThing(":aPerson_Measurement_6").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.75", XSD.DOUBLE).addFact(":hasWeight", "63", XSD.DOUBLE).addFact(":hasDate", "2021-08-06", XSD.DATE).addFact(":measurementOf", aPerson);


			Thing BMI = graph.getThing(":BMI").addFact(SCRIPT.SCRIPTCODE,
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);
			
			for (Resource measurement : graph.getThing(":Measurement").getFacts("^rdf:type")) {
				((Thing) measurement).addFact(":hasBMI", "<:BMI>", SCRIPT.GROOVY);
			}
			

			assertEquals("\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.49134948096886\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.723183391003463\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.918367346938776\"^^<http://www.w3.org/2001/XMLSchema#double>\"20.571428571428573\"^^<http://www.w3.org/2001/XMLSchema#double>",
					graph.getThing(":aPerson").getFacts("^:measurementOf/:hasBMI").toString());
			assertEquals("22.03856749311295",
					graph.getThing(":aPerson").getFact("^:measurementOf[:hasDate %1]/:hasBMI",
							literal(LocalDate.parse("2021-08-02"))).stringValue());

			assertEquals("21.453287197231838",
					graph.getThing(":aPerson").getFact("^:measurementOf[:hasDate [lt %1]]/:hasBMI",
							literal(LocalDate.parse("2021-08-03"))).stringValue());

		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	@Test
	@Order(6)
	void test_6() {
		try {
			org.eclipse.rdf4j.repository.Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_TutorialTests6/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example6/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/example6>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/example5>");
			Thing Person = graph.getThing(":Person");
			Thing aPerson = graph.getThing(":aPerson");


			Thing Measurement = graph.getThing(":Measurement");
			
			Thing Observation = graph.getThing(":Observation").addFact("rdfs:subClassof", RDF.STATEMENT);
			Thing ObservationType = graph.getThing(":ObservationType");
			Thing observationOf = graph.getThing(":observationOf").addFact("rdfs:subPropertyOf", RDF.SUBJECT).addFact("rdfs:domain", Observation).addFact("rdfs:range", Person);
			Thing observationType = graph.getThing(":observationType").addFact("rdfs:subPropertyOf", RDF.PREDICATE).addFact("rdfs:domain", Observation).addFact("rdfs:range", ObservationType);
			Thing observationMeasurement = graph.getThing(":observationMeasurement").addFact("rdfs:subPropertyOf", RDF.OBJECT).addFact("rdfs:domain", Observation).addFact("rdfs:range", Measurement);

			
			

			
			Thing Measurement_1=graph.getThing(":aPerson_Observation_1").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "62", XSD.DOUBLE).addFact(":hasDate", "2021-08-01", XSD.DATE);
			Thing Measurement_2=graph.getThing(":aPerson_Observation_2").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.65", XSD.DOUBLE).addFact(":hasWeight", "60", XSD.DOUBLE).addFact(":hasDate", "2021-08-02", XSD.DATE);
			Thing Measurement_3=graph.getThing(":aPerson_Observation_3").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "65", XSD.DOUBLE).addFact(":hasDate", "2021-08-03", XSD.DATE);
			Thing Measurement_4=graph.getThing(":aPerson_Observation_4").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.7", XSD.DOUBLE).addFact(":hasWeight", "57", XSD.DOUBLE).addFact(":hasDate", "2021-08-04", XSD.DATE);
			Thing Measurement_5=graph.getThing(":aPerson_Observation_5").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.75", XSD.DOUBLE).addFact(":hasWeight", "61", XSD.DOUBLE).addFact(":hasDate", "2021-08-05", XSD.DATE);
			Thing Measurement_6=graph.getThing(":aPerson_Observation_6").addFact(RDF.TYPE, Measurement).addFact(":hasHeight", "1.75", XSD.DOUBLE).addFact(":hasWeight", "63", XSD.DOUBLE).addFact(":hasDate", "2021-08-06", XSD.DATE);

			Thing BMI = graph.getThing(":BMI").addFact(SCRIPT.SCRIPTCODE,
					"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)",
					SCRIPT.GROOVY);
			
			for (Resource measurement : graph.getThing(":Measurement").getFacts("^rdf:type")) {
				((Thing) measurement).addFact(":hasBMI", "<:BMI>", SCRIPT.GROOVY);
			}
			Thing BMIObservation = graph.getThing(":BMIObservation");
			
			graph.getThing(":aPerson_Observation_1").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_1).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);
			graph.getThing(":aPerson_Observation_2").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_2).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);
			graph.getThing(":aPerson_Observation_3").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_3).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);
			graph.getThing(":aPerson_Observation_4").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_4).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);
			graph.getThing(":aPerson_Observation_5").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_5).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);
			graph.getThing(":aPerson_Observation_6").addFact(RDF.TYPE, Observation).addFact(":observationMeasurement", Measurement_6).addFact(":observationType", BMIObservation).addFact(":observationOf", aPerson);

			assertEquals("\"21.453287197231838\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.03856749311295\"^^<http://www.w3.org/2001/XMLSchema#double>\"22.49134948096886\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.723183391003463\"^^<http://www.w3.org/2001/XMLSchema#double>\"19.918367346938776\"^^<http://www.w3.org/2001/XMLSchema#double>\"20.571428571428573\"^^<http://www.w3.org/2001/XMLSchema#double>",
					graph.getThing(":aPerson").getFacts(":Observation@:BMIObservation/:hasBMI").toString());
			
			assertEquals("22.03856749311295",
					graph.getThing(":aPerson").getFact(":Observation@:BMIObservation[:hasDate %1]/:hasBMI",
							literal(LocalDate.parse("2021-08-02"))).stringValue());

			assertEquals("21.453287197231838",
					graph.getThing(":aPerson").getFact(":Observation@:BMIObservation[:hasDate [lt %1]]/:hasBMI",
							literal(LocalDate.parse("2021-08-03"))).stringValue());

		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
}
