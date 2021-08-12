/*
 * inova8 2020
 */
package olgap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inova8.intelligentgraph.vocabulary.OLGAP;

import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.CloseableIteratorIteration;
import org.eclipse.rdf4j.common.iteration.ConvertingIteration;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.spin.function.InverseMagicProperty;

/**
 * The Class DegreeDistributionTupleFunction.
 */
public class DegreeDistributionTupleFunction implements InverseMagicProperty{
	
	/** The logger. */
	private static final Logger logger   = LoggerFactory.getLogger(DegreeDistributionTupleFunction.class);
	
	/**
	 * Instantiates a new degree distribution tuple function.
	 */
	public DegreeDistributionTupleFunction()  {
		super();
		logger.info("Initiating DegreeDistributionTupleFunction");
	}	
	
	/** The conn. */
	public  RepositoryConnection conn;

	
	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	@Override
	public String getURI() {
		return OLGAP.DEGREEDISTRIBUTION;//NAMESPACE + "degreeDistribution";
	}
	
	/**
	 * Evaluate.
	 *
	 * @param valueFactory the value factory
	 * @param args the args
	 * @return the closeable iteration<? extends list<? extends value>, query evaluation exception>
	 * @throws QueryEvaluationException the query evaluation exception
	 */
	@Override
	public CloseableIteration<? extends List<? extends Value>, QueryEvaluationException> evaluate(
			ValueFactory valueFactory, Value... args) throws QueryEvaluationException {

		//Repository rep = new HTTPRepository(args[0].stringValue(),args[1].stringValue());//"http://localhost:8082/rdf4j-server/", "tfl");
		org.eclipse.rdf4j.repository.Repository rep = new SPARQLRepository(args[0].stringValue());
		rep.init();	
		conn = rep.getConnection();
		
		String queryString = "SELECT  ?degree (COUNT(?degree) AS ?count)\r\n" + "WHERE{\r\n"
				+ "  {SELECT(COUNT(?n)AS?degree)\r\n" + "    WHERE{\r\n" + "      {?n?out_edge?out}\r\n"
				+ "      UNION\r\n" + "      {?in?in_edge?n}\r\n" + "    }GROUP BY ?n}}\r\n" + "GROUP BY ?degree";

		TupleQuery query = conn.prepareTupleQuery(queryString);

		Collection<BindingSet> results  = new ArrayList<BindingSet>();
		try (TupleQueryResult result = query.evaluate()) {
			while (result.hasNext()) {
				results.add(result.next());
			}
		}
		return new ConvertingIteration<BindingSet, List<Value>, QueryEvaluationException>(
				new CloseableIteratorIteration<>(results.iterator())) {

			@Override
			protected List<Value> convert(BindingSet bindings) throws QueryEvaluationException {
				List<Value> results = new ArrayList<>();
				for (String bindingName : bindings.getBindingNames()) {
					results.add(bindings.getValue(bindingName));
				}
				return results;
			}
		};
	}
}
