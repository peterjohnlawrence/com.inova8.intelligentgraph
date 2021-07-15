package pathPatternElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Iterations extends LinkedHashMap<Integer,Integer>{
	private static final long serialVersionUID = 1L;
	public static Iterations create(PathElement element) {
		element.resetIteration();
		Integer iteration=0;
		Iterations iterations = new Iterations();
		do {
			iterations.put(iteration, element.getPathLength(iteration));
			iteration++;
		}while( element.hasNextCardinality(iteration));
		return iterations;
	}
	public Iterations sortByPathLength(
	        ) {
	    List<Integer> mapKeys = new ArrayList<>(this.keySet());
	    List<Integer> mapValues = new ArrayList<>(this.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);

	    Iterations sortedMap =
	        new Iterations();

	    Iterator<Integer> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	    	Integer val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Integer key = keyIt.next();
	            Integer comp1 = this.get(key);
	            Integer comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	public String toString() {
		return super.toString();
	}
}
