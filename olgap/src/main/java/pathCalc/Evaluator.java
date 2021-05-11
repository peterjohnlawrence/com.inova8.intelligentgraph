/*
 * inova8 2020
 */
package pathCalc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import org.python.core.Options;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.eclipse.rdf4j.model.IRI;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class Evaluator.
 */
public class Evaluator {

	/** The Constant logger. */
	static private final Logger logger = LogManager.getLogger(Evaluator.class);

	/** The Constant sources. */
	static protected final Sources sources = new Sources();

	/** The script engine manager. */
	static protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

	/** The script engines. */
	//static protected HashMap<String, ScriptEngine> scriptEngines = new HashMap<String, ScriptEngine>();

	static protected final Properties scriptEngines = new Properties();

	/** The digest. */
	static private MessageDigest digest;

	/** The trace. */
	static protected boolean trace = false;

	/** The initialized. */
	//	static protected  boolean initialized = false;

	/** The Constant CACHE_HASH. */
	public static final String CACHE_HASH = "cacheHash";

	/** The Constant OLGAPNAMESPACE. */
	public static final String OLGAPNAMESPACE = "http://inova8.com/olgap/";

	/** The Constant SCRIPTNAMESPACE. */
	public static final String SCRIPTNAMESPACE = "http://inova8.com/script/";

	public static final IRI GROOVY = iri(SCRIPTNAMESPACE + "groovy");
	public static final IRI JS = iri(SCRIPTNAMESPACE + "javascript");
	public static final IRI ANONTHING = iri(SCRIPTNAMESPACE + "anonThing");
	public static final IRI ANONPREDICATE = iri(SCRIPTNAMESPACE + "anonPredicate");
	/** The Constant SCRIPTPROPERTY. */
	public static final String SCRIPTPROPERTY = "http://inova8.com/script/scriptCode";

	/** The Constant SCRIPT_DATA_GRAPH. */
	public static final String SCRIPT_DATA_GRAPH = "http://inova8.com/script/data/";

	/** The Constant OWL_INVERSE_OF. */
	public static final String OWL_INVERSE_OF = "http://www.w3.org/2002/07/owl#inverseOf";

	/** The Constant RDFS_DOMAIN. */
	public static final String RDFS_DOMAIN = "http://www.w3.org/2000/01/rdf-schema#domain";

	/** The Constant RDFS_SUB_PROPERTY_OF. */
	public static final String RDFS_SUB_PROPERTY_OF = "http://www.w3.org/2000/01/rdf-schema#subPropertyOf";

	/** The Constant RDF_OBJECT. */
	public static final String RDF_OBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#object";

	/** The Constant RDF_PREDICATE. */
	public static final String RDF_PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate";

	/** The Constant RDF_SUBJECT. */
	public static final String RDF_SUBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject";

	/** The Constant RDF_STATEMENT. */
	public static final String RDF_STATEMENT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement";

	/** The Constant CACHE_DATE_TIME. */
	public static final String CACHE_DATE_TIME = "cacheDateTime";
	public static final String ISPRIVATE = "isPrivate";
	static {
		try {
			digest = MessageDigest.getInstance("MD5");
			List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();
			if (engineFactories.isEmpty()) {
				logger.error("No scripting engines were found");
			}
			String engineNames = "";
			String unknownEngineNames = "";
			Integer engineFactoryCount = 0;
			Options.importSite = false;
			for (ScriptEngineFactory engineFactory : engineFactories) {
				if (!engineFactory.getEngineName().toLowerCase().contains("nashorn")) {
					engineFactoryCount++;
					for (String engineName : engineFactory.getNames()) {
						ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
						if (engine != null) {
							//getScriptEngines().put(engineName, engine);
							scriptEngines.put(iri(SCRIPTNAMESPACE + engineName), engine);
							engineNames = engineNames + engineName + ";";
						} else {
							unknownEngineNames = unknownEngineNames + engineName + ";";
						}
					}
				}
			}
			logger.info(
					engineFactoryCount + " scripting engines were found with the following short names:" + engineNames);
			if(unknownEngineNames.length()>0) logger.info(
					"These scripting engines were *NOT* found with the following short names:" + unknownEngineNames);
			setTrace(Evaluator.trace);
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * Bytes to hex.
	 *
	 * @param bytes
	 *            the bytes
	 * @return the string
	 */
	public static String bytesToHex(byte[] bytes) {
		final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] hexChars = new char[bytes.length * 2];
		int v;
		for (int j = 0; j < bytes.length; j++) {
			v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Sets the trace.
	 *
	 * @param trace
	 *            the trace
	 * @return true, if successful
	 */
	public static boolean setTrace(boolean trace) {
		if (trace) {
			Configurator.setRootLevel(Level.TRACE);
			Evaluator.trace = true;
		} else {
			Configurator.setRootLevel(Level.DEBUG);
			Evaluator.trace = false;
		}
		return Evaluator.trace;
	}

	/**
	 * Gets the digest.
	 *
	 * @param key
	 *            the key
	 * @return the digest
	 */
	protected static byte[] getDigest(String key) {
		return digest.digest(key.getBytes());
	}

	/**
	 * Gets the hex key.
	 *
	 * @param key
	 *            the key
	 * @return the hex key
	 */
	public static String getHexKey(String key) {
		return bytesToHex(getDigest(key));
	}

	/**
	 * Gets the script engines.
	 *
	 * @return the script engines
	 */
	protected static Properties getScriptEngines() {
		return scriptEngines;
	}

	/**
	 * Gets the script engine.
	 *
	 * @param engineIRI
	 *            the engine IRI
	 * @return the script engine
	 */

	public static ScriptEngine getScriptEngine(IRI engineIRI) {
		return (ScriptEngine) scriptEngines.get(engineIRI);
	}

	/**
	 * Checks if is script engine.
	 *
	 * @param engineIRI
	 *            the engine IRI
	 * @return the boolean
	 */

	public static Boolean isScriptEngine(IRI engineIRI) {
		return scriptEngines.containsKey(engineIRI);
	}

	/**
	 * Clear cache.
	 */
	public static void clearCache() {
		sources.clear();
	}

	public static Properties getEngineNames() {
		return scriptEngines;
	}
}
