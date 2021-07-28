/*
 * inova8 2020
 */
package localPathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathCalc.Trace;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetTraceFact_Tests {
		
	/** The source. */
//	private static PathQLRepository source;
	
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	//	workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_GetTraceFact_Tests/");
		workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/Local_GetTraceFact_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");	
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("def", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
		//source = PathQLRepository.create(workingRep);

	}

	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}	
		@Test
		@Order(1)
		void test_1() {
			try {
				PathQLRepository source = PathQLRepository.create(workingRep);
				Thing _this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit2"));
				Resource result1 = _this.getFact(":volumeFlow");
				Trace trace = _this.traceFact(":massFlow");
				_this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"));
				Resource result2 = _this.getFact(":massThroughput");
				
				
				//assertEquals("40", result1.stringValue());
				Query.assertEqualsWOSpaces
				//assertEquals
				("<olstyle='list-style-type:none;'><li>Gettingfacts':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]</li></li><li>Redirectingevaluationofpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,to<b>&lt;http://inova8.com/calc2graph/id/calculateMassFlow&gt;</b>script</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>varresult=_this.getFact(&quot;:volumeFlow&quot;).floatValue()*_this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40</li></li><li>Returnedfact':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p><li>Gettingfacts':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=.7</li></li><li>Returnedfact':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=.7^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p></ol>",
						trace.asHTML());
				assertEquals("37.99999952316284", result2.stringValue());
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
	
		}
		@Test
		@Order(2)
		void test_2() {
			try {
				PathQLRepository source = PathQLRepository.create(workingRep);
				Thing _this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"));
				Trace trace =  _this.traceFact(":volumeFlow");
				
				Query.assertEqualsWOSpaces
				//assertEquals
				("<olstyle='list-style-type:none;'><li>Gettingfacts':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p></ol>"
						, trace.asHTML());
				Query.assertEqualsWOSpaces
				//assertEquals
				("1.Gettingfacts':volumeFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>2....withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]1.EvaluatingpredicatevolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>,byinvokingjavascriptscript2.59;3.EvaluatedvolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>3.CalculatedvolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>4.RetrievedcachedvaluevolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>5.Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>"
						, trace.asText());
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
	
		}
	
		@Test
		@Order(3)
		void test_3() {
	
			try {
				PathQLRepository source = PathQLRepository.create(workingRep);
				Thing _this =source.getThing(iri("http://inova8.com/calc2graph/id/Unit1"), null);
				source.clearCache();
				Trace trace = _this.traceFact(":massThroughput");
				Query.assertEqualsWOSpaces
				//assertEquals
				("<olstyle='list-style-type:none;'><li>Gettingfacts':massThroughput'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a></li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>varmassThroughput=0.0;<br>for(batterylimitin_this.getFacts(&quot;:hasProductBatteryLimit&quot;)){<br>&nbsp;&nbsp;&nbsp;massThroughput+=batterylimit.getFact(&quot;:massFlow&quot;).doubleValue()<br>};<br>massThroughput;</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':hasProductBatteryLimit'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/hasProductBatteryLimit'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=http://inova8.com/calc2graph/id/BatteryLimit2</li></li><li>Gettingfacts':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><li>Redirectingevaluationofpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,to<b>&lt;http://inova8.com/calc2graph/id/calculateMassFlow&gt;</b>script</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>varresult=_this.getFact(&quot;:volumeFlow&quot;).floatValue()*_this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>,byinvoking<b>javascript</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>40;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40</li></li><li>Returnedfact':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=40^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p><li>Gettingfacts':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=.7</li></li><li>Returnedfact':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=.7^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842</li></li><li>Returnedfact':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit2'target='_blank'>BatteryLimit2</a>=27.999999523162842^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p><li>Returnedfact'http://inova8.com/calc2graph/def/hasProductBatteryLimit'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=http://inova8.com/calc2graph/id/BatteryLimit3</li></li><li>Gettingfacts':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a></li></li><li>Redirectingevaluationofpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,to<b>&lt;http://inova8.com/calc2graph/id/calculateMassFlow&gt;</b>script</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>varresult=_this.getFact(&quot;:volumeFlow&quot;).floatValue()*_this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>20;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20</li></li><li>Returnedfact':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=20^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p><li>Gettingfacts':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=.5</li></li><li>Returnedfact':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=.5^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0</li></li><li>Returnedfact':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit3'target='_blank'>BatteryLimit3</a>=10.0^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massThroughput'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p></ol>"
						, trace.asHTML());
		
				 trace = _this.traceFact(":massThroughput");
				Query.assertEqualsWOSpaces
				// assertEquals
					("<olstyle='list-style-type:none;'><li>Gettingfacts':massThroughput'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a></li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]</li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massThroughput'target='_blank'>massThroughput</a>of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massThroughput'of<ahref='http://inova8.com/calc2graph/id/Unit1'target='_blank'>Unit1</a>=37.99999952316284^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p></ol>"
							, trace.asHTML());
				source.clearCache();
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
		}
		@Test
		@Order(4)
		void test_4() {
	
			try {
				PathQLRepository source = PathQLRepository.create(workingRep);
				//Graph graph = source.addGraph("<file://src/test/resources/calc2graph.data.ttl>");
				//Thing _this =graph.getThing("<http://inova8.com/calc2graph/id/BatteryLimit1>");
				Thing _this =source.getThing(iri("http://inova8.com/calc2graph/id/BatteryLimit1"), null);
				Trace trace = _this.traceFact(":massFlow");
				Query.assertEqualsWOSpaces
				// assertEquals
				("<olstyle='list-style-type:none;'><li>Gettingfacts':massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]</li></li><li>Redirectingevaluationofpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,to<b>&lt;http://inova8.com/calc2graph/id/calculateMassFlow&gt;</b>script</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>varresult=_this.getFact(&quot;:volumeFlow&quot;).floatValue()*_this.getFact(&quot;:Attribute@:density&quot;).floatValue();&nbsp;<br>result;</code></pre></div></li><olstyle='list-style-type:none;'><li>Gettingfacts':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>,byinvoking<b>javascript</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>59;</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/volumeFlow'target='_blank'>volumeFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59</li></li><li>Returnedfact':volumeFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=59^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p><li>Gettingfacts':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=.42</li></li><li>Returnedfact':Attribute@:density'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=.42^^<ahref='http://www.w3.org/2001/XMLSchema#string'target='_blank'>string</a></li></li><p></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/massFlow'target='_blank'>massFlow</a>of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/massFlow'of<ahref='http://inova8.com/calc2graph/id/BatteryLimit1'target='_blank'>BatteryLimit1</a>=24.77999922633171^^<ahref='http://www.w3.org/2001/XMLSchema#double'target='_blank'>double</a></li></li><p></ol>"
						, trace.asHTML());
				//String renderedTrace = new net.htmlparser.jericho.Source(trace).getRenderer().setMaxLineLength(Integer.MAX_VALUE).setNewLine(null).toString();
				source.clearCache();
				Trace renderedTrace = _this.traceFact(":massFlow");
				Query.assertEqualsWOSpaces
				// assertEquals
				("1.Gettingfacts':massFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>2....withincontexts:[file://src/test/resources/calc2graph.def.ttl,file://src/test/resources/calc2graph.data.ttl]3.RedirectingevaluationofpredicatemassFlow<http://inova8.com/calc2graph/def/massFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>,to<http://inova8.com/calc2graph/id/calculateMassFlow>script1.EvaluatingpredicatemassFlow<http://inova8.com/calc2graph/def/massFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>,byinvokinggroovyscript2.varresult=_this.getFact(\":volumeFlow\").floatValue()*_this.getFact(\":Attribute@:density\").floatValue();result;1.Gettingfacts':volumeFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>1.EvaluatingpredicatevolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>,byinvokingjavascriptscript2.59;3.EvaluatedvolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>2.CalculatedvolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>3.RetrievedcachedvaluevolumeFlow<http://inova8.com/calc2graph/def/volumeFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>4.Returnedfact'http://inova8.com/calc2graph/def/volumeFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=595.Returnedfact':volumeFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=59^^int<http://www.w3.org/2001/XMLSchema#int>6.Gettingfacts':Attribute@:density'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>7.Returnedfact'http://inova8.com/calc2graph/def/density'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=.428.Returnedfact':Attribute@:density'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=.42^^string<http://www.w3.org/2001/XMLSchema#string>3.EvaluatedmassFlow<http://inova8.com/calc2graph/def/massFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=24.77999922633171^^double<http://www.w3.org/2001/XMLSchema#double>4.CalculatedmassFlow<http://inova8.com/calc2graph/def/massFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=24.77999922633171^^double<http://www.w3.org/2001/XMLSchema#double>5.RetrievedcachedvaluemassFlow<http://inova8.com/calc2graph/def/massFlow>ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=24.77999922633171^^double<http://www.w3.org/2001/XMLSchema#double>6.Returnedfact'http://inova8.com/calc2graph/def/massFlow'ofBatteryLimit1<http://inova8.com/calc2graph/id/BatteryLimit1>=24.77999922633171^^double<http://www.w3.org/2001/XMLSchema#double>"
						, renderedTrace.asText());
			} catch (Exception e) {
				fail();
				e.printStackTrace();
			}
		}
	@Test
	@Order(5)
	void test_5() {
		
		try {
			PathQLRepository source = PathQLRepository.create(workingRep);
			source.removeGraph("<http://inova8.com/calc2graph/contextGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/contextGraph>");
			Thing myCountry = graph.getThing(":myCountry");
			myCountry.addFact(":myOption", "_customQueryOptions.get(\"time\").integerValue() ;", Evaluator.GROOVY);
			CustomQueryOptions  customQueryOptions1 = new CustomQueryOptions();
			customQueryOptions1.add("time",42);
		    customQueryOptions1.add("name","Peter");
			Thing myCountry1 = graph.getThing( ":myCountry");
			Trace result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions1);
			Query.assertEqualsWOSpaces
			//assertEquals
			("<olstyle='list-style-type:none;'><li>Gettingfacts'&lt;http://inova8.com/calc2graph/def/myOption&gt;'of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a></li></li><li>...usingoptions:[name=&quot;Peter&quot;&amp;time=&quot;42&quot;^^&lt;http://www.w3.org/2001/XMLSchema#int&gt;]</li></li><li>...withincontexts:[file://src/test/resources/calc2graph.def.ttl,http://inova8.com/calc2graph/contextGraph,file://src/test/resources/calc2graph.data.ttl]</li></li><olstyle='list-style-type:none;'><li>Evaluatingpredicate<ahref='http://inova8.com/calc2graph/def/myOption'target='_blank'>myOption</a>of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a>,byinvoking<b>groovy</b>script</li></li><li><divstyle='border:1pxsolidblack;'><pre><code>_customQueryOptions.get(&quot;time&quot;).integerValue();</code></pre></div></li><olstyle='list-style-type:none;'></ol><li>Evaluated<ahref='http://inova8.com/calc2graph/def/myOption'target='_blank'>myOption</a>of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a>=42^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li></ol><li>Calculated<ahref='http://inova8.com/calc2graph/def/myOption'target='_blank'>myOption</a>of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a>=42^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Retrievedcachedvalue<ahref='http://inova8.com/calc2graph/def/myOption'target='_blank'>myOption</a>of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a>=42^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><li>Returnedfact'http://inova8.com/calc2graph/def/myOption'of<ahref='http://inova8.com/calc2graph/def/myCountry'target='_blank'>myCountry</a>=42^^<ahref='http://www.w3.org/2001/XMLSchema#int'target='_blank'>int</a></li></li><p></ol>"
					, result.asHTML());
			CustomQueryOptions  customQueryOptions2 = new CustomQueryOptions();
			customQueryOptions2.add("time",43);
			result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions2 );
			Query.assertEqualsWOSpaces
			//assertEquals
			("1.Gettingfacts'<http://inova8.com/calc2graph/def/myOption>'ofmyCountry<http://inova8.com/calc2graph/def/myCountry>2....usingoptions:[time=\"43\"^^<http://www.w3.org/2001/XMLSchema#int>]3....withincontexts:[file://src/test/resources/calc2graph.def.ttl,http://inova8.com/calc2graph/contextGraph,file://src/test/resources/calc2graph.data.ttl]1.EvaluatingpredicatemyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>,byinvokinggroovyscript2._customQueryOptions.get(\"time\").integerValue();3.EvaluatedmyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=43^^int<http://www.w3.org/2001/XMLSchema#int>4.CalculatedmyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=43^^int<http://www.w3.org/2001/XMLSchema#int>5.RetrievedcachedvaluemyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=43^^int<http://www.w3.org/2001/XMLSchema#int>6.Returnedfact'http://inova8.com/calc2graph/def/myOption'ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=43^^int<http://www.w3.org/2001/XMLSchema#int>"
					, result.asText());
			result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>&time='2019'^^xsd:int" );
			Query.assertEqualsWOSpaces
			//assertEquals
			("1.Gettingfacts'<http://inova8.com/calc2graph/def/myOption>&time='2019'^^xsd:int'ofmyCountry<http://inova8.com/calc2graph/def/myCountry>2....withincontexts:[file://src/test/resources/calc2graph.def.ttl,http://inova8.com/calc2graph/contextGraph,file://src/test/resources/calc2graph.data.ttl]1.EvaluatingpredicatemyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>,byinvokinggroovyscript2._customQueryOptions.get(\"time\").integerValue();3.EvaluatedmyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=2019^^int<http://www.w3.org/2001/XMLSchema#int>3.CalculatedmyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=2019^^int<http://www.w3.org/2001/XMLSchema#int>4.RetrievedcachedvaluemyOption<http://inova8.com/calc2graph/def/myOption>ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=2019^^int<http://www.w3.org/2001/XMLSchema#int>5.Returnedfact'http://inova8.com/calc2graph/def/myOption'ofmyCountry<http://inova8.com/calc2graph/def/myCountry>=2019^^int<http://www.w3.org/2001/XMLSchema#int>"
					, result.asText());
			ResourceResults results = myCountry1.getFacts("<http://inova8.com/calc2graph/def/myOption>&time='2020'^^xsd:int" );
			for (Resource result1:results ) {
				Query.assertEqualsWOSpaces("2020", result1.stringValue());
			}
			
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		} 
	}
}
