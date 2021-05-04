/*
 * inova8 2020
 */
package pathQLModel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;

import pathCalc.EvaluationContext;
import pathCalc.Thing;
import pathCalc.Tracer;
import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLRepository.PathQLRepository;
import pathQLResults.ResourceResults;

import org.eclipse.rdf4j.model.Value;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;

 /**
  * The Class Resource.
  */
 public  abstract class Resource {
	
	/** The super value. */
	private Value superValue;
	
	/** The logger. */
	protected final Logger logger = LogManager.getLogger(Resource.class);
	protected EvaluationContext evaluationContext  = new EvaluationContext();	
	
	/** The prefixes. */
	protected HashMap<String,IRI> prefixes = new HashMap<String,IRI>();
	
	/** The source. */
	private  PathQLRepository source;
	
	/** The cached resources. */
	private HashMap<String, Resource> cachedResources;
	
	public static Resource create(PathQLRepository source, Value value,EvaluationContext evaluationContext) {
		switch (value.getClass().getSimpleName()) {
		case "SimpleLiteral":
		case "BooleanLiteral":
		case "BooleanLiteralImpl":
		case "CalendarLiteral":
		case "DecimalLiteral":
		case "IntegerLiteral":
		case "MemLiteral":
		case "BooleanMemLiteral":
		case "CalendarMemLiteral":
		case "DecimalMemLiteral":
		case "IntegerMemLiteral":
		case "NumericMemLiteral":
		case "NativeLiteral":
		case "NumericLiteral":
			return new Literal(value);
		default:
			return Thing.create(source,(IRI) value, evaluationContext);
		}
		
	}
	/**
	 * Instantiates a new resource.
	 *
	 * @param value the value
	 */
	protected Resource(Value value) {
		super();
		this.superValue = value;
	}
	protected Resource(Value value,EvaluationContext evaluationContext) {
		super();
		this.superValue = value;
		if (evaluationContext!=null)
			this.evaluationContext = evaluationContext;
	}	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		if(getValue()!=null)
			return getValue().toString();
		else
			return null;
	}
	
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}
	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public PathQLRepository getSource() {
		return source;
	}
	
//	/**
//	 * Gets the cached values.
//	 *
//	 * @return the cached values
//	 */
//	public HashMap<String, Resource> getCachedValues() {
//		return getCachedResources();
//	}
	
	/**
	 * Gets the custom query options.
	 *
	 * @return the custom query options
	 */
	public  HashMap<String, Resource> getCustomQueryOptions() {
		if (evaluationContext!=null)
			return evaluationContext.getCustomQueryOptions();
		else
			return null;
	}
	
	/**
	 * Gets the tracer.
	 *
	 * @return the tracer
	 */
	public Tracer getTracer() {
		if (evaluationContext!=null)
			return evaluationContext.getTracer();
		else
			return null;
	}

	/**
	 * Sets the tracer.
	 *
	 * @param tracer the tracer
	 * @return the resource
	 */
	public void setTracer(Tracer tracer) {
		if (evaluationContext!=null)
			 evaluationContext.setTracer(tracer);
//		this.tracer = tracer;
//		return this;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Value getValue() {
		return getSuperValue();
	}

	/**
	 * Gets the HTML value.
	 *
	 * @return the HTML value
	 */
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
	
	/**
	 * Gets the HTML value.
	 *
	 * @param objectValue the object value
	 * @return the HTML value
	 */
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
	
	/**
	 * Not tracing.
	 *
	 * @return true, if successful
	 */
	protected boolean notTracing() {
		if (evaluationContext!=null && evaluationContext.getTracer()!= null)
			return !evaluationContext.isTracing();
		else
			return true;
	}

	/** 
	 * Adds the trace.
	 *
	 * @param message the message
	 */
	public void addTrace(Message message) {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().addTrace(message);
	}

	/**
	 * Adds the trace.
	 *
	 * @param message the message
	 */
	protected void addTrace(String message) {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().addTrace(message);
	}

	/**
	 * Adds the script.
	 *
	 * @param script the script
	 */
	protected void addScript(String script) {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().addScript(script);
	}

	/**
	 * Adds the IRIH ref.
	 *
	 * @param iri the iri
	 * @return the string
	 */
	protected String addIRIHRef(IRI iri) {
		return "<a href='" + iri.stringValue() + "' target='_blank'>" + iri.getLocalName() + "</a>";
	}

	/**
	 * Adds the IRI.
	 *
	 * @param iri the iri
	 * @return the string
	 */
	protected String addIRI(IRI iri) {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			return addIRIHRef(iri);
		else
			return "";
	}

	/**
	 * Adds the IRIH ref.
	 *
	 * @param value the value
	 * @return the string
	 */
	protected String addIRIHRef(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRIHRef(iri);
	}

	/**
	 * Adds the IRI.
	 *
	 * @param value the value
	 * @return the string
	 */
	public String addIRI(org.eclipse.rdf4j.model.Value value) {
		IRI iri = (IRI) value;
		return addIRI(iri);
	}

	/**
	 * Adds the this IRI.
	 *
	 * @return the string
	 */
	protected String addThisIRI() {
		IRI iri = (IRI) getValue();
		return addIRI(iri);
	}

	/**
	 * Decrement trace level.
	 */
	protected void decrementTraceLevel() {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().decrementLevel();
	}

	/**
	 * Increment trace level.
	 */
	protected void incrementTraceLevel() {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().incrementLevel();
	}

	/**
	 * Indent script for trace.
	 *
	 * @param script the script
	 * @return the string
	 */
	protected String indentScriptForTrace(String script) {
		if (evaluationContext!=null && evaluationContext.getTracer() != null)
			return evaluationContext.getTracer().indentScriptForTrace(script);
		else
			return null;
	}

	/**
	 * Gets the stack.
	 *
	 * @return the stack
	 */
	public Stack<String> getStack() {
		if (evaluationContext!=null && evaluationContext.getStack() != null) {
			return evaluationContext.getStack();
		}else {
			return null;
		}
//		if(stack==null) {
//			stack = new  Stack<String>();
//		}
//		return stack;
	}

