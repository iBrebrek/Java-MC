package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * A node representing a command which generates some textual output dynamically.
 * 
 * It's written in tags {$ = ... $}
 * where ... can be any list of elements.
 * Once elements are initializes they can not be changed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class EchoNode extends Node {
	
	/** All elements in echo node. */
	private final Element[] elements;

	/**
	 * Initializes echo node with read-only elements.
	 * 
	 * @param elements	elements in echo node.
	 */
	public EchoNode(Element[] elements) {
		if(elements == null) {
			throw new IllegalArgumentException("Elements can not be null.");
		}
		this.elements = elements;
	}
	
	/**
	 * Returns all elements in this echo node.
	 * 
	 * @return elements in echo node.
	 */
	public Element[] getElements() {
		return elements;
	}

	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("{$= ");
		
		for(Element element : elements) {
			sb.append(element.asText()).append(" ");
		}
		
		sb.append("$}");
		
		return sb.toString();
	}
}
