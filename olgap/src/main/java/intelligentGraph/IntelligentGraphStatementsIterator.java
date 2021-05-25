/*
 * inova8 2020
 */
package intelligentGraph;

import java.util.Properties;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.sail.SailException;
import static org.eclipse.rdf4j.model.util.Values.literal;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQLRepository.PathQLRepository;

/**
 * The Class IntelligentGraphStatementsIterator.
 */
public class IntelligentGraphStatementsIterator extends AbstractCloseableIteration< Statement, SailException> { // CloseableIteration
	
	/** The statements iterator. */
	private CloseableIteration<? extends Statement, SailException> statementsIterator;
	
	/** The intelligent graph sail. */ 
	//private AbstractCloseableIteration<? extends Statement, SailException> statementsIterator;
	private IntelligentGraphSail intelligentGraphSail;
	
	/** The intelligent graph connection. */
	private IntelligentGraphConnection intelligentGraphConnection;
	
	/** The contexts. */
	private Resource[] contexts;
	
	/**
	 * Instantiates a new intelligent graph statements iterator.
	 *
	 * @param statementsIterator the statements iterator
	 * @param intelligentGraphSail the intelligent graph sail
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @param contexts the contexts
	 */
	public IntelligentGraphStatementsIterator(
			CloseableIteration<? extends Statement, SailException> statementsIterator, 
			IntelligentGraphSail intelligentGraphSail, IntelligentGraphConnection intelligentGraphConnection, Resource... contexts) {
		super();
		this.statementsIterator = 	statementsIterator;
		this.intelligentGraphSail = intelligentGraphSail;
		this.intelligentGraphConnection = intelligentGraphConnection;
		this.contexts = contexts;
	}
	
	/**
	 * Gets the statements iterator.
	 *
	 * @return the statements iterator
	 */
	public CloseableIteration<? extends Statement, SailException> getStatementsIterator() {
		return statementsIterator;
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
		return intelligentGraphSail.getParameters();
	}
	
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public PathQLRepository getSource() {
		return PathQLRepository.create(intelligentGraphConnection);
		//return new PathQLRepository(intelligentGraphConnection);
	}
	
	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws SailException the sail exception
	 */
	@Override
	public boolean hasNext() throws SailException {

	 return	getStatementsIterator().hasNext();
	}

	/**
	 * Next.
	 *
	 * @return the statement
	 * @throws SailException the sail exception
	 */
	@Override
	public Statement next() throws SailException {
		Statement nextStatement = getStatementsIterator().next();
		if( nextStatement.getObject().isLiteral()) {
			SimpleLiteral literalValue = (SimpleLiteral)(nextStatement.getObject());
			if(Evaluator.getEngineNames().containsKey(literalValue.getDatatype())){
				PathQLRepository source = getSource();
				EvaluationContext evaluationContext = new EvaluationContext(source.getPrefixes(), contexts);
				Thing subjectThing = Thing.create(source, (IRI)nextStatement.getContext(), nextStatement.getSubject(), evaluationContext);	
				 try {
					 pathQLModel.Resource fact = subjectThing.getFact(nextStatement.getPredicate(),//new PredicateElement(getSource(),nextStatement.getPredicate()),
							 literalValue);
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), fact.getSuperValue(), nextStatement.getContext());
				 }catch (Exception e) {
					 String exceptionMessage = e.getMessage();
					 if (exceptionMessage==null) exceptionMessage="Exception w/o message";
					 return getValueFactory().createStatement(nextStatement.getSubject(), nextStatement.getPredicate(), literal(exceptionMessage), nextStatement.getContext());
				 }
			}else {
				return nextStatement;
			}
		}	else {
			return nextStatement;
		}
	}

	/**
	 * Removes the.
	 *
	 * @throws SailException the sail exception
	 */
	@Override
	public void remove() throws SailException {
		getStatementsIterator().remove();
		
	}



}
