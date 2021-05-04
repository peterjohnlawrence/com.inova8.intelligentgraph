/*
 * inova8 2020
 */
package olgap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
 * The Class FactDebugTest.
 */
public class FactDebugTest extends OlgapTest{

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

		String modelFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream input = new FileInputStream(modelFilename);
		Model model = Rio.parse(input, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(model.getStatements(null, null, null));
	}

	/**
	 * Test 0 1.
	 */
	@Test
	void test0_1() {
		String queryString = "";
		String script = "42;";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> PREFIX script: <http://inova8.com/calc2graph/def/script/>\n";
		queryString += "select ?subject  ?property ?object  ?trace ?scripttype\r\n" + 
				"{VALUES(?object ?property ?service ?subject ){(\'0.56;\'^^<http://inova8.com/calc2graph/def/script/groovy> <http://inova8.com/calc2graph/def/volumeFlow> <http://localhost:8080/rdf4j-server/repositories/olgap?distinct=true> <http://inova8.com/calc2graph/id/BatteryLimit2> )} \r\n" + 
				"	\r\n" + 
				"		BIND(<http://inova8.com/olgap/factDebug>(?subject , ?property, ?object, 'start',42) as ?trace)\r\n" + 
				"	 BIND (datatype(?object) as ?scripttype)\r\n" + 
				"} LIMIT 1000	";
		String result = runQuery(conn, queryString);
		assertEquals( "result=-0.8200013935565948;\r\n",result);
	}
	
	/**
	 * Test 1 1.
	 */
	@Test
	void test1_1() {
		String queryString = "";
		String script = "var massFlowBalance=0.0; " + 
				"for(batterylimit in $this.getFacts(\\\"<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>\\\")){" + 
				"	massFlowBalance += batterylimit.getFact(\\\"<http://inova8.com/calc2graph/def/massFlow>\\\").doubleValue()" + 
				"}; " + 
				"for(batterylimit in $this.getFacts(\\\"<http://inova8.com/calc2graph/def/hasProductBatteryLimit>\\\")){" + 
				"	massFlowBalance -= batterylimit.getFact(\\\"<http://inova8.com/calc2graph/def/massFlow>\\\").doubleValue()" + 
				"}; " + 
				"massFlowBalance;";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massFlowBalance>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=-0.8200013935565948;\r\n",result);
	}
	
	/**
	 * Test 2 1.
	 */
	@Test
	void test2_1() {
		String script = "<http://inova8.com/calc2graph/id/calculateMassFlow>";
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/massFlow>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=24.77999922633171;\r\n" + 
				"",result);
	}
	
	/**
	 * Test 2 1 1.
	 */
	@Test
	void test2_1_1() {
		String script =
		"return $this.getThing(\\\"http://inova8.com/calc2graph/id/BatteryLimit2\\\").getReifiedFact(\\\"http://inova8.com/calc2graph/def/Attribute\\\",\\\"http://inova8.com/calc2graph/def/density\\\").doubleValue()";
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/testProperty1>,\"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		try{
			   Double.parseDouble(result);
				assertEquals( "0.36" ,result);
			}catch(NumberFormatException exception){
			 fail(result);
			}	}
	
	/**
	 * Test 2 2.
	 */
	@Test
	void test2_2() {
		String script = "<http://inova8.com/calc2graph/id/calculateMassFlow>";
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massFlow>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=14.40000057220459;\r\n" + 
				"",result);
	}
	
	/**
	 * Test 2 3.
	 */
	@Test
	void test2_3() {
		String script = "<http://inova8.com/calc2graph/id/calculateMassFlow>";
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massFlow>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=11.200000047683716;\r\n" +
				"",result);
	}
	
	/**
	 * Test 3 1.
	 */
	@Test
	void test3_1() {
		String script = "var maximumMassFlow=0.0;  " + 
			"var maximumMassFlowBatteryLimit; " + 
			"for(batterylimit in $this.getFacts(\\\"<http://inova8.com/calc2graph/def/hasProductBatteryLimit>\\\")){ " + 
			"	massFlow = batterylimit.getFact(\\\"<http://inova8.com/calc2graph/def/massFlow>\\\").doubleValue(); " + 
			"	if(massFlow > maximumMassFlow){ " + 
			"		maximumMassFlow = massFlow; " + 
			"		maximumMassFlowBatteryLimit= batterylimit; " + 
			"	} " + 
			"}; " + 
			"return maximumMassFlowBatteryLimit;";
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";

		queryString +=	 "  SELECT  * \n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/maximumMassFlow>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n",result);
	}
	
	/**
	 * Test 4 1.
	 */
	@Test
	void test4_1() {
		String script = "var modelBuilder = $builder;  " + 
			"modelBuilder = modelBuilder.setNamespace(\\\"id\\\",\\\"http://inova8.com/calc2graph/id/\\\").subject($this.superValue.toString());  " + 
			"for(batterylimit in $this.getFacts(\\\"<http://inova8.com/calc2graph/def/hasFeedBatteryLimit>\\\")){  " + 
			"    modelBuilder.add(\\\"id:hasBatteryLimit\\\",batterylimit.getValue());  " + 
			"};   " + 
			"for(batterylimit in $this.getFacts(\\\"<http://inova8.com/calc2graph/def/hasProductBatteryLimit>\\\")){  " + 
			"    modelBuilder.add(\\\"id:hasBatteryLimit\\\",batterylimit.getValue());  " + 
			"};   " + 
			"return modelBuilder.build();";
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/batteryLimits>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
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
		String script = "var massThroughput=0.0;  " +
			"for(batterylimit in $this.getFacts(\\\"http://inova8.com/calc2graph/def/hasProductBatteryLimit\\\")){ " +
			"	massThroughput += batterylimit.getFact(\\\"http://inova8.com/calc2graph/def/massFlow\\\",$args).doubleValue() n" +
			"};  " +
			"massThroughput;"; 
		String queryString5 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString5 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massThroughput>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString5);
		assertEquals("result=25.600000619888306;\r\n",result);
	}
	
	/**
	 * Test 6 1.
	 */
	@Test
	void test6_1() {
		String script = "<http://inova8.com/calc2graph/id/calculateMassYield>";
		String queryString6 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString6 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massYield>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString6);
		assertEquals( "result=0.43749998195562556;\r\n",result);
	}
	
	/**
	 * Test 7 1.
	 */
	@Test
	void test7_1() {
		String script = "59;";
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factDebug(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/volumeFlow>, \"" + script + "\"^^olgap:groovy,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=59;\r\n",result);
	}

}
