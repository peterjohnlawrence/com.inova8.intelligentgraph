/*
 * inova8 2020
 */
package localPathCalc;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.evaluation.RepositoryTripleSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;

import utilities.Query;
/**
 * The Class PathQLTests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Local_AddGetDeleteFact_Test {
	
	
	/** The conn. */
	private static RepositoryConnection conn;
	
	/** The repository triple source. */
	static RepositoryTripleSource repositoryTripleSource;
	
	/** The source. */
	//private static PathQLRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/Local_AddGetDeleteFact_Test/");
		Query.addFile(workingRep, "src/test/resources/calc2graph.data.ttl");
		Query.addFile(workingRep, "src/test/resources/calc2graph.def.ttl");

		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/calc2graph/def/");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	}
	@AfterAll
	static void closeClass() throws Exception {
		conn.close();
	}

	@Test
	@Order(0)
	void ig_0() {

		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph1>");
			Thing myCountry = graph.getThing(":Country1");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			ResourceResults facts = myCountry.getFacts(":sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			//myCountry = graph.getThing(":Country1");////////////////////////////////////////////////////
			myCountry.deleteFacts(":sales[eq '3']");
			factsinrange = myCountry.getFacts(":sales[ge '2';lt '4']").count();
			assertEquals(1, factsinrange);
			//Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph1>");
			source.removeGraph("<http://inova8.com/calc2graph/testGraph1>");
			//assertEquals(true, closed);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}
	@Test
	@Order(1)
	void ig_1() {

		try {
			IntelligentGraphRepository source = IntelligentGraphRepository.create(workingRep);
			source.removeGraph("<http://inova8.com/calc2graph/testGraph2>");
			Graph graph = source.addGraph("<http://inova8.com/calc2graph/testGraph2>");
			Thing myCountry = graph.getThing(":Country2");
			myCountry.addFact(":Attribute@:sales", "1");
			myCountry.addFact(":Attribute@:sales", "2");
			myCountry.addFact(":Attribute@:sales", "3");
			myCountry.addFact(":Attribute@:sales", "4");
			ResourceResults facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			Integer factsinrange = facts.count();
			assertEquals(2, factsinrange);
			myCountry.deleteFacts(":Attribute@:sales[eq '3']");
			facts = myCountry.getFacts(":Attribute@:sales[ge '2';lt '4']");
			factsinrange = facts.count();
			assertEquals(1, factsinrange);
//			double totalSales = 0; 
//			int count = 0;
//			for(Resource sales : myCountry.getFacts(":Attribute@:sales")){
//				totalSales +=  sales.doubleValue();
//				count++;
//			};	
//			double averagesales= totalSales/count;
			String averageSalesScript = "totalSales=0; count=0;for(sales in _this.getFacts(\":Attribute@:sales\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			Resource averageSales = myCountry.getFact(":averageSales");
			assertEquals(2.3333333333333335, averageSales.doubleValue());
		//	Thing country3= myCountry.getThing(":Country3");
			Thing country3=graph.getThing(":Country3");
			String averageSalesScript3 = "totalSales=0; count=0; myCountry=_this.getThing(\":Country2\"); for(sales in myCountry.getFacts(\":Attribute@:sales\")){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			country3.addFact(":averageSales", averageSalesScript3, SCRIPT.GROOVY) ;
			Resource averageSales3 = myCountry.getFact(":averageSales");
			assertEquals(2.3333333333333335, averageSales3.doubleValue());
			Boolean closed =source.closeGraph("<http://inova8.com/calc2graph/testGraph2>");
			assertEquals(true, closed);
		} catch (Exception e) {
			assertEquals("", e.getMessage());
		}
	}	

}
