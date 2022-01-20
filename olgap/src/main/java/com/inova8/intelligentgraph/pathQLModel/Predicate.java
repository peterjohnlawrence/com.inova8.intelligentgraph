package com.inova8.intelligentgraph.pathQLModel;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.path.Edge.Direction;
import com.inova8.intelligentgraph.pathQLResults.FactResults;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import static org.eclipse.rdf4j.model.util.Values.iri;
public class Predicate extends Resource{
	private static final long serialVersionUID = 1L;
	IRI reification=null;	
	Boolean isDereified =false;
	Boolean direction= true;
	protected Predicate(Value value, Boolean direction, IRI reification, Boolean isDereified) {
		super(value);
		this.direction =direction;
		this.reification =reification;
		this.isDereified =isDereified;
	}

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

	@Override
	public Resource getFact(String predicatePattern, Value... bindValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactResults getFacts(String predicatePattern, Value... bindValues) {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		String predicate = this.superValue.stringValue();
		if((isDereified!=null) && isDereified) predicate+="#";
		if(reification!=null )predicate= reification.stringValue()+"@"+predicate;
		if((this.direction !=null) && !this.direction )predicate= "^"+predicate;
		return predicate;
	}

	@Override
	public Resource addFact(String property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(IRI property, String value, IRI dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(IRI property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(String property, Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource addFact(String property, String value) {
		// TODO Auto-generated method stub
		return null;
	}

}
