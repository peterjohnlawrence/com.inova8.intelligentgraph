/*
 * inova8 2020
 */
package intelligentGraph;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailWrapper;

import pathCalc.Evaluator;


/**
 * The Class IntelligentGraphSail.
 */
public class IntelligentGraphSail extends NotifyingSailWrapper {
	
	/**
	 * The Enum ResponseType.
	 */
	public enum ResponseType { 
 /** The script. */
 SCRIPT, 
 /** The trace. */
 TRACE, 
 /** The value. */
 VALUE};

	/** The Constant INTELLIGENTGRAPH_DEBUG. */
	public static final String INTELLIGENTGRAPH_DEBUG = "DEBUG";
	
	/** The Constant SCRIPT_POSTFIX. */
	public static final String SCRIPT_POSTFIX = "_SCRIPT";
	
	/** The Constant TRACE_POSTFIX. */
	public static final String TRACE_POSTFIX = "_TRACE";
	
	/** The Constant SCRIPT. */
	public static final String SCRIPT = null;
	
	/** The closed. */
	private final AtomicBoolean closed = new AtomicBoolean(false);
	
	/** The parameters. */
	protected final Properties parameters = new Properties();
	
	/** The evaluator. */
	protected final Evaluator evaluator = new Evaluator();
	
	/**
	 * Instantiates a new intelligent graph sail.
	 */
	public IntelligentGraphSail() {
		super();

	}
	
	/**
	 * Sets the parameter.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setParameter(String key, String value) {
		parameters.setProperty(key, value);
	}
	
	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public Properties getParameters() {
		return parameters;
	}
	
	/**
	 * Initialize.
	 *
	 * @throws SailException the sail exception
	 */
	@Override
	public void initialize() throws SailException {
		super.initialize();
		
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 * @throws SailException the sail exception
	 */
	@Override
	public IntelligentGraphConnection getConnection() throws SailException {
		if (!closed.get()) {
			return new IntelligentGraphConnection(super.getConnection(),this);
		} else {
			throw new SailException("Sail is shut down or not initialized");
		}
	}
	
	/**
	 * Gets the value factory.
	 *
	 * @return the value factory
	 */
	@Override
	public ValueFactory getValueFactory() {
		return getBaseSail().getValueFactory();
	}
//	public PathQLRepository getSource() {
//		return source;
//	}
	
}
