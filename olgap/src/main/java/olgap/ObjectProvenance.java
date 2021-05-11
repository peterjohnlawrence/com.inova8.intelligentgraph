/*
 * inova8 2020
 */
package olgap;

import java.util.Arrays;
import java.util.HashMap;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;

import pathCalc.EvaluationContext;
import pathCalc.Evaluator;
import pathCalc.Thing;
import pathPatternElement.PredicateElement;
import pathQLRepository.PathQLRepository;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.LogManager;

//import groovy.lang.GroovyShell;

/**
 * The Class ObjectProvenance.
 */
public class ObjectProvenance extends Evaluator implements Function {
	
	/** The logger. */
	private final Logger logger = LogManager.getLogger(ObjectProvenance.class);


	/**
	 * Instantiates a new object provenance.
	 */
	public ObjectProvenance()  {
		super();
		logger.info(new ParameterizedMessage("Initiating ObjectProvenance"));
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
				if( isScriptEngine(literalValue.getDatatype())) {
					Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
					PathQLRepository source = sources.getSource(tripleSource, argumentArray );
					HashMap<String, pathQLModel.Resource> customQueryOptions = source.getCustomQueryOptions(argumentArray);
					EvaluationContext evaluationContext = new EvaluationContext(customQueryOptions);
					evaluationContext.setTracing(true);
					Thing subjectThing = Thing.create(source, subject, evaluationContext);	
					pathQLModel.Resource fact = subjectThing.getFact( new PredicateElement(source,predicate),literalValue);
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
