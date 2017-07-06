package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * This interface defines methods 
 * that are used once object that 
 * is an instance of specific
 * node class is visited.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (31.5.2016.)
 */
public interface INodeVisitor {
	
	/**
	 * What will be done once instance 
	 * of {@link TextNode} is visited.
	 * 
	 * @param node		{@link TextNode} being visited.
	 */
	void visitTextNode(TextNode node);
	
	/**
	 * What will be done once instance 
	 * of {@link ForLoopNode} is visited.
	 * 
	 * @param node		{@link ForLoopNode} being visited.
	 */
	void visitForLoopNode(ForLoopNode node);
	
	/**
	 * What will be done once instance 
	 * of {@link EchoNode} is visited.
	 * 
	 * @param node		{@link EchoNode} being visited.
	 */
	void visitEchoNode(EchoNode node);
	
	/**
	 * What will be done once instance 
	 * of {@link DocumentNode} is visited.
	 * 
	 * @param node		{@link DocumentNode} being visited.
	 */
	void visitDocumentNode(DocumentNode node);
}
