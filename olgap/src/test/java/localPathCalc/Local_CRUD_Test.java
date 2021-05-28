/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.antlr.v4.runtime.RecognitionException;
import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
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

import intelligentGraph.IntelligentGraphConfig;
import intelligentGraph.IntelligentGraphFactory;
import intelligentGraph.IntelligentGraphSail;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternProcessor.PathPatternException;
import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import pathQLResults.ResourceResults;
import utilities.Query;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_CRUD_Test {
	
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	private static PathQLRepository source;
	
	/** The evaluator. */
	private static Evaluator evaluator;


	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/Local_CRUD_Test/");
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
		
		LuceneSail lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");

		
		Sail baseSail = new NativeStore(dataDir);		
		lucenesail.setBaseSail(baseSail);
		intelligentGraphSail.setBaseSail(lucenesail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		
		
		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null),iri("http://default"));

		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null),iri("http://default"));

		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source = PathQLRepository.create(workingRep);
	}

	
	/**
	 * Ig 0.
	 */
	@Test
	@Order(0)
	void ig_0() {

		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			ResourceResults facts = myCountry.getFacts(":sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			myCountry.deleteFacts(":sales[eq '3']");
			factsinrange = myCountry.getFacts(":sales[ge '2';lt '4']").count();
			assertEquals(1, factsinrange);
			Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph1>");
			assertEquals(true, closed);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(1)
	void ig_1() {

		try {
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country2");
			myCountry.addFact(":Attribute@:sales", "1");
			myCountry.addFact(":Attribute@:sales", "2");
			myCountry.addFact(":Attribute@:sales", "3");
			myCountry.addFact(":Attribute@:sales", "4");
			ResourceResults facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			myCountry.deleteFacts(":Attribute@:sales[eq '3']");
			facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			factsinrange = facts.count();
			assertEquals(1, factsinrange);
			String averageSalesScript = "totalSales=0; count=0;for(sales in $this.getFacts(\":Attribute@:sales\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, Evaluator.GROOVY) ;
			Resource averageSales = myCountry.getFact(":averageSales");
			assertEquals(2.3333333333333335, averageSales.doubleValue());
			Thing country3= myCountry.getThing(":Country3");
			String averageSalesScript3 = "totalSales=0; count=0; myCountry=$this.getThing(\":Country2\"); for(sales in myCountry.getFacts(\":Attribute@:sales\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			country3.addFact(":averageSales", averageSalesScript3, Evaluator.GROOVY) ;
			Resource averageSales3 = myCountry.getFact(":averageSales");
			assertEquals(2.3333333333333335, averageSales3.doubleValue());
			Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals(true, closed);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}	
	@Test
	@Order(2)
	void ig_2() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(0.7, ((Literal) object).doubleValue(),0);
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}	
	@Test
	@Order(3)
	void ig_3() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				Value object = statement.getObject();
			}
			statements = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), iri("http://inova8.com/calc2graph/def/testProperty1"), null);
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(0.7, ((Literal) object).doubleValue(),0);
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void ig_4() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements((org.eclipse.rdf4j.model.Resource)iri("http://inova8.com/calc2graph/id/Unit1"), iri("http://inova8.com/pathql/getFacts"), literal("<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>"));
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals("http://inova8.com/calc2graph/id/BatteryLimit1",  object.stringValue());
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void ig_5() {

		try {
			RepositoryResult<Statement> statements = conn.getStatements((org.eclipse.rdf4j.model.Resource)iri("http://inova8.com/calc2graph/id/Unit1"), iri("http://inova8.com/pathql/getFacts"), literal("<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>/<http://inova8.com/calc2graph/def/density>"));
			for(Statement statement: statements) {
				Value object = statement.getObject();
				assertEquals(".42",  object.stringValue());
			}
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}
}
