package olgap;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.Repository;

public class Source {
	private static Repository cacheRep;
	private static String cacheService;
	private static  TripleSource tripleSource;
	private static ModelBuilder modelBuilder;
	
	public Source(TripleSource tripleSource ) {
		Source.tripleSource=tripleSource;
		Source.modelBuilder =new ModelBuilder();
		//Source.cacheRep=cacheRep;
		//Source.cacheService=cacheService;

	}
	public static Repository getCacheRep() {
		return cacheRep;
	}
	public static void setCacheRep(Repository cacheRep) {
		Source.cacheRep = cacheRep;
	}
	public static String getCacheService() {
		return cacheService;
	}
	public static void setCacheService(String cacheService) {
		Source.cacheService = cacheService;
	}
	public static TripleSource getTripleSource() {
		return tripleSource;
	}
	public static void setTripleSource(TripleSource tripleSource) {
		Source.tripleSource = tripleSource;
	}
	public static ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

}
