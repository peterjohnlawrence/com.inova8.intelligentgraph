package intelligentGraph;

import static org.eclipse.rdf4j.model.util.Values.literal;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;

import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQLRepository.PathQLRepository;

public class IntelligentStatement extends ContextStatement {

	/**
	 * 
	 */
	final PathQLRepository source;
	final EvaluationContext evaluationContext;
	final ContextStatement contextStatement;
	private static final long serialVersionUID = 5600312937126282355L;

	protected IntelligentStatement(ContextStatement contextStatement, PathQLRepository source, EvaluationContext evaluationContext) {
		super(contextStatement.getSubject(), contextStatement.getPredicate(), contextStatement.getObject(),contextStatement.getContext());
		this.contextStatement=contextStatement;
		this.source=source;
		this.evaluationContext=evaluationContext;
	}
	@Override
	public Value getObject() {
		if( contextStatement.getObject().isLiteral()) {
			SimpleLiteral literalValue = (SimpleLiteral)(contextStatement.getObject());
			if(Evaluator.getEngineNames().containsKey(literalValue.getDatatype())){
				Thing subjectThing = Thing.create(getSource(), (IRI)getContext(), contextStatement.getSubject(), getEvaluationContext());	
				 try {
					 pathQLModel.Resource fact = subjectThing.getFact(contextStatement.getPredicate(),		 literalValue);
					 return fact.getSuperValue();
				 }catch (Exception e) {
					 String exceptionMessage = e.getMessage();
					 if (exceptionMessage==null) exceptionMessage="Exception w/o message";
					 return  literal(exceptionMessage);
				 }
			}else {
				return contextStatement.getObject();
			}
		}	else {
			return contextStatement.getObject();
		}
	}
	public PathQLRepository getSource() {
		return source;
	}
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	
}
