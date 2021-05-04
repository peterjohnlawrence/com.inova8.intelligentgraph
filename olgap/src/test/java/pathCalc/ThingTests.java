/*
 * inova8 2020
 */
package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.lucene.QuerySpec;
import org.eclipse.rdf4j.sail.lucene.SearchIndex;
import org.eclipse.rdf4j.sail.lucene.SearchQueryEvaluator;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import olgap.ClearCache;
import pathQL.Match;
import pathQLModel.MatchFact;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;
import pathQLResults.ResourceResults;

/**
 * The Class ThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ThingTests {
	
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

	private static Match match;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
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
		match = new Match(source);
	}
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	
	/**
	 * Test 0.
	 */
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

	/**
	 * Match 1.
	 */
	@Test
	@Order(1)
	void match_1() {

		try {
//			NotifyingSailConnection luceneConnection = lucenesail.getConnection();
//			SearchIndex luceneIndex = lucenesail.getLuceneIndex();
//			
//			String matchesVarName = "anon";
//			String propertyVarName = "property";
//			String scoreVarName = "score";
//			String snippetVarName = "snippet";
//			//IRI subject = iri("entity");
//			String queryString = "Unit1";
//			IRI propertyURI = null;
//			SearchQueryEvaluator querySpec = new QuerySpec(matchesVarName, propertyVarName, scoreVarName, snippetVarName, null,
//					queryString, propertyURI);
//			Collection<BindingSet> queryResultSet = luceneIndex.evaluate(querySpec);

			MatchResults matchResultsIterator = match.entityMatch("Unit1");
			while (matchResultsIterator.hasNext()) {
				MatchFact nextMatch = matchResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=\"Location Unit1\"], predicate=http://www.w3.org/2000/01/rdf-schema#label, subject=http://inova8.com/calc2graph/id/Location_Unit1],snippet=Location <B>Unit1</B>, score=2.5126757621765137]",
						nextMatch.toString());
				break;
			}
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/volumeFlow>");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Resource result = $this.getFact(":volumeFlow");
			assertEquals("40", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}

	/**
	 * Test 2 1.
	 */
	@Test
	@Order(21)
	void test_2_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":hasFeedBatteryLimit/:volumeFlow");
			assertEquals("59", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}

	}

	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {

		try {
			Thing $this = Thing.create(source, iri("http://inova8.com/calc2graph/id/Unit1"), 	null);
			$this.prefix("http://inova8.com/calc2graph/def/");
			Double fact = 0.0;
			for (Resource batterylimit : $this.getFacts(":hasProductBatteryLimit")) {
				fact += batterylimit.getFact(":massFlow").doubleValue();
			}
			ResourceResults batterylimits = $this.getFacts(":hasProductBatteryLimit");
			Resource batterylimit;
			while (batterylimits.hasNext()) {
				batterylimit = batterylimits.next();
				fact -= batterylimit.getFact(":massFlow").doubleValue();
			}
			;
			assertEquals(0.0, fact);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact(":massThroughput");
			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	/**
	 * Test 4.1.
	 */
	@Test
	@Order(4)
	void test_4_1() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
			String trace = $this.traceFact(":massThroughput");
			assertEqualsWOSpaces("<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varmassThroughput=0.0;<br>for(batterylimitin$this.getFacts(&quot;:hasProductBatteryLimit&quot;)){<br>&nbsp;&nbsp;&nbsp;massThroughput+=batterylimit.getFact(&quot;:massFlow&quot;).doubleValue()<br>};<br>massThroughput;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Retrievedliteral<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=.306^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=0.306^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=0.306^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=12.239999771118164^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=12.239999771118164^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>script</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Retrievedliteral<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=.56^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=0.56^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=0.56^^<ahref='http://www.w3.org/2001/XMLSchema#float'target='_blank'>float</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=11.200000047683716^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=11.200000047683716^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=23.43999981880188^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=23.43999981880188^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol>", trace);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	@Test
	@Order(4)
	void test_4_2() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			String trace = $this.traceFact(":massFlow");
			assertEqualsWOSpaces("<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><li>Retrievedliteral<ahref='http://inova8.com/calc2graph/def/density'target='_blank'>density</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=.42^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol>", trace);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("def", "<http://inova8.com/calc2graph/def/>");
			Resource result = $this.getFact("def:Attribute@def:density");
			if (result != null)
				assertEquals(".42", result.stringValue());
			else
				fail();
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit3"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massFlow>");
			assertEquals("11.200000047683716", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massThroughput>");
			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Resource result = $this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput");

			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 9.
	 */
	@Test
	@Order(9)
	void test_9() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact(":massFlow").floatValue()
					/ $this.getFact("^:hasProductBatteryLimit").getFact(":massThroughput").floatValue();

			assertEquals("0.52218425", result.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"), null);
			Float result = $this.getFact("^:hasProductBatteryLimit/:massThroughput").floatValue();

			assertEquals("23.44", result.toString());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 11.
	 */
	@Test
	@Order(11)
	void test_11() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact(":lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 12.
	 */
	@Test
	@Order(12)
	void test_12() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 13.
	 */
	@Test
	@Order(13)
	void test_13() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph1]#").getFact(":lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 14.
	 */
	@Test
	@Order(14)
	void test_14() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq id:Calc2Graph2]#"); //""

			if (result != null)
				assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());

		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 15.
	 */
	@Test
	@Order(15)
	void test_15() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/long>");

			assertEquals("501", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 16.
	 */
	@Test
	@Order(16)
	void test_16() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]#");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL2", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 17.
	 */
	@Test
	@Order(17)
	void test_17() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit2]");

			assertEquals("http://inova8.com/calc2graph/id/BatteryLimit2", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 18.
	 */
	@Test
	@Order(18)
	void test_18() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Calc2Graph1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact("^:Location@:appearsOn[eq id:BatteryLimit1]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 19.
	 */
	@Test
	@Order(19)
	void test_19() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[ rdfs:label 'Calc2Graph2']#");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 20.
	 */
	@Test
	@Order(20)
	void test_20() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph2']]#/:lat");

			assertEquals("400", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 21.
	 */
	@Test
	@Order(21)
	void test_21() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph1 ]/:long");

			assertEquals("501", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 22.
	 */
	@Test
	@Order(22)
	void test_22() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");

			assertEquals("502", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 23.
	 */
	@Test
	@Order(23)
	void test_23() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]");

			assertEquals("http://inova8.com/calc2graph/id/Location_BL1_2", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 24.
	 */
	@Test
	@Order(24)
	void test_24() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");

			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 25.
	 */
	@Test
	@Order(25)
	void test_25() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");

			assertEquals("23.43999981880188", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 26.
	 */
	@Test
	@Order(26)
	void test_26() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit3"), null);

			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");

			assertEquals("24.77999922633171", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 27.
	 */
	@Test
	@Order(27)
	void test_27() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit3"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massFlowBalance");

			assertEquals("1.339999407529831", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	/**
	 * Test 28.
	 */
	@Test
	@Order(28)
	void test_28() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			ResourceResults results = $this.getFacts("(:Attribute@:density  |:density)");
			for (Resource result : results) {
				assertEquals(".42", result.stringValue());
			}
		} catch (Exception e) {
			fail();
		}
	}

	/**
	 * Test 30.
	 */
	@Test
	@Order(30)
	void test_30() {
		try {
			source.addGraph("http://inova8.com/calc2graph/testGraph");
			
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			ResourceResults results = $this.getFacts("(:Attribute@:density  |:density)");
			for (Resource result : results) {
				assertEquals(".42", result.stringValue());
			}
		} catch (Exception e) {
			fail();
		}
	}
	@Test
	@Order(34)
	void test_34() {
		
		try {
			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit3"),null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massYield>");
			assertEquals("0.4778156952152066", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	/**
	 * Test 100.
	 */
	@Test
	@Order(100)
	//literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
	void test_100() {
		try {
			ClearCache clearCache = new ClearCache();
			org.eclipse.rdf4j.model.Value result = clearCache.evaluate(repositoryTripleSource,
					iri("http://inova8.com/calc2graph/id/BatteryLimit1"),
					iri("http://inova8.com/calc2graph/def/testProperty4"),
					literal("$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;",
							iri("http://inova8.com/calc2graph/def/groovy")));
			assertEquals("true", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
}
