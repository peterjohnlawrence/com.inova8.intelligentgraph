/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.dashjoin;

/**
 * The Class Resource.
 */
public class Resource {
	 
 	/** The database. */
 	String database;
	 
 	/** The table. */
 	String table;
	 
 	/** The iri. */
 	String IRI;	
	 
 	/**
	  * Instantiates a new resource.
	  *
	  * @param value the value
	  */
 	public Resource( org.eclipse.rdf4j.model.Value value) {
		 if(value instanceof org.eclipse.rdf4j.model.Value) {
			 IRI =  value.stringValue();
			 
		 }else {
			 IRI =  value.stringValue();
		 }
		 
	 }
		
		/**
		 * To string.
		 *
		 * @return the string
		 */
		public String toString() {			
			String resourceString = "_dj_resource:{";
			resourceString += "database:"+"unknown"+"," +"table:"+"null"+","+"pk:"+IRI;
			return resourceString+"}";
		}
}
