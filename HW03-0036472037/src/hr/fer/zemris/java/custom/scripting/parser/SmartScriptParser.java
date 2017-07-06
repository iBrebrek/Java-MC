package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.lexer.SSLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.SSToken;
import hr.fer.zemris.java.custom.scripting.lexer.SSTokenType;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;

/**
 * The parser have a single constructor which accepts a string that contains document body.
 * 
 * If any exception occurs during parsing, parser will catch it 
 * and re-throw an instance of SmartScriptParserException.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (28.3.2016.)
 */
public class SmartScriptParser {
	
	/** Tag name for for loop. */
	private static final String FOR = "FOR";
	
	/** Tag name for closing "loops". */
	private static final String END = "END";
	
	/** Tag name for echo. */
	private static final String ECHO = "=";

	/** Lexer used for lexical analysis. */
	private SmartScriptLexer lexer;
	
	/** Stack used for implementation of tree, document node will always be last. */ 
	private ObjectStack nodeStack = new ObjectStack();
	
	/** This node is at the top of node stack */
	private Node currentNode; //this is used instead of stack.peek so we don't need to cast everytime
	
	/**
	 * Constructor for this parser. 
	 * Initializes document and starts parsing.
	 * 
	 * @param data 	string that contains document body
	 */
	public SmartScriptParser(String data) {

		try {
			lexer = new SmartScriptLexer(data);
			startParsing();
			
		} catch(Exception exception) {
			throw new SmartScriptParserException(exception.getMessage());
		}
	}
	
	/**
	 * Returns document node containing all other nodes.
	 * 
	 * @return document node generated from this parser.
	 */
	public DocumentNode getDocumentNode() {
		//this will always be a document node because startParsing would throw exception otherwise
		return (DocumentNode)currentNode; 
	}
	
	/**
	 * Method used to start parsing tokens from lexer.
	 * Method stops with EOF token or exception.
	 */
	private void startParsing() {
		currentNode = new DocumentNode();
		nodeStack.push(currentNode);
		
		while(lexer.nextToken().getType() != SSTokenType.EOF) {
			SSToken currentToken = lexer.getToken();
			
			//once tag name is read whole tag will be processed
			switch (currentToken.getType()) {
				case TEXT:
					TextNode textNode = new TextNode(currentToken.getValue());
					currentNode.addChildNode(textNode);
					break;
					
				case TAG_START:
					lexer.setState(SSLexerState.TAG);
					break;
					
				case VARIABLE: //does same thing as a symbol case
				case SYMBOL: //this can happen only at the beginning of tags
					delegateTagName(currentToken.getValue());
					break;
					
				default:
					throw new SmartScriptParserException("Invalid expression.");
			}
		}
		//at this point EOF was reached
		if(nodeStack.size() !=1) {
			//only document node should be on stack
			throw new SmartScriptParserException
						("EOF was reached but not every for loop was closed.");
		}
		
		if(lexer.getState() == SSLexerState.TAG) {
			throw new SmartScriptParserException("Text should not end with unclosed tag.");
		}
	}
	
	/**
	 * Checks if tag name is correct and if so does work based on tags name.
	 * 
	 * @param tagName	string being validated for right tag.
	 */
	private void delegateTagName(String tagName) {
		switch(tagName.toUpperCase()) {
			case FOR:
				createForLoop();
				break;
				
			case ECHO:
				createEchoNode();
				break;
			
			case END:
				closeLoop();
				break;
			
			default:
				throw new SmartScriptParserException("Invalid tag name.");
		}
	}

	/**
	 * Creates a new for loop node, 
	 * adds it as a child to the current node at the top of stack,
	 * and then pushes it to the top of stack.
	 * 
	 * Also checks if for loop is used correctly.
	 */
	private void createForLoop() {
		ElementVariable variable = new ElementVariable(lexer.nextToken().getValue());
		Element start = createElementFromToken(lexer.nextToken());
		Element end = createElementFromToken(lexer.nextToken());
		Element step = lexer.nextToken().getType() == SSTokenType.TAG_END ?
							null : createElementFromToken(lexer.getToken());
		
		ForLoopNode forNode = new ForLoopNode(variable, start, end, step);
		
		currentNode.addChildNode(forNode);
		currentNode = forNode;
		nodeStack.push(forNode);
		
		//now there must be an end of for tag (because FOR can have only 3 or 4 args)
		//also, end of tags might have already been read if step is null
		if(lexer.getToken().getType() == SSTokenType.TAG_END) { //will be true if step is null
			lexer.setState(SSLexerState.TEXT);
			return; //this is OK
		}
		if(lexer.nextToken().getType() == SSTokenType.TAG_END) { //check in case step wasn't null
			lexer.setState(SSLexerState.TEXT);
			return; //this is OK too
		} else {
			//this is not OK
			throw new SmartScriptParserException("Incorrect usage of FOR tag.");
		}
	}
	
	/**
	 * Creates a new echo node and 
	 * adds it as a child to the current node at the top of stack.
	 * 
	 * Also checks if tag is used correctly.
	 */
	private void createEchoNode() {
		ArrayIndexedCollection collectionOfElements = new ArrayIndexedCollection();
		
		while(lexer.nextToken().getType() != SSTokenType.TAG_END) {
			collectionOfElements.add(createElementFromToken(lexer.getToken()));
		}
		
		lexer.setState(SSLexerState.TEXT); //because tag is over
		
		currentNode.addChildNode(new EchoNode(collectionToElements(collectionOfElements)));
	}
	
	/**
	 * Closes a loop.
	 * In other words: removes one node from the stack.
	 */
	private void closeLoop() {
		if(lexer.nextToken().getType() != SSTokenType.TAG_END) {
			throw new SmartScriptParserException("Invalid usage of END tag.");
		}
		lexer.setState(SSLexerState.TEXT);
		
		nodeStack.pop();
		if(nodeStack.isEmpty()) {
			throw new SmartScriptParserException
				("There is too many {$ END $} tags.");
		}
		currentNode = (Node)nodeStack.peek();
	}
	
	/**
	 * "Transforms" collection to Element[].
	 * 
	 * @param collectionOfElements	collection being transformed.
	 * @return new array of elements.
	 */
	private Element[] collectionToElements(ArrayIndexedCollection collectionOfElements) {
		/*
		 * Had to create this because I was not able to cast Object[] to Element[]
		 */
		
		int size = collectionOfElements.size();
		Element[] elements = new Element[size];
		
		for(int index = 0; index < size; index++) {
			elements[index] = (Element)collectionOfElements.get(index);
		}
		
		return elements;
	}

	/**
	 * From given token creates element based on token's type.
	 * 
	 * @param token		token being transformed into element.
	 * @return new element.
	 */
	private Element createElementFromToken(SSToken token) {
		switch(token.getType()) {
			case STRING:
				return new ElementString(token.getValue());
				
			case NUMBER_DECIMAL:
				return new ElementConstantDouble(token.getValue());
				
			case NUMBER_INTEGER:
				return new ElementConstantInteger(token.getValue());
				
			case SYMBOL:
				return new ElementOperator(token.getValue());
				
			case FUNCTION:
				return new ElementFunction(token.getValue());
				
			case VARIABLE:
				return new ElementVariable(token.getValue());
				
			default:
				throw new SmartScriptParserException("Expression is not correct.");
		}
	}
}
