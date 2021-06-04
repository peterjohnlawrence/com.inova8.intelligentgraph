package pathQLResults;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.repository.RepositoryException;

import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathQLModel.Fact;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

public  class ResourceStatementResults extends ResourceResults {
	CloseableIteration<Statement, RepositoryException> statementSet;
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet,
			PathQLRepository source, PathElement pathElement) {

		super( source, pathElement);
		this.statementSet =statementSet;
	}
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet,  Thing thing ){
		super( thing);
		this.statementSet =statementSet;
	}
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet,  Thing thing, PathElement pathElement  ){
		super( thing,pathElement);
		this.statementSet =statementSet;
	}
	public ResourceStatementResults(CloseableIteration<Statement, RepositoryException> statementSet) {
		super();
		this.statementSet =statementSet;
	}
	public  Resource nextResource() {
		return thing;
		
	};

	protected	CloseableIteration<Statement, RepositoryException> getStatements() {
		return (CloseableIteration<Statement, RepositoryException>) statementSet;
	}
	@Override
	public void close() throws QueryEvaluationException {
		statementSet.close();		
	}
	@Override
	public void remove() throws QueryEvaluationException {
		statementSet.remove();	
	}
	public CloseableIteration<Statement, RepositoryException> getStatementSet() {
		return statementSet;
	}
	public Statement nextStatement() {
		return getStatementSet().next();
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return getStatementSet().hasNext();
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		 Statement next = getStatementSet().next();
		return Resource.create(thing.getSource(), next.getObject(), getEvaluationContext());
	}
	@Override
	public Fact nextFact() {
		 Statement next = getStatementSet().next();
		return new Fact(next.getSubject(), next.getPredicate(),next.getObject() );
	}
	@Override
	public IRI nextReifiedValue() {
		return null;
	}

}
