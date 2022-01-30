/*
 * inova8 2020
 */
package com.inova8.olgap;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import com.inova8.intelligentgraph.evaluator.Evaluator;
import com.inova8.intelligentgraph.vocabulary.OLGAP;

/**
 * The Class Trace.
 */
@Deprecated
public class Trace extends Evaluator implements Function{

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAP.TRACE;
	}

	/**
	 * Evaluate.
	 *
	 * @param valueFactory the value factory
	 * @param args the args
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@SuppressWarnings("static-access")
	@Override
	@Deprecated
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		try {
			boolean setTraceOn = ((SimpleLiteral)args[0]).booleanValue();
			Evaluator.setTrace( setTraceOn);

		}catch(Exception e) {
			//Do nothing but return state of trace
		}
		return valueFactory.createLiteral(super.trace);
	}
}
