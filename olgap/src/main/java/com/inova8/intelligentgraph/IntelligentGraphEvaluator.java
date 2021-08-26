/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

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

import com.inova8.intelligentgraph.IntelligentGraphSail.ResponseType;
import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathCalc.Prefixes;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.SCRIPT;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.eclipse.rdf4j.model.util.Values.iri;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

/**
 * The Class IntelligentGraphEvaluator.
 */
public class IntelligentGraphEvaluator extends AbstractCloseableIteration<BindingSet, QueryEvaluationException> {
	
	/** The evaluator. */
	CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluator;
	
	/** The intelligent graph sail. */
	//private AbstractCloseableIteration<? extends BindingSet, QueryEvaluationException> evaluator;
	private IntelligentGraphSail intelligentGraphSail;
	
	/** The query context. */
	private final QueryContext queryContext;
	/** The query context. */
	private  CustomQueryOptions  customQueryOptions = new CustomQueryOptions();	
	/** The original target names. */
	private Set<String> originalTargetNames;
	
	/** The original projection elem list. */
	private ProjectionElemList originalProjectionElemList;
	//	private Set<String> originalSourceNames;

	/**
	 * Instantiates a new intelligent graph evaluator.
	 *
	 * @param iter the iter
	 * @param queryContext the query context
	 * @param originalProjectionElemList the original projection elem list
	 */
	public IntelligentGraphEvaluator(CloseableIteration<? extends BindingSet, QueryEvaluationException> iter,
			QueryContext queryContext, ProjectionElemList originalProjectionElemList) {
		this.queryContext = queryContext;
		this.evaluator = iter;
		this.originalProjectionElemList = originalProjectionElemList;
		if (this.originalProjectionElemList != null)
			this.originalTargetNames = originalProjectionElemList.getTargetNames();
	}

	/**
	 * Gets the evaluator.
	 *
	 * @return the evaluator
	 */
	private CloseableIteration<? extends BindingSet, QueryEvaluationException> getEvaluator() {
		return evaluator;
	}

	/**
	 * Gets the value factory.
	 *
	 * @return the value factory
	 */
	public ValueFactory getValueFactory() {
		return intelligentGraphSail.getValueFactory();
	}

	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public Properties getParameters() {
		return ((IntelligentGraphSail) queryContext.getAttribute(IntelligentGraphConstants.SAIL)).getParameters();
	}


	public Prefixes getPrefixes() {
		return (Prefixes) queryContext.getAttribute(IntelligentGraphConstants.PREFIXES);
	}

	/**
	 * Gets the tuple expr.
	 *
	 * @return the tuple expr
	 */
	public TupleExpr getTupleExpr() {
		return ((TupleExpr) queryContext.getAttribute(IntelligentGraphConstants.TUPLEEXPR));
	}

	/**
	 * Gets the dataset.
	 *
	 * @return the dataset
	 */
	public Dataset getDataset() {
		return ((Dataset) queryContext.getAttribute(IntelligentGraphConstants.DATASET));
	}

	/**
	 * Gets the default graphs.
	 *
	 * @return the default graphs
	 */
	public Set<IRI> getDefaultGraphs() {
		if (getDataset() != null)
			return getDataset().getDefaultGraphs();
		else
			return null;
	}

	/**
	 * Gets the query type.
	 *
	 * @return the query type
	 */
	public QueryType getQueryType() {
		return ((QueryType) queryContext.getAttribute(IntelligentGraphConstants.QUERYTYPE));
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public IntelligentGraphRepository getSource() {
		SailConnection conn = (SailConnection) queryContext.getAttribute(IntelligentGraphConstants.INTELLIGENTGRAPHCONNECTION);
		return IntelligentGraphRepository.create((IntelligentGraphConnection) conn) ;	
	}

	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		try {
			boolean next = getEvaluator().hasNext();
			return next;
		} catch (NoSuchElementException e) {
			close();
			throw e;
		}
	}

	/**
	 * Required element.
	 *
	 * @param bindingValue the binding value
	 * @return the string
	 */
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

