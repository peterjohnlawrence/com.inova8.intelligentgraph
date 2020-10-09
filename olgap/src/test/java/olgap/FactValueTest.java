package olgap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;

public class FactValueTest extends OlgapTest{

	private static RepositoryConnection conn;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File dataDir = new File("src/test/resources/datadir/");
		FileUtils.deleteDirectory(dataDir);
		Repository workingRep = new SailRepository(new NativeStore(dataDir));

		String modelFilename = "src/test/resources/calc2graph.data.ttl";
		InputStream input = new FileInputStream(modelFilename);
		Model model = Rio.parse(input, "", RDFFormat.TURTLE);
		conn = workingRep.getConnection();
		conn.add(model.getStatements(null, null, null));
	}

	@Test
	void test0_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testProperty4>,'start','2010-08-01T00:00:00.000000000+00:00'^^xsd:dateTime, 'end','2010-08-02T00:00:00.000000000+00:00'^^xsd:dateTime, 'aggregate','average') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals("result=0.25075123726851867;\r\n",result);
	}
	@Test
	void test0_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  ?result \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/testProperty2>) as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals("result=0.21622;\r\n",result);
	}
	@Test
	void test1_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massFlowBalance>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=0.949998676776886\r\n",result);
	}
	@Test
	void test2_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=26.54999929666519;\r\n" + 
				"",result);
	}
	@Test
	void test2_2() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit2> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=14.40000057220459;\r\n" + 
				"",result);
	}
	@Test
	void test2_3() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=11.200000047683716;\r\n" +
				"",result);
	}
	@Test
	void test3_1() {
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  * \n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/maximumMassFlow>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n",result);
	}
	@Test
	void test4_1() {
		String queryString = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/batteryLimits>,'service',<http://localhost:8080/rdf4j-server/repositories/olgap>) as ?result ).\n"
		+ "  SERVICE <http://localhost:8080/rdf4j-server/repositories/olgap>{ GRAPH ?result { ?s ?p ?o }}"
		+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit1;\r\n" + 
				"result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit2;\r\n" + 
				"result=http://inova8.com/cat2graph/data/501A35D75E3A97E9A1FB2D7A7917A96D;p=http://inova8.com/calc2graph/id/hasBatteryLimit;s=http://inova8.com/calc2graph/id/Unit1;o=http://inova8.com/calc2graph/id/BatteryLimit3;\r\n" + 
				"",result);
	}
	@Test
	void test5_1() {
		String queryString5 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString5 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/Unit1> , <http://inova8.com/calc2graph/def/massThroughput>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString5);
		assertEquals("result=25.600000619888306;\r\n",result);
	}
	@Test
	void test6_1() {
		String queryString6 = "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString6 +=	 "  SELECT  *\n" 
		+ "WHERE {\n"
		+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit3> , <http://inova8.com/calc2graph/def/massYield>,'abc','def') as ?result ).\n"
		+ "}";
		String result = runQuery(conn, queryString6);
		assertEquals( "result=0.43749998195562556;\r\n",result);
	}
	@Test
	void test7_1() {
		String queryString = "";
		queryString += "PREFIX olgap: <http://inova8.com/olgap/> \n";
		queryString += "  SELECT  * \n" + "WHERE {\n"
				+ "	 BIND( olgap:factValue(<http://inova8.com/calc2graph/id/BatteryLimit1> , <http://inova8.com/calc2graph/def/volumeFlow>,'abc','def') as ?result ).\n"
				+ "}";
		String result = runQuery(conn, queryString);
		assertEquals( "result=59;\r\n",result);
	}

}
