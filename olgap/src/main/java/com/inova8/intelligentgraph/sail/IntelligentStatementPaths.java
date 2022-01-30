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
import com.inova8.intelligentgraph.model.Thing;
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




/**
 * The Class IntelligentStatementPaths.
 */
public  class IntelligentStatementPaths extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	
	/** The results iterator. */
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	
	/** The thing. */
	Thing thing;
	
	/** The path element. */
	PathElement pathElement;
	
	/** The sorted iterations. */
	Iterations sortedIterations;
	
	/** The path iteration. */
	private Integer pathIteration;
	
	/** The intelligent graph connection. */
	IntelligentGraphConnection intelligentGraphConnection;
	
	/** The source. */
	IntelligentGraphRepository source;
	
	/** The simple value factory. */
	private SimpleValueFactory simpleValueFactory;
	
	/** The custom query options. */
	private CustomQueryOptions customQueryOptions;
	
	/** The contexts. */
	private Resource[] contexts;
	
	/** The path tuple expr. */
	private PathTupleExpr pathTupleExpr;
	
	/**
	 * Instantiates a new intelligent statement paths.
	 *
	 * @param resultsIterator the results iterator
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 */
	public IntelligentStatementPaths(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	
	/**
	 * Instantiates a new intelligent statement paths.
	 *
	 * @param source the source
	 * @param thing the thing
	 * @param pathElement the path element
	 * @param intelligentGraphConnection the intelligent graph connection
	 * @param customQueryOptions the custom query options
	 * @param contexts the contexts
	 */
	public IntelligentStatementPaths( IntelligentGraphRepository source, Thing thing,
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
	
	/**
	 * Checks for next.
	 *
	 * @return true, if successful
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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
	
	/**
	 * Next.
	 *
	 * @return the intelligent statement
	 * @throws QueryEvaluationException the query evaluation exception
	 */
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
	
	/**
	 * Gets the results iterator.
	 *
	 * @return the results iterator
	 */
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
	
	/**
	 * Removes the.
	 *
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public void remove() throws QueryEvaluationException {
		getResultsIterator().remove();	
	}
	
	/**
	 * Path to literal.
	 *
	 * @param bindingset the bindingset
	 * @return the literal
	 */
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
