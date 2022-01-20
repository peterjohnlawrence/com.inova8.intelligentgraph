/*
 * inova8 2020
 */
package tutorialExample;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.*;
import com.inova8.intelligentgraph.model.*;
import com.inova8.intelligentgraph.vocabulary.*;

//import com.inova8.intelligentgraph.intelligentGraphRepository.Graph;
//import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
//import com.inova8.intelligentgraph.pathCalc.Thing;
//import com.inova8.intelligentgraph.pathQLModel.Resource;
//import com.inova8.intelligentgraph.vocabulary.SCRIPT;

import static org.eclipse.rdf4j.model.util.Values.literal;
import utilities.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PlantDemoTests {

	private static IntelligentGraphRepository source;
	static org.eclipse.rdf4j.repository.Repository workingRep ;
	static RepositoryConnection conn;
	static RepositoryConnection conn1;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		//workingRep = Query.createNativeLuceneIntelligentGraphRepository("src/test/resources/datadir/PlantDemo_Tests/");
		workingRep = Query.createNativeIntelligentGraphRepository("src/test/resources/datadir/PlantDemo_Tests/");
		//workingRep = Query.createMemoryIntelligentGraphRepository("src/test/resources/datadir/PlantDemo_Tests/");
		//workingRep = Query.createNativeRepository("src/test/resources/datadir/PlantDemo_Tests/");
		conn = workingRep.getConnection();
		conn.setNamespace("", "http://inova8.com/intelligentgraph/plantdemo/");
		conn.setNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		conn.setNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		conn.setNamespace("SCRIPT","http://inova8.com/script/");
		
		source = IntelligentGraphRepository.create(workingRep);
		 conn1 = source.getRepository().getConnection();

	}
	@AfterAll
	static void closeClass() throws Exception {
		//conn.close();
	}

	@Test
	@Order(1)
	void plantDemo_PathQL() {

		try {
			source.removeGraph("<http://inova8.com/intelligentgraph/plantdemo/>");
			Graph graph = source.addGraph("<http://inova8.com/intelligentgraph/plantdemo/>");
			Thing unit4 = graph.getThing(":Unit.4");
			Thing feed41 = graph.getThing(":Feed.4.1");
			Thing product41 = graph.getThing(":Product.4.1");
			Thing product42 = graph.getThing(":Product.4.2");
			Thing flare41 = graph.getThing(":Flare.4.1");

			unit4.addFact(":hasFeed", feed41.addFact(":hasVolumeFlow", "60.3",XSD.DOUBLE).addFact(":hasDensity", "0.78",XSD.DOUBLE));
			unit4.addFact(":hasProduct", product41.addFact(":hasVolumeFlow", "20.7",XSD.DOUBLE).addFact(":hasDensity", "0.65",XSD.DOUBLE));
			unit4.addFact(":hasProduct", product42.addFact(":hasVolumeFlow", "34.0",XSD.DOUBLE).addFact(":hasDensity", "0.91",XSD.DOUBLE));
			unit4.addFact(":hasProduct", flare41.addFact(":hasVolumeFlow", "12.4",XSD.DOUBLE).addFact(":hasDensity", "0.10",XSD.DOUBLE));
			
			@SuppressWarnings("unused")
			Thing massFlow = graph.getThing(":MassFlow").addFact("<http://inova8.com/script/scriptCode>",
					"_this.getFact(':hasVolumeFlow').doubleValue()* _this.getFact(':hasDensity').doubleValue();",
					SCRIPT.GROOVY); 
			assertEquals(60.3,feed41.getFact(":hasVolumeFlow").doubleValue());
			//assertEquals("[http://inova8.com/intelligentgraph/plantdemo/Product.4.1;http://inova8.com/intelligentgraph/plantdemo/Product.4.2;http://inova8.com/intelligentgraph/plantdemo/Flare.4.1;]",unit4.getFacts(":hasProduct"));
			//assertEquals(60.3,unit4.getFacts(":hasProduct|:hasFeed"));
			for( com.inova8.intelligentgraph.model.Resource stream : unit4.getFacts(":hasProduct|:hasFeed") ) {
				stream.addFact(":hasMassFlow", "<:MassFlow>",SCRIPT.GROOVY );
			}
			assertEquals(47.034,feed41.getFact(":hasMassFlow").doubleValue());
			unit4.addFact(":totalProduction", "var totalProduction=_this.getFacts(':hasProduct/:hasMassFlow').total(); return totalProduction; ",SCRIPT.GROOVY);
			unit4.addFact(":totalFeed", "var totalFeed= _this.getFacts(':hasFeed/:hasMassFlow').total(); return totalFeed ",SCRIPT.GROOVY);
			unit4.addFact(":massBalance", "_this.getFact(':totalProduction').doubleValue()-_this.getFact(':totalFeed').doubleValue()  ",SCRIPT.GROOVY);
			unit4.addFact(":massBalanceRatio", "_this.getFact(':massBalance').doubleValue()/_this.getFact(':totalFeed').doubleValue()  ",SCRIPT.GROOVY);
			
			assertEquals(45.635000000000005,unit4.getFact(":totalProduction").doubleValue());
			assertEquals(47.034,unit4.getFact(":totalFeed").doubleValue());
			assertEquals(-1.3989999999999938,unit4.getFact(":massBalance").doubleValue());
			assertEquals(-0.029744440192201255,unit4.getFact(":massBalanceRatio").doubleValue());

			@SuppressWarnings("unused")
			Thing massYield = graph.getThing(":MassYield").addFact("<http://inova8.com/script/scriptCode>",
					"_this.getFact(':hasMassFlow').doubleValue()/ _this.getFact('^:hasProduct/:totalProduction').doubleValue();",
					SCRIPT.GROOVY);
			for( com.inova8.intelligentgraph.model.Resource product : unit4.getFacts(":hasProduct") ) {
 				product.addFact(":hasMassYield", "<:MassYield>",SCRIPT.GROOVY );
			}
			
			assertEquals(0.2948394872356743,product41.getFact(":hasMassYield").doubleValue());
			
		} catch (Exception e) {
			assertEquals("", e.getMessage());
			e.printStackTrace();
		}
	}
		@Test
		@Order(2)
		void plantIIoTDemo_PathQL() {

			try {

				source.removeGraph("<http://inova8.com/intelligentgraph/plantdemo/>");

				Graph graph = source.openGraph("<http://inova8.com/intelligentgraph/plantdemo/>");
				Thing unit4 = graph.getThing(":Unit.4");
				Thing feed41 = graph.getThing(":Feed.4.1");
				Thing product41 = graph.getThing(":Product.4.1");
				Thing product42 = graph.getThing(":Product.4.2");
				Thing flare41 = graph.getThing(":Flare.4.1");
								
				unit4.addFact(":hasFeed", feed41.addFact(":hasVolumeFlow", "_this.getSignal(\"<SEEQ://localhost:34216/api/signals/C8BB6BE4-1CC7-4803-B785-C383ACC31572>\", _customQueryOptions).doubleValue()",SCRIPT.GROOVY).addFact(":hasDensity", "0.85",XSD.DOUBLE));
				unit4.addFact(":hasProduct", product41.addFact(":hasVolumeFlow", "_this.getSignal(\"<SEEQ://localhost:34216/api/signals/1C6EE32D-25D3-4C20-AACD-7C8B2BF44D4F>\", _customQueryOptions).doubleValue()",SCRIPT.GROOVY).addFact(":hasDensity", "0.55",XSD.DOUBLE));
				unit4.addFact(":hasProduct", product42.addFact(":hasVolumeFlow", "_this.getSignal(\"<SEEQ://localhost:34216/api/signals/FF64D48B-B680-43E2-878F-17EA0E8DA3AA>\", _customQueryOptions).doubleValue()",SCRIPT.GROOVY).addFact(":hasDensity", "0.91",XSD.DOUBLE));
				unit4.addFact(":hasProduct", flare41.addFact(":hasVolumeFlow", "_this.getSignal(\"<SEEQ://localhost:34216/api/signals/88A59116-DA7E-467E-B83F-7D2E130ED64D>\", _customQueryOptions).doubleValue()",SCRIPT.GROOVY).addFact(":hasDensity", "0.10",XSD.DOUBLE));
				
				@SuppressWarnings("unused")
				Thing massFlow = graph.getThing(":MassFlow").addFact("<http://inova8.com/script/scriptCode>",
						"_this.getFact(':hasVolumeFlow', _customQueryOptions).doubleValue()* _this.getFact(':hasDensity', _customQueryOptions).doubleValue();",
						SCRIPT.GROOVY); 
				for( com.inova8.intelligentgraph.model.Resource stream : unit4.getFacts(":hasProduct|:hasFeed") ) {
					stream.addFact(":hasMassFlow", "<:MassFlow>",SCRIPT.GROOVY );
				}

				unit4.addFact(":totalProduction", "var totalProduction=_this.getFacts(':hasProduct/:hasMassFlow', _customQueryOptions).total(); return totalProduction; ",SCRIPT.GROOVY);
				unit4.addFact(":totalFeed", "var totalFeed= _this.getFacts(':hasFeed/:hasMassFlow', _customQueryOptions).total(); return totalFeed ",SCRIPT.GROOVY);
				unit4.addFact(":massBalance", "_this.getFact(':totalProduction', _customQueryOptions).doubleValue()-_this.getFact(':totalFeed', _customQueryOptions).doubleValue()  ",SCRIPT.GROOVY);
				unit4.addFact(":massBalanceRatio", "_this.getFact(':massBalance', _customQueryOptions).doubleValue()/_this.getFact(':totalFeed', _customQueryOptions).doubleValue()  ",SCRIPT.GROOVY);
				
				CustomQueryOptions customQueryOptions = new CustomQueryOptions();
				customQueryOptions.add("end", literal("2021-08-02T10:00:00Z",XSD.DATETIMESTAMP));
				assertEquals(42.778,feed41.getFact(":hasVolumeFlow", customQueryOptions).doubleValue());
				assertEquals(27.165,product41.getFact(":hasVolumeFlow", customQueryOptions).doubleValue());
				assertEquals(23.918333333333333,product42.getFact(":hasVolumeFlow", customQueryOptions).doubleValue());
				assertEquals(0.40631,flare41.getFact(":hasVolumeFlow", customQueryOptions).doubleValue());
				
				assertEquals(36.747064333333334,unit4.getFact(":totalProduction", customQueryOptions).doubleValue());
				assertEquals(36.3613,unit4.getFact(":totalFeed", customQueryOptions).doubleValue());
				assertEquals(0.38576433333333426,unit4.getFact(":massBalance", customQueryOptions).doubleValue());
				assertEquals(0.010609200807818594,unit4.getFact(":massBalanceRatio", customQueryOptions).doubleValue());

				@SuppressWarnings("unused")
				Thing massYield = graph.getThing(":MassYield").addFact("<http://inova8.com/script/scriptCode>",
						"_this.getFact(':hasMassFlow', _customQueryOptions).doubleValue()/ _this.getFact('^:hasProduct/:totalProduction', _customQueryOptions).doubleValue();",
						SCRIPT.GROOVY);
				for( com.inova8.intelligentgraph.model.Resource product : unit4.getFacts(":hasProduct") ) {
					product.addFact(":hasMassYield", "<:MassYield>",SCRIPT.GROOVY );
				}
				
				assertEquals(0.4065834991462765,product41.getFact(":hasMassYield", customQueryOptions).doubleValue());
				
				String result = Query.runQuery(conn, "PREFIX plantdemo: <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#/>\r\n"
						+ "SELECT  ?g ?s ?p ?o ?end {VALUES(?s){(plantdemo:Feed.4.1)(plantdemo:Product.4.1)(plantdemo:Product.4.2)(plantdemo:Flare.4.1)} \r\n"
						+ "                 VALUES(?p){(plantdemo:hasVolumeFlow)} \r\n"
						+ "                 VALUES(?end){(\"2021-08-02T10:00:00Z\"^^xsd:dateTimeStamp)(\"2021-08-03T10:00:00Z\"^^xsd:dateTimeStamp)}\r\n"
						+ "                graph ?g {?s ?p ?o}}");
				assertEquals("p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Feed.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=42.778;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Feed.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=42.431666666666665;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=27.165;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=27.136666666666667;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.2;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=23.918333333333333;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.2;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=24.605333333333334;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Flare.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=0.40631;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Flare.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=0.3337;\r\n"
						+ "",result
						);
			} catch (Exception e) {
				assertEquals("",e.getMessage());
			}
	}
		@Test
		@Order(3)
		void plantIIoTDemo_SPARQL() {
			/*
plantdemo

CLEAR  GRAPH <http://inova8.com/intelligentgraph/plantdemo/>;

PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX SCRIPT: <http://inova8.com/script/>
INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{
  :Feed.4.1 :hasVolumeFlow "_this.getSignal(\"<SEEQ://host.docker.internal:34216/api/signals/C8BB6BE4-1CC7-4803-B785-C383ACC31572>\", _customQueryOptions).doubleValue()"^^SCRIPT:groovy  ; 
			:hasDensity 0.85 ;
			:hasMassFlow "<:MassFlow>"^^SCRIPT:groovy .
  :Product.4.1 :hasVolumeFlow "_this.getSignal(\"<SEEQ://host.docker.internal:34216/api/signals/1C6EE32D-25D3-4C20-AACD-7C8B2BF44D4F>\", _customQueryOptions).doubleValue()"^^SCRIPT:groovy ; 
			:hasDensity 0.55 ; 
			:hasMassFlow "<:MassFlow>"^^SCRIPT:groovy .
  :Product.4.2 :hasVolumeFlow "_this.getSignal(\"<SEEQ://host.docker.internal:34216/api/signals/FF64D48B-B680-43E2-878F-17EA0E8DA3AA>\", _customQueryOptions).doubleValue()"^^SCRIPT:groovy  ; 
			:hasDensity 0.91 ; 
			:hasMassFlow "<:MassFlow>"^^SCRIPT:groovy .
  :Flare.4.1 :hasVolumeFlow "_this.getSignal(\"<SEEQ://host.docker.internal:34216/api/signals/88A59116-DA7E-467E-B83F-7D2E130ED64D>\", _customQueryOptions).doubleValue()"^^SCRIPT:groovy  ; 
			:hasDensity 0.10 ; 
			:hasMassFlow "<:MassFlow>"^^SCRIPT:groovy .
  :MassFlow SCRIPT:scriptCode "_this.getFact(':hasVolumeFlow', _customQueryOptions).doubleValue()* _this.getFact(':hasDensity', _customQueryOptions).doubleValue();"^^SCRIPT:groovy .   
}};

PREFIX plantdemo: <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#/>
SELECT ?s ?p ?o ?end {
	VALUES(?s){(plantdemo:Feed.4.1)(plantdemo:Product.4.1)(plantdemo:Product.4.2)(plantdemo:Flare.4.1)}
	VALUES(?p){(plantdemo:hasVolumeFlow)(plantdemo:hasMassFlow)}
	VALUES(?end){("2021-08-02T10:00:00Z"^^xsd:dateTimeStamp)("2021-08-03T10:00:00Z"^^xsd:dateTimeStamp)}
	?s ?p ?o
}


PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX SCRIPT: <http://inova8.com/script/>
INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{
  :Unit.4	:hasFeed :Feed.4.1  ;
			:hasProduct :Product.4.1  ;
			:hasProduct :Product.4.2  ;
			:hasProduct :Flare.4.1  ;
			:totalProduction  "var totalProduction= _this.getFacts(':hasProduct/:hasMassFlow', _customQueryOptions).total(); return totalProduction;"^^SCRIPT:groovy ;
			:totalFeed  "var totalFeed=  _this.getFacts(':hasFeed/:hasMassFlow', _customQueryOptions).total(); return totalFeed"^^SCRIPT:groovy ; 
			:massBalance  "_this.getFact(':totalProduction', _customQueryOptions).doubleValue()-_this.getFact(':totalFeed', _customQueryOptions).doubleValue()"^^SCRIPT:groovy ; 
			:massBalanceRatio  "_this.getFact(':massBalance', _customQueryOptions).doubleValue()/_this.getFact(':totalFeed', _customQueryOptions).doubleValue()"^^SCRIPT:groovy . 
}};

PREFIX plantdemo: <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#/>
SELECT ?s ?p ?o ?end {
 	VALUES(?s){(plantdemo:Unit.4)}
 	VALUES(?p){(plantdemo:totalFeed)(plantdemo:totalProduction)(plantdemo:massBalance)(plantdemo:massBalanceRatio)}
	VALUES(?end){("2021-08-02T10:00:00Z"^^xsd:dateTimeStamp)("2021-08-03T10:00:00Z"^^xsd:dateTimeStamp)}
	?s ?p ?o
} order by ?end

PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX SCRIPT: <http://inova8.com/script/>
INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{
  :MassYield 	SCRIPT:scriptCode "_this.getFact(':hasMassFlow', _customQueryOptions).doubleValue()/ _this.getFact('^:hasProduct/:totalProduction', _customQueryOptions).doubleValue();"^^SCRIPT:groovy .
  :Product.4.1	:hasMassYield  "<:MassYield>"^^SCRIPT:groovy .
  :Product.4.2	:hasMassYield  "<:MassYield>"^^SCRIPT:groovy .
  :Flare.4.1	:hasMassYield  "<:MassYield>"^^SCRIPT:groovy .
  }}; 

PREFIX plantdemo: <http://inova8.com/intelligentgraph/plantdemo/>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#/>
SELECT ?s ?p ?o ?end {
	VALUES(?s){(plantdemo:Product.4.1)}
	VALUES(?p){(plantdemo:hasMassFlow)(plantdemo:hasMassYield)}
	VALUES(?end){("2021-08-02T10:00:00Z"^^xsd:dateTimeStamp)("2021-08-03T10:00:00Z"^^xsd:dateTimeStamp)}
	?s ?p ?o
} order by ?end


*/
			
			
			
			try {
				RepositoryConnection conn = workingRep.getConnection();
				Update updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"CLEAR  GRAPH <http://inova8.com/intelligentgraph/plantdemo/>"
						);
				updateQuery.execute();
				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :Unit.4 :hasFeed :Feed.4.1  .\r\n"
						+ "  :Unit.4 :hasProduct :Product.4.1  .\r\n"
						+ "  :Unit.4 :hasProduct :Product.4.2  .\r\n"
						+ "  :Unit.4 :hasProduct :Flare.4.1  .\r\n"
						+ "  :Feed.4.1 :hasVolumeFlow \"_this.getSignal(\\\"<SEEQ://host.docker.internal:34216/api/signals/C8BB6BE4-1CC7-4803-B785-C383ACC31572>\\\", _customQueryOptions).doubleValue()\"^^SCRIPT:groovy  ; :hasDensity 0.85 .\r\n"
						+ "  :Product.4.1 :hasVolumeFlow \"_this.getSignal(\\\"<SEEQ://host.docker.internal:34216/api/signals/1C6EE32D-25D3-4C20-AACD-7C8B2BF44D4F>\\\", _customQueryOptions).doubleValue()\"^^SCRIPT:groovy ; :hasDensity 0.55 .\r\n"
						+ "  :Product.4.2 :hasVolumeFlow \"_this.getSignal(\\\"<SEEQ://host.docker.internal:34216/api/signals/FF64D48B-B680-43E2-878F-17EA0E8DA3AA>\\\", _customQueryOptions).doubleValue()\"^^SCRIPT:groovy  ; :hasDensity 0.91 .\r\n"
						+ "  :Flare.4.1 :hasVolumeFlow \"_this.getSignal(\\\"<SEEQ://host.docker.internal:34216/api/signals/88A59116-DA7E-467E-B83F-7D2E130ED64D>\\\", _customQueryOptions).doubleValue()\"^^SCRIPT:groovy  ; :hasDensity 0.10 .\r\n"
						+ "}}");
				updateQuery.execute();

				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :MassFlow SCRIPT:scriptCode \"_this.getFact(':hasVolumeFlow').doubleValue()* _this.getFact(':hasDensity').doubleValue();\"^^SCRIPT:groovy }}");
				updateQuery.execute();
				
				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :Feed.4.1 :hasMassFlow \"<:MassFlow>\"^^SCRIPT:groovy .\r\n"
						+ "  :Product.4.1 :hasMassFlow \"<:MassFlow>\"^^SCRIPT:groovy.\r\n"
						+ "  :Product.4.2 :hasMassFlow \"<:MassFlow>\"^^SCRIPT:groovy .\r\n"
						+ "  :Flare.4.1 :hasMassFlow \"<:MassFlow>\"^^SCRIPT:groovy .\r\n"
						+ "}}");
				updateQuery.execute();
				
				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :Unit.4  :totalProduction  \"var totalProduction= _this.getFacts(':hasProduct/:hasMassFlow').total(); return totalProduction;\"^^SCRIPT:groovy .\r\n"
						+ "  :Unit.4  :totalFeed  \"var totalFeed=  _this.getFacts(':hasFeed/:hasMassFlow').total(); return totalFeed\"^^SCRIPT:groovy . \r\n"
						+ "  :Unit.4  :massBalance  \"_this.getFact(':totalProduction').doubleValue()-_this.getFact(':totalFeed').doubleValue()\"^^SCRIPT:groovy . \r\n"
						+ "  :Unit.4  :massBalanceRatio  \"_this.getFact(':massBalance').doubleValue()/_this.getFact(':totalFeed').doubleValue()\"^^SCRIPT:groovy . \r\n"
						+ "}}");
				updateQuery.execute();

				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :MassYield SCRIPT:scriptCode \"_this.getFact(':hasMassFlow').doubleValue()/ _this.getFact('^:hasProduct/:totalProduction').doubleValue();\"^^SCRIPT:groovy .}}");
				updateQuery.execute();

				updateQuery = conn.prepareUpdate(QueryLanguage.SPARQL, 
						"PREFIX : <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX SCRIPT: <http://inova8.com/script/>\r\n"
						+ "INSERT DATA{GRAPH <http://inova8.com/intelligentgraph/plantdemo/>{\r\n"
						+ "  :Product.4.1  :hasMassYield  \"<:MassYield>\"^^SCRIPT:groovy .\r\n"
						+ "  :Product.4.2  :hasMassYield  \"<:MassYield>\"^^SCRIPT:groovy .\r\n"
						+ "  :Flare.4.1  :hasMassYield  \"<:MassYield>\"^^SCRIPT:groovy .\r\n"
						+ "}}");
				updateQuery.execute();
				String result = Query.runQuery(conn, "PREFIX plantdemo: <http://inova8.com/intelligentgraph/plantdemo/>\r\n"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#/>\r\n"
						+ "SELECT ?g ?s ?p ?o ?end {VALUES(?s){(plantdemo:Feed.4.1)(plantdemo:Product.4.1)(plantdemo:Product.4.2)(plantdemo:Flare.4.1)} \r\n"
						+ "                 VALUES(?p){(plantdemo:hasVolumeFlow)} \r\n"
						+ "                 VALUES(?end){(\"2021-08-02T10:00:00Z\"^^xsd:dateTimeStamp)(\"2021-08-03T10:00:00Z\"^^xsd:dateTimeStamp)}\r\n"
						+ "              graph ?g {?s ?p ?o}}");
				assertEquals("p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Feed.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=42.778;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Feed.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=42.431666666666665;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=27.165;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=27.136666666666667;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.2;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=23.918333333333333;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Product.4.2;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=24.605333333333334;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Flare.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-02T10:00:00Z;o=0.40631;\r\n"
						+ "p=http://inova8.com/intelligentgraph/plantdemo/hasVolumeFlow;s=http://inova8.com/intelligentgraph/plantdemo/Flare.4.1;g=http://inova8.com/intelligentgraph/plantdemo/;end=2021-08-03T10:00:00Z;o=0.3337;\r\n"
						+ "",result
						);
				
			} catch (Exception e) {
				assertEquals("", e.getMessage());
			}
		}


}
