/*
 * inova8 2020
 */
package pathPatternElement;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.algebra.Var;

/**
 * The Class Variable.
 */
public class Variable extends Var{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * To sparql.
	 *
	 * @return the string
	 */
	String toSparql() {	
		if (hasValue()) {
			return getValue().stringValue();
		}else {
			return getName();
		}
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {	
		if (hasValue()) {
			return getValue().stringValue();
		}else {
			return getName();
		}
	}
	
	/**
	 * Instantiates a new variable.
	 */
	public Variable() {
		super();

	}

	/**
	 * Instantiates a new variable.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public Variable(String name, Value value) {
		super(name, value);
	}

	/**
	 * Instantiates a new variable.
	 *
	 * @param name the name
	 */
	public Variable(String name) {
		super(name);

	}
}
