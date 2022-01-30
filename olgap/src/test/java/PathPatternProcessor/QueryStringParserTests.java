/*
 * inova8 2020
 */
package PathPatternProcessor;

import static org.junit.jupiter.api.Assertions.*;

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

import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.pathPattern.PathPatternLexer;
import com.inova8.pathql.pathPattern.PathPatternParser;
import com.inova8.pathql.pathPattern.PathPatternParser.QueryStringContext;
import com.inova8.pathql.processor.PathPatternVisitor;

/**
 * The Class QueryStringParserTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QueryStringParserTests {
	
	/** The repository context. */
	static RepositoryContext repositoryContext;


	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		repositoryContext= new RepositoryContext();
		repositoryContext.prefix("http://default/").prefix("local","http://local/").prefix("rdfs","http://rdfs/").prefix("id","http://id/").prefix("xsd","http://www.w3.org/2001/XMLSchema#");

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
		//PathPatternContext pathPatternTree = parser.pathPattern();
		QueryStringContext queryStringTree = parser.queryString();
		PathPatternVisitor pathPatternVisitor = new PathPatternVisitor(repositoryContext);
		PathElement element = pathPatternVisitor.visit(queryStringTree);
		return element;
	}
	
	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	void test_0() {
		CharStream input = CharStreams.fromString( ":parent[:gender :female]/:parent[:gender :male; :birthplace [rdfs:label 'Maidstone']]/:parent&time='2019'&date='45'&double='42'");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent>[<http://default/gender> <http://default/female> ] / <http://default/parent>[<http://default/gender> <http://default/male> ;<http://default/birthplace> [<http://rdfs/label> Maidstone ] ] / <http://default/parent>&date=\"45\"&double=\"42\"&time=\"2019\""
				 , element.toString()+"&"+element.getCustomQueryOptions().toString());
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		CharStream input = CharStreams.fromString( ":parent&time='2019'&date='45'&double='42'^^xsd:double");
		PathElement element = prepareElement(input);
		assertEquals ("<http://default/parent>&date=\"45\"&double=\"42\"^^<http://www.w3.org/2001/XMLSchema#double>&time=\"2019\""
				 , element.toString()+"&"+element.getCustomQueryOptions().toString());
	}

}
