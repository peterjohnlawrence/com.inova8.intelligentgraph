/*
 * inova8 2020
 */
package pathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.iterator.QueryContextIteration;
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
import pathQL.PathQL;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLResults.FactResults;
import pathQLResults.PathQLResults;
import static org.eclipse.rdf4j.model.util.Values.iri;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IntelligentGraphTests {
	
	/** The lucenesail. */
	static LuceneSail lucenesail ;
	
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
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
		
	    lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");

		Sail baseSail = new NativeStore(dataDir);		
		lucenesail.setBaseSail(baseSail);
		intelligentGraphSail.setBaseSail(lucenesail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		
//		Sail baseSail = new NativeStore(dataDir);		
//		intelligentGraphSail.setBaseSail(baseSail);		
//		lucenesail.setBaseSail(intelligentGraphSail);	
//		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(lucenesail);
		
		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null));

		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null),iri("http://default"));
		boolean namespace = conn.getNamespaces().hasNext();
		boolean context = conn.getContextIDs().hasNext();
		long size = conn.size();
		int i=1;
	}

	public static String runQuery(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		QueryContext queryContext = new QueryContext();
		queryContext.setAttribute("test", conn);
		QueryContextIteration contextResults = new QueryContextIteration(query.evaluate(), queryContext);
		StringBuilder aResult = new StringBuilder();
		while (contextResults.hasNext()) {

			BindingSet solution = contextResults.next();
			Set<String> bindingNames = solution.getBindingNames();
			if(bindingNames.size()==1) {
				for (String bindingName : bindingNames) {
					if (solution.getValue(bindingName) != null)
						aResult.append(solution.getValue(bindingName).stringValue());
				}
			}else {
			for (String bindingName : bindingNames) {
				if (solution.getValue(bindingName) != null)
					aResult.append(bindingName).append("=").append(solution.getValue(bindingName).stringValue())
							.append(";");
			}
			}
		}
		contextResults.close();
		return aResult.toString();
	}

	public static String runSPARQL(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		StringBuilder aResult = new StringBuilder();
		try(TupleQueryResult result = query.evaluate()){
			for (BindingSet bindingSet: result) {
				Set<String> bindingNames = bindingSet.getBindingNames();
				for (String bindingName : bindingNames) {
					if (bindingSet.getValue(bindingName) != null)
						aResult.append(bindingName).append("=").append(bindingSet.getValue(bindingName).stringValue())
								.append(";");
				}
			}
			
		}
		return aResult.toString();
	}
	@Test
	@Order(1)
	void ig_1() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} ?s <http://inova8.com/calc2graph/def/testProperty6> $o } limit 1";

			String result = runQuery(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/id/BatteryLimit1;time=41;o=41;"
					,result);
//			String result = runSPARQL(conn, queryString1);
//			assertEquals("time=41;o=41;"
//					,result);
		} catch (Exception e) {

			fail();
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void ig_2() {

		try {
			//RepositoryResult<Statement> results = conn.getStatements(null, iri("http://inova8.com/calc2graph/def/volumeFlow"), null);
			//   RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null, null);
			RepositoryResult<Statement> results = conn.getStatements(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), iri("http://inova8.com/calc2graph/def/volumeFlow"), null);
			Statement result;
			while( results.hasNext()) {
				result=results.next();
				org.eclipse.rdf4j.model.Resource subject = result.getSubject();
				 Value object = result.getObject();
				 assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/volumeFlow, \"59\"^^<http://www.w3.org/2001/XMLSchema#int>) [null]",result.toString());
				 break;
				
			}
		//	
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	@Order(3)
	void ig_3() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/volumeFlow> $o,$o_SCRIPT, $o_TRACE } limit 1";

			String result = runSPARQL(conn, queryString1);
			assertEquals("o_SCRIPT=59;;o_TRACE=<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a>, by invoking <b>javascript</b> script\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >59;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/volumeFlow' target='_blank'>volumeFlow</a> of <a href='http://inova8.com/calc2graph/id/BatteryLimit1' target='_blank'>BatteryLimit1</a> =  59^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol></ol>;time=41;o=59;"
					+ "",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void ig_4() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/massFlow> $o} limit 1";

			String result = runQuery(conn, queryString1);
			assertEquals("time=41;o=24.77999922633171;"
					,result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(5)
	void ig_5() {

		try {
			String queryString1 = "select * {BIND(\"42*3;\"^^<http://inova8.com/script/groovy> as ?result) } ";

			String result = runQuery(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
