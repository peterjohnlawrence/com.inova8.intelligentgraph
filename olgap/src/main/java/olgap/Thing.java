package olgap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;

public class Thing extends olgap.Value {
	Source source;
	private HashMap<String, olgap.Value> values;

	public Thing(Source source, org.eclipse.rdf4j.model.Value superValue) {
		this.superValue = superValue;
		this.source = source;
	}

	public HashMap<String, olgap.Value> getValues() {
		return values;
	}

	public void setValues(HashMap<String, olgap.Value> values) {
		this.values = values;
	}

	public String toString() {
		return superValue.toString();
	}
	public final olgap.Thing getThing(String subjectIRI) {
		return this.getThing( subjectIRI,null);
	}
	public final olgap.Thing getThing(String subjectIRI, HashMap<String, olgap.Value> customQueryOptions) {
		IRI subject = Source.getTripleSource().getValueFactory().createIRI(subjectIRI);
		return source.thingFactory(subject, this.getStack());
	}
	public final HashSet<olgap.Value> getFacts(IRI predicate) {
		return this.getFacts(predicate,null);
	}
	public final HashSet<olgap.Value> getFacts(IRI predicate, HashMap<String, olgap.Value> customQueryOptions) {
		addTrace(new ParameterizedMessage("Seeking values {} of subject {}", addIRI(predicate), addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements((IRI) superValue, predicate, null);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(), objectStatement.getObject(), getStack()));
			//objects.add(new  olgap.Value(tripleSource, shell, objectStatement.getObject()));
		}
		addTrace(new ParameterizedMessage("Retrieved objects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				values));
		return values;
	}
	public final HashSet<olgap.Value> getFacts(String predicateIRI) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getFacts(predicate);
	}
	public final HashSet<olgap.Value> getFacts(String predicateIRI, HashMap<String, olgap.Value> customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getFacts(predicate, customQueryOptions);
	}
	public final HashSet<olgap.Value> getIsFactsOf(IRI predicate) {
		return this.getIsFactsOf(predicate,null);
	}
	public final HashSet<olgap.Value> getIsFactsOf(IRI predicate, HashMap<String, olgap.Value> customQueryOptions) {
		addTrace(new ParameterizedMessage("Seeking subjects {} of {}", addIRI(predicate), addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements(null, predicate, (IRI) superValue);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(), objectStatement.getSubject(), getStack()));
		}
		addTrace(new ParameterizedMessage("Retrieved subjects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				values));
		return values;
	}
	public final HashSet<olgap.Value> getIsFactsOf(String predicateIRI) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getIsFactsOf(predicate);
	}
	public final HashSet<olgap.Value> getIsFactsOf(String predicateIRI,	HashMap<String, olgap.Value> customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getIsFactsOf(predicate, customQueryOptions);
	}
	public final olgap.Value getFact(IRI predicate, SimpleLiteral scriptString) {
		return this.getFact(predicate,scriptString,null);
	}
	public final olgap.Value getFact(IRI predicate, SimpleLiteral scriptString,	HashMap<String, olgap.Value> customQueryOptions) {
		olgap.Value result = this.handleScript(scriptString, predicate, customQueryOptions);
		return result;
	}

