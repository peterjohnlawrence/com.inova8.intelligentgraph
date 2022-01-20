package com.inova8.pathql.context;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;

public class Reifications {
	private static final Logger logger   = LoggerFactory.getLogger(Reifications.class);
	ConcurrentHashMap<String, ReificationType> reificationTypes = new ConcurrentHashMap<String, ReificationType>();
	private ConcurrentHashMap<String, ReificationType> predicateReificationTypes = new ConcurrentHashMap<String, ReificationType>();
	/** The is lazy loaded. */
	private Boolean reificationsAreLazyLoaded = false;
//	private IntelligentGraphRepository pathQLRepository;
//	public Reifications(IntelligentGraphRepository pathQLRepository) {
//		this.pathQLRepository=pathQLRepository;
//	}
	public Reifications() {
	}	
//	private IntelligentGraphRepository getPathQLRepository() {
//		return pathQLRepository;
//	}

	public ReificationType getPredicateReificationType(String predicate) {
		if (getPredicateReificationTypes().containsKey(predicate)) {
			return getPredicateReificationTypes().get(predicate);
		} else {
			return null;
		}
	}
	ConcurrentHashMap<String, ReificationType> getPredicateReificationTypes() {
		return predicateReificationTypes;
	}
	public IRI getReificationIsObjectOf(IRI reificationType) {
		return getReificationIsObjectOf(reificationType.stringValue());
	}
	public IRI getReificationIsObjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsObjectOf();
		else
			return null;
	}
	public IRI getReificationIsPredicateOf(IRI reificationType) {
		return getReificationIsPredicateOf(reificationType.stringValue());
	}
	public IRI getReificationIsPredicateOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsPredicateOf();
		else
			return null;
	}
	public IRI getReificationIsSubjectOf(IRI reificationType) {
		return getReificationIsSubjectOf(reificationType.stringValue());
	}
	public IRI getReificationIsSubjectOf(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationIsSubjectOf();
		else
			return null;
	}
	public IRI getReificationObject(IRI reificationType) {
		return getReificationObject(reificationType.stringValue());
	}
	public IRI getReificationObject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationObject();
		else
			return null;
	}
	public IRI getReificationPredicate(IRI reificationType) {
		return getReificationPredicate(reificationType.stringValue());
	}
	public IRI getReificationPredicate(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationPredicate();
		else
			return null;
	}
	public IRI getReificationSubject(IRI reificationType) {
		return getReificationSubject(reificationType.stringValue());
	}

	public IRI getReificationSubject(String reificationType) {
		if (getReificationType(reificationType) != null)
			return getReificationType(reificationType).getReificationSubject();
		else
			return null;
	}

	private ReificationType getReificationType(String reificationType) {
		return getReificationTypes(null).get(reificationType);
	}

	public ConcurrentHashMap<String, ReificationType> getReificationTypes(RepositoryConnection repositoryConnection) {
		if (!reificationsAreLazyLoaded && repositoryConnection != null)
			initializeReificationTypes(repositoryConnection);
		return reificationTypes;
	}

	protected void initializeReificationTypes(RepositoryConnection repositoryConnection) {
		StringBuilder initializedReifications = new StringBuilder(
				" reifications initialized: <" + RDF.STATEMENT + "> ");
		int initializedReification = 1;
		IRI RDFStatement = RDF.STATEMENT;
	
		IRI RDFSsubPropertyOf = RDFS.SUB_PROPERTY_OF;
		IRI RDFsubject = RDF.SUBJECT;
		IRI RDFpredicate = RDF.PREDICATE;
		IRI RDFobject = RDF.OBJECT;
		IRI RDFSdomain = RDFS.DOMAIN;
		IRI OWLinverseOf = OWL.OWL_INVERSE_OF;
	
		reificationTypes.put(RDF.STATEMENT.stringValue(),
				new ReificationType(RDFStatement, RDFsubject, RDFpredicate, RDFobject));
	
		CloseableIteration<Statement, RepositoryException> reificationPredicateStatements = 
				/*this.getPathQLRepository().getTripleSource()*/ repositoryConnection.getStatements(null, RDFSsubPropertyOf, RDFpredicate);
		while (reificationPredicateStatements.hasNext()) {
			Value reificationType = null;
	
			org.eclipse.rdf4j.model.Resource reificationPredicate;
			org.eclipse.rdf4j.model.Resource reificationIsPredicateOf = null;
			org.eclipse.rdf4j.model.Resource reificationSubject = null;
			org.eclipse.rdf4j.model.Resource reificationIsSubjectOf = null;
			org.eclipse.rdf4j.model.Resource reificationObject = null;
			org.eclipse.rdf4j.model.Resource reificationIsObjectOf = null;
			Statement reificationPredicateStatement = reificationPredicateStatements.next();
			reificationPredicate = reificationPredicateStatement.getSubject();
			CloseableIteration<Statement, RepositoryException> reificationTypeStatements = /*this	.getPathQLRepository().getTripleSource(). */
					repositoryConnection.getStatements(reificationPredicate, RDFSdomain, null);
			while (reificationTypeStatements.hasNext()) {
				Statement reificationTypeStatement = reificationTypeStatements.next();
				reificationType = reificationTypeStatement.getObject();
				break;
			}
			CloseableIteration<Statement, RepositoryException> reificationSubjectStatements = /*this.getPathQLRepository().getTripleSource().*/  
					repositoryConnection.getStatements(null, RDFSdomain, reificationType);
			while (reificationSubjectStatements.hasNext()) {
				Statement reificationSubjectStatement = reificationSubjectStatements.next();
				org.eclipse.rdf4j.model.Resource reificationProperty = reificationSubjectStatement.getSubject();
				CloseableIteration<Statement, RepositoryException> reificationSubPropertyOfStatements = /*this.getPathQLRepository().getTripleSource()*/					
						repositoryConnection.getStatements(reificationProperty, RDFSsubPropertyOf, null);
				while (reificationSubPropertyOfStatements.hasNext()) {
					Statement reificationSubPropertyOfStatement = reificationSubPropertyOfStatements.next();
					CloseableIteration<Statement, RepositoryException> reificationInverseOfStatements = /*this.getPathQLRepository().getTripleSource()*/
							repositoryConnection.getStatements(reificationProperty, OWLinverseOf, null);
					org.eclipse.rdf4j.model.Resource reificationInverseProperty = null;
					while (reificationInverseOfStatements.hasNext()) {
						Statement reificationInverseOfStatement = reificationInverseOfStatements.next();
						reificationInverseProperty = (org.eclipse.rdf4j.model.Resource) reificationInverseOfStatement
								.getObject();
						break;
					}
					org.eclipse.rdf4j.model.Resource subPropertyOf = (org.eclipse.rdf4j.model.Resource) reificationSubPropertyOfStatement
							.getObject();
					switch (subPropertyOf.stringValue()) {
					case RDF.subject:
						reificationSubject = reificationProperty;
						reificationIsSubjectOf = reificationInverseProperty;
						break;
					case RDF.object:
						reificationObject = reificationProperty;
						reificationIsObjectOf = reificationInverseProperty;
						break;
					case RDF.predicate:
						reificationPredicate = reificationProperty;
						reificationIsPredicateOf = reificationInverseProperty;
						break;
					default:
					}
				}
			}
			initializedReification++;
			initializedReifications.append("<").append(((IRI) reificationType).stringValue()).append("> ");
			ReificationType newReificationType = new ReificationType((org.eclipse.rdf4j.model.Resource) reificationType,
					reificationSubject, reificationPredicate, reificationObject, reificationIsSubjectOf,
					reificationIsPredicateOf, reificationIsObjectOf);
			reificationTypes.put(((IRI) reificationType).stringValue(), newReificationType);
			predicateReificationTypes.put(((IRI) reificationPredicate).stringValue(), newReificationType);
		}
		reificationsAreLazyLoaded = true;
		logger.debug(initializedReification + initializedReifications.toString());
	}
	public void setReificationsAreLazyLoaded(Boolean reificationsAreLazyLoaded) {
		this.reificationsAreLazyLoaded = reificationsAreLazyLoaded;
	}

	public void addReificationType(Resource reificationType, Resource reificationSubject, Resource reificationPredicate,
			Resource reificationObject, Resource reificationIsSubjectOf, Resource reificationIsPredicateOf,
			Resource reificationIsObjectOf) {
		ReificationType newReificationType = new ReificationType(reificationType, reificationSubject,
				reificationPredicate, reificationObject, reificationIsSubjectOf, reificationIsPredicateOf,
				reificationIsObjectOf);
		reificationTypes.put(((IRI) reificationType).stringValue(), newReificationType);
		predicateReificationTypes.put(((IRI) reificationPredicate).stringValue(), newReificationType);
	
	}
}
