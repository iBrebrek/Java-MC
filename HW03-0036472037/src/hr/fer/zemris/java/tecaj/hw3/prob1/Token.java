package hr.fer.zemris.java.tecaj.hw3.prob1;

/**
 * Stores a single token (its type and value).
 * Once creates, token's type and value can not be changed.
 * 
 * EOF is null,
 * WORD is an instance of String (but every char must be letter),
 * NUMBER is an instance of Long,
 * SYMBOL is an instance of Character.
 * 
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.3.2016.)
 */
public class Token {
	
	/** Any type defined in enumeration TokenType, can not be null. */
	private TokenType type;
	
	/** Value of a token. */
	private Object value;

	/**
	 * Initializes new Token.
	 * 
	 * Throws LexerException if type and value are not compatible.
	 * 
	 * @param type	Type of a token.
	 * @param value	Value of a token.
	 */
	public Token(TokenType type, Object value) {
		if(type != TokenType.EOF && value == null) {
			throw new LexerException("Only EOF can have a null value.");
		}
		
		switch (type) {
			case EOF:
				if(value != null) {
					throw new LexerException("EOF can not have a value.");
				}
				break;
				
			case NUMBER:
				if(!(value instanceof Long)) {
					throw new LexerException("NUMBER must be an instance of Long.");
				}
				break;
				
			case SYMBOL:
				if(!(value instanceof Character)) {
					throw new LexerException("SYMBOL must be an instance of Character");
				} 
				Character charValue = (Character) value;
				if(Character.isLetter(charValue) || Character.isSpaceChar(charValue) 
								|| Character.isDigit(charValue)) {
					throw new LexerException("SYMBOL can not be number, letter or space.");
				}
				break;
				
			case WORD:
				if(String.valueOf(value).matches("\\s")) {
					throw new LexerException("WORD can not not have space(s).");
				}
				break;
				
			default:
				throw new LexerException("Token type must be EOF, NUMBER, SYMBOL or WORD.");
		}
		
		this.type = type;
		this.value = value;
	}

	/**
	 * Getter for type of a token.
	 * 
	 * @return token's type.
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Getter for value of a token.
	 * 
	 * @return token's value.
	 */
	public Object getValue() {
		return value;
	}
}
