package com.inova8.pathql.context;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.pathql.utilities.Utilities;

public class RepositoryContext {
	private static final Logger logger   = LoggerFactory.getLogger(RepositoryContext.class);
	public RepositoryContext(Prefixes prefixes, Reifications reifications) {
		super();
		this.prefixes = prefixes;
		this.reifications = reifications;
	}
	public RepositoryContext() {
		super();
		this.prefixes = new Prefixes();
		this.reifications = new Reifications();
	}
	private Prefixes prefixes;
	private Reifications reifications;

	public Prefixes getPrefixes() {
		return prefixes;
	}

	public void initialize(RepositoryConnection repositoryConnection) {
		this.prefixes.initializePrefixes(repositoryConnection);
		this.reifications.initializeReificationTypes(repositoryConnection);
	}
	public Reifications getReifications() {
		return reifications;
	}
	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
		return reifications.reificationTypes;
	}
	public Prefixes prefix(String IRI) {
		this.prefixes.prefix("", IRI);
			return prefixes;

	}
	public Prefixes prefix(String prefix, String IRI) {
		this.prefixes.prefix(prefix, IRI);
		return prefixes;
	}
	public IRI convertQName(String predicateIRI) {
		return convertQName(predicateIRI,this.prefixes);
	}
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
