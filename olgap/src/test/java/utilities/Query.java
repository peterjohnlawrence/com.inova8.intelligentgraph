/*
 * inova8 2020
 */
package utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.iterator.QueryContextIteration;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.config.SailConfigException;
import org.eclipse.rdf4j.sail.lucene.LuceneSail;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.query.explanation.*;

import com.inova8.intelligentgraph.sail.IntelligentGraphConfig;
import com.inova8.intelligentgraph.sail.IntelligentGraphFactory;
import com.inova8.intelligentgraph.sail.IntelligentGraphSail;

import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class Query.
 */
public class Query {
	
	/**
	 * Creates the native lucene intelligent graph repository.
	 *
	 * @param dir the dir
	 * @return the org.eclipse.rdf 4 j.repository. repository
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SailConfigException the sail config exception
	 */
	public static org.eclipse.rdf4j.repository.Repository createNativeLuceneIntelligentGraphRepository(String dir) throws IOException, SailConfigException {
		File dataDir = new File(dir);
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
		
		LuceneSail lucenesail = new LuceneSail();
		lucenesail.setParameter(LuceneSail.LUCENE_RAMDIR_KEY, "true");

		Sail baseSail = new NativeStore(dataDir);		
		lucenesail.setBaseSail(baseSail);
		intelligentGraphSail.setBaseSail(lucenesail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		return workingRep;
	}
	
	/**
	 * Creates the native intelligent graph repository.
	 *
	 * @param dir the dir
	 * @return the org.eclipse.rdf 4 j.repository. repository
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SailConfigException the sail config exception
	 */
	public static org.eclipse.rdf4j.repository.Repository createNativeIntelligentGraphRepository(String dir) throws IOException, SailConfigException {
		File dataDir = new File(dir);
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);
		//IntelligentGraphSail intelligentGraphSail = new IntelligentGraphSail();		
		

		Sail baseSail = new NativeStore(dataDir);		
		intelligentGraphSail.setBaseSail(baseSail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		return workingRep;
	}
	
	/**
	 * Creates the native repository.
	 *
	 * @param dir the dir
	 * @return the org.eclipse.rdf 4 j.repository. repository
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SailConfigException the sail config exception
	 */
	public static org.eclipse.rdf4j.repository.Repository createNativeRepository(String dir) throws IOException, SailConfigException {
		File dataDir = new File(dir);
		FileUtils.deleteDirectory(dataDir);
		Sail baseSail = new NativeStore(dataDir);		

		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(baseSail);
		return workingRep;
	}
	
	/**
	 * Creates the memory intelligent graph repository.
	 *
	 * @param dir the dir
	 * @return the org.eclipse.rdf 4 j.repository. repository
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws SailConfigException the sail config exception
	 */
	public static org.eclipse.rdf4j.repository.Repository createMemoryIntelligentGraphRepository(String dir) throws IOException, SailConfigException {
		File dataDir = new File(dir);
		FileUtils.deleteDirectory(dataDir);
		
		IntelligentGraphConfig intelligentGraphConfig = new IntelligentGraphConfig();
		IntelligentGraphFactory intelligentGraphFactory = new IntelligentGraphFactory();
		IntelligentGraphSail intelligentGraphSail= (IntelligentGraphSail)intelligentGraphFactory.getSail(intelligentGraphConfig);

		Sail baseSail = new MemoryStore();		

		intelligentGraphSail.setBaseSail(baseSail);
		org.eclipse.rdf4j.repository.Repository workingRep = new SailRepository(intelligentGraphSail);
		return workingRep;
	}
	
	/**
	 * Adds the file.
	 *
	 * @param workingRep the working rep
	 * @param dataFilename the data filename
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RDFParseException the RDF parse exception
	 * @throws UnsupportedRDFormatException the unsupported RD format exception
	 * @throws RepositoryException the repository exception
	 */
	public static void addFile(org.eclipse.rdf4j.repository.Repository workingRep, String dataFilename)
			throws FileNotFoundException, IOException, RDFParseException, UnsupportedRDFormatException,
			RepositoryException {
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.TURTLE);
		RepositoryConnection conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null),iri("file://"+dataFilename));
	}
	
	/**
	 * Adds the RDF file.
	 *
	 * @param workingRep the working rep
	 * @param dataFilename the data filename
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws RDFParseException the RDF parse exception
	 * @throws UnsupportedRDFormatException the unsupported RD format exception
	 * @throws RepositoryException the repository exception
	 */
	public static void addRDFFile(org.eclipse.rdf4j.repository.Repository workingRep, String dataFilename)
			throws FileNotFoundException, IOException, RDFParseException, UnsupportedRDFormatException,
			RepositoryException {
		InputStream dataInput = new FileInputStream(dataFilename);
		Model dataModel = Rio.parse(dataInput, "", RDFFormat.RDFXML);
		RepositoryConnection conn = workingRep.getConnection();
		conn.add(dataModel.getStatements(null, null, null),iri("file://"+dataFilename));
	}
	
	/**
	 * Run boolean.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the boolean
	 */
	public static Boolean runBoolean(RepositoryConnection conn, String queryString) {
		BooleanQuery query = conn.prepareBooleanQuery(queryString);
		Boolean result = query.evaluate();
		return result;
	}

	/**
	 * Run query.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runQuery(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		QueryContext queryContext = new QueryContext();
		queryContext.setAttribute("test", conn);
		QueryContextIteration contextResults = new QueryContextIteration(query.evaluate(), queryContext);
		StringBuilder aResult = new StringBuilder();
		while (contextResults.hasNext()) {
	
			BindingSet solution = contextResults.next();
			Set<String> bindingNames = solution.getBindingNames();
			if(bindingNames.size()==1) {
				for (String bindingName : bindingNames) {
					if (solution.getValue(bindingName) != null)
						aResult.append(solution.getValue(bindingName).stringValue());
				}
			}else {
			for (String bindingName : bindingNames) {
				if (solution.getValue(bindingName) != null)
					aResult.append(bindingName).append("=").append(solution.getValue(bindingName).stringValue())
							.append(";");
			}
			aResult.append("\r\n");
			}
		}
		contextResults.close();
		return aResult.toString();
	}

	/**
	 * Run SPARQL.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runSPARQL(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		StringBuilder aResult = new StringBuilder();
		try(TupleQueryResult result = query.evaluate()){
			for (BindingSet bindingSet: result) {
				Set<String> bindingNames = bindingSet.getBindingNames();
				for (String bindingName : bindingNames) {
					if (bindingSet.getValue(bindingName) != null)
						aResult.append(bindingName).append("=").append(bindingSet.getValue(bindingName).stringValue())
								.append(";");
				}
				aResult.append("\r\n");
			}
			
		}
		return aResult.toString();
	}
	
	/**
	 * Parses the SPARQL.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String parseSPARQL(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		return query.toString();
	}
	public static String explainSPARQL(RepositoryConnection conn, String queryString) {
		TupleQuery query = conn.prepareTupleQuery(queryString);
		 Explanation explanation = query.explain(Explanation.Level.Optimized);
		return explanation.toString();
	}
	/**
	 * Run CONSTRUCT.
	 *
	 * @param conn the conn
	 * @param queryString the query string
	 * @return the string
	 */
	public static String runCONSTRUCT(RepositoryConnection conn, String queryString) {
		GraphQuery query = conn.prepareGraphQuery(queryString);
		StringBuilder aResult = new StringBuilder();
		try(GraphQueryResult result = query.evaluate()){
			while(result.hasNext()) {
				Statement nextResult = result.next();
				aResult.append(nextResult.toString()).append("\n");
			}

			
		}catch(Exception e){
			return aResult.toString();
		}
		return aResult.toString();
	}

	/**
	 * Removes the white spaces.
	 *
	 * @param input the input
	 * @return the string
	 */
	 static String removeWhiteSpaces(String input) {
	    return input.replaceAll("\\s+", "");
	    //return input;
	}

	/**
	 * Assert equals WO spaces.
	 *
	 * @param actual the actual
	 * @param expected the expected
	 */
	 public static  void assertEqualsWOSpaces(String actual, String expected){
			assertEquals(removeWhiteSpaces(actual), removeWhiteSpaces(expected));
	}

}
