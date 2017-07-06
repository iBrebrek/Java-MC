package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class Node {
	
	/** Internally managed collection of children */
	private ArrayIndexedCollection allChildNodes;
	
	/**
	 * Adds given child to an internally managed collection of children.
	 * 
	 * @param child		child node being added.
	 */
	public void addChildNode(Node child) {
		if(child == null) {
			throw new IllegalArgumentException("Child node can not be null.");
		}
		if(allChildNodes == null) {
			allChildNodes = new ArrayIndexedCollection();
		}
		allChildNodes.add(child);
	}
	
	/**
	 *  Returns a number of (direct) children.
	 * 
	 * @return number of this node's children.
	 */
	public int numberOfChildren() {
		if(allChildNodes == null) {
			return 0;
		}
		return allChildNodes.size();
	}
	
	/**
	 *  Returns selected child or throws IndexOutOfBoundsException if the index is invalid.
	 * 
	 * @param index		index of this node's child.
	 * @return child node at given index.
	 */
	public Node getChild(int index) {
		if(allChildNodes == null) {
			throw new IndexOutOfBoundsException("This node has no children.");
		}
		//commented because that is being checked in method get
//		if(index < 0 || index > allChildNodes.size()-1) {
//			throw new IndexOutOfBoundsException("Index can be 0 to " + (allChildNodes.size()-1));
//		}
		return (Node)allChildNodes.get(index);
	}
}
