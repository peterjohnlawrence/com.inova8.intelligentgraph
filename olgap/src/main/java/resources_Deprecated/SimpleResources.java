package resources_Deprecated;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iterator.CloseableIterationIterator;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.QueryParserUtil;

import pathCalc.Resource;
import pathCalc.Source;
import pathCalc.Tracer;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.Resources;
import pathPatternProcessor.Thing;
import pathPatternProcessor.Resources.Selector;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class SimpleResources extends Resources{

	private CloseableIteration<? extends Statement, QueryEvaluationException>statements;

	public SimpleResources( Thing thing, PredicateElement predicateElement	){
		//TODO
		super( Resources.Selector.OBJECT, thing, predicateElement.getReification(), predicateElement.getPredicate());
		
//		//TODO Test
//		EvaluationStrategy strategy = new StrictEvaluationStrategy(Source.getTripleSource(), null);
//		
//		String query = "SELECT * WHERE {VALUES(?this){(<http://inova8.com/calc2graph/id/BatteryLimit1> )} ?this ^<http://inova8.com/calc2graph/def/hasFeedBatteryLimit> ?n1 . ?n1 <http://inova8.com/calc2graph/def/hasProductBatteryLimit> ?n2 .?n2 <http://www.w3.org/2000/01/rdf-schema#label> 'BatteryLimit3'.?n2 <http://inova8.com/calc2graph/def/massFlow> ?result .}";
//		ParsedQuery pq = QueryParserUtil.parseQuery(QueryLanguage.SPARQL, query, null);
//		IRI  subject1IRI =  iri("http://inova8.com/calc2graph/id/Unit1");
//		IRI  property1IRI =  iri("http://inova8.com/calc2graph/def/hasFeedBatteryLimit");
//		IRI  object1IRI =  iri("http://inova8.com/calc2graph/id/BatteryLimit1");
//		Var  subject1 = new Var("s1",subject1IRI);
//		Var  property1 = new Var("p1",property1IRI);
//		Var  object1 = new Var("o1");
//		
//		IRI  subject2IRI =  iri("http://inova8.com/calc2graph/id/Unit1");
//		IRI  property2IRI =  iri("http://www.w3.org/2000/01/rdf-schema#label");
//		IRI  object2IRI =  iri("http://inova8.com/calc2graph/id/BatteryLimit1");
//		Var  subject2 = new Var("o1");
//		Var  property2 = new Var("p2",property2IRI);
//		Var  object2 = new Var("o2");
//		pq.getTupleExpr();
//		StatementPattern statementPattern1 = new StatementPattern(subject1,property1,object1);
//		StatementPattern statementPattern2 = new StatementPattern(subject2,property2,object2);
//		Join joinPattern1 = new Join(statementPattern1,statementPattern2);
//		BindingSet bindings = new QueryBindingSet();
//		//CloseableIteration<BindingSet, QueryEvaluationException> result = strategy.evaluate(pq.getTupleExpr(),bindings);
//		CloseableIteration<BindingSet, QueryEvaluationException> result = strategy.evaluate(joinPattern1,bindings);
//		if(result.hasNext()) {
//			BindingSet bs = result.next();
//			boolean s = bs.hasBinding("s");
//			boolean p = bs.hasBinding("p1");
//			boolean o = bs.hasBinding("o");
//		}
		
		
//		//
		
		
		
		statements = Source.getTripleSource().getStatements((IRI) thing.getSuperValue(), predicate, null);
	}
	public SimpleResources( Value subject ,IRI predicate, Value object	){
		//TODO
		super( Resources.Selector.OBJECT, null, null, null);
		statements = Source.getTripleSource().getStatements((org.eclipse.rdf4j.model.Resource)subject, predicate, object);
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
   CloseableIteration<? extends Statement, QueryEvaluationException> getStatements() {
		return statements;
	}
	public Thing getThing() {
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
