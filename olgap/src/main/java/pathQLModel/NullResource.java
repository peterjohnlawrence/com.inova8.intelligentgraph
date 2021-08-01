/*
 * inova8 2020
 */
package pathQLModel;

import org.eclipse.rdf4j.model.Value;

import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLResults.ResourceResults;

public class NullResource extends Resource {

	private static final long serialVersionUID = 1L;

	public NullResource() {
		super(null);
	}

	@Override
	public Resource getFact(String predicatePattern) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(String predicatePattern, Value... bindValues) throws PathPatternException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceResults getFacts(PredicateElement path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getSubject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource getPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSnippet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getScore() {
		// TODO Auto-generated method stub
		return null;
	}

}
