/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pathCalc.CustomQueryOptions;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQLModel.Resource;
import pathQLRepository.Graph;
import pathQLRepository.PathQLRepository;
import pathQLResults.ResourceResults;
import utilities.Query;

/**
 * The Class ThingTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_GetContextFact_Tests {
	
	
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_GetContextFact_Tests/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");	
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("id", "http://inova8.com/calc2graph/id/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
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

	@Test
	@Order(1)
	void test_1() {
		
		try {
			PathQLRepository source = PathQLRepository.create(workingRep);
			//source.prefix("<http://inova8.com/calc2graph/def/>");
			//source.prefix("rdfs", "<http://www.w3.org/2000/01/rdf-schema#>");
			source.removeGraph("<http://inova8.com/calc2graph/contextGraph>");
			//source.prefix("xsd", "<http://www.w3.org/2001/XMLSchema#>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/contextGraph>");
			Thing myCountry = graph.getThing(":myCountry");
			myCountry.addFact(":myOption", "$customQueryOptions.get(\"time\").integerValue() ;", Evaluator.GROOVY);
			CustomQueryOptions  customQueryOptions1 = new CustomQueryOptions();
			customQueryOptions1.add("time",42);
		    customQueryOptions1.add("name","Peter");
			Thing myCountry1 = graph.getThing( ":myCountry");
			Resource result;
		    result = myCountry1.getFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions1);
			assertEquals("42", result.stringValue());
			CustomQueryOptions  customQueryOptions2 = new CustomQueryOptions();
			customQueryOptions2.add("time",43);
			result = myCountry1.getFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions2 );
			assertEquals("43", result.stringValue());
			result = myCountry.getFact(":myOption&time='2019'^^xsd:int" );
			assertEquals("2019", result.stringValue());
			ResourceResults results = myCountry.getFacts("<http://inova8.com/calc2graph/def/myOption>&time='2020'^^xsd:int" );
			for (Resource result1:results ) {
				assertEquals("2020", result1.stringValue());
			}
			
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
			source.removeGraph("<http://inova8.com/calc2graph/contextGraph>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/contextGraph>");
			Thing myCountry = graph.getThing(":myCountry");
			myCountry.addFact(":myOption", "$customQueryOptions.get(\"time\").integerValue() ;", Evaluator.GROOVY);
			CustomQueryOptions  customQueryOptions1 = new CustomQueryOptions();
			customQueryOptions1.add("time",42);
		    customQueryOptions1.add("name","Peter");
			Thing myCountry1 = graph.getThing( ":myCountry");
			String result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions1);
			Query.assertEqualsWOSpaces("<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\n"
					+ "</li>\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\n"
					+ "</li>\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\n"
					+ "</ol>", result);
			CustomQueryOptions  customQueryOptions2 = new CustomQueryOptions();
			customQueryOptions2.add("time",43);
			result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>",customQueryOptions2 );
			Query.assertEqualsWOSpaces("<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol>", result);
			result = myCountry1.traceFact("<http://inova8.com/calc2graph/def/myOption>&time='2019'^^xsd:int" );
			Query.assertEqualsWOSpaces("<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 42^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 43^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  2019^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 2019^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a>, by invoking <b>groovy</b> script\r\n"
					+ "</li>\r\n"
					+ "<li><div  style='border: 1px solid black;'> <pre><code >$customQueryOptions.get(&quot;time&quot;).integerValue() ;</code></pre></div></li>\r\n"
					+ "<ol style='list-style-type:none;'></ol><li>Evaluated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> =  2019^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol><li>Calculated <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 2019^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "<li>Retrieved literal <a href='http://inova8.com/calc2graph/def/myOption' target='_blank'>myOption</a> of <a href='http://inova8.com/calc2graph/def/myCountry' target='_blank'>myCountry</a> = 2019^^<a href='http://www.w3.org/2001/XMLSchema#int' target='_blank'>int</a></li>\r\n"
					+ "</ol>", result);
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
