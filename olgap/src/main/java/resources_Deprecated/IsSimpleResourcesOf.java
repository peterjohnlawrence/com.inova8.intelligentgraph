package resources_Deprecated;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.QueryEvaluationException;

import pathCalc.Resource;
import pathCalc.Source;
import pathCalc.Tracer;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.Resources;
import pathPatternProcessor.Thing;
import pathPatternProcessor.Resources.Selector;
@Deprecated
public class IsSimpleResourcesOf extends Resources{

	private CloseableIteration<? extends Statement, QueryEvaluationException>statements;

	public IsSimpleResourcesOf( DeprecatedThing thing, PredicateElement predicateElement	){
		super( Resources.Selector.SUBJECT, thing, predicateElement.getReification(), predicateElement.getPredicate());
		statements = Source.getTripleSource().getStatements(null, predicate, (IRI) thing.getSuperValue() );
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return statements.hasNext();
	}

	@Override
	public Resource next() throws QueryEvaluationException {
		Statement objectStatement = statements.next();
		if (objectStatement==null) return null;
		switch (getSelector()) {
		case SUBJECT:
			return thing.getSource().resourceFactory(getTracer(), objectStatement.getSubject(), getStack(), getCustomQueryOptions(),getPrefixes());
		case PREDICATE:
			return thing.getSource().resourceFactory(getTracer(), objectStatement.getPredicate(), getStack(), getCustomQueryOptions(),getPrefixes());
		case OBJECT:
			return thing.getSource().resourceFactory(getTracer(), objectStatement.getObject(), getStack(), getCustomQueryOptions(),getPrefixes());
		case STATEMENT:
			break;
		default:
			break;
		}
		return null;

	}

	@Override
	public void remove() throws QueryEvaluationException {
		statements.remove();
	}

	@Override
	public void close() throws QueryEvaluationException {
		statements.close();

	}

	@SuppressWarnings("unused") 
	CloseableIteration<? extends Statement, QueryEvaluationException> getStatements() {
		return statements;
	}
	public DeprecatedThing getThing() {
		return thing;
	}
	protected Selector getSelector() {
		return selector;
	}

	protected Tracer getTracer() {
		return thing.getTracer();
	}

	protected HashMap<String, IRI> getPrefixes() {
		return thing.getPrefixes();
	}

	protected Stack<String> getStack() {
		return thing.getStack();
	}

	protected HashMap<String, Resource> getCustomQueryOptions() {
		return thing.getCustomQueryOptions();
	}

	public IRI getPredicate() {
		return predicate;
	}
//	public IRI getReificationType() {
//		return reificationType;
//	}
	@Override
	public Iterator<Resource> iterator() {
		return new CloseableIterationIterator<>(this);
	}

}
