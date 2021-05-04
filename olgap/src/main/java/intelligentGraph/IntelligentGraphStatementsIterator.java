package intelligentGraph;

import java.util.Properties;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.evaluation.SailTripleSource;
import static org.eclipse.rdf4j.model.util.Values.literal;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathQLRepository.PathQLRepository;

public class IntelligentGraphStatementsIterator implements CloseableIteration< Statement, SailException> {
	private CloseableIteration<? extends Statement, SailException> statementsIterator;
	private IntelligentGraphSail intelligentGraphSail;
	private IntelligentGraphConnection intelligentGraphConnection;
	public IntelligentGraphStatementsIterator(
			CloseableIteration<? extends Statement, SailException> statementsIterator, IntelligentGraphSail intelligentGraphSail, IntelligentGraphConnection intelligentGraphConnection) {
		super();
		this.statementsIterator = statementsIterator;
		this.intelligentGraphSail = intelligentGraphSail;
		this.intelligentGraphConnection = intelligentGraphConnection;
	}
	public CloseableIteration<? extends Statement, SailException> getStatementsIterator() {
		return statementsIterator;
	}
	public ValueFactory getValueFactory() {
		return intelligentGraphSail.getValueFactory();
	}
	public Properties getParameters() {
		return intelligentGraphSail.getParameters();
	}
	public PathQLRepository getSource() {
		return new PathQLRepository(new SailTripleSource(intelligentGraphConnection,true,null));
	}
	@Override
	public boolean hasNext() throws SailException {

	 return	getStatementsIterator().hasNext();
	}

	@Override
	public Statement next() throws SailException {
		Statement nextStatement = getStatementsIterator().next();
		if( nextStatement.getObject().isLiteral()) {
			SimpleLiteral literalValue = (SimpleLiteral)(nextStatement.getObject());
			if(Evaluator.getEngineNames().containsKey(literalValue.getDatatype())){
				//TODO evaluate the script and replace literal with value
				
				EvaluationContext evaluationContext = new EvaluationContext();
				Thing subjectThing = Thing.create(getSource(), nextStatement.getSubject(), evaluationContext);	
				 pathQLModel.Resource fact = subjectThing.getFact(new PredicateElement(getSource(),nextStatement.getPredicate()),literalValue);
				 if( fact !=null) {
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), fact.getSuperValue(), nextStatement.getContext());
				 }else {
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), literal("null"), nextStatement.getContext());
				 }
			}else {
				return nextStatement;
			}
		}	else {
			return nextStatement;
		}
	}

	@Override
	public void remove() throws SailException {
		getStatementsIterator().remove();
		
	}

	@Override
	public void close() throws SailException {
		getStatementsIterator().close();	
	}


}
