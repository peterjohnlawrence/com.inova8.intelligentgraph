package intelligentGraph;

import org.eclipse.rdf4j.sail.config.AbstractDelegatingSailImplConfig;
import org.eclipse.rdf4j.sail.config.SailImplConfig;

public class IntelligentGraphConfig  extends AbstractDelegatingSailImplConfig{
    public IntelligentGraphConfig() {
        super(IntelligentGraphFactory.SAIL_TYPE);
    }
	public IntelligentGraphConfig(SailImplConfig delegate) {
		super(IntelligentGraphFactory.SAIL_TYPE, delegate);
	}
}
