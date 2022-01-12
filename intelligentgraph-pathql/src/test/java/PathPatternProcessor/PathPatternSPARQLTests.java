/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.constants.PathConstants;
import com.inova8.intelligentgraph.exceptions.PathPatternException;
import com.inova8.intelligentgraph.repositoryContext.Prefixes;
import com.inova8.intelligentgraph.repositoryContext.Reifications;
import com.inova8.intelligentgraph.repositoryContext.RepositoryContext;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class PathPatternSPARQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternSPARQLTests {
	static RepositoryContext repositoryContext;	
	
	/** The indices. */
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		repositoryContext= new RepositoryContext(new Prefixes(),new Reifications());
		repositoryContext.getReifications().addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		repositoryContext.getReifications().addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		repositoryContext.getReifications().addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		repositoryContext.getReifications().setReificationsAreLazyLoaded(true);
		repositoryContext.getPrefixes().prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}
	
	/**
	 * Test 05.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(0)
	void test_05() throws RecognitionException, PathPatternException {
		PathElement element =  PathParser.parsePathPattern(repositoryContext,":parent1/:parent2/:parent3");
		assertEquals ("?n0 <http://default/parent1> ?n1 .\r\n"
				+ "?n1 <http://default/parent2> ?n2 .\r\n"
				+ "?n2 <http://default/parent3> ?n3 .\r\n"
				+ "" , element.toSPARQL());
	}


	/**
	 * Test 0.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(0)
	void test_0() throws RecognitionException, PathPatternException {
		PathElement element = PathParser.parsePathPattern(repositoryContext,":parent1[:gender :female]/:parent2[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent3");
		assertEquals ("?n0 <http://default/parent1> ?n1 .\r\n"
				+ "?n1 <http://default/gender> <http://default/female> .\r\n"
				+ "?n1 <http://default/parent2> ?n2 .\r\n"
				+ "?n2 <http://default/gender> <http://default/male> .\r\n"
				+ "?n2 <http://default/birthplace> ?n2_1 .\r\n"
				+ "?n2_1 <http://rdfs/label> 'Maidstone' .\r\n"
				+ "?n2 <http://default/parent3> ?n3 .\r\n"
				+ ""
				 , element.toSPARQL());
	}

	/**
	 * Test 1.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(1)
	void test_1() throws RecognitionException, PathPatternException {
		PathElement element = PathParser.parsePathPattern(repositoryContext,"^:Attribute@:volumeFlow");
		assertEquals ("{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n0 }UNION{ ?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?r1 }}\r\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/volumeFlow> }UNION{ <http://default/volumeFlow> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?r1 }}\r\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n1 }UNION{ ?n1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?r1 }}\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 2.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(2)
	void test_2()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext,"<http://local#volumeFlow>");
		assertEquals ("?n0 <http://local#volumeFlow> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 3.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(3)
	void test_3()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext,"^:hasProductBatteryLimit>:massThroughput");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "" ,((PathElement)element).toSPARQL());
	}
	
	/**
	 * Test 4.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(4)
	void test_4()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext, ":volumeFlow [ gt \"35\" ]");
		assertEquals ("?n0 <http://default/volumeFlow> ?n1 .\r\n"
				+ "FILTER(?n1 gt '35')\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 5.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(5)
	void test_5()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext,":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat");
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\r\n"
				+ "?n1 <http://rdfs/label> 'eastman3d' .\r\n"
				+ "?r1 <http://default/lat> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 6.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(6)
	void test_6()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext,":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\r\n"
				+ "?n1 <http://rdfs/label> 'Calc2Graph1' .\r\n"
				+ "?n2 <http://default/lat> ?r1 .\r\n"
				+ "?n2 <http://default/long> ?n3 .\r\n"
				+ "?n4 <http://default/left> ?n3 .\r\n"
				+ "?n4 <http://default/right> ?n5 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 7.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(7)
	void test_7()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext,":volumeFlow [ gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph2\"] , :Calc2Graph3 ,\"Calc2Graph4\" ]");
		assertEquals ("?n0 <http://default/volumeFlow> <http://default/Calc2Graph3> .\r\n"
				+ "FILTER(<http://default/Calc2Graph3> gt '35')\r\n"
				+ "<http://default/Calc2Graph3> <http://rdfs/label> 'Calc2Graph1' .\r\n"
				+ "<http://default/Calc2Graph3> <http://rdfs/label> 'Calc2Graph2' .\r\n"
				+ "?n0 <http://default/volumeFlow> 'Calc2Graph4' .\r\n"
				+ "FILTER('Calc2Graph4' gt '35')\r\n"
				+ "'Calc2Graph4' <http://rdfs/label> 'Calc2Graph1' .\r\n"
				+ "'Calc2Graph4' <http://rdfs/label> 'Calc2Graph2' .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 8.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(8)
	void test_8() throws RecognitionException, PathPatternException {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\r\n"
				+ "?n1 <http://rdfs/label> 'eastman3d' .\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Location> .\r\n"
				+ "?r1 <http://default/lat> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 9.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(9)
	void test_9()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(repositoryContext, ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\r\n"
				+ "?r1 <http://default/location.Map> <http://id/Calc2Graph2> .\r\n"
				+ "?r1 <http://default/long> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 10.
	 *
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	@Test
	@Order(10)
	void test_10()  throws RecognitionException, PathPatternException {

		PathElement element = PathParser.parsePathPattern(repositoryContext,":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\r\n"
				+ "?n1 <http://rdfs/label> 'Calc2Graph1' .\r\n"
				+ "?r1 <http://default/lat> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}
	
	/**
	 * Test 11.
	 */
	@Test 
	@Order(11)
	void test_11() {
		try {

			PathElement element = PathParser.parsePathPattern(repositoryContext,":Location@:appearsOn[eq id:Calc2Graph2]#");
			assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph2>\r\n"
					+ "BIND(<http://id/Calc2Graph2> as <http://id/Calc2Graph2>)\r\n"
					+ "" , element.toSPARQL());
		}catch(Exception e){
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ;]#","" );
		}
	}
