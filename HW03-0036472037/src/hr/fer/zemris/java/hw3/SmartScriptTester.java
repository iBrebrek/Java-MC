package hr.fer.zemris.java.hw3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;

/**
 * Tester for smart script parser (and lexer).
 * 
 * Program needs one argument- path to the file.
 * Content from that file will be parsed and written on the system.out.
 * 
 * Test works like this:
 * if structural tree on the system.out is as in the file then parser passed the test.
 * 
 * Also, produced string (which is also written on system.out) is being parsed again.
 * Then first parsed string and second parsed string are compared. They should be the same.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (28.3.2016.)
 */
public class SmartScriptTester {

	/**
	 * Entry point for this program.
	 * 
	 * @param args path to a file.
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Argument must be a path to the file. "
					+ "If path has spaces, put it in quotation marks.");
			return;
		}
		
		String docBody;
		
		try {
			docBody = new String(
					Files.readAllBytes(Paths.get(args[0])),
					StandardCharsets.UTF_8
			);
		} catch (IOException e1) {
			System.err.println("Could not load the file.");
			return;
		}
		
		SmartScriptParser parser = null;
		try {
			parser = new SmartScriptParser(docBody);
		} catch (SmartScriptParserException e) {
			System.err.println("Unable to parse document! \n" + e.getMessage());
			System.exit(-1);
		} catch (Exception e) {
			System.err.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println(originalDocumentBody); // should write something like
													// original
													// content of docBody

		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String originalDocumentBody2 = createOriginalDocumentBody(document2);
		System.out.println("\n\n document and document2 give the same parser result: " + 
				originalDocumentBody.equals(originalDocumentBody2));
		// now document and document2 should be structurally identical trees
	}

	/**
	 * String is equal to combination of given node and its children.
	 * 
	 * Giving document node to this method will result with such a string
	 * that if you parse it result will be equal to given document node.
	 * 
	 * NOTE: giving any node that is not DocumentNode will obviously 
	 * not result with string with same structure as document. 
	 * 
	 * @param node	DocumentNode from which is string created.
	 * @return new string will same structural tree as document.
	 */
	private static String createOriginalDocumentBody(Node node) {
		StringBuilder sb = new StringBuilder(node.toString());
		
		for(int total = node.numberOfChildren(), index = 0; index < total; index++) {
			sb.append(createOriginalDocumentBody(node.getChild(index)));
		}
		
		if(node instanceof ForLoopNode) {
			sb.append("{$ END $}");
		}
		
		return sb.toString();
	}
}