	/**
	 * Next.
	 *
	 * @return the binding set
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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

	/**
	 * Next boolean.
	 *
	 * @return the binding set
	 */
	private BindingSet nextBoolean() {
		return getEvaluator().next();
	}

	/**
	 * Next graph.
	 *
	 * @return the binding set
	 */
	private BindingSet nextGraph() {
		BindingSet nextBindingSet = getEvaluator().next();
		try {
			if (nextBindingSet.hasBinding("subject") && nextBindingSet.hasBinding("predicate")
					&& nextBindingSet.hasBinding("object")) {
				if (nextBindingSet.getValue("object").isLiteral()) {
					SimpleLiteral literalValue = (SimpleLiteral) (nextBindingSet.getValue("object"));
					if (Evaluator.getEngineNames().containsKey(literalValue.getDatatype())) {
						QueryBindingSet modifiedBindingSet = new QueryBindingSet();
						modifiedBindingSet.addBinding(nextBindingSet.getBinding("subject"));
						modifiedBindingSet.addBinding(nextBindingSet.getBinding("predicate"));
						IntelligentGraphRepository source = getSource();
						EvaluationContext evaluationContext = new EvaluationContext(
								customQueryOptions, getDataset(), getPrefixes());
						Thing subjectThing = Thing.create(source, nextBindingSet.getValue("subject"),
								evaluationContext);
						try {
							com.inova8.intelligentgraph.pathQLModel.Resource fact = subjectThing.getFact(
									(IRI) nextBindingSet.getValue("predicate"),	literalValue,customQueryOptions);
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
						return locateCustomQueryOptions(nextBindingSet);
					}
				} else {
					return locateCustomQueryOptions(nextBindingSet);
				}
			} else {
				//Incomplete s p o within dataset so could not calculate
				return nextBindingSet;
			}
		} catch (Exception e) {
			//Should not be any exceptions that are not handled, but even so ...
			return nextBindingSet;
		}
	}



	/**
	 * Next tuple.
	 *
	 * @return the binding set
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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
								customQueryOptions= getCustomQueryOptions(nextBindingSet);
								EvaluationContext evaluationContext = new EvaluationContext(
										customQueryOptions, getDataset(), getPrefixes());
								switch (responseType) {
								case VALUE:
									Thing subjectThing = Thing.create(getSource(), subject, evaluationContext);
									try {
										com.inova8.intelligentgraph.pathQLModel.Resource fact = subjectThing
												.getFact(predicate,	literalValue,customQueryOptions);
										Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
												fact.getValue());
										modifiedBindingSet.addBinding(modifiedBindingValue);
									} catch (Exception e) {
										Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
												literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())));
										modifiedBindingSet.addBinding(modifiedBindingValue);
									}
									break;
								case SCRIPT:
									modifiedBindingSet.addBinding(bindingValue);
									break;
								case TRACE:
									evaluationContext.setTracing(true);
									Thing subjectThingTrace = Thing.create(getSource(), subject, evaluationContext);
									try {								
										subjectThingTrace.getFact(predicate,literalValue,customQueryOptions);
										Binding modifiedBindingValueTrace = new SimpleBinding(modifiedBindingValueName,
												literal(evaluationContext.getTracer().getTrace().asHTML()));
										modifiedBindingSet.addBinding(modifiedBindingValueTrace);
									}catch(Exception e) {
										Binding modifiedBindingValueTrace = new SimpleBinding(modifiedBindingValueName,
												literal(evaluationContext.getTracer().getTrace().asHTML()));
										modifiedBindingSet.addBinding(modifiedBindingValueTrace);
									}
									break;
								}
							} else {
								modifiedBindingSet.addBinding(bindingValue);
							}
						} else {
							EvaluationContext evaluationContext = new EvaluationContext(
									getCustomQueryOptions(nextBindingSet), getDataset());
							Thing subjectThing = Thing.create(getSource(), SCRIPT.ANONTHING, evaluationContext);
							try {
								com.inova8.intelligentgraph.pathQLModel.Resource fact = subjectThing.getFact(
										SCRIPT.ANONPREDICATE,literalValue,null);
								Binding modifiedBindingValue = new SimpleBinding(bindingValue.getName(),
										fact.getValue());
								modifiedBindingSet.addBinding(modifiedBindingValue);
							} catch (Exception e) {
								Binding modifiedBindingValue = new SimpleBinding(modifiedBindingValueName,
										literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())));
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

	/**
	 * Find bound.
	 *
	 * @param statementPatterns the statement patterns
	 * @param nextBindingSet the next binding set
	 * @return the statement pattern
	 */
	private StatementPattern findBound(List<StatementPattern> statementPatterns, BindingSet nextBindingSet) {
		for (StatementPattern statementPattern : statementPatterns) {
			if (getSubjectValue(statementPattern, nextBindingSet) != null
					&& getPredicateValue(statementPattern, nextBindingSet) != null) {
				return statementPattern;
			}
		}
		return null;
	}

	/**
	 * Gets the subject value.
	 *
	 * @param statementPattern the statement pattern
	 * @param nextBindingSet the next binding set
	 * @return the subject value
	 */
	private Value getSubjectValue(StatementPattern statementPattern, BindingSet nextBindingSet) {
		if (statementPattern.getSubjectVar().getValue() != null)
			return statementPattern.getSubjectVar().getValue();
		else if (nextBindingSet.getValue(statementPattern.getSubjectVar().getName()) != null) {
			return nextBindingSet.getValue(statementPattern.getSubjectVar().getName());
		} else {
			return null;
		}
	}

	/**
	 * Gets the predicate value.
	 *
	 * @param statementPattern the statement pattern
	 * @param nextBindingSet the next binding set
	 * @return the predicate value
	 */
	private IRI getPredicateValue(StatementPattern statementPattern, BindingSet nextBindingSet) {
		if (statementPattern.getPredicateVar().getValue() != null)
			return (IRI) statementPattern.getPredicateVar().getValue();
		else if (nextBindingSet.getValue(statementPattern.getPredicateVar().getName()) != null) {
			return (IRI) nextBindingSet.getValue(statementPattern.getPredicateVar().getName());
		} else {
			return null;
		}
	}

	/**
	 * Gets the response type.
	 *
	 * @param bindingValueName the binding value name
	 * @return the response type
	 */
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

	/**
	 * Match postfix.
	 *
	 * @param bindingValueName the binding value name
	 * @param postfix the postfix
	 * @return true, if successful
	 */
	private boolean matchPostfix(String bindingValueName, String postfix) {
		if (postfix.length() > bindingValueName.length())
			return false;
		else
			return bindingValueName.substring(bindingValueName.length() - postfix.length()).equals(postfix);
	}

	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		getEvaluator().remove();

	}

	/**
	 * Gets the custom query options.
	 *
	 * @param nextBindingSet the next binding set
	 * @return the custom query options
	 */
	public CustomQueryOptions getCustomQueryOptions(BindingSet nextBindingSet) {
		if (nextBindingSet.size() == 0) {
			return null;
		} else {
			CustomQueryOptions customQueryOptions = new CustomQueryOptions();
			for (Binding bindingValue : nextBindingSet) {
				String customQueryOptionParameter = bindingValue.getName();
				Value customQueryOptionValue = bindingValue.getValue();
				customQueryOptions.put(customQueryOptionParameter,
						Resource.create(getSource(), customQueryOptionValue, null));
			}
			return customQueryOptions;
		}
	}
	private BindingSet locateCustomQueryOptions(BindingSet nextBindingSet) {
		if(nextBindingSet.getValue("subject").equals(iri("http://inova8.com/context"))) {
			String customQueryOptionParameter = ((IRI)(nextBindingSet.getValue("predicate"))).getLocalName();
			Value customQueryOptionValue =nextBindingSet.getValue("object");
			customQueryOptions.put(customQueryOptionParameter,
					Resource.create(getSource(), customQueryOptionValue, null));
		}
		return nextBindingSet;
	}
}
