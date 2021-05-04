package intelligentGraph;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.impl.SimpleBinding;
import org.eclipse.rdf4j.sail.SailConnection;
import org.eclipse.rdf4j.sail.evaluation.SailTripleSource;

import intelligentGraph.IntelligentGraphSail.ResponseType;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class IntelligentGraphEvaluator extends AbstractCloseableIteration<BindingSet, QueryEvaluationException> {
	CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluator;
	private IntelligentGraphSail intelligentGraphSail;
	private final QueryContext queryContext;

	public IntelligentGraphEvaluator(CloseableIteration<? extends BindingSet, QueryEvaluationException> iter,
			QueryContext queryContext) {
		this.queryContext = queryContext;
		this.evaluator = iter;
	}

	private CloseableIteration<? extends BindingSet, QueryEvaluationException> getEvaluator() {
		return evaluator;
	}

	public ValueFactory getValueFactory() {
		return intelligentGraphSail.getValueFactory();
	}

	public Properties getParameters() {
		return ((IntelligentGraphSail) queryContext.getAttribute("sail")).getParameters();
	}

	public TupleExpr getTupleExpr() {
		return ((TupleExpr) queryContext.getAttribute("tupleExpr"));
	}

	public PathQLRepository getSource() {
		SailConnection conn = (SailConnection) queryContext.getAttribute("wrappedCon");
		return new PathQLRepository(new SailTripleSource(conn, true, null));
		//return ((IntelligentGraphSail)queryContext.getAttribute("sail")).getSource();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return getEvaluator().hasNext();
	}

	@Override
	public BindingSet next() throws QueryEvaluationException {
		BindingSet nextBindingSet = getEvaluator().next();
		QueryBindingSet modifiedBindingSet = new QueryBindingSet();
		for (Binding bindingValue : nextBindingSet) {
			if (bindingValue.getValue().isLiteral()) {
				SimpleLiteral literalValue = (SimpleLiteral) (bindingValue.getValue());
				if (Evaluator.getEngineNames().containsKey(literalValue.getDatatype())) {
					String bindingValueName = bindingValue.getName();
					List<StatementPattern> statementPatterns = SubjectPredicateCollector.process(getTupleExpr(),
							bindingValueName);
					if (!statementPatterns.isEmpty()) {
						StatementPattern boundStatement = findBound(statementPatterns,nextBindingSet);
						if (boundStatement != null) {
							Value subject = getSubjectValue(boundStatement,nextBindingSet );
							IRI predicate = getPredicateValue(boundStatement,nextBindingSet );
							ResponseType responseType = getResponseType(bindingValueName);
							EvaluationContext evaluationContext = new EvaluationContext(
									getCustomQueryOptions(nextBindingSet));
							switch (responseType) {
							case VALUE:
								Thing subjectThing = Thing.create(getSource(), subject, evaluationContext);
								pathQLModel.Resource fact = subjectThing
										.getFact(new PredicateElement(getSource(), predicate), literalValue);
								Binding modifiedBindingValue = new SimpleBinding(bindingValue.getName(),
										fact.getValue());
								modifiedBindingSet.addBinding(modifiedBindingValue);
								break;
							case SCRIPT:
								modifiedBindingSet.addBinding(bindingValue);
								break;
							case TRACE:
								evaluationContext.setTracing(true);
								Thing subjectThingTrace = Thing.create(getSource(), subject, evaluationContext);

								//pathQLModel.Resource factTrace = 
								subjectThingTrace.getFact(new PredicateElement(getSource(), predicate), literalValue);
								Binding modifiedBindingValueTrace = new SimpleBinding(bindingValue.getName(),
										literal(evaluationContext.getTrace()));
								modifiedBindingSet.addBinding(modifiedBindingValueTrace);
								break;

							}
						}else {
							modifiedBindingSet.addBinding(bindingValue);
						}
					} else {
						EvaluationContext evaluationContext = new EvaluationContext(
								getCustomQueryOptions(nextBindingSet));
						Thing subjectThing = Thing.create(getSource(), Evaluator.ANONTHING, evaluationContext);
						pathQLModel.Resource fact = subjectThing
								.getFact(new PredicateElement(getSource(), Evaluator.ANONPREDICATE), literalValue);
						Binding modifiedBindingValue = new SimpleBinding(bindingValue.getName(), fact.getValue());
						modifiedBindingSet.addBinding(modifiedBindingValue);
					}
				} else {
					modifiedBindingSet.addBinding(bindingValue);
				}
			} else {
				modifiedBindingSet.addBinding(bindingValue);
			}
		}

		return modifiedBindingSet;
	}

	private StatementPattern findBound(List<StatementPattern> statementPatterns, BindingSet nextBindingSet) {
		for (StatementPattern statementPattern : statementPatterns) {
			if (getSubjectValue( statementPattern,   nextBindingSet)!=null
					&& getPredicateValue( statementPattern,  nextBindingSet)!=null ) {
				return statementPattern;
			}
		}
		return null;

	}
 private Value getSubjectValue(StatementPattern statementPattern,  BindingSet nextBindingSet) {
	 if(statementPattern.getSubjectVar().getValue() !=null)
		 return statementPattern.getSubjectVar().getValue();
	 else if(nextBindingSet.getValue(statementPattern.getSubjectVar().getName()) != null) {
		 return nextBindingSet.getValue(statementPattern.getSubjectVar().getName());		 
	 }else {
		 return null;
	 } 
 }
 private IRI getPredicateValue(StatementPattern statementPattern,  BindingSet nextBindingSet) {
	 if(statementPattern.getPredicateVar().getValue() !=null)
		 return (IRI) statementPattern.getPredicateVar().getValue();
	 else if(nextBindingSet.getValue(statementPattern.getPredicateVar().getName()) != null) {
		 return (IRI) nextBindingSet.getValue(statementPattern.getPredicateVar().getName());		 
	 }else {
		 return null;
	 } 
 }
	private ResponseType getResponseType(String bindingValueName) {
		ResponseType responseType;
		if (matchPostfix(bindingValueName, IntelligentGraphSail.SCRIPT_POSTFIX)) {
			responseType = ResponseType.SCRIPT;
		} else if (matchPostfix(bindingValueName, IntelligentGraphSail.TRACE_POSTFIX)) {
			responseType = ResponseType.TRACE;
		} else {
			responseType = ResponseType.VALUE;
		}
		return responseType;
	}

	private boolean matchPostfix(String bindingValueName, String postfix) {
		if (postfix.length() > bindingValueName.length())
			return false;
		else
			return bindingValueName.substring(bindingValueName.length() - postfix.length()).equals(postfix);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		getEvaluator().remove();

	}

	public HashMap<String, pathQLModel.Resource> getCustomQueryOptions(BindingSet nextBindingSet) {
		if (nextBindingSet.size() == 0) {
			return null;
		} else {
			HashMap<String, pathQLModel.Resource> customQueryOptions = new HashMap<String, pathQLModel.Resource>();
			for (Binding bindingValue : nextBindingSet) {
				String customQueryOptionParameter = bindingValue.getName();
				Value customQueryOptionValue = bindingValue.getValue();
				customQueryOptions.put(customQueryOptionParameter,
						Resource.create(getSource(), customQueryOptionValue, null));
			}
			return customQueryOptions;
		}
	}

}
