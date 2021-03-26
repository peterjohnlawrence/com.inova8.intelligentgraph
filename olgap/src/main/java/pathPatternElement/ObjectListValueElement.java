package pathPatternElement;

import java.util.ArrayList;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;

public class ObjectListValueElement extends ValueElement {
	ArrayList<ObjectElement>  objectList;
	public ObjectListValueElement() {
		super();
		operator=PathConstants.Operator.OBJECTLIST;
	}

	public ArrayList<ObjectElement> getObjectList() {
		return objectList;
	}

	public void setObjectList(ArrayList<ObjectElement> objectList) {
		this.objectList = objectList;
	}
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex,EdgeCode edgeCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
