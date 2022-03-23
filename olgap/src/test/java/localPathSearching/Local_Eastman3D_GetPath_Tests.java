/*
 * inova8 2020
 */
package localPathSearching;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.results.PathResults;
import com.inova8.intelligentgraph.results.ResourceResults;

import utilities.Query;

/**
 * The Class Local_Eastman3D_GetPath_Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_Eastman3D_GetPath_Tests {
	

	
	/** The source. */
	private static IntelligentGraphRepository source;
	

	/** The working rep. */
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/eastman3d/");
		Query.addFile(workingRep, "src/test/resources/eastman3d/Eastman.3d.data.rev2.ttl");
		Query.addFile(workingRep, "src/test/resources/eastman3d/icore.def.ttl");
		Query.addFile(workingRep, "src/test/resources/eastman3d/Plant.3d.def.rev2.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("eastman", "http://inova8.com/eastman/id/");
		conn.setNamespace("eastman.Signal", "http://inova8.com/eastman/id/Signal/");
		conn.setNamespace("eastman.BatteryLimit", "http://inova8.com/eastman/id/BatteryLimit/");
		conn.setNamespace("eastman.Valve", "http://inova8.com/eastman/id/Valve/");
		conn.setNamespace("plant", "http://inova8.com/plant/def/");
		conn.setNamespace("plant.TransferenceKind", "http://inova8.com/plant/def/TransferenceKind/");
		
		source = IntelligentGraphRepository.create(workingRep);


	}
	
	/**
	 * Close class.
	 *
	 * @throws Exception the exception
	 */
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}	
	
	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}
	
	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	void assertEqualsWOSpaces(String actual, String expected){
		assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
}	
	
	/**
	 * Test 0.
	 */
	@Test
	@Order(0)
	void test_0() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			 ResourceResults facts = _this.getFacts("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("[ {s=http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Valve/U8}; {s=http://inova8.com/eastman/id/Valve/U8, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Pump/G103}; {s=http://inova8.com/eastman/id/Pump/G103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Vessel/V103};]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 01.
	 */
	@Test
	@Order(0)
	void test_01() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths = _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 1.
	 */
	@Test
	@Order(1)
	void test_1() {
		try {
			Thing _this =source.getThing("eastman.Valve:U8");
			PathResults paths =  _this.getPaths("plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 2.
	 */
	@Test
	@Order(2)
	void test_2() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths =  _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{1,4}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/eastman/id/ProcessFlow/B103-processFlow-V103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Boiler/B103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/eastman/id/ProcessFlow/Stripper.Condensate-processFlow-V103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Vessel/V103,<http://inova8.com/eastman/id/ProcessFlow/Stripper.Feed-processFlow-V103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 3.
	 */
	@Test
	@Order(3)
	void test_3() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			PathResults paths =  _this.getPaths("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,3}");//
			assertEquals("Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "Path=[[http://inova8.com/eastman/id/Signal/XMEAS17,http://inova8.com/plant/def/attribute.providedBy,http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,INVERSE]\r\n"
					+ "[http://inova8.com/eastman/id/Attribute/Stripper.Bottoms.volumeFlow.XMEAS17,http://inova8.com/plant/def/attribute.of.PlantItem,http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,DIRECT]\r\n"
					+ "[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
					+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 4.
	 */
	@Test
	@Order(4)
	void test_4() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			 ResourceResults facts = _this.getFacts("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,6}");//
			assertEquals("[ {s=http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Valve/U8}; {s=http://inova8.com/eastman/id/Valve/U8, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Pump/G103}; {s=http://inova8.com/eastman/id/Pump/G103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Vessel/V103}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Boiler/B103}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed}; {s=http://inova8.com/eastman/id/Boiler/B103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Vessel/V103}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/Boiler/B103}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate}; {s=http://inova8.com/eastman/id/Vessel/V103, p=^http://inova8.com/plant/def/Transference@http://inova8.com/plant/def/TransferenceKind/ProcessFlow, o=http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed};]", facts.toString());
//			assertEquals("[http://inova8.com/eastman/id/Valve/U8;http://inova8.com/eastman/id/Pump/G103;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;http://inova8.com/eastman/id/Vessel/V103;http://inova8.com/eastman/id/Boiler/B103;http://inova8.com/eastman/id/BatteryLimit/Stripper.Condensate;http://inova8.com/eastman/id/BatteryLimit/Stripper.Feed;]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 5.
	 */
	@Test
	@Order(5)
	void test_5() {
		try {
			Thing _this =source.getThing("eastman.Signal:XMEAS17");
			ResourceResults facts = _this.getFacts("^plant:attribute.providedBy/plant:attribute.of.PlantItem/^plant:Transference@plant.TransferenceKind:ProcessFlow{1,6}/^plant:attribute.of.PlantItem/plant:attribute.providedBy");//
			assertEquals("[ {s=http://inova8.com/eastman/id/Attribute/U1.valvePosition.XMV8, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMV8}; {s=http://inova8.com/eastman/id/Attribute/V103.level.XMEAS15, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS15}; {s=http://inova8.com/eastman/id/Attribute/V103.temperature.XMEAS18, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS18}; {s=http://inova8.com/eastman/id/Attribute/Stripper.Condensate.volumeFlow.XMEAS14, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS14}; {s=http://inova8.com/eastman/id/Attribute/Stripper.Feed.volumeFlow.XMEAS4, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS4}; {s=http://inova8.com/eastman/id/Attribute/V103.level.XMEAS15, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS15}; {s=http://inova8.com/eastman/id/Attribute/V103.temperature.XMEAS18, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS18}; {s=http://inova8.com/eastman/id/Attribute/Stripper.Condensate.volumeFlow.XMEAS14, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS14}; {s=http://inova8.com/eastman/id/Attribute/Stripper.Feed.volumeFlow.XMEAS4, p=http://inova8.com/plant/def/attribute.providedBy, o=http://inova8.com/eastman/id/Signal/XMEAS4};]", facts.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test 6.
	 */
	@Test
	@Order(6)
	void test_6() {
		try {
			Thing _this =source.getThing("eastman.BatteryLimit:Stripper.Bottoms");
			PathResults paths =  _this.getPaths("^plant:Transference@plant.TransferenceKind:ProcessFlow{3,3}");
			for(Path path: paths) {
					assertEquals("Path=[[http://inova8.com/eastman/id/BatteryLimit/Stripper.Bottoms,<http://inova8.com/eastman/id/ProcessFlow/U8-processFlow-Stripper.Bottoms>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Valve/U8,INVERSE,false]\r\n"
							+ "[http://inova8.com/eastman/id/Valve/U8,<http://inova8.com/eastman/id/ProcessFlow/G103-processFlow-U8>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Pump/G103,INVERSE,false]\r\n"
							+ "[http://inova8.com/eastman/id/Pump/G103,<http://inova8.com/eastman/id/ProcessFlow/V103-processFlow-G103>:<http://inova8.com/plant/def/Transference>@http://inova8.com/plant/def/TransferenceKind/ProcessFlow,http://inova8.com/eastman/id/Vessel/V103,INVERSE,false]\r\n"
							+ "]\r\n"
							+ "", path.toString());
				break;	
			}

		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
