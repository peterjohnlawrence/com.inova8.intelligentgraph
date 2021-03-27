package pathCalc;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.Evaluator;
import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathQLTests { 
	static LuceneSail lucenesail ;
	private static RepositoryConnection conn;
	static RepositoryTripleSource repositoryTripleSource;
	private static PathQLRepository source;
	private static Evaluator evaluator;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);

		Sail baseSail = new NativeStore(dataDir);
	    lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");
		lucenesail.setBaseSail(baseSail);

		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(lucenesail);
		//org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(new NativeStore(dataDir));

		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null));

		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null));
		repositoryTripleSource = new RepositoryTripleSource(conn);
		source = new PathQLRepository(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
		evaluator = new Evaluator();
	}

	@Test
	@Order(0)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_0() {
		try {
			evaluator.clearCache();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	@Order(1)
	void pathql_1() {

		try {
			PathQLResults pathqlResultsIterator = (PathQLResults) PathQL.evaluate(source,"[ eq :Unit1]");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=null, subject=http://inova8.com/calc2graph/def/Unit1],snippet=null, score=null]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(2)
	void pathql_2() {

		try {
			PathQLResults pathqlResultsIterator = (PathQLResults) PathQL.evaluate(source,"[ like 'Unit1']");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=null], predicate=http://www.w3.org/2000/01/rdf-schema#label, subject=http://inova8.com/calc2graph/id/Location_Unit1],snippet=Location <B>Unit1</B>, score=2.5126757621765137]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(3)
	void pathql_3() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ eq :Unit1]/:hasProductBatteryLimit");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void pathql_4() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ eq :Unit1]/:hasProductBatteryLimit/:volumeFlow");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=\"40\"^^<http://www.w3.org/2001/XMLSchema#int>], predicate=http://inova8.com/calc2graph/def/volumeFlow, subject=http://inova8.com/calc2graph/id/BatteryLimit2]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void pathql_5() {
		
		try {
			FactResults pathqlResultsIterator = (FactResults) PathQL.evaluate(source,"[ like \"Unit1\"]>:hasProductBatteryLimit");
			while (pathqlResultsIterator.hasNext()) {
				Resource nextMatch = pathqlResultsIterator.nextResource();
				assertEquals(
						"Fact [Resource[ object=http://inova8.com/calc2graph/id/BatteryLimit2], predicate=http://inova8.com/calc2graph/def/hasProductBatteryLimit, subject=http://inova8.com/calc2graph/id/Unit1]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
