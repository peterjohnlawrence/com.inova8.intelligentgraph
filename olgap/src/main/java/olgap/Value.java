package olgap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public abstract class Value {
	protected org.eclipse.rdf4j.model.Value superValue;
	protected final Logger logger = LogManager.getLogger(Value.class);
	public org.eclipse.rdf4j.model.Value getValue() {
		return superValue;
		
	}
}
