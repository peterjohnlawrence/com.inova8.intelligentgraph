/*
 * inova8 2020
 */
package intelligentGraph;

import org.eclipse.rdf4j.common.iteration.AbstractCloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.ContextStatement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.sail.SailException;

import path.EdgeBinding;
import path.PathBinding;
import path.PathTupleExpr;

import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.eclipse.rdf4j.model.util.Values.iri;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import pathCalc.CustomQueryOptions;
import pathCalc.Thing;
import pathPatternElement.Iterations;
import pathPatternElement.PathElement;
import pathQLRepository.PathQLRepository;


public  class IntelligentStatementPaths extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	Thing thing;
	PathElement pathElement;
	Iterations sortedIterations;
	private Integer pathIteration;
	IntelligentGraphConnection intelligentGraphConnection;
	PathQLRepository source;
	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;
	private Resource[] contexts;
	private PathTupleExpr pathTupleExpr;
	public IntelligentStatementPaths(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
	//	this.pathTupleExpr=pathTupleExpr;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	public IntelligentStatementPaths( PathQLRepository source, Thing thing,
			PathElement pathElement, IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=null;
		this.source=source;
		this.thing=thing;
		this.pathElement=pathElement;
		this.sortedIterations = pathElement.getIterations().sortByPathLength();
		this.pathIteration=0;
		//this.pathTupleExpr=pathTupleExpr;
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		this.contexts = contexts;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}
	@Override
	public boolean hasNext() throws QueryEvaluationException {
		boolean hasNext = getResultsIterator().hasNext();
		if(hasNext) {
			return true;
		}else {
			pathIteration ++;
			if(pathIteration < this.sortedIterations.size()) {
				pathTupleExpr = pathElement.pathPatternQuery(thing,pathIteration);
				this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement, pathTupleExpr,contexts);
				return getResultsIterator().hasNext();
			}else {
				return false;
			}
		}	
	}

	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = getResultsIterator().next();
		Binding predBinding =nextBindingset.getBinding("n0");
		Literal obj = pathToLiteral(nextBindingset);
		if(obj!=null )
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement(this.thing.getIRI(), iri(IntelligentGraphConnection.HASPATH), obj,null),null,thing.getEvaluationContext(), customQueryOptions);
		else
			return new IntelligentStatement(null, null, null,null);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		getResultsIterator().remove();	
	}
	private Literal pathToLiteral(BindingSet bindingset) {
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("",IntelligentGraphConnection.PATHQL);
		PathBinding thisPathBinding =pathTupleExpr.getPath();
		
		for(EdgeBinding edge: thisPathBinding) {
			ModelBuilder pathModelBuilder = builder.subject("urn://path/"+bindingset.hashCode());
			String edgeCode = "urn://edge/"+bindingset.hashCode()+"/"+ edge.toString().hashCode();
			pathModelBuilder.add(IntelligentGraphConnection.path_Edge, edgeCode);
			ModelBuilder subject = builder.subject(edgeCode);
			subject.add(RDF.TYPE , iri(IntelligentGraphConnection.Edge));
			subject.add(IntelligentGraphConnection.edge_Source , bindingset.getBinding(edge.getSourceVariable().getName()).getValue());
			subject.add(IntelligentGraphConnection.edge_Predicate , bindingset.getBinding(edge.getPredicateVariable().getName()).getValue());
			subject.add(IntelligentGraphConnection.edge_Target , bindingset.getBinding(edge.getTargetVariable().getName()).getValue());
			subject.add(IntelligentGraphConnection.edge_Direct ,edge.getDirection());
			if(edge.getIsDereified()!=null) subject.add(IntelligentGraphConnection.edge_Dereified , edge.getIsDereified());
			if(edge.getReification()!=null) subject.add(IntelligentGraphConnection.edge_Reification , edge.getReification());			
		}
		Model pathModel = builder.build();
		OutputStream pathJSON = new ByteArrayOutputStream();
		Rio.write(pathModel, pathJSON, RDFFormat.TURTLE);
		return literal(pathJSON.toString());
	}
	public CloseableIteration<BindingSet, QueryEvaluationException>  getResultsIterator() {
		if(resultsIterator!=null)
			return resultsIterator;
		else {
			pathTupleExpr = pathElement.pathPatternQuery(thing,pathIteration);
			this.resultsIterator=intelligentGraphConnection.getResultsIterator(source, thing,pathElement,pathTupleExpr, contexts);
			return resultsIterator;
		}
	}
}
