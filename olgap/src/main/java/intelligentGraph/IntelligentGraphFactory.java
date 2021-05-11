package intelligentGraph;

import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.config.SailConfigException;
import org.eclipse.rdf4j.sail.config.SailFactory;
import org.eclipse.rdf4j.sail.config.SailImplConfig;

public class IntelligentGraphFactory implements SailFactory {
	public static final String SAIL_TYPE = "openrdf:IntelligentGraph";
	@Override
	public String getSailType() {

		return SAIL_TYPE;
	}

	@Override
	public SailImplConfig getConfig() {
		return new IntelligentGraphConfig();
	}

	@Override
	public Sail getSail(SailImplConfig config) throws SailConfigException {
        if (!SAIL_TYPE.equals(config.getType())) {
            throw new SailConfigException("Invalid Sail type: " + config.getType());
        }
        if (config instanceof IntelligentGraphConfig) {
        	//IntelligentGraphConfig intelligentGraphConfig = (IntelligentGraphConfig) config;
            //instantiate the sail
        	IntelligentGraphSail sail = new IntelligentGraphSail();
            return sail;
        } else {
            throw new SailConfigException("Invalid configuration: " + config);
        }
	}

}
