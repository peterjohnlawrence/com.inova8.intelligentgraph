package olgap;

import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

public class ClearCache extends Evaluator implements Function{
	private final Logger logger = LogManager.getLogger(ClearCache.class);
	public ClearCache() throws NoSuchAlgorithmException {
		super();
		logger.info(new ParameterizedMessage("Initiating ClearCache"));
	}
	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "clearCache";
	}
	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {

		//TODO
//		if(!sources.containsKey(tripleSource.hashCode()) ){
//			sources.put(tripleSource.hashCode(),  new Source(tripleSource));
//		}
//		Source source = sources.get(tripleSource.hashCode());
//		source.clearCache(args);
		//Workaround for now
		String keys = sources.getKeys().toString();
		clearCache();
		logger.error(new ParameterizedMessage("Caches cleared {}",keys));
		return tripleSource.getValueFactory().createLiteral(true);
	}
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		// TODO Auto-generated method stub
		return null;
	}
}
