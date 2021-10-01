/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathCalc;

import java.util.ArrayList;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;

import com.inova8.intelligentgraph.URNCustomQueryOptionsDecode;
import com.inova8.intelligentgraph.path.Path;
import com.inova8.intelligentgraph.path.PathBinding;

import com.inova8.intelligentgraph.pathCalc.Tracer;
import com.inova8.intelligentgraph.pathQLModel.Resource;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.PredicateElement;

/**
 * The Class Tracer.
 */
public class Tracer {
	private StringBuilder traceStringBuilder = new StringBuilder("<ol style='list-style-type:none;'>");

	private int level = 0;

	private boolean tracing = false;

	public boolean isTracing() {
		return tracing;
	}

	public Tracer setTracing(boolean tracing) {
		this.tracing = tracing;
		return this;
	}

	public int getLevel() {
		return level;
	}

	public Tracer setLevel(int level) {
		if (tracing)
			this.level = level;
		return this;
	}

	public Tracer decrementLevel() {
		if (tracing)
			this.traceStringBuilder.append("</ol>");
		return this;
	}

	public Tracer incrementLevel() {
		if (tracing)
			this.traceStringBuilder.append("<ol style='list-style-type:none;'>");
		;
		return this;
	}
	public void clear() {
		traceStringBuilder = new StringBuilder("<ol style='list-style-type:none;'>");
		//trace = new Trace();
	}
	public Trace getTrace() {
		return new Trace(this.traceStringBuilder.toString());
	}
	public String getTraceHTML() {
		if (tracing) {
			return this.traceStringBuilder.append("</ol>").toString();
		} else {
			return "empty";
		}
	}
	public String getRenderedTrace() {
		String trace = this.getTraceHTML();
		return new net.htmlparser.jericho.Source(trace).getRenderer().setMaxLineLength(Integer.MAX_VALUE).setNewLine(null).toString();
	}
	
	public String indentScriptForTrace(String script) {
		String indentedScriptString = ("\n" + script).replace("\n", "\n" + StringUtils.repeat("\t", getLevel() + 1));
		return "<code>" + indentedScriptString + "</code>";
	}

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

	public Tracer addTrace(String message) {
		this.traceStringBuilder.append("<li>").append(message).append("</li>").append("</li>");//.append("\r\n");
		return this;
	}
	private Tracer addParagraph() {
		this.traceStringBuilder.append("<p>");//.append("\r\n");
		return this;
	}
	private void addScript(String script) {
	this.traceStringBuilder.append("<li>").append(
					"<div  style='border: 1px solid black;'> <pre><code >" + toHTML(script) + "</code></pre></div>")
					.append("</li>");//.append("\r\n");
	}


	private String addIRIHRef(IRI iri) {
		return "<a href='" + iri.stringValue() + "' target='_blank'>" + iri.getLocalName() + "</a>";
	}


	private String addIRI(IRI iri) {
		return addIRIHRef(iri);
	}


	@SuppressWarnings("unused")
	private String addIRIHRef(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRIHRef(iri);
	}


	private String addIRI(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRI(iri);
	}

