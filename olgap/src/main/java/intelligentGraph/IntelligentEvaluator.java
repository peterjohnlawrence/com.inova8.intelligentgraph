package intelligentGraph;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pathCalc.Thing;

public class IntelligentEvaluator {
	private static final String NULLVALUERETURNED_EXCEPTION = "NullValueReturned";
	private static final String SCRIPTFAILED_EXCEPTION = "ScriptFailed";
	private static final String CIRCULARREFERENCE_EXCEPTION = "CircularReference";
	private static final String SCRIPTNOTFOUND_EXCEPTION = null;
	private static final String NULLVALUESCRIPT_EXCEPTION = null;
	private final  FactCache factCache=new FactCache();
	protected final Logger logger = LoggerFactory.getLogger(IntelligentEvaluator.class);
	private final IntelligentGraphSail intelligentGraphSail;
	public IntelligentEvaluator(IntelligentGraphSail intelligentGraphSail) {
		this.intelligentGraphSail=intelligentGraphSail;
	}

	public pathQLModel.Resource returnResult(Thing thing ,Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "NullValue":
				return pathQLModel.Resource.create(thing.getSource(), literal((String) "null"), thing.getEvaluationContext());
			case "Integer":
				return pathQLModel.Resource.create(thing.getSource(), literal((Integer) result), thing.getEvaluationContext());
			case "Double":
				return pathQLModel.Resource.create(thing.getSource(), literal((Double) result), thing.getEvaluationContext());
			case "Float":
				return pathQLModel.Resource.create(thing.getSource(), literal((Float) result), thing.getEvaluationContext());
			case "decimal":
				return pathQLModel.Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigDecimal":
				return pathQLModel.Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigInteger":
				return pathQLModel.Resource.create(thing.getSource(), literal((BigInteger) result), thing.getEvaluationContext());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				thing.getSource().writeModelToCache(thing, result, cacheContextIRI);
				return Thing.create(thing.getSource(), cacheContextIRI, thing.getEvaluationContext());
			case "Literal":
				Value content = ((pathQLModel.Literal) result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
				case "int":
				case "integer":
					return pathQLModel.Resource.create(thing.getSource(),
							literal((BigInteger) ((pathQLModel.Literal) result).bigIntegerValue()),
							thing.getEvaluationContext());
				case "decimal":
					return pathQLModel.Resource.create(thing.getSource(), literal(((pathQLModel.Literal) result).decimalValue()),
							thing.getEvaluationContext());
				case "double":
					return pathQLModel.Resource.create(thing.getSource(), literal(((pathQLModel.Literal) result).doubleValue()),
							thing.getEvaluationContext());
				case "string": 
					return pathQLModel.Resource.create(thing.getSource(), literal(content.stringValue()), thing.getEvaluationContext());
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),
							((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName());
					return pathQLModel.Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
				}
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return pathQLModel.Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
			}
		} else {
			return null;
		}
	}
	public  void clearCache(Value... args) {
		factCache.clear();
	}

	public FactCache getFactCache() {
		return factCache;
	}

}
