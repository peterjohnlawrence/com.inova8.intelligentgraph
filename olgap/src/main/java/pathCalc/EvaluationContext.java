package pathCalc;

import static org.eclipse.rdf4j.model.util.Values.iri;

import java.util.HashMap;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.query.Dataset;

import pathQLModel.Resource;
import pathQLRepository.PathQLRepository;

public class EvaluationContext {
	protected final Logger logger = LogManager.getLogger(EvaluationContext.class);
	private Tracer tracer = new Tracer();
	private Stack<String> stack = new Stack<String>();
	private HashMap<String, Resource> customQueryOptions = new   HashMap<String, Resource>();	
	private HashMap<String, IRI> prefixes = new HashMap<String, IRI>();
	private Dataset dataset;
	private org.eclipse.rdf4j.model.Resource[] contexts;

	//	private HashMap<String, IRI> defaultPrefixes = new HashMap<String, IRI>();
	public EvaluationContext(Tracer tracer, Stack<String> stack, HashMap<String, Resource> customQueryOptions,
			HashMap<String, IRI> prefixes) {
		super();
		this.tracer = tracer;
		this.stack = stack;
		this.customQueryOptions = customQueryOptions;
		this.prefixes = prefixes;
	}
	public EvaluationContext(HashMap<String, Resource> customQueryOptions) {
		this.customQueryOptions = customQueryOptions;
	}
	public EvaluationContext() {
	}
	public EvaluationContext(HashMap<String, Resource> customQueryOptions, Dataset dataset) {
		this.customQueryOptions = customQueryOptions;
		this.dataset =dataset;
	}
	public EvaluationContext(HashMap<String, Resource> customQueryOptions, Dataset dataset, HashMap<String,IRI> prefixes) {
		this.customQueryOptions = customQueryOptions;
		this.dataset =dataset;
		this.prefixes = prefixes;
	}
	public EvaluationContext(HashMap<String, IRI> prefixes, org.eclipse.rdf4j.model.Resource[] contexts) {
		this.prefixes = prefixes;
		this.contexts = contexts;
	}
	public void setPrefixes(HashMap<String, IRI> prefixes) {
		this.prefixes = prefixes;
	}
	public Tracer getTracer() {
		return tracer;
	}
	public void  setTracer(Tracer tracer) {
		this.tracer = tracer;
	}
	public Stack<String> getStack() {
		if(stack==null) {
			stack = new  Stack<String>();
		}
		return stack;
	}
	public void setStack(Stack<String> stack) {
		this.stack = stack;
	}
	public  HashMap<String, Resource> getCustomQueryOptions() {
		return customQueryOptions;
	}
	public HashMap<String, IRI> getPrefixes() {
		return prefixes;
	}
	public IRI convertQName(String predicateIRI) {
		predicateIRI=PathQLRepository.trimIRIString( predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":");
		IRI predicate = null;
		if(predicateIRIParts[0].equals("a")) {
				predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			}
		else if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		}else {
			IRI namespace = getNamespace(predicateIRIParts[0]);
			if(namespace==null) {
				logger.error(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
				addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", predicateIRI));
			}else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}
	private IRI getNamespace(String namespaceString) {
		IRI namespace = getPrefixes().get(namespaceString);
		return namespace;
	}
	protected boolean notTracing() {
		if (tracer != null)
			return false;
		else
			return true;
	}

	public void addTrace(Message message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	protected void addTrace(String message) {
		if (tracer != null)
			tracer.addTrace(message);
	}

	protected void addScript(String script) {
		if (tracer != null)
			tracer.addScript(script);
	}
	protected void decrementTraceLevel() {
		if (tracer != null)
			tracer.decrementLevel();
	}

	protected void incrementTraceLevel() {
		if (tracer != null)
			tracer.incrementLevel();
	}
	public String getTrace() {
		return tracer.getTrace();

	}
	protected String indentScriptForTrace(String script) {
		if (tracer != null)
			return tracer.indentScriptForTrace(script);
		else
			return null;
	}
	public void setTracing(boolean b) {
		if (tracer != null)
			tracer.setTracing(b);
		
	}
	public boolean isTracing() {
		if (tracer != null)
			return tracer.isTracing();
		else
			return false;
	}
	public Dataset getDataset() {
		return dataset;
	}
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	public org.eclipse.rdf4j.model.Resource[] getContexts() {
		return contexts;
	}

}

