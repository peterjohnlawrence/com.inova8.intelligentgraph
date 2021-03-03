package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import pathCalc.Source;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternVisitor;
import pathPatternProcessor.PathProcessor;
import pathPatternProcessor.Thing;
import resources_Deprecated.PathProcessorOld;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternParserTests {
	static Source source;
	static Thing thing;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source = new Source();
		thing = new Thing(source, null, null, null);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/");

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
		return element;
	}
	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> 'Maidstone' ] ] / <http://default/parent>"
				 , element.toString());
	}


	@Test
	@Order(0)
	void test_05() {
		CharStream input = CharStreams.fromString( ":parent/:parent/:parent");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent> / <http://default/parent> / <http://default/parent>" , element.toString());
	}
	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( "^:Attribute@:volumeFlow");
		PathElement element = prepareElement(input);
		assertEquals ("^<http://default/Attribute>@<http://default/volumeFlow>" , element.toString());
	}
	@Test
	@Order(2)
	void test_2() {
		CharStream input = CharStreams.fromString( "<http://local#volumeFlow>");
		PathElement element = prepareElement(input);
		assertEquals ("<http://local#volumeFlow>" , element.toString());
	}
	@Test
	@Order(3)
	void test_3() {
		CharStream input = CharStreams.fromString( "^:hasProductBatteryLimit/:massThroughput");
		PathElement element = prepareElement(input);
		assertEquals ("^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>" ,((PathElement)element).toString());
	}
	@Test
	@Order(4)
	void test_4() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/volumeFlow>[gt \"35\" ]" , element.toString());
	}
	@Test
	@Order(5)
	void test_5() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[<http://rdfs/label> \"eastman3d\" ]# / <http://default/lat>" , element.toString());
	}
	@Test
	@Order(6)
	void test_6() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq [<http://rdfs/label> \"Calc2Graph1\" ] ]# / ^<http://default/lat> / <http://default/long> / ^<http://default/left> / <http://default/right>" , element.toString());
	}
	@Test
	@Order(7)
	void test_7() {
		CharStream input = CharStreams.fromString( ":volumeFlow [ gt \"35\" ; rdfs:label \"Calc2Graph1\" ; eq [ rdfs:label \"Calc2Graph1\"] , :Calc2Graph1 ,\"Calc2Graph1\" ]");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/volumeFlow>[gt \"35\" ;<http://rdfs/label> \"Calc2Graph1\" ;eq ([<http://rdfs/label> \"Calc2Graph1\" ] , <http://default/Calc2Graph1>  , \"Calc2Graph1\" ) ]" , element.toString());
	}
	@Test
	@Order(8)
	void test_8() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[ rdfs:label \"eastman3d\" ]#[a :Location ]/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[<http://rdfs/label> \"eastman3d\" ]#[<http://rdftype> <http://default/Location> ] / <http://default/lat>" , element.toString());
	}
	@Test
	@Order(9)
	void test_9() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn#[:location.Map  id:Calc2Graph2 ]/:long");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>#[<http://default/location.Map> <http://id/Calc2Graph2> ] / <http://default/long>" , element.toString());
	}
	@Test
	@Order(10)
	void test_10() {
		CharStream input = CharStreams.fromString( ":Location@:appearsOn[eq [ rdfs:label 'Calc2Graph1']]#/:lat");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq [<http://rdfs/label> 'Calc2Graph1' ] ]# / <http://default/lat>" , element.toString());
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
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ]#" , element.toString());
		}catch(Exception e){
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq <http://id/Calc2Graph2> ;]#","" );
		}
	}
	@Test 
	@Order(12)
	void test_12() {
		try {

			PathProcessor.parsePathPattern(thing, ":Location@:appearsOn][eq id:Calc2Graph2]#");
		}catch(Exception e){
			assertEquals ("[line 1:20 in \":Location@:appearsOn][eq id:Calc2Graph2]#\": mismatched input ']' expecting {<EOF>, '|', '/', '{', '[', '#'}]"
					,e.getMessage() );
		}
	}
	@Test 
	@Order(13)
	void test_13() {
		try {

			PathElement element = PathProcessor.parsePathPattern(thing, ":Location@:appearsOn[eq id:Calc2Graph1, id:Calc2Graph2]#");
			assertEquals ("<http://default/Location>@<http://default/appearsOn>[eq (<http://id/Calc2Graph1> , <http://id/Calc2Graph2> ) ]#" , element.toString());
		}catch(Exception e){
			fail();
		}
	}

@Test 
@Order(14)
void test_14() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit{1, 42}/:massThroughput");
		assertEquals ("^<http://default/hasProductBatteryLimit>{1,42} / <http://default/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(15)
void test_15() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit{1,}/:massThroughput");
		assertEquals ("^<http://default/hasProductBatteryLimit>{1,*} / <http://default/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(16)
void test_16() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2}" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(17)
void test_17() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2} / <http://default/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(18)
void test_18() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / <http://default/massThroughput>){1,2} / *" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(19)
void test_19() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		assertEquals ("(^<http://default/hasProductBatteryLimit> / *){1,2} / <http://default/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(20)
void test_20() {
	try {
		
		new PathProcessor();
		PathElement element = PathProcessor.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		assertEquals ("*{1,2} / <http://default/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(21)
void test_21() {
	try {
		thing.prefix("http://default/override/");
		PathElement element = PathProcessor.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		assertEquals ("*{1,2} / <http://default/override/massThroughput>" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(22)
void test_22() {
	try {

		thing.prefix("http://default/");
		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/*");
		assertEquals ("^<http://default/hasProductBatteryLimit> / *" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(23)
void test_23() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow)");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (<http://default/massFlow> | <http://default/volumeFlow>)" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(24)
void test_24() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:massFlow |:volumeFlow  |:density)");
		assertEquals ("^<http://default/hasProductBatteryLimit> / ((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>)" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(25)
void test_25() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(:temp | (:massFlow |! :volumeFlow  |! :density))");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (<http://default/temp> | ((<http://default/massFlow> | !<http://default/volumeFlow>) | !<http://default/density>))" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(26)
void test_26() {
	try {

		PathElement element = PathProcessor.parsePathPattern(thing, "^:hasProductBatteryLimit/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("^<http://default/hasProductBatteryLimit> / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(27)
void test_27() {
	try {
		
		PathElement element = PathProcessor.parsePathPattern(thing, "(* | !^:hasProductBatteryLimit)/(* | !(:massFlow |:volumeFlow  |:density))");
		assertEquals ("(* | !^<http://default/hasProductBatteryLimit>) / (* | !((<http://default/massFlow> | <http://default/volumeFlow>) | <http://default/density>))" , element.toString());
	}catch(Exception e){
		fail();
	}
}
}
