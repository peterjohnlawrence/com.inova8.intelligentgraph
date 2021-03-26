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
import pathQLRepository.PathQLRepository;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;

//import groovy.lang.GroovyShell;

public class ObjectProvenance extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(ObjectProvenance.class);


	public ObjectProvenance()  {
		super();
		logger.info(new ParameterizedMessage("Initiating ObjectProvenance"));
	}

	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "objectProvenance";
	}

	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
	
		logger.traceEntry(new ParameterizedMessage("Evaluate for <{}> with args <{}>",tripleSource, args));
		if(args.length <3) {
			ParameterizedMessage message = new ParameterizedMessage("At least subject,predicate, and objectscript arguments required");
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
			SimpleLiteral literalValue;
			try{
				literalValue = (SimpleLiteral)args[2];
				if( isScriptEngine(literalValue.getDatatype().getLocalName())) {
					Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
					PathQLRepository source = sources.getSource(tripleSource, argumentArray );
					HashMap<String, pathQLModel.Resource> customQueryOptions = source.getCustomQueryOptions(argumentArray);
					
//					Source source;
//					if(!sources.containsKey(tripleSource.hashCode()) ){
//						source = new Source(tripleSource);
//						sources.put(tripleSource.hashCode(),  source);
//					}else {
//						source = sources.get(tripleSource.hashCode());
//					}
//					HashMap<String, olgap.Value> customQueryOptions = source.getCustomQueryOptions(Arrays.copyOfRange(args, 3, args.length));
					Tracer tracer = new Tracer();
					tracer.setTracing(true);
					Thing subjectThing = source.thingFactory(tracer,  subject, new Stack<String>(),customQueryOptions);
					pathQLModel.Resource fact = subjectThing.getFact( new PredicateElement(predicate),literalValue);
					if( fact != null) {
						fact.getValue();
						logger.debug("Trace\r\n"+tracer.getTrace());
						return tripleSource.getValueFactory().createLiteral(tracer.getTrace());
					}else {
						return null;
					}			
				}else {
					return  args[2];
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
