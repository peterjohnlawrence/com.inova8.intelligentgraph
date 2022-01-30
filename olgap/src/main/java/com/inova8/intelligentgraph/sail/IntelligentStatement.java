/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.evaluator.Evaluator;
import com.inova8.intelligentgraph.evaluator.IntelligentEvaluator;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;

/**
 * The Class IntelligentStatement.
 */
public class IntelligentStatement extends ContextStatement {

	/** The source. */
	final IntelligentGraphRepository source;
	
	/** The evaluation context. */
	final EvaluationContext evaluationContext;
	
	/** The custom query options. */
	final CustomQueryOptions customQueryOptions;
	
	/** The context statement. */
	final ContextStatement contextStatement;
	
	/** The contexts. */
	private Resource[] contexts;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5600312937126282355L;

	/**
	 * Instantiates a new intelligent statement.
	 *
	 * @param contextStatement the context statement
	 * @param source the source
	 * @param evaluationContext the evaluation context
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 */
	protected IntelligentStatement(ContextStatement contextStatement, IntelligentGraphRepository source, EvaluationContext evaluationContext, CustomQueryOptions customQueryOptions, Resource ... contexts) {
		super(contextStatement.getSubject(), contextStatement.getPredicate(), contextStatement.getObject(),contextStatement.getContext());
		this.contextStatement=contextStatement;
		this.source=source;
		this.evaluationContext=evaluationContext;
		this.customQueryOptions = customQueryOptions;
		this.contexts = contexts;
	}
	
	/**
	 * Instantiates a new intelligent statement.
	 *
	 * @param source the source
	 * @param evaluationContext the evaluation context
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 */
	protected IntelligentStatement( IntelligentGraphRepository source, EvaluationContext evaluationContext, CustomQueryOptions customQueryOptions, Resource ... contexts) {
		super(iri("http://null"), iri("http://null"), iri("http://null"),null);
		this.contextStatement=null;
		this.source=source;
		this.evaluationContext=evaluationContext;
		this.customQueryOptions = customQueryOptions;
		this.contexts = contexts;
	}
	
	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	@Override
	public Value getObject() {
		if(contextStatement!=null) {
			if( contextStatement.getObject().isLiteral()) {
				SimpleLiteral literalValue = (SimpleLiteral)(contextStatement.getObject());
				if(Evaluator.getEngineNames().containsKey(literalValue.getDatatype())){
					Thing subjectThing = Thing.create(getSource(), (IRI)getContext(), contextStatement.getSubject(), getEvaluationContext());	
					CustomQueryOptions customQueryOptions= CustomQueryOption.getCustomQueryOptions(getEvaluationContext().getContexts(),source.getRepositoryContext().getPrefixes());
					 try {
						//TODO  com.inova8.intelligentgraph.model.Resource fact = subjectThing.getFact(contextStatement.getPredicate(),literalValue,customQueryOptions, contexts);
						com.inova8.intelligentgraph.model.Resource fact =IntelligentEvaluator.processFactObjectValue(subjectThing,	contextStatement.getPredicate(),	literalValue,customQueryOptions,contexts);
						 return fact.getSuperValue();
					 }catch (Exception e) {
						 String exceptionMessage = "";
						 for (Throwable t = e.getCause(); t != null; t = t.getCause()) {
							exceptionMessage +=  t.getMessage()+"\n";
						 }			
						 if (exceptionMessage=="") exceptionMessage=e.getMessage();
						 if (exceptionMessage=="") exceptionMessage="Exception w/o message";
						 return  literal(exceptionMessage);
					 }
				}else {
					return contextStatement.getObject(); 
				}
			}	else {
				return contextStatement.getObject();
			}
		}else {
			return null;
		}
			
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public IntelligentGraphRepository getSource() {
		return source;
	}
	
	/**
	 * Gets the evaluation context.
	 *
	 * @return the evaluation context
	 */
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	
}
