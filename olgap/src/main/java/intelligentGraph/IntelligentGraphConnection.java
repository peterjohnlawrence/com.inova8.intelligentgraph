package intelligentGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.Dataset;
import org.eclipse.rdf4j.query.Query.QueryType;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.ProjectionElem;
import org.eclipse.rdf4j.query.algebra.ProjectionElemList;
import org.eclipse.rdf4j.query.algebra.TupleExpr;
import org.eclipse.rdf4j.query.algebra.evaluation.QueryContext;
import org.eclipse.rdf4j.query.algebra.helpers.VarNameCollector;
import org.eclipse.rdf4j.sail.NotifyingSailConnection;
import org.eclipse.rdf4j.sail.SailException;
import org.eclipse.rdf4j.sail.helpers.NotifyingSailConnectionWrapper;

import pathCalc.Evaluator;
import static org.eclipse.rdf4j.model.util.Values.iri;
public class IntelligentGraphConnection extends NotifyingSailConnectionWrapper {

	private IntelligentGraphSail intelligentGraphSail;
	private HashMap<String,IRI> prefixes = new HashMap<String,IRI>();

	public IntelligentGraphConnection(NotifyingSailConnection wrappedCon, IntelligentGraphSail intelligentGraphSail) {
		super(wrappedCon);
		this.intelligentGraphSail= intelligentGraphSail;
		CloseableIteration<? extends Namespace, SailException> namespaces = wrappedCon.getNamespaces();
		while(namespaces.hasNext()) {
			Namespace namespace = namespaces.next();
			prefixes.put(namespace.getPrefix(), iri(namespace.getName()));
		}
	}
	public HashMap<String, IRI> getPrefixes() {
		return prefixes;
	}

	@Override
	public CloseableIteration<? extends Statement, SailException> getStatements(Resource subj, IRI pred, Value obj,
			boolean includeInferred, Resource... contexts) throws SailException {
		return new IntelligentGraphStatementsIterator( super.getStatements(subj, pred, obj, includeInferred, contexts),intelligentGraphSail,this,contexts); 
	}

	@Override
	public CloseableIteration<? extends BindingSet, QueryEvaluationException> evaluate(TupleExpr tupleExpr,
			Dataset dataset, BindingSet bindings, boolean includeInferred) throws SailException {
		ProjectionElemList originalProjectionElemList = null;
		if(getQueryType().equals(QueryType.TUPLE)) {
			 originalProjectionElemList = getOriginalProjectionElemList(tupleExpr);
		}
		QueryContext queryContext = new QueryContext(); 
		queryContext.setAttribute("sail", intelligentGraphSail);
		queryContext.setAttribute("tupleExpr", tupleExpr);
		queryContext.setAttribute("wrappedCon", this.getWrappedConnection());
		queryContext.setAttribute("dataset", dataset);
		queryContext.setAttribute("prefixes", prefixes);
		queryContext.setAttribute("queryType", getQueryType() );
		return new IntelligentGraphEvaluator(super.evaluate(tupleExpr, dataset, bindings, includeInferred) ,queryContext,originalProjectionElemList);
	}
	@Override
	public void addStatement(Resource subject, IRI predicate, Value object, Resource... contexts)
			throws SailException {
		Evaluator.clearCache();
		super.addStatement(subject, predicate, object, contexts);
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
