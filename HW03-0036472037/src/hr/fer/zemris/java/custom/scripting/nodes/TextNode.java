package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * A node representing a piece of textual data.
 * 
 * Text enables escaping with \, only \ and { can be escaped.
 * 
 * Constructor expects string with already finished escaping,
 * but toString will write \ in front of every escaped char. 
 * 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class TextNode extends Node {
	
	/** Read-only text in this node. */
	private final String text;
	
	/**
	 * Initializes this node's read-only text.
	 * Text can not be null.
	 * 
	 * @param text	this node's text.
	 */
	public TextNode(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Text can not be null.");
		}
		this.text = text;
	}
	
	/**
	 * Returns node's text.
	 * 
	 * @return node's text.
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// replace all \ with \\ so parsing returned string gets same result.
		return text.replace("\\", "\\\\").replace("{", "\\{"); 
	}
}
