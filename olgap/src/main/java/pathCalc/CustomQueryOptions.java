package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.util.concurrent.ConcurrentHashMap;

import pathQLModel.Literal;
import pathQLModel.Resource;

public class CustomQueryOptions extends ConcurrentHashMap<String, Resource> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5718476100009689495L;

	public void put(String key,Object value ) {
		put(key,new Literal( literal(value)));
		
	}
}
