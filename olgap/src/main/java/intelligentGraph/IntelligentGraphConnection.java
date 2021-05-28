/*
 * inova8 2020
 */
package intelligentGraph;

import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.RecognitionException;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
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
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.UpdateContext;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailConnectionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pathCalc.Prefixes;
import pathCalc.Thing;
import pathPatternElement.PathElement;
import pathPatternProcessor.PathPatternException;
import pathQL.PathParser;
import pathQLRepository.PathQLRepository;
import pathQLResults.StatementResults;

//import pathCalc.Prefixes;

import static org.eclipse.rdf4j.model.util.Values.iri;

/**
 * The Class IntelligentGraphConnection.
 */
public class IntelligentGraphConnection extends NotifyingSailConnectionWrapper {
	static final String QUERYTYPE = "queryType";
	static final String PREFIXES = "prefixes";
	static final String DATASET = "dataset";
	static final String INTELLIGENTGRAPHCONNECTION = "intelligentGraphConnection";
	static final String TUPLEEXPR = "tupleExpr";
	static final String SAIL = "sail";
	private static final String GETFACTS = "http://inova8.com/pathql/getFacts";
	private static final String GETFACT = "http://inova8.com/pathql/getFact";
	protected final Logger logger = LoggerFactory.getLogger(IntelligentGraphConnection.class);
	/** The intelligent graph sail. */
	private IntelligentGraphSail intelligentGraphSail;
	
	/** The prefixes. */
	private Prefixes prefixes = new Prefixes();

	/**
	 * Instantiates a new intelligent graph connection.
	 *
	 * @param wrappedCon the wrapped con
	 * @param intelligentGraphSail the intelligent graph sail
	 */
	public IntelligentGraphConnection(NotifyingSailConnection wrappedCon, IntelligentGraphSail intelligentGraphSail) {
		super(wrappedCon);
		this.intelligentGraphSail= intelligentGraphSail;
		CloseableIteration<? extends Namespace, SailException> namespaces = wrappedCon.getNamespaces();
		while(namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}
	}
	
	
	public IntelligentGraphSail getIntelligentGraphSail() {
		return intelligentGraphSail;
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
	//	Evaluator.clearCache();
		super.addStatement(subject, predicate, object, contexts);
	}


	@Override
	public void removeStatements(Resource subj, IRI pred, Value obj, Resource... contexts) throws SailException {
	//	Evaluator.clearCache();
		super.removeStatements(subj, pred, obj, contexts);
	}
	


	@Override
	public void addStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
	//	Evaluator.clearCache();
		super.addStatement(modify, subj, pred, obj, contexts);
	}


	@Override
	public void removeStatement(UpdateContext modify, Resource subj, IRI pred, Value obj, Resource... contexts)
			throws SailException {
	//	Evaluator.clearCache();
		super.removeStatement(modify, subj, pred, obj, contexts);
	}


	@Override
	public void removeNamespace(String prefix) throws SailException {
	//	Evaluator.clearCache();
		super.removeNamespace(prefix);
	}


	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	public Prefixes getPrefixes() {
		return prefixes;
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
		if(pred!=null)
			switch (pred.stringValue()){
				case GETFACT: 
					
				case GETFACTS: 
					return  getFacts( subj,  obj, includeInferred, contexts);
				default:
					return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, obj, includeInferred, contexts),intelligentGraphSail,this,contexts); 
			}
		else
			return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, obj, includeInferred, contexts),intelligentGraphSail,this,contexts); 
		}catch(Exception e) {
			throw new SailException(e);
		}
	}
	private CloseableIteration<? extends IntelligentStatement, SailException> getFacts(Resource thingresource, Value pathQLValue,boolean includeInferred, Resource... contexts ) throws PathPatternException {
		PathQLRepository source = PathQLRepository.create(this);
		Thing thing = Thing.create(source, thingresource, null);
		String pathQL = pathQLValue.stringValue();
		PathElement pathElement = null;
		try {
			pathElement = PathParser.parsePathPattern(thing, pathQL);
		} catch (RecognitionException e) {
			throw e;
		} catch (PathPatternException e) {
			throw e;
		}
		
		EvaluationStrategy evaluationStrategy = new StrictEvaluationStrategy(source.getTripleSource(),thing.getDataset(), null);
		TupleExpr pathElementPattern = pathElement.pathPatternQuery(thing,null,null);
		pathElement.getSourceVariable().setValue( thing.getValue());
		BindingSet bindings = new QueryBindingSet();
		CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator = evaluationStrategy.evaluate(pathElementPattern,bindings);
		return (CloseableIteration<? extends IntelligentStatement, SailException>) new StatementResults( resultsIterator,thing, pathElement);
	}

	/**
	 * Evaluate.
	 *
	 * @param tupleExpr the tuple expr
	 * @param dataset the dataset
	 * @param bindings the bindings
	 * @param includeInferred the include inferred
	 * @return the closeable iteration<? extends binding set, query evaluation exception>
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
		queryContext.setAttribute(SAIL, intelligentGraphSail);
		queryContext.setAttribute(TUPLEEXPR, tupleExpr);
		queryContext.setAttribute(INTELLIGENTGRAPHCONNECTION, this);//this.getWrappedConnection());
		queryContext.setAttribute(DATASET, dataset);
		queryContext.setAttribute(PREFIXES, prefixes);
		queryContext.setAttribute(QUERYTYPE, getQueryType() );
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
