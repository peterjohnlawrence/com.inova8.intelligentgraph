/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

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
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.intelligentgraph.vocabulary.RDF;
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

	public static CustomQueryOptions customQueryOptions;
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
	

	public IntelligentGraphSail getIntelligentGraphSail() {
		return intelligentGraphSail;
	}
	@Override
	public synchronized void clear(Resource... contexts) throws SailException {
		boolean local =  startLocalTransaction();
		//TODO clear(contexts) fails to commit the chnages to the named graph, so removeStatements used instead
		//getWrappedConnection(). clear(contexts);
		removeStatements(null,null,null,contexts);
		conditionalCommit(local);
		this.intelligentGraphSail.setContextsAreLazyLoaded(false);
	}

	protected final boolean startLocalTransaction() throws RepositoryException {
		if (!isActive()) {
			begin();
			return true;
		}
		return false;
	}

	protected final void conditionalCommit(boolean condition) throws RepositoryException {
		if (condition) {
			commit();
		}
	}
	@Override
	public void setNamespace(String prefix, String name) throws SailException {
		this.intelligentGraphSail.setPrefixesAreLazyLoaded(false);
		super.setNamespace(prefix, name);
	}

	@Override
	public void clearNamespaces() throws SailException {
		this.intelligentGraphSail.setPrefixesAreLazyLoaded(false);
		super.clearNamespaces();
	}

	@Override
	public void addStatement(Resource subject, IRI predicate, Value object, Resource... contexts)
			throws SailException {
//		this.intelligentGraphSail.clearCache();
//		//TODO
//		//If pred= http://inova8.com/script/isPrivate then add subject to public/private contexts
//		//Or set lazyLoaded =false
//		super.addStatement(subject, predicate, object, contexts);
		try {
			String[] predicateParts;
			if (predicate != null) {
				predicateParts= predicate.stringValue().split("\\?pathQL=");
				switch (predicateParts[0]) {
				case PATHQL.addFact:
					addFact(subject, URLDecoder.decode(predicateParts[1],StandardCharsets.UTF_8.toString()) , object, contexts);
					break;
				default:
					super.addStatement(subject, predicate, object, contexts);
				}
			}
			else
				super.addStatement(subject, predicate, object, contexts);
			this.intelligentGraphSail.clearCache();
			IntelligentGraphRepository.create(this).clearRepositoryContext();
		} catch (Exception e) {
			throw new SailException(e);
		}		
		
		
	}

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
		}
		
	}


	@Override
	public void addStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
		this.intelligentGraphSail.clearCache();
		super.addStatement(modify, subj, pred, obj, contexts);
	}
	@Override
	public void removeStatements(Resource subj, IRI pred, Value obj, Resource... contexts) throws SailException {
		try {
			if (pred != null)
				switch (pred.stringValue()) {
				case PATHQL.removeFact:
				case PATHQL.removeFacts:
					deleteFacts(null, subj, obj, contexts);
					break;
				default:
					super.removeStatements(subj, pred, obj, contexts);
				}
			else
				super.removeStatements(subj, pred, obj, contexts);
		} catch (Exception e) {
			throw new SailException(e);
		}
	}
	@Override
	public void removeStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
		try {
		if(pred!=null)
			switch (pred.stringValue()){
				case PATHQL.removeFact: 					
				case PATHQL.removeFacts: 
					deleteFacts(modify, subj,  obj,  contexts);
					break;
				default:
					super.removeStatement(modify, subj, pred, obj, contexts);
			}
		else
			super.removeStatement(modify, subj, pred, obj, contexts);
		}catch(Exception e) {
			throw new SailException(e);
		}
	}	

	@Override
	public void removeNamespace(String prefix) throws SailException {
		super.removeNamespace(prefix);
	}

	public HashSet<IRI> getPublicContexts() {
		return intelligentGraphSail.getPublicContexts();
	}
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
	public HashSet<IRI> getPrivateContexts() {
		return intelligentGraphSail.getPrivateContexts();
	}
	@Override
	public CloseableIteration<? extends IntelligentStatement, SailException> getStatements(Resource subj, IRI pred, Value obj,
			boolean includeInferred, Resource... contexts) throws SailException {
		try {
			Resource[] extendedContexts=contexts;
			if(pred!=null && !pred.stringValue().equals("http://inova8.com/script/isPrivate")) {
					extendedContexts = getContexts(contexts);
			}
			if(pred!=null) {
				switch (pred.stringValue()){
					case PATHQL.getFact: 						
					case PATHQL.getFacts: 
						return  getFacts( subj,  obj,  extendedContexts);
					case PATHQL.getPath: 						
					case PATHQL.getPaths: 
						return  getPaths( subj,  obj,  extendedContexts);
					case PATHQL.traceFact: 						
					case PATHQL.traceFacts: 
						return  traceFacts( subj,  obj,  extendedContexts); 						
					case PATHQL.clearCache: 
						return  clearCache( subj,  pred, obj,  extendedContexts);
					case PATHQL.getScript: 						
						return  getScript( subj,  obj,  extendedContexts);
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
	private CloseableIteration<? extends IntelligentStatement, SailException> clearCache(Resource subj, IRI pred,Value obj,
			Resource[] extendedContexts) {
		intelligentGraphSail.clearCache(obj); 
		return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, literal(true), false, extendedContexts),intelligentGraphSail,this,extendedContexts); 
	}


	private CloseableIteration<? extends IntelligentStatement, SailException> getScript(Resource subj, Value pathQLValue,	 Resource... contexts) {
		// TODO Auto-generated method stub
		return null;
	}


	@SuppressWarnings("deprecation")
	private CloseableIteration<? extends IntelligentStatement, SailException> traceFacts(Resource thingresource, Value pathQLValue,	  Resource... contexts) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		thing.getEvaluationContext().setTracing(true);
		thing.getEvaluationContext().getTracer().clear();
		
		thing.getEvaluationContext().getTracer().traceFacts(thing, pathQLValue,source.getRepositoryContext().getPrefixes(), contexts);
		String pathQL = toPathQLString(pathQLValue);
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return traceThingFacts(source, thing,pathElement, contexts );
	}

	private CloseableIteration<? extends IntelligentStatement, SailException> getFacts(Resource thingresource, Value pathQLValue, Resource... contexts ) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = toPathQLString(pathQLValue);
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return getThingFacts(source, thing,pathElement, contexts );
	}
	private CloseableIteration<? extends IntelligentStatement, SailException> getPaths(Resource thingresource, Value pathQLValue, Resource... contexts ) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = toPathQLString(pathQLValue);
		PathElement pathElement =  PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		return getThingPaths(source, thing,pathElement, contexts );
	}
	private CloseableIteration<? extends IntelligentStatement, SailException> getThingFacts(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) 
				new IntelligentStatementResults(source,thing, pathElement,this,customQueryOptions,contexts);
	}
	private CloseableIteration<? extends IntelligentStatement, SailException> traceThingFacts(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) 
				new IntelligentStatementResults(source,thing, pathElement,this,customQueryOptions,true,contexts);
	}	
	
	private CloseableIteration<? extends IntelligentStatement, SailException> getThingPaths(IntelligentGraphRepository source,Thing thing, PathElement pathElement,Resource... contexts ) throws PathPatternException {
		return (CloseableIteration<? extends IntelligentStatement, SailException>) new IntelligentStatementPaths( source,thing, pathElement,  this,customQueryOptions,contexts);	
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
	private void deleteFacts(UpdateContext modify, Resource thingresource, Value pathQLValue,  Resource... contexts) throws PathPatternException {
		IntelligentGraphRepository source = IntelligentGraphRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = toPathQLString(pathQLValue);
		PathElement pathElement = PathParser.parsePathPattern(source.getRepositoryContext(), pathQL);
		pathElement.getSourceVariable().setValue( thing.getValue());
		deleteThingFacts( modify, source, thing,  pathElement, contexts) ;

	}
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
				super.removeStatements((Resource)bindingSet.getBinding(subj).getValue(), (IRI)bindingSet.getBinding(pred).getValue(), bindingSet.getBinding(obj).getValue(), contexts);
			}
		}
	}

	CloseableIteration<BindingSet, QueryEvaluationException> getResultsIterator(IntelligentGraphRepository source,Thing thing, PathElement pathElement, PathTupleExpr pathTupleExpr, Resource... contexts)
			throws IllegalArgumentException, QueryEvaluationException {
		TupleExpr tupleExpr = pathTupleExpr.getTupleExpr();
		SimpleDataset dataset = prepareDataset(pathElement, source,contexts);
		BindingSet bindings = new QueryBindingSet();
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(),dataset, null);
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(tupleExpr,bindings);
		return resultsIterator;
	}

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


	private CustomQueryOptions prepareCustomQueryOptions(PathElement pathElement, IntelligentGraphRepository source, Resource... contexts) {
		CustomQueryOptions customQueryOptions = CustomQueryOption.getCustomQueryOptions(contexts,source.getRepositoryContext().getPrefixes());//TODOgetPrefixes());
		CustomQueryOptions pathQLCustomQueryOptions = pathElement.getCustomQueryOptions();
		if( pathQLCustomQueryOptions !=null) {
			pathQLCustomQueryOptions.addInherited(customQueryOptions);
			customQueryOptions= pathQLCustomQueryOptions;
		}
		return customQueryOptions;
	}

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
	private Boolean containsCustomQueryOptions(Resource[] contexts) {
		for(Resource context:contexts) {
			if(context.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS) ) {
				return true;
			}
		}
		return false;
	}
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
