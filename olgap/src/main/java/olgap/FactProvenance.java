/*
 * inova8 2020
 */
package olgap;

import java.util.Arrays;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.algebra.evaluation.ValueExprEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.function.Function;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathCalc.EvaluationContext;
import com.inova8.intelligentgraph.pathCalc.Evaluator;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.intelligentgraph.vocabulary.OLGAP;

// TODO: Auto-generated Javadoc
/**
 * The Class FactProvenance.
 */
@Deprecated
public class FactProvenance extends Evaluator implements Function {
	
	/** The logger. */
	private static final Logger logger   = LoggerFactory.getLogger(FactProvenance.class);

	/**
	 * Instantiates a new fact provenance.
	 */
	public FactProvenance()  {
		super();
		logger.info("Initiating FactProvenance");
	}
	
	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return  OLGAP.FACTPROVENANCE;
	}

	/**
	 * Evaluate.
	 * 
	 * Returns the calculation trace (if calculated) of the value of the predicate of the subject
	 *
	 * @param tripleSource the triple source
	 * @param args the args, args[0] subject, args[1] predicate 
	 * @return the value
	 * @throws ValueExprEvaluationException the value expr evaluation exception
	 */
	@Override
	public Value evaluate(TripleSource tripleSource, Value... args) throws ValueExprEvaluationException {
		logger.debug("Trace Evaluate for <{}>, {} with args <{}>",tripleSource, tripleSource.getValueFactory(),args);
		if(args.length <2) {
			String message ="At least subject, and predicate arguments required";
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
			try{
				Value[] argumentArray = Arrays.copyOfRange(args,2, args.length);
				IntelligentGraphRepository source = sources.getSource(tripleSource, argumentArray );
				CustomQueryOptions customQueryOptions = source.getCustomQueryOptions(argumentArray);
				EvaluationContext evaluationContext = new EvaluationContext(customQueryOptions);
				evaluationContext.setTracing(true);
				Thing subjectThing = Thing.create(source, subject, evaluationContext);
				//olgap.Value fact = 
				subjectThing.getFact("<"+ predicate.stringValue()+">");// PredicateElement(source,predicate));
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
