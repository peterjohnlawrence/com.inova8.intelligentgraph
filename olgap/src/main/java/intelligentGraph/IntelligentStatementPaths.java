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
import pathPatternElement.PathElement;


public  class IntelligentStatementPaths extends AbstractCloseableIteration< IntelligentStatement, SailException> {
	CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator;
	Thing thing;
	PathElement pathElement;
	IntelligentGraphConnection intelligentGraphConnection;

	private SimpleValueFactory simpleValueFactory;
	private CustomQueryOptions customQueryOptions;

	private PathTupleExpr pathTupleExpr;
	private PathBinding path;
	public IntelligentStatementPaths(CloseableIteration<BindingSet, QueryEvaluationException> resultsIterator, Thing thing,
			PathElement pathElement,PathTupleExpr pathTupleExpr,  IntelligentGraphConnection intelligentGraphConnection, CustomQueryOptions customQueryOptions,Resource ...contexts ) {
		this.resultsIterator=resultsIterator;
		this.thing=thing;
		this.pathElement=pathElement;
		this.pathTupleExpr=pathTupleExpr;
		this.path=pathTupleExpr.getPath();
		this.intelligentGraphConnection=intelligentGraphConnection;
		this.customQueryOptions=customQueryOptions;
		simpleValueFactory= SimpleValueFactory.getInstance();
	}

	@Override
	public boolean hasNext() throws QueryEvaluationException {
		return resultsIterator.hasNext();
	}

	@Override
	public IntelligentStatement next() throws QueryEvaluationException {
		BindingSet nextBindingset = resultsIterator.next();
		Binding predBinding =nextBindingset.getBinding("n0");
		Literal obj = pathToLiteral(nextBindingset);
		if(obj!=null )
				return new IntelligentStatement((ContextStatement) simpleValueFactory.createStatement(this.thing.getIRI(), iri(IntelligentGraphConnection.HASPATH), obj,null),null,thing.getEvaluationContext(), customQueryOptions);
		else
			return new IntelligentStatement(null, null, null,null);
	}

	@Override
	public void remove() throws QueryEvaluationException {
		resultsIterator.remove();	
	}
	private Literal pathToLiteral(BindingSet bindingset) {
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("",IntelligentGraphConnection.PATHQL);
		
		
		for(EdgeBinding edge: this.path) {
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

}
