package pathQLRepository;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;

public class ReificationType {
	 private IRI reificationType;
	 private IRI reificationSubject;
	 private IRI reificationPredicate;
	 private IRI reificationObject;	
	 private IRI reificationIsSubjectOf;
	 private IRI reificationIsPredicateOf;
	 private IRI reificationIsObjectOf;	
	public ReificationType(IRI reificationType, IRI reificationSubject, IRI reificationPredicate, IRI reificationObject) {
		super();
		this.reificationType = reificationType;
		this.reificationSubject = reificationSubject;
		this.reificationPredicate = reificationPredicate;
		this.reificationObject = reificationObject;
	}	
	public ReificationType(Resource reificationType, Resource reificationSubject, Resource reificationPredicate,
			Resource reificationObject , Resource reificationIsSubjectOf ,Resource reificationIsPredicateOf ,Resource reificationIsObjectOf   ) {
		this.reificationType = (IRI)reificationType;
		this.reificationSubject = (IRI)reificationSubject;
		this.reificationPredicate = (IRI)reificationPredicate;
		this.reificationObject =(IRI) reificationObject;
		
		this.reificationIsSubjectOf = (IRI)reificationIsSubjectOf;
		this.reificationIsPredicateOf = (IRI)reificationIsPredicateOf;
		this.reificationIsObjectOf =(IRI) reificationIsObjectOf;
	}
	public  IRI getReificationType() {
		return reificationType;
	}
	public  IRI getReificationSubject() {
		return reificationSubject;
	}
	public  IRI getReificationPredicate() {
		return reificationPredicate;
	}
	public  IRI getReificationObject() {
		return reificationObject;
	}
	public IRI getReificationIsSubjectOf() {
		return reificationIsSubjectOf;
	}
	public IRI getReificationIsPredicateOf() {
		return reificationIsPredicateOf;
	}
	public IRI getReificationIsObjectOf() {
		return reificationIsObjectOf;
	}


}
