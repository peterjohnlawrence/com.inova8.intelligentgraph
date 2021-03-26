package pathPatternElement;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import pathCalc.Thing;
import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class ObjectElement extends PathElement{

	IRI iri;
	Literal literal;
	ArrayList<VerbObjectList>  blankNodeVerbObjectList;
	public ObjectElement() {
		super();
		operator=PathConstants.Operator.OBJECT;
	}
	public IRI getIri() {
		return iri;
	}
	public void setIri(IRI iri) {
		this.iri = iri;
	}
	public Literal getLiteral() {
		return literal;
	}
	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	public ArrayList<VerbObjectList> getBlankNodeVerbObjectList() {
		return blankNodeVerbObjectList;
	}
	public void setBlankNodeVerbObjectList(ArrayList<VerbObjectList> blankNodeVerbObjectList) {
		this.blankNodeVerbObjectList = blankNodeVerbObjectList;
	}
	public String toString() {
		String object ="";
		if(iri!=null ) {
			object += "<"+ iri.stringValue() +">";
		}else if(literal!=null ) {
			object +=  literal.stringValue();
		}else if(blankNodeVerbObjectList!=null) {
			object +=  blankNodeVerbObjectList.toString() ;	
		}
		return object;
	}
	public String toSPARQL() {
		String object ="";
		if(iri!=null ) {
			object += "<"+ iri.stringValue() +">";
		}else if(literal!=null ) {
			object +=  literal.stringValue();
		}else if(blankNodeVerbObjectList!=null) {
			for(   VerbObjectList verbObjectList  : blankNodeVerbObjectList) {
				object +=  verbObjectList.toSPARQL() ;	
			}			
		}
		return object;
	}
	
	@Override
	public String toHTML() {

		return null;
	}
	@Override
	public TupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable) {
		return null;
	}
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setEntryIndex(entryIndex);
		setBaseIndex(baseIndex);
		if(blankNodeVerbObjectList!=null) {
			for(   VerbObjectList verbObjectList  : blankNodeVerbObjectList) {
				verbObjectList.indexVisitor(baseIndex, entryIndex,edgeCode) ;	
			}			
		}
		setExitIndex(entryIndex);
		return getExitIndex();
	}
	@Override
	public Boolean getIsNegated() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setIsNegated(Boolean isDereified) {
		// TODO Auto-generated method stub
		
	}


}
