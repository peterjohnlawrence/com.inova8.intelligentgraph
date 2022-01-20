/*
 * inova8 2020
 */
package com.inova8.olgap;

import java.util.Arrays;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.OLGAP;

import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.slf4j.Logger;

/**
 * The Class FactDebug.

 */
@Deprecated
public class FactDebug extends Evaluator implements Function {
	
	/** The logger. */
	private static final Logger logger   = LoggerFactory.getLogger(FactDebug.class);

	/**
	 * Instantiates a new fact debug.
	 */
	public FactDebug() {
		super();
		logger.info("Initiating FactDebug");
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAP.FACTDEBUG;
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
		//logger.debug("Trace Evaluate for <{}>, {} with args <{}>",tripleSource, tripleSource.getValueFactory(),Arrays.toString(args));
		if(args.length <3) {
			String message = "At least subject, predicate, and script arguments required";
			logger.error(message);
			return tripleSource.getValueFactory().createLiteral(message);
		}else {

			IRI subject ;
			IRI predicate;
			SimpleLiteral scriptLiteral;
			try {
				subject = (IRI) args[0];
				predicate = (IRI) args[1];
				scriptLiteral = (SimpleLiteral) args[2];
			} catch(Exception e) {
				String message ="Subject and predicate must be valid IRI, and script must be a literal";
				logger.error(message);
				return tripleSource.getValueFactory().createLiteral(message.toString());
			}
			try{
				Value[] argumentArray = Arrays.copyOfRange(args, 3, args.length);
				IntelligentGraphRepository source = sources.getSource(tripleSource,argumentArray );
				CustomQueryOptions customQueryOptions = source.getCustomQueryOptions(argumentArray);
				EvaluationContext evaluationContext = new EvaluationContext(customQueryOptions);
				evaluationContext.setTracing(true);
				Thing subjectThing = Thing.create(source, subject, evaluationContext);
				subjectThing.getFact(predicate,//new PredicateElement(source, predicate),
						scriptLiteral,customQueryOptions);
				logger.debug("Trace\r\n"+evaluationContext.getTrace());
				return tripleSource.getValueFactory().createLiteral(evaluationContext.getTrace());
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
