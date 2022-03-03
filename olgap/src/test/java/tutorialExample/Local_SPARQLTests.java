/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.intelligentgraph.vocabulary.XSD;

import utilities.Query;
import org.eclipse.rdf4j.query.Update;

/**
 * The Class Local_SPARQLTests.
 */
public class Local_SPARQLTests {
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			Repository workingRep = Query
					.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_SPARQLTests1/");

			RepositoryConnection conn = workingRep.getConnection();
			conn.setNamespace("", "http://inova8.com/intelligentgraph/example1/");
			conn.setNamespace(XSD.PREFIX, XSD.NAMESPACE);
			conn.setNamespace( RDF.PREFIX, RDF.NAMESPACE);
			conn.setNamespace(RDFS.PREFIX , RDFS.NAMESPACE);
			conn.setNamespace(OWL.PREFIX, OWL.NAMESPACE);
			
			Update updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
					"PREFIX :<http://inova8.com/intelligentgraph/example1/> "
					+ "PREFIX script:<http://inova8.com/script/> "
					+ "INSERT DATA {GRAPH <http://inova8.com/intelligentgraph/example1/> "
					+ "{ :aPerson rdf:type :Person ; :hasHeight '1.7'^^xsd:double ;:hasWeight '62'^^xsd:double }"
					+ "}");
			updateQuery.execute();
			
			updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
					"PREFIX :<http://inova8.com/intelligentgraph/example1/> "
					+ "PREFIX script:<http://inova8.com/script/> "
					+ "INSERT DATA {GRAPH <http://inova8.com/intelligentgraph/example1/> "
					+ "{ :aPerson :hasBMI   \"height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)\"^^script:groovy }"
					+ "}");
			updateQuery.execute();
			
			assertEquals("Height=1.7;BMI_TRACE=<ol style='list-style-type:none;'><ol style='list-style-type:none;'><li>Evaluating predicate <a href='http://inova8.com/intelligentgraph/example1/hasBMI' target='_blank'>hasBMI</a> of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a>, by invoking <b>groovy</b> script\n"
					+ "</li></li><li><div  style='border: 1px solid black;'> <pre><code >height=_this.getFact(':hasHeight').doubleValue(); _this.getFact(':hasWeight').doubleValue()/(height*height)</code></pre></div></li><ol style='list-style-type:none;'><li>Getting facts ':hasHeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> </li></li><li>Next fact 'http://inova8.com/intelligentgraph/example1/hasHeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> = 1.7</li></li><li>Returned fact ':hasHeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> = 1.7^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li><p><li>Getting facts ':hasWeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> </li></li><li>Next fact 'http://inova8.com/intelligentgraph/example1/hasWeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> = 62</li></li><li>Returned fact ':hasWeight' of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> = 62^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li><p></ol><li>Evaluated <a href='http://inova8.com/intelligentgraph/example1/hasBMI' target='_blank'>hasBMI</a> of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> =  21.453287197231838^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li></ol><li>Calculated <a href='http://inova8.com/intelligentgraph/example1/hasBMI' target='_blank'>hasBMI</a> of <a href='http://inova8.com/intelligentgraph/example1/aPerson' target='_blank'>aPerson</a> = 21.453287197231838^^<a href='http://www.w3.org/2001/XMLSchema#double' target='_blank'>double</a></li></li>;Person=http://inova8.com/intelligentgraph/example1/aPerson;Weight=62;BMI=21.453287197231838;\r\n",
					Query.runQuery(conn, "PREFIX :<http://inova8.com/intelligentgraph/example1/>\r\n "
					+ "Select ?Person ?Height ?Weight ?BMI ?BMI_TRACE \r\n"
					+ "{\r\n"
					+ "	?Person	:hasHeight ?Height ;\r\n"
					+ "			:hasWeight ?Weight ;\r\n"
					+ "			:hasBMI ?BMI,?BMI_TRACE .\r\n"
					+ "}\r\n"
					+ ""));

			assertEquals("Height=1.7;Person=http://inova8.com/intelligentgraph/example1/aPerson;Weight=62;BMI=21.453287197231838;\r\n"
					+ "",
					Query.runQuery(conn, "PREFIX :<http://inova8.com/intelligentgraph/example1/>\r\n "
					+ "Select ?Person ?Height ?Weight ?BMI \r\n"
					+ "{\r\n"
					+ "	?Person	:hasHeight ?Height ;\r\n"
					+ "			:hasWeight ?Weight ;\r\n"
					+ "			:hasBMI ?BMI .\r\n"
					+ "}\r\n"
					+ ""));


		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
}
