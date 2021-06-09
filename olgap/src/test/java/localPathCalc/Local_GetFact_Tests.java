/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQL.Match;
import pathQLModel.MatchFact;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.MatchResults;
import pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetFact_Tests {
	

	
	/** The source. */
	private static PathQLRepository source;
	

	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_GetFact_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
		source = PathQLRepository.create(workingRep);
		source.prefix("<http://inova8.com/calc2graph/def/>");
		source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");

	}
	
	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	
	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	


	/**
	 * Match 1.
	 */
	@Test
	@Order(1)
	void match_1() {

		try {
			Match match=new Match(source);
			MatchResults matchResultsIterator = match.entityMatch("Unit1");
			while (matchResultsIterator.hasNext()) {
				MatchFact nextMatch = matchResultsIterator.nextResource();
				assertEquals(
						"MatchFact [Fact [Resource[ object=\"Location Unit1\"], predicate=http://www.w3.org/2000/01/rdf-schema#label, subject=http://inova8.com/calc2graph/id/Location_Unit1],snippet=Location <B>Unit1</B>, score=2.309943199157715]",
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
	void test_1() {
		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			$this.getFact("<http://inova8.com/calc2graph/def/testProperty1>");
		} catch (Exception e) {
			assertEquals("javax.script.ScriptException: Exceptions.ScriptFailedException: javax.script.ScriptException: Exceptions.ScriptFailedException: javax.script.ScriptException: Exceptions.CircularReferenceException: Circular reference encountered when evaluating <http://inova8.com/calc2graph/def/testProperty2> of <http://inova8.com/calc2graph/id/BatteryLimit1>.\r\n"
					+ "[<http://inova8.com/calc2graph/def/testProperty2> <http://inova8.com/calc2graph/id/BatteryLimit1>; queryOptions=\r\n"
					+ ", <http://inova8.com/calc2graph/def/testProperty3> <http://inova8.com/calc2graph/id/BatteryLimit1>; queryOptions=\r\n"
					+ "]", e.getMessage());
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
			assertEquals("37.99999952316284", result.stringValue());
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
			assertEqualsWOSpaces("<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varmassThroughput=0.0;<br>for(batterylimitin$this.getFacts(&quot;:hasProductBatteryLimit&quot;)){<br>&nbsp;&nbsp;&nbsp;massThroughput+=batterylimit.getFact(&quot;:massFlow&quot;).doubleValue()<br>};<br>massThroughput;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varmassThroughput=0.0;<br>for(batterylimitin$this.getFacts(&quot;:hasProductBatteryLimit&quot;)){<br>&nbsp;&nbsp;&nbsp;massThroughput+=batterylimit.getFact(&quot;:massFlow&quot;).doubleValue()<br>};<br>massThroughput;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><li>Retrievedliteral<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol>", trace);
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 4 2.
	 */
	@Test
	@Order(4)
	void test_4_2() {

		try {
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
			String trace = $this.traceFact(":massFlow");
			assertEqualsWOSpaces("<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>$this.prefix(&quot;&lt;http://inova8.com/calc2graph/def/&gt;&quot;);<br>varresult=$this.getFact(&quot;:volumeFlow&quot;).floatValue()*$this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li><li>Retrievedliteral<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></ol>", trace);
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
			assertEquals("10.0", result.stringValue());
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
			assertEquals("37.99999952316284", result.stringValue());
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

			assertEquals("37.99999952316284", result.stringValue());
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

			assertEquals("0.7368421", result.toString());
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

			assertEquals("38.0", result.toString());
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

			assertEquals("37.99999952316284", result.stringValue());
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
			String averageSalesScript = "return $this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\").average();";
			Thing $this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit2"), null);
			$this.prefix("id", "<http://inova8.com/calc2graph/id/>");
			Resource result = $this.getFact(":massThroughput");

			assertEquals("37.99999952316284", result.stringValue());
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

			assertEquals("-13.220000296831131", result.stringValue());
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
	
	/**
	 * Test 34.
	 */
	@Test
	@Order(34)
	void test_34() {
		
		try {
			source = PathQLRepository.create(workingRep);

			Thing $this = source.getThing( iri("http://inova8.com/calc2graph/id/BatteryLimit3"),null);
			Resource result = $this.getFact("<http://inova8.com/calc2graph/def/massYield>");
			assertEquals("0.26315789803903855", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
	/**
	 * Test 35.
	 */
	@Test
	@Order(35)
	void test_35() {
		
		try {
			source = PathQLRepository.create(workingRep);
			source.prefix("<http://inova8.com/calc2graph/def/>");
			source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
			source.removeGraph("<http://inova8.com/calc2graph/contextGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/contextGraph>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":time", "$customQueryOptions.get(\"time\") ;", Evaluator.GROOVY);
			CustomQueryOptions  customQueryOptions = new CustomQueryOptions();
			customQueryOptions.add("time",42);
			customQueryOptions.add("name","Peter");
			Thing myCountry1 = graph.getThing( ":Country1");
			Resource result = myCountry1.getFact("<http://inova8.com/calc2graph/def/time>",customQueryOptions);
			assertEquals("42", result.stringValue());
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}

}