//	@Test 
//	@Order(12)
//	void test_12() {
//		try {
//			
//			PathProcessor2 pathProcessor = new PathProcessor2();
//			PathElement element = PathProcessor2.parsePathPattern(repositoryContext, ":Location@:appearsOn][eq id:Calc2Graph2]#");
//		}catch(Exception e){
//			assertEquals ("[line 1:20 in \":Location@:appearsOn][eq id:Calc2Graph2]#\": mismatched input ']' expecting {<EOF>, '|', '/', '{', '[', '#'}]"
//					,e.getMessage() );
//		}
/**
 * Test 13.
 */
//	}
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathParser.parsePathPattern(repositoryContext, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\r\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\r\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph1>\r\n"
					+ "BIND(<http://id/Calc2Graph1> as <http://id/Calc2Graph1>)\r\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph2>\r\n"
					+ "BIND(<http://id/Calc2Graph1> as <http://id/Calc2Graph2>)\r\n"
					+ "" , element.toSPARQL());
		}catch(Exception e){
			assertEquals("", e.getMessage());
		}
	}

/**
 * Test 14.
 */
@Test 
@Order(14)
void test_14() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit{1, 42}/:massThroughput");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "#,42}\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 15.
 */
@Test 
@Order(15)
void test_15() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit{1,}/:massThroughput");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "#,20}\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 16.
 */
