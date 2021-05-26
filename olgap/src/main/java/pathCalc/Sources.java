/*
 * inova8 2020
 */
package pathCalc;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;

import pathQLRepository.PathQLRepository;

/**
 * The Class Sources.
 */
public class Sources {

	/** The logger. */
	private final Logger logger = LoggerFactory.getLogger(Sources.class);
	
	/** The sources. */
	static protected   ConcurrentHashMap<Integer, PathQLRepository > sources = new ConcurrentHashMap<Integer, PathQLRepository>();
	
	/**
	 * Contains key.
	 *
	 * @param cacheHash the cache hash
	 * @return the boolean
	 */
	Boolean containsKey(Integer cacheHash){
		return sources.containsKey(cacheHash);
	}
	 
 	/**
 	 * Put.
 	 *
 	 * @param cacheHash the cache hash
 	 * @param source the source
 	 * @return the path QL repository
 	 */
 	PathQLRepository put(Integer cacheHash,PathQLRepository source){
		return sources.put(cacheHash,  source);
	}
	 
 	/**
 	 * Gets the.
 	 *
 	 * @param cacheHash the cache hash
 	 * @return the path QL repository
 	 */
 	PathQLRepository get(Integer cacheHash){
		 return sources.get(cacheHash);
	 }
	 
 	/**
 	 * Gets the source.
 	 *
 	 * @param tripleSource the triple source
 	 * @param args the args
 	 * @return the source
 	 */
 	public PathQLRepository getSource(TripleSource tripleSource,Value[] args){
		 Integer cacheHash;
		 
		 if(args.length>0) {
			 cacheHash = locateCachHashArgument( args);
			 if(cacheHash==null) cacheHash=tripleSource.hashCode();
		  }else {
			  cacheHash=tripleSource.hashCode();
		  }		
			PathQLRepository source;
			if(!sources.containsKey(cacheHash) ){
				source = PathQLRepository.create(tripleSource);
				sources.put(cacheHash,  source);
				logger.error("Failed to locate hash <{}>, new source created  <{}>",locateCachHashArgument( args),cacheHash);
			}else {
				source = sources.get(cacheHash);
				//Need to ensure we are using the latest triplesource, which changes even though from same triplestore
				source.setTripleSource(tripleSource);
			}
		 return source;
	 } 
	 
 	/**
 	 * Locate cach hash argument.
 	 *
 	 * @param args the args
 	 * @return the integer
 	 */
 	Integer locateCachHashArgument(Value[] args){
		 for (int customQueryOptionsArrayIndex = 0; customQueryOptionsArrayIndex < args.length; customQueryOptionsArrayIndex += 2) {
			 if(args[customQueryOptionsArrayIndex].stringValue().equals(Evaluator.CACHE_HASH)) {
				 try {
					 return (((SimpleLiteral) args[customQueryOptionsArrayIndex+1]).integerValue()).intValue();
				 }catch(NumberFormatException e) {
					 return (Integer) null;
				 }
			 }
		 }
		return (Integer) null;
	 }
	 
 	/**
 	 * Gets the keys.
 	 *
 	 * @return the keys
 	 */
 	public Set<Integer> getKeys (){
		 return sources.keySet(); 
	 }
	 
 	/**
 	 * Clear.
 	 */
 	public void clear (){
		 sources.clear();
	 }
}
