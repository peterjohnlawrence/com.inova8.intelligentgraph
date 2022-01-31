# What Is IntelligentGraph?

The IntelligentGraph SAIL offers an extended capability for embedded calculation support within any RDF graph. When enabled as an RDF4J SAIL, it offers calculation functionality as part of the RDF4J engine, on top of any RDF4J repository, using a variety of script engines including JavaScript, Jython, and Groovy. It preserves the SPARQL capability of RDF4J, but with additional capabilities for calculation debugging and tracing. 

IntelligentGraph includes the PathQL query language. Just as a spreadsheet cell calculation needs to access other cells, an IntelligentGraph calculation needs to access other nodes within the graph. Although full access to the underlying graph is available to any of the scripts, PathQL provides a succinct, and efficient method to access directly or indirectly related nodes. PathQL can either return just the contents of the referenced nodes, or the contents and the path to the referenced nodes.

PathQL can also be used standalone to query the IntelligentGraph-enabled RDF database. This supplements, rather than replaces, SPARQL and GraphQL, as it provides graph-path querying rather than graph-pattern querying capabilities to any IntelligentGraph-enabled RDF database.

+ The principles of IntelligentGraph are described [here:](https://inova8.com/bg_inova8.com/intelligent-graph-knowledge-graph-embedded-analysis/)
+ The full PathQL syntax is described [here:](https://inova8.com/bg_inova8.com/pathpatternql-intelligently-finding-knowledge-as-a-path-through-a-maze-of-facts/)
+ Using Jupyter as an IDE to IntelligentGraph and RDF4J, shown [here:](https://inova8.com/bg_inova8.com/intelligentgraph-getting-started/)
+ IntelligentGraph source is here in [GitHub:](https://github.com/peterjohnlawrence/com.inova8.intelligentgraph)
+ IntelligentGraph Docker containers are available [here:](https://hub.docker.com/repository/docker/inova8/intelligentgraph)

## Creating an IntelligentGraph-enabled RDF4J Server

### Download

The project is located here in Github, from where the intelligentgraph-0.9.0.jar can be downloaded from [here](https://github.com/peterjohnlawrence/com.inova8.olgap/tree/master/olgap/target).

### Install

- Copy `intelligentgraph-0.9.0.jar` (or the latest version)
- To `/usr/local/tomcat/webapps/rdf4j-server/WEB-INF/lib/intelligentgraph-0.9.0.jar`

The RDF4J server will need to be restarted for it to recognize this new JAR and initiate the scripting engine.

### Installation Notes 

+ IntelligentGraph will work only with RDF4J version 3.3.0 and above. The jar is compiled using RDF4J 3.7.4
+ The IntelligentGraph.jar should include all dependencies. However, if you want to use another scripting language,  you would have to be certain all dependencies are already available.


## Using IntelligentGraph Docker Image

One of the easiest ways to get started with IntelligentGraph is to pull the IntelligentGraph docker image from Dockerhub:

>docker pull inova8/intelligentgraph

This Docker image contains Tomcat v9 preinstalled, with the following applications pre-installed 

+ http://localhost:8080/manager: Tomcat Manager (username tomcat, password tomcat)
+ http://localhost:8080/rdf4j-server/: Rdf4j Server with IntelligentGraph pre-installed
+ http://localhost:8080/rdf4j-workbench/: Rdf4j Workbench with IntelligentGraph build scripts pre-installed 

IntelligentGraph-enabled repositories can be created using one of the following RDF4J Repository types

+ MemoryStore + IntelligentGraph
+ NativeStore + IntelligentGraph
+ NativeStore + Lucene + IntelligentGraph

### Installation Notes

+ Setting the container port to the default 8080 is recommended
+ The host path is provided if you wish to persist the repository datastores outside of the docker image

## Creating an IntelligentGraph-enabled Repository

Creating an IntelligentGraph-enabled RDF4J repository is no different than any other RDF4J repository, and is well documented [here](https://rdf4j.org/documentation/tools/server-workbench/).
 
### IntelligentGraph Repository Creation via RDF4J-Workbench

The following summarises the essential steps:

- Open RDF4J-Workbench
    - by navigating to  http://localhost:8080/rdf4j-workbench/
- Select *New Repository** to create a new repository
- Select repository type as one of
    - MemoryStore + IntelligentGraph
    - NativeStore + IntelligentGraph
    - NativeStore + Lucene + IntelligentGraph
+ Provide ID and Title
    - such as *example1*
+ **Next** to enter Triple Indices
    - it is recommended that “spoc,pocs,cpso,cspo,oscp,ocsp”, the default, is used.
+ Finally **Create** to instantiate your IntelligentGraph-enabled repository

#### You now have an IntelligentGraph enabled, but empty, repository

- Use **Add** to load a model file (eg example1.ttl, shown below)
    - We now see that there are 10 statements
- Verify **Contexts** exists 
    - View contexts, including calculated values (:hasBMI in this case), by clickling on context id 
- Verify script with SPARQL **Query**

		PREFIX example1: <http://inova8.com/intelligentgraph/example1>
		SELECT *
		{
			example1:aPerson example1:hasBMI ?BMI, ?BMI_SCRIPT 
		}
+ Execute to show calculated value and script

### IntelligentGraph Repository Creation via RDF4J Console

RDF4J also supports a text console application, documented [here](https://rdf4j.org/documentation/tools/console/). This can also be used to create an IntelligentGraph-enabled repository

- Start the console application
    - `./console.bat` 
- Connect to a repository
    - `connect http://localhost:8080/rdf4j-server`
- If replacing an existing repository, drop it first
    - `drop example1`
    - `Yes`
- Create a new repository
    - `create native-lucene-intelligentgraph`
    - `example1`
    - `example1`
    - `10000`
    - `spoc,pocs,cpso,cspo,oscp,ocsp`
- Load some data
    - `open example1`
    - `load example1.ttl into file://example1.ttl`  

## Debugging

Since IntelligentGraph combines calculations with the knowledge graph, it is inevitable that any evaluation will involve calls to values of other nodes which are in turn calculations.  
A trace of a calculation’s execution can be obtained via SPARQL by using a _TRACE postfix to the calculated script variable name.  
 
For example the following SPARQL will retrieve the calculated value and the trace (as structured HTML)

	PREFIX example1: <http://inova8.com/intelligentgraph/example1/>
	SELECT * 
	{
		example1:aPerson example1:hasBMI ?bmi, ?bmi_TRACE, ?bmi_SCRIPT
	}

## Example1.ttl

A simple model of persons, with one example. TYhe person has height and weight properties, but also a calculated BMI index.

	# baseURI: http://inova8.com/intelligentgraph/example1/
	# prefix: example1
	
	@prefix : <http://inova8.com/intelligentgraph/example1/> .
	@prefix example1: <http://inova8.com/intelligentgraph/example1/> .
	@prefix owl: <http://www.w3.org/2002/07/owl#> .
	@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
	@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
	@prefix script: <http://inova8.com/script/> .
	@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
	
	example1:
	  a owl:Ontology ;
	.
	example1:Person
	  a owl:Class ;
	  rdfs:label "Person" ;
	.
	example1:aPerson
	  a example1:Person ;
	  example1:hasBMI "double height=_this.getFact(':hasHeight').doubleValue();  _this.getFact(':hasWeight').doubleValue()/(height*height)"^^script:groovy ;
	  example1:hasHeight 1.7 ;
	  example1:hasWeight "62"^^xsd:decimal ;
	.
	example1:hasBMI
	  a owl:DatatypeProperty ;
	.
	example1:hasHeight
	  a owl:DatatypeProperty ;
	.
	example1:hasWeight
	  a owl:DatatypeProperty ;
	.

