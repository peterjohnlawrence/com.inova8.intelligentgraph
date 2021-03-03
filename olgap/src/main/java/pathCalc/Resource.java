package pathCalc;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;

import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathPatternProcessor.Resources;

import org.eclipse.rdf4j.model.Value;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;

abstract public class Resource {
	private Value superValue;
	protected final Logger logger = LogManager.getLogger(Resource.class);
	private Tracer tracer;
	private Stack<String> stack;
	protected HashMap<String,IRI> prefixes = new HashMap<String,IRI>();
	private Source source;
	private HashMap<String, Resource> cachedResources;
	protected HashMap<String, Resource> customQueryOptions;
	@Override
	public String toString() {
		if(getValue()!=null)
			return getValue().toString();
		else
			return null;
	}
	public Source getSource() {
		return source;
	}
	public HashMap<String, Resource> getCachedValues() {
		return getCachedResources();
	}
	public HashMap<String, Resource> getCustomQueryOptions() {
		return customQueryOptions;
	}
	public Tracer getTracer() {
		return tracer;
	}

	public Resource setTracer(Tracer tracer) {
		this.tracer = tracer;
		return this;
	}

	public Value getValue() {
		return getSuperValue();
	}

	public String getHTMLValue() {
		switch (getValue().getClass().getSimpleName()) {
		case "MemIRI":
		case "NativeIRI":
			return "<a href='" + ((IRI) getValue()).getLocalName() + "'>" + ((IRI) getValue()).stringValue() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal) getValue()).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal) getValue()).getDatatype().stringValue();
				return ((org.eclipse.rdf4j.model.Literal) getValue()).getLabel() + "^^<a href='" + datatype
						+ "' target='_blank'>" + localDatatype + "</a>";
			} catch (Exception e) {
				return ((org.eclipse.rdf4j.model.Literal) getValue()).toString() + "(unknown value class: "
						+ getValue().getClass().getSimpleName() + ")";
			}
		}
	}
	public String getHTMLValue(Value objectValue) {
		switch (objectValue.getClass().getSimpleName()) {
		case "MemIRI":
		case "NativeIRI":
			return "<a href='" + ((IRI) objectValue).getLocalName() + "'>" + ((IRI) objectValue).stringValue() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().stringValue();
				return ((org.eclipse.rdf4j.model.Literal) objectValue).getLabel() + "^^<a href='" + datatype
						+ "' target='_blank'>" + localDatatype + "</a>";
			} catch (Exception e) {
				return ((org.eclipse.rdf4j.model.Literal) objectValue).toString() + "(unknown value class: "
						+ getValue().getClass().getSimpleName() + ")";
			}
		}
	}
	protected boolean notTracing() {
		if (tracer != null)
			return false;
		else
			return true;
	}

	public void addTrace(Message message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	protected void addTrace(String message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	protected void addScript(String script) {
		if (tracer != null)
			tracer.addScript(script);
	}

	protected String addIRIHRef(IRI iri) {
		return "<a href='" + iri.stringValue() + "' target='_blank'>" + iri.getLocalName() + "</a>";
	}

	protected String addIRI(IRI iri) {
		if (tracer != null)
			return addIRIHRef(iri);
		else
			return "";
	}

	protected String addIRIHRef(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRIHRef(iri);
	}

	public String addIRI(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRI(iri);
	}

	protected String addThisIRI() {
		IRI iri = (IRI) getValue();
		return addIRI(iri);
	}

	protected void decrementTraceLevel() {
		if (tracer != null)
			tracer.decrementLevel();
	}

	protected void incrementTraceLevel() {
		if (tracer != null)
			tracer.incrementLevel();
	}

	protected String indentScriptForTrace(String script) {
		if (tracer != null)
			return tracer.indentScriptForTrace(script);
		else
			return null;
	}

	public Stack<String> getStack() {
		if(stack==null) {
			stack = new  Stack<String>();
		}
		return stack;
	}

	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}

	public Resource getFact(String predicatePattern) throws PathPatternException {
		return null;
	}

	public Resources getFacts(String predicatePattern) throws PathPatternException {
		return null;
	}
	public Resources getFacts( PredicateElement path) {
		return null;
	}
	public Resources getReifiedFacts(PredicateElement path) {
		return null;
	}
	public Resources getIsFactsOf( PredicateElement path) {
		return null;
	}
	public Resources getIsReifiedFactsOf(PredicateElement path) {
		return null;
	}
	public pathCalc.Resource getSignal(String signal) {
		return null;
	}
	String getLabel() {
		if (getValue() != null)
			return getValue().stringValue();
		else
			return null;
	}

	public String stringValue() {
		return getLabel();
	}

	public Boolean booleanValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseBoolean(getLabel());
		else
			return (Boolean) null;
	}

	public Byte byteValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseByte(getLabel());
		else
			return (Byte) null;
	}

	public Short shortValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseShort(getLabel());
		else
			return (Short) null;
	}

	public Integer integerValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseInt(getLabel());
		else
			return (Integer) null;
	}

	public Long longValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseLong(getLabel());
		else
			return (Long) null;
	}

	public Float floatValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseFloat(getLabel());
		else
			return (Float) null;
	}

	public Double doubleValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseDouble(getLabel());
		else
			return (Double) null;
	}

	public BigInteger bigIntegerValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseInteger(getLabel());
		else
			return (BigInteger) null;
	}

	public BigDecimal decimalValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseDecimal(getLabel());
		else
			return (BigDecimal) null;
	}

	public XMLGregorianCalendar calendarValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseCalendar(getLabel());
		else
			return (XMLGregorianCalendar) null;
	}

	public IRI convertQName(String predicateIRI) {
		predicateIRI=Source.trimIRIString( predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":");
		IRI predicate = null;
		if(predicateIRIParts[0].equals("a")) {
				predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			}
		else if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		}else {
			IRI namespace = getNamespace(predicateIRIParts[0]);
			if(namespace==null) {
				logger.error(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
				addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
			}else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}
	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}

	
	public HashMap<String, IRI> getPrefixes() {
		return prefixes;
	}
	public HashMap<String, Resource> getCachedResources() {
		if( cachedResources==null) {
			cachedResources= new HashMap<String, Resource> ();
		}
		return cachedResources;
	}
	public void setCachedResources(HashMap<String, Resource> cachedResources) {
		this.cachedResources = cachedResources;
	}
	public Value getSuperValue() {
		return superValue;
	}
	public void setSuperValue(Value superValue) {
		this.superValue = superValue;
	}
	public void setSource(Source source) {
		this.source = source;
	}
}
