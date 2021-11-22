/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathErrorListener;
import com.inova8.pathql.processor.PathPatternVisitor;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;

/**
 * The Class PathPatternParserTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternTests {
	
	/** The source. */
	static IntelligentGraphRepository source;
	
	/** The thing. */
	static Thing thing;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source = new IntelligentGraphRepository();
		thing = source.getThing( "http://",null);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");

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
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
		PathElement element = pathPatternVisitor.visit(pathPatternTree);
		return element;
	}
	
	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> Maidstone ] ] / <http://default/parent>"
				 , element.toString());
	}

	/**
	 * Test 01.
	 */
	@Test
	@Order(0)
	void test_01() {
		CharStream input = CharStreams.fromString( "[eq :Peter]/:parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidston']]/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("[eq <http://default/Peter> ] / <http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> Maidston ] ] / <http://default/parent>"
				 , element.toString());
	}
	
	/**
	 * Test 02.
	 */
	@Test
	@Order(0)
	void test_02() {
		CharStream input = CharStreams.fromString( "[like 'Peter']/:parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidston']]/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("[like Peter ] / <http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> Maidston ] ] / <http://default/parent>"
				 , element.toString());
	}
	
	/**
	 * Test 03.
	 */
	@Test
	@Order(0)
	void test_03() {
		CharStream input = CharStreams.fromString( "[like [query 'Peter'; property :label]]/:parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidston']]/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("[like [query Peter ;property <http://default/label> ] ] / <http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> Maidston ] ] / <http://default/parent>"
				 , element.toString());
	}
	
	/**
	 * Test 05.
	 */
	@Test
	@Order(0)
	void test_05() {
		CharStream input = CharStreams.fromString( ":parent/:parent/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent> / <http://default/parent> / <http://default/parent>" , element.toString());
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( "^:Attribute@:volumeFlow");
		PathElement element = prepareElement(input);
		assertEquals ("^<http://default/Attribute>@<http://default/volumeFlow>" , element.toString());
	}
	
	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		CharStream input = CharStreams.fromString( "<http://local#volumeFlow>");
		PathElement element = prepareElement(input);
		assertEquals ("<http://local#volumeFlow>" , element.toString());
	}
	
	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {
		CharStream input = CharStreams.fromString( "^:hasProductBatteryLimit/:massThroughput");
		PathElement element = prepareElement(input);
		assertEquals ("^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>" ,((PathElement)element).toString());
	}
	
	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/volumeFlow>[gt 35 ]" , element.toString());
	}
	
	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[<http://rdfs/label> eastman3d ]# / <http://default/lat>" , element.toString());
	}
	
	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq [<http://rdfs/label> Calc2Graph1 ] ]# / ^<http://default/lat> / <http://default/long> / ^<http://default/left> / <http://default/right>" , element.toString());
	}
	
	/**
	 * Test 7.
	 */
	@Test
	@Order(7)
	void test_7() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph1\"] , :Calc2Graph1 ,\"Calc2Graph1\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/volumeFlow>[gt 35 ;<http://rdfs/label> Calc2Graph1 ;eq ([<http://rdfs/label> Calc2Graph1 ] , <http://default/Calc2Graph1>  , Calc2Graph1 ) ]" , element.toString());
	}
	
	/**
	 * Test 8.
	 */
	@Test
	@Order(8)
	void test_8() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[<http://rdfs/label> eastman3d ]#[<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://default/Location> ] / <http://default/lat>" , element.toString());
	}
	
	/**
	 * Test 9.
	 */
	@Test
	@Order(9)
	void test_9() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>#[<http://default/location.Map> <http://id/Calc2Graph2> ] / <http://default/long>" , element.toString());
	}
	
	/**
	 * Test 10.
	 */
	@Test
	@Order(10)
	void test_10() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq [<http://rdfs/label> Calc2Graph1 ] ]# / <http://default/lat>" , element.toString());
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
			PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(thing);
			PathElement element = pathPatternVisitor.visit(pathPatternTree); 
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ]#" , element.toString());
		}catch(Exception e){
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ;]#","" );
		}
	}
	
	/**
	 * Test 12.
	 */
	@Test 
	@Order(12)
	void test_12() {
		try {

			PathParser.parsePathPattern(thing, ":Location@:appearsOn][eq id:Calc2Graph2]#");
		}catch(Exception e){
			assertEquals ("[line 1:20 in \":Location@:appearsOn][eq id:Calc2Graph2]#\": mismatched input ']' expecting {<EOF>, '/', '|', '>', KEY}]"
					,e.getMessage() );
		}
	}
	
	/**
	 * Test 13.
	 */
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq (<http://id/Calc2Graph1> , <http://id/Calc2Graph2> ) ]#" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit{1, 42}/:massThroughput");
		assertEquals ("^<http://default/hasProductBatteryLimit>{1,42} / <http://default/massThroughput>" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit{1,}/:massThroughput");
		assertEquals ("^<http://default/hasProductBatteryLimit>{1,20} / <http://default/massThroughput>" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2}" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2} / <http://default/massThroughput>" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2} / *" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / *){1,2} / <http://default/massThroughput>" , element.toString());
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
		
		new PathParser();
		PathElement element = PathParser.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		assertEquals ("*{1,2} / <http://default/massThroughput>" , element.toString());
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
		PathElement element = PathParser.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		assertEquals ("*{1,2} / <http://default/massThroughput>" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/*");
		assertEquals ("^<http://default/hasProductBatteryLimit> / *" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow)");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (<http://default/massFlow> | <http://default/volumeFlow>)" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow  |:density)");
		assertEquals ("^<http://default/hasProductBatteryLimit> / ((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>)" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (<http://default/temp> | ((<http://default/massFlow> | !<http://default/volumeFlow>) | !<http://default/density>))" , element.toString());
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

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toString());
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
		
		PathElement element = PathParser.parsePathPattern(thing, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("(* | !^<http://default/hasProductBatteryLimit>) / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(28)
void test_28() {
	try {
		
		PathElement element = PathParser.parsePathPattern(thing, "^:type[:hasHeight [gt %1; lt %2]]/:bmi");
		assertEquals ("^<http://default/type>[<http://default/hasHeight> [gt %1 ;lt %2 ] ] / <http://default/bmi>" , element.toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(29)
void test_29() {
	try {
		
		PathElement element = PathParser.parsePathPattern(thing, "^:type[:hasLocation :Tideswell  ; :hasGender :Male ]/:hasBMI");
		assertEquals ("^<http://default/type>[<http://default/hasLocation> <http://default/Tideswell> ;<http://default/hasGender> <http://default/Male> ] / <http://default/hasBMI>" , element.toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test
@Order(30)
void test_30() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:measurementOf[:hasOrdinal  %1]/:hasBMI");
		//Query.assertEqualsWOSpaces 
				assertEquals
				 ("^<http://default/measurementOf>[<http://default/hasOrdinal> %1 ] / <http://default/hasBMI>" ,element.toString());
	}catch(Exception e){
		assertEquals("",e.getMessage());
	}
}
}
