/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.evaluator.EvaluationContext;
import com.inova8.intelligentgraph.evaluator.EvaluationStack;
import com.inova8.intelligentgraph.evaluator.Tracer;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.results.ResourceResults;
import com.inova8.pathql.processor.PathPatternException;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;
import org.eclipse.rdf4j.model.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * The Class Resource.
 */
public abstract class Resource implements Value {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The predicate. */
	Predicate predicate =null;
	
	/** The subject. */
	Resource subject;
	
	/** The super value. */
	protected Value superValue;

	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(Resource.class);

	/** The evaluation context. */
	protected EvaluationContext evaluationContext;

//	protected ConcurrentHashMap<String, IRI> prefixes = new ConcurrentHashMap<String, IRI>();

	/** The source. */
private IntelligentGraphRepository source;

	/**
	 * Creates the.
	 *
	 * @param source the source
	 * @param value the value
	 * @param evaluationContext the evaluation context
	 * @return the resource
	 */
	public static Resource create(IntelligentGraphRepository source, Value value, EvaluationContext evaluationContext) {
		if(value!=null) {
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
				return Thing.create(source, (IRI) value, evaluationContext);
			}
		}else {
			return new Literal(null);
		}
	}
	
	/**
	 * Creates the.
	 *
	 * @param source the source
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param value the value
	 * @param evaluationContext the evaluation context
	 * @return the resource
	 */
	public static Resource create(IntelligentGraphRepository source, Resource subject, Predicate predicate, Value value, EvaluationContext evaluationContext) {
		Resource resource = Resource.create(source, value, evaluationContext);
		resource.subject =subject;
		resource.predicate =predicate;
		return resource;
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

	/**
	 * Instantiates a new resource.
	 *
	 * @param value the value
	 * @param evaluationContext the evaluation context
	 */
	protected Resource(Value value, EvaluationContext evaluationContext) {
		super();
		this.superValue = value;
		if (evaluationContext != null)
			this.evaluationContext = evaluationContext;
	}

	/**
	 * Instantiates a new resource.
	 *
	 * @param value the value
	 * @param customQueryOptions the custom query options
	 */
	public Resource(Value value, CustomQueryOptions customQueryOptions) {
		super();
		this.superValue = value;
		evaluationContext = new EvaluationContext(customQueryOptions);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		if (getValue() != null)
			return getValue().toString();
		else
			return null;
	}

	/**
	 * Gets the evaluation context.
	 *
	 * @return the evaluation context
	 */
	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public IntelligentGraphRepository getSource() {
		return source;
	}

	/**
	 * Gets the custom query options.
	 *
	 * @return the custom query options
	 */
	@Deprecated
	public CustomQueryOptions getCustomQueryOptions() {
		if (evaluationContext != null)
			return evaluationContext.getCustomQueryOptions();
		else
			return null;
	}

	/**
	 * Gets the tracer.
	 *
	 * @return the tracer
	 */
	@Deprecated
	public Tracer getTracer() {
		if (evaluationContext != null)
			return evaluationContext.getTracer();
		else
			return null;
	}

	/**
	 * Sets the tracer.
	 *
	 * @param tracer the new tracer
	 */
	public void setTracer(Tracer tracer) {
		if (evaluationContext != null)
			evaluationContext.setTracer(tracer);
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
		case "AbstractIRI":
		case "MemIRI":
		case "SimpleIRI":
		case "NativeIRI":
		case "GenericIRI":
		case "URIImpl":
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
	@Deprecated
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
						+ objectValue.getClass().getSimpleName() + ")";
			}
		}
	}


	/**
	 * Gets the stack.
	 *
	 * @return the stack
	 */
	public EvaluationStack getStack() {
		if (evaluationContext != null && evaluationContext.getStack() != null) {
			return evaluationContext.getStack();
		} else {
			return null;
		}
	}

	/**
	 * Search stack.
	 *
	 * @param stackKey the stack key
	 * @return true, if successful
	 */
	public boolean searchStack(String stackKey) {
		//		if (evaluationContext!=null && evaluationContext.getStack() != null) {
		//		if(evaluationContext.getStack().size()>10 ) return true;
		return evaluationContext.getStack().contains(stackKey);
		//		}else {
		//			return true;
		//		}
	}

	/**
	 * Push stack.
	 *
	 * @param stackKey the stack key
	 */
	public void pushStack(String stackKey) {
		evaluationContext.getStack().push(stackKey);
	}

	/**
	 * Pop stack.
	 */
	public void popStack() {
		evaluationContext.getStack().pop();
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	public abstract Resource addFact(String property, String value, IRI dataType);
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @param dataType the data type
	 * @return the resource
	 */
	public abstract Resource addFact(IRI property, String value, IRI dataType );
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	public abstract Resource addFact(IRI property, Value value);
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	public abstract Resource addFact(String property, Value value);
	
	/**
	 * Adds the fact.
	 *
	 * @param property the property
	 * @param value the value
	 * @return the resource
	 */
	public abstract Resource addFact(String property, String value);
	
	/**
	 * Gets the fact.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the fact
	 * @throws PathPatternException the path pattern exception
	 */
	public abstract Resource getFact(String predicatePattern, Value... bindValues) throws PathPatternException;

	/**
	 * Gets the facts.
	 *
	 * @param predicatePattern the predicate pattern
	 * @param bindValues the bind values
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	public abstract ResourceResults getFacts(String predicatePattern, Value... bindValues) throws PathPatternException;

	/**
	 * Gets the signal.
	 *
	 * @param signal the signal
	 * @return the signal
	 */
	public com.inova8.intelligentgraph.model.Resource getSignal(String signal) {
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

//	@Deprecated
//	public IRI convertQName(String predicateIRI) {
//		predicateIRI = Utilities.trimIRIString(predicateIRI);
//		String[] predicateIRIParts = predicateIRI.split(":");
//		IRI predicate = null;
//		if (predicateIRIParts[0].equals("a")) {
//			predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
//		} else if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
//			predicate = iri(predicateIRI);
//		} else {
//			IRI namespace = getNamespace(predicateIRIParts[0]);
//			if (namespace == null) {
//				logger.error(String.format("Error identifying namespace of qName %s", predicateIRI));
//				this.getEvaluationContext().getTracer().traceQNameError(predicateIRI);
//				//	addTrace(message);
//			} else {
//				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
//			}
//		}
//		return predicate;
//	}

//	@Deprecated
//	private IRI getNamespace(String namespaceString) {
//		IRI namespace = getPrefixes().get(namespaceString);
//		return namespace;
//	}

//	@Deprecated
//	public Prefixes getPrefixes() {
//		if (this.getEvaluationContext() != null)
//			return this.getEvaluationContext().getPrefixes();
//		else
//			return null;
//	}

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
	public void setSource(IntelligentGraphRepository source) {
		this.source = source;
	}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public  Resource getSubject() {
		return subject;
	};

	/**
	 * Gets the predicate.
	 *
	 * @return the predicate
	 */
	public  Predicate getPredicate() {
		return predicate;
	};

//	public abstract Object getSnippet();
//
//	public abstract Object getScore();

}
