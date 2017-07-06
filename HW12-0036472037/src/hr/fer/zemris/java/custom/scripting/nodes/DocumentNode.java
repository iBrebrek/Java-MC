package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing an entire document. 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class DocumentNode extends Node {
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//returns empty string because document node are here only to store other non-empty nodes
		return "";
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
