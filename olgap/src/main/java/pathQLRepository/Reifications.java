package pathQLRepository;

import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pathCalc.Evaluator;

public class Reifications {
	private static final Logger logger   = LoggerFactory.getLogger(Reifications.class);
	private ConcurrentHashMap<String, ReificationType> reificationTypes = new ConcurrentHashMap<String, ReificationType>();
	private ConcurrentHashMap<String, ReificationType> predicateReificationTypes = new ConcurrentHashMap<String, ReificationType>();
	/** The is lazy loaded. */
	private Boolean reificationsAreLazyLoaded = false;
	private PathQLRepository pathQLRepository;
	public Reifications(PathQLRepository pathQLRepository) {
		this.pathQLRepository=pathQLRepository;
	}
	
	private PathQLRepository getPathQLRepository() {
		return pathQLRepository;
	}

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
		return getReificationTypes().get(reificationType);
	}

	public ConcurrentHashMap<String, ReificationType> getReificationTypes() {
		if (!reificationsAreLazyLoaded && this.getPathQLRepository().getTripleSource() != null)
			initializeReificationTypes();
		return reificationTypes;
	}

	protected void initializeReificationTypes() {
		StringBuilder initializedReifications = new StringBuilder(
				" reifications initialized: <" + Evaluator.RDF_STATEMENT + "> ");
		int initializedReification = 1;
		IRI RDFStatement = Evaluator.RDF_STATEMENT_IRI;
	
		IRI RDFSsubPropertyOf = Evaluator.RDFS_SUB_PROPERTY_OF_IRI;
		IRI RDFsubject = Evaluator.RDF_SUBJECT_IRI;
		IRI RDFpredicate = Evaluator.RDF_PREDICATE_IRI;
		IRI RDFobject = Evaluator.RDF_OBJECT_IRI;
		IRI RDFSdomain = Evaluator.RDFS_DOMAIN_IRI;
		IRI OWLinverseOf = Evaluator.OWL_INVERSE_OF_IRI;
	
		reificationTypes.put(Evaluator.RDF_STATEMENT,
				new ReificationType(RDFStatement, RDFsubject, RDFpredicate, RDFobject));
	
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = 
				this.getPathQLRepository().getTripleSource().getStatements(null, RDFSsubPropertyOf, RDFpredicate);
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
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationTypeStatements = this
					.getPathQLRepository().getTripleSource().getStatements(reificationPredicate, RDFSdomain, null);
			while (reificationTypeStatements.hasNext()) {
				Statement reificationTypeStatement = reificationTypeStatements.next();
				reificationType = reificationTypeStatement.getObject();
				break;
			}
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = this
					.getPathQLRepository().getTripleSource().getStatements(null, RDFSdomain, reificationType);
			while (reificationSubjectStatements.hasNext()) {
				Statement reificationSubjectStatement = reificationSubjectStatements.next();
				org.eclipse.rdf4j.model.Resource reificationProperty = reificationSubjectStatement.getSubject();
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubPropertyOfStatements = this
						.getPathQLRepository().getTripleSource().getStatements(reificationProperty, RDFSsubPropertyOf, null);
				while (reificationSubPropertyOfStatements.hasNext()) {
					Statement reificationSubPropertyOfStatement = reificationSubPropertyOfStatements.next();
					CloseableIteration<? extends Statement, QueryEvaluationException> reificationInverseOfStatements = this
							.getPathQLRepository().getTripleSource().getStatements(reificationProperty, OWLinverseOf, null);
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
					case Evaluator.RDF_SUBJECT:
						reificationSubject = reificationProperty;
						reificationIsSubjectOf = reificationInverseProperty;
						break;
					case Evaluator.RDF_OBJECT:
						reificationObject = reificationProperty;
						reificationIsObjectOf = reificationInverseProperty;
						break;
					case Evaluator.RDF_PREDICATE:
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
