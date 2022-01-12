/*
 * inova8 2020
 */
package com.inova8.intelligentgraph;

import org.eclipse.rdf4j.sail.config.AbstractDelegatingSailImplConfig;
import org.eclipse.rdf4j.sail.config.SailImplConfig;

/**
 * The Class IntelligentGraphConfig.
 */
public class IntelligentGraphConfig  extends AbstractDelegatingSailImplConfig{
    
    /**
     * Instantiates a new intelligent graph config.
     */
    public IntelligentGraphConfig() {
        super(IntelligentGraphFactory.SAIL_TYPE);
    }
	
	/**
	 * Instantiates a new intelligent graph config.
	 *
	 * @param delegate the delegate
	 */
	public IntelligentGraphConfig(SailImplConfig delegate) {
		super(IntelligentGraphFactory.SAIL_TYPE, delegate);
	}
}
