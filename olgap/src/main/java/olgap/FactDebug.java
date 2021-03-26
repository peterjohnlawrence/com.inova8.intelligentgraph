package olgap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import pathCalc.Thing;
import pathCalc.Tracer;
import pathPatternElement.PredicateElement;
import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;

public class FactDebug extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(FactDebug.class);

	public FactDebug() {
		super();
		logger.info(new ParameterizedMessage("Initiating FactDebug"));
	}

	@Override
	public String getURI() {
		return  OLGAPNAMESPACE + "factDebug";
	}

	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
		//logger.debug(new ParameterizedMessage("Trace Evaluate for <{}>, {} with args <{}>",tripleSource, tripleSource.getValueFactory(),Arrays.toString(args)));
		if(args.length <3) {
			ParameterizedMessage message = new ParameterizedMessage("At least subject, predicate, and script arguments required");
			logger.error(message);
			return tripleSource.getValueFactory().createLiteral(message.toString());
		}else {

			IRI subject ;
			IRI predicate;
			SimpleLiteral scriptLiteral;
			try {
				subject = (IRI) args[0];
				predicate = (IRI) args[1];
				scriptLiteral = (SimpleLiteral) args[2];
			} catch(Exception e) {
				ParameterizedMessage message = new ParameterizedMessage("Subject and predicate must be valid IRI, and script must be a literal");
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message.toString());
			}
			try{
				Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
				PathQLRepository source = sources.getSource(tripleSource,argumentArray );
				HashMap<String, Resource> customQueryOptions = source.getCustomQueryOptions(argumentArray);
				
//				if(!sources.containsKey(tripleSource.hashCode()) ){
//					sources.put(tripleSource.hashCode(),  new Source(tripleSource));
//				}
//				Source source = sources.get(tripleSource.hashCode());
//				HashMap<String, olgap.Value> customQueryOptions = source.getCustomQueryOptions(Arrays.copyOfRange(args, 3, args.length));
				
				
				Tracer tracer = new Tracer();
				tracer.setTracing(true);
				Thing subjectThing = source.thingFactory(tracer, subject, new Stack<String>(),customQueryOptions);
				subjectThing.getFact(new PredicateElement( predicate), scriptLiteral);
				logger.debug("Trace\r\n"+tracer.getTrace());
				return tripleSource.getValueFactory().createLiteral(tracer.getTrace());
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
