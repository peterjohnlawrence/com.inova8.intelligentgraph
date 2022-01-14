package com.inova8.intelligentgraph.dashjoin;

public class Resource {
	 String database;
	 String table;
	 String IRI;	
	 public Resource( org.eclipse.rdf4j.model.Value value) {
		 if(value instanceof org.eclipse.rdf4j.model.Value) {
			 IRI =  value.stringValue();
			 
		 }else {
			 IRI =  value.stringValue();
		 }
		 
	 }
		public String toString() {			
			String resourceString = "_dj_resource:{";
			resourceString += "database:"+"unknown"+"," +"table:"+"null"+","+"pk:"+IRI;
			return resourceString+"}";
		}
}
