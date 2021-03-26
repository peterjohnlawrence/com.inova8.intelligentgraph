package pathQLModel;

import java.net.URI;

import pathPatternElement.PredicateElement;
import pathPatternProcessor.PathPatternException;
import pathQLResults.ResourceResults;

public class NullValue extends Resource {
  public NullValue(){
	super(null);
}

@Override
public Resource getFact(String predicatePattern) throws PathPatternException {
	// TODO Auto-generated method stub
	return null;
}

@Override
public ResourceResults getFacts(String predicatePattern) throws PathPatternException {
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

@Override
public URI getId() {
	// TODO Auto-generated method stub
	return null;
}
}
