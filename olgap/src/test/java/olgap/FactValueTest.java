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
 * The Class FactValueTest.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FactValueTest extends OlgapTest{

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
	 * Test 0 0.
	 */
	@Test
	@Order(3)
	void test0_0() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:objectValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testProperty4>,'$this.prefix(\"<http://inova8.com/calc2graph/def/>\");var result= $this.getFact(\":volumeFlow\").floatValue()* $this.getFact(\":Attribute@:density\").floatValue();  result;'^^<http://inova8.com/script/groovy>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
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
	 * Test 0 1.
	 */
	@Order(3)
	void test0_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testProperty4>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
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
	 * Test 0 2.
	 */
	@Test
	void test0_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/density>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime,'end','','aggregate','Average') as ?result ).\n"
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
	void test1_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massFlowBalance>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			 Double.parseDouble(result);
			//   assertEquals("0.949998676776886",result);
			}catch(NumberFormatException exception){
			 fail(result);
			}
	}
	
	/**
	 * Test 2 0.
	 */
	@Test
	void test2_0() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/density>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
				assertEquals( "" ,result);
			}catch(NumberFormatException exception){
				assertEquals( "" ,result);
			}

	}
	
	/**
	 * Test 2 0 1.
	 */
	@Test
	void test2_0_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/density>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "0.24775666666666668" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}

	}
	
	/**
	 * Test 2 0 2.
	 */
	@Test
	void test2_0_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "14.058126911520958" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}

	}
	
	/**
	 * Test 2 1.
	 */
	@Test
	void test2_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/testProperty1>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
				assertEquals( "0.36" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}	}
	
	/**
	 * Test 2 1 1.
	 */
	@Test
	void test2_1_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/testProperty2>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "0.36" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}	
	}
	
	/**
	 * Test 2 2.
	 */
	@Test
	void test2_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);

		try{
			   Double.parseDouble(result);
			//	assertEquals( "26.54999929666519" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}	
	}
	
	/**
	 * Test 2 3.
	 */
	@Test
	void test2_3() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "14.40000057220459" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}	
	}
	
	/**
	 * Test 2 4.
	 */
	@Test
	void test2_4() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "11.200000047683716" ,result);
			}catch(NumberFormatException exception){
				 fail(result);
			}
	}
	
	/**
	 * Test 3 1.
	 */
	@Test
	void test3_1() {
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  * \n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/maximumMassFlow>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "http://inova8.com/calc2graph/id/BatteryLimit2",result);
	}
	
	/**
	 * Test 4 1.
	 */
	@Test
	void test4_1() {
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/batteryLimits>,'service',<http://localhost:8080/rdf4j-server/repositories/calc2graph>) as ?result ).\n"
		+ "  SERVICE <http://localhost:8080/rdf4j-server/repositories/calc2graph>{ GRAPH ?result { ?s ?p ?o }}"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit1;\r\n" + 
				"result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n" + 
				"result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit3;\r\n" + 
				"",result);
	}
	
	/**
	 * Test 5 1.
	 */
	@Test
	void test5_1() {
		String queryString5 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString5 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massThroughput>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString5);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "25.600000619888306" ,result);
			}catch(NumberFormatException exception){
				 fail(result);
			}
	}
	
	/**
	 * Test 6 1.
	 */
	@Test
	void test6_1() {
		String queryString6 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString6 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massYield>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString6);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "0.43749998195562556" ,result);
			}catch(NumberFormatException exception){
				 fail(result);
			}
	}
	
	/**
	 * Test 7 1.
	 */
	@Test
	void test7_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/volumeFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
			//	assertEquals( "59" ,result);
			}catch(NumberFormatException exception){
				 fail(result);
			}
	}

}
