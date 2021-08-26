/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.pathQLResults.PathResults;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;

import java.time.LocalDate;
import static org.eclipse.rdf4j.model.util.Values.literal;

import utilities.Query;

/**
 * The Class RemoteThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_PathTutorialTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}

	@Test
	@Order(1)
	void test_1() {
		try {
			Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_PathTutorialTests1/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/path1/");
			conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);

			source.removeGraph("<http://inova8.com/intelligentgraph/path1>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/path1>");
			Thing aPerson = graph.getThing(":aPerson");
			Thing Another1 = graph.getThing(":Another1");
			Thing Another2 = graph.getThing(":Another2");
			Thing Another3 = graph.getThing(":Another3");
			Thing Another4 = graph.getThing(":Another4");
			Thing Another5 = graph.getThing(":Another5");
			Thing Another6 = graph.getThing(":Another6");
			Thing Another7 = graph.getThing(":Another7");
			Thing Another8 = graph.getThing(":Another8");
			Thing Another9 = graph.getThing(":Another9");
			Thing Another10 = graph.getThing(":Another10");
			Thing Another11 = graph.getThing(":Another11");
			Thing Another12 = graph.getThing(":Another12");
			
			Thing Location = graph.getThing(":Location");
			Thing Maidstone = graph.getThing(":Maidstone").addFact(RDF.TYPE, Location);
			Thing Tideswell = graph.getThing(":Tideswell").addFact(RDF.TYPE, Location);
			
			Thing Gender = graph.getThing(":Gender");
			Thing Male = graph.getThing(":Male").addFact(RDF.TYPE, Gender);
			Thing Female = graph.getThing(":Female").addFact(RDF.TYPE, Gender);
						
			Thing Person = graph.getThing(":Person");
			aPerson.addFact(RDF.TYPE, Person).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male).addFact(":hasParent", Another1).addFact(":hasParent", Another3);
			Another1.addFact(RDF.TYPE, Person).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male).addFact(":hasParent", Another2);
			Another2.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			Another3.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female).addFact(":hasParent", Another4);
			Another4.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male).addFact(":hasParent", Another5).addFact(":hasParent", Another6);
			Another5.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female);
			Another6.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			Another7.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female).addFact(":hasParent", Another5).addFact(":hasParent", Another8).addFact(":hasParent", Another9);
			Another8.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Male);
			Another9.addFact(RDF.TYPE, Person).addFact(":hasLocation", Maidstone).addFact(":hasGender", Female).addFact(":hasParent", Another10).addFact(":hasParent", Another11).addFact(":hasParent", Another12);
			Another10.addFact(RDF.TYPE, Person).addFact(":hasLocation", Tideswell).addFact(":hasGender", Female);
			Another11.addFact(RDF.TYPE, Person).addFact(":hasLocation", Tideswell).addFact(":hasGender", Male);
			Another12.addFact(RDF.TYPE, Person).addFact(":hasLocation", Tideswell).addFact(":hasGender", Female);	
					
//			assertEquals("http://inova8.com/intelligentgraph/path1/aPerson", aPerson.stringValue());
//			assertEquals("http://inova8.com/intelligentgraph/path1/Another3", aPerson.getFact(":hasParent[:hasGender :Female]").stringValue());
//			assertEquals("http://inova8.com/intelligentgraph/path1/Another3", aPerson.getFact(":hasParent[:hasLocation :Maidstone]").stringValue());
			assertEquals("http://inova8.com/intelligentgraph/path1/Another3", aPerson.getFact(":hasParent{0,4}/:hasParent[:hasGender :Female]").stringValue());
			assertEquals("http://inova8.com/intelligentgraph/path1/Another3http://inova8.com/intelligentgraph/path1/Another5", aPerson.getFacts(":hasParent{0,4}/:hasParent[:hasGender :Female]").toString());
//			PathElement element = PathParser.parsePathPattern(aPerson, ":hasParent[:hasGender :Female]{1,4}");
//			assertEquals ("<http://inova8.com/intelligentgraph/path1/hasParent>[<http://inova8.com/intelligentgraph/path1/hasGender> <http://inova8.com/intelligentgraph/path1/Female> ]{1,4}" , element.toString());
//			assertEquals ("{0=1, 1=2, 2=3, 3=4}",element.getIterations().toString());
//			PathResults paths =  aPerson.getPaths(":hasParent[:hasGender :Female]{1,4}");
//			String pathString ="";
//			for (Path path : paths) {
//				pathString +=path.toString();
//			}
//			assertEquals("Path=[[http://inova8.com/intelligentgraph/path1/aPerson,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another3,INVERSE]\r\n"
//					+ "]\r\n"
//					+ "", pathString);
//			assertEquals("Path=[[http://inova8.com/intelligentgraph/path1/aPerson,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another3,INVERSE]\r\n"
//					+ "]\r\n"
//					+ "", paths.toString());
//			 paths =  aPerson.getPaths(":hasParent{1,4}/:hasParent[:hasGender :Female]");
//				assertEquals("Path=[[http://inova8.com/intelligentgraph/path1/aPerson,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another3,INVERSE]\r\n"
//						+ "[http://inova8.com/intelligentgraph/path1/Another3,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another4,INVERSE]\r\n"
//						+ "[http://inova8.com/intelligentgraph/path1/Another4,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another5,INVERSE]\r\n"
//						+ "]\r\n"
//						+ "", paths.toString());	
			PathResults	 paths =  aPerson.getPaths(":hasParent{0,4}/:hasParent[:hasGender :Female]");
					assertEquals("Path=[[http://inova8.com/intelligentgraph/path1/aPerson,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another3,INVERSE]\r\n"
							+ "]\r\n"
							+ "Path=[[http://inova8.com/intelligentgraph/path1/aPerson,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another3,INVERSE]\r\n"
							+ "[http://inova8.com/intelligentgraph/path1/Another3,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another4,INVERSE]\r\n"
							+ "[http://inova8.com/intelligentgraph/path1/Another4,http://inova8.com/intelligentgraph/path1/hasParent,http://inova8.com/intelligentgraph/path1/Another5,INVERSE]\r\n"
							+ "]\r\n"
							+ "", paths.toString());	
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			fail();
		}
	}

}