	public final olgap.Value getFact(String predicateIRI, HashMap<String, olgap.Value> customQueryOptions) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getFact(predicate, customQueryOptions);
	}
	public final olgap.Value getFact(IRI predicate) {
		return this.getFact(predicate, (HashMap<String, olgap.Value>)null);
	}
	public final olgap.Value getFact(IRI predicate, HashMap<String, olgap.Value> customQueryOptions) {
		String key = predicate.toString() + StringUtils.join(customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicate),
				addIRI(superValue), customQueryOptions));
		if (notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", addIRI(predicate), addIRI(superValue),
					result.getValue()));
			return result;
		} else {
			CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source
					.getTripleSource().getStatements((IRI) superValue, predicate, null);
			while (objectStatements.hasNext()) {
				Statement objectStatement = objectStatements.next();
				Value objectValue = objectStatement.getObject();
				SimpleLiteral literalValue;
				try {
					literalValue = (SimpleLiteral) objectValue;
					if (Evaluator.scriptEngines.containsKey(literalValue.getDatatype().getLocalName())) {

						olgap.Value result = getFact(predicate, literalValue, customQueryOptions);
						if (result != null) {

							values.put(key, result);
							addTrace(new ParameterizedMessage("Calculated {} of {} = {}", addIRI(predicate),
									addIRI(superValue), result.getHTMLValue()));
						}
						return result;
					} else {
						addTrace(new ParameterizedMessage("Retrieved literal {} of {} = {}", addIRI(predicate),
								addIRI(superValue), getHTMLValue(objectValue)));
						return source.valueFactory(getTracer(), objectValue, getStack());
					}
				} catch (Exception e) {
					addTrace(new ParameterizedMessage("Retrieved resource {} of {} = {}", addIRI(predicate),
							addIRI(superValue), getHTMLValue(objectValue)));
					return source.valueFactory(getTracer(), objectValue, getStack());
				}
			}
			addTrace(new ParameterizedMessage("Error: No predicate {} found for subject {}", addIRI(predicate),
					addThisIRI()));
			// It could be there are reified attributes associated with scripts or signals
			Value reifiedValue = getReifiedValue(predicate);
			if (reifiedValue != null) {
				return source.valueFactory(getTracer(), reifiedValue, getStack());
			}
			decrementTraceLevel();
			return null;
		}
	}

	private Value getReifiedValue(IRI predicate) {
		/*{
		 ?s <http://inova8.com/calc2graph/def/attribute.of.Item>  <superValue> .
		 ?s	<http://inova8.com/calc2graph/def/attribute.Property> <predicate> .
		 ?s	<http://inova8.com/calc2graph/def/attribute.producedBy.Signal> ?signal   
		}*/
		//Check if this thing is associated with one or more attributes that might refer to the required predicate
		CloseableIteration<? extends Statement, QueryEvaluationException> attributeOfItemStatements = Source
				.getTripleSource().getStatements(null, Source.getAttributeOfItem(), (IRI) superValue);
		while (attributeOfItemStatements.hasNext()) {
			Statement attributeOfItemStatement = attributeOfItemStatements.next();
			//Now check if this attribute is associated with required predicate
			Value attribute = attributeOfItemStatement.getSubject();
			CloseableIteration<? extends Statement, QueryEvaluationException> attributePropertyStatements = Source
					.getTripleSource().getStatements((IRI) attribute, Source.getAttributeProperty(), predicate);
			while (attributePropertyStatements.hasNext()) {
				//Now check if this attribute/predicate is associated with a signal
				CloseableIteration<? extends Statement, QueryEvaluationException> attributeProducedBySignalStatements = Source
						.getTripleSource().getStatements((IRI) attribute, Source.getAttributeProducedBySignal(), null);
				while (attributeProducedBySignalStatements.hasNext()) {
					//Statement attributeProducedBySignalStatement = 
					attributeProducedBySignalStatements.next();
					return Source.getTripleSource().getValueFactory().createLiteral(Math.random());
					//return attributeProducedBySignalStatement.getObject();
				}
			}
		}
		//No reified values found
		return null;
	}

	private String getHTMLValue(Value objectValue) {
		switch (objectValue.getClass().getSimpleName()) {
		case "MemIRI":
		case "NativeIRI":
			return "<a href='" + ((IRI) objectValue).getLocalName() + "'>" + ((IRI) objectValue).toString() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().stringValue();
				return ((org.eclipse.rdf4j.model.Literal) objectValue).getLabel() + "^^<a href='" + datatype
						+ "' target='_blank'>" + localDatatype + "</a>";
			} catch (Exception e) {
				return ((org.eclipse.rdf4j.model.Literal) objectValue).toString() + "(unknown value class: "
						+ superValue.getClass().getSimpleName() + ")";
			}
		}
	}

	protected final olgap.Value handleScript(SimpleLiteral scriptString, IRI predicate,	HashMap<String, olgap.Value> customQueryOptions) {
		String scriptCode = scriptString.getLabel();
		scriptCode = scriptCode.trim();
		if (scriptCode.startsWith("$$")) {
			String scriptIRI = scriptCode.substring(2);
			Resource scriptResource = Source.getTripleSource().getValueFactory().createIRI(scriptIRI);
			IRI scriptPropertyIRI = Source.getTripleSource().getValueFactory()
					.createIRI("http://inova8.com/calc2graph/def/scriptCode");
			CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = Source
					.getTripleSource().getStatements(scriptResource, scriptPropertyIRI, null);
			while (scriptStatements.hasNext()) {
				Statement scriptStatement = scriptStatements.next();
				SimpleLiteral scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
				return handleScript(scriptCodeliteral, predicate, customQueryOptions);
			}
			logger.error(new ParameterizedMessage("Reference script <{}> not found for subject {}", scriptIRI, this));
			return source.valueFactory(getTracer(),	 "**Script Reference Error**", getStack());
		} else {
			incrementTraceLevel();
			IRI cacheContextIRI = generateCacheContext(predicate, customQueryOptions);
			String scripttype = scriptString.getDatatype().getLocalName();
			addTrace(new ParameterizedMessage("Evaluating predicate {} of {}, by invoking <b>{}</b> script\n",
					addIRI(predicate), addIRI(superValue), scripttype));
			addScript(scriptString.getLabel());
			incrementTraceLevel();
			Object scriptResult = null;
			try {
				//Test to see if the same 'call' is on the stack
				//If so report that circular reference encountered
				//If not push on stack
				String stackKey = "<" + predicate.toString() + "> <" + this.toString() + ">; queryOptions="
						+ (customQueryOptions == null ? "" : customQueryOptions.toString()) + "\r\n";
				if (getStack().search(stackKey) < 1) {
					getStack().push(stackKey);
					CompiledScript compiledScriptCode = source.compiledScriptFactory(scriptString);
					ScriptContext scriptContext = new SimpleScriptContext();
					scriptContext.setAttribute("$tripleSource", Source.getTripleSource(), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$this", this, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$property",
							source.thingFactory(getTracer(), predicate, this.getStack()), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$customQueryOptions", customQueryOptions, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI),
							ScriptContext.ENGINE_SCOPE);
					scriptResult = compiledScriptCode.eval(scriptContext);
					olgap.Value result = returnResult(scriptResult, cacheContextIRI);
					//Since script complete ,pop from stack
					getStack().pop();
					decrementTraceLevel();
					addTrace(new ParameterizedMessage("Evaluated {} of {} =  {}", addIRI(predicate), addIRI(superValue),
							result.getHTMLValue()));
					decrementTraceLevel();
					return result;
				} else {
					ParameterizedMessage circularReferenceMessage = new ParameterizedMessage(
							"Circular reference encountered when evaluating {} of {}:", addIRI(predicate),
							addIRI(superValue));
					addTrace(circularReferenceMessage);
					addScript(getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
							.toString());
					logger.error(new ParameterizedMessage(
							"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
							predicate.stringValue(), ((IRI) superValue).stringValue(),
							getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
									.toString()));
					return source.valueFactory(getTracer(),	"**Circular Reference**", getStack());
				}
			} catch (ScriptException e) {
				decrementTraceLevel();
				ParameterizedMessage scriptFailedMesssage = new ParameterizedMessage(
						"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">{}</span></code >",
						StringEscapeUtils.escapeHtml4(e.getMessage()));
				logger.error(scriptFailedMesssage.getFormattedMessage());
				addTrace(scriptFailedMesssage);
				return source.valueFactory(getTracer(),	 "**Script Error**", getStack());
			}
		}
	}

	private final IRI generateCacheContext(IRI predicate, HashMap<String, olgap.Value> customQueryOptions) {
		String key = superValue.toString() + predicate.toString() + StringUtils.join(customQueryOptions);
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI = Source.getTripleSource().getValueFactory().createIRI("http://inova8.com/cat2graph/data/",
				cacheContext);
		return cacheContextIRI;
	}

	public olgap.Value returnResult(Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "Integer":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Integer) result), getStack());
			case "Double":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Double) result), getStack());
			case "BigDecimal":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((BigDecimal) result), getStack());
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				source.writeModelToCache(this, result, cacheContextIRI);
				return source.thingFactory(getTracer(), cacheContextIRI, getStack());
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return source.valueFactory(getTracer(),	 "**Handler Error**", getStack());
			}
		} else {
			return null;
		}
	}
	//	public void writeResultToCache(){
	////		source.writeModelToCache(result, cacheContext);
	//		
	//	}

	public String getLabel() {
		return superValue.stringValue();
	}

	public Value getValue() {
		return superValue;
	}

	public olgap.Value getSignal(String signal, HashMap<String, olgap.Value> customQueryOptions) {
		incrementTraceLevel();
		String[] elements = signal.split("/");

		Object result;
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			SEEQSource seeqSource = null;
			try {
				addTrace(new ParameterizedMessage("Fetching SEEQ signal {} with customQueryOptions {}", elements[5], customQueryOptions));
				seeqSource = source.seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], customQueryOptions);
				decrementTraceLevel();
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Double) result), getStack());
			} catch (ScriptException e) {
				return source.valueFactory(getTracer(),	 "**SEEQ Source Error**", getStack());
			}
		case "HTTP:":
			ParameterizedMessage httpMessage = new ParameterizedMessage("HTTP not supported signal source: {}", signal);
			logger.error(httpMessage.getFormattedMessage());
			addTrace(httpMessage);
			return source.valueFactory(getTracer(),	 "**HTTP Source Error**", getStack());
		default:
			ParameterizedMessage defaultMessage = new ParameterizedMessage("Unsupported signal source: {}", signal);
			logger.error(defaultMessage.getFormattedMessage());
			addTrace(defaultMessage);
			return source.valueFactory(getTracer(),	 "**Unsupported Source Error**", getStack());

		}
	}
}
