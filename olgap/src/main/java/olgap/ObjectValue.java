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
import pathPatternElement.PredicateElement;
import pathQLRepository.PathQLRepository;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;


public class ObjectValue extends Evaluator implements Function {
	private final Logger logger = LogManager.getLogger(ObjectValue.class);

	public ObjectValue()  {
		super();
		logger.info(new ParameterizedMessage("Initiating ObjectValue"));
	}

	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "objectValue";
	}

	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
	
		logger.debug(new ParameterizedMessage("Evaluate for <{}> ,{}> with args <{}>",tripleSource, tripleSource.getValueFactory(), args));
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
				ParameterizedMessage message = new ParameterizedMessage("Subject and predicate must be valid IRI. Subject {}, Object {}",args[0],args[1]);
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message.toString());
			}
			SimpleLiteral literalValue;
			try{
				literalValue = (SimpleLiteral)args[2];
				if( isScriptEngine(literalValue.getDatatype().getLocalName()) ) {		
					Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
					PathQLRepository source = sources.getSource(tripleSource, argumentArray );
					HashMap<String, pathQLModel.Resource> customQueryOptions = source.getCustomQueryOptions(argumentArray);
				
					Thing subjectThing = source.thingFactory( null, subject, new Stack<String>(),customQueryOptions);	
					
					pathQLModel.Resource fact = subjectThing.getFact(new PredicateElement(predicate),literalValue); 
					if( fact != null) {
						Value result = fact.getValue();
						//source.writeModelToCache(result, cacheContext);
						logger.debug(new ParameterizedMessage("ObjectValue = {}",result));
						return  result;
					}else {
						return null;
					}			
				}else {
					return args[2];
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
