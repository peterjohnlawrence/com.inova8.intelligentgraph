/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.intelligentGraphRepository.*;
import com.inova8.intelligentgraph.model.*;
import com.inova8.intelligentgraph.vocabulary.*;

import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GettingStarted_Tests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/GettingStarted_Tests/");
		
		RepositoryConnection conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/gettingStarted/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		source = IntelligentGraphRepository.create(workingRep);

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}

	@Test
	@Order(1)
	void gettingStarted_1() {

		try {
			source.removeGraph("<http://inova8.com/intelligentgraph/gettingStarted>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/gettingStarted>");
			Thing myCountry = graph.getThing(":MyCountry");
			myCountry.addFact(":sales", "1");
			myCountry.addFact(":sales", "2");
			myCountry.addFact(":sales", "3");
			myCountry.addFact(":sales", "4");
			myCountry.addFact(":sales", "5");
			for (Resource sale : myCountry.getFacts(":sales")){
				   System.out.println(sale.doubleValue() ); 
				}
			double totalSales=0;
			int count=0;
			for (Resource sale : myCountry.getFacts(":sales")){
			   totalSales+=sale.doubleValue() ;
			   count++;
			}
			double averageSales = totalSales/count;

			assertEquals(3.0,averageSales);
			String averageSalesScript = "double totalSales=0; int count=0;for(sales in _this.getFacts(':sales')){totalSales +=  sales.doubleValue();count++}; return totalSales/count;";
			myCountry.addFact(":averageSales", averageSalesScript, SCRIPT.GROOVY) ;
			assertEquals(3.0, myCountry.getFact(":averageSales").doubleValue());
			myCountry.addFact(":sales", "500");
			assertEquals(85.83333333333333, myCountry.getFact(":averageSales").doubleValue());
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}


}
