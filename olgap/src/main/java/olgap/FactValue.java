package olgap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import pathCalc.Source;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.Thing;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;


public class FactValue extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(FactValue.class);
	public FactValue()  {
		super();
		logger.info(new ParameterizedMessage("Initiating FactValue"));
	}

	@Override
	public String getURI() {
		return  OLGAPNAMESPACE + "factValue";
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
				Value[] argumentArray = Arrays.copyOfRange(args, 2, args.length);
				Source source = sources.getSource(tripleSource, argumentArray );
				HashMap<String, pathCalc.Resource> customQueryOptions = source.getCustomQueryOptions(argumentArray);
				
//				if(!sources.containsKey(tripleSource.hashCode()) ){
//					sources.put(tripleSource.hashCode(),  new Source(tripleSource));
//				}
//				Source source = sources.get(tripleSource.hashCode());
//				HashMap<String, olgap.Value> customQueryOptions = source.getCustomQueryOptions(Arrays.copyOfRange(args, 2, args.length));
				
				Thing subjectThing = source.thingFactory(null, subject,new Stack<String>(),customQueryOptions);
				pathCalc.Resource fact = subjectThing.getFact( new PredicateElement(predicate));
				if( fact != null && fact.getValue()!=null) {
					Value result = fact.getValue();
					logger.debug(new ParameterizedMessage("FactValue = {}",result));
					return  result;
				}else {
					return tripleSource.getValueFactory().createLiteral("");
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
