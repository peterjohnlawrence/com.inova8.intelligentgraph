/*
 * inova8 2020
 */
package com.inova8.olgap;
/*
 
 
#Find endpoints

select ?dataset ?endpoint  {
?dataset a odata4sparql:Dataset .
?dataset  odata4sparql:dataRepository ?dataRepostory .
 ?dataRepostory rep:repositoryImpl ?dataImplementation .
?dataImplementation sparql:query-endpoint ?endpoint.
}
#Find prefixes for endpoints

select ?dataset ?prefix  ?namespace  {
?dataset a odata4sparql:Dataset .
?dataset  odata4sparql:datasetPrefix ?datasetPrefix .
?datasetPrefix odata4sparql:prefix ?prefix .
?datasetPrefix odata4sparql:namespace ?namespace .
}

#Initiate FRONT

INSERT{GRAPH<http://front:0>{ ?frontNode <http://edge> ?startNode.
	 }}
WHERE {
		BIND(<http://in4mium.com/londontube/id/Mornington_Crescent> as ?startNode)
		BIND(<http://in4mium.com/londontube/id/Mornington_Crescent> as ?frontNode)
	 }
#Initiate BACK

INSERT{GRAPH<http://back:0>{ ?backNode <http://edge> ?endNode.
	 }}
WHERE {
		BIND(<http://in4mium.com/londontube/id/Oakleigh_Park> as ?endNode)
		BIND(<http://in4mium.com/londontube/id/Oakleigh_Park> as ?backNode)
	 }

 #Increment FRONT

INSERT{GRAPH<http://front:1>{ 
	?frontNode <http://edge> ?uuid .
    ?uuid  ?designation ?startNode;
	       <http://property> ?property.
}}
WHERE { 
	{	SERVICE <http://localhost:8080/rdf4j-server/repositories/tfl?distinct=true&infer=false> 
		{
			SELECT DISTINCT ?startNode ?frontNode (IF(BOUND(?predicate),<http://startNode>,<http://endNode>) as ?designation)   (COALESCE(?predicate,?inversePredicate ) as ?property) ?uuid
			{
				{	SELECT ?startNode 
					{	SERVICE <http://localhost:8080/rdf4j-server/repositories/test> 
						{	GRAPH <http://front:0>{ SELECT DISTINCT ?startNode	{?startNode <http://edge> ?o} } }
					}
				}
						{
#VALUES(?startNode){(<http://in4mium.com/londontube/id/Mornington_Crescent>)}
							?startNode ?predicate  ?frontNode .  FILTER(isIRI(?frontNode)) .
 FILTER (?predicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/connectsTo>))
						}UNION{
#VALUES(?startNode){(<http://in4mium.com/londontube/id/Mornington_Crescent>)}
							?frontNode  ?inversePredicate   ?startNode .
 FILTER (?inversePredicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/hasStationInZone>,<http://in4mium.com/londontube/ref/hasStationOnLine>,<http://in4mium.com/londontube/ref/connectsFrom>))
						}
        				BIND(UUID() as ?uuid)
			}
		}
	}
}

#Increment BACK

INSERT{GRAPH<http://back:1>{ 
	?backNode <http://edge> ?uuid . 
	?uuid ?designation ?endNode ;
			<http://property> ?property.
}}
WHERE {
	{	SERVICE <http://localhost:8080/rdf4j-server/repositories/tfl?distinct=true&infer=false> 
		{
			
			SELECT DISTINCT ?endNode ?backNode (IF(BOUND(?inversePredicate),<http://startNode>,<http://endNode>) as ?designation)   (COALESCE(?predicate,?inversePredicate ) as ?property) ?uuid
			{
				{	SELECT ?endNode 
					{	SERVICE <http://localhost:8080/rdf4j-server/repositories/test> 
						{	GRAPH <http://back:0>{ SELECT DISTINCT ?endNode	{?endNode <http://edge> ?o} } }
					}
				}
						{
#VALUES(?endNode){(<http://in4mium.com/londontube/id/Oakleigh_Park>)}
							?backNode  ?predicate   ?endNode .
 FILTER (?predicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/connectsTo>))
						}UNION{
#VALUES(?endNode){(<http://in4mium.com/londontube/id/Oakleigh_Park>)}
							?endNode ?inversePredicate  ?backNode .FILTER(isIRI(?backNode)).
 FILTER (?inversePredicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/hasStationInZone>,<http://in4mium.com/londontube/ref/hasStationOnLine>,<http://in4mium.com/londontube/ref/connectsFrom>))
						}
				BIND(UUID() as ?uuid)
			}
		}
	}
}
#Match front to back

SELECT distinct ?frontNode  ?priorFrontNode  ?priorBackNode{
	GRAPH <http://front:1>{
	    ?frontNode <http://edge> ?priorFrontNode .   
	    GRAPH <http://back:0>{
	    	?frontNode <http://edge> ?priorBackNode
	    }
	}
} LIMIT 1

#recover front path

SELECT  (3 as ?edge) ?subject ?property  ?object ?direct {
	{VALUES(?object){(<http://in4mium.com/londontube/id/Old_Street>)}
	GRAPH <http://front:2>{
	    { ?object <http://edge> ?uuid1 .
          ?uuid1  <http://startNode> ?subject ;  
			<http://property> ?property . BIND( true as ?direct)
	    } UNION 
	    { ?object <http://edge> ?uuid2 .
          ?uuid2  <http://endNode> ?subject ;  
			<http://property> ?property . BIND( false  as ?direct)
	    } 
	    } 
	}}

#recover back path

SELECT (6 as ?edge) ?subject ?property  ?object ?direct  {
	{VALUES(?subject){(<http://in4mium.com/londontube/id/Old_Street>)}
	GRAPH <http://back:2>{
	   	{ ?subject  <http://edge> ?uuid1 .
          ?uuid1  <http://endNode> ?object ;  
			<http://property> ?property . BIND( false as ?direct) 
	    } UNION 
	    { ?subject  <http://edge> ?uuid2 .
          ?uuid2  <http://startNode> ?object ;  
			<http://property> ?property . BIND( true  as ?direct)
	    } 
	}}}
*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.vocabulary.OLGAP;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.common.iteration.ConvertingIteration;
import org.eclipse.rdf4j.common.iteration.Iteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;

import static org.eclipse.rdf4j.model.util.Values.iri;



/**
 * The Class ShortestPathTupleFunction.
 */
