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
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import pathCalc.Source;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternVisitor;
import pathPatternProcessor.PathProcessor;
import pathPatternProcessor.Thing;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternSPARQLTests {
	static Thing thing;
	static Source source;
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source= new Source();
		Source.addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		Source.addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		Source.addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/");
		thing = new Thing(source, null, null, null);
		indices.add(0, 0);
	}

	@BeforeEach
	void setUp() throws Exception {
	}
	private PathElement prepareElement(CharStream input) throws RecognitionException {
		PathPatternLexer lexer = new PathPatternLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		PathPatternParser parser = new PathPatternParser(tokens);
		PathPatternContext pathPatternTree = parser.pathPattern();
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
		PathElement element = pathPatternVisitor.visit(pathPatternTree);
		indices.set(0, 0);
		element.buildIndices(indices, null);
		return element;
	}	
	@Test
	@Order(0)
	void test_05() {
		CharStream input = CharStreams.fromString( ":parent1/:parent2/:parent3");
		PathElement element = prepareElement(input);
		assertEquals ("?n0 <http://default/parent1> ?n1 .\n"
				+ "?n1 <http://default/parent2> ?n2 .\n"
				+ "?n2 <http://default/parent3> ?n3 .\n"
				+ "" , element.toSPARQL());
	}


	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent1[:gender :female]/:parent2[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent3");
		PathElement element = prepareElement(input);
		assertEquals ("?n0 <http://default/parent1> ?n1 .\n"
				+ "?n1 <http://default/gender> <http://default/female> .\n"
				+ "?n1 <http://default/parent2> ?n2 .\n"
				+ "?n2 <http://default/gender> <http://default/male> .\n"
				+ "?n2 <http://default/birthplace> ?n2_0 .\n"
				+ "?n2_0 <http://rdfs/label> 'Maidstone' .\n"
				+ "?n2 <http://default/parent3> ?n3 .\n"
				+ ""
				 , element.toSPARQL());
	}

	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( "^:Attribute@:volumeFlow");
		PathElement element = prepareElement(input);
		assertEquals ("{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n0 }UNION{ ?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?r1 }}\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/volumeFlow> }UNION{ <http://default/volumeFlow> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?r1 }}\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n1 }UNION{ ?n1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?r1 }}\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(2)
	void test_2() {
		CharStream input = CharStreams.fromString( "<http://local#volumeFlow>");
		PathElement element = prepareElement(input);
		assertEquals ("?n0 <http://local#volumeFlow> ?n1 .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(3)
	void test_3() {
		CharStream input = CharStreams.fromString( "^:hasProductBatteryLimit/:massThroughput");
		PathElement element = prepareElement(input);
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "" ,((PathElement)element).toSPARQL());
	}
	@Test
	@Order(4)
	void test_4() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("?n0 <http://default/volumeFlow> ?n1 .\n"
				+ "FILTER(?n1 gt '35')\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(5)
	void test_5() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\n"
				+ "?n1 <http://rdfs/label> 'eastman3d' .\n"
				+ "?r1 <http://default/lat> ?n2 .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(6)
	void test_6() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathElement element = prepareElement(input);
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\n"
				+ "?n1 <http://rdfs/label> 'Calc2Graph1' .\n"
				+ "?n2 <http://default/lat> ?r1 .\n"
				+ "?n2 <http://default/long> ?n3 .\n"
				+ "?n4 <http://default/left> ?n3 .\n"
				+ "?n4 <http://default/right> ?n5 .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(7)
	void test_7() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph2\"] , :Calc2Graph3 ,\"Calc2Graph4\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("?n0 <http://default/volumeFlow> <http://default/Calc2Graph3> .\n"
				+ "FILTER(<http://default/Calc2Graph3> gt '35')\n"
				+ "<http://default/Calc2Graph3> <http://rdfs/label> 'Calc2Graph1' .\n"
				+ "<http://default/Calc2Graph3> <http://rdfs/label> 'Calc2Graph2' .\n"
				+ "?n0 <http://default/volumeFlow> 'Calc2Graph4' .\n"
				+ "FILTER('Calc2Graph4' gt '35')\n"
				+ "'Calc2Graph4' <http://rdfs/label> 'Calc2Graph1' .\n"
				+ "'Calc2Graph4' <http://rdfs/label> 'Calc2Graph2' .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(8)
	void test_8() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\n"
				+ "?n1 <http://rdfs/label> 'eastman3d' .\n"
				+ "?r1 <http://rdftype> <http://default/Location> .\n"
				+ "?r1 <http://default/lat> ?n2 .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(9)
	void test_9() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		PathElement element = prepareElement(input);
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\n"
				+ "?r1 <http://default/location.Map> <http://id/Calc2Graph2> .\n"
				+ "?r1 <http://default/long> ?n2 .\n"
				+ "" , element.toSPARQL());
	}
	@Test
	@Order(10)
	void test_10() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
				+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1\n"
				+ "?n1 <http://rdfs/label> 'Calc2Graph1' .\n"
				+ "?r1 <http://default/lat> ?n2 .\n"
				+ "" , element.toSPARQL());
	}
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
			PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
			PathElement element = pathPatternVisitor.visit(pathPatternTree); 
			indices.set(0, 0);
			element.buildIndices(indices, null);
			assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph2>\n"
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
//			PathElement element = PathProcessor2.parsePathPattern(thing, ":Location@:appearsOn][eq id:Calc2Graph2]#");
//		}catch(Exception e){
//			assertEquals ("[line 1:20 in \":Location@:appearsOn][eq id:Calc2Graph2]#\": mismatched input ']' expecting {<EOF>, '|', '/', '{', '[', '#'}]"
//					,e.getMessage() );
//		}
//	}
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathProcessor.parsePathPattern(thing, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			indices.set(0, 0);
			element.buildIndices(indices, null);
			assertEquals ("?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/appearsOn>\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph1>\n"
					+ "?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> <http://id/Calc2Graph2>\n"
					+ "" , element.toSPARQL());
		}catch(Exception e){
			fail();
		}
	}

