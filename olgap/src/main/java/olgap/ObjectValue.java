/*
 * inova8 2020
 */
package olgap;

import java.util.Arrays;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import pathCalc.CustomQueryOptions;
import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathQLRepository.PathQLRepository;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ObjectValue.
 */
public class ObjectValue extends Evaluator implements Function {
	
	/** The logger. */
	private static final Logger logger   = LoggerFactory.getLogger(ObjectValue.class);

	/**
	 * Instantiates a new object value.
	 */
	public ObjectValue()  {
		super();
		logger.info("Initiating ObjectValue");
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "objectValue";
	}

	/**
	 * Evaluate.
	 *
	 * @param tripleSource the triple source
	 * @param args the args
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
	
		logger.debug("Evaluate for <{}> ,{}> with args <{}>",tripleSource, tripleSource.getValueFactory(), args);
		if(args.length <3) {
			String message = "At least subject,predicate, and objectscript arguments required";
			logger.error(message);
			return tripleSource.getValueFactory().createLiteral(message);
		}else {

			IRI subject ;
			IRI predicate;
			try {
				subject = (IRI) args[0];
				predicate = (IRI) args[1];
			} catch(Exception e) {
				String message = String.format("Subject and predicate must be valid IRI. Subject %s, Object %s",args[0],args[1]);
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message);
			}
			SimpleLiteral literalValue;
			try{
				literalValue = (SimpleLiteral)args[2];
				if( isScriptEngine(literalValue.getDatatype()) ) {		
					Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
					PathQLRepository source = PathQLRepository.create(tripleSource);//sources.getSource(tripleSource, argumentArray );
					CustomQueryOptions customQueryOptions = source.getCustomQueryOptions(argumentArray);
				
					EvaluationContext evaluationContext = new EvaluationContext(customQueryOptions);
					//Thing subjectThing = source.thingFactory( null, subject, new Stack<String>(),customQueryOptions);	
					Thing subjectThing = Thing.create(source, subject, evaluationContext);	
					pathQLModel.Resource fact = subjectThing.getFact(predicate,//new PredicateElement(source,predicate),
							literalValue); 
					if( fact != null) {
						Value result = fact.getValue();
						//source.writeModelToCache(result, cacheContext);
						logger.debug("ObjectValue = {}",result);
						return  result;
					}else {
						return null;
					}			
				}else {
					return args[2];
				}
			}catch(Exception e) {
				return tripleSource.getValueFactory().createLiteral(e.getMessage());
			}
			
		}
	}

	/**
	 * Evaluate.
	 *
	 * @param valueFactory the value factory
	 * @param args the args
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@Override
	public Value evaluate(ValueFactory valueFactory, Value... args) throws ValueExprEvaluationException {
		return null;
	}

}
