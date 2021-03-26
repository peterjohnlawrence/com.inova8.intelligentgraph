package pathCalc;

import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;

import olgap.Evaluator;
import pathQLRepository.PathQLRepository;

public class Sources {

	private final Logger logger = LogManager.getLogger(Sources.class);
	static protected   HashMap<Integer, PathQLRepository > sources = new HashMap<Integer, PathQLRepository>();
	
	Boolean containsKey(Integer cacheHash){
		return sources.containsKey(cacheHash);
	}
	 PathQLRepository put(Integer cacheHash,PathQLRepository source){
		return sources.put(cacheHash,  source);
	}
	 PathQLRepository get(Integer cacheHash){
		 return sources.get(cacheHash);
	 }
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
				source = new PathQLRepository(tripleSource);
				sources.put(cacheHash,  source);
				logger.error(new ParameterizedMessage("Failed to locate hash <{}>, new source created  <{}>",locateCachHashArgument( args),cacheHash));
			}else {
				source = sources.get(cacheHash);
				//Need to ensure we are using the latest triplesource, which changes even though from same triplestore
				source.setTripleSource(tripleSource);
			}
		 return source;
	 } 
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
	 public Set<Integer> getKeys (){
		 return sources.keySet(); 
	 }
	 public void clear (){
		 sources.clear();
	 }
}
