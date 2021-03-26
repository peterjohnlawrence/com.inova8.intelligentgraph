package pathPatternElement;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.algebra.Var;

public class Variable extends Var{

	private static final long serialVersionUID = 1L;
	
	String toSparql() {	
		if (hasValue()) {
			return getValue().stringValue();
		}else {
			return getName();
		}
	}

	public Variable() {
		super();

	}

	public Variable(String name, Value value) {
		super(name, value);
	}

	public Variable(String name) {
		super(name);

	}
}
