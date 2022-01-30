/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

/**
 * The Class CallingClass.
 */
public class CallingClass extends SecurityManager {
    
    /** The Constant INSTANCE. */
    public static final CallingClass INSTANCE = new CallingClass();

    /**
     * Gets the calling classes.
     *
     * @return the calling classes
     */
    @SuppressWarnings("rawtypes")
	public Class[] getCallingClasses() {
        return getClassContext();
    }
}
