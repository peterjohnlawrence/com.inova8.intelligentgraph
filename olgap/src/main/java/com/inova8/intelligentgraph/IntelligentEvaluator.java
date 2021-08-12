package com.inova8.intelligentgraph;

import static org.eclipse.rdf4j.model.util.Values.literal;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.pathCalc.Thing;

public class IntelligentEvaluator {
	private final  FactCache factCache=new FactCache();
	protected final Logger logger = LoggerFactory.getLogger(IntelligentEvaluator.class);
	private boolean factCacheDirty=false;
	public IntelligentEvaluator(IntelligentGraphSail intelligentGraphSail) {
	}

	public com.inova8.intelligentgraph.pathQLModel.Resource returnResult(Thing thing ,Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "NullValue":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((String) "null"), thing.getEvaluationContext());
			case "Integer":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((Integer) result), thing.getEvaluationContext());
			case "Double":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((Double) result), thing.getEvaluationContext());
			case "Float":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((Float) result), thing.getEvaluationContext());
			case "decimal":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigDecimal":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((BigDecimal) result), thing.getEvaluationContext());
			case "BigInteger":
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal((BigInteger) result), thing.getEvaluationContext());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				thing.getSource().writeModelToCache(thing, result, cacheContextIRI);
				return Thing.create(thing.getSource(), cacheContextIRI, thing.getEvaluationContext());
			case "Literal":
				Value content = ((com.inova8.intelligentgraph.pathQLModel.Literal) result).getValue();
				switch (((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName()) {
				case "int":
				case "integer":
					return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(),
							literal((BigInteger) ((com.inova8.intelligentgraph.pathQLModel.Literal) result).bigIntegerValue()),
							thing.getEvaluationContext());
				case "decimal":
					return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal(((com.inova8.intelligentgraph.pathQLModel.Literal) result).decimalValue()),
							thing.getEvaluationContext());
				case "double":
					return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal(((com.inova8.intelligentgraph.pathQLModel.Literal) result).doubleValue()),
							thing.getEvaluationContext());
				case "string": 
					return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal(content.stringValue()), thing.getEvaluationContext());
				default:
					logger.error("No literal handler found for result {} of class {}", result.toString(),
							((org.eclipse.rdf4j.model.Literal) content).getDatatype().getLocalName());
					return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
				}
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return com.inova8.intelligentgraph.pathQLModel.Resource.create(thing.getSource(), literal("**Handler Error**"), thing.getEvaluationContext());
			}
		} else {
			return null;
		}
	}
	public  void clearCache(Value... args) {
		factCacheDirty = true;
	}

	public FactCache getFactCache() {
		if(factCacheDirty) {
			factCache.clear();
			factCacheDirty = false;
		}
		return factCache;
	}

}
