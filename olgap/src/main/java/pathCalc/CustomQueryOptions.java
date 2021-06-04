package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pathQLModel.Literal;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

public class CustomQueryOptions extends Hashtable<String, Resource> {
	private static final Logger logger   = LoggerFactory.getLogger(CustomQueryOptions.class);
	private static final long serialVersionUID = -5718476100009689495L;
 
	ArrayList<CustomQueryOptions> inheritedCustomQueryOptionsList = new ArrayList<CustomQueryOptions>();

	
	public CustomQueryOptions() {
		super();
	}
	static public CustomQueryOptions create(PathQLRepository pathQLRepository,Value[] customQueryOptionsArray) {

		if (customQueryOptionsArray.length == 0) {
			return null;
		} else if (customQueryOptionsArray.length % 2 != 0) {
			logger.error("Must have matching args tag/value pairs '{}'",
					customQueryOptionsArray.length);
			return null;
		} else {
			CustomQueryOptions customQueryOptions = new CustomQueryOptions();
			for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < customQueryOptionsArray.length; customQueryOptionsArrayIndex += 2) {
				String customQueryOptionParameter = customQueryOptionsArray[customQueryOptionsArrayIndex].stringValue();
				String customQueryOptionValue = customQueryOptionsArray[customQueryOptionsArrayIndex + 1].stringValue();
				if (customQueryOptionValue != null && !customQueryOptionValue.isEmpty())
					customQueryOptions.put(customQueryOptionParameter,
							pathQLModel.Resource.create(pathQLRepository, literal(customQueryOptionValue), null));
			}
			return customQueryOptions;
		}
	}
	public CustomQueryOptions(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}
	public CustomQueryOptions(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}
	public CustomQueryOptions(Map<? extends String, ? extends Resource> t) {
		super(t);
		// TODO Auto-generated constructor stub
	}
	public void add(String key,Object value ) {
		switch (value.getClass().getSimpleName()) {
		case "SimpleLiteral":
		case "BooleanLiteral":
		case "BooleanLiteralImpl":
		case "CalendarLiteral":
		case "DecimalLiteral":
		case "IntegerLiteral":
		case "MemLiteral":
		case "BooleanMemLiteral":
		case "CalendarMemLiteral":
		case "DecimalMemLiteral":
		case "IntegerMemLiteral":
		case "NumericMemLiteral":
		case "NativeLiteral":
		case "NumericLiteral":
			this.put(key,new Literal((Value) value));
			break;
		default:
			put(key,new Literal(literal(value)));
		}
		
		
	}
	public Resource getInherited(String key) {
		if(this.contains(key)) {
			return this.get(key);
		}else if(!inheritedCustomQueryOptionsList.isEmpty()){
			for(CustomQueryOptions parentCustomQueryOptions: inheritedCustomQueryOptionsList) {
				if(parentCustomQueryOptions.contains(key) ) {
					return parentCustomQueryOptions.get(key);
				}
			}
			for(CustomQueryOptions parentCustomQueryOptions: inheritedCustomQueryOptionsList) {
				if(parentCustomQueryOptions.containsInherited(key) ) {
					return parentCustomQueryOptions.getInherited(key);
				}
			}
			return null;
		}else {
			return null;
		}
	}
	public Boolean containsInherited(String key) {
		if(this.contains(key)) {
			return true;
		}else if(!inheritedCustomQueryOptionsList.isEmpty()){
			for(CustomQueryOptions parentCustomQueryOptions: inheritedCustomQueryOptionsList) {
				if(parentCustomQueryOptions.contains(key) ) {
					return true;
				}
			}
			for(CustomQueryOptions parentCustomQueryOptions: inheritedCustomQueryOptionsList) {
				if(parentCustomQueryOptions.containsInherited(key) ) {
					return true;
				}
			}
			return null;
		}else {
			return null;
		}
	}
	@Override
	public String toString() {
		String queryOptionsstring = "";
		for ( java.util.Map.Entry<String, Resource> queryOption:this.entrySet()) {
			queryOptionsstring+="&"+queryOption.getKey()+"="+queryOption.getValue();
		}
		return queryOptionsstring;
	}
	public void addInherited(CustomQueryOptions inheritedCustomQueryOptions) {
		this.inheritedCustomQueryOptionsList.add(inheritedCustomQueryOptions);
	}
	public void clearParents() {
		this.inheritedCustomQueryOptionsList.clear();
	}
	public String toSPARQL() {
		return null;
	}
	public String toHTML() {
		return null;
	}
}
