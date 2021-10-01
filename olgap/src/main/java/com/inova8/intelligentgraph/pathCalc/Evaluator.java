/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.pathCalc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import org.python.core.Options;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.vocabulary.SCRIPT;

import com.inova8.intelligentgraph.pathCalc.Evaluator;
import org.eclipse.rdf4j.model.IRI;
import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class Evaluator.
 */
public class Evaluator {

	private static final Logger logger   = LoggerFactory.getLogger(Evaluator.class);

	static protected final Sources sources = new Sources();

	static protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();


	static protected final Properties scriptEngines=new Properties();

	static private MessageDigest digest;

	static protected boolean trace = false;

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
							Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
							bindings.put("polyglot.js.allowAllAccess", true);
							bindings.put("polyglot.js.allowHostAccess", true);
							bindings.put("polyglot.js.nashorn-compat", true);
							bindings.put("polyglot.js.allowHostClassLookup", true);
							scriptEngines.put(iri(SCRIPT.NAMESPACE + engineName), engine);
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
		}finally {
			scriptEngines.clear();
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
			//Configurator.setRootLevel(Level.TRACE);
			Evaluator.trace = true;
		} else {
			//Configurator.setRootLevel(Level.DEBUG);
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

	/**
	 * Gets the engine names.
	 *
	 * @return the engine names
	 */
	public static Properties getEngineNames() {
		return scriptEngines;
	}
}
