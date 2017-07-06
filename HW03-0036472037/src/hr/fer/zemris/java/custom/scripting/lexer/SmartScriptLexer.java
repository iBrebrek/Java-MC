package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Lexical analysis used in smart script parser.
 * 
 * This lexer has two states: TEXT and TAG.
 * In TEXT state lexer reads everything till start of tags.
 * In TAG state lexer reads words by word, or number by number, or symbol by symbol.
 * 
 * States can be changed with setState.
 * 
 * Once lexer reads something it produces a SSToken.
 * That token is returned with nextToken (nextToken also produces tokens).
 * Last produced token(without producing new one) can be accessed with getToken.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class SmartScriptLexer {
	
	/** Character used for escaping. */
	private static final char ESCAPE = '\\';

	/** Lexer working state, starting state is outside of tags. */
	private SSLexerState state = SSLexerState.TEXT;
	
	/** Input text. */
	private char[] data;
	
	/** Current token. */
	private SSToken token;
	
	/** Index of first unprocessed character */
	private int currentIndex;
	
	/**
	 * Initializes lexical analysis that generates token from given input.
	 * 
	 * @param data	input being generated into tokens.
	 */
	public SmartScriptLexer(String data) {
		if(data == null) {
			throw new IllegalArgumentException("Data can not be null.");
		}
		this.data = data.toCharArray();
	}
	
	/**
	 * Generates and returns next token.
	 * Throws LexerException if next token can not be created.
	 * 
	 * @return next token.
	 */
	public SSToken nextToken() {
		if(token!=null && token.getType() == SSTokenType.EOF) {
			throw new SSLexerException("EOF was reached. Can not generate new token.");
			
		} else if(currentIndex >= data.length) {
			token = new SSToken(null, SSTokenType.EOF);
			
		} else if(state == SSLexerState.TEXT) {
			readText();
		} else {
			readTags();
		}
		
		return token;
	}
	
	/**
	 * Changes lexical analysis working state.
	 * Setting state to null with result with IllegalArgumentException.
	 * 
	 * @param state 	lexical analysis working state.
	 */
	public void setState(SSLexerState state) {
		if(state == null) {
			throw new IllegalArgumentException("State can not be null.");
		}
		this.state = state;
	}
	
	/**
	 * Returns this lexical analysis current working state.
	 * 
	 * @return current working state.
	 */
	public SSLexerState getState() {
		return state;
	}
	
	/**
	 * Returns last generated token.
	 * 
	 * @return last generated token.
	 */
	public SSToken getToken() {
		return token;
	}
	
	/**
	 * Working state outside of tags.
	 * Can read only text and start of tags.
	 */
	private void readText() {
		if(isStartOfTags()) {
			token = new SSToken(null, SSTokenType.TAG_START);
			currentIndex += 2;
			return;
		}
		
		String text = "";
		
		while(currentIndex < data.length && !isStartOfTags()) {
			
			if(data[currentIndex] == ESCAPE) {
				currentIndex++;
				
				if(currentIndex == data.length 
						|| (data[currentIndex] != '\\' && data[currentIndex] != '{')) {
					throw new SSLexerException("Invalid usage of escape in text.");
				}
			}
			
			text += data[currentIndex];
			currentIndex++;
		}
		
		token = new SSToken(text, SSTokenType.TEXT);
	}
	
	/**
	 * Working state in tags.
	 * Can read numbers, strings, variables, functions, key words, symbols, end of tag.
	 */
	private void readTags() {
		//skip spaces
		while((currentIndex < data.length) && isSpace()) {
			currentIndex++;
		}
				
		if(isEndOfTags()) {
			token = new SSToken(null, SSTokenType.TAG_END);
			currentIndex += 2;
			return;
		}
		
		if(Character.isDigit(data[currentIndex])) {
			readNumber();
			return;
		} 
		
		if(Character.isLetter(data[currentIndex])) {
			//NOTE: tag names FOR and END will be here
			token = new SSToken(readTillNonWord(), SSTokenType.VARIABLE);
			return;
		}
		
		switch(data[currentIndex]) {
			case '\"':
				currentIndex++; //to skip "
				readString();
				break;
				
			case '@':
				currentIndex++; //to skip @
				token = new SSToken(readTillNonWord(), SSTokenType.FUNCTION);
				break;
				
			case '-':
				if(currentIndex+1 < data.length && Character.isDigit(data[currentIndex+1])) {
					readNumber();
					break;
				}
				//if '-' and no digits after it will fall to default
			default: 
				//because if nothing else it must be a symbol
				//NOTE: tag name = will be here
				token = new SSToken(String.valueOf(data[currentIndex]), SSTokenType.SYMBOL);
				currentIndex++;
		}
		
	}
	
	/**
	 *	Reads next number and changes token to that number.
	 *	Can produce decimal and integer, positive and negative numbers.
	 */
	private void readNumber() {
		String number = "";
		
		if(data[currentIndex] == '-') {
				number = "-";
				currentIndex++;
		}
		
		while(currentIndex < data.length 
				&& (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
			number += data[currentIndex];
			currentIndex++;
		}
		
		if(number.contains(".")) {
			token = new SSToken(number, SSTokenType.NUMBER_DECIMAL);
		} else {
			token = new SSToken(number, SSTokenType.NUMBER_INTEGER);
		}
	}
	
	/**
	 * Reads everything starting from current index
	 * till char that isn't letter, number or underscore.
	 * 
	 * @return read string.
	 */
	private String readTillNonWord() { //This is used for reading variables and functions.
		String name = "";
		
		while(currentIndex < data.length 
				&& (Character.isLetter(data[currentIndex])
						|| Character.isDigit(data[currentIndex])
						|| data[currentIndex] == '_')) {
			
			name += data[currentIndex];
			currentIndex++;
		}
		
		return name;
	}

	/**
	 * Read from " to ".
	 * Changes token to string.
	 */
	private void readString() {
		String string = "";
		
		while(currentIndex < data.length && data[currentIndex] != '\"') {
			
			if(data[currentIndex] == ESCAPE) {
				currentIndex++;
				
				if(currentIndex != data.length) {
					switch(data[currentIndex]) { //cases are all valid escapes
						case '\\':
							break;
						case '"':
							break;
						case 'n':
							data[currentIndex] = '\n';
							break;
						case 'r':
							data[currentIndex] = '\r';
							break;
						case 't':
							data[currentIndex] = '\t';
							break;
						default:
							throw new SSLexerException("Invalid usage of escape in string.");	
					}
				} else {
					//currentIndex == data.length -> data[currentIndex] would be out of bounds
					throw new SSLexerException("Invalid expression.");
				}
			}
			
			//only this is done if escape is not used
			string += data[currentIndex];
			currentIndex++;
		}
		
		if(currentIndex == data.length) {
			// This means that while loop was finish because we got to the end of array,
			// therefore, last character was not ", which means that string was not closed
			throw new SSLexerException("End of file reached but string was not closed.");
		} else {
			currentIndex++; //skips ending "
		}
		
		token = new SSToken(string, SSTokenType.STRING);
	}
	
	/**
	 * Checks if next two characters represents start of tag.
	 * 
	 * @return true if start of tag is next.
	 */
	private boolean isStartOfTags() {
		return data[currentIndex] == '{' 
				&& currentIndex+1<data.length //because index out of bound can happen in next one
				&& data[currentIndex+1] == '$';
	}
	
	/**
	 * Checks if next two characters represents end of tag.
	 * 
	 * @return true if end of tag is next.
	 */
	private boolean isEndOfTags() {
		return data[currentIndex] == '$' 
				&& currentIndex+1<data.length //because index out of bound can happen in next one
				&& data[currentIndex+1] == '}';
	}
	
	/**
	 * Checks if current index points to space character.
	 * (' ', '\n', '\r', '\t').
	 * 
	 * @return true if current character is space.
	 */
	private boolean isSpace() {
		return(data[currentIndex]==' ' || data[currentIndex]=='\n' 
				|| data[currentIndex]=='\r' || data[currentIndex]=='\t');
	}
}
