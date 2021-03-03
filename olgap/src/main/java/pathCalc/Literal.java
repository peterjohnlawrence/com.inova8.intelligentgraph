package pathCalc;

import org.eclipse.rdf4j.model.Value;

public class Literal extends Resource {
	//	private final Logger logger = LogManager.getLogger(Literal.class);
	public Literal(Value value) {
		setSuperValue(value);
	}
}
