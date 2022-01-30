/*
 * inova8 2020
 */
package com.inova8.pathql.context;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import com.inova8.pathql.utilities.Utilities;

/**
 * The Class Prefixes.
 */
public class Prefixes extends ConcurrentHashMap<String, IRI> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Prefix.
	 *
	 * @param IRI the iri
	 * @return the prefixes
	 */
	public Prefixes prefix(String IRI) {
		org.eclipse.rdf4j.model.IRI iri = Utilities.trimAndCheckIRIString(IRI);
		this.put("", iri);
			return this;

	}
	
	/**
	 * Prefix.
	 *
	 * @param prefix the prefix
	 * @param IRI the iri
	 * @return the prefixes
	 */
	public Prefixes prefix(String prefix, String IRI) {
		org.eclipse.rdf4j.model.IRI iri = Utilities.trimAndCheckIRIString(IRI);
		if (iri != null) {
				this.put(prefix, iri);
		} else {
		//TODO	logger.error("Invalid IRI specified. Ensure enclosed in <...> ", IRI);
		}
		return this;
	}
	
	/**
	 * Initialize prefixes.
	 *
	 * @param repositoryConnection the repository connection
	 */
	public void initializePrefixes(RepositoryConnection repositoryConnection) {
		RepositoryResult<Namespace> namespaces = repositoryConnection.getNamespaces();
		while (namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			this.put(namespace.getPrefix(), iri(namespace.getName()));
		}
		
	}
}
