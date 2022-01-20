/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathQLQueryIterator;

import java.util.NoSuchElementException;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.EmptyIteration;
import org.eclipse.rdf4j.common.iteration.LookAheadIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;

/**
 * The Class MatchJoinIterator.
 */
@Deprecated
public class MatchJoinIterator extends LookAheadIteration<BindingSet, QueryEvaluationException>{
	
	/** The strategy. */
	private final EvaluationStrategy strategy;


	/** The left iter. */
	private final CloseableIteration<BindingSet, QueryEvaluationException> leftIter;
	
	/** The right arg. */
	private final TupleExpr  rightArg;
	
	/** The right iter. */
	private volatile CloseableIteration<BindingSet, QueryEvaluationException> rightIter;

	
	/**
	 * Instantiates a new match join iterator.
	 *
	 * @param strategy the strategy
	 * @param leftIter the left iter
	 * @param rightArg the right arg
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	public MatchJoinIterator(EvaluationStrategy strategy, CloseableIteration<BindingSet, QueryEvaluationException> leftIter, TupleExpr  rightArg ) throws QueryEvaluationException {
		this.strategy = strategy;
		this.leftIter = leftIter;
		this.rightArg = rightArg;
		rightIter = new EmptyIteration<>();

		//rightArg.setAlgorithm(this);
	}
	
	/**
	 * Gets the next element.
	 *
	 * @return the next element
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	protected BindingSet getNextElement() throws QueryEvaluationException {
		try {
			while (rightIter.hasNext() || leftIter.hasNext()) {
				if (rightIter.hasNext()) {
					return rightIter.next();
				}

				// Right iteration exhausted
				rightIter.close();

				if (leftIter.hasNext()) {
					rightIter = strategy.evaluate(rightArg, leftIter.next());
				}
			}
		} catch (NoSuchElementException ignore) {
			// probably, one of the iterations has been closed concurrently in
			// handleClose();
		}

		return null;
	}
	
	/**
	 * Handle close.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	protected void handleClose() throws QueryEvaluationException {
		try {
			super.handleClose();
		} finally {
			try {
				leftIter.close();
			} finally {
				rightIter.close();
			}
		}
	}
}
