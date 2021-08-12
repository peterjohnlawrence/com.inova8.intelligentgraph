/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import org.eclipse.rdf4j.sail.Sail;
import org.eclipse.rdf4j.sail.config.SailConfigException;
import org.eclipse.rdf4j.sail.config.SailFactory;
import org.eclipse.rdf4j.sail.config.SailImplConfig;

/**
 * A factory for creating IntelligentGraph objects.
 */
public class IntelligentGraphFactory implements SailFactory {
	
	/** The Constant SAIL_TYPE. */
	public static final String SAIL_TYPE = "openrdf:IntelligentGraph";
	
	/**
	 * Gets the sail type.
	 *
	 * @return the sail type
	 */
	@Override
	public String getSailType() {

		return SAIL_TYPE;
	}

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	@Override
	public SailImplConfig getConfig() {
		return new IntelligentGraphConfig();
	}

	/**
	 * Gets the sail.
	 *
	 * @param config the config
	 * @return the sail
	 * @throws SailConfigException the sail config exception
	 */
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
