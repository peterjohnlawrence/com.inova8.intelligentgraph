/*
 * inova8 2020
 */
package pathPatternElement;

import java.util.ArrayList;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathQLRepository.PathQLRepository;

/**
 * The Class ObjectListValueElement.
 */
public class ObjectListValueElement extends ValueElement {
	
	/** The object list. */
	ArrayList<ObjectElement>  objectList;
	
	/**
	 * Instantiates a new object list value element.
	 */
	public ObjectListValueElement(PathQLRepository source) {
		super(source);
		operator=PathConstants.Operator.OBJECTLIST;
	}

	/**
	 * Gets the object list.
	 *
	 * @return the object list
	 */
	public ArrayList<ObjectElement> getObjectList() {
		return objectList;
	}

	/**
	 * Sets the object list.
	 *
	 * @param objectList the new object list
	 */
	public void setObjectList(ArrayList<ObjectElement> objectList) {
		this.objectList = objectList;
	}
	
	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex,EdgeCode edgeCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
