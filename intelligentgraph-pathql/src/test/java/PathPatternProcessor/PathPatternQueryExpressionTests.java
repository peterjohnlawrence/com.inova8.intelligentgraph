/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.constants.PathConstants;
import com.inova8.intelligentgraph.repositoryContext.Prefixes;
import com.inova8.intelligentgraph.repositoryContext.Reifications;
import com.inova8.intelligentgraph.repositoryContext.RepositoryContext;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathErrorListener;
import com.inova8.pathql.processor.PathPatternVisitor;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;

import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class PathPatternQueryExpressionTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternQueryExpressionTests {
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
		repositoryContext.getPrefixes().prefix("http://default/").prefix("local","http://local/").prefix("rdf","http://rdf/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");

		indices.add(0, 0);
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
	 * Prepare element.
	 *
	 * @param input the input
	 * @return the path element
	 * @throws RecognitionException the recognition exception
	 */
	private PathElement prepareElement(CharStream input) throws RecognitionException {
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathPatternContext pathPatternTree = parser.pathPattern();
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(repositoryContext);
		PathElement element = pathPatternVisitor.visit(pathPatternTree);
		element.indexVisitor(null, 0,null);
		element.setIterations(Iterations.create(element));
		return element;
	}	

	@Test
	@Order(0)
	void test_05() {
		CharStream input = CharStreams.fromString( ":parent1/:parent2/:parent3");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n0)\r\n"
						+ "         Variable (name=p_n0_n1, value=http://default/parent1)\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2, value=http://default/parent2)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n2)\r\n"
						+ "      Variable (name=p_n2_n3, value=http://default/parent3)\r\n"
						+ "      Variable (name=n3)\r\n"
						+ "" , element.pathPatternQuery().toString());
	}
	@Test
	@Order(0)
	void test_06() {
		CharStream input = CharStreams.fromString( ":parent1[ :birthplace [rdfs:label 'Maidstone']]");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "      Variable (name=p_n0_n1, value=http://default/parent1)\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n1_1, value=http://default/birthplace)\r\n"
						+ "         Variable (name=n1_1)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1_1)\r\n"
						+ "         Variable (name=p_n1_1_n1_1_1, value=http://rdfs/label)\r\n"
						+ "         Variable (name=n1_1_1, value=\"Maidstone\")\r\n"
						+ ""
				 , element.pathPatternQuery().toString());
	}
	@Test
	@Order(0)
	void test_07() {
		CharStream input = CharStreams.fromString( ":parent1[:gender :female; :birthplace [rdfs:label 'Maidstone']]");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n0)\r\n"
						+ "         Variable (name=p_n0_n1, value=http://default/parent1)\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n1_1, value=http://default/gender)\r\n"
						+ "         Variable (name=n1_1, value=http://default/female)\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n1_2, value=http://default/birthplace)\r\n"
						+ "         Variable (name=n1_2)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1_2)\r\n"
						+ "         Variable (name=p_n1_1_n1_2_1, value=http://rdfs/label)\r\n"
						+ "         Variable (name=n1_2_1, value=\"Maidstone\")\r\n"
						+ ""
				 , element.pathPatternQuery().toString());
	}
	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent1[:gender :female; :birthplace [rdfs:label 'Maidstone']]/:parent2[:gender :male]/:parent3");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      Join\r\n"
						+ "         Join\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n0)\r\n"
						+ "               Variable (name=p_n0_n1, value=http://default/parent1)\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n1_1, value=http://default/gender)\r\n"
						+ "               Variable (name=n1_1, value=http://default/female)\r\n"
						+ "         Join\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n1_2, value=http://default/birthplace)\r\n"
						+ "               Variable (name=n1_2)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1_2_1)\r\n"
						+ "               Variable (name=p_n1_2_1_n1_2_2, value=http://rdfs/label)\r\n"
						+ "               Variable (name=n1_2_2, value=\"Maidstone\")\r\n"
						+ "      Join\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p_n1_n2, value=http://default/parent2)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "            Variable (name=p_n2_n2_1, value=http://default/gender)\r\n"
						+ "            Variable (name=n2_1, value=http://default/male)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n2)\r\n"
						+ "      Variable (name=p_n2_n3, value=http://default/parent3)\r\n"
						+ "      Variable (name=n3)\r\n"
						+ ""
				 , element.pathPatternQuery().toString());
	}

	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( "^:Attribute@:volumeFlow");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      Union\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "            Variable (name=n0)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n0)\r\n"
				 		+ "            Variable (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "      Union\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "            Variable (name=p_n0_n1, value=http://default/volumeFlow)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=p_n0_n1, value=http://default/volumeFlow)\r\n"
				 		+ "            Variable (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "   Union\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=r1)\r\n"
				 		+ "         Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "         Variable (name=r1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		CharStream input = CharStreams.fromString( "<http://local#volumeFlow>");
		PathElement element = prepareElement(input);
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1, value=http://local#volumeFlow)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {
		CharStream input = CharStreams.fromString( "^:hasProductBatteryLimit>:massThroughput");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
						+ "      Variable (name=n2)\r\n"
						+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ]");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Filter\r\n"
				 		+ "   Compare (>)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      ValueConstant (value=\"35\")\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/volumeFlow)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat"); //
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      Join\r\n"
				 		+ "         Join\r\n"
				 		+ "            StatementPattern\r\n"
				 		+ "               Variable (name=r1)\r\n"
				 		+ "               Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "               Variable (name=n0)\r\n"
				 		+ "            StatementPattern\r\n"
				 		+ "               Variable (name=r1)\r\n"
				 		+ "               Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "               Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n1_1, value=http://rdfs/label)\r\n"
				 		+ "         Variable (name=n1_1, value=\"eastman3d\")\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=r1)\r\n"
				 		+ "      Variable (name=p_r1_n2, value=http://default/lat)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathElement element = prepareElement(input);
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      Join\r\n"
						+ "         Join\r\n"
						+ "            Join\r\n"
						+ "               Join\r\n"
						+ "                  StatementPattern\r\n"
						+ "                     Variable (name=r1)\r\n"
						+ "                     Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
						+ "                     Variable (name=n0)\r\n"
						+ "                  StatementPattern\r\n"
						+ "                     Variable (name=r1)\r\n"
						+ "                     Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
						+ "                     Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
						+ "               StatementPattern\r\n"
						+ "                  Variable (name=r1)\r\n"
						+ "                  Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
						+ "                  Variable (name=n1)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "               Variable (name=p_r1_n2, value=http://default/lat)\r\n"
						+ "               Variable (name=r1)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "            Variable (name=p_n2_n3, value=http://default/long)\r\n"
						+ "            Variable (name=n3)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n4)\r\n"
						+ "         Variable (name=p_n3_n4, value=http://default/left)\r\n"
						+ "         Variable (name=n3)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n4)\r\n"
						+ "      Variable (name=p_n4_n5, value=http://default/right)\r\n"
						+ "      Variable (name=n5)\r\n"
						+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ eq \"36\" ; gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph2\"] , :Calc2Graph3 ,\"Calc2Graph4\" ]");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Filter\r\n"
				 		+ "      Compare (>)\r\n"
				 		+ "         Variable (name=n1, value=\"36\")\r\n"
				 		+ "         ValueConstant (value=\"35\")\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n0)\r\n"
				 		+ "         Variable (name=p_n0_n1, value=http://default/volumeFlow)\r\n"
				 		+ "         Variable (name=n1, value=\"36\")\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1, value=\"36\")\r\n"
				 		+ "      Variable (name=p_n1_n1_1, value=http://rdfs/label)\r\n"
				 		+ "      Variable (name=n1_1, value=\"Calc2Graph1\")\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      Join\r\n"
				 		+ "         Join\r\n"
				 		+ "            Join\r\n"
				 		+ "               StatementPattern\r\n"
				 		+ "                  Variable (name=r1)\r\n"
				 		+ "                  Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "                  Variable (name=n0)\r\n"
				 		+ "               StatementPattern\r\n"
				 		+ "                  Variable (name=r1)\r\n"
				 		+ "                  Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "                  Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
				 		+ "            StatementPattern\r\n"
				 		+ "               Variable (name=r1)\r\n"
				 		+ "               Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "               Variable (name=n1)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "            Variable (name=p_n1_n1_1, value=http://rdfs/label)\r\n"
				 		+ "            Variable (name=n1_1, value=\"eastman3d\")\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=r1)\r\n"
				 		+ "         Variable (name=p_r1_n0_1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type)\r\n"
				 		+ "         Variable (name=n0_1, value=http://default/Location)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=r1)\r\n"
				 		+ "      Variable (name=p_r1_n2, value=http://default/lat)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 9.
	 */
	@Test
	@Order(9)
	void test_9() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      Join\r\n"
				 		+ "         Join\r\n"
				 		+ "            StatementPattern\r\n"
				 		+ "               Variable (name=r1)\r\n"
				 		+ "               Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "               Variable (name=n0)\r\n"
				 		+ "            StatementPattern\r\n"
				 		+ "               Variable (name=r1)\r\n"
				 		+ "               Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "               Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=r1)\r\n"
				 		+ "         Variable (name=p_r1_n0_1, value=http://default/location.Map)\r\n"
				 		+ "         Variable (name=n0_1, value=http://id/Calc2Graph2)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=r1)\r\n"
				 		+ "      Variable (name=p_r1_n2, value=http://default/long)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		PathElement element = prepareElement(input);
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      Join\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				 		+ "            Variable (name=n0)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=r1)\r\n"
				 		+ "            Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				 		+ "            Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=r1)\r\n"
				 		+ "         Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=r1)\r\n"
				 		+ "      Variable (name=p_r1_n2, value=http://default/lat)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}
	
	/**
	 * Test 11.
	 */
	@Test 
	@Order(11)
	void test_11() {
		try {
			String expression = ":Location@:appearsOn[eq id:Calc2Graph2]#";
			CharStream input = CharStreams.fromString(expression);
			PathPatternLexer lexer = new PathPatternLexer(input);
			 PathErrorListener errorListener = new PathErrorListener(expression);
			lexer.removeErrorListeners(); 
			lexer.addErrorListener(errorListener); 
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			PathPatternParser parser = new PathPatternParser(tokens);
			parser.removeErrorListeners(); 
			parser.addErrorListener(errorListener); 
			PathPatternContext pathPatternTree = parser.pathPattern();
			PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(repositoryContext);
			PathElement element = pathPatternVisitor.visit(pathPatternTree); 
			assertEquals
			 ("Join\r\n"
			 		+ "   Join\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=rnull)\r\n"
			 		+ "         Variable (name=subjectnull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
			 		+ "         Variable (name=nnull)\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=rnull)\r\n"
			 		+ "         Variable (name=propertynull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
			 		+ "         Variable (name=p_nnull_nnull, value=http://default/appearsOn)\r\n"
			 		+ "   StatementPattern\r\n"
			 		+ "      Variable (name=rnull)\r\n"
			 		+ "      Variable (name=objectnull, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
			 		+ "      Variable (name=nnull, value=http://id/Calc2Graph2)\r\n"
			 		+ "" ,element.pathPatternQuery().toString());
		}catch(Exception e){
			Query.assertEqualsWOSpaces ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ;]#","" );
		}
	}
	@Test 
	@Order(12)
	void test_12() {
		try {
			
			PathElement element = PathParser.parsePathPattern(repositoryContext, ":Location@:appearsOn[eq id:Calc2Graph1]#");
			assertEquals ("Join\r\n"
					+ "   Join\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
					+ "         Variable (name=n0)\r\n"
					+ "      StatementPattern\r\n"
					+ "         Variable (name=r1)\r\n"
					+ "         Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
					+ "         Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
					+ "   StatementPattern\r\n"
					+ "      Variable (name=r1)\r\n"
					+ "      Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
					+ "      Variable (name=n1, value=http://id/Calc2Graph1)\r\n"
					+ ""
					,element.pathPatternQuery().toString() );
		}catch(Exception e){
			assertEquals("", e.getMessage());
		}
		}
