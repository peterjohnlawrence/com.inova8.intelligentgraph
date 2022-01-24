/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.sail;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.SailException;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.model.EvaluatorThing;
import com.inova8.intelligentgraph.path.EdgeBinding;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.utilities.CustomQueryOption;
import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.pathql.element.Iterations;
import com.inova8.pathql.element.PathElement;

import static org.eclipse.rdf4j.model.util.Values.literal;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;




public  class IntelligentStatementPaths extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	EvaluatorThing thing;
	PathElement pathElement;
	Iterations sortedIterations;
	private Integer pathIteration;
	IntelligentGraphConnection intelligentGraphConnection;
	IntelligentGraphRepository source;
	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private PathTupleExpr pathTupleExpr;
	public IntelligentStatementPaths(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, EvaluatorThing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	public IntelligentStatementPaths( IntelligentGraphRepository source, EvaluatorThing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=null;
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.pathIteration=0;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		if(resultsIterator!=null && resultsIterator.hasNext()) {
			return true;
		}else {		
			while(pathIteration < this.sortedIterations.size() ) {
				CustomQueryOptions customQueryOptions= CustomQueryOption.getCustomQueryOptions(contexts,source.getRepositoryContext().getPrefixes());
				pathTupleExpr = pathElement.pathPatternQuery(pathIteration,customQueryOptions);
				pathIteration ++;
				this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathTupleExpr,contexts);
				boolean hasNext = resultsIterator.hasNext();
				if(hasNext) return true;
			}
			return false;
		}	
	}
	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = getResultsIterator().next();
		//Binding predBinding =nextBindingset.getBinding("n0");
		Literal obj = pathToLiteral(nextBindingset);
		if(obj!=null )
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement(this.thing.getIRI(), PATHQL.HASPATH, obj,null),null,thing.getEvaluationContext(), customQueryOptions);
		else
			return new IntelligentStatement(null, null, null);
	}
	public CloseableIteration<BindingSet, QueryEvaluationException>  getResultsIterator() {
		if(resultsIterator!=null)
			return resultsIterator;
		else {	
//			CustomQueryOptions customQueryOptions= URNCustomQueryOptionsDecode.getCustomQueryOptions(contexts,source.getIntelligentGraphConnection().getPrefixes());
//			pathTupleExpr = pathElement.pathPatternQuery(thing,pathIteration,customQueryOptions);
//			this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement,pathTupleExpr, contexts);
			return resultsIterator;
		}
	}
	@Override
	public void remove() throws QueryEvaluationException {
		getResultsIterator().remove();	
	}
	private Literal pathToLiteral(BindingSet bindingset) {
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("",PATHQL.NAMESPACE);
		PathBinding thisPathBinding =pathTupleExpr.getPath();
		
		for(EdgeBinding edge: thisPathBinding) {
			edge.addEdgeToPathModel(builder,bindingset);
					
		}
		Model pathModel = builder.build();
		OutputStream pathJSON = new ByteArrayOutputStream();
		Rio.write(pathModel, pathJSON, RDFFormat.TURTLE);
		return literal(pathJSON.toString());
	}

}
