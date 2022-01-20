/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import java.util.Properties;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.SailException;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;

/**
 * The Class IntelligentGraphStatementsIterator.
 */
public class IntelligentGraphStatementsIterator extends AbstractCloseableIteration< IntelligentStatement, SailException> { // CloseableIteration
	
	/** The statements iterator. */
	private CloseableIteration<? extends Statement, SailException> statementsIterator;
	
	/** The intelligent graph sail. */ 
	//private AbstractCloseableIteration<? extends Statement, SailException> statementsIterator;
	private IntelligentGraphSail intelligentGraphSail;
	
	/** The intelligent graph connection. */
	private IntelligentGraphConnection intelligentGraphConnection;
	private EvaluationContext evaluationContext;
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
		this.evaluationContext = new EvaluationContext(null/*getSource().getPrefixes()*/, contexts);
	}
	
	/**
	 * Gets the statements iterator.
	 *
	 * @return the statements iterator
	 */
	@SuppressWarnings("unchecked")
	public CloseableIteration<? extends IntelligentStatement, SailException> getStatementsIterator() {
		return (CloseableIteration< IntelligentStatement, SailException>) statementsIterator;
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
	public IntelligentGraphRepository getSource() {
		return IntelligentGraphRepository.create(intelligentGraphConnection);
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
	private EvaluationContext getEvaluationContext() {

	 return  evaluationContext;
	}

	/**
	 * Next.
	 *
	 * @return the statement
	 * @throws SailException the sail exception
	 */
	@Override
	public IntelligentStatement next() throws SailException {
		return  new IntelligentStatement(getStatementsIterator().next(),getSource(),getEvaluationContext(),null,this.contexts);
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