public class ShortestPathTupleFunction implements InverseMagicProperty {
	
	/** The Constant logger. */
	private static final Logger logger   = LoggerFactory.getLogger(ShortestPathTupleFunction.class);
	
	/**
	 * The Class ResultsIterator.
	 */
	private final class ResultsIterator extends ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException> {
		
		/**
		 * Instantiates a new results iterator.
		 *
		 * @param iter the iter
		 */
		private ResultsIterator(Iteration<? extends BindingSet, ? extends QueryEvaluationException> iter) {
			super(iter);
			logger.info("Initiating ShortestPathTupleFunction");
		}
		
		/**
		 * Convert.
		 *
		 * @param bindings the bindings
		 * @return the list
		 * @throws QueryEvaluationException the query evaluation exception
		 */
		@Override
		protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
			List<Value> results = new ArrayList<Value>();
			for (String bindingName : bindings.getBindingNames()) {
				results.add(bindings.getValue(bindingName));
			}
			return results;
		}
	}
	
	/** The Constant hosted. */
	private static final boolean hosted = false;
	
	/** The Constant MAX_UPDATE_STRING. */
	private static final int MAX_UPDATE_STRING = 100000;

	/** The Constant LT. */
	protected static final String LT = "<";
	
	/** The Constant GT. */
	protected static final String GT = ">";
	

	/** The working rep. */
	public static org.eclipse.rdf4j.repository.Repository workingRep;


	/** The property path regex. */
	final String propertyPathRegex = "([!^]*)?\\(([^)]*)\\)";
	
	/** The property path. */
	String propertyPath;
	
	/** The pattern. */
	final Pattern pattern = Pattern.compile(propertyPathRegex, Pattern.MULTILINE);

	/** The predicate filter clause. */
	String predicateFilterClause;
	
	/** The not predicate filter clause. */
	String notPredicateFilterClause;
	
	/** The inverse predicate filter clause. */
	String inversePredicateFilterClause;
	
	/** The not inverse predicate filter clause. */
	String notInversePredicateFilterClause;
	
	/** The query context. */
	QueryContext queryContext;

	/** The service. */
	private String service;
	
	/** The olgap service. */
	private String olgapService;

	/** The results cache. */
	static private HashMap<Integer,Collection<BindingSet>> resultsCache = new HashMap<Integer,Collection<BindingSet>>();
	
	/**
	 * Instantiates a new shortest path tuple function.
	 */
	public ShortestPathTupleFunction() {
		super();
		/*		
				Alternative to memory store for exceptionally large path searches, if required.
				String folder = System.getProperty("user.home") +"\\rdf4j\\" ;
				File dataDir = new File(folder);
				dataDir.setWritable(true);
				dataDir.setReadable(true);
				workingRep = new SailRepository(new NativeStore(dataDir,"cspo,spoc"));
		*/	
		if(hosted) {
			//Use a host triplestore for the path manipulation
			String rdf4jServer = "http://localhost:8070/rdf4j-server/";
			String repositoryID = "olgap";
			workingRep = new HTTPRepository(rdf4jServer, repositoryID);		
			olgapService ="SERVICE <" + rdf4jServer + "repositories/" + repositoryID + "?distinct=true&infer=false>";
			logger.info("Shortestpath hosted at:" + olgapService);
		}else {
			//Use a in-memory triplestore for the path manipulation, however the SPARQ:L cannot use SERVICE call backs to this memory store
			workingRep = new SailRepository(new MemoryStore());
			logger.info("Shortestpath hosted in memory");
		}	
		workingRep.init();
	}
	
	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAP.SHORTESTPATH;
	}
	
	/**
	 * Evaluate.
	 *
	 * @param valueFactory the value factory
	 * @param args the args
	 * @return the closeable iteration{@code<? extends list<? extends value>, query evaluation exception>}
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args) throws QueryEvaluationException {
		if (args.length != 5) {
			throw new ValueExprEvaluationException(
					OLGAP.SHORTESTPATH + " requires" + "exactly 5 arguments: service, start, end, path, max. Got "
							+ args.length);
		}
//		queryContext=QueryContext.getQueryContext();
//		TripleSource tripleSource = this.queryContext.getQueryPreparer().getTripleSource();
		
		service = "SERVICE <" + args[0].toString() + "?distinct=true&infer=false>";

		Value startNode = args[1];
		Value endNode = args[2];
		prepareFilters(((Literal) args[3]).getLabel());
		final int maxPathLength = (((Literal) (args[4])).intValue());
		Integer queryHash = args[0].hashCode() +  args[1].hashCode() + args[2].hashCode() + args[3].hashCode() + args[4].hashCode();
		if(resultsCache.containsKey(queryHash)) {
			logger.info("Results from cache" );
			Collection<BindingSet> results = resultsCache.get(queryHash);
			if(results != null) {
				//return new ResultsIterator(new CloseableIteratorIteration<>(results.iterator()));	
			}else {
				results = new ArrayList<BindingSet>();
				//return new ResultsIterator(new CloseableIteratorIteration<>(results.iterator()));		
			}
			return new ResultsIterator(new CloseableIteratorIteration<>(results.iterator()));	
		}
		
		String HTTP_BACK = "http://B" +  queryHash + ":";
		String HTTP_FRONT = "http://F"+  queryHash + ":";
		Boolean pathFound = false;
		RepositoryConnection workingConn;
		workingConn = workingRep.getConnection();
		workingConn.begin();
		//ValueFactory factory = workingRep.getValueFactory();
		IRI edgePredicateIRI = iri("http://", "edge");
//		IRI startNodePredicateIRI = factory.createIRI("http://", "startNode");
//		IRI endNodePredicateIRI = factory.createIRI("http://", "endNode");
		ArrayList<Resource> frontContexts = new ArrayList<Resource>();
		ArrayList<Resource> backContexts = new ArrayList<Resource>();

		int iterator = 0;

		//Initiate FRONT front:0
		frontContexts.add(iri(HTTP_FRONT + iterator));
		workingConn.add(iri(startNode.stringValue()), edgePredicateIRI,
				iri(startNode.stringValue()), frontContexts.get(0));

		//Initiate BACK back:0
		backContexts.add(iri(HTTP_BACK + iterator));
		workingConn.add(iri(endNode.stringValue()), edgePredicateIRI,
				iri(endNode.stringValue()), backContexts.get(0));
		workingConn.commit();
		ArrayList<BindingSet> path =null;
		while (iterator < Math.ceil( maxPathLength/2.) && !pathFound) {
			//increment n	
			iterator++;
			//Increment front:n	
			if(hosted)
				path = incrementFrontUsingService(workingConn, iterator,HTTP_FRONT, HTTP_BACK ); 
			else
				path = incrementFront(workingConn, iterator,HTTP_FRONT, HTTP_BACK ); 	
			frontContexts.add(iri(HTTP_FRONT + iterator));

			pathFound = !(path == null);
			if (!pathFound) {
				//increment back:n		
				if(hosted)
					path = incrementBackUsingService(workingConn, iterator,HTTP_FRONT, HTTP_BACK );
				else
					path = incrementBack(workingConn, iterator,HTTP_FRONT, HTTP_BACK );
				backContexts.add(iri(HTTP_BACK + iterator));
				pathFound = !(path == null);
			}
		}
		if (path != null) {
			resultsCache.put(queryHash, path);
			dropGraphs(workingConn,frontContexts);
			dropGraphs(workingConn,backContexts);
		} else {
			logger.info("No Path after " + iterator + " iterations");
			resultsCache.put(queryHash, path);
			dropGraphs(workingConn,frontContexts);
			dropGraphs(workingConn,backContexts);
		}
		workingConn.close();
		return new ResultsIterator(new CloseableIteratorIteration<>(path.iterator()));
	}
	
	/**
	 * Drop graphs.
	 *
	 * @param workingConn the working conn
	 * @param contexts the contexts
	 */
	public void dropGraphs(RepositoryConnection workingConn,ArrayList<Resource> contexts) {
		for (Resource context: contexts) workingConn.clear(context);

	}
	
	/**
	 * Predicate filter.
	 *
	 * @param filterVariable the filter variable
	 * @param filterPredicate the filter predicate
	 * @return the string
	 */
	public String predicateFilter(String filterVariable, String filterPredicate) {
		return predicateFilter(filterVariable, filterPredicate, "");
	}

	/**
	 * Predicate filter.
	 *
	 * @param filterVariable the filter variable
	 * @param filterPredicate the filter predicate
	 * @param negation the negation
	 * @return the string
	 */
	public String predicateFilter(String filterVariable, String filterPredicate, String negation) {
		String predicateFilter = "";
		if (filterPredicate != "") {
			String[] filterPredicates = filterPredicate.split("\\|");
			if( filterPredicates.length>1) {
				predicateFilter = " FILTER (" + filterVariable + " " + negation + " in (" + filterPredicate.replace('|', ',')
					+ "))\r\n";
			}else {
				//Workaround for apparent RDF4J bug that does not allow only one value in 'IN' list
				predicateFilter = " FILTER (" + filterVariable + " " + negation + " in (" + filterPredicates[0] + "," + filterPredicates[0]
				+ "))\r\n";				
			}
		}
		return predicateFilter;
	}

	/**
	 * Prepare filters.
	 *
	 * @param filter the filter
	 */
	public void prepareFilters(String filter) {
		
		final Matcher matcher = pattern.matcher(filter);
		String predicate = "";
		String notPredicate = "";
		String inversePredicate = "";
		String notInversePredicate = "";
		while (matcher.find()) {
			switch (matcher.group(1)) {
			case "^":
				inversePredicate = matcher.group(2);
				break;
			case "!":
				notPredicate = matcher.group(2);
				break;
			case "^!":
				notInversePredicate = matcher.group(2);
				break;
			case "!^":
				notInversePredicate = matcher.group(2);
				break;
			default:
				predicate = matcher.group(2);
				break;
			}
		}
		predicateFilterClause = predicateFilter("?predicate", predicate);
		notPredicateFilterClause = predicateFilter("?predicate", notPredicate, "NOT");
		inversePredicateFilterClause = predicateFilter("?inversePredicate", inversePredicate);
		notInversePredicateFilterClause = predicateFilter("?inversePredicate", notInversePredicate, "NOT");	
		logger.debug("predicateFilterClause " + predicateFilterClause );
		logger.debug("notPredicateFilterClause " + notPredicateFilterClause );
		logger.debug("inversePredicateFilterClause " + inversePredicateFilterClause );
		logger.debug("notInversePredicateFilterClause " + notInversePredicateFilterClause );
	}

	/**
	 * Increment front.
	 *
	 * @param workingConn the working conn
	 * @param frontIndex the front index
	 * @param HTTP_FRONT the http front
	 * @param HTTP_BACK the http back
	 * @return the array list
	 */
	private ArrayList<BindingSet> incrementFront(RepositoryConnection workingConn, int frontIndex, String HTTP_FRONT,String HTTP_BACK  ) {
		String graphA = LT + HTTP_FRONT + frontIndex + GT;
		String graphB = LT + HTTP_FRONT + (frontIndex - 1) + GT;
		ArrayList<BindingSet> path = null;
		int nodeCount=0;
		logger.debug("incrementFront " + frontIndex );
		
		String frontNodesString = "select ?startNode { GRAPH " + graphB  + "{ select distinct ?startNode	{?startNode <http://edge> ?o}}} ";
		TupleQuery frontNodesQuery = workingConn.prepareTupleQuery(frontNodesString);
		StringBuilder startNodesValues = new StringBuilder("VALUES(?startNode){");
		try (TupleQueryResult result = frontNodesQuery.evaluate()) {
			while (result.hasNext()) {
				nodeCount++;
				BindingSet solution = result.next();
				Value startNode = solution.getValue("startNode");
				try {
					IRI iriStartNode = (IRI)startNode;
					startNodesValues.append("(<" + iriStartNode.stringValue() + ">)");
				}catch(Exception e) {
					//Ignore blank and non-IRI nodes
				//	BNode blankStartNode = (BNode)startNode;
				//	startNodesValues.append("(" + blankStartNode.stringValue() + ")");
				}
				if (startNodesValues.length()> MAX_UPDATE_STRING) {
					startNodesValues.append("}");
					logger.debug("Adding to front " + frontIndex + ", "  + nodeCount + ". String length=" + startNodesValues.length());
					executeIncrementFront(workingConn, frontIndex, graphA, startNodesValues);
					path = checkFrontToBack(workingConn, frontIndex, frontIndex - 1, HTTP_FRONT,HTTP_BACK);
					if(path!=null) return path;
					startNodesValues = new StringBuilder("VALUES(?startNode){");				
				}			
			}
			startNodesValues.append("}");
			logger.debug("Adding to front " + frontIndex + ", "  + nodeCount + ". String length=" + startNodesValues.length());
		}
		
		executeIncrementFront(workingConn, frontIndex, graphA, startNodesValues);
		logger.debug("checkFrontToBack " + frontIndex );
		path = checkFrontToBack(workingConn, frontIndex, frontIndex - 1, HTTP_FRONT,HTTP_BACK);
		return path;
	}

	/**
	 * Increment front using service.
	 *
	 * @param workingConn the working conn
	 * @param frontIndex the front index
	 * @param HTTP_FRONT the http front
	 * @param HTTP_BACK the http back
	 * @return the array list
	 */
	@SuppressWarnings("unused")
	private ArrayList<BindingSet> incrementFrontUsingService(RepositoryConnection workingConn, int frontIndex, String HTTP_FRONT,String HTTP_BACK  ) {
		String graphA = LT + HTTP_FRONT + frontIndex + GT;
		String graphB = LT + HTTP_FRONT + (frontIndex - 1) + GT;
		ArrayList<BindingSet> path = null;
		int nodeCount=0;
		logger.debug("incrementFront " + frontIndex );
		
		StringBuilder startNodesValues = new StringBuilder("{	SELECT ?startNode 	{" + olgapService + "{	GRAPH "+ graphB + "{ SELECT DISTINCT ?startNode	{?startNode <http://edge> ?o} } } } }\n");
		
		executeIncrementFront(workingConn, frontIndex, graphA, startNodesValues);
		logger.debug("checkFrontToBack " + frontIndex );
		path = checkFrontToBack(workingConn, frontIndex, frontIndex - 1, HTTP_FRONT,HTTP_BACK);
		return path;
	}
	
	/**
	 * Execute increment front.
	 *
	 * @param workingConn the working conn
	 * @param frontIndex the front index
	 * @param graphA the graph A
	 * @param startNodesValues the start nodes values
	 */
	protected void executeIncrementFront(RepositoryConnection workingConn, int frontIndex, String graphA,
			StringBuilder startNodesValues) {
		workingConn.begin();
		String incrementFrontString = "		INSERT{GRAPH " + graphA 
				+ "{ \r\n"
				+ "			?frontNode <http://edge> ?uuid . \r\n"
				+ " 		?uuid  ?designation ?startNode ;\r\n"
				+ "			       <http://property> ?property.\r\n " 
				+ "		}}\r\n" 
				+ "		WHERE {\r\n"
				+ "			{ " + service + "\r\n" 
				+ "				{\r\n"
				+ "					SELECT DISTINCT ?startNode ?frontNode (IF(BOUND(?predicate),<http://startNode>,<http://endNode>) as ?designation) (COALESCE(?predicate,?inversePredicate ) as ?property) ?uuid\r\n"
				+ "					{\r\n" 
				//Move deeper for performance
//				+ startNodesValues.toString()+"\n"
				+ "						{\r\n"
				+ startNodesValues.toString()+"\n"
				+ "							?startNode ?predicate  ?frontNode .  FILTER(isIRI(?frontNode)) .\r\n" 
				+ notPredicateFilterClause
				+ predicateFilterClause 
				+ "						}UNION{\r\n"
				+ startNodesValues.toString()+"\n"
				+ "							?frontNode  ?inversePredicate   ?startNode .\r\n"
				+ notInversePredicateFilterClause 
				+ inversePredicateFilterClause 
				+ "						}\r\n"
				+ "						BIND(UUID() as ?uuid)\r\n"
				+ "					}\r\n" 
				+ "				}\r\n" 
				+ "			}\r\n"
				+ "		}";
		logger.debug(incrementFrontString );
		Update incrementFront = workingConn.prepareUpdate(incrementFrontString);
		incrementFront.execute();
		workingConn.commit();
	}

	/**
	 * Increment back.
	 *
	 * @param workingConn the working conn
	 * @param backIndex the back index
	 * @param HTTP_FRONT the http front
	 * @param HTTP_BACK the http back
	 * @return the array list
	 */
	private ArrayList<BindingSet> incrementBack(RepositoryConnection workingConn, int backIndex, String HTTP_FRONT,String HTTP_BACK) {
		String graphA = LT+ HTTP_BACK + backIndex + GT;
		String graphB = LT +HTTP_BACK + (backIndex - 1) + GT;
		ArrayList<BindingSet> path = null;
		int nodeCount=0;
		logger.debug("incrementBack " + backIndex );
		String backNodesString = "select ?endNode { GRAPH " + graphB  + "{ select distinct ?endNode	{?endNode <http://edge> ?o}}} ";
		TupleQuery backNodesQuery = workingConn.prepareTupleQuery(backNodesString);
		StringBuilder endNodesValues = new StringBuilder("VALUES(?endNode){");
		try (TupleQueryResult result = backNodesQuery.evaluate()) {
			while (result.hasNext()) {
				nodeCount++;
				BindingSet solution = result.next();
				Value endNode = solution.getValue("endNode");
				try {
					IRI iriEndNode = (IRI)endNode;
					endNodesValues.append("(<" + iriEndNode.stringValue() + ">)");
				}catch(Exception e) {
					//Ignore blank and non-IRI nodes
				//	BNode blankEndNode = (BNode)endNode;
				//	endNodesValues.append("(_:" + blankEndNode.getID() + ")");
				}
				if (endNodesValues.length()> MAX_UPDATE_STRING) {
					endNodesValues.append("}");
					logger.debug("Adding to back " + backIndex + ", "  + nodeCount + ". String length=" + endNodesValues.length() );
					executeIncrementFront(workingConn, backIndex, graphA, endNodesValues);
					path = checkFrontToBack(workingConn, backIndex, backIndex, HTTP_FRONT,HTTP_BACK);
					if(path!=null) return path;
					endNodesValues = new StringBuilder("VALUES(?endNode){");				
				}
			}
			endNodesValues.append("}");
			logger.debug("Adding to back " + backIndex + ", "  + nodeCount + ". String length=" + endNodesValues.length() );
		}	
		executeIncrementBack(workingConn, backIndex, graphA, endNodesValues);
		path = checkFrontToBack(workingConn, backIndex, backIndex, HTTP_FRONT,HTTP_BACK);
		return path;
	}
	
	/**
	 * Increment back using service.
	 *
	 * @param workingConn the working conn
	 * @param backIndex the back index
	 * @param HTTP_FRONT the http front
	 * @param HTTP_BACK the http back
	 * @return the array list
	 */
	private ArrayList<BindingSet> incrementBackUsingService(RepositoryConnection workingConn, int backIndex, String HTTP_FRONT,String HTTP_BACK) {
		String graphA = LT+ HTTP_BACK + backIndex + GT;
		String graphB = LT +HTTP_BACK + (backIndex - 1) + GT;
		ArrayList<BindingSet> path = null;
		logger.debug("incrementBack " + backIndex );
		StringBuilder endNodesValues = new StringBuilder("{	SELECT ?endNode 	{" + olgapService + "{	GRAPH "+ graphB + "{ SELECT DISTINCT ?endNode	{?endNode <http://edge> ?o} } } } }\n");
		executeIncrementBack(workingConn, backIndex, graphA, endNodesValues);
		path = checkFrontToBack(workingConn, backIndex, backIndex, HTTP_FRONT,HTTP_BACK);
		return path;
	}
	
	/**
	 * Execute increment back.
	 *
	 * @param workingConn the working conn
	 * @param backIndex the back index
	 * @param graphA the graph A
	 * @param endNodesValues the end nodes values
	 */
	protected void executeIncrementBack(RepositoryConnection workingConn, int backIndex, String graphA,
			StringBuilder endNodesValues) {
		workingConn.begin();
		String incrementBackString = "		INSERT{GRAPH " + graphA 
				+ "{ \r\n"
				+ "			?backNode  <http://edge> ?uuid .\r\n"
				+ " 		?uuid  ?designation ?endNode ;\r\n"
				+ "			 		<http://property> ?property.\r\n " 
				+ "		}}\r\n" 
				+ "		WHERE {\r\n"
				+ "			{ " + service + "\r\n" 
				+ "				{\r\n"
				+ "					SELECT DISTINCT ?endNode ?backNode (IF(BOUND(?predicate),<http://startNode>,<http://endNode>) as ?designation) (COALESCE(?predicate,?inversePredicate ) as ?property) ?uuid \r\n"
				+ "					{\r\n" 
				//Move deeper for performance
//				+ endNodesValues.toString() +"\n"		
				+ "						{\r\n"
				+ endNodesValues.toString() +"\n"	
				+ "							?backNode  ?predicate   ?endNode .\r\n"
				+ notPredicateFilterClause
				+ predicateFilterClause

				+ "						}UNION{\r\n"
							+ endNodesValues.toString() +"\n"	
				+ "							?endNode ?inversePredicate  ?backNode .FILTER(isIRI(?backNode)).\r\n"		
				+ notInversePredicateFilterClause 
				+ inversePredicateFilterClause 
				+ "						}\r\n" 
				+ "						BIND(UUID() as ?uuid)\r\n"
				+ "					}\r\n"
				+ "				}\r\n" 
				+ "			}\r\n" 
				+ "		}";
		logger.debug(incrementBackString );
		Update incrementBack = workingConn.prepareUpdate(incrementBackString);
		incrementBack.execute();
		workingConn.commit();
	}

	/**
	 * Check front to back.
	 *
	 * @param workingConn the working conn
	 * @param frontIndex the front index
	 * @param backIndex the back index
	 * @param HTTP_FRONT the http front
	 * @param HTTP_BACK the http back
	 * @return the array list
	 */
	private ArrayList<BindingSet> checkFrontToBack(RepositoryConnection workingConn, int frontIndex, int backIndex, String HTTP_FRONT,String HTTP_BACK) {
		ArrayList<BindingSet> results = null;
		Boolean pathFound = false;
		String graphA = LT +HTTP_FRONT + frontIndex + GT;
		String graphB = LT+ HTTP_BACK + backIndex + GT;
		logger.debug("checkBackToFront " + frontIndex );
		String frontToBackQueryString = "		SELECT  ?frontNode  ?priorFrontNode  ?priorBackNode{\r\n"
				+ "			GRAPH " + graphA + "{\r\n" 
				+ "			    	?frontNode <http://edge> ?priorFrontNode .   \r\n"
				+ "			}\r\n" 
				+ "			    	GRAPH " + graphB + "{\r\n" 
				+ "			    		?frontNode <http://edge> ?priorBackNode\r\n"
				+ "			    	}\r\n" 
				+ "		} LIMIT 1";
		logger.debug(frontToBackQueryString );
		TupleQuery frontToBackQuery = workingConn.prepareTupleQuery(frontToBackQueryString);
		try (TupleQueryResult result = frontToBackQuery.evaluate()) {
			Value pivotNode = null;
			while (result.hasNext()) {
				pathFound = true;
				BindingSet solution = result.next();
				pivotNode = solution.getValue("frontNode");
				result.close();
				//found an edge so since we only want the shortestpath we can stop looking further
				break;
			}
			if (pathFound) {
				logger.info("Path found after" + frontIndex + " @ " + pivotNode.toString());
				results = new ArrayList<BindingSet>();
				Value frontPivotNode = pivotNode;
				for (int index = frontIndex; index > 0; index--) {
					String graph = LT +  HTTP_FRONT + index + GT;
					String reconstructPathString = "		SELECT  (" + index
							+ " as ?edge) ?subject ?property  ?direct  ?object{\r\n" 
							+ "			{VALUES(?object){(<"+ frontPivotNode.stringValue() + ">)}\r\n" 
							+ "			GRAPH " + graph + "{\r\n"
							+ "			    { ?object <http://edge> ?uuid1 .  ?uuid1  <http://startNode> ?subject ;  \r\n"
							+ "					<http://property> ?property . BIND( true as ?direct)\r\n"
							+ "			    } UNION \r\n" 
							+ "			    { ?object <http://edge> ?uuid2 .  ?uuid2  <http://endNode> ?subject ;  \r\n"
							+ "					<http://property> ?property . BIND( false  as ?direct)\r\n"
							+ "			    } \r\n" 
							+ "			}}} LIMIT 1";
					logger.debug(reconstructPathString );
					TupleQuery reconstructPathQuery = workingConn.prepareTupleQuery(reconstructPathString);
					try (TupleQueryResult pathResult = reconstructPathQuery.evaluate()) {
						while (pathResult.hasNext()) {
							BindingSet solution = pathResult.next();
							frontPivotNode = solution.getValue("subject");
							logger.debug("Front " + index + ", " + solution.toString() );
							results.add(solution);
							//found an edge so since we only want the shortestpath we can stop looking further
							pathResult.close();
							break;
						}
					}
				}
				Value backPivotNode = pivotNode;
				for (int index = backIndex; index > 0; index--) {
					String graph = LT + HTTP_BACK + index + GT;
					String reconstructPathString = "		SELECT (" + (frontIndex + backIndex - index + 1)
							+ " as ?edge) ?subject ?property  ?direct ?object  {\r\n"
							+ "			{VALUES(?subject){(<" + backPivotNode.stringValue() + ">)}\r\n"
							+ "			GRAPH " + graph + "{\r\n"
							+ "			   	{ ?subject <http://edge> ?uuid1 .  ?uuid1  <http://endNode> ?object ;  \r\n"
							+ "					<http://property> ?property . BIND( false as ?direct) \r\n" //false
							+ "			    } UNION \r\n"
							+ "			    { ?subject <http://edge> ?uuid2 .  ?uuid2  <http://startNode> ?object ;  \r\n"
							+ "					<http://property> ?property . BIND( true  as ?direct)\r\n" //true
							+ "			    } \r\n" 
							+ "			}}} LIMIT 1";
					logger.debug(reconstructPathString );
					TupleQuery reconstructPathQuery = workingConn.prepareTupleQuery(reconstructPathString);
					try (TupleQueryResult pathResult = reconstructPathQuery.evaluate()) {
						while (pathResult.hasNext()) {
							BindingSet solution = pathResult.next();
							backPivotNode = solution.getValue("object");
							logger.debug("Back " + index + ", " + solution.toString() );
							results.add(solution);
							//found an edge so since we only want the shortestpath we can stop looking further
							pathResult.close();
							break;
						}
					}
				}
			}
		}
		return results;
	}
}
