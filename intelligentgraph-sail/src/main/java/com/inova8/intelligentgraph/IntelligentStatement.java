package com.inova8.intelligentgraph;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.URNCustomQueryOptionsDecode;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathQLModel.Thing;

public class IntelligentStatement extends ContextStatement {

	final IntelligentGraphRepository source;
	final EvaluationContext evaluationContext;
	final ICustomQueryOptions customQueryOptions;
	final ContextStatement contextStatement;
	private Resource[] contexts;
	private static final long serialVersionUID = 5600312937126282355L;

	protected IntelligentStatement(ContextStatement contextStatement, IntelligentGraphRepository source, EvaluationContext evaluationContext, ICustomQueryOptions customQueryOptions, Resource ... contexts) {
		super(contextStatement.getSubject(), contextStatement.getPredicate(), contextStatement.getObject(),contextStatement.getContext());
		this.contextStatement=contextStatement;
		this.source=source;
		this.evaluationContext=evaluationContext;
		this.customQueryOptions = customQueryOptions;
		this.contexts = contexts;
	}
	protected IntelligentStatement( IntelligentGraphRepository source, EvaluationContext evaluationContext, ICustomQueryOptions customQueryOptions, Resource ... contexts) {
		super(iri("http://null"), iri("http://null"), iri("http://null"),null);
		this.contextStatement=null;
		this.source=source;
		this.evaluationContext=evaluationContext;
		this.customQueryOptions = customQueryOptions;
		this.contexts = contexts;
	}
	@Override
	public Value getObject() {
		if(contextStatement!=null) {
			if( contextStatement.getObject().isLiteral()) {
				SimpleLiteral literalValue = (SimpleLiteral)(contextStatement.getObject());
				if(Evaluator.getEngineNames().containsKey(literalValue.getDatatype())){
					Thing subjectThing = Thing.create(getSource(), (IRI)getContext(), contextStatement.getSubject(), getEvaluationContext());	
					ICustomQueryOptions customQueryOptions= URNCustomQueryOptionsDecode.getCustomQueryOptions(getEvaluationContext().getContexts(),source.getIntelligentGraphConnection().getPrefixes());
					 try {
						 com.inova8.intelligentgraph.IResource.Resource fact = subjectThing.getFact(contextStatement.getPredicate(),literalValue,customQueryOptions, contexts);
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
	public IntelligentGraphRepository getSource() {
		return source;
	}
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	
}