//	/**
//	 * Sets the stack.
//	 *
//	 * @param stack the new stack
//	 */
//	public void setStack(Stack<String> stack) {
//		this.stack = stack;
//	}

	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	public abstract  Resource getFact(String predicatePattern) throws PathPatternException ;

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	public abstract  ResourceResults getFacts(String predicatePattern) throws PathPatternException ;
	
	/**
	 * Gets the facts.
	 *
	 * @param path the path
	 * @return the facts
	 */
	public abstract ResourceResults getFacts( PredicateElement path) ;

/**
 * Gets the signal.
 *
 * @param signal the signal
 * @return the signal
 */

	public pathQLModel.Resource getSignal(String signal) {
		return null;
	}
	
	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	String getLabel() {
		if (getValue() != null)
			return getValue().stringValue();
		else
			return null;
	}

	/**
	 * String value.
	 *
	 * @return the string
	 */
	public String stringValue() {
		return getLabel();
	}

	/**
	 * Boolean value.
	 *
	 * @return the boolean
	 */
	public Boolean booleanValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseBoolean(getLabel());
		else
			return (Boolean) null;
	}

	/**
	 * Byte value.
	 *
	 * @return the byte
	 */
	public Byte byteValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseByte(getLabel());
		else
			return (Byte) null;
	}

	/**
	 * Short value.
	 *
	 * @return the short
	 */
	public Short shortValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseShort(getLabel());
		else
			return (Short) null;
	}

	/**
	 * Integer value.
	 *
	 * @return the integer
	 */
	public Integer integerValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseInt(getLabel());
		else
			return (Integer) null;
	}

	/**
	 * Long value.
	 *
	 * @return the long
	 */
	public Long longValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseLong(getLabel());
		else
			return (Long) null;
	}

	/**
	 * Float value.
	 *
	 * @return the float
	 */
	public Float floatValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseFloat(getLabel());
		else
			return (Float) null;
	}

	/**
	 * Double value.
	 *
	 * @return the double
	 */
	public Double doubleValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseDouble(getLabel());
		else
			return (Double) null;
	}

	/**
	 * Big integer value.
	 *
	 * @return the big integer
	 */
	public BigInteger bigIntegerValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseInteger(getLabel());
		else
			return (BigInteger) null;
	}

	/**
	 * Decimal value.
	 *
	 * @return the big decimal
	 */
	public BigDecimal decimalValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseDecimal(getLabel());
		else
			return (BigDecimal) null;
	}

	/**
	 * Calendar value.
	 *
	 * @return the XML gregorian calendar
	 */
	public XMLGregorianCalendar calendarValue() {
		if (getLabel() != null)
			return XMLDatatypeUtil.parseCalendar(getLabel());
		else
			return (XMLGregorianCalendar) null;
	}

	/**
	 * Convert Q name.
	 *
	 * @param predicateIRI the predicate IRI
	 * @return the iri
	 */
	public IRI convertQName(String predicateIRI) {
		predicateIRI=PathQLRepository.trimIRIString( predicateIRI);
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
	
	/**
	 * Gets the namespace.
	 *
	 * @param namespaceString the namespace string
	 * @return the namespace
	 */
	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}

	
	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public HashMap<String, IRI> getPrefixes() {
		if(this.getEvaluationContext()!=null )
			return this.getEvaluationContext().getPrefixes();
		else
			return null;
	}
	
	/**
	 * Gets the cached resources.
	 *
	 * @return the cached resources
	 */
	public HashMap<String, Resource> getCachedResources() {
		if( cachedResources==null) {
			cachedResources= new HashMap<String, Resource> ();
		}
		return cachedResources;
	}
	
	/**
	 * Sets the cached resources.
	 *
	 * @param cachedResources the cached resources
	 */
	public void setCachedResources(HashMap<String, Resource> cachedResources) {
		this.cachedResources = cachedResources;
	}
	
	/**
	 * Gets the super value.
	 *
	 * @return the super value
	 */
	public Value getSuperValue() {
		return superValue;
	}
	
	/**
	 * Sets the super value.
	 *
	 * @param superValue the new super value
	 */
	public void setSuperValue(Value superValue) {
		this.superValue = superValue;
	}
	
	/**
	 * Sets the source.
	 *
	 * @param source the new source
	 */
	public void setSource(PathQLRepository source) {
		this.source = source;
	}
	
	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public abstract Resource getSubject();
	
	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public abstract Resource getPredicate();
	
	/**
	 * Gets the snippet.
	 *
	 * @return the snippet
	 */
	public abstract Object getSnippet();
	
	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public abstract Object getScore();
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public abstract URI getId();
}
