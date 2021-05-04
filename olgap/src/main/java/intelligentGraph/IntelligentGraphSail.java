package intelligentGraph;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailWrapper;

import pathCalc.Evaluator;
import pathQLRepository.PathQLRepository;


public class IntelligentGraphSail extends NotifyingSailWrapper {
	public enum ResponseType { SCRIPT, TRACE, VALUE};

	public static final String INTELLIGENTGRAPH_DEBUG = "DEBUG";
	public static final String SCRIPT_POSTFIX = "_SCRIPT";
	public static final String TRACE_POSTFIX = "_TRACE";
	public static final String SCRIPT = null;
	private final AtomicBoolean closed = new AtomicBoolean(false);
	protected final Properties parameters = new Properties();
	protected final Evaluator evaluator = new Evaluator();
	private PathQLRepository source;
	public void setParameter(String key, String value) {
		parameters.setProperty(key, value);
	}
	public Properties getParameters() {
		return parameters;
	}
	@Override
	public void initialize() throws SailException {
		super.initialize();
		
	}
	@Override
	public IntelligentGraphConnection getConnection() throws SailException {
		if (!closed.get()) {
			return new IntelligentGraphConnection(super.getConnection(),this);
		} else {
			throw new SailException("Sail is shut down or not initialized");
		}
	}
	@Override
	public ValueFactory getValueFactory() {
		return getBaseSail().getValueFactory();
	}
	public PathQLRepository getSource() {
		return source;
	}
	
}
