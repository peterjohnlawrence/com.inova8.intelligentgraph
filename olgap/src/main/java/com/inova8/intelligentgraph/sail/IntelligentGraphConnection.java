/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.Dataset;
import org.eclipse.rdf4j.query.Query.QueryType;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.ProjectionElem;
import org.eclipse.rdf4j.query.algebra.ProjectionElemList;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.EvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryBindingSet;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.evaluation.impl.StrictEvaluationStrategy;
import org.eclipse.rdf4j.query.algebra.helpers.VarNameCollector;
import org.eclipse.rdf4j.query.impl.SimpleDataset;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.UpdateContext;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailConnectionWrapper;
import static org.eclipse.rdf4j.model.util.Values.literal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;
import com.inova8.intelligentgraph.vocabulary.OWL;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.intelligentgraph.vocabulary.RDF;
import com.inova8.intelligentgraph.vocabulary.RDFS;
import com.inova8.pathql.context.ReificationType;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.parser.PathParser;
import com.inova8.pathql.processor.PathPatternException;

//import pathCalc.Prefixes;

import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class IntelligentGraphConnection.
 */
public class IntelligentGraphConnection extends NotifyingSailConnectionWrapper {

	private static final String PATH_QL_REGEX = "\\?pathQL=";

	/** The custom query options. */
	public static CustomQueryOptions customQueryOptions;
	
	/** The logger. */
	protected final Logger logger = LoggerFactory.getLogger(IntelligentGraphConnection.class);
	
	/** The intelligent graph sail. */
	private IntelligentGraphSail intelligentGraphSail;
	

	/**
	 * Instantiates a new intelligent graph connection.
	 *
	 * @param wrappedCon the wrapped con
	 * @param intelligentGraphSail the intelligent graph sail
	 */
	public IntelligentGraphConnection(NotifyingSailConnection wrappedCon, IntelligentGraphSail intelligentGraphSail) {
		super(wrappedCon);
		this.intelligentGraphSail= intelligentGraphSail;
	}
	

	/**
	 * Gets the intelligent graph sail.
	 *
	 * @return the intelligent graph sail
	 */
	public IntelligentGraphSail getIntelligentGraphSail() {
		return intelligentGraphSail;
	}
	
	/**
	 * Clear.
	 *
	 * @param contexts the contexts
	 * @throws SailException the sail exception
	 */
	@Override
	public synchronized void clear(Resource... contexts) throws SailException {
		boolean local =  startLocalTransaction();
		//TODO clear(contexts) fails to commit the chnages to the named graph, so removeStatements used instead
		//getWrappedConnection(). clear(contexts);
		removeStatements(null,null,null,contexts);
		conditionalCommit(local);
		IntelligentGraphRepository.create(this).clearRepositoryNamespaceContext();
		IntelligentGraphRepository.create(this).clearRepositoryReificationContext();
		//this.intelligentGraphSail.setContextsAreLazyLoaded(false);
	}

	/**
	 * Start local transaction.
	 *
	 * @return true, if successful
	 * @throws RepositoryException the repository exception
	 */
	protected final boolean startLocalTransaction() throws RepositoryException {
		if (!isActive()) {
			begin();
			return true;
		}
		return false;
	}

	/**
	 * Conditional commit.
	 *
	 * @param condition the condition
	 * @throws RepositoryException the repository exception
	 */
	protected final void conditionalCommit(boolean condition) throws RepositoryException {
		if (condition) {
			commit();
		}
	}
	
	/**
	 * Sets the namespace.
	 *
	 * @param prefix the prefix
	 * @param name the name
	 * @throws SailException the sail exception
	 */
	@Override
	public void setNamespace(String prefix, String name) throws SailException {
		IntelligentGraphRepository.create(this).clearRepositoryNamespaceContext();
	//	this.intelligentGraphSail.setPrefixesAreLazyLoaded(false);
		super.setNamespace(prefix, name);
	}

	/**
	 * Clear namespaces.
	 *
	 * @throws SailException the sail exception
	 */
	@Override
	public void clearNamespaces() throws SailException {
		IntelligentGraphRepository.create(this).clearRepositoryNamespaceContext();
		//this.intelligentGraphSail.setPrefixesAreLazyLoaded(false);
		super.clearNamespaces();
	}
	
