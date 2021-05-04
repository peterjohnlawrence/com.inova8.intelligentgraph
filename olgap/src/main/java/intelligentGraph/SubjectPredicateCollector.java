package intelligentGraph;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.query.algebra.Filter;
import org.eclipse.rdf4j.query.algebra.QueryModelNode;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;

public class SubjectPredicateCollector extends AbstractQueryModelVisitor<RuntimeException> {

	public static List<StatementPattern> process(QueryModelNode node, String name) {
		SubjectPredicateCollector collector = new SubjectPredicateCollector();
		bindingValueName=name;
		node.visit(collector);
		return collector.getStatementPatterns();
	}

	private List<StatementPattern> stPatterns = new ArrayList<>();
	private static String bindingValueName;

	public List<StatementPattern> getStatementPatterns() {
		return stPatterns;
	}

	@Override
	public void meet(Filter node) {
		// Skip boolean constraints
		node.getArg().visit(this);
	}

	@Override
	public void meet(StatementPattern node) {
		 Var objectVar = node.getObjectVar();
		 if(objectVar!=null) 
			 if(objectVar.getName().equals(bindingValueName) )		
					stPatterns.add(node);		 		
	}
}