@Test 
@Order(14)
void test_14() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit{1, 42}/:massThroughput");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "#,42}\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(15)
void test_15() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit{1,}/:massThroughput");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		indices.set(0, 0);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "#,*}\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(16)
void test_16() {
	try {
		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "#,2}\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(17)
void test_17() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "#,2}\n"
				+ "?n2 <http://default/massThroughput> ?n3 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(18)
void test_18() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "*");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("?n0 ?p0 ?n1 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}@Test 
@Order(19)
void test_19() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "#,2}\n"
				+ "?n2 ?p2 ?n3 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(20)
void test_20() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 ?p1 ?n2 .\n"
				+ "#,2}\n"
				+ "?n2 <http://default/massThroughput> ?n3 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(21)
void test_21() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("#{1\n"
				+ "?n0 ?p0 ?n1 .\n"
				+ "#,2}\n"
				+ "?n1 <http://default/massThroughput> ?n2 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(22)
void test_22() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/*");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "?n1 ?p1 ?n2 .\n"
				+ "" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(23)
void test_23() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow)");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "{{?n1 <http://default/massFlow> ?n2 .\n"
				+ "}UNION{\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(24)
void test_24() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow  |:density)");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\n"
				+ "}UNION{\n"
				+ "?n1 <http://default/volumeFlow> ?n2 .\n"
				+ "}}}UNION{\n"
				+ "?n1 <http://default/density> ?n2 .\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(25)
void test_25() {
	try {
		
		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("?n1 <http://default/hasProductBatteryLimit> ?n0 .\n"
				+ "{{?n1 <http://default/temp> ?n2 .\n"
				+ "}UNION{\n"
				+ "{{{{?n1 <http://default/massFlow> ?n2 .\n"
				+ "}UNION{\n"
				+ "?n1 ?p1 ?n2. FILTER(?p1!=<http://default/volumeFlow>)\n"
				+ "}}}UNION{\n"
				+ "?n1 ?p1 ?n2. FILTER(?p1!=<http://default/density>)\n"
				+ "}}}}" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(26)
void test_26() {
	try {
		
		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("^<http://default/hasProductBatteryLimit> / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(27)
void test_27() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("(* | !^<http://default/hasProductBatteryLimit>) / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(28)
void test_28() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(:Attribute@:density  |:density)");
		indices.set(0, 0);
		element.buildIndices(indices, null);
		assertEquals ("{{{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?n0 }UNION{ ?n0 <http://www.w3.org/1999/02/22-rdf-syntax-ns#subject> ?r1 }}\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> <http://default/density> }UNION{ <http://default/density> <http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate> ?r1 }}\n"
				+ "{{?r1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?n1 }UNION{ ?n1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#object> ?r1 }}\n"
				+ "}UNION{\n"
				+ "?n0 <http://default/density> ?n1 .\n"
				+ "}}" , element.toSPARQL());
	}catch(Exception e){
		fail();
	}
}
}
