package path;

import org.eclipse.rdf4j.query.algebra.TupleExpr;

public class PathTupleExpr {
	TupleExpr tupleExpr;
	PathBinding path= new PathBinding();
	
	public PathTupleExpr(TupleExpr tupleExpr) {
		super();
		this.tupleExpr = tupleExpr;
	}
	public TupleExpr getTupleExpr() {
		return tupleExpr;
	}
	public PathBinding getPath() {
		return path;
	}
	public void  setPath(PathBinding path) {
		this.path= path;;
	}
	public String toString(){
		return getTupleExpr().toString();
	}
	public void setTupleExpr(TupleExpr tupleExpr) {
		this.tupleExpr = tupleExpr;
	}
	public Object pathToString() {
		return getPath().toString();
	}

}
