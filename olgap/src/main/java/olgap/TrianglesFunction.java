package olgap;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class TrianglesFunction  implements Function{
	public RepositoryConnection conn;
	public Repository workingRep;
	public TrianglesFunction() {
		super();
		workingRep = new SailRepository(new MemoryStore());
		workingRep.init();
	}
	public static final String NAMESPACE = "http://inova8.com/olgap/";
	@Override
	public String getURI() {
		return NAMESPACE + "triangles";
	}
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		Repository rep = new SPARQLRepository(args[0].stringValue());
		rep.init();
		
		
		//QueryContext queryContext;
		//queryContext=QueryContext.getQueryContext();
		//TripleSource tripleSource = queryContext.getQueryPreparer().getTripleSource();
		//ParsedUpdate parsedUpdate = new ParsedUpdate("insert data{graph <http://test> {	<http://exampleSub> <http://examplePred> <http://exampleObj> ");
		//Update update = queryContext.getQueryPreparer().prepare(parsedUpdate);
		//update.execute();
		
		try (RepositoryConnection workingConn = workingRep.getConnection();RepositoryConnection conn = rep.getConnection(); ) {
			
			//SPARQLRepository updaterepository = new SPARQLRepository(args[0].stringValue()+"/statements");
			//Update update =updaterepository.getConnection().prepareUpdate(QueryLanguage.SPARQL,"insert data{graph <http://test> {<http://exampleSub> <http://examplePred> <http://exampleObj> }}");
			//update.execute();
			
			String graphQueryString = "CONSTRUCT{?vertex <label:connect> ?neighbor.}\r\n" 
					+ "WHERE{\r\n"
					+ "SELECT ?vertex ?neighbor\r\n" 
					+ "WHERE{{?vertex ?p ?neighbor.}\r\n"
					+ "    UNION{?neighbor ?p ?vertex.}FILTER(STR(?vertex) <STR(?neighbor))}\r\n" 
					+ "}";
			GraphQueryResult graphResult = conn.prepareGraphQuery(graphQueryString).evaluate();
			
			Resource graph = workingRep.getValueFactory().createIRI("ng:temp");
			while (graphResult.hasNext()) {
				Statement st = graphResult.next();
				// ... do something with the resulting statement here.
				workingConn.add(st, graph);
			}

			String queryString = "SELECT(COUNT(DISTINCT*) AS ?triangles)\r\n" 
					+ "WHERE{\r\n" 
					+ "  {\r\n" + "GRAPH<ng:temp>   {\r\n"
					+ "      ?x?p?y.\r\n" 
					+ "      ?y?p?z.\r\n" 
					+ "      ?x?p?z.\r\n" 
					+ "    }\r\n" 
					+ "  }\r\n" 
					+ "}";

			TupleQuery query = workingConn.prepareTupleQuery(queryString);

			try (TupleQueryResult result = query.evaluate()) {
				// we just iterate over all solutions in the result...
				while (result.hasNext()) {
					BindingSet solution = result.next();
					return valueFactory.createLiteral( ((SimpleLiteral) solution.getValue("triangles")).intValue());

				}
			}
		}
		return null;

	}

}
