package olgap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.message.FormattedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import groovy.lang.GroovyShell;

public class Evaluator {
	private final Logger logger = LogManager.getLogger(Evaluator.class);
	private static HashMap<String, Thing> things = new HashMap<String, Thing>();
	static public MessageDigest digest;
	public RepositoryConnection cacheConn;
	public static TripleSource tripleSource;
	protected static boolean hosted = true;
	protected static boolean trace = true;
	protected static boolean initialized = false;
	static protected ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	static protected HashMap<String, ScriptEngine> scriptEngines = new HashMap<String, ScriptEngine>();
	public static Repository cacheRep;
	private String cacheService;
	public static final String NAMESPACE = "http://inova8.com/olgap/";

	public Evaluator() {
		super();
		if(!initialized) {
			try {
				if(hosted) {
					//Use a host triplestore for the path manipulation
					String rdf4jServer = "http://localhost:8080/rdf4j-server/";
					String repositoryID = "olgap";
					cacheRep = new HTTPRepository(rdf4jServer, repositoryID);	
					cacheConn = cacheRep.getConnection();	
					cacheService ="SERVICE <" + rdf4jServer + "repositories/" + repositoryID + "?distinct=true&infer=false>";
					logger.info("Evaluator hosted at:" + cacheService);

				}else {
					//Use a in-memory triplestore for the path manipulation, however the SPARQ:L cannot use SERVICE call backs to this memory store
					cacheRep = new SailRepository(new MemoryStore());
					logger.info("Evaluator hosted in memory");
				}	
				digest = MessageDigest.getInstance("MD5");
				List<ScriptEngineFactory> engineFactories = scriptEngineManager.getEngineFactories();
				if (engineFactories.isEmpty()) {
					logger.error("No scripting engines were found");
					return;
				}
				String engineNames = "";
				for (ScriptEngineFactory engineFactory : engineFactories) {
					for (String engineName : engineFactory.getNames()) {
						ScriptEngine engine = scriptEngineManager.getEngineByName(engineName);
						scriptEngines.put(engineName, engine);
						engineNames = engineNames + engineName + ";";
					}
				}
				logger.info( engineFactories.size() + " scripting engines were found with the following short names:" + engineNames);
				setTrace(Evaluator.trace);
				
				initialized=true;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}

	public Thing thingFactory(Resource subject) {
		if (things.containsKey(subject.toString())) {
			return things.get(subject.toString());
		} else {
			olgap.Thing newThing = new Thing(this, tripleSource,  subject);
			things.put(subject.toString(), newThing);
			return newThing;
		}
	}

	public olgap.Value valueFactory(Value value) {
		switch (value.getClass().getSimpleName()) {
		case "SimpleLiteral":
		case "BooleanLiteral":
		case "BooleanLiteralImpl":
		case "CalendarLiteral":
		case "DecimalLiteral":
		case "IntegerLiteral":
		case "MemLiteral":
		case "BooleanMemLiteral":
		case "CalendarMemLiteral":
		case "DecimalMemLiteral":
		case "IntegerMemLiteral":
		case "NumericMemLiteral":
		case "NativeLiteral":
		case "NumericLiteral":
			return new olgap.Literal(value);
		default:
			//logger.error(new FormattedMessage("No handler found for objectvalue class {}",value.getClass().getSimpleName()));
			return thingFactory((IRI) value);
		}
	}

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

	public static void clearCache() {
		things.clear();
	}
	public static boolean setTrace(boolean trace) {
		if(trace) {
			Configurator.setRootLevel(Level.TRACE);
			Evaluator.trace=true;
		}else {
			Configurator.setRootLevel(Level.DEBUG);
			Evaluator.trace=false;
		}	
		return Evaluator.trace;
	}
}
