package olgap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.eclipse.rdf4j.model.IRI;

import java.util.Stack;

import org.apache.logging.log4j.LogManager;
public  class Value {

	protected org.eclipse.rdf4j.model.Value superValue;
	protected final Logger logger = LogManager.getLogger(Value.class);
	private Tracer tracer;
	private Stack<String> stack;
	@Override
	public String toString() {
		return superValue.toString() ;
	}
	public Tracer getTracer() {
		return tracer;
	}
	public Value setTracer(Tracer tracer) {
		this.tracer = tracer;
		return this;
	}
	public org.eclipse.rdf4j.model.Value getValue() {
		return superValue;	
	}
	public String getHTMLValue() {
		switch (superValue.getClass().getSimpleName()){
		case "MemIRI":
		case "NativeIRI":
			return   "<a href='" + ((IRI)superValue).getLocalName() + "'>" + ((IRI)superValue).toString() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal)superValue).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal)superValue).getDatatype().stringValue();
				return  ((org.eclipse.rdf4j.model.Literal)superValue).getLabel() + "^^<a href='" + datatype + "' target='_blank'>" + localDatatype + "</a>";
			}catch(Exception e) {
				return  ((org.eclipse.rdf4j.model.Literal)superValue).toString() + "(unknown value class: " + superValue.getClass().getSimpleName() + ")";
			}	
		}
	}
	protected boolean notTracing() {
		if(tracer!=null)
			return false;
		else
			return true;
	}
	protected void addTrace(Message message) {
		if (tracer!=null)
			tracer.addTrace(message);
	}
	protected void addTrace(String message) {
		if (tracer!=null)
			tracer.addTrace(message);
	}
	protected void addScript(String script) {
		if (tracer!=null)
			tracer.addScript(script);
	}
	protected String  addIRIHRef(IRI iri) {
		return "<a href='" + iri.stringValue() +"' target='_blank'>" + iri.getLocalName() + "</a>";
	}
	protected String  addIRI(IRI iri) {
		if (tracer!=null)
			return addIRIHRef(iri);
		else
			return "";
	}	
	protected String  addIRIHRef(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI)value;
		return addIRIHRef(iri);
	}
	protected String  addIRI(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI)value;
		return addIRI(iri);
	}
	protected String  addThisIRI() {
		IRI iri = (IRI)superValue;
		return addIRI(iri);
	}	
	protected void decrementTraceLevel() {
		if (tracer!=null)
			tracer.decrementLevel();
	}	
	protected void incrementTraceLevel() {
		if (tracer!=null)
			tracer.incrementLevel();
	}
	protected String indentScriptForTrace(String script) {
		if (tracer!=null)
			return tracer.indentScriptForTrace(script);
		else
			return null;
	}
	public Stack<String> getStack() {
		return stack;
	}
	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

}
