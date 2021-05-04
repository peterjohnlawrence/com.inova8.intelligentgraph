/*
 * inova8 2020
 */
package olgap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

/**
 * The Class FactTest.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FactTest extends OlgapTest{

	/** The conn. */
	private static RepositoryConnection conn;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(new NativeStore(dataDir));

		String dataFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null));
		
		String modelFilename = "src/test/resources/calc2graph.def.ttl";
		InputStream modelInput = new FileInputStream(modelFilename);
		Model modelModel = Rio.parse(modelInput, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(modelModel.getStatements(null, null, null));
//		String plantFilename = "src/test/resources/Plant.2d.def.ttl";
//		InputStream plantInput = new FileInputStream(plantFilename);
//		Model plantModel = Rio.parse(plantInput, "", RDFFormat.TURTLE);
//		conn = workingRep.getConnection();
//		conn.add(plantModel.getStatements(null, null, null));
	}
	
	/**
	 * Test clear cache.
	 */
	@Test
	@Order(1)
	void Test_ClearCache() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:clearCache() as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals("true",result);
	}
	
	/**
	 * Test clear cache service.
	 */
	@Test
	@Order(2)
	void Test_ClearCacheService() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:clearCache('service',<http://localhost:8080/rdf4j-server/repositories/olgap>, 'before','2021-08-01T00:00:00.000000000+00:00'^^xsd:dateTime) as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals("true",result);
	}
	
	/**
	 * Test 1 0.
	 */
	@Test
	@Order(3)
	void test1_0() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testProperty4>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;'^^<http://inova8.com/script/groovy>, 'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 1.
	 */
	@Test
	@Order(4)
	void test1_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massYield>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue();  result;'^^<http://inova8.com/script/groovy>, 'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 2.
	 */
	@Test
	@Order(5)
	void test1_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/property4>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massThroughput\").floatValue();  result;'^^<http://inova8.com/script/groovy>) as ?result ).\n"
//				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massThroughput>) as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 3.
	 */
	@Test
	@Order(6)
	void test1_3() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massYield>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;'^^<http://inova8.com/script/groovy>, 'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 4.
	 */
	@Test
	@Order(7)
	void test1_4() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massYield>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":massFlow\").floatValue()/ $this.getFact(\"^:hasProductBatteryLimit\").getFact(\":massThroughput\").floatValue();  result;'^^<http://inova8.com/script/groovy>, 'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 5.
	 */
	@Test
	@Order(8)
	void test1_5() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massYield>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result=  $this.getFact(\"^:hasProductBatteryLimit/:massThroughput\").floatValue();  result;'^^<http://inova8.com/script/groovy>, 'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 1 6.
	 */
	@Test
	@Order(9)
	void test1_6() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testproperty1>, '$this.prefix(\"calc2graph\",\"<http://inova8.com/calc2graph/def/>\"); $this.getFact(\"calc2graph:testProperty2\").doubleValue()'^^<http://inova8.com/script/groovy>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.23683333333333334",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
}
