package intelligentGraph;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

public class FactCache {
	final  Repository factCache;
	public FactCache() {
		factCache = new SailRepository(new MemoryStore());
	}
	public boolean contains(IRI thing, IRI predicate, Resource customQueryOptions) {
		return factCache.getConnection().hasStatement(thing, predicate, null, false, customQueryOptions);
		
	}
	public Value getFacts(IRI thing, IRI predicate, Resource customQueryOptions) {
		RepositoryResult<Statement> statements = factCache.getConnection().getStatements(thing, predicate, null, false, customQueryOptions);
		if(statements.hasNext()) {
			Statement statement = statements.next();
			Value object = statement.getObject();
			return object;
		}
		return null;
		
	}
	public void addFact(IRI thing, IRI predicate, Value fact, Resource customQueryOptions) {
		factCache.getConnection().add(thing, predicate, fact,customQueryOptions  );		
	}
	public void clear() {
		factCache.getConnection().clear();
	}
}
