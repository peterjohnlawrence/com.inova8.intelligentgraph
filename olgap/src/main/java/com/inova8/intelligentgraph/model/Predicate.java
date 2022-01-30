/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.intelligentgraph.results.FactResults;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class Predicate.
 */
public class Predicate extends Resource{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The reification. */
	IRI reification=null;	
	
	/** The is dereified. */
	Boolean isDereified =false;
	
	/** The direction. */
	Boolean direction= true;
	
	/**
	 * Instantiates a new predicate.
	 *
	 * @param value the value
	 * @param direction the direction
	 * @param reification the reification
	 * @param isDereified the is dereified
	 */
	protected Predicate(Value value, Boolean direction, IRI reification, Boolean isDereified) {
		super(value);
		this.direction =direction;
		this.reification =reification;
		this.isDereified =isDereified;
	}

	/**
	 * Instantiates a new predicate.
	 *
	 * @param predicate the predicate
	 * @throws URISyntaxException the URI syntax exception
	 */
	public Predicate(IRI predicate) throws URISyntaxException {
		super(null);
		URI predicateURI = new URI( predicate.stringValue());
		List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(predicateURI, Charset.forName("UTF-8")); 
		for(NameValuePair nameValuePair:nameValuePairs ) {
			switch(nameValuePair.getName()) {
			case PATHQL.EDGE_DIRECTIONSTRING:
				this.direction =nameValuePair.getValue().equals(Direction.INVERSE.toString()) ? false:true;
				break;
			case PATHQL.EDGE_REIFICATIONSTRING:
				this.reification =iri(nameValuePair.getValue());
				break;
			case PATHQL.EDGE_DEREIFIEDSTRING:
				this.isDereified = nameValuePair.getValue().equals("true")?true:false;
				break;
			default:
			}
		}
		this.superValue = iri(predicateURI.toString().split("\\?")[0]);
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 */
	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 */
	@Override
	public FactResults getFacts(String predicatePattern, Value... bindValues) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String predicate = this.superValue.stringValue();
		if((isDereified!=null) && isDereified) predicate+="#";
		if(reification!=null )predicate= reification.stringValue()+"@"+predicate;
		if((this.direction !=null) && !this.direction )predicate= "^"+predicate;
		return predicate;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
