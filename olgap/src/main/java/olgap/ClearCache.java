package olgap;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

public class ClearCache extends Evaluator implements Function{

	@Override
	public String getURI() {
		return NAMESPACE + "clearCache";
	}
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		Evaluator.sources.get(valueFactory);
		Source.clearCache();
		return valueFactory.createLiteral(true);
	}
}
