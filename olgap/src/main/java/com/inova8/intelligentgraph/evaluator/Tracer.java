/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

import java.util.ArrayList;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.Tracer;
import com.inova8.intelligentgraph.model.Resource;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;
import com.inova8.pathql.context.Prefixes;
import com.inova8.pathql.element.PredicateElement;

/**
 * The Class Tracer.
 */
public class Tracer {
	
	/** The trace string builder. */
	private StringBuilder traceStringBuilder = new StringBuilder("<ol style='list-style-type:none;'>");

	/** The level. */
	private int level = 0;

	/** The tracing. */
	private boolean tracing = false;

	/**
	 * Checks if is tracing.
	 *
	 * @return true, if is tracing
	 */
	public boolean isTracing() {
		return tracing;
	}

	/**
	 * Sets the tracing.
	 *
	 * @param tracing the tracing
	 * @return the tracer
	 */
	public Tracer setTracing(boolean tracing) {
		this.tracing = tracing;
		return this;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level.
	 *
	 * @param level the level
	 * @return the tracer
	 */
	public Tracer setLevel(int level) {
		if (tracing)
			this.level = level;
		return this;
	}

	/**
	 * Decrement level.
	 *
	 * @return the tracer
	 */
	public Tracer decrementLevel() {
		if (tracing)
			this.traceStringBuilder.append("</ol>");
		return this;
	}

	/**
	 * Increment level.
	 *
	 * @return the tracer
	 */
	public Tracer incrementLevel() {
		if (tracing)
			this.traceStringBuilder.append("<ol style='list-style-type:none;'>");
		;
		return this;
	}
	
	/**
	 * Clear.
	 */
	public void clear() {
		traceStringBuilder = new StringBuilder("<ol style='list-style-type:none;'>");
		//trace = new Trace();
	}
	
	/**
	 * Gets the trace.
	 *
	 * @return the trace
	 */
	public Trace getTrace() {
		return new Trace(this.traceStringBuilder.toString());
	}
	
	/**
	 * Gets the trace HTML.
	 *
	 * @return the trace HTML
	 */
	public String getTraceHTML() {
		if (tracing) {
			return this.traceStringBuilder.append("</ol>").toString();
		} else {
			return "empty";
		}
	}
	
	/**
	 * Gets the rendered trace.
	 *
	 * @return the rendered trace
	 */
	public String getRenderedTrace() {
		String trace = this.getTraceHTML();
		return new net.htmlparser.jericho.Source(trace).getRenderer().setMaxLineLength(Integer.MAX_VALUE).setNewLine(null).toString();
	}
	
	/**
	 * Indent script for trace.
	 *
	 * @param script the script
	 * @return the string
	 */
	public String indentScriptForTrace(String script) {
		String indentedScriptString = ("\n" + script).replace("\n", "\n" + StringUtils.repeat("\t", getLevel() + 1));
		return "<code>" + indentedScriptString + "</code>";
	}

	/**
	 * To HTML.
	 *
	 * @param s the s
	 * @return the string
	 */
	public static String toHTML(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("<br>");
				break;
			// We need Tab support here, because we print StackTraces as HTML
			case '\t':
				builder.append("&nbsp; &nbsp; &nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}

	/**
	 * Adds the trace.
	 *
	 * @param message the message
	 * @return the tracer
	 */
	public Tracer addTrace(String message) {
		this.traceStringBuilder.append("<li>").append(message).append("</li>").append("</li>");//.append("\r\n");
		return this;
	}
	
	/**
	 * Adds the paragraph.
	 *
	 * @return the tracer
	 */
	private Tracer addParagraph() {
		this.traceStringBuilder.append("<p>");//.append("\r\n");
		return this;
	}
	
	/**
	 * Adds the script.
	 *
	 * @param script the script
	 */
	private void addScript(String script) {
	this.traceStringBuilder.append("<li>").append(
					"<div  style='border: 1px solid black;'> <pre><code >" + toHTML(script) + "</code></pre></div>")
					.append("</li>");//.append("\r\n");
	}


	/**
	 * Adds the IRIH ref.
	 *
	 * @param iri the iri
	 * @return the string
	 */
	private String addIRIHRef(IRI iri) {
		return "<a href='" + iri.stringValue() + "' target='_blank'>" + iri.getLocalName() + "</a>";
	}


	/**
	 * Adds the IRI.
	 *
	 * @param iri the iri
	 * @return the string
	 */
	private String addIRI(IRI iri) {
		return addIRIHRef(iri);
	}


	/**
	 * Adds the IRIH ref.
	 *
	 * @param value the value
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private String addIRIHRef(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRIHRef(iri);
	}


	/**
	 * Adds the IRI.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String addIRI(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRI(iri);
	}

	/**
	 * Adds the service.
	 *
	 * @param service the service
	 * @return the string
	 */
	private String addService(String service) {
		return "<a href='" + service + "' target='_blank'>" + service + "</a>";
	}
	
	/**
	 * Adds the this IRI.
	 *
	 * @param thing the thing
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private String addThisIRI(Thing thing) {
		IRI iri = (IRI) thing.getValue();
		return addIRI(iri);
	}
	
	/**
	 * Gets the HTML value.
	 *
	 * @param objectValue the object value
	 * @return the HTML value
	 */
	private String getHTMLValue(Value objectValue) {
		switch (objectValue.getClass().getSimpleName()) {
		case "MemIRI":
		case "NativeIRI":
		case "SimpleIRI":
			return "<a href='" + ((IRI) objectValue).getLocalName() + "'>" + ((IRI) objectValue).stringValue() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().stringValue();
				return ((org.eclipse.rdf4j.model.Literal) objectValue).getLabel() + "^^<a href='" + datatype
						+ "' target='_blank'>" + localDatatype + "</a>";
			} catch (Exception e) {
				return ((org.eclipse.rdf4j.model.Literal) objectValue).toString() + "(unknown value class: "
						+ objectValue.getClass().getSimpleName() + ")";
			}
		}
	}
	
	
	/**
	 * Trace evaluating.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param scriptLiteral the script literal
	 */
	public void traceEvaluating(Thing thing, IRI predicate, SimpleLiteral scriptLiteral) {
		if(!tracing)return;
		String scripttype = scriptLiteral.getDatatype().getLocalName();
		addTrace(String.format("Evaluating predicate %s of %s, by invoking <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), scripttype));
		addScript(scriptLiteral.getLabel());
		incrementLevel();
	}
	
	/**
	 * Trace redirecting.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param scriptCode the script code
	 */
	public void traceRedirecting(Thing thing, IRI predicate, String scriptCode) {
		addTrace(String.format("Redirecting evaluation of predicate %s of %s, to <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), toHTML(scriptCode)));
		
	}
	
	/**
	 * Trace redirecting failed.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param scriptCode the script code
	 */
	public void traceRedirectingFailed(Thing thing, IRI predicate, String scriptCode) {
		addTrace(String.format("Redirecting evaluation failed for predicate %s of %s, to <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), scriptCode));
		
	}
	
	/**
	 * Trace facts.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void traceFacts(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Getting facts '%s' of %s ",
				toHTML(predicatePattern), addIRI(thing.getSuperValue())));
	}
	
	/**
	 * Trace paths.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void tracePaths(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Getting path from '%s' via %s ",
				toHTML(predicatePattern), addIRI(thing.getSuperValue())));
	}
	
	/**
	 * Trace facts.
	 *
	 * @param thing the thing
	 * @param pathQLValue the path QL value
	 * @param prefixes the prefixes
	 * @param contexts the contexts
	 */
	public void traceFacts(Thing thing, Value pathQLValue, Prefixes prefixes, org.eclipse.rdf4j.model.Resource ... contexts) {
		if(!tracing)return;
		
		addTrace(String.format("Getting facts  '%s' of %s",
				toHTML(pathQLValue.stringValue()), addIRI(thing.getSuperValue())));
		CustomQueryOptions customQueryOptions = CustomQueryOption.getCustomQueryOptions(contexts,prefixes);
		
		if(!customQueryOptions.isEmpty()) addTrace(String.format("...using options: [%s]", toHTML(customQueryOptions.toString())));
		ArrayList<org.eclipse.rdf4j.model.Resource> coreContexts = CustomQueryOption.getCoreContexts(contexts);
		if(!coreContexts.isEmpty())addTrace(String.format("...within contexts: %s",coreContexts.toString()));
	}
	
	/**
	 * Trace seeking.
	 *
	 * @param thing the thing
	 * @param predicateElement the predicate element
	 * @param customQueryOptions the custom query options
	 */
	public void traceSeeking(Thing thing, PredicateElement predicateElement, CustomQueryOptions customQueryOptions) {
		if(!tracing)return;
		addTrace(String.format("Seeking value %s of %s using customQueryOptions {}",
				addIRI(predicateElement.getPredicate()), addIRI(thing.getSuperValue()), customQueryOptions));
	}
	
	/**
	 * Trace retrieved.
	 *
	 * @param thing the thing
	 * @param predicateElement the predicate element
	 * @param result the result
	 */
	public void traceRetrieved(Thing thing, PredicateElement predicateElement, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Retrieved cache %s of %s = %s", predicateElement.toString(),
				addIRI(thing.getSuperValue()), getHTMLValue(result.getValue())));
	}
	
	/**
	 * Trace retrieved cache.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param result the result
	 */
	public void traceRetrievedCache(Thing thing, IRI predicate, Value result) {
		if(!tracing)return;
		addTrace(String.format("Retrieved cached value %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), getHTMLValue(result)));
	}
	
	/**
	 * Trace retrieved literal.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param value the value
	 */
	public void traceRetrievedLiteral(Thing thing, IRI predicate, Value value) {
		if(!tracing)return;
		addTrace(String.format("Retrieved literal %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), getHTMLValue(value)));
	}
	
	/**
	 * Trace calculated.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param result the result
	 */
	public void traceCalculated(Thing thing, IRI predicate, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Calculated %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), result.getHTMLValue()));
	}
	
	/**
	 * Trace evaluated.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param result the result
	 */
	public void traceEvaluated(Thing thing, IRI predicate, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Evaluated %s of %s =  %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), result.getHTMLValue()));
	}
	
	/**
	 * Trace no predicate.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 */
	public void traceNoPredicate(Thing thing, IRI predicate) {
		if(!tracing)return;
		addTrace(String.format("Error: No predicate %s found for subject %s", addIRI(predicate),
				addIRI(thing.getSuperValue())));
	}
	
	/**
	 * Trace circular reference.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param evaluationStack the evaluation stack
	 * @param stackKey the stack key
	 */
	public void traceCircularReference(Thing thing, IRI predicate, EvaluationStack evaluationStack, String stackKey) {
		if(!tracing)return;
		addTrace(String.format(
				"Circular reference encountered when evaluating %s of %s:", addIRI(predicate),
				addIRI(thing.getSuperValue())));
		addScript(evaluationStack.subList(evaluationStack.size() - evaluationStack.search(stackKey), evaluationStack.size())
				.toString());
	}
	
	/**
	 * Trace signal error.
	 *
	 * @param error the error
	 */
	public void traceSignalError(String error) {
		if(!tracing)return;
		addTrace(error);
	}
	
	/**
	 * Trace SEEQ.
	 *
	 * @param seeq the seeq
	 * @param customQueryOptions the custom query options
	 */
	public void traceSEEQ(String seeq, CustomQueryOptions customQueryOptions) {
		if(!tracing)return;
		addTrace(String.format("Fetching SEEQ signal %s with customQueryOptions %s", seeq,
				customQueryOptions));
	}
	
	/**
	 * Trace SEEQHTTP error.
	 *
	 * @param signal the signal
	 */
	public void traceSEEQHTTPError(String signal) {
		if(!tracing)return;
		addTrace(String.format("HTTP not supported signal source: %s", signal));
	}
	
	/**
	 * Trace SEEQ error.
	 *
	 * @param signal the signal
	 */
	public void traceSEEQError(String signal) {
		if(!tracing)return;
		addTrace(String.format("Unsupported signal source: %s", signal));
	}
	
	/**
	 * Trace script error.
	 *
	 * @param e the e
	 */
	public void traceScriptError(ScriptException e) {
		if(!tracing)return;
		addTrace(String.format(
				"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
				StringEscapeUtils.escapeHtml4(e.getMessage())));
	}
	
	/**
	 * Trace Q name error.
	 *
	 * @param qname the qname
	 */
	public void traceQNameError(String  qname) {
		if(!tracing)return;
		addTrace(String.format("Error identifying namespace of qName %s", qname));
	}
	
	/**
	 * Trace results cached.
	 *
	 * @param cacheService the cache service
	 * @param cacheContext the cache context
	 */
	public void traceResultsCached(String cacheService,String cacheContext) {
		addTrace(String.format("Results cached to service %s in graph %s",
				addService(cacheService), addService(cacheContext)));
	}
	
	/**
	 * Trace results cached error.
	 *
	 * @param cacheService the cache service
	 */
	public void traceResultsCachedError(String cacheService) {
		if(!tracing)return;
		addTrace(String.format("Results NOT cached to service:%s",
				addService(cacheService)));
	}
	
	/**
	 * Trace results cached no service.
	 */
	public void traceResultsCachedNoService() {
		if(!tracing)return;
		addTrace(String.format("No service to cached results"));
	}

	/**
	 * Trace fact return null.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void traceFactReturnNull(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Returned Null fact '%s' of %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
		
	}
	
	/**
	 * Trace path return null.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void tracePathReturnNull(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Returned Null path '%s' from %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
		
	}
	
	/**
	 * Trace fact next.
	 *
	 * @param thing the thing
	 * @param predicate the predicate
	 * @param value the value
	 */
	public void traceFactNext(Thing thing, Value predicate, Value value) {
		if(!tracing)return;
		addTrace(String.format("Next fact '%s' of %s = %s", toHTML(predicate.stringValue()), addIRI(thing.getSuperValue()),  value.stringValue()));
	}
	
	/**
	 * Trace fact return value.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 * @param nextFact the next fact
	 */
	public void traceFactReturnValue(Thing thing, String predicatePattern, Resource nextFact) {
		if(!tracing)return;
		addTrace(String.format("Returned fact '%s' of %s = %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), getHTMLValue(nextFact.getValue())));
		addParagraph();
	}
	
	/**
	 * Trace path return.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 * @param nextPath the next path
	 */
	public void tracePathReturn(Thing thing, String predicatePattern, PathBinding nextPath) {
		if(!tracing)return;
		addTrace(String.format("Returned path '%s' from %s as %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), nextPath.toString()));
		addParagraph();
	}
	
	/**
	 * Trace path return.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 * @param path the path
	 */
	public void tracePathReturn(Thing thing, String predicatePattern, Path path) {
		if(!tracing)return;
		addTrace(String.format("Returned path '%s' from %s as %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), path.toString()));
		addParagraph();
	}
	
	/**
	 * Trace fact return value.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 * @param value the value
	 */
	public void traceFactReturnValue(Thing thing, String predicatePattern, Value value) {
		if(!tracing)return;
		addTrace(String.format("Returned fact '%s' of %s = %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), getHTMLValue(value)));
		addParagraph();
	}
	
	/**
	 * Trace fact empty.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void traceFactEmpty(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("No fact '%s' of %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
	}
	
	/**
	 * Trace path empty.
	 *
	 * @param thing the thing
	 * @param predicatePattern the predicate pattern
	 */
	public void tracePathEmpty(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("No path '%s' from %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
	}
}