/**
 * Test 13.
 */
//	}
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathParser.parsePathPattern(repositoryContext, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			assertEquals
			 ("Join\r\n"
			 		+ "   Join\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=r1)\r\n"
			 		+ "         Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
			 		+ "         Variable (name=n0)\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=r1)\r\n"
			 		+ "         Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
			 		+ "         Variable (name=p_n0_n1, value=http://default/appearsOn)\r\n"
			 		+ "   Join\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=r1)\r\n"
			 		+ "         Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
			 		+ "         Variable (name=n1, value=http://id/Calc2Graph1)\r\n"
			 		+ "      StatementPattern\r\n"
			 		+ "         Variable (name=r1)\r\n"
			 		+ "         Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
			 		+ "         Variable (name=n1, value=http://id/Calc2Graph2)\r\n"
			 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "         Variable (name=n0)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n2)\r\n"
						+ "      Variable (name=p_n2_n3, value=http://default/massThroughput)\r\n"
						+ "      Variable (name=n3)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "         Variable (name=n0)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				 		+ "         Variable (name=n2)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "      Variable (name=p_n2_n3)\r\n"
				 		+ "      Variable (name=n3)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				("Join\r\n"
						+ "   Join\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "         Variable (name=n0)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n2)\r\n"
						+ "      Variable (name=p_n2_n3, value=http://default/massThroughput)\r\n"
						+ "      Variable (name=n3)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "      Variable (name=p_n0_n1)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "   Union\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n2_alt_L, value=http://default/massFlow)\r\n"
				 		+ "         Variable (name=n2)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n2_alt_LR, value=http://default/volumeFlow)\r\n"
				 		+ "         Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				("Join\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "   Union\r\n"
						+ "      Union\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p_n1_n2_alt_LL, value=http://default/massFlow)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p_n1_n2_alt_LLR, value=http://default/volumeFlow)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2_alt_LR, value=http://default/density)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
		/**************************************************Negation not yet supported*/
		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
				assertEquals
				("Join\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "   Union\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2_alt_L, value=http://default/temp)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "      Union\r\n"
						+ "         Union\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n2_alt_LRLL, value=http://default/massFlow)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p1_2)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p1_2)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
		/**************************************************Negation not yet supported*/
		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
				assertEquals
				("Join\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "   Union\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2_alt_L)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "      Union\r\n"
						+ "         Union\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n2_alt_LRLL, value=http://default/massFlow)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n2_alt_LRLLR, value=http://default/volumeFlow)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p_n1_n2_alt_LRLR, value=http://default/density)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
		/**************************************************Negation not yet supported*/
		PathElement element = PathParser.parsePathPattern(repositoryContext, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
				assertEquals
				("Join\r\n"
						+ "   Union\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n0)\r\n"
						+ "         Variable (name=p_n0_n1_alt_L)\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "      Filter\r\n"
						+ "         Compare (!=)\r\n"
						+ "            Variable (name=p0_1)\r\n"
						+ "            Variable (name=p2, value=http://default/hasProductBatteryLimit)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p0_1)\r\n"
						+ "            Variable (name=n0)\r\n"
						+ "   Union\r\n"
						+ "      StatementPattern\r\n"
						+ "         Variable (name=n1)\r\n"
						+ "         Variable (name=p_n1_n2_alt_L)\r\n"
						+ "         Variable (name=n2)\r\n"
						+ "      Union\r\n"
						+ "         Union\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n2_alt_LRLL, value=http://default/massFlow)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n1)\r\n"
						+ "               Variable (name=p_n1_n2_alt_LRLLR, value=http://default/volumeFlow)\r\n"
						+ "               Variable (name=n2)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=p_n1_n2_alt_LRLR, value=http://default/density)\r\n"
						+ "            Variable (name=n2)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				("Union\r\n"
						+ "   Join\r\n"
						+ "      Join\r\n"
						+ "         Union\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=r1)\r\n"
						+ "               Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
						+ "               Variable (name=n0)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=n0)\r\n"
						+ "               Variable (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
						+ "               Variable (name=r1)\r\n"
						+ "         Union\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=r1)\r\n"
						+ "               Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
						+ "               Variable (name=p_n0_n1_alt_L, value=http://default/density)\r\n"
						+ "            StatementPattern\r\n"
						+ "               Variable (name=p_n0_n1_alt_L, value=http://default/density)\r\n"
						+ "               Variable (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
						+ "               Variable (name=r1)\r\n"
						+ "      Union\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=r1)\r\n"
						+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "         StatementPattern\r\n"
						+ "            Variable (name=n1)\r\n"
						+ "            Variable (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
						+ "            Variable (name=r1)\r\n"
						+ "   StatementPattern\r\n"
						+ "      Variable (name=n0)\r\n"
						+ "      Variable (name=p_n0_n1_alt_LR, value=http://default/density)\r\n"
						+ "      Variable (name=n1)\r\n"
						+ "" ,element.pathPatternQuery().toString());
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
				assertEquals
				("StatementPattern\r\n"
						+ "   Variable (name=n0)\r\n"
						+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "   Variable (name=n1)\r\n"
						+ "" 
				,element.pathPatternQuery().toString());
		Query.assertEqualsWOSpaces ("BIND(<http://default/Unit1> as ?n0)\n"	+ "" , element.getLeftPathElement().toSPARQL());
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
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
		Query.assertEqualsWOSpaces ("?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\n"
				+ "" , element.getLeftPathElement().toSPARQL());
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
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
		Query.assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit1'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0]." , element.getLeftPathElement().toSPARQL());
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
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
		Query.assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit* NOT (location OR product*)'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0]." , element.getLeftPathElement().toSPARQL());
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
				assertEquals
				("StatementPattern\r\n"
						+ "   Variable (name=n0)\r\n"
						+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
						+ "   Variable (name=n1)\r\n"
						+ "" ,element.pathPatternQuery().toString());
		Query.assertEqualsWOSpaces ("?n0 <http://www.openrdf.org/contrib/lucenesail#matches> [<http://www.openrdf.org/contrib/lucenesail#query> 'Unit'; <http://www.openrdf.org/contrib/lucenesail#property> ?property_0;<http://www.openrdf.org/contrib/lucenesail#score> ?score_0;<http://www.openrdf.org/contrib/lucenesail#snippet> ?snippet_0].?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Unit> .\n"
				+ "" , element.getLeftPathElement().toSPARQL());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

/**
 * Test 34.
 */
@Test 
@Order(34)
void test_34() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":hasProductBatteryLimit");
				assertEquals
				 ("StatementPattern\r\n"
				 		+ "   Variable (name=n0)\r\n"
				 		+ "   Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "   Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}


@Test 
@Order(35)
void test_35() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":hasProductBatteryLimit[a  :BatteryLimit]");
				assertEquals
				 ("Join\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n1_1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#type)\r\n"
				 		+ "      Variable (name=n1_1, value=http://default/BatteryLimit)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(36)
void test_36() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "rdf:type[ lt '1.7' ]");
				assertEquals
				 ("Filter\r\n"
				 		+ "   Compare (<)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      ValueConstant (value=\"1.7\")\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://rdf/type)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
@Test 
@Order(37)
void test_37() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "rdf:type[:hasHeight  [ lt '1.7' ]]/:hasBMI");
				assertEquals
				 ("Join\r\n"
				 		+ "   Filter\r\n"
				 		+ "      Compare (<)\r\n"
				 		+ "         Variable (name=n1_1)\r\n"
				 		+ "         ValueConstant (value=\"1.7\")\r\n"
				 		+ "      Join\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n0)\r\n"
				 		+ "            Variable (name=p_n0_n1, value=http://rdf/type)\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "            Variable (name=p_n1_n1_1, value=http://default/hasHeight)\r\n"
				 		+ "            Variable (name=n1_1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/hasBMI)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
@Test 
@Order(38)
void test_38() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^rdf:type[ lt '1.7' ]");
				assertEquals
				 ("Filter\r\n"
				 		+ "   Compare (<)\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      ValueConstant (value=\"1.7\")\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n0_n1, value=http://rdf/type)\r\n"
				 		+ "      Variable (name=n0)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
@Test 
@Order(39)
void test_39() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^rdf:type[:hasHeight  [ lt '1.7' ]]/:hasBMI");
				assertEquals
				 ("Join\r\n"
				 		+ "   Filter\r\n"
				 		+ "      Compare (<)\r\n"
				 		+ "         Variable (name=n0_1)\r\n"
				 		+ "         ValueConstant (value=\"1.7\")\r\n"
				 		+ "      Join\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "            Variable (name=p_n0_n1, value=http://rdf/type)\r\n"
				 		+ "            Variable (name=n0)\r\n"
				 		+ "         StatementPattern\r\n"
				 		+ "            Variable (name=n1)\r\n"
				 		+ "            Variable (name=p_n1_n0_1, value=http://default/hasHeight)\r\n"
				 		+ "            Variable (name=n0_1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/hasBMI)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
@Test
@Order(40)
void test_40() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^rdf:type[:hasLocation :Tideswell  ; :hasGender :Male ]");
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n0_n1, value=http://rdf/type)\r\n"
				 		+ "         Variable (name=n0)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n0_1, value=http://default/hasLocation)\r\n"
				 		+ "         Variable (name=n0_1, value=http://default/Tideswell)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n0_1, value=http://default/hasGender)\r\n"
				 		+ "      Variable (name=n0_1, value=http://default/Male)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
@Test
@Order(41)
void test_41() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:measurementOf[:hasOrdinal  %1]/:hasBMI");
				assertEquals
				 ("Join\r\n"
				 		+ "   Join\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n0_n1, value=http://default/measurementOf)\r\n"
				 		+ "         Variable (name=n0)\r\n"
				 		+ "      StatementPattern\r\n"
				 		+ "         Variable (name=n1)\r\n"
				 		+ "         Variable (name=p_n1_n0_1, value=http://default/hasOrdinal)\r\n"
				 		+ "         Variable (name=n0_1)\r\n"
				 		+ "   StatementPattern\r\n"
				 		+ "      Variable (name=n1)\r\n"
				 		+ "      Variable (name=p_n1_n2, value=http://default/hasBMI)\r\n"
				 		+ "      Variable (name=n2)\r\n"
				 		+ "" ,element.pathPatternQuery().toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
}
