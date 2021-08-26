package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.parser.PathParser;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PathIterationTests {
	ArrayList<Integer> spanMin = new ArrayList<Integer>();
	ArrayList<Integer> spanMax = new ArrayList<Integer>();
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
	@Test 
	@Order(0)
	void test_0() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit{1, 4}");
			assertEquals ("<http://default/hasProductBatteryLimit>{1,4}" , element.toString());;
			assertEquals ("{0=1, 1=2, 2=3, 3=4}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=1, 1=2, 2=3, 3=4}",sortedIterations.toString());
			assertEquals ("{1,2,4}",element.getPathShareString(1));
			assertEquals ("{1,4,4}",element.getPathShareString(3));
		}catch(Exception e){
			fail();
		}
	}
	@Test 
	@Order(2)
	void test_2() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit{1, 4}/:massThroughput{1,2}");
			assertEquals ("<http://default/hasProductBatteryLimit>{1,4} / <http://default/massThroughput>{1,2}" , element.toString());
			assertEquals ("{0=2, 1=3, 2=3, 3=4, 4=4, 5=5, 6=5, 7=6}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=2, 1=3, 2=3, 3=4, 4=4, 5=5, 6=5, 7=6}",sortedIterations.toString());
			assertEquals ("({1,1,4}/{1,2,2}){1,1,1}",element.getPathShareString(1));
			assertEquals ("({1,2,4}/{1,2,2}){1,1,1}",element.getPathShareString(3));
			
		}catch(Exception e){
			fail();
		}
	}
	@Test 
	@Order(3)
	void test_3() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit{1, 2}/:massThroughput{1,2}/:massThroughput{1,2}");
			assertEquals ("<http://default/hasProductBatteryLimit>{1,2} / <http://default/massThroughput>{1,2} / <http://default/massThroughput>{1,2}" , element.toString());
			assertEquals ("{0=3, 1=4, 2=4, 3=5, 4=4, 5=5, 6=5, 7=6}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=3, 1=4, 2=4, 4=4, 3=5, 5=5, 6=5, 7=6}",sortedIterations.toString());
			assertEquals ("(({1,1,2}/{1,1,2}){1,1,1}/{1,2,2}){1,1,1}",element.getPathShareString(1));
			assertEquals ("(({1,1,2}/{1,2,2}){1,1,1}/{1,2,2}){1,1,1}",element.getPathShareString(3));
			
		}catch(Exception e){
			fail();
		}
	}
	@Test 
	@Order(4)
	void test_4() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, "((^:hasProductBatteryLimit/:hasUnit){1, 2}/:massThroughput){1,3}");
			assertEquals ("((^<http://default/hasProductBatteryLimit> / <http://default/hasUnit>){1,2} / <http://default/massThroughput>){1,3}" , element.toString());
			assertEquals ("{0=3, 1=5, 2=6, 3=10, 4=9, 5=15}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=3, 1=5, 2=6, 4=9, 3=10, 5=15}",sortedIterations.toString());
			assertEquals ("(({1,1,1}/{1,1,1}){1,2,2}/{1,1,1}){1,1,3}",element.getPathShareString(1));
			assertEquals ("(({1,1,1}/{1,1,1}){1,1,2}/{1,1,1}){1,2,3}",element.getPathShareString(2));
			assertEquals ("(({1,1,1}/{1,1,1}){1,2,2}/{1,1,1}){1,2,3}",element.getPathShareString(3));
			assertEquals ("(({1,1,1}/{1,1,1}){1,1,2}/{1,1,1}){1,3,3}",element.getPathShareString(4));
			assertEquals ("(({1,1,1}/{1,1,1}){1,2,2}/{1,1,1}){1,3,3}",element.getPathShareString(5));
		}catch(Exception e){
			fail();
		}
	}
	@Test 
	@Order(5)
	void test_5() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":hasProductBatteryLimit{0, 4}/:massThroughput{1,2}");
			assertEquals ("<http://default/hasProductBatteryLimit>{0,4} / <http://default/massThroughput>{1,2}" , element.toString());
			assertEquals ("{0=1, 1=2, 2=2, 3=3, 4=3, 5=4, 6=4, 7=5, 8=5, 9=6}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=1, 1=2, 2=2, 3=3, 4=3, 5=4, 6=4, 7=5, 8=5, 9=6}",sortedIterations.toString());
			assertEquals ("({0,0,4}/{1,2,2}){1,1,1}",element.getPathShareString(1));
			assertEquals ("({0,1,4}/{1,2,2}){1,1,1}",element.getPathShareString(3));
			
		}catch(Exception e){
			fail();
		}
	}
	@Test 
	@Order(6)
	void test_6() {
		try {

			PathElement element = PathParser.parsePathPattern(thing, ":hasParent{0,4}/:hasParent[:hasGender :Female]");
			assertEquals ("<http://default/hasParent>{0,4} / <http://default/hasParent>[<http://default/hasGender> <http://default/Female> ]" , element.toString());
			assertEquals ("{0=1, 1=2, 2=3, 3=4, 4=5}",element.getIterations().toString());
			Iterations sortedIterations = element.getIterations().sortByPathLength();
			assertEquals ("{0=1, 1=2, 2=3, 3=4, 4=5}",sortedIterations.toString());
			assertEquals ("({0,0,4}/{1,1,1}){1,1,1}",element.getPathShareString(0));
			assertEquals ("({0,1,4}/{1,1,1}){1,1,1}",element.getPathShareString(1));
			assertEquals ("({0,3,4}/{1,1,1}){1,1,1}",element.getPathShareString(3));
			
		}catch(Exception e){
			fail();
		}
	}
}
