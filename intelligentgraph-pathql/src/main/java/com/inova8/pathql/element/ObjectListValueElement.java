/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import java.util.ArrayList;

import com.inova8.intelligentgraph.constants.PathConstants;
import com.inova8.intelligentgraph.constants.PathConstants.EdgeCode;
import com.inova8.intelligentgraph.repositoryContext.RepositoryContext;

/**
 * The Class ObjectListValueElement.
 */
public class ObjectListValueElement extends ValueElement {
	
	/** The object list. */
	ArrayList<ObjectElement>  objectList;
	
	/**
	 * Instantiates a new object list value element.
	 *
	 * @param source the source
	 */
//	public ObjectListValueElement(IntelligentGraphRepository source) {
//		super(source);
//		operator=PathConstants.Operator.OBJECTLIST;
//	}
	public ObjectListValueElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.OBJECTLIST ;
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
