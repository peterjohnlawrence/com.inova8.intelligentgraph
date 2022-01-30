/*
 * inova8 2020
 */
package com.inova8.pathql.context;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.pathql.utilities.Utilities;

/**
 * The Class RepositoryContext.
 */
public class RepositoryContext {
	
	/** The Constant logger. */
	private static final Logger logger   = LoggerFactory.getLogger(RepositoryContext.class);
	
	/**
	 * Instantiates a new repository context.
	 *
	 * @param prefixes the prefixes
	 * @param reifications the reifications
	 */
	public RepositoryContext(Prefixes prefixes, Reifications reifications) {
		super();
		this.prefixes = prefixes;
		this.reifications = reifications;
	}
	
	/**
	 * Instantiates a new repository context.
	 */
	public RepositoryContext() {
		super();
		this.prefixes = new Prefixes();
		this.reifications = new Reifications();
	}
	
	/** The prefixes. */
	private Prefixes prefixes;
	
	/** The reifications. */
	private Reifications reifications;

	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public Prefixes getPrefixes() {
		return prefixes;
	}

	/**
	 * Initialize.
	 *
	 * @param repositoryConnection the repository connection
	 */
	public void initialize(RepositoryConnection repositoryConnection) {
		initializePrefixes(repositoryConnection);
		initializeReifications(repositoryConnection);
	}
	
	/**
	 * Initialize prefixes.
	 *
	 * @param repositoryConnection the repository connection
	 */
	public void initializePrefixes(RepositoryConnection repositoryConnection) {
		this.prefixes.initializePrefixes(repositoryConnection);;
	}
	
	/**
	 * Initialize reifications.
	 *
	 * @param repositoryConnection the repository connection
	 */
	public void initializeReifications(RepositoryConnection repositoryConnection) {
		this.reifications.initializeReificationTypes(repositoryConnection);
	}
	
	/**
	 * Gets the reifications.
	 *
	 * @return the reifications
	 */
	public Reifications getReifications() {
		return reifications;
	}
	
	/**
	 * Gets the reification types.
	 *
	 * @return the reification types
	 */
	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
		return reifications.reificationTypes;
	}
	
	/**
	 * Prefix.
	 *
	 * @param IRI the iri
	 * @return the prefixes
	 */
	public Prefixes prefix(String IRI) {
		this.prefixes.prefix("", IRI);
			return prefixes;

	}
	
	/**
	 * Prefix.
	 *
	 * @param prefix the prefix
	 * @param IRI the iri
	 * @return the prefixes
	 */
	public Prefixes prefix(String prefix, String IRI) {
		this.prefixes.prefix(prefix, IRI);
		return prefixes;
	}
	
	/**
	 * Convert Q name.
	 *
	 * @param predicateIRI the predicate IRI
	 * @return the iri
	 */
	public IRI convertQName(String predicateIRI) {
		return convertQName(predicateIRI,this.prefixes);
	}
	
	/**
	 * Convert Q name.
	 *
	 * @param predicateIRI the predicate IRI
	 * @param localPrefixes the local prefixes
	 * @return the iri
	 */
	public IRI convertQName(String predicateIRI, Prefixes localPrefixes) {
		predicateIRI = Utilities.trimIRIString(predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":|~");
		IRI predicate = null;
		if (predicateIRIParts[0].equals("a")) {
			predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		} else if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		} else {
			IRI namespace = localPrefixes.get(predicateIRIParts[0] );//      getNamespace(predicateIRIParts[0], localPrefixes);
			if (namespace == null) {
				logger.error("Error identifying namespace of qName {}", predicateIRI);
			} else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}
}
