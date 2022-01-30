/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.intelligentGraphRepository;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.contextaware.ContextAwareConnection;

/**
 * The Class PathQLContextAwareConnection.
 */
public class PathQLContextAwareConnection extends ContextAwareConnection{

	/**
	 * Instantiates a new path QL context aware connection.
	 *
	 * @param repository the repository
	 * @throws RepositoryException the repository exception
	 */
	public PathQLContextAwareConnection(Repository repository) throws RepositoryException {
		super(repository);
	}
	
	/**
	 * Instantiates a new path QL context aware connection.
	 *
	 * @param connection the connection
	 */
	public PathQLContextAwareConnection(RepositoryConnection connection) {
		super(connection);
		
	}
	
	/**
	 * Instantiates a new path QL context aware connection.
	 *
	 * @param repository the repository
	 * @param connection the connection
	 */
	public PathQLContextAwareConnection(Repository repository, RepositoryConnection connection){
		super(repository, connection);
		
	}
}
