/*
 * inova8 2020
 */
package olgap;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleLiteral;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.QueryEvaluationException;

public class Thing extends olgap.Value {

	Source source;
	private HashMap<String, olgap.Value> values;
	private HashMap<String, olgap.Value> customQueryOptions;
	private HashMap<String,String> prefixes = new HashMap<String,String>();

	public Thing(Source source, org.eclipse.rdf4j.model.Value superValue, HashMap<String, olgap.Value> customQueryOptions ) {
		this.superValue = superValue;
		this.source = source;
		this.customQueryOptions = customQueryOptions;
	}
	public Thing(Source source, org.eclipse.rdf4j.model.Value superValue, HashMap<String, olgap.Value> customQueryOptions, HashMap<String,String> prefixes ) {
		this.superValue = superValue;
		this.source = source;
		this.customQueryOptions = customQueryOptions;
		if(prefixes!=null) {
			prefixes.forEach(
				    (key, value) -> this.prefixes.merge( key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v2)
				);
		}
	}
	private final IRI generateCacheContext(IRI predicate) {
		String key = superValue.toString() + predicate.toString() + StringUtils.join(customQueryOptions);
		String cacheContext = Evaluator.getHexKey(key);
		IRI cacheContextIRI = Source.getTripleSource().getValueFactory().createIRI(Evaluator.SCRIPT_DATA_GRAPH,
				cacheContext);
		return cacheContextIRI;
	}

