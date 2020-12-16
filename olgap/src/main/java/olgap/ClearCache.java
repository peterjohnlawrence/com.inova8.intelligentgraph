package olgap;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

public class ClearCache extends Evaluator implements Function{

	@Override
	public String getURI() {
		return NAMESPACE + "clearCache";
	}
	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
		
		if(!sources.containsKey(tripleSource.hashCode()) ){
			sources.put(tripleSource.hashCode(),  new Source(tripleSource));
		}
		Source source = sources.get(tripleSource.hashCode());
		source.clearCache(args);
		return tripleSource.getValueFactory().createLiteral(true);
	}
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		// TODO Auto-generated method stub
		return null;
	}
}
