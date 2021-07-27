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
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.eclipse.rdf4j.model.util.Values.iri;
import PathPattern.PathPatternLexer;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.PathPatternContext;
import path.PathTupleExpr;
import pathCalc.Thing;
import pathPatternElement.Iterations;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathErrorListener;
import pathPatternProcessor.PathPatternVisitor;
import pathQL.PathParser;
import pathQLRepository.PathQLRepository;
import utilities.Query;

/**
 * The Class PathPatternQueryExpressionTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiPathPatternQueryExpressionTests {
	
	/** The thing. */
	static Thing thing;
	
	/** The source. */
	static PathQLRepository source;
	
	/** The indices. */
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source= new PathQLRepository();
		source.getReifications().addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.getReifications().addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		source.getReifications().addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.getReifications().setReificationsAreLazyLoaded(true);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");
		//Dummy
		thing = source.getThing( "http://",null);
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
	

@Test 
@Order(1)
void test_1() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "^:hasProductBatteryLimit{1, 5}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1,INVERSE]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(0).toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(2)
void test_2() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit{1, 8}");
		Iterations iterations = Iterations.create(element);
		assertEquals ("{0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8}",iterations.toString());
		Iterations sortedIterations = iterations.sortByPathLength();
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,2);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "         Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n1_i2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1_i2)\r\n"
				+ "      Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1_i1,DIRECT]\r\n"
				+ "[n1_i1,http://default/hasProductBatteryLimit,n1_i2,DIRECT]\r\n"
				+ "[n1_i2,http://default/hasProductBatteryLimit,n1,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(2).toString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(3)
void test_3() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, ":Attribute@:density{1, 42}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,0);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "            Variable (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "            Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "            Variable (name=p0_1, value=http://default/density)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=p0_1, value=http://default/density)\r\n"
				+ "            Variable (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "            Variable (name=r1)\r\n"
				+ "   Union\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "         Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "         Variable (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "         Variable (name=r1)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,<http://default/Attribute>@http://default/density,n1,DIRECT,false]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}
@Test 
@Order(4)
void test_4() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, ":Attribute@:density{1, 42}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces 
		assertEquals
		 ("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1_i1)\r\n"
				+ "               Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n0)\r\n"
				+ "               Variable (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=r1_i1)\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1_i1)\r\n"
				+ "               Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
				+ "               Variable (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=r1_i1)\r\n"
				+ "      Union\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=r1_i1)\r\n"
				+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n1)\r\n"
				+ "            Variable (name=isObjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
				+ "            Variable (name=r1_i1)\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Variable (name=subject1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=n1_i1)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=n1_i1)\r\n"
				+ "               Variable (name=isSubjectOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#subject)\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "         Union\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=r1)\r\n"
				+ "               Variable (name=property1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
				+ "            StatementPattern\r\n"
				+ "               Variable (name=p0_1, value=http://default/density)\r\n"
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
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,<http://default/Attribute>@http://default/density,n1_i1,DIRECT,false]\r\n"
				+ "[n1_i1,<http://default/Attribute>@http://default/density,n1,DIRECT,false]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}

@Test 
@Order(5)
void test_5() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,0);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n0)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1,INVERSE]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}

@Test 
@Order(6)
void test_6() {
	try {
		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in1)\r\n"
				+ "         Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in1)\r\n"
				+ "         Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n0_i1)\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in2)\r\n"
				+ "         Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in2)\r\n"
				+ "         Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,http://default/massThroughput,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(1).toString());//
	}catch(Exception e){
		fail();
	}
}


@Test 
@Order(7)
void test_7() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p2_3, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,http://default/massThroughput,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "[n2,http://default/massThroughput,n3,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}

@Test 
@Order(8)
void test_8() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p2_3)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,http://default/massThroughput,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "[n2,p2_3,n3,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}


@Test 
@Order(9)
void test_9() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p1_2)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p0_1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p1_2)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p2_3, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,p1_2,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,p1_2,n2,DIRECT]\r\n"
				+ "[n2,http://default/massThroughput,n3,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}

@Test 
@Order(10)
void test_10() {
	try {

		PathElement element = PathParser.parsePathPattern(thing, "(*){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(thing,1);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Variable (name=p0_1)\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "         Variable (name=p0_1)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p1_2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,p0_1,n1_i1,DIRECT]\r\n"
				+ "[n1_i1,p0_1,n1,DIRECT]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		fail();
	}
}
















}
