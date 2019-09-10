package olgap;
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

INSERT{GRAPH<http://front:0>{ ?frontNode <http://startNode> ?startNode.
	 }}
WHERE {
		BIND(<http://in4mium.com/londontube/id/Harringay> as ?startNode)
		BIND(<http://in4mium.com/londontube/id/Harringay> as ?frontNode)
	 }
#Initiate BACK

INSERT{GRAPH<http://back:0>{ ?backNode <http://endNode> ?endNode.
	 }}
WHERE {
		BIND(<http://in4mium.com/londontube/id/Euston_Square> as ?endNode)
		BIND(<http://in4mium.com/londontube/id/Euston_Square> as ?backNode)
	 }

 #Increment FRONT

INSERT{GRAPH<http://front:2>{ 
	?frontNode ?designation ?startNode.
	?frontNode <http://property> ?property.
}}
WHERE {
	{	SERVICE <http://localhost:8080/rdf4j-server/repositories/tfl> 
		{
			#BIND(londontube:Abbey_Road as ?startNode)
			SELECT DISTINCT ?startNode ?frontNode (IF(BOUND(?predicate),<http://startNode>,<http://endNode>) as ?designation)   (COALESCE(?predicate,?inversePredicate ) as ?property)
			{
				{
					?startNode ?predicate  ?frontNode .
					# !(...)
					 FILTER (?predicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/connectsTo>))
					# (...)
					 FILTER (?predicate  in (<http://in4mium.com/londontube/ref/connectsFrom>,<http://in4mium.com/londontube/ref/connectsFrom>))
				}UNION{
					?frontNode  ?inversePredicate   ?startNode .
					# !^(...)
					FILTER (?inversePredicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/hasStationInZone>,<http://in4mium.com/londontube/ref/hasStationOnLine>,<http://in4mium.com/londontube/ref/connectsFrom>))
					# ^(...)
					#FILTER ( (?inversePredicate in () )
				}
			}
		}
	}
	GRAPH <http://front:1>{ select distinct ?startNode	{?startNode ?p ?o} }
}

#Increment BACK

INSERT{GRAPH<http://back:1>{ 
	?backNode ?designation ?endNode.
	?backNode <http://property> ?property.
}}
WHERE {
	{	SERVICE <http://localhost:8080/rdf4j-server/repositories/tfl> 
		{
			#BIND(londontube:Abbey_Road as ?startNode)
			SELECT DISTINCT ?endNode ?backNode (IF(BOUND(?inversePredicate),<http://startNode>,<http://endNode>) as ?designation)   (COALESCE(?predicate,?inversePredicate ) as ?property)
			{
				{
					?backNode ?inversePredicate  ?endNode .
					# !^(rdf:type)
					 FILTER (?inversePredicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/hasStationInZone>,<http://in4mium.com/londontube/ref/hasStationOnLine>,<http://in4mium.com/londontube/ref/connectsFrom>))
					# ^(tfl:connectsTo)
					#FILTER ( (?inversePredicate in (<http://in4mium.com/londontube/ref/connectsFrom>,  <http://in4mium.com/londontube/ref/hasStationOnLine>)))
				}UNION{
					?endNode  ?predicate   ?backNode .
					# !(rdf:type)
					 FILTER (?predicate NOT in (rdf:type,<http://in4mium.com/londontube/ref/connectsTo>))
					# (tfl:connectsFrom | tfl:hasStationInZone)
					 FILTER (?predicate  in (<http://in4mium.com/londontube/ref/connectsFrom>,<http://in4mium.com/londontube/ref/connectsFrom>))
				}
			}
		}
	}
	GRAPH <http://back:1>{ select distinct ?endNode	{?endNode ?p ?o} }
}	

#Match front to back

SELECT distinct ?frontNode  ?priorFrontNode  ?priorBackNode{
	GRAPH <http://front:1>{
	    ?frontNode ?p0 ?priorFrontNode .   
	    GRAPH <http://back:0>{
	    	?frontNode ?p1 ?priorBackNode
	    }
	}
} LIMIT 1

#recover front path

SELECT  (5 as ?edge) ?subject ?property  ?object ?direct {
	{VALUES(?object){(<http://in4mium.com/londontube/id/Golders_Green>)}
	GRAPH <http://front:5>{
	    { ?object <http://startNode> ?subject ;  
			<http://property> ?property . BIND( true as ?direct)
	    } UNION 
	    { ?object <http://endNode> ?subject ;  
			<http://property> ?property . BIND( false  as ?direct)
	    } 
	    } 
	}}	

#recover back path

SELECT (6 as ?edge) ?subject ?property  ?object ?direct  {
	{VALUES(?subject){(<http://in4mium.com/londontube/id/Golders_Green>)}
	GRAPH <http://back:4>{
	   	{ ?subject <http://endNode> ?object ;  
			<http://property> ?property . BIND( true as ?direct) 
	    } UNION 
	    { ?subject <http://startNode> ?object ;  
			<http://property> ?property . BIND( false  as ?direct)
	    } 
	}}}
*/

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.common.iteration.ConvertingIteration;
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
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.sail.nativerdf.NativeStore;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShortestPathTupleFunction implements InverseMagicProperty {
	private static final int MAX_UPDATE_STRING = 100000;
	private final Logger log = LoggerFactory.getLogger(ShortestPathTupleFunction.class);
	protected static final String LT = "<";
	protected static final String GT = ">";
	public static final String NAMESPACE = "http://inova8.com/olgap/";
	protected static final String SHORTEST_PATH_PROPERTY = "shortestPath";

	public static Repository workingRep;
	RepositoryConnection workingConn;

	final String propertyPathRegex = "([!^]*)?\\(([^)]*)\\)";
	String propertyPath;
	final Pattern pattern = Pattern.compile(propertyPathRegex, Pattern.MULTILINE);

	String predicateFilterClause;
	String notPredicateFilterClause;
	String inversePredicateFilterClause;
	String notInversePredicateFilterClause;

	private String service;

	static private HashMap<Integer,Collection<BindingSet>> resultsCache = new HashMap<Integer,Collection<BindingSet>>();
	
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
		workingRep = new SailRepository(new MemoryStore());
		workingRep.init();
	}

	@Override
	public String getURI() {
		return NAMESPACE + SHORTEST_PATH_PROPERTY;
	}
	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args) throws QueryEvaluationException {
		if (args.length != 5) {
			throw new ValueExprEvaluationException(
					SHORTEST_PATH_PROPERTY + " requires" + "exactly 5 arguments: service, start, end, path, max. Got "
							+ args.length);
		}
		service = "SERVICE <" + args[0].toString() + "?distinct=true&infer=false>";
		Value startNode = args[1];
		Value endNode = args[2];
		prepareFilters(((Literal) args[3]).getLabel());
		final int maxPathLength = (((Literal) (args[4])).intValue());
		Integer queryHash = args[0].hashCode() +  args[1].hashCode() + args[2].hashCode() + args[3].hashCode() + args[4].hashCode();
		if(resultsCache.containsKey(queryHash)) {
			log.info("Results from cache" );
			Collection<BindingSet> results = resultsCache.get(queryHash);
			if(results != null) {
				return new ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException>(
						new CloseableIteratorIteration<>(results.iterator())) {
					@Override
					protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
						List<Value> results = new ArrayList<>();
						for (String bindingName : bindings.getBindingNames()) {
							results.add(bindings.getValue(bindingName));
						}
						return results;
					}
				};	
			}else {
				return new ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException>(
						new CloseableIteratorIteration<>(null)) {
					@Override
					protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
						List<Value> results = new ArrayList<>();
						for (String bindingName : bindings.getBindingNames()) {
							results.add(bindings.getValue(bindingName));
						}
						return results;
					}
				};		
				
			}
		}
		
		String HTTP_BACK = "http://B" +  queryHash + ":";
		String HTTP_FRONT = "http://F"+  queryHash + ":";
		Boolean pathFound = false;

		workingConn = workingRep.getConnection();

		ValueFactory factory = workingRep.getValueFactory();
		IRI startNodePredicateIRI = factory.createIRI("http://", "startNode");
		IRI endNodePredicateIRI = factory.createIRI("http://", "endNode");
		ArrayList<Resource> frontContexts = new ArrayList<Resource>();
		ArrayList<Resource> backContexts = new ArrayList<Resource>();

		int iterator = 0;

		//Initiate FRONT front:0
		frontContexts.add(factory.createIRI(HTTP_FRONT + iterator));
		workingConn.add(factory.createIRI(startNode.stringValue()), startNodePredicateIRI,
				factory.createIRI(startNode.stringValue()), frontContexts.get(0));

		//Initiate BACK back:0
		backContexts.add(factory.createIRI(HTTP_BACK + iterator));
		workingConn.add(factory.createIRI(endNode.stringValue()), endNodePredicateIRI,
				factory.createIRI(endNode.stringValue()), backContexts.get(0));

		Collection<BindingSet> path = null;
		while (iterator < Math.ceil( maxPathLength/2.) && !pathFound) {
			//increment n	
			iterator++;
			//Increment front:n	
			path = incrementFront(workingConn, iterator,HTTP_FRONT, HTTP_BACK );
			frontContexts.add(factory.createIRI(HTTP_FRONT + iterator));

			pathFound = !(path == null);
			if (!pathFound) {
				//increment back:n		
				path = incrementBack(workingConn, iterator,HTTP_FRONT, HTTP_BACK );
				backContexts.add(factory.createIRI(HTTP_BACK + iterator));
				pathFound = !(path == null);
			}
		}
		if (path != null) {
			log.info("iteratingPath " + iterator );
			resultsCache.put(queryHash, path);
			dropGraphs(frontContexts);
			dropGraphs(backContexts);
			return new ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException>(
					new CloseableIteratorIteration<>(path.iterator())) {

				@Override
				protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
					List<Value> results = new ArrayList<>();
					for (String bindingName : bindings.getBindingNames()) {
						results.add(bindings.getValue(bindingName));
					}
					return results;
				}
			};
		} else {
			log.info("No Path after " + iterator );
			resultsCache.put(queryHash, path);
			dropGraphs(frontContexts);
			dropGraphs(backContexts);
			return new ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException>(
					new CloseableIteratorIteration<>(null)) {
				@Override
				protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
					List<Value> results = new ArrayList<>();
					for (String bindingName : bindings.getBindingNames()) {
						results.add(bindings.getValue(bindingName));
					}
					return results;
				}
			};
		}
	}
	public void dropGraphs(ArrayList<Resource> contexts) {
		for (Resource context: contexts) workingConn.clear(context);

	}
	public String predicateFilter(String filterVariable, String filterPredicate) {
		return predicateFilter(filterVariable, filterPredicate, "");
	}

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
	}

	private Collection<BindingSet> incrementFront(RepositoryConnection workingConn, int frontIndex, String HTTP_FRONT,String HTTP_BACK  ) {
		String graphA = LT + HTTP_FRONT + frontIndex + GT;
		String graphB = LT + HTTP_FRONT + (frontIndex - 1) + GT;
		Collection<BindingSet> path = null;
		int nodeCount=0;
		log.info("incrementFront " + frontIndex );
		
		String frontNodesString = "select ?startNode { GRAPH " + graphB  + "{ select distinct ?startNode	{?startNode ?p ?o}}} ";
		TupleQuery frontNodesQuery = workingConn.prepareTupleQuery(frontNodesString);
		StringBuilder startNodesValues = new StringBuilder("VALUES(?startNode){");
		try (TupleQueryResult result = frontNodesQuery.evaluate()) {
			while (result.hasNext()) {
				nodeCount++;
				BindingSet solution = result.next();
				Value startNode = solution.getValue("startNode");
				startNodesValues.append("(<" + startNode.stringValue() + ">)");
				if (startNodesValues.length()> MAX_UPDATE_STRING) {
					startNodesValues.append("}");
					log.info("Adding to front " + frontIndex + ", "  + nodeCount + ". String length=" + startNodesValues.length());
					executeIncrementFront(workingConn, frontIndex, graphA, startNodesValues);
					path = checkFrontToBack(workingConn, frontIndex, frontIndex - 1, HTTP_FRONT,HTTP_BACK);
					if(path!=null) return path;
					startNodesValues = new StringBuilder("VALUES(?startNode){");				
				}			
			}
			startNodesValues.append("}");
			log.info("Adding to front " + frontIndex + ", "  + nodeCount + ". String length=" + startNodesValues.length());
		}
		
		executeIncrementFront(workingConn, frontIndex, graphA, startNodesValues);
		log.info("checkFrontToBack " + frontIndex );
		path = checkFrontToBack(workingConn, frontIndex, frontIndex - 1, HTTP_FRONT,HTTP_BACK);
		return path;
	}

	protected void executeIncrementFront(RepositoryConnection workingConn, int frontIndex, String graphA,
			StringBuilder startNodesValues) {
		String incrementFrontString = "		INSERT{GRAPH " + graphA 
				+ "{ \r\n"
				+ "			?frontNode ?designation ?startNode.\r\n"
				+ "			?frontNode <http://property> ?property.\r\n " 
				+ "		}}\r\n" 
				+ "		WHERE {\r\n"
				+ "			{ " + service + "\r\n" 
				+ "				{\r\n"
				+ "					SELECT DISTINCT ?startNode ?frontNode (IF(BOUND(?predicate),<http://startNode>,<http://endNode>) as ?designation) (COALESCE(?predicate,?inversePredicate ) as ?property) \r\n"
				+ "					{\r\n" 
				+ startNodesValues.toString()+"\n"
				+ "						{\r\n"
				+ "							?startNode ?predicate  ?frontNode .  FILTER(isIRI(?frontNode)) .\r\n" 
				+ notPredicateFilterClause
				+ predicateFilterClause 
				+ "						}UNION{\r\n"
				+ "							?frontNode  ?inversePredicate   ?startNode .\r\n"
				+ notInversePredicateFilterClause 
				+ inversePredicateFilterClause 
				+ "						}\r\n"
				+ "					}\r\n" 
				+ "				}\r\n" 
				+ "			}\r\n"
				+ "		}";
		Update incrementFront = workingConn.prepareUpdate(incrementFrontString);
		incrementFront.execute();
	}

	private Collection<BindingSet> incrementBack(RepositoryConnection workingConn, int backIndex, String HTTP_FRONT,String HTTP_BACK) {
		String graphA = LT+ HTTP_BACK + backIndex + GT;
		String graphB = LT +HTTP_BACK + (backIndex - 1) + GT;
		Collection<BindingSet> path = null;
		int nodeCount=0;
		log.info("incrementBack " + backIndex );
		String backNodesString = "select ?endNode { GRAPH " + graphB  + "{ select distinct ?endNode	{?endNode ?p ?o}}} ";
		TupleQuery backNodesQuery = workingConn.prepareTupleQuery(backNodesString);
		StringBuilder endNodesValues = new StringBuilder("VALUES(?endNode){");
		try (TupleQueryResult result = backNodesQuery.evaluate()) {
			while (result.hasNext()) {
				nodeCount++;
				BindingSet solution = result.next();
				Value endNode = solution.getValue("endNode");
				endNodesValues.append("(<" + endNode.stringValue() + ">)");
				if (endNodesValues.length()> MAX_UPDATE_STRING) {
					endNodesValues.append("}");
					log.info("Adding to back " + backIndex + ", "  + nodeCount + ". String length=" + endNodesValues.length() );
					executeIncrementFront(workingConn, backIndex, graphA, endNodesValues);
					path = checkFrontToBack(workingConn, backIndex, backIndex, HTTP_FRONT,HTTP_BACK);
					if(path!=null) return path;
					endNodesValues = new StringBuilder("VALUES(?endNode){");				
				}
			}
			endNodesValues.append("}");
			log.info("Adding to back " + backIndex + ", "  + nodeCount + ". String length=" + endNodesValues.length() );
		}	
		executeIncrementBack(workingConn, backIndex, graphA, endNodesValues);
		path = checkFrontToBack(workingConn, backIndex, backIndex, HTTP_FRONT,HTTP_BACK);
		return path;
	}

	protected void executeIncrementBack(RepositoryConnection workingConn, int backIndex, String graphA,
			StringBuilder endNodesValues) {
		String incrementBackString = "		INSERT{GRAPH " + graphA 
				+ "{ \r\n"
				+ "			?backNode  ?designation ?endNode.\r\n"
				+ "			?backNode <http://property> ?property.\r\n " 
				+ "		}}\r\n" 
				+ "		WHERE {\r\n"
				+ "			{ " + service + "\r\n" 
				+ "				{\r\n"
				+ "					SELECT DISTINCT ?endNode ?backNode (IF(BOUND(?inversePredicate),<http://startNode>,<http://endNode>) as ?designation) (COALESCE(?predicate,?inversePredicate ) as ?property)\r\n"
				//				+ "					SELECT DISTINCT ?endNode ?backNode (IF(BOUND(?inversePredicate),<http://endtNode>,<http://startNode>) as ?designation) (COALESCE(?predicate,?inversePredicate ) as ?property)\r\n"
				+ "					{\r\n" 
				+ endNodesValues.toString() +"\n"
//				+ "						{\r\n"
//				+ "							?backNode ?inversePredicate  ?endNode .\r\n"
//				+ notInversePredicateFilterClause 
//				+ inversePredicateFilterClause 
//				+ "						}UNION{\r\n"
//				+ "							?endNode  ?predicate   ?backNode . FILTER(isIRI(?backNode)) .\r\n"
//				+ notPredicateFilterClause
//				+ predicateFilterClause
//				+ "						}\r\n" 
				
				+ "						{\r\n"
				+ "							?endNode  ?predicate   ?backNode . FILTER(isIRI(?backNode)) .\r\n"
				+ notPredicateFilterClause
				+ predicateFilterClause
				+ "						}UNION{\r\n"
				+ "							?backNode ?inversePredicate  ?endNode .\r\n"		
				+ notInversePredicateFilterClause 
				+ inversePredicateFilterClause 
				+ "						}\r\n" 
				+ "					}\r\n"
				+ "				}\r\n" 
				+ "			}\r\n" 
				+ "		}";
		Update incrementBack = workingConn.prepareUpdate(incrementBackString);
		incrementBack.execute();
	}

	private Collection<BindingSet> checkFrontToBack(RepositoryConnection workingConn, int frontIndex, int backIndex, String HTTP_FRONT,String HTTP_BACK) {
		Collection<BindingSet> results = null;
		Boolean pathFound = false;
		String graphA = LT +HTTP_FRONT + frontIndex + GT;
		String graphB = LT+ HTTP_BACK + backIndex + GT;
		log.info("checkBackToFront " + frontIndex );
		String frontToBackQueryString = "		SELECT  ?frontNode  ?priorFrontNode  ?priorBackNode{\r\n"
				+ "			GRAPH " + graphA + "{\r\n" 
				+ "			    	?frontNode ?p0 ?priorFrontNode .   \r\n"
				+ "			}\r\n" 
				+ "			    	GRAPH " + graphB + "{\r\n" 
				+ "			    		?frontNode ?p1 ?priorBackNode\r\n"
				+ "			    	}\r\n" 
				+ "		} LIMIT 1";
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
				log.info("Path found " + frontIndex + " @ " + pivotNode.toString());
				results = new ArrayList<BindingSet>();
				Value frontPivotNode = pivotNode;
				for (int index = frontIndex; index > 0; index--) {
					String graph = LT +  HTTP_FRONT + index + GT;
					String reconstructPathString = "		SELECT  (" + index
							+ " as ?edge) ?subject ?property  ?object ?direct {\r\n" 
							+ "			{VALUES(?object){(<"+ frontPivotNode.stringValue() + ">)}\r\n" 
							+ "			GRAPH " + graph + "{\r\n"
							+ "			    { ?object <http://startNode> ?subject ;  \r\n"
							+ "					<http://property> ?property . BIND( true as ?direct)\r\n"
							+ "			    } UNION \r\n" 
							+ "			    { ?object <http://endNode> ?subject ;  \r\n"
							+ "					<http://property> ?property . BIND( false  as ?direct)\r\n"
							+ "			    } \r\n" 
							+ "			}}} LIMIT 1";
					TupleQuery reconstructPathQuery = workingConn.prepareTupleQuery(reconstructPathString);
					try (TupleQueryResult pathResult = reconstructPathQuery.evaluate()) {
						while (pathResult.hasNext()) {
							BindingSet solution = pathResult.next();
							frontPivotNode = solution.getValue("subject");
							log.info("Front " + index + ", " + solution.toString() );
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
							+ " as ?edge) ?subject ?property  ?object ?direct  {\r\n"
							+ "			{VALUES(?subject){(<" + backPivotNode.stringValue() + ">)}\r\n"
							+ "			GRAPH " + graph + "{\r\n"
							+ "			   	{ ?subject <http://endNode> ?object ;  \r\n"
							+ "					<http://property> ?property . BIND( false as ?direct) \r\n"
							+ "			    } UNION \r\n"
							+ "			    { ?subject <http://startNode> ?object ;  \r\n"
							+ "					<http://property> ?property . BIND( true  as ?direct)\r\n"
							+ "			    } \r\n" 
							+ "			}}} LIMIT 1";
					TupleQuery reconstructPathQuery = workingConn.prepareTupleQuery(reconstructPathString);
					try (TupleQueryResult pathResult = reconstructPathQuery.evaluate()) {
						while (pathResult.hasNext()) {
							BindingSet solution = pathResult.next();
							backPivotNode = solution.getValue("object");
							log.info("Back " + index + ", " + solution.toString() );
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
