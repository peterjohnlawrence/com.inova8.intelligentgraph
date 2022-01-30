/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;
import com.inova8.pathql.processor.PathPatternException;

import utilities.Query;

/**
 * The Class Local_Sparql_IntelligentGraphTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_Sparql_IntelligentGraphTests {
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The source. */
	private static IntelligentGraphRepository source;
	


	/** The working rep. */
	private static org.eclipse.rdf4j.repository.Repository workingRep;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		//workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_Sparql_IntelligentGraphTests/");
		workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/Local_Sparql_IntelligentGraphTests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");
		
		conn = workingRep.getConnection();

		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
		source = IntelligentGraphRepository.create(workingRep);
	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		conn.close();
	}
	
	/**
	 * Adds the graph 2.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph2() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
		Thing myCountry = graph.getThing(":Country2");
		myCountry.addFact(":sales", "1");
		myCountry.addFact(":sales", "2");
		myCountry.addFact(":sales", "3");
		myCountry.addFact(":sales", "4");
		myCountry.addFact(":sales", "5");
		myCountry.addFact(":sales", "60");
		String averageSalesScript = "totalSales=0; count=0;for(sales in _this.getFacts(\"<http://inova8.com/calc2graph/def/sales>\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
		myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Adds the graph 3.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph3() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph3>");
		Thing myCountry = graph.getThing(":Country3");
		myCountry.addFact(":sales", "10");
		myCountry.addFact(":sales", "20");
		myCountry.addFact(":sales", "30");
		myCountry.addFact(":sales", "40");
		myCountry.addFact(":sales", "50");
		String totalSalesScript = "return _this.getFacts(\":sales\").total();";
		myCountry.addFact(":totalSales", totalSalesScript, SCRIPT.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Adds the graph 4.
	 *
	 * @return the thing
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private Thing addGraph4() throws RecognitionException, PathPatternException {
		source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph3>");
		source.removeGraph("<http://inova8.com/calc2graph/testGraph4>");
		Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph4>");
		Thing myCountry = graph.getThing(":Country4");
		myCountry.addFact(":sales", "100");
		myCountry.addFact(":sales", "200");
		myCountry.addFact(":sales", "300");
		myCountry.addFact(":sales", "400");
		myCountry.addFact(":sales", "500");
		String averageSalesScript = "return _this.getFacts(\":sales\").average();";
		myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
		return myCountry;
	}
	
	/**
	 * Ig 0.
	 */
	@Test
	@Order(0)
	void ig_0() {

		try {
			String queryString1 = "ASK { <file://calc2graph.def.ttl> <http://inova8.com/script/isPrivate> \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> }";

			Boolean result = Query.runBoolean(conn, queryString1);
			assertEquals(false
					,result);
		} catch (Exception e) {

			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 1.
	 */
	@Test
	@Order(1)
	void ig_1() {

		try {
			String queryString1 = "select $o  ?time FROM <file://src/test/resources/calc2graph.data.ttl> WHERE {VALUES(?time){(41)(42)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/testProperty6> $o } ";

			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("time=41;o=41;\r\n"
					+ "time=42;o=42;\r\n"
					+ ""
					,result); 
		} catch (Exception e) {

			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}



	/**
	 * Ig 3.
	 */
	@Test
	@Order(3)
	void ig_3() {

		try {
			String queryString1 = "select ?time $o $o_SCRIPT $o_TRACE  WHERE {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/volumeFlow> $o,$o_SCRIPT, $o_TRACE } limit 1";

			String result = Query.runSPARQL(conn, queryString1);
			Query.assertEqualsWOSpaces("o_SCRIPT=59;;o_TRACE=<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li>;time=41;o=59;"
					,result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 4.
	 */
	@Test
	@Order(4)
	void ig_4() {

		try {
			String queryString1 = "select * {VALUES(?time){(41)} <http://inova8.com/calc2graph/id/BatteryLimit1> <http://inova8.com/calc2graph/def/massFlow> $o} limit 1";

			String result = Query.runQuery(conn, queryString1);
			assertEquals("time=41;o=24.77999922633171;\r\n"
					+ ""
					,result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 5.
	 */
	@Test
	@Order(5)
	void ig_5() {
 
		try {
			String queryString1 = "select * {BIND(\"42*3;\"^^<http://inova8.com/script/groovy> as ?result) } ";

			String result = Query.runQuery(conn, queryString1);
			assertEquals("126",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 6.
	 */
	@Test
	@Order(6)
	void ig_6() {
		
		try {
			addGraph2();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select  ?o \n"
					+ "FROM <http://inova8.com/calc2graph/testGraph2>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=12.5;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 7.
	 */
	@Test
	@Order(7)
	void ig_7() {

		try {
			addGraph3();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph3>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :totalSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=150.0;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 8.
	 */
	@Test
	@Order(8)
	void ig_8() {

		try {
			addGraph4();
			String queryString1 = "PREFIX : <http://inova8.com/calc2graph/def/> select ?o "
					+ "FROM <http://inova8.com/calc2graph/testGraph4>\n"
					+ "FROM <file://calc2graph.data.ttl>\n"
					+ "FROM <file://calc2graph.def.ttl>\n"
					+ "{\n"
					+ "  ?s  :averageSales  ?o ,?o_SCRIPT,?o_TRACE} limit 10";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=300.0;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 9.
	 */
	@Test
	@Order(9)
	void ig_9() {
		try {
			addGraph4();
			String queryString1 = "select ?s   ?o "
					+ "{\n"
					+ "  ?s  <http://inova8.com/script/scriptCode> ?o } limit 1";


			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("s=http://inova8.com/calc2graph/id/calculateLatitude;o=return _this.getFact(\":Location@:appearsOn[eq id:Calc2Graph1]#\").getFact(\":lat\").integerValue();;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 10.
	 */
	@Test
	@Order(10)
	void ig_10() {
		try {
			String queryString1 = "select   ?o "
					+ "{\n"
					+ "  <http://inova8.com/calc2graph/id/Attribute_3>  <http://inova8.com/calc2graph/def/attribute.value> ?o }";
			String result = Query.runSPARQL(conn, queryString1);
			assertEquals("o=.5;\r\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 11.
	 */
	@Test
	@Order(11)
	void ig_11() {
		try {
			String queryString1 = "select   ?o ?o_TRACE ?o_SCRIPT "
					+ "{\n"
					+ "  <http://inova8.com/calc2graph/id/BatteryLimit1>  <http://inova8.com/calc2graph/def/testProperty2> ?o,  ?o_TRACE, ?o_SCRIPT }";
			String result = Query.runSPARQL(conn, queryString1);
			Query.assertEqualsWOSpaces("o_SCRIPT=_this.getFact(\":testProperty3\").doubleValue();o_TRACE=<olstyle='list-style-type:none;'><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;:testProperty3&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':testProperty3'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty3'target='_blank'>testProperty3</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts'&lt;http://inova8.com/calc2graph/def/testProperty2&gt;'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;:testProperty3&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Circularreferenceencounteredwhenevaluating<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>:</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>]</code></pre></div></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;:testProperty3&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Circularreferenceencounteredwhenevaluating<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>:</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>]</code></pre></div></li><li>Nextfact'http://inova8.com/calc2graph/def/testProperty2'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]</li></li><li>Returnedfact'&lt;http://inova8.com/calc2graph/def/testProperty2&gt;'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Scriptfailedwith<br/><code><spanstyle=\"white-space:pre-wrap\">java.lang.NumberFormatException:Forinputstring:&quot;Circularreferenceencounteredwhenevaluating&lt;http://inova8.com/calc2graph/def/testProperty2&gt;of&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;.[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;]&quot;</span></code></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty3'target='_blank'>testProperty3</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts'&lt;http://inova8.com/calc2graph/def/testProperty2&gt;'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;:testProperty3&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Circularreferenceencounteredwhenevaluating<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>:</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>]</code></pre></div></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_this.getFact(&quot;:testProperty3&quot;).doubleValue()</code></pre></div></li><olstyle='list-style-type:none;'><li>Circularreferenceencounteredwhenevaluating<ahref='http://inova8.com/calc2graph/def/testProperty2'target='_blank'>testProperty2</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>:</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;<br>]</code></pre></div></li><li>Nextfact'http://inova8.com/calc2graph/def/testProperty2'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]</li></li><li>Returnedfact'&lt;http://inova8.com/calc2graph/def/testProperty2&gt;'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Scriptfailedwith<br/><code><spanstyle=\"white-space:pre-wrap\">java.lang.NumberFormatException:Forinputstring:&quot;Circularreferenceencounteredwhenevaluating&lt;http://inova8.com/calc2graph/def/testProperty2&gt;of&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;.[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;]&quot;</span></code></li></li><li>Nextfact'http://inova8.com/calc2graph/def/testProperty3'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=java.lang.NumberFormatException:Forinputstring:\"Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]\"Forinputstring:\"Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]\"</li></li><li>Returnedfact':testProperty3'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=java.lang.NumberFormatException:Forinputstring:\"Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]\"Forinputstring:\"Circularreferenceencounteredwhenevaluating<http://inova8.com/calc2graph/def/testProperty2>of<http://inova8.com/calc2graph/id/BatteryLimit1>.[<http://inova8.com/calc2graph/def/testProperty2><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>,<http://inova8.com/calc2graph/def/testProperty3><http://inova8.com/calc2graph/id/BatteryLimit1>;queryOptions=o_SCRIPT=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o_TRACE=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>&o=\"_this.getFact(\":testProperty3\").doubleValue()\"^^<http://inova8.com/script/groovy>]\"^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Scriptfailedwith<br/><code><spanstyle=\"white-space:pre-wrap\">java.lang.NumberFormatException:Forinputstring:&quot;java.lang.NumberFormatException:Forinputstring:&quot;Circularreferenceencounteredwhenevaluating&lt;http://inova8.com/calc2graph/def/testProperty2&gt;of&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;.[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;]&quot;Forinputstring:&quot;Circularreferenceencounteredwhenevaluating&lt;http://inova8.com/calc2graph/def/testProperty2&gt;of&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;.[&lt;http://inova8.com/calc2graph/def/testProperty2&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;,&lt;http://inova8.com/calc2graph/def/testProperty3&gt;&lt;http://inova8.com/calc2graph/id/BatteryLimit1&gt;;queryOptions=o_SCRIPT=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o_TRACE=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;&amp;o=&quot;_this.getFact(&quot;:testProperty3&quot;).doubleValue()&quot;^^&lt;http://inova8.com/script/groovy&gt;]&quot;&quot;</span></code></li></li>;o=javax.script.ScriptException:java.lang.NumberFormatException:Forinputstring:\\\"java.lang.NumberFormatException:Forinputstring:\\\"Circularreferenceencounteredwhenevaluating<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2>of<http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>.\\r\\n[<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2><http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>;queryOptions=o_SCRIPT=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o_TRACE=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>\\r\\n,<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty3><http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>;queryOptions=o_SCRIPT=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o_TRACE=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>\\r\\n]\\\"\\nForinputstring:\\\"Circularreferenceencounteredwhenevaluating<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2>of<http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>.\\r\\n[<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty2><http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>;queryOptions=o_SCRIPT=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o_TRACE=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>\\r\\n,<http:\\/\\/inova8.com\\/calc2graph\\/def\\/testProperty3><http:\\/\\/inova8.com\\/calc2graph\\/id\\/BatteryLimit1>;queryOptions=o_SCRIPT=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o_TRACE=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>&o=\\\"_this.getFact(\\\":testProperty3\\\").doubleValue()\\\"^^<http:\\/\\/inova8.com\\/script\\/groovy>\\r\\n]\\\"\\\";",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Ig 12.
	 */
	@Test
	@Order(12)
	void ig_12() {
		try {
			String queryString1 = "CONSTRUCT {\n"
					+ "	#targetEntityIdentifier\n"
					+ "	?BatteryLimit_s <http://targetEntity> <http://inova8.com/calc2graph/def/BatteryLimit> .\n"
					+ "	?BatteryLimit_s <http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId> ?BatteryLimit_s .\n"
					+ "	#constructPath\n"
					+ "	?BatteryLimit_s ?BatteryLimit_p ?BatteryLimit_o .\n"
					+ "	#constructComplex\n"
					+ "	?BatteryLimit_s <http://inova8.com/calc2graph/def/item.attribute> ?BatteryLimititem_attribute_s .\n"
					+ "	#constructExpandSelect\n"
					+ "}\n"
					+ "WHERE {\n"
					+ "	{\n"
					+ "	#selectExpand\n"
					+ "	{	SELECT *\n"
					+ "		#selectExpandWhere\n"
					+ "		{\n"
					+ "			#selectPath\n"
					+ "			{	SELECT DISTINCT\n"
					+ "					?BatteryLimit_s\n"
					+ "				WHERE {\n"
					+ "					#clausesPath_URI1\n"
					+ "					VALUES(?class){(<http://inova8.com/calc2graph/def/BatteryLimit>)}\n"
					+ "					?BatteryLimit_s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?class .\n"
					+ "					#search\n"
					+ "					#clausesFilter\n"
					+ "					#clausesExpandFilter\n"
					+ "					#filter\n"
					+ "				} LIMIT 152\n"
					+ "			}\n"
					+ "		}\n"
					+ "	}\n"
					+ "	#clausesPathProperties\n"
					+ "	VALUES(?BatteryLimit_p){\n"
					+ "   #   (<http://inova8.com/calc2graph/def/appearsOn>)\n"
					+ "   #   (<http://inova8.com/calc2graph/def/density>)(<http://inova8.com/calc2graph/def/lat>)\n"
					+ "      (<http://inova8.com/calc2graph/def/long>)\n"
					+ "   #  (<http://inova8.com/calc2graph/def/massFlow>)\n"
					+ "      #(<http://inova8.com/calc2graph/def/massYield>)\n"
					+ "    #  (<http://inova8.com/calc2graph/def/testProperty1>)\n"
					+ "      #(<http://inova8.com/calc2graph/def/testProperty2>)\n"
					+ "   #  (<http://inova8.com/calc2graph/def/testProperty3>)\n"
					+ "  #    (<http://inova8.com/calc2graph/def/testProperty4>)\n"
					+ "     # (<http://inova8.com/calc2graph/def/testProperty5>)\n"
					+ "  #    (<http://inova8.com/calc2graph/def/volumeFlow>)\n"
					+ "  #    (<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>)\n"
					+ "  #    (<http://www.w3.org/2000/01/rdf-schema#comment>)\n"
					+ "      (<http://www.w3.org/2000/01/rdf-schema#label>)\n"
					+ "    }\n"
					+ "		?BatteryLimit_s ?BatteryLimit_p ?BatteryLimit_o .\n"
					+ "	#clausesComplex\n"
					+ "	}\n"
					+ "	#clausesExpandSelect\n"
					+ "} LIMIT 10000000";
			String result = Query.runCONSTRUCT(conn, queryString1);
			assertEquals("(http://inova8.com/calc2graph/id/BatteryLimit1, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit1)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/long, \"501\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit1\")\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit2)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://inova8.com/calc2graph/def/long, \"600\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit2, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit2\")\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://targetEntity, http://inova8.com/calc2graph/def/BatteryLimit)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://www.w3.org/1999/02/22-rdf-syntax-ns#subjectId, http://inova8.com/calc2graph/id/BatteryLimit3)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://inova8.com/calc2graph/def/long, \"0\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit3, http://www.w3.org/2000/01/rdf-schema#label, \"BatteryLimit3\")\n"
					+ "",result);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}

	}		
	
	/**
	 * Ig 13.
	 */
	@Test
	@Order(13)
	void ig_13() {

		try {
			String queryString1 = "CONSTRUCT{<http://inova8.com/context>  <http://inova8.com/context/time>   42. ?s <http://inova8.com/calc2graph/def/testProperty6> $o   } WHERE {select ?time ?s $o {VALUES(?time){(41)} ?s <http://inova8.com/calc2graph/def/testProperty6> $o }} ";

			String result = Query.runCONSTRUCT(conn, queryString1);
			assertEquals("(http://inova8.com/context, http://inova8.com/context/time, \"42\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ "(http://inova8.com/calc2graph/id/BatteryLimit1, http://inova8.com/calc2graph/def/testProperty6, \"42\"^^<http://www.w3.org/2001/XMLSchema#integer>)\n"
					+ ""
					,result); 
		} catch (Exception e) {

			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
