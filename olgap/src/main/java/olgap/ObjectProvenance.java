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

//import groovy.lang.GroovyShell;

/**
 * The Class ObjectProvenance.
 */
public class ObjectProvenance extends Evaluator implements Function {
	
	/** The logger. */
	private static final Logger logger   = LoggerFactory.getLogger(ObjectProvenance.class);


	/**
	 * Instantiates a new object provenance.
	 */
	public ObjectProvenance()  {
		super();
		logger.info("Initiating ObjectProvenance");
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAPNAMESPACE + "objectProvenance";
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
	
		logger.trace("Evaluate for <{}> with args <{}>",tripleSource, args);
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
				String message = "Subject and predicate must be valid IRI";
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message);
			}
			SimpleLiteral literalValue;
			try{
				literalValue = (SimpleLiteral)args[2];
				if( isScriptEngine(literalValue.getDatatype())) {
					Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
					PathQLRepository source = sources.getSource(tripleSource, argumentArray );
					CustomQueryOptions customQueryOptions = source.getCustomQueryOptions(argumentArray);
					EvaluationContext evaluationContext = new EvaluationContext(customQueryOptions);
					evaluationContext.setTracing(true);
					Thing subjectThing = Thing.create(source, subject, evaluationContext);	
					pathQLModel.Resource fact = subjectThing.getFact( predicate,//new PredicateElement(source,predicate),
							literalValue,customQueryOptions);
					if( fact != null) {
						fact.getValue();
						logger.debug("Trace\r\n"+evaluationContext.getTrace());
						return tripleSource.getValueFactory().createLiteral(evaluationContext.getTrace());
					}else {
						return null;
					}			
				}else {
					return  args[2];
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