@Test 
@Order(16)
void test_16() {
	try {
		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "#,2}\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 17.
 */
@Test 
@Order(17)
void test_17() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "#,2}\r\n"
				+ "?n2 <http://default/massThroughput> ?n3 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 18.
 */
@Test 
@Order(18)
void test_18() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "*");
		assertEquals ("?n0 ?p0_1 ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
/**
 * Test 19.
 */
@Test 
@Order(19)
void test_19() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "#,2}\r\n"
				+ "?n2 ?p2_3 ?n3 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 20.
 */
@Test 
@Order(20)
void test_20() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		assertEquals ("#{1\r\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 ?p1_2 ?n2 .\r\n"
				+ "#,2}\r\n"
				+ "?n2 <http://default/massThroughput> ?n3 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 21.
 */
@Test 
@Order(21)
void test_21() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(*){1, 2}/:massThroughput");
		assertEquals ("#{1\r\n"
				+ "?n0 ?p0_1 ?n1 .\r\n"
				+ "#,2}\r\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 22.
 */
@Test 
@Order(22)
void test_22() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/*");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "?n1 ?p1_2 ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 23.
 */
@Test 
@Order(23)
void test_23() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow)");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "{{?n1 <http://default/massFlow> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\r\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 24.
 */
@Test 
@Order(24)
void test_24() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow  |:density)");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\r\n"
				+ "}}}UNION{\r\n"
				+ "?n1 <http://default/density> ?n2 .\r\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 25.
 */
@Test 
@Order(25)
void test_25() {
	try {
		
		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "{{?n1 <http://default/temp> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1 ?p1_2 ?n2. FILTER(?p1_2!=<http://default/volumeFlow>)\r\n"
				+ "}}}UNION{\r\n"
				+ "?n1 ?p1_2 ?n2. FILTER(?p1_2!=<http://default/density>)\r\n"
				+ "}}}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 26.
 */
@Test 
@Order(26)
void test_26() {
	try {
		
		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\r\n"
				+ "{{?n1 ?p1_2 ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\r\n"
				+ "}}}UNION{\r\n"
				+ "?n1 <http://default/density> ?n2 .\r\n"
				+ "}}}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 27.
 */
@Test 
@Order(27)
void test_27() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("{{?n0 ?p0_1 ?n1 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1?p0_1 ?n0. FILTER(?p0_1!=<http://default/hasProductBatteryLimit>).\r\n"
				+ "}}{{?n1 ?p1_2 ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\r\n"
				+ "}UNION{\r\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\r\n"
				+ "}}}UNION{\r\n"
				+ "?n1 <http://default/density> ?n2 .\r\n"
				+ "}}}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 28.
 */
@Test 
@Order(28)
void test_28() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(:Attribute@:density  |:density)");
		assertEquals ("{{{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0 }UNION{ ?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?r1 }}\r\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/density> }UNION{ <http://default/density> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?r1 }}\r\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1 }UNION{ ?n1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?r1 }}\r\n"
				+ "}UNION{\r\n"
				+ "?n0 <http://default/density> ?n1 .\r\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 29.
 */
@Test 
@Order(29)
void test_29() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "[ eq :Unit1]/:hasProductBatteryLimit");
		assertEquals ("BIND(<http://default/Unit1> as ?n0)\r\n"
				+ "?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 30.
 */
@Test 
@Order(30)
void test_30() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "[ a :Unit]/:hasProductBatteryLimit");
		assertEquals ("?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\r\n"
				+ "?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 31.
 */
@Test 
@Order(31)
void test_31() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "[ like \"Unit1\"]/:hasProductBatteryLimit");

		assertEquals ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit1'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0].?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 32.
 */
@Test 
@Order(32)
void test_32() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "[ like \"Unit* NOT (location OR product*)\"]/:hasProductBatteryLimit");
		assertEquals ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit* NOT (location OR product*)'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0].?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 33.
 */
@Test 
@Order(33)
void test_33() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "[ like \"Unit\" ; a :Unit]/:hasProductBatteryLimit");
		assertEquals ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0].?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\r\n"
				+ "?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}


@Test 
@Order(34)
void test_34() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":hasProductBatteryLimit");
		assertEquals ("?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}


@Test 
@Order(35)
void test_35() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":hasProductBatteryLimit[a  :BatteryLimit]");
		assertEquals ("?n0 <http://default/hasProductBatteryLimit> ?n1 .\r\n"
				+ "?n1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/BatteryLimit> .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(36)
void test_36() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:type[:hasLocation :Tideswell  ; :hasGender :Male ]/:hasBMI");
		assertEquals ("?n1 <http://default/type> ?n0 .\r\n"
				+ "?n1 <http://default/hasLocation> <http://default/Tideswell> .\r\n"
				+ "?n1 <http://default/hasGender> <http://default/Male> .\r\n"
				+ "?n1 <http://default/hasBMI> ?n2 .\r\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
}
