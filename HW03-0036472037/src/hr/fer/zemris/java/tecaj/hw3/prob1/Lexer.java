package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Represents a lexical analysis which creates tokens from given text.
 * Possible token type are represented with TokenType, 
 * while token is represented with class Token.
 * 
 * Lexer will skip all spaces('\r','\n', '\t', ' ').
 * 
 * Escape is supported.
 * Writing escape character before number or symbol will treat it as a letter,
 * therefore a word with numbers and symbols can be created.
 * 
 * Lexer has two possible states, see LexerState for more informations.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.3.2016.)
 */
public class Lexer {
	
	/** Character used for escaping symbols and numbers. */
	private static final char ESCAPE = '\\';
	
	/** Character used in state changing. */
	private static final char STATE_CHANGE = '#';
	
	/** Lexer working state */
	private LexerState state = LexerState.BASIC;
	
	/** Input text. */
	private char[] data;
	
	/** Current token. */
	private Token token;
	
	/** Index of first unprocessed character */
	private int currentIndex;
	
	/**
	 * Initializes lexical analysis that generates token from given input.
	 * 
	 * @param text	input being generated into tokens.
	 */
	public Lexer(String text) {
		if(text == null) {
			throw new IllegalArgumentException("Input text can not be null.");
		}
		data = text.toCharArray();
	}
	
	/**
	 * Generates and returns next token.
	 * Throws LexerException if next token can not be created.
	 * 
	 * @return next token.
	 */
	public Token nextToken() {
		while((currentIndex < data.length) && isSpace()) {
			currentIndex++;
		}
		
		if(token!=null && token.getType() == TokenType.EOF) {
			throw new LexerException("EOF was reached. Can not generate new token.");
		} else if(currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			
		} else if(state == LexerState.BASIC) {
			basicGenerator();
		} else {
			extendedGenerator();
		}
		
		return token;
	}
	
	/**
	 * Generation done in basic state.
	 * 
	 * Generates next token.
	 * Throws LexerException if next token can not be created.
	 */
	private void basicGenerator() {
		if(Character.isDigit(data[currentIndex])) {
			token = processDigits();
			
		} else if(Character.isLetter(data[currentIndex]) || data[currentIndex]==ESCAPE) {
			token = processLetters();
			
		} else {//because it must be symbol if not any of 3 above
			token = new Token(TokenType.SYMBOL, data[currentIndex++]);
		}
	}
	
	/**
	 * Generation done in extended state.
	 * 
	 * Generates next token.
	 * Throws LexerException if next token can not be created.
	 * 
	 */
	private void extendedGenerator() {
		if(data[currentIndex] == STATE_CHANGE) {
			token = new Token(TokenType.SYMBOL, STATE_CHANGE);
			currentIndex++;
			return;
		}
		
		String word = "";
		
		while(currentIndex < data.length && data[currentIndex] != STATE_CHANGE) {
			if(isSpace()) {
				currentIndex++;
				break;
			}
			word += data[currentIndex];
			currentIndex++;
		}
		token = new Token(TokenType.WORD, word);
	}
	
	/**
	 * Returns last generated token.
	 * 
	 * @return last generated token.
	 */
	public Token getToken() {
//		if(token == null) {
//			throw new LexerException("There is no generated token.");
//		}
		return token;
	}

	/**
	 * Changes lexical analysis working state.
	 * Setting state to null with result with IllegalArgumentException.
	 * 
	 * @param state 	lexical analysis working state.
	 */
	public void setState(LexerState state) {
		if(state == null) {
			throw new IllegalArgumentException("State can not be null.");
		}
		this.state = state;
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

	/**
	 * Process full sequence of letters starting from current index.
	 * 
	 * @return new token representing processed digits.
	 */
	private Token processLetters() {
		String word = "";
		
		while(currentIndex < data.length) {
			
			if(data[currentIndex] == ESCAPE) {
				currentIndex++;
				
				if(currentIndex >= data.length) {
					throw new LexerException("Escape character can not be last.");
				}
				if(Character.isLetter(data[currentIndex])) {
					throw new LexerException("Letters can not be escaped.");
				}
				
			} else if(!Character.isLetter(data[currentIndex])) {
				break;
			}
			
			word += data[currentIndex];
			currentIndex++;
		}
		return new Token(TokenType.WORD, word);
	}

	/**
	 * Process full sequence of digits starting from current index.
	 * 
	 * @return new token representing processed digits.
	 */
	private Token processDigits() {
		String number = "";
		
		while(currentIndex < data.length) {
			if(!Character.isDigit(data[currentIndex])) {
				break;
			}
			number += data[currentIndex];
			currentIndex++;
		}
		
		try {
			return new Token(TokenType.NUMBER, Long.parseLong(number));
		} catch(Exception exception) { //maybe will happen if number is too big
			throw new LexerException("Number can not be shown as long.");
		}
	}
}
