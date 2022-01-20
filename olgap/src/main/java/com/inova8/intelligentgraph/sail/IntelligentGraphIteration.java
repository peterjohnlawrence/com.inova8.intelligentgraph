package com.inova8.intelligentgraph.sail;

import org.eclipse.rdf4j.common.iteration.ExceptionConvertingIteration;
import org.eclipse.rdf4j.common.iteration.Iteration;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.sail.SailException;

/**
 * The Class IntelligentGraphIteration.
 *
 * @param <E> the element type
 */
public class IntelligentGraphIteration <E> extends ExceptionConvertingIteration<E, RepositoryException>{

	/**
	 * Instantiates a new intelligent graph iteration.
	 *
	 * @param iter the iter
	 */
	protected IntelligentGraphIteration(Iteration<? extends E, ? extends Exception> iter) {
		super(iter);
	}

	/**
	 * Convert.
	 *
	 * @param e the e
	 * @return the repository exception
	 */
	@Override
	protected RepositoryException convert(Exception e) {
		if (e instanceof SailException) {
			return new RepositoryException(e);
		} else if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else if (e == null) {
			throw new IllegalArgumentException("e must not be null");
		} else {
			throw new IllegalArgumentException("Unexpected exception type: " + e.getClass());
		}
	}

}
