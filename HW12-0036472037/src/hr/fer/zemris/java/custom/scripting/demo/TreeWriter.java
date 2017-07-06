package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Program takes single argument, path to the file.
 * It will parse given file and reproduce its 
 * (approximate) original form onto standard output.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (31.5.2016.)
 */
public class TreeWriter {
	
	/**
	 * Entry point.
	 * 
	 * @param args		file path.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Program takes only 1 argument: file name.");
			System.exit(-1);
		}
		Path path = Paths.get(args[0]);
		
		if(Files.notExists(path)) {
			System.err.println("Given file doesn't exist.");
			System.exit(-1);
		}
		
		String docBody = null;
		try {
			docBody = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.err.println("Unable to read given file.");
			System.exit(-1);
		}
		
		SmartScriptParser p = null;
		try {
			p = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException exc) {
			System.err.println("Unable to parse given file.");
			System.exit(-1);
		}
		
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

	/**
	 * Class used to "visit" every node starting from document node.
	 * This class does the job of printing to system output.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (31.5.2016.)
	 */
	private static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node);
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print(node);
			iterate(node);
			System.out.print("{$ END $}");
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			System.out.print(node);
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			System.out.print(node);
			iterate(node);
		}

		/**
		 * Calls {@link Node#accept(INodeVisitor)} for
		 * every direct child of given node.
		 * Visitor is {@code this}.
		 * 
		 * @param node		parent node.
		 */
		private void iterate(Node node) {
			for(int i = 0, max = node.numberOfChildren(); i < max; i++) {
				node.getChild(i).accept(this);
			}
		}
	}
}
