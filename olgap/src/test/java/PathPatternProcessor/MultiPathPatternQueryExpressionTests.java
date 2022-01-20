/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathConstants;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class PathPatternQueryExpressionTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MultiPathPatternQueryExpressionTests {
	
	/** The thing. */
	static Thing thing;
	
	/** The source. */
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
		repositoryContext= new RepositoryContext();
		repositoryContext.getReifications().addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		repositoryContext.getReifications().addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		repositoryContext.getReifications().addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		repositoryContext.getReifications().setReificationsAreLazyLoaded(true);
		repositoryContext.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");
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

		PathElement element = PathParser.parsePathPattern(repositoryContext, "^:hasProductBatteryLimit{1, 5}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery();
		//Query.assertEqualsWOSpaces 
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
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1,INVERSE]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(0).toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(2)
void test_2() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":hasProductBatteryLimit{1, 8}");
		Iterations iterations = Iterations.create(element);
		assertEquals ("{0=1, 1=2, 2=3, 3=4, 4=5, 5=6, 6=7, 7=8}",iterations.toString());
		@SuppressWarnings("unused")
		Iterations sortedIterations = iterations.sortByPathLength();
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(2,null);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "         Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n1_i2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1_i2)\r\n"
				+ "      Variable (name=p_n0_n1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1_i1,DIRECT]\r\n"
				+ "[n1_i1,http://default/hasProductBatteryLimit,n1_i2,DIRECT]\r\n"
				+ "[n1_i2,http://default/hasProductBatteryLimit,n1,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(2).toString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(3)
void test_3() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, ":Attribute@:density{1, 42}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(0,null);
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
				+ "            Variable (name=p_n0_n1, value=http://default/density)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=p_n0_n1, value=http://default/density)\r\n"
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
		assertEquals("", e.getMessage());
	}
}
@Test 
@Order(4)
void test_4() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext,":Attribute@:density{1, 42}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
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
		 		+ "               Variable (name=p_n0_n1, value=http://default/density)\r\n"
		 		+ "            StatementPattern\r\n"
		 		+ "               Variable (name=p_n0_n1, value=http://default/density)\r\n"
		 		+ "               Variable (name=isPropertyOf1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate)\r\n"
		 		+ "               Variable (name=r1_i1)\r\n"
		 		+ "      Union\r\n"
		 		+ "         StatementPattern\r\n"
		 		+ "            Variable (name=r1_i1)\r\n"
		 		+ "            Variable (name=object1, value=http://www.w3.org/1999/02/22-rdf-syntax-ns#object)\r\n"
		 		+ "            Variable (name=n1_i1)\r\n"
		 		+ "         StatementPattern\r\n"
		 		+ "            Variable (name=n1_i1)\r\n"
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
		 		+ "               Variable (name=p_n0_n1, value=http://default/density)\r\n"
		 		+ "            StatementPattern\r\n"
		 		+ "               Variable (name=p_n0_n1, value=http://default/density)\r\n"
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
		assertEquals("", e.getMessage());
	}
}

@Test 
@Order(5)
void test_5() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(0,null);
		//Query.assertEqualsWOSpaces 
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
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n1,INVERSE]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

@Test 
@Order(6)
void test_6() {
	try {
		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1,2}");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in1)\r\n"
				+ "         Variable (name=p_n0_n0_in1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in1)\r\n"
				+ "         Variable (name=p_n0_in1_n0_i1, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n0_i1)\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in2)\r\n"
				+ "         Variable (name=p_n0_i1_n0_in2, value=http://default/hasProductBatteryLimit)\r\n"
				+ "         Variable (name=n0_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0_in2)\r\n"
				+ "         Variable (name=p_n0_in2_n2, value=http://default/massThroughput)\r\n"
				+ "         Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,http://default/massThroughput,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());//element.getPathBindings().get(1).toString());//
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}


@Test 
@Order(7)
void test_7() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext,"(^:hasProductBatteryLimit/:massThroughput){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_n0_in1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_in1_n0_i1, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_i1_n0_in2, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_in2_n2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p_n2_n3, value=http://default/massThroughput)\r\n"
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
		assertEquals("", e.getMessage());
	}
}

@Test 
@Order(8)
void test_8() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/:massThroughput){1, 2}/*");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
		//Query.assertEqualsWOSpaces
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_n0_in1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_in1_n0_i1, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_i1_n0_in2, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_in2_n2, value=http://default/massThroughput)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p_n2_n3)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,http://default/massThroughput,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "[n2,p_n2_n3,n3,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}


@Test 
@Order(9)
void test_9() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(^:hasProductBatteryLimit/*){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_n0_in1, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in1)\r\n"
				+ "            Variable (name=p_n0_in1_n0_i1)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "      Join\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_i1_n0_in2, value=http://default/hasProductBatteryLimit)\r\n"
				+ "            Variable (name=n0_i1)\r\n"
				+ "         StatementPattern\r\n"
				+ "            Variable (name=n0_in2)\r\n"
				+ "            Variable (name=p_n0_in2_n2)\r\n"
				+ "            Variable (name=n2)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "      Variable (name=p_n2_n3, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n3)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,http://default/hasProductBatteryLimit,n0_in1,INVERSE]\r\n"
				+ "[n0_in1,p_n0_in1_n0_i1,n0_i1,DIRECT]\r\n"
				+ "[n0_i1,http://default/hasProductBatteryLimit,n0_in2,INVERSE]\r\n"
				+ "[n0_in2,p_n0_in2_n2,n2,DIRECT]\r\n"
				+ "[n2,http://default/massThroughput,n3,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}

@Test 
@Order(10)
void test_10() {
	try {

		PathElement element = PathParser.parsePathPattern(repositoryContext, "(*){1, 2}/:massThroughput");
		PathTupleExpr pathTupleExpr = element.pathPatternQuery(1,null);
		//Query.assertEqualsWOSpaces 
		assertEquals
		("Join\r\n"
				+ "   Join\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n0)\r\n"
				+ "         Variable (name=p_n0_n1)\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "      StatementPattern\r\n"
				+ "         Variable (name=n1_i1)\r\n"
				+ "         Variable (name=p_n0_n1)\r\n"
				+ "         Variable (name=n1)\r\n"
				+ "   StatementPattern\r\n"
				+ "      Variable (name=n1)\r\n"
				+ "      Variable (name=p_n1_n2, value=http://default/massThroughput)\r\n"
				+ "      Variable (name=n2)\r\n"
				+ "" ,pathTupleExpr.toString());
		assertEquals
		("[n0,p_n0_n1,n1_i1,DIRECT]\r\n"
				+ "[n1_i1,p_n0_n1,n1,DIRECT]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathTupleExpr.pathToString());
	}catch(Exception e){
		assertEquals("", e.getMessage());
	}
}
















}
