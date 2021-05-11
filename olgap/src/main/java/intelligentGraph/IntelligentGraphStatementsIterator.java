package intelligentGraph;

import java.util.Properties;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.sail.SailException;
import static org.eclipse.rdf4j.model.util.Values.literal;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathQLRepository.PathQLRepository;

public class IntelligentGraphStatementsIterator implements CloseableIteration< Statement, SailException> {
	private CloseableIteration<? extends Statement, SailException> statementsIterator;
	//private AbstractCloseableIteration<? extends Statement, SailException> statementsIterator;
	private IntelligentGraphSail intelligentGraphSail;
	private IntelligentGraphConnection intelligentGraphConnection;
	private Resource[] contexts;
	public IntelligentGraphStatementsIterator(
			CloseableIteration<? extends Statement, SailException> statementsIterator, 
			IntelligentGraphSail intelligentGraphSail, IntelligentGraphConnection intelligentGraphConnection, Resource... contexts) {
		super();
		this.statementsIterator = 	statementsIterator;
		this.intelligentGraphSail = intelligentGraphSail;
		this.intelligentGraphConnection = intelligentGraphConnection;
		this.contexts = contexts;
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
		return new PathQLRepository(intelligentGraphConnection);
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
				PathQLRepository source = getSource();
				EvaluationContext evaluationContext = new EvaluationContext(source.getPrefixes(), contexts);
				Thing subjectThing = Thing.create(source, nextStatement.getSubject(), evaluationContext);	
				 try {
					 pathQLModel.Resource fact = subjectThing.getFact(new PredicateElement(getSource(),nextStatement.getPredicate()),literalValue);
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), fact.getSuperValue(), nextStatement.getContext());
				 }catch (Exception e) {
					// return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), literal(StringEscapeUtils.escapeEcmaScript(e.getMessage())), nextStatement.getContext());
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), literal(e.getMessage()), nextStatement.getContext());
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
