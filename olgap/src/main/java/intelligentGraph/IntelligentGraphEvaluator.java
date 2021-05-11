package intelligentGraph;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.Dataset;
import org.eclipse.rdf4j.query.Query.QueryType;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.ProjectionElemList;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

public class IntelligentGraphEvaluator extends AbstractCloseableIteration<BindingSet, QueryEvaluationException> {
	CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluator;
	//private AbstractCloseableIteration<? extends BindingSet, QueryEvaluationException> evaluator;
	private IntelligentGraphSail intelligentGraphSail;
	private final QueryContext queryContext;
	private Set<String> originalTargetNames;
	private ProjectionElemList originalProjectionElemList;
//	private Set<String> originalSourceNames;

	public IntelligentGraphEvaluator(CloseableIteration<? extends BindingSet, QueryEvaluationException> iter,
			QueryContext queryContext, ProjectionElemList originalProjectionElemList) {
		this.queryContext = queryContext;
		this.evaluator = //(AbstractCloseableIteration<? extends BindingSet, QueryEvaluationException>) 
				iter;
		this.originalProjectionElemList = originalProjectionElemList;
		if (this.originalProjectionElemList != null)
			this.originalTargetNames = originalProjectionElemList.getTargetNames();
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

	@SuppressWarnings("unchecked")
	public HashMap<String, IRI> getPrefixes() {
		return ((HashMap<String, IRI>) queryContext.getAttribute("prefixes"));
	}

	public TupleExpr getTupleExpr() {
		return ((TupleExpr) queryContext.getAttribute("tupleExpr"));
	}

	public Dataset getDataset() {
		return ((Dataset) queryContext.getAttribute("dataset"));
	}

	public Set<IRI> getDefaultGraphs() {
		if (getDataset() != null)
			return getDataset().getDefaultGraphs();
		else
			return null;
	}

	public QueryType getQueryType() {
		return ((QueryType) queryContext.getAttribute("queryType"));
	}

	public PathQLRepository getSource() {
		SailConnection conn = (SailConnection) queryContext.getAttribute("wrappedCon");
		return new PathQLRepository(new SailTripleSource(conn, true, null));
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		try {
			boolean next = getEvaluator().hasNext();
			return next;
		}catch (NoSuchElementException e) {
			close();
			throw e;
		}	
	}

	public String requiredElement(Binding bindingValue) {
		HashSet<String> sourceNames = new HashSet<String>();
		sourceNames.add(bindingValue.getName());
		if (originalProjectionElemList != null) {
			Set<String> targetElement = originalProjectionElemList.getTargetNamesFor(sourceNames);
			if (targetElement.iterator().hasNext())
				return targetElement.iterator().next();
			else if (originalTargetNames.contains(bindingValue.getName()))
				return bindingValue.getName();
			else
				return null;
		} else
			return null;

	}

	@Override
	public BindingSet next() throws QueryEvaluationException {
		switch (getQueryType()) {
		case GRAPH:
			return nextGraph();

		case BOOLEAN:
			return nextBoolean();

		case TUPLE:
			return nextTuple();

		default:
			return getEvaluator().next();
		}
	}

	private BindingSet nextBoolean() {

		return null;
	}

	private BindingSet nextGraph() {
		BindingSet nextBindingSet = getEvaluator().next();
		if (nextBindingSet.getValue("object").isLiteral()) {
			SimpleLiteral literalValue = (SimpleLiteral) (nextBindingSet.getValue("object"));
			if (Evaluator.getEngineNames().containsKey(literalValue.getDatatype())) {
				QueryBindingSet modifiedBindingSet = new QueryBindingSet();
				modifiedBindingSet.addBinding(nextBindingSet.getBinding("subject"));
				modifiedBindingSet.addBinding(nextBindingSet.getBinding("predicate"));
				PathQLRepository source = getSource();
				EvaluationContext evaluationContext = new EvaluationContext(getCustomQueryOptions(nextBindingSet),
						getDataset(), getPrefixes());
				Thing subjectThing = Thing.create(source, nextBindingSet.getValue("subject"), evaluationContext);
				try {
					pathQLModel.Resource fact = subjectThing.getFact(
							new PredicateElement(getSource(), (IRI) nextBindingSet.getValue("predicate")),
							literalValue);
					Binding modifiedBindingValue = new SimpleBinding("object", fact.getValue());
					modifiedBindingSet.addBinding(modifiedBindingValue);
					return modifiedBindingSet;
				} catch (Exception e) {
					Binding modifiedBindingValue = new SimpleBinding("object",
							literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())));
							//literal(e.getMessage()));
					modifiedBindingSet.addBinding(modifiedBindingValue);
					return modifiedBindingSet;
				}
			} else {
				return nextBindingSet;
			}
		} else {
			return nextBindingSet;
		}
	}

	private BindingSet nextTuple() throws QueryEvaluationException {
		BindingSet nextBindingSet = getEvaluator().next();
		QueryBindingSet modifiedBindingSet = new QueryBindingSet(); // Iterations.asSet(evaluator)
		for (Binding bindingValue : nextBindingSet) {
			//Only add bindings that were in the original projection
			String modifiedBindingValueName = requiredElement(bindingValue);
			if (modifiedBindingValueName != null) {
				if (bindingValue.getValue().isLiteral()) {
					SimpleLiteral literalValue = (SimpleLiteral) (bindingValue.getValue());
					if (Evaluator.getEngineNames().containsKey(literalValue.getDatatype())) {
						String bindingValueName = bindingValue.getName();
						List<StatementPattern> statementPatterns = SubjectPredicateCollector.process(getTupleExpr(),
								bindingValueName);
						if (!statementPatterns.isEmpty()) {
							//TODO not all variables need be in projection list so will not match, need the full list of variables to complete this
							StatementPattern boundStatement = findBound(statementPatterns, nextBindingSet);
							if (boundStatement != null) {
								Value subject = getSubjectValue(boundStatement, nextBindingSet);
								IRI predicate = getPredicateValue(boundStatement, nextBindingSet);
								ResponseType responseType = getResponseType(bindingValueName);
								EvaluationContext evaluationContext = new EvaluationContext(
										getCustomQueryOptions(nextBindingSet), getDataset(), getPrefixes());
								switch (responseType) {
								case VALUE:
									Thing subjectThing = Thing.create(getSource(), subject, evaluationContext);
									try {
										pathQLModel.Resource fact = subjectThing
												.getFact(new PredicateElement(getSource(), predicate), literalValue);
										Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
												fact.getValue());
										modifiedBindingSet.addBinding(modifiedBindingValue);
									} catch (Exception e) {
										Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
												literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())));
												//literal(e.getMessage()));
										modifiedBindingSet.addBinding(modifiedBindingValue);
									}
									break;
								case SCRIPT:
									modifiedBindingSet.addBinding(bindingValue);
									break;
								case TRACE:
									evaluationContext.setTracing(true);
									Thing subjectThingTrace = Thing.create(getSource(), subject, evaluationContext);
									subjectThingTrace.getFact(new PredicateElement(getSource(), predicate),
											literalValue);
									Binding modifiedBindingValueTrace = new SimpleBinding(modifiedBindingValueName,
											literal(evaluationContext.getTrace()));
									modifiedBindingSet.addBinding(modifiedBindingValueTrace);
									break;

								}
							} else {
								modifiedBindingSet.addBinding(bindingValue);
							}
						} else {
							EvaluationContext evaluationContext = new EvaluationContext(
									getCustomQueryOptions(nextBindingSet), getDataset());

							Thing subjectThing = Thing.create(getSource(), Evaluator.ANONTHING, evaluationContext);
							try {
								pathQLModel.Resource fact = subjectThing.getFact(
										new PredicateElement(getSource(), Evaluator.ANONPREDICATE), literalValue);
								Binding modifiedBindingValue = new SimpleBinding(bindingValue.getName(),
										fact.getValue());
								modifiedBindingSet.addBinding(modifiedBindingValue);
							} catch (Exception e) {
								Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
										literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())));
										//literal(e.getMessage()));
								modifiedBindingSet.addBinding(modifiedBindingValue);
							}
						}
					} else {
						modifiedBindingSet.addBinding(bindingValue);
					}
				} else {
					if (bindingValue.getName().equals(modifiedBindingValueName))
						modifiedBindingSet.addBinding(bindingValue);
					else {
						Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
								bindingValue.getValue());
						modifiedBindingSet.addBinding(modifiedBindingValue);
					}
				}
			}
		}

		return modifiedBindingSet;
	}

	private StatementPattern findBound(List<StatementPattern> statementPatterns, BindingSet nextBindingSet) {
		for (StatementPattern statementPattern : statementPatterns) {
			if (getSubjectValue(statementPattern, nextBindingSet) != null
					&& getPredicateValue(statementPattern, nextBindingSet) != null) {
				return statementPattern;
			}
		}
		return null;
	}

	private Value getSubjectValue(StatementPattern statementPattern, BindingSet nextBindingSet) {
		if (statementPattern.getSubjectVar().getValue() != null)
			return statementPattern.getSubjectVar().getValue();
		else if (nextBindingSet.getValue(statementPattern.getSubjectVar().getName()) != null) {
			return nextBindingSet.getValue(statementPattern.getSubjectVar().getName());
		} else {
			return null;
		}
	}

	private IRI getPredicateValue(StatementPattern statementPattern, BindingSet nextBindingSet) {
		if (statementPattern.getPredicateVar().getValue() != null)
			return (IRI) statementPattern.getPredicateVar().getValue();
		else if (nextBindingSet.getValue(statementPattern.getPredicateVar().getName()) != null) {
			return (IRI) nextBindingSet.getValue(statementPattern.getPredicateVar().getName());
		} else {
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
