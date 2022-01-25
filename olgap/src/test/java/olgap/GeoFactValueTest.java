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
 * The Class GeoFactValueTest.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Deprecated
public class GeoFactValueTest extends OlgapTest{

	/** The conn. */
	private static RepositoryConnection conn;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/olgap/");
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
	 * Test 1 lat.
	 */
	@Test
	@Order(3)
	void test1_lat() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/lat>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
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
	 * Test 1 long.
	 */
	@Test
	void test1_long() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/long>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime,'end','','aggregate','Average') as ?result ).\n"
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
	 * Test 2 lat.
	 */
	@Test

	void test2_lat() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/lat>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
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
	 * Test 2 long.
	 */
	@Test
	void test2_long() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/long>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime,'end','','aggregate','Average') as ?result ).\n"
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
