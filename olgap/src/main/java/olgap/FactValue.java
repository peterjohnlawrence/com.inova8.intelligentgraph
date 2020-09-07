package olgap;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;


public class FactValue extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(FactValue.class);
	public FactValue() throws NoSuchAlgorithmException {
		super();
		logger.info(new ParameterizedMessage("Initiating FactValue"));
	}

	@Override
	public String getURI() {
		return  NAMESPACE + "factValue";
	}

	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
	
		logger.debug(new ParameterizedMessage("Evaluate for {} with args <{}>", tripleSource.getValueFactory(),args));
		if(args.length <2) {
			ParameterizedMessage message = new ParameterizedMessage("At least subject, and predicate arguments required");
			logger.error(message);
			return tripleSource.getValueFactory().createLiteral(message.toString());
		}else {

			IRI subject ;
			IRI predicate;
			try {
				subject = (IRI) args[0];
				predicate = (IRI) args[1];
			} catch(Exception e) {
				ParameterizedMessage message = new ParameterizedMessage("Subject and predicate must be valid IRI");
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message.toString());
			}
			try{
				if(!sources.containsKey(tripleSource.getValueFactory()) ){
					sources.put(tripleSource.getValueFactory(),  new Source(tripleSource));
				}
				Source source = sources.get(tripleSource.getValueFactory());
				HashMap<String, olgap.Value> customQueryOptions = source.getCustomQueryOptions(Arrays.copyOfRange(args, 2, args.length));
				Thing subjectThing = source.thingFactory(subject);
				olgap.Value fact = subjectThing.getFact( predicate, customQueryOptions);
				if( fact != null) {
					Value result = fact.getValue();
					logger.debug(new ParameterizedMessage("FactValue = {}",result));
					return  result;
				}else {
					return null;
				}			

			}catch(Exception e) {
				return args[2];
			}
		}
	}

	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		return null;
	}

}