	/**
	 * Adds the statement.
	 *
	 * @param modify the modify
	 * @param subj the subj
	 * @param pred the pred
	 * @param obj the obj
	 * @param contexts the contexts
	 * @throws SailException the sail exception
	 */
	@Override
	public void addStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
		try {
			String[] predicateParts;
			if (pred != null) {
				predicateParts= pred.stringValue().split(PATH_QL_REGEX);
				switch (predicateParts[0]) {
				case PATHQL.addFact:
					addFact(modify,subj, URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString()) , obj, contexts);
					break;

				default:
					super.addStatement(modify, subj, pred, obj, contexts);
					checkReificationsChanged(pred);
				}
			}
			else
				super.addStatement(modify, subj, pred, obj, contexts);
			this.intelligentGraphSail.clearCache();
		} catch (Exception e) {
			throw new SailException(e);
		}	
	}
	
	/**
	 * Adds the statement.
	 *
	 * @param subject the subject
	 * @param predicate the predicate
	 * @param object the object
	 * @param contexts the contexts
	 * @throws SailException the sail exception
	 */
	@Override
	public void addStatement(Resource subject, IRI predicate, Value object, Resource... contexts)
			throws SailException {
		try {
			String[] predicateParts;
			if (predicate != null) {
				predicateParts= predicate.stringValue().split(PATH_QL_REGEX);
				switch (predicateParts[0]) {
				case PATHQL.addFact:
					addFact(subject, URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString()) , object, contexts);
					break;

				default:
					super.addStatement(subject, predicate, object, contexts);
					checkReificationsChanged(predicate);
				}
			}
			else
				super.addStatement(subject, predicate, object, contexts);
			this.intelligentGraphSail.clearCache();
		} catch (Exception e) {
			throw new SailException(e);
		}		
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param modify the modify
	 * @param thingresource the thingresource
	 * @param pathQL the path QL
	 * @param value the value
	 * @param contexts the contexts
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private void addFact(UpdateContext modify, Resource thingresource, String pathQL, Value value , Resource... contexts) throws RecognitionException, PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		PredicateElement predicateElement = (PredicateElement) PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		predicateElement.getSourceVariable().setValue( thing.getValue());
		
		if (predicateElement.getIsReified()) {
			ReificationType reificationType = source.getRepositoryContext().getReificationTypes().get(predicateElement.getReification().stringValue());
			if (reificationType != null) {
				IRI reification = iri(
						reificationType.getReificationType().stringValue() + "/" + thing.getIRI().hashCode() + "."
								+ predicateElement.getPredicate().hashCode() + "." + value.hashCode());
				super.addStatement(modify,reification, RDF.TYPE, predicateElement.getReification(),
						contexts);
				super.addStatement(modify,reification, reificationType.getReificationSubject(), thing.getIRI(),
						contexts);
				super.addStatement(modify,reification, reificationType.getReificationPredicate(), predicateElement.getPredicate(),
						contexts);
				super.addStatement(modify,reification, reificationType.getReificationObject(), value,
						contexts);
			} else {
				logger.error("Reified type not supported:" + predicateElement.toString());
			}

		} else {
			IRI propertyIri = predicateElement.getPredicate();
			
			super.addStatement(modify,thing.getIRI(), propertyIri, value, contexts);
			checkReificationsChanged(propertyIri);
		}
		
	}
	
	/**
	 * Adds the fact.
	 *
	 * @param thingresource the thingresource
	 * @param pathQL the path QL
	 * @param value the value
	 * @param contexts the contexts
	 * @throws RecognitionException the recognition exception
	 * @throws PathPatternException the path pattern exception
	 */
	private void addFact(Resource thingresource, String pathQL, Value value , Resource... contexts) throws RecognitionException, PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		PredicateElement predicateElement = (PredicateElement) PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		predicateElement.getSourceVariable().setValue( thing.getValue());
		
		if (predicateElement.getIsReified()) {
			ReificationType reificationType = source.getRepositoryContext().getReificationTypes().get(predicateElement.getReification().stringValue());
			if (reificationType != null) {
				IRI reification = iri(
						reificationType.getReificationType().stringValue() + "/" + thing.getIRI().hashCode() + "."
								+ predicateElement.getPredicate().hashCode() + "." + value.hashCode());
				super.addStatement(reification, RDF.TYPE, predicateElement.getReification(),
						contexts);
				super.addStatement(reification, reificationType.getReificationSubject(), thing.getIRI(),
						contexts);
				super.addStatement(reification, reificationType.getReificationPredicate(), predicateElement.getPredicate(),
						contexts);
				super.addStatement(reification, reificationType.getReificationObject(), value,
						contexts);
			} else {
				logger.error("Reified type not supported:" + predicateElement.toString());
			}

		} else {
			IRI propertyIri = predicateElement.getPredicate();
			
			super.addStatement(thing.getIRI(), propertyIri, value, contexts);
			checkReificationsChanged(propertyIri);
		}
		
	}

	/**
	 * Check reifications changed.
	 *
	 * @param propertyIri the property iri
	 */
	private void checkReificationsChanged(IRI propertyIri) {
		switch (propertyIri.stringValue()) {
			case RDFS.subClassOf:
			case RDFS.subPropertyOf:
			case RDFS.domain:
			case RDFS.range: 
			case OWL.inverse_of:
				IntelligentGraphRepository.create(this).clearRepositoryReificationContext();
			default:	
				
		}
	}

	/**
	 * Removes the statements.
	 *
	 * @param subj the subj
	 * @param pred the pred
	 * @param obj the obj
	 * @param contexts the contexts
	 * @throws SailException the sail exception
	 */
	@Override
	public void removeStatements(Resource subj, IRI pred, Value obj, Resource... contexts) throws SailException {
		try {
			String[] predicateParts;
			if (pred != null) {
				predicateParts= pred.stringValue().split(PATH_QL_REGEX);
			//if (pred != null)
				switch (predicateParts[0]) {
				case PATHQL.removeFact:
				case PATHQL.removeFacts:
					deleteFacts(null, subj, URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString()),obj, contexts);
					break;
				default:
					super.removeStatements(subj, pred, obj, contexts);
					checkReificationsChanged(pred);
				}
			}
			else {
				super.removeStatements(subj, pred, obj, contexts);
			}
		} catch (Exception e) {
			throw new SailException(e);
		}
	}
	
	/**
	 * Removes the statement.
	 *
	 * @param modify the modify
	 * @param subj the subj
	 * @param pred the pred
	 * @param obj the obj
	 * @param contexts the contexts
	 * @throws SailException the sail exception
	 */
	@Override
	public void removeStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
		try {
			String[] predicateParts;
			if (pred != null) {
				predicateParts= pred.stringValue().split(PATH_QL_REGEX);
				switch (predicateParts[0]){
					case PATHQL.removeFact: 					
					case PATHQL.removeFacts: 
						deleteFacts(modify, subj,  URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString()),obj,  contexts);
						break;
					default:
						super.removeStatement(modify, subj, pred, obj, contexts);
						checkReificationsChanged(pred);
				}
		}
		else {
			super.removeStatement(modify, subj, pred, obj, contexts);
			
		}
		}catch(Exception e) {
			throw new SailException(e);
		}
	}	

	/**
	 * Removes the namespace.
	 *
	 * @param prefix the prefix
	 * @throws SailException the sail exception
	 */
	@Override
	public void removeNamespace(String prefix) throws SailException {
		super.removeNamespace(prefix);
	}

	/**
	 * Gets the public contexts.
	 *
	 * @return the public contexts
	 */
	public HashSet<IRI> getPublicContexts() {
		return intelligentGraphSail.getPublicContexts();
	}
	
	/**
	 * Gets the contexts.
	 *
	 * @param contexts the contexts
	 * @return the contexts
	 */
	public  Resource[] getContexts(Resource... contexts) {
		HashSet<IRI> extendedContexts = new HashSet<IRI>();// intelligentGraphSail.getPublicContexts();
		for(IRI context: this.intelligentGraphSail.getPublicContexts()) {
			extendedContexts.add( context);
		}
		for(Resource context:contexts) {
			extendedContexts.add((IRI) context);
		}
	return 	 extendedContexts.toArray(new org.eclipse.rdf4j.model.Resource[0] );
	}
	
	/**
	 * Gets the private contexts.
	 *
	 * @return the private contexts
	 */
	public HashSet<IRI> getPrivateContexts() {
		return intelligentGraphSail.getPrivateContexts();
	}
	
	/**
	 * Gets the statements.
	 *
	 * @param subj the subj
	 * @param pred the pred
	 * @param obj the obj
	 * @param includeInferred the include inferred
	 * @param contexts the contexts
	 * @return the statements
	 * @throws SailException the sail exception
	 */
	@Override
	public CloseableIteration<? extends IntelligentStatement, SailException> getStatements(Resource subj, IRI pred, Value obj,
			boolean includeInferred, Resource... contexts) throws SailException {
		try {
			Resource[] extendedContexts=contexts;
			if(pred!=null && !pred.stringValue().equals("http://inova8.com/script/isPrivate")) {
					extendedContexts = getContexts(contexts);
			}
			String[] predicateParts;
			if (pred != null) {
				predicateParts= pred.stringValue().split(PATH_QL_REGEX);
				switch (predicateParts[0]){
					case PATHQL.getFact: 						
					case PATHQL.getFacts: 
						return  getFacts( subj, decodePathQL(predicateParts,obj), obj,  extendedContexts);
					case PATHQL.getPath: 						
					case PATHQL.getPaths: 
						return  getPaths( subj, decodePathQL(predicateParts,obj), obj,  extendedContexts);
					case PATHQL.traceFact: 						
					case PATHQL.traceFacts: 
						return  traceFacts( subj, decodePathQL(predicateParts,obj), obj,  extendedContexts); 						
					case PATHQL.clearCache: 
						return  clearCache( subj,  pred, obj,  extendedContexts);
					case PATHQL.getScript: 						
						return  getScript( subj,  decodePathQL(predicateParts,obj), obj,  extendedContexts);
					default:
						return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, obj, includeInferred, extendedContexts),intelligentGraphSail,this,extendedContexts); 
				}
			}else {
				return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, obj, includeInferred, extendedContexts),intelligentGraphSail,this,extendedContexts); 
			}
		}catch(Exception e) {
			throw new SailException(e.getMessage(),e);
		}
	}
	
	private String decodePathQL(String[] predicateParts, Value obj ) throws UnsupportedEncodingException {
		if(predicateParts.length>1) {
			return URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString());
		}else {
			return toPathQLString(obj);
		}
	}

	private String toPathQLString(Value pathQLValue) {
		String pathQL;
		if( pathQLValue.isIRI()) {
			 pathQL = "<"+ pathQLValue.stringValue()+">";
		}else {
			 pathQL = pathQLValue.stringValue();
		}
		return pathQL;
	}
	/**
	 * Clear cache.
	 *
	 * @param subj the subj
	 * @param pred the pred
	 * @param obj the obj
	 * @param extendedContexts the extended contexts
	 * @return the closeable iteration<? extends intelligent statement, sail exception>
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> clearCache(Resource subj, IRI pred,Value obj,
			Resource[] extendedContexts) {
		intelligentGraphSail.clearCache(obj); 
		return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, literal(true), false, extendedContexts),intelligentGraphSail,this,extendedContexts); 
	}


	/**
	 * Gets the script.
	 *
	 * @param subj the subj
	 * @param pathQLValue the path QL value
	 * @param contexts the contexts
	 * @return the script
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> getScript(Resource subj, String pathQLValue, Value obj,  Resource... contexts) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Trace facts.
	 *
	 * @param thingresource the thingresource
	 * @param pathQLValue the path QL value
	 * @param contexts the contexts
	 * @return the closeable iteration<? extends intelligent statement, sail exception>
	 * @throws PathPatternException the path pattern exception
	 */
	@SuppressWarnings("deprecation")
	private CloseableIteration<? extends IntelligentStatement, SailException> traceFacts(Resource thingresource, String pathQLValue, Value obj,  Resource... contexts) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		thing.getEvaluationContext().setTracing(true);
		thing.getEvaluationContext().getTracer().clear();
		
		thing.getEvaluationContext().getTracer().traceFacts(thing, pathQLValue,source.getRepositoryContext().getPrefixes(), contexts);
		String pathQL = pathQLValue;
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return traceThingFacts(source, thing,pathElement, contexts );
	}

	/**
	 * Gets the facts.
	 *
	 * @param thingresource the thingresource
	 * @param pathQLValue the path QL value
	 * @param contexts the contexts
	 * @return the facts
	 * @throws PathPatternException the path pattern exception
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> getFacts(Resource thingresource, String pathQLValue, Value obj, Resource... contexts ) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL =pathQLValue;
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return getThingFacts(source, thing,pathElement, contexts );
	}
	
	/**
	 * Gets the paths.
	 *
	 * @param thingresource the thingresource
	 * @param pathQLValue the path QL value
	 * @param contexts the contexts
	 * @return the paths
	 * @throws PathPatternException the path pattern exception
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> getPaths(Resource thingresource,   String pathQLValue, Value obj, Resource... contexts ) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = pathQLValue;
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return getThingPaths(source, thing,pathElement, contexts );
	}
	
	/**
	 * Gets the thing facts.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param contexts the contexts
	 * @return the thing facts
	 * @throws PathPatternException the path pattern exception
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> getThingFacts(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) 
				new IntelligentStatementResults(source,thing, pathElement,this,customQueryOptions,contexts);
	}
	
	/**
	 * Trace thing facts.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param contexts the contexts
	 * @return the closeable iteration<? extends intelligent statement, sail exception>
	 * @throws PathPatternException the path pattern exception
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> traceThingFacts(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) 
				new IntelligentStatementResults(source,thing, pathElement,this,customQueryOptions,true,contexts);
	}	
	
	/**
	 * Gets the thing paths.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param contexts the contexts
	 * @return the thing paths
	 * @throws PathPatternException the path pattern exception
	 */
	private CloseableIteration<? extends IntelligentStatement, SailException> getThingPaths(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) new IntelligentStatementPaths( source,thing, pathElement,  this,customQueryOptions,contexts);	
	}


	
	/**
	 * Delete facts.
	 *
	 * @param modify the modify
	 * @param thingresource the thingresource
	 * @param pathQLValue the path QL value
	 * @param contexts the contexts
	 * @throws PathPatternException the path pattern exception
	 */
	private void deleteFacts(UpdateContext modify, Resource thingresource, String pathQLValue, Value obj, Resource... contexts) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = pathQLValue;//TODO toPathQLString(pathQLValue);
		PathElement pathElement = PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		deleteThingFacts( modify, source, thing,  pathElement, contexts) ;

	}
	
	/**
	 * Delete thing facts.
	 *
	 * @param modify the modify
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param contexts the contexts
	 * @throws PathPatternException the path pattern exception
	 */
	private void deleteThingFacts(UpdateContext modify,IntelligentGraphRepository source,Thing thing, PathElement pathElement, Resource... contexts) throws PathPatternException {
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = getResultsIterator(source, thing,pathElement, 0,contexts);
		if(((PredicateElement)pathElement).getIsReified()) {
			String reified = ((PredicateElement)pathElement).getReifiedVariable().getName();
			while(resultsIterator.hasNext()){
				BindingSet bindingSet = resultsIterator.next();
				super.removeStatements((Resource)bindingSet.getBinding(reified).getValue(), null, null, contexts);
			}			
		}else {
			String subj = pathElement.getTargetSubject().getName();
			String pred = pathElement.getTargetPredicate().getName();
			String obj = pathElement.getTargetVariable().getName();	
			while(resultsIterator.hasNext()){
				BindingSet bindingSet = resultsIterator.next();
				IRI predicate = (IRI)bindingSet.getBinding(pred).getValue();
				super.removeStatements((Resource)bindingSet.getBinding(subj).getValue(), (IRI)bindingSet.getBinding(pred).getValue(), bindingSet.getBinding(obj).getValue(), contexts);
				checkReificationsChanged(predicate);
			}
		}
	}

	/**
	 * Gets the results iterator.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param pathTupleExpr the path tuple expr
	 * @param contexts the contexts
	 * @return the results iterator
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	CloseableIteration<BindingSet, QueryEvaluationException> getResultsIterator(IntelligentGraphRepository source,Thing thing, PathElement pathElement, PathTupleExpr pathTupleExpr, Resource... contexts)
			throws IllegalArgumentException, QueryEvaluationException {
		TupleExpr tupleExpr = pathTupleExpr.getTupleExpr();
		SimpleDataset dataset = prepareDataset(pathElement, source,contexts);
		BindingSet bindings = new QueryBindingSet();
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(),dataset, null);
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(tupleExpr,bindings);
		return resultsIterator;
	}

	/**
	 * Gets the results iterator.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param pathIteration the path iteration
	 * @param contexts the contexts
	 * @return the results iterator
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	CloseableIteration<BindingSet, QueryEvaluationException> getResultsIterator(IntelligentGraphRepository source,Thing thing, PathElement pathElement, Integer pathIteration, Resource... contexts)
			throws IllegalArgumentException, QueryEvaluationException {
		CustomQueryOptions customQueryOptions= CustomQueryOption.getCustomQueryOptions(contexts,source.getRepositoryContext().getPrefixes());
		TupleExpr tupleExpr = pathElement.pathPatternQuery(pathIteration,customQueryOptions).getTupleExpr();
		SimpleDataset dataset = prepareDataset(pathElement, source, contexts);
		BindingSet bindings = new QueryBindingSet();
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(),dataset, null);
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(tupleExpr,bindings);
		return resultsIterator;
	}

	/**
	 * Prepare dataset.
	 *
	 * @param pathElement the path element
	 * @param source the source
	 * @param contexts the contexts
	 * @return the simple dataset
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	private SimpleDataset prepareDataset(PathElement pathElement,IntelligentGraphRepository source, Resource... contexts)
			throws IllegalArgumentException {
		CustomQueryOptions customQueryOptions = prepareCustomQueryOptions(pathElement, source, contexts);
		SimpleDataset dataset = getDataset(contexts);
		if(customQueryOptions!=null && !customQueryOptions.isEmpty()) {
			if(dataset==null)
				dataset = new SimpleDataset();
			dataset.addDefaultGraph(iri(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS+"?"+customQueryOptions.toURIEncodedString()));
		}
		return dataset;
	}


	/**
	 * Prepare custom query options.
	 *
	 * @param pathElement the path element
	 * @param source the source
	 * @param contexts the contexts
	 * @return the custom query options
	 */
	private CustomQueryOptions prepareCustomQueryOptions(PathElement pathElement, IntelligentGraphRepository source, Resource... contexts) {
		CustomQueryOptions customQueryOptions = CustomQueryOption.getCustomQueryOptions(contexts,source.getRepositoryContext().getPrefixes());//TODOgetPrefixes());
		CustomQueryOptions pathQLCustomQueryOptions = pathElement.getCustomQueryOptions();
		if( pathQLCustomQueryOptions !=null) {
			pathQLCustomQueryOptions.addInherited(customQueryOptions);
			customQueryOptions= pathQLCustomQueryOptions;
		}
		return customQueryOptions;
	}

	/**
	 * Gets the dataset.
	 *
	 * @param contexts the contexts
	 * @return the dataset
	 */
	private SimpleDataset getDataset(Resource[] contexts) {
		if(contexts==null) {
			//If contexts==null then return null. This is the default 'get everything' that is assumed
			return null;	
		}else if(containsCustomQueryOptions(contexts)) {
			//If contexts==urn:customQueryOptions *ONLY* then parse the query options, and add *ALL* public contexts to the dataset		
			SimpleDataset dataset = new SimpleDataset();
			for (Resource graph : contexts) {
				dataset.addDefaultGraph((IRI)graph);
			}
			if(containsOnlyPrivateContexts(contexts) ) {
				//If contexts==urn:customQueryOptions and private contexts then add all public contexts as well.
				for (IRI graph : getPublicContexts()) {
					dataset.addDefaultGraph(graph);
				}
			}
			return dataset;
		}else {
			SimpleDataset dataset = new SimpleDataset();
			for (Resource graph : contexts) {
				dataset.addDefaultGraph((IRI) graph);
			}
			return dataset;
		}
	}
	
	/**
	 * Contains custom query options.
	 *
	 * @param contexts the contexts
	 * @return the boolean
	 */
	private Boolean containsCustomQueryOptions(Resource[] contexts) {
		for(Resource context:contexts) {
			if(context.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Contains only private contexts.
	 *
	 * @param contexts the contexts
	 * @return the boolean
	 */
	@SuppressWarnings("unlikely-arg-type")
	private Boolean containsOnlyPrivateContexts(Resource[] contexts) {
		for(Resource context:contexts) {
			if(context.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS) ) {
			}else if( getPublicContexts().contains(context.stringValue())){
				return false;
			}
		}
		return true;
	}

	/**
	 * Evaluate.
	 *
	 * @param tupleExpr the tuple expr
	 * @param dataset the dataset
	 * @param bindings the bindings
	 * @param includeInferred the include inferred
	 * @return the closeable iteration{@code<? extends binding set, query evaluation exception>}
	 * @throws SailException the sail exception
	 */
	@Override
	public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(TupleExpr tupleExpr,
			Dataset dataset, BindingSet bindings, boolean includeInferred) throws SailException {
		ProjectionElemList originalProjectionElemList = null;
		if(getQueryType().equals(QueryType.TUPLE)) {
			 originalProjectionElemList = getOriginalProjectionElemList(tupleExpr);
		}
		QueryContext queryContext = new QueryContext(); 
		queryContext.setAttribute(IntelligentGraphConstants.SAIL, intelligentGraphSail);
		queryContext.setAttribute(IntelligentGraphConstants.TUPLEEXPR, tupleExpr);
		queryContext.setAttribute(IntelligentGraphConstants.INTELLIGENTGRAPHCONNECTION, this);//this.getWrappedConnection());
		queryContext.setAttribute(IntelligentGraphConstants.DATASET, dataset);
		//queryContext.setAttribute(IntelligentGraphConstants.PREFIXES, prefixes);
		queryContext.setAttribute(IntelligentGraphConstants.QUERYTYPE, getQueryType() );
		return new IntelligentGraphEvaluator(super.evaluate(tupleExpr, dataset, bindings, includeInferred) ,queryContext,originalProjectionElemList);
	}
	
	/**
	 * Gets the original projection elem list.
	 *
	 * @param tupleExpr the tuple expr
	 * @return the original projection elem list
	 */
	private ProjectionElemList getOriginalProjectionElemList(TupleExpr tupleExpr){
		Set<String> varnames = VarNameCollector.process(tupleExpr);
		ProjectionElemList projElemList = new ProjectionElemList();
		for (String varname: varnames) {
			ProjectionElem projectionElem = new ProjectionElem(varname);
			projElemList.addElement(projectionElem);	
		}
		List<ProjectionElemList> projectionElemLists = ProjectionElemListCollector.process(tupleExpr);
		if(projectionElemLists.size()>0) {
			ProjectionElemList originalProjectionElemList = projectionElemLists.get(0);
			Set<String> targetNames = projElemList.getTargetNames();
			for (ProjectionElem projectionElem: originalProjectionElemList.getElements()) {//projectionElem.getSourceName()
				if(!targetNames.contains(projectionElem.getSourceName()))
					projElemList.addElement(projectionElem);	
			}
			originalProjectionElemList.getParentNode().replaceChildNode(originalProjectionElemList, projElemList);
			return originalProjectionElemList;
		}else
			return null;

	}

	/**
	 * Gets the query type.
	 *
	 * @return the query type
	 */
	QueryType getQueryType() {
		@SuppressWarnings("rawtypes")
		Class[] callingClasses = CallingClass.INSTANCE.getCallingClasses();	
		for(@SuppressWarnings("rawtypes") Class callingClass :callingClasses) {
			switch (callingClass.getName()){		
				case "org.eclipse.rdf4j.repository.sail.SailGraphQuery": return QueryType.GRAPH;
				case "org.eclipse.rdf4j.repository.sail.SailTupleQuery": return QueryType.TUPLE;
				case "org.eclipse.rdf4j.repository.sail.SailBooleanQuery": return QueryType.BOOLEAN;			
			}		
		}
		return null;
	}
}
