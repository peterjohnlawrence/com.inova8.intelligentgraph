package pathQLQueryIterator;

import java.util.NoSuchElementException;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.EmptyIteration;
import org.eclipse.rdf4j.common.iteration.LookAheadIteration;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;

public class MatchJoinIterator extends LookAheadIteration<BindingSet, QueryEvaluationException>{
	private final EvaluationStrategy strategy;


	private final CloseableIteration<BindingSet, QueryEvaluationException> leftIter;
	private final TupleExpr  rightArg;
	private volatile CloseableIteration<BindingSet, QueryEvaluationException> rightIter;

	
	public MatchJoinIterator(EvaluationStrategy strategy, CloseableIteration<BindingSet, QueryEvaluationException> leftIter, TupleExpr  rightArg ) throws QueryEvaluationException {
		this.strategy = strategy;
		this.leftIter = leftIter;
		this.rightArg = rightArg;
		rightIter = new EmptyIteration<>();

		//rightArg.setAlgorithm(this);
	}
	
	@Override
	protected BindingSet getNextElement() throws QueryEvaluationException {
		try {
			while (rightIter.hasNext() || leftIter.hasNext()) {
				if (rightIter.hasNext()) {
					return rightIter.next();
				}

				// Right iteration exhausted
				rightIter.close();

				if (leftIter.hasNext()) {;
					rightIter = strategy.evaluate(rightArg, leftIter.next());
				}
			}
		} catch (NoSuchElementException ignore) {
			// probably, one of the iterations has been closed concurrently in
			// handleClose()
		}

		return null;
	}
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
