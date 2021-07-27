/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.antlr.v4.runtime.RecognitionException;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import path.PathBinding;
import path.PathBindings;

import static org.eclipse.rdf4j.model.util.Values.iri;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQLRepository.PathQLRepository;

/**
 * The Class PathPatternPathTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PathPatternPathTests {
	
	/** The thing. */
	static Thing thing;
	
	/** The source. */
	static PathQLRepository source;
	
	static ArrayList<Integer> indices = new ArrayList<Integer>();
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		source= new PathQLRepository();
		source.getReifications().addReificationType(PathConstants.RDF_STATEMENT_IRI, PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.getReifications().addReificationType(iri("http://default/Attribute"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, PathConstants.RDF_ISSUBJECTOF_IRI, PathConstants.RDF_ISPREDICATEOF_IRI, PathConstants.RDF_ISOBJECTOF_IRI);
		source.getReifications().addReificationType(iri("http://default/Location"), PathConstants.RDF_SUBJECT_IRI, PathConstants.RDF_PREDICATE_IRI, PathConstants.RDF_OBJECT_IRI, null, null, null);
		source.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");
		thing = source.getThing( "http://",null);
	}

	@BeforeEach
	void setUp() throws Exception {
	}
	

	@Test
	@Order(1)
	void test_1() throws RecognitionException, PathPatternException {
		PathElement element =  PathParser.parsePathPattern(thing,":parent1/:parent2/:parent3");
		PathBinding pathBinding = new PathBinding();
		pathBinding = element.visitPathBinding(pathBinding,0);
		assertEquals ("[n0,http://default/parent1,n1,DIRECT]\r\n"
				+ "[n1,http://default/parent2,n2,DIRECT]\r\n"
				+ "[n2,http://default/parent3,n3,DIRECT]\r\n"
				+ "" , pathBinding.toString());
	}


	@Test
	@Order(2)
	void test_2() throws RecognitionException, PathPatternException {
		PathElement element = PathParser.parsePathPattern(thing,":parent1[:gender :female]/^:child2[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent3");
		PathBinding pathBinding = new PathBinding();
		pathBinding = element.visitPathBinding(pathBinding,0);
		assertEquals ("[n0,http://default/parent1,n1,DIRECT]\r\n"
				+ "[n1,http://default/child2,n2,INVERSE]\r\n"
				+ "[n2,http://default/parent3,n3,DIRECT]\r\n"
				+ ""
				 , pathBinding.toString());
	}


	@Test
	@Order(3)
	void test_3()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(thing,"^:hasProductBatteryLimit>:massThroughput");
		PathBinding pathBinding = new PathBinding();
		pathBinding = element.visitPathBinding(pathBinding,0);
		assertEquals ("[n0,http://default/hasProductBatteryLimit,n1,INVERSE]\r\n"
				+ "[n1,http://default/massThroughput,n2,DIRECT]\r\n"
				+ "" ,pathBinding.toString());
	}	
	

	@Test
	@Order(4)
	void test_4()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(thing,":Location@:appearsOn[ eq [ rdfs:label \"Calc2Graph1\"] ]#/^:lat/:long/^:left/:right");
		PathBinding pathBinding = new PathBinding();
		pathBinding = element.visitPathBinding(pathBinding,0);
		assertEquals ("[n0,<http://default/Location>@http://default/appearsOn,r1,DIRECT,true]\r\n"
				+ "[r1,http://default/lat,n2,INVERSE]\r\n"
				+ "[n2,http://default/long,n3,DIRECT]\r\n"
				+ "[n3,http://default/left,n4,INVERSE]\r\n"
				+ "[n4,http://default/right,n5,DIRECT]\r\n"
				+ "" , pathBinding.toString());
	}
	@Test
	@Order(5)
	void test_5()  throws RecognitionException, PathPatternException{
		PathElement element = PathParser.parsePathPattern(thing,":connectedTo{1,3}");
		PathBinding pathBinding = new PathBinding();
		pathBinding = element.visitPathBinding(pathBinding,2);
		assertEquals ("[n0,http://default/connectedTo,n1_i1,DIRECT]\r\n"
				+ "[n1_i1,http://default/connectedTo,n1_i2,DIRECT]\r\n"
				+ "[n1_i2,http://default/connectedTo,n1,DIRECT]\r\n"
				+ "" , pathBinding.toString());
	}
//	@Test
//	@Order(6)
//	//Test no longer valid as pathBindings created on creation of PathTupleExpr
//	void test_6()  throws RecognitionException, PathPatternException{
//		PathElement element = PathParser.parsePathPattern(thing,":connectedTo{1,3}");
//		assertEquals ("{0=[n0,http://default/connectedTo,n1,DIRECT]\r\n"
//				+ ", 1=[n0,http://default/connectedTo,n1_i1,DIRECT]\r\n"
//				+ "[n1_i1,http://default/connectedTo,n1,DIRECT]\r\n"
//				+ ", 2=[n0,http://default/connectedTo,n1_i1,DIRECT]\r\n"
//				+ "[n1_i1,http://default/connectedTo,n1_i2,DIRECT]\r\n"
//				+ "[n1_i2,http://default/connectedTo,n1,DIRECT]\r\n"
//				+ "}" , element.getPathBindings().toString());
//	}
}
