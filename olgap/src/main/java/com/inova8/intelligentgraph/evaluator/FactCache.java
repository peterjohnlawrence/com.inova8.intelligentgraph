/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 * The Class FactCache.
 */
public class FactCache {
	
	/** The fact cache. */
	final  Repository factCache;
	
	/** The is dirty. */
	private boolean isDirty=false;
	
	/**
	 * Instantiates a new fact cache.
	 */
	public FactCache() {
		factCache = new SailRepository(new MemoryStore());
	}
	
	/**
	 * Contains.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param customQueryOptions the custom query options
	 * @return true, if successful
	 */
	public boolean contains(IRI thing, IRI predicate, Resource customQueryOptions) {
		if(isDirty()) {
			clear();		
			return false;
		}else {
			return factCache.getConnection().hasStatement(thing, predicate, null, false, customQueryOptions);
		}	
	}
	
	/**
	 * Gets the facts.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param customQueryOptions the custom query options
	 * @return the facts
	 */
	public Value getFacts(IRI thing, IRI predicate, Resource customQueryOptions) {
		if(isDirty()) {
			clear();		
			return null;
		}else {
			RepositoryResult<Statement> statements = factCache.getConnection().getStatements(thing, predicate, null, false, customQueryOptions);
			if(statements.hasNext()) {
				Statement statement = statements.next();
				Value object = statement.getObject();
				return object;
			}
			return null;
		}	
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param fact the fact
	 * @param customQueryOptions the custom query options
	 */
	public void addFact(IRI thing, IRI predicate, Value fact, Resource customQueryOptions) {
		factCache.getConnection().add(thing, predicate, fact,customQueryOptions  );		
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		factCache.getConnection().clear();
		setDirty(false);
	}
	
	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty() {
		return isDirty;
	}
	
	/**
	 * Sets the dirty.
	 *
	 * @param isDirty the new dirty
	 */
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
}
