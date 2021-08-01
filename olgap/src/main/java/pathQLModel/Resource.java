/*
 * inova8 2020
 */
package pathQLModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;

import pathCalc.CustomQueryOptions;
import pathCalc.EvaluationContext;
import pathCalc.EvaluationStack;
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
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.XMLGregorianCalendar;

public abstract class Resource implements Value {

	private static final long serialVersionUID = 1L;

	private Value superValue;

	protected final Logger logger = LoggerFactory.getLogger(Resource.class);

	protected EvaluationContext evaluationContext;

	protected ConcurrentHashMap<String, IRI> prefixes = new ConcurrentHashMap<String, IRI>();

	private PathQLRepository source;

	public static Resource create(PathQLRepository source, Value value, EvaluationContext evaluationContext) {
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

	}

	protected Resource(Value value) {
		super();
		this.superValue = value;
	}

	protected Resource(Value value, EvaluationContext evaluationContext) {
		super();
		this.superValue = value;
		if (evaluationContext != null)
			this.evaluationContext = evaluationContext;
	}

	public Resource(Value value, CustomQueryOptions customQueryOptions) {
		super();
		this.superValue = value;
		evaluationContext = new EvaluationContext(customQueryOptions);
	}

	@Override
	public String toString() {
		if (getValue() != null)
			return getValue().toString();
		else
			return null;
	}

	public EvaluationContext getEvaluationContext() {
		return evaluationContext;
	}

	public PathQLRepository getSource() {
		return source;
	}

	@Deprecated
	public CustomQueryOptions getCustomQueryOptions() {
		if (evaluationContext != null)
			return evaluationContext.getCustomQueryOptions();
		else
			return null;
	}

	@Deprecated
	public Tracer getTracer() {
		if (evaluationContext != null)
			return evaluationContext.getTracer();
		else
			return null;
	}

	public void setTracer(Tracer tracer) {
		if (evaluationContext != null)
			evaluationContext.setTracer(tracer);
	}

	public Value getValue() {
		return getSuperValue();
	}

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

	@Deprecated
	public boolean notTracing() {
		if (evaluationContext != null && evaluationContext.getTracer() != null)
			return !evaluationContext.isTracing();
		else
			return true;
	}

	@Deprecated
	public void decrementTraceLevel() {
		if (evaluationContext != null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().decrementLevel();
	}

	@Deprecated
	public void incrementTraceLevel() {
		if (evaluationContext != null && evaluationContext.getTracer() != null)
			evaluationContext.getTracer().incrementLevel();
	}

	@Deprecated
	protected String indentScriptForTrace(String script) {
		if (evaluationContext != null && evaluationContext.getTracer() != null)
			return evaluationContext.getTracer().indentScriptForTrace(script);
		else
			return null;
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
	 * @param stackKey
	 *            the stack key
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
	 * @param stackKey
	 *            the stack key
	 */
	public void pushStack(String stackKey) {
		evaluationContext.getStack().push(stackKey);
	}

	public void popStack() {
		evaluationContext.getStack().pop();
	}

	public abstract Resource getFact(String predicatePattern) throws PathPatternException;

	public abstract ResourceResults getFacts(String predicatePattern, Value... bindValues) throws PathPatternException;

	public abstract ResourceResults getFacts(PredicateElement path);

	public pathQLModel.Resource getSignal(String signal) {
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

	@Deprecated
	public IRI convertQName(String predicateIRI) {
		predicateIRI = PathQLRepository.trimIRIString(predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":");
		IRI predicate = null;
		if (predicateIRIParts[0].equals("a")) {
			predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
		} else if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		} else {
			IRI namespace = getNamespace(predicateIRIParts[0]);
			if (namespace == null) {
				logger.error(String.format("Error identifying namespace of qName %s", predicateIRI));
				this.getEvaluationContext().getTracer().traceQNameError(predicateIRI);
				//	addTrace(message);
			} else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}

	@Deprecated
	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}

	@Deprecated
	public ConcurrentHashMap<String, IRI> getPrefixes() {
		if (this.getEvaluationContext() != null)
			return this.getEvaluationContext().getPrefixes();
		else
			return null;
	}

	public Value getSuperValue() {
		return superValue;
	}

	public void setSuperValue(Value superValue) {
		this.superValue = superValue;
	}

	public void setSource(PathQLRepository source) {
		this.source = source;
	}

	public abstract Resource getSubject();

	public abstract Resource getPredicate();

	public abstract Object getSnippet();

	public abstract Object getScore();

}