	private String addService(String service) {
		return "<a href='" + service + "' target='_blank'>" + service + "</a>";
	}
	@SuppressWarnings("unused")
	private String addThisIRI(Thing thing) {
		IRI iri = (IRI) thing.getValue();
		return addIRI(iri);
	}
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
	
	
	public void traceEvaluating(Thing thing, IRI predicate, SimpleLiteral scriptLiteral) {
		if(!tracing)return;
		String scripttype = scriptLiteral.getDatatype().getLocalName();
		addTrace(String.format("Evaluating predicate %s of %s, by invoking <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), scripttype));
		addScript(scriptLiteral.getLabel());
		incrementLevel();
	}
	public void traceRedirecting(Thing thing, IRI predicate, String scriptCode) {
		addTrace(String.format("Redirecting evaluation of predicate %s of %s, to <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), toHTML(scriptCode)));
		
	}
	public void traceRedirectingFailed(Thing thing, IRI predicate, String scriptCode) {
		addTrace(String.format("Redirecting evaluation failed for predicate %s of %s, to <b>%s</b> script\n",
				addIRI(predicate), addIRI(thing.getSuperValue()), scriptCode));
		
	}
	public void traceFacts(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Getting facts '%s' of %s ",
				toHTML(predicatePattern), addIRI(thing.getSuperValue())));
	}
	public void tracePaths(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Getting path from '%s' via %s ",
				toHTML(predicatePattern), addIRI(thing.getSuperValue())));
	}
	public void traceFacts(Thing thing, Value pathQLValue, Prefixes prefixes, org.eclipse.rdf4j.model.Resource ... contexts) {
		if(!tracing)return;
		
		addTrace(String.format("Getting facts  '%s' of %s",
				toHTML(pathQLValue.stringValue()), addIRI(thing.getSuperValue())));
		CustomQueryOptions customQueryOptions = URNCustomQueryOptionsDecode.getCustomQueryOptions(contexts,prefixes);
		
		if(!customQueryOptions.isEmpty()) addTrace(String.format("...using options: [%s]", toHTML(customQueryOptions.toString())));
		ArrayList<org.eclipse.rdf4j.model.Resource> coreContexts = URNCustomQueryOptionsDecode.getCoreContexts(contexts);
		if(!coreContexts.isEmpty())addTrace(String.format("...within contexts: %s",coreContexts.toString()));
	}
	public void traceSeeking(Thing thing, PredicateElement predicateElement, CustomQueryOptions customQueryOptions) {
		if(!tracing)return;
		addTrace(String.format("Seeking value %s of %s using customQueryOptions {}",
				addIRI(predicateElement.getPredicate()), addIRI(thing.getSuperValue()), customQueryOptions));
	}
	public void traceRetrieved(Thing thing, PredicateElement predicateElement, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Retrieved cache %s of %s = %s", predicateElement.toString(),
				addIRI(thing.getSuperValue()), getHTMLValue(result.getValue())));
	}
	public void traceRetrievedCache(Thing thing, IRI predicate, Value result) {
		if(!tracing)return;
		addTrace(String.format("Retrieved cached value %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), getHTMLValue(result)));
	}
	public void traceRetrievedLiteral(Thing thing, IRI predicate, Value value) {
		if(!tracing)return;
		addTrace(String.format("Retrieved literal %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), getHTMLValue(value)));
	}
	public void traceCalculated(Thing thing, IRI predicate, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Calculated %s of %s = %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), result.getHTMLValue()));
	}
	public void traceEvaluated(Thing thing, IRI predicate, Resource result) {
		if(!tracing)return;
		addTrace(String.format("Evaluated %s of %s =  %s", addIRI(predicate),
				addIRI(thing.getSuperValue()), result.getHTMLValue()));
	}
	public void traceNoPredicate(Thing thing, IRI predicate) {
		if(!tracing)return;
		addTrace(String.format("Error: No predicate %s found for subject %s", addIRI(predicate),
				addIRI(thing.getSuperValue())));
	}
	public void traceCircularReference(Thing thing, IRI predicate, EvaluationStack evaluationStack, String stackKey) {
		if(!tracing)return;
		addTrace(String.format(
				"Circular reference encountered when evaluating %s of %s:", addIRI(predicate),
				addIRI(thing.getSuperValue())));
		addScript(evaluationStack.subList(evaluationStack.size() - evaluationStack.search(stackKey), evaluationStack.size())
				.toString());
	}
	public void traceSEEQ(String seeq, CustomQueryOptions customQueryOptions) {
		if(!tracing)return;
		addTrace(String.format("Fetching SEEQ signal %s with customQueryOptions %s", seeq,
				customQueryOptions));
	}
	public void traceSEEQHTTPError(String signal) {
		if(!tracing)return;
		addTrace(String.format("HTTP not supported signal source: %s", signal));
	}
	public void traceSEEQError(String signal) {
		if(!tracing)return;
		addTrace(String.format("Unsupported signal source: %s", signal));
	}
	public void traceScriptError(ScriptException e) {
		if(!tracing)return;
		addTrace(String.format(
				"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">%s</span></code >",
				StringEscapeUtils.escapeHtml4(e.getMessage())));
	}
	public void traceQNameError(String  qname) {
		if(!tracing)return;
		addTrace(String.format("Error identifying namespace of qName %s", qname));
	}
	public void traceResultsCached(String cacheService,String cacheContext) {
		addTrace(String.format("Results cached to service %s in graph %s",
				addService(cacheService), addService(cacheContext)));
	}
	public void traceResultsCachedError(String cacheService) {
		if(!tracing)return;
		addTrace(String.format("Results NOT cached to service:%s",
				addService(cacheService)));
	}
	public void traceResultsCachedNoService() {
		if(!tracing)return;
		addTrace(String.format("No service to cached results"));
	}

	public void traceFactReturnNull(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Returned Null fact '%s' of %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
		
	}
	public void tracePathReturnNull(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("Returned Null path '%s' from %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
		
	}
	public void traceFactNext(Thing thing, Value predicate, Value value) {
		if(!tracing)return;
		addTrace(String.format("Next fact '%s' of %s = %s", toHTML(predicate.stringValue()), addIRI(thing.getSuperValue()),  value.stringValue()));
	}
	public void traceFactReturnValue(Thing thing, String predicatePattern, Resource nextFact) {
		if(!tracing)return;
		addTrace(String.format("Returned fact '%s' of %s = %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), getHTMLValue(nextFact.getValue())));
		addParagraph();
	}
	public void tracePathReturn(Thing thing, String predicatePattern, PathBinding nextPath) {
		if(!tracing)return;
		addTrace(String.format("Returned path '%s' from %s as %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), nextPath.toString()));
		addParagraph();
	}
	public void tracePathReturn(Thing thing, String predicatePattern, Path path) {
		if(!tracing)return;
		addTrace(String.format("Returned path '%s' from %s as %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), path.toString()));
		addParagraph();
	}
	public void traceFactReturnValue(Thing thing, String predicatePattern, Value value) {
		if(!tracing)return;
		addTrace(String.format("Returned fact '%s' of %s = %s",toHTML(predicatePattern), addIRI(thing.getSuperValue()), getHTMLValue(value)));
		addParagraph();
	}
	public void traceFactEmpty(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("No fact '%s' of %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
	}
	public void tracePathEmpty(Thing thing, String predicatePattern) {
		if(!tracing)return;
		addTrace(String.format("No path '%s' from %s found",toHTML(predicatePattern), addIRI(thing.getSuperValue())));
		addParagraph();
	}
}
