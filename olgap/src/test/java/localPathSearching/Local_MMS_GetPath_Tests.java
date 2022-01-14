/*
 * inova8 2020
 */
package localPathSearching;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathQLModel.MatchFact;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.pathQLResults.PathResults;

import utilities.Query;

/**
 * The Class ThingTests.
 */
@SuppressWarnings("deprecation")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_MMS_GetPath_Tests {
	

	
	/** The source. */
	private static IntelligentGraphRepository source;
	

	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/mms/");
		Query.addRDFFile(workingRep, "src/test/resources/mms/meta-model-schema.rdf");
		Query.addFile(workingRep, "src/test/resources/mms/fluentOnemms.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/fluentOnemmsEnh.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/masterdata.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/mms-ext.pii.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/mms-ext.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/mms-ext.view.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/northwindmms.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/sakilamms.pii.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/sakilamms.ttl");
		Query.addFile(workingRep, "src/test/resources/mms/sakilamms.views.ttl");
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("mms", "http://rdf.cdisc.org/mms#");
		conn.setNamespace("mms_ext", "http://in4mium.com/masterdatakg/schema/mms-ext#");
		conn.setNamespace("masterdata", "http://in4mium.com/masterdatakg/");
		conn.setNamespace("mmsid", "http://in4mium.com/masterdatakg/id/");
		conn.setNamespace("mmsdef", "http://in4mium.com/masterdatakg/def/");
		conn.setNamespace("masterdata11179", "http://in4mium.com/masterdatakg/11179/id/");		
		conn.setNamespace("views", "http://in4mium.com/masterdatakg/data/sakila/sakilamms/views#");		
		conn.setNamespace("pii", "http://in4mium.com/masterdatakg/data/sakila/sakilamms/pii#");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("dc", "http://purl.org/dc/elements/1.1/");
		
		conn.setNamespace("sakila", "http://in4mium.com/masterdatakg/mms/id/sakila/");
		
		
		source = IntelligentGraphRepository.create(workingRep);


	}
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

	@Test
	@Order(1)
	void test_1() {
		try {
			Thing _this =source.getThing("sakila:column.customer_list.SID");
			PathResults paths =  _this.getPaths("mms:context");
			assertEquals("Path=[[http://in4mium.com/masterdatakg/mms/id/sakila/column.customer_list.SID,http://rdf.cdisc.org/mms#context,http://in4mium.com/masterdatakg/mms/id/sakila/dataset.customer_list,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	@Order(2)
	void test_2() {
		try {
			Thing _this =source.getThing("mms_ext:Sensitive_PII");
			PathResults paths =  _this.getPaths("^mms_ext:dataElementSensitivity/(^mms_ext:isDerivedFrom){1,5}/mms:context[eq sakila:dataset.customer_list]");
			assertEquals("Path=[[http://in4mium.com/masterdatakg/schema/mms-ext#Sensitive_PII,http://in4mium.com/masterdatakg/schema/mms-ext#dataElementSensitivity,http://in4mium.com/masterdatakg/mms/id/sakila/column.customer.first_name,INVERSE]\r\n"
					+ "[http://in4mium.com/masterdatakg/mms/id/sakila/column.customer.first_name,http://in4mium.com/masterdatakg/schema/mms-ext#isDerivedFrom,http://in4mium.com/masterdatakg/mms/id/sakila/column.customer_list.name,INVERSE]\r\n"
					+ "[http://in4mium.com/masterdatakg/mms/id/sakila/column.customer_list.name,http://rdf.cdisc.org/mms#context,http://in4mium.com/masterdatakg/mms/id/sakila/dataset.customer_list,DIRECT]\r\n"
					+ "]\r\n"
					+ "Path=[[http://in4mium.com/masterdatakg/schema/mms-ext#Sensitive_PII,http://in4mium.com/masterdatakg/schema/mms-ext#dataElementSensitivity,http://in4mium.com/masterdatakg/mms/id/sakila/column.customer.last_name,INVERSE]\r\n"
					+ "[http://in4mium.com/masterdatakg/mms/id/sakila/column.customer.last_name,http://in4mium.com/masterdatakg/schema/mms-ext#isDerivedFrom,http://in4mium.com/masterdatakg/mms/id/sakila/column.customer_list.name,INVERSE]\r\n"
					+ "[http://in4mium.com/masterdatakg/mms/id/sakila/column.customer_list.name,http://rdf.cdisc.org/mms#context,http://in4mium.com/masterdatakg/mms/id/sakila/dataset.customer_list,DIRECT]\r\n"
					+ "]\r\n"
					+ "", paths.toString());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
}
