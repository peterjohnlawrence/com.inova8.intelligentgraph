/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.dashjoin;

import java.util.HashMap;
import java.util.Map;

import com.inova8.intelligentgraph.path.Edge;
import com.inova8.intelligentgraph.vocabulary.PATHQL;

/**
 * The Class Step.
 */
public class Step {
    
    /** The end. */
    public Object end;
    
    /** The edge. */
    public Map<String, Object> edge;
	
	/**
	 * Instantiates a new step.
	 *
	 * @param edge the edge
	 */
	public Step(Edge edge) {
		//Step step = new Step();
		this.end = new Resource(edge.getTarget());
		
		this.edge  = new HashMap<String,Object>();
		this.edge.put(PATHQL.EDGE_PREDICATESTRING , edge.getPredicate());
		if(edge.getDirection()!=null)this.edge.put(PATHQL.EDGE_DIRECTIONSTRING , edge.getDirection());
		if(edge.getIsDereified()!=null)this.edge.put(PATHQL.EDGE_DEREIFIEDSTRING , edge.getIsDereified());
		if(edge.getReification()!=null)this.edge.put(PATHQL.EDGE_REIFICATIONSTRING , edge.getReification());
		
	}   
    
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		
		String stepString = "\r\n\t[\tend:"+end.toString()+";\r\n\t\tedge:[\r\n";
		for ( String key : edge.keySet()) {
			stepString += "\t\t\t"+ key.toString() +":" + edge.get(key).toString()+";\r\n";
		};
		return stepString+"\t\t]\r\n\t]\r\n";
	}
}