	/**
	 * @param predicate
	 * @param customQueryOptions
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue }
	 */
	public final olgap.Value getFact(IRI predicate) {
		String key = predicate.toString() + StringUtils.join(this.customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicate),
				addIRI(superValue), customQueryOptions));
		if (notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", addIRI(predicate), addIRI(superValue),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicate, key);
		}
	}

	/**
	 * Gets the fact.
	 *
	 * @param predicate the predicate
	 * @param scriptString the script string
	 * @return the fact
	 */
	public final olgap.Value getFact(IRI predicate, SimpleLiteral scriptString) {
		olgap.Value result = this.handleScript(scriptString, predicate);
		return result;
	}

	public final olgap.Value getFact(String predicateIRI) {
		return this.getFact(convertQName( predicateIRI));
	}


	public final HashSet<olgap.Value> getFacts(IRI predicate) {
		addTrace(new ParameterizedMessage("Seeking values {} of subject {}", addIRI(predicate), addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements((IRI) superValue, predicate, null);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(), objectStatement.getObject(), getStack(),this.customQueryOptions));
		}
		addTrace(new ParameterizedMessage("Retrieved objects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				values));
		return values;
	}

	public final HashSet<olgap.Value> getFacts(String predicateIRI) {
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getFacts(predicate);
	}

	private String getHTMLValue(Value objectValue) {
		switch (objectValue.getClass().getSimpleName()) {
		case "MemIRI":
		case "NativeIRI":
			return "<a href='" + ((IRI) objectValue).getLocalName() + "'>" + ((IRI) objectValue).toString() + "</a>";
		default:
			try {
				String localDatatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().getLocalName();
				String datatype = ((org.eclipse.rdf4j.model.Literal) objectValue).getDatatype().stringValue();
				return ((org.eclipse.rdf4j.model.Literal) objectValue).getLabel() + "^^<a href='" + datatype
						+ "' target='_blank'>" + localDatatype + "</a>";
			} catch (Exception e) {
				return ((org.eclipse.rdf4j.model.Literal) objectValue).toString() + "(unknown value class: "
						+ superValue.getClass().getSimpleName() + ")";
			}
		}
	}

	public final olgap.Value getIsFactOf(IRI predicate) {
		String key = predicate.toString() + StringUtils.join(this.customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicate),
				addIRI(superValue), customQueryOptions));
		if (notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", addIRI(predicate), addIRI(superValue),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicate, key);
		}
	}

	public final olgap.Value getIsFactOf(String predicateIRI) {		
		return this.getIsFactOf(convertQName( predicateIRI));
	}

	private IRI convertQName(String predicateIRI) {
		String[] predicateIRIParts = predicateIRI.split(":");
		IRI predicate = null;
		if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
			predicate = Source.createIRI(predicateIRI);
		}else {
			String namespace = prefixes.get(predicateIRIParts[0]);
			if(namespace==null) {
				addTrace(new ParameterizedMessage("Error indetifying namespace of qName {}", predicateIRI));
			}else {
				predicate = Source.createIRI( namespace, predicateIRIParts[1]);
			}
		}
		return predicate;
	}
	public final HashSet<olgap.Value> getIsFactsOf(IRI predicate) {
		addTrace(new ParameterizedMessage("Seeking subjects {} of {}", addIRI(predicate), addIRI(superValue)));
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source.getTripleSource()
				.getStatements(null, predicate, (IRI) superValue);
		HashSet<olgap.Value> values = new HashSet<olgap.Value>();
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			values.add(source.valueFactory(getTracer(), objectStatement.getSubject(), getStack(),this.customQueryOptions));
		}
		addTrace(new ParameterizedMessage("Retrieved subjects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				values));
		return values;
	}
	public final HashSet<olgap.Value> getIsFactsOf(String predicateIRI) {
		return this.getIsFactsOf(convertQName( predicateIRI));
	}

	public final olgap.Value getIsReifiedFactOf(IRI reificationType,  IRI predicate) {
		String key = predicate.toString() + StringUtils.join(this.customQueryOptions);
		addTrace(new ParameterizedMessage("Seeking value {} of {} using customQueryOptions {}", addIRI(predicate),
				addIRI(superValue), customQueryOptions));
		if (notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved cache {} of {} = {}", addIRI(predicate), addIRI(superValue),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			return retrieveFact(predicate, key);
		}
	}
	public final olgap.Value getIsReifiedFactOf(String reificationTypeIRI, String predicateIRI) {
		IRI reificationType = Source.getTripleSource().getValueFactory().createIRI(reificationTypeIRI);
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getIsReifiedFactOf(reificationType, predicate);
	}

	public final HashSet<olgap.Value> getIsReifiedFactsOf(IRI reificationType, IRI predicate) {
		addTrace(new ParameterizedMessage("Seeking {} values {} of subject {}", addIRI(reificationType), addIRI(predicate), addIRI(superValue)));
		HashSet<olgap.Value> isReifiedFactsOf = getReifiedValuesOf(reificationType.stringValue(),predicate );
		addTrace(new ParameterizedMessage("Retrieved objects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				isReifiedFactsOf));
		return  isReifiedFactsOf;
	}

	public final HashSet<olgap.Value> getIsReifiedFactsOf(String reificationTypeIRI, String predicateIRI) {
		IRI reificationType = Source.getTripleSource().getValueFactory().createIRI(reificationTypeIRI);
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getIsReifiedFactsOf(reificationType,predicate);
	}
	public String getLabel() {
		return superValue.stringValue();
	}
	/**
	 * @param reificationTypeIRI
	 * @param predicateIRI
	 * @param customQueryOptions
	 * @return Finds factValue in nthe equivalent SPARQL:
	    PREFIX plant: <http://inova8.com/plant/def/>
		select  ?thing  ?relationshipType ?property  ?time ?factValue
		{
			VALUES(?thing ?relationshipType ?property ?time ){
			      (UNDEF UNDEF UNDEF  "2015-06-01T00:00:00.000"^^xsd:dateTime )}	
			      
			OPTIONAL{	?subject rdfs:domain ?relationshipType;	
							rdfs:subPropertyOf rdf:subject  . 
						?reifiedRelationship  ?subject ?thing .}
			OPTIONAL{	?isSubjectOf rdfs:range ?relationshipType;
							rdfs:subPropertyOf rdf:isSubjectOf  . 
						?thing  ?isSubjectOf  ?reifiedRelationship .}
			FILTER(!BOUND(?subject) || !BOUND(?isSubjectOf))
			OPTIONAL{ 	?predicate rdfs:domain ?relationshipType  ;
							rdfs:subPropertyOf rdf:predicate  . 
						?reifiedRelationship  ?predicate ?property . }
			OPTIONAL{	?isPredicateOf rdfs:range ?relationshipType  ;
							rdfs:subPropertyOf rdf:isPredicateOf  .
						?property  ?isPredicateOf  ?reifiedRelationship .}
			FILTER(!BOUND(?predicate) || !BOUND(?isPredicateOf))
			OPTIONAL{ 	?object  rdfs:domain ?relationshipType  ;
							rdfs:subPropertyOf rdf:object  . 
						?reifiedRelationship  ?object ?factValue .}
			OPTIONAL{	?isObjectOf  rdfs:range ?relationshipType  ;	
							rdfs:subPropertyOf rdf:isObjectOf  . 
						?factValue  ?isObjectOf  ?reifiedRelationship .}
			FILTER(!BOUND(?object) || !BOUND(?isObjectOf))  
			FILTER(BOUND(?factValue))
			
			OPTIONAL {?reifiedRelationship  :hasOccurrentPeriod ?period .
				OPTIONAL{ ?period :periodStart ?periodStart }
				OPTIONAL{ ?period :periodEnd  ?periodEnd  }
			}
			FILTER ( (!BOUND(?periodStart)|| (?periodStart <  ?time)) 
			&& (!BOUND(?periodEnd ) || (?periodEnd>  ?time)) ) 

		}
	 */
	public final olgap.Value getReifiedFact(String reificationTypeIRI, String predicateIRI) {
		String key = predicateIRI.toString() + StringUtils.join(customQueryOptions);
		IRI predicate = convertQName( predicateIRI);
		addTrace(new ParameterizedMessage("Seeking reified {} factvalue {} of {} using customQueryOptions {}", reificationTypeIRI, addIRI(predicate),
				addIRI(superValue), customQueryOptions));
		if (notTracing() && values.containsKey(key)) {
			olgap.Value result = values.get(key);
			addTrace(new ParameterizedMessage("Retrieved reified {} cache factvalue {} of {} = {}", reificationTypeIRI, addIRI(predicate), addIRI(superValue),
					getHTMLValue(result.getValue())));
			return result;
		} else {
			IRI reificationType = convertQName( reificationTypeIRI);
			Value reifiedObjectValue = getReifiedValue(reificationType, predicate);
			if(reifiedObjectValue!=null ) {
				olgap.Value  returnReifiedResult = processFactObjectValue(predicate,  key, reifiedObjectValue);
				if (returnReifiedResult != null)
					return returnReifiedResult;
				return null;
			}else {
				//Try and see if this is a plain attribute rather than a 3D attribute
				return getFact( predicate);
			}
		}	
	}
	public final HashSet<olgap.Value> getReifiedFacts(IRI reificationType, IRI predicate) {
		addTrace(new ParameterizedMessage("Seeking {} values {} of subject {}", addIRI(reificationType), addIRI(predicate), addIRI(superValue)));
		HashSet<olgap.Value> reifiedFacts = getReifiedValues(reificationType.stringValue(),predicate );
		addTrace(new ParameterizedMessage("Retrieved objects {} of {} = {}", addIRI(predicate), addIRI(superValue),
				reifiedFacts));
		return  reifiedFacts;
	}
	public final HashSet<olgap.Value> getReifiedFacts(String reificationTypeIRI, String predicateIRI) {
		IRI reificationType = Source.getTripleSource().getValueFactory().createIRI(reificationTypeIRI);
		IRI predicate = Source.getTripleSource().getValueFactory().createIRI(predicateIRI);
		return this.getReifiedFacts(reificationType,predicate);
	}
	private Value getReifiedValue(IRI reificationType,IRI predicate) {
		/*{
		 ?reification :reificationSubject  :thing .  :thing   :reificationIsSubjectOf  ?reification .
		 ?reification :reificationPredicate :predicate . :predicate   :reificationIsPredicateOf ?reification .
		 ?reification :reificationObject ?reifiedValue .  ?reifiedValue   :reificationIsObjectOf  ?reification .
		}*/
		//Check if this thing is associated with one or more attributes that might refer to the required predicate
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = null;
		String reificationTypeIRI = reificationType.stringValue();
		if(Source.getReificationSubject(reificationTypeIRI)!=null) reificationSubjectStatements = Source.getTripleSource().getStatements(null, Source.getReificationSubject(reificationTypeIRI), (IRI) superValue);
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsSubjectOfStatements = null ;
		if(Source.getReificationIsSubjectOf(reificationTypeIRI)!=null) reificationIsSubjectOfStatements = Source.getTripleSource().getStatements((IRI) superValue, Source.getReificationIsSubjectOf(reificationTypeIRI), null );
		UnionIterator unionReificationSubjectStatements = new UnionIterator(reificationSubjectStatements, reificationIsSubjectOfStatements);
		while (unionReificationSubjectStatements.hasNext()) {
			Value reification = unionReificationSubjectStatements.getNextSubjectValue();
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = null;
			if(Source.getReificationPredicate(reificationTypeIRI)!=null) reificationPredicateStatements =  Source.getTripleSource().getStatements((IRI) reification, Source.getReificationPredicate(reificationTypeIRI), predicate);
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsPredicateOfStatements = null;
			if(Source.getReificationIsPredicateOf(reificationTypeIRI) !=null) reificationIsPredicateOfStatements =  Source.getTripleSource().getStatements( predicate , Source.getReificationIsPredicateOf(reificationTypeIRI),(IRI) reification);
			UnionIterator unionReificationPredicateStatements = new UnionIterator(reificationPredicateStatements, reificationIsPredicateOfStatements);
			while (unionReificationPredicateStatements.hasNext()) {
				unionReificationPredicateStatements.getNextSubjectValue();
				//Now check if this attribute/predicate is associated with a reifiedValue 
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationObjectStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationObjectStatements = Source.getTripleSource().getStatements((IRI) reification, Source.getReificationObject(reificationTypeIRI), null);
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsObjectOfStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationIsObjectOfStatements= Source.getTripleSource().getStatements(null , Source.getReificationObject(reificationTypeIRI), (IRI) reification);
				UnionIterator unionReificationObjectStatements = new UnionIterator(reificationObjectStatements, reificationIsObjectOfStatements);
				while (unionReificationObjectStatements.hasNext()) {			
					Value reificationObject = unionReificationObjectStatements.getNextObjectValue();
					return reificationObject;
				}
			}
		}
		//No reified values found
		return null;
	}

	private HashSet<olgap.Value> getReifiedValues(String reificationTypeIRI,IRI predicate) {
		/*{
		 ?reification :reificationSubject  :thing .  :thing   :reificationIsSubjectOf  ?reification .
		 ?reification :reificationPredicate :predicate . :predicate   :reificationIsPredicateOf ?reification .
		 ?reification :reificationObject ?reifiedValue .  ?reifiedValue   :reificationIsObjectOf  ?reification .
		}*/
		HashSet<olgap.Value> reifiedValues = new HashSet<olgap.Value>();
		//Check if this thing is associated with one or more attributes that might refer to the required predicate
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = null;
		if(Source.getReificationSubject(reificationTypeIRI)!=null) reificationSubjectStatements = Source.getTripleSource().getStatements(null, Source.getReificationSubject(reificationTypeIRI), (IRI) superValue);
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsSubjectOfStatements = null ;
		if(Source.getReificationIsSubjectOf(reificationTypeIRI)!=null) reificationIsSubjectOfStatements = Source.getTripleSource().getStatements((IRI) superValue, Source.getReificationIsSubjectOf(reificationTypeIRI), null );
		UnionIterator unionReificationSubjectStatements = new UnionIterator(reificationSubjectStatements, reificationIsSubjectOfStatements);
		while (unionReificationSubjectStatements.hasNext()) {
			Value reification = unionReificationSubjectStatements.getNextSubjectValue();
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = null;
			if(Source.getReificationPredicate(reificationTypeIRI)!=null) reificationPredicateStatements =  Source.getTripleSource().getStatements((IRI) reification, Source.getReificationPredicate(reificationTypeIRI), predicate);
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsPredicateOfStatements = null;
			if(Source.getReificationIsPredicateOf(reificationTypeIRI) !=null) reificationIsPredicateOfStatements =  Source.getTripleSource().getStatements( predicate , Source.getReificationIsPredicateOf(reificationTypeIRI),(IRI) reification);
			UnionIterator unionReificationPredicateStatements = new UnionIterator(reificationPredicateStatements, reificationIsPredicateOfStatements);
			while (unionReificationPredicateStatements.hasNext()) {
				unionReificationPredicateStatements.getNextSubjectValue();
				//Now check if this attribute/predicate is associated with a reifiedValue 
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationObjectStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationObjectStatements = Source.getTripleSource().getStatements((IRI) reification, Source.getReificationObject(reificationTypeIRI), null);
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsObjectOfStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationIsObjectOfStatements= Source.getTripleSource().getStatements(null , Source.getReificationObject(reificationTypeIRI), (IRI) reification);
				UnionIterator unionReificationObjectStatements = new UnionIterator(reificationObjectStatements, reificationIsObjectOfStatements);
				while (unionReificationObjectStatements.hasNext()) {			
					Value reificationObject = unionReificationObjectStatements.getNextObjectValue();
					reifiedValues.add(source.valueFactory(getTracer(), reificationObject, getStack(),customQueryOptions));
				}
			}
		}	
		return reifiedValues;
	}
	private HashSet<olgap.Value> getReifiedValuesOf(String reificationTypeIRI,IRI predicate) {
		/*{
		 ?reification :reificationObject :thing  .  :thing    :reificationIsObjectOf  ?reification .
		 ?reification :reificationSubject  ?reifiedValue .  ?reifiedValue   :reificationIsSubjectOf  ?reification .
		 ?reification :reificationPredicate :predicate . :predicate  :reificationIsPredicateOf ?reification .
		}*/
		HashSet<olgap.Value> reifiedValues = new HashSet<olgap.Value>();
		//Check if this thing is associated with one or more attributes that might refer to the required predicate
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationObjectStatements = null;
		if(Source.getReificationSubject(reificationTypeIRI)!=null) reificationObjectStatements = Source.getTripleSource().getStatements(null, Source.getReificationObject(reificationTypeIRI), (IRI) superValue);
		CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsObjectOfStatements = null ;
		if(Source.getReificationIsSubjectOf(reificationTypeIRI)!=null) reificationIsObjectOfStatements = Source.getTripleSource().getStatements((IRI) superValue, Source.getReificationIsSubjectOf(reificationTypeIRI), null );
		UnionIterator unionReificationObjectStatements = new UnionIterator(reificationObjectStatements, reificationIsObjectOfStatements);
		while (unionReificationObjectStatements.hasNext()) {
			Value reification = unionReificationObjectStatements.getNextSubjectValue();
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationPredicateStatements = null;
			if(Source.getReificationPredicate(reificationTypeIRI)!=null) reificationPredicateStatements =  Source.getTripleSource().getStatements((IRI) reification, Source.getReificationPredicate(reificationTypeIRI), predicate);
			CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsPredicateOfStatements = null;
			if(Source.getReificationIsPredicateOf(reificationTypeIRI) !=null) reificationIsPredicateOfStatements =  Source.getTripleSource().getStatements( predicate , Source.getReificationIsPredicateOf(reificationTypeIRI),(IRI) reification);
			UnionIterator unionReificationPredicateStatements = new UnionIterator(reificationPredicateStatements, reificationIsPredicateOfStatements);
			while (unionReificationPredicateStatements.hasNext()) {
				unionReificationPredicateStatements.getNextSubjectValue();
				//Now check if this attribute/predicate is associated with a reifiedValue 
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationSubjectStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationSubjectStatements = Source.getTripleSource().getStatements((IRI) reification, Source.getReificationObject(reificationTypeIRI), null);
				CloseableIteration<? extends Statement, QueryEvaluationException> reificationIsSubjectOfStatements = null;
				if(Source.getReificationObject(reificationTypeIRI)!=null) reificationIsSubjectOfStatements= Source.getTripleSource().getStatements(null , Source.getReificationObject(reificationTypeIRI), (IRI) reification);
				UnionIterator unionReificationSubjectStatements = new UnionIterator(reificationSubjectStatements, reificationIsSubjectOfStatements);
				while (unionReificationSubjectStatements.hasNext()) {			
					Value reificationSubject = unionReificationSubjectStatements.getNextObjectValue();
					reifiedValues.add(source.valueFactory(getTracer(), reificationSubject, getStack(),customQueryOptions));
				}
			}
		}	
		return reifiedValues;
	}

	public olgap.Value getSignal(String signal) {
		incrementTraceLevel();
		String[] elements = signal.split("/");

		Object result;
		switch (elements[0].toUpperCase()) {
		case "SEEQ:":
			SEEQSource seeqSource = null;
			try {
				addTrace(new ParameterizedMessage("Fetching SEEQ signal {} with customQueryOptions {}", elements[5],
						customQueryOptions));
				seeqSource = source.seeqSourceFactory(elements[2]);
				result = seeqSource.getSignal(elements[5], customQueryOptions);
				decrementTraceLevel();
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),customQueryOptions);
			} catch (ScriptException e) {
				return source.valueFactory(getTracer(), "**SEEQ Source Error**", getStack(),customQueryOptions);
			} catch (HandledException e) {
				return source.valueFactory(getTracer(), e.getCode(), getStack(),customQueryOptions);
			}
		case "HTTP:":
			ParameterizedMessage httpMessage = new ParameterizedMessage("HTTP not supported signal source: {}", signal);
			logger.error(httpMessage.getFormattedMessage());
			addTrace(httpMessage);
			return source.valueFactory(getTracer(), "**HTTP Source Error**", getStack(),customQueryOptions);
		default:
			ParameterizedMessage defaultMessage = new ParameterizedMessage("Unsupported signal source: {}", signal);
			logger.error(defaultMessage.getFormattedMessage());
			addTrace(defaultMessage);
			return source.valueFactory(getTracer(), "**Unsupported Source Error**", getStack(),customQueryOptions);

		}
	}
	public final olgap.Thing getThing(String subjectIRI) {
		return source.thingFactory(getTracer(),convertQName( subjectIRI), this.getStack(),this.customQueryOptions,this.prefixes);
	}
	public Value getValue() {
		return superValue;
	}
	public HashMap<String, olgap.Value> getValues() {
		return values;
	}

	protected final olgap.Value handleScript(SimpleLiteral scriptString, IRI predicate) {
		String scriptCode = scriptString.getLabel();
		scriptCode = scriptCode.trim();
		if (scriptCode.startsWith("<")) {
			String scriptIRI = scriptCode.substring(0, scriptCode.length() - 1).substring(1);
			Resource scriptResource =convertQName( scriptIRI);
			IRI scriptPropertyIRI = Source.getTripleSource().getValueFactory().createIRI(Evaluator.NAMESPACE,
					"scriptCode");
			Statement scriptStatement;
			SimpleLiteral scriptCodeliteral = null;
			try {
				CloseableIteration<? extends Statement, QueryEvaluationException> scriptStatements = Source
						.getTripleSource().getStatements(scriptResource, scriptPropertyIRI, null);

				while (scriptStatements.hasNext()) {
					scriptStatement = scriptStatements.next();
					scriptCodeliteral = (SimpleLiteral) scriptStatement.getObject();
				}
			} catch (QueryEvaluationException qe) {
				logger.error("Script query fails");
			}
			if (scriptCodeliteral != null) {
				return handleScript(scriptCodeliteral, predicate);
			} else {
				logger.error(
						new ParameterizedMessage("Reference script <{}> not found for subject {}", scriptIRI, this));
				return source.valueFactory(getTracer(), "**Script Reference Error**", getStack(),customQueryOptions);
			}
		} else {
			incrementTraceLevel();
			IRI cacheContextIRI = generateCacheContext(predicate);
			String scripttype = scriptString.getDatatype().getLocalName();
			addTrace(new ParameterizedMessage("Evaluating predicate {} of {}, by invoking <b>{}</b> script\n",
					addIRI(predicate), addIRI(superValue), scripttype));
			addScript(scriptString.getLabel());
			incrementTraceLevel();
			Object scriptResult = null;
			try {
				//Test to see if the same 'call' is on the stack
				//If so report that circular reference encountered
				//If not push on stack
				String stackKey = "<" + predicate.toString() + "> <" + this.toString() + ">; queryOptions="
						+ (customQueryOptions == null ? "" : customQueryOptions.toString()) + "\r\n";
				if (getStack().search(stackKey) < 1) {
					getStack().push(stackKey);
					CompiledScript compiledScriptCode = source.compiledScriptFactory(scriptString);
					ScriptContext scriptContext = new SimpleScriptContext();
					scriptContext.setAttribute("$tripleSource", Source.getTripleSource(), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$this", this, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$property",
							source.thingFactory(getTracer(), predicate, this.getStack(),customQueryOptions), ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$customQueryOptions", customQueryOptions, ScriptContext.ENGINE_SCOPE);
					scriptContext.setAttribute("$builder", (new ModelBuilder()).namedGraph(cacheContextIRI),
							ScriptContext.ENGINE_SCOPE);
					scriptResult = compiledScriptCode.eval(scriptContext);
					olgap.Value result = returnResult(scriptResult, cacheContextIRI);
					//Since script complete ,pop from stack
					getStack().pop();
					decrementTraceLevel();
					addTrace(new ParameterizedMessage("Evaluated {} of {} =  {}", addIRI(predicate), addIRI(superValue),
							result.getHTMLValue()));
					decrementTraceLevel();
					return result;
				} else {
					ParameterizedMessage circularReferenceMessage = new ParameterizedMessage(
							"Circular reference encountered when evaluating {} of {}:", addIRI(predicate),
							addIRI(superValue));
					addTrace(circularReferenceMessage);
					addScript(getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
							.toString());
					logger.error(new ParameterizedMessage(
							"Circular reference encountered when evaluating <{}> of <{}>.\r\n{}",
							predicate.stringValue(), ((IRI) superValue).stringValue(),
							getStack().subList(getStack().size() - getStack().search(stackKey), getStack().size())
									.toString()));
					return source.valueFactory(getTracer(), "**Circular Reference**", getStack(),customQueryOptions);
				}
			} catch (ScriptException e) {
				decrementTraceLevel();
				ParameterizedMessage scriptFailedMesssage = new ParameterizedMessage(
						"Script failed with <br/><code ><span style=\"white-space: pre-wrap\">{}</span></code >",
						StringEscapeUtils.escapeHtml4(e.getMessage()));
				logger.error(scriptFailedMesssage.getFormattedMessage());
				addTrace(scriptFailedMesssage);
				return source.valueFactory(getTracer(), "**Script Error**", getStack(),customQueryOptions);
			}
		}
	}

	/**
	 * Process fact object value.
	 *
	 * @param predicate the predicate
	 * @param key the key
	 * @param objectValue the object value
	 * @return Processes the objectValue
	 */
	private olgap.Value processFactObjectValue(IRI predicate, String key, Value objectValue) {
		olgap.Value returnResult;
		SimpleLiteral literalValue;
		try {
			literalValue = (SimpleLiteral) objectValue;
			if (Evaluator.scriptEngines.containsKey(literalValue.getDatatype().getLocalName())) {

				olgap.Value result = getFact(predicate, literalValue);
				if (result != null) {

					values.put(key, result);
					addTrace(new ParameterizedMessage("Calculated {} of {} = {}", addIRI(predicate),
							addIRI(superValue), result.getHTMLValue()));
				}
				returnResult = result;
			} else {
				addTrace(new ParameterizedMessage("Retrieved literal {} of {} = {}", addIRI(predicate),
						addIRI(superValue), getHTMLValue(objectValue)));
				returnResult = source.valueFactory(getTracer(), objectValue, getStack(),customQueryOptions);
			}
		} catch (Exception e) {
			addTrace(new ParameterizedMessage("Retrieved resource {} of {} = {}", addIRI(predicate),
					addIRI(superValue), getHTMLValue(objectValue)));
			returnResult = source.valueFactory(getTracer(), objectValue, getStack(),customQueryOptions);
		}
		return returnResult;
	}

	/**
	 * @param predicate
	 * @param key
	 * @return Finds factvalue in the pattern { :thing :predicate ?factValue } unless key can be found in cache
	 * @throws QueryEvaluationException
	 */
	private olgap.Value retrieveFact(IRI predicate,  String key)
			throws QueryEvaluationException {
		CloseableIteration<? extends Statement, QueryEvaluationException> objectStatements = Source
				.getTripleSource().getStatements((IRI) superValue, predicate, null);
		olgap.Value returnResult = null;
		while (objectStatements.hasNext()) {
			Statement objectStatement = objectStatements.next();
			Value objectValue = objectStatement.getObject();		
			returnResult = processFactObjectValue(predicate,  key, objectValue);
		}
		if (returnResult != null)
			return returnResult;
		addTrace(new ParameterizedMessage("Error: No predicate {} found for subject {}", addIRI(predicate),
				addThisIRI()));
		// It could be there are reified attributes associated with scripts or signals
		Value reifiedValue = getReifiedValue(Source.createIRI(Evaluator.RDF_STATEMENT),predicate);
		if (reifiedValue != null) {
			return source.valueFactory(getTracer(), reifiedValue, getStack(),this.customQueryOptions);
		}
		decrementTraceLevel();
		return source.valueFactory(getTracer(),getStack());
	}

	/**
	 * Return result.
	 *
	 * @param result the result
	 * @param cacheContextIRI the cache context IRI
	 * @return the olgap. value
	 */
	public olgap.Value returnResult(Object result, IRI cacheContextIRI) {
		if (result != null) {
			switch (result.getClass().getSimpleName()) {
			case "Integer":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Integer) result), getStack(),null);
			case "Double":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Double) result), getStack(),null);
			case "Float":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((Float) result), getStack(),null);
			case "BigDecimal":
				return source.valueFactory(getTracer(),
						Source.getTripleSource().getValueFactory().createLiteral((BigDecimal) result), getStack(),null);
			case "Thing":
				return (Thing) result;
			case "LinkedHashModel":
				source.writeModelToCache(this, result, cacheContextIRI);
				return source.thingFactory(getTracer(), cacheContextIRI, getStack(),this.customQueryOptions);
			default:
				logger.error("No handler found for result {} of class {}", result.toString(),
						result.getClass().getSimpleName());
				return source.valueFactory(getTracer(), "**Handler Error**", getStack(),null);
			}
		} else {
			return null;
		}
	}

	public void setValues(HashMap<String, olgap.Value> values) {
		this.values = values;
	}

	public String toString() {
		return superValue.toString();
	}
	public Thing prefix(String prefix, String IRI) {
		prefixes.put(prefix,IRI);	
		return this;
	}
	public Thing prefix(String IRI) {
		prefixes.put("",IRI);	
		return this;
	}
}
