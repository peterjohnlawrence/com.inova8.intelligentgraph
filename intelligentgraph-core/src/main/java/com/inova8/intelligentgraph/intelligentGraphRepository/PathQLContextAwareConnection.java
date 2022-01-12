package com.inova8.intelligentgraph.intelligentGraphRepository;

import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.contextaware.ContextAwareConnection;

public class PathQLContextAwareConnection extends ContextAwareConnection{

	public PathQLContextAwareConnection(Repository repository) throws RepositoryException {
		super(repository);
	}
	public PathQLContextAwareConnection(RepositoryConnection connection) {
		super(connection);
		
	}
	public PathQLContextAwareConnection(Repository repository, RepositoryConnection connection){
		super(repository, connection);
		
	}
}
