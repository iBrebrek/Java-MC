package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Token stores one value.
 * Tokens are generated in smart script lexer, and are used by smart script parser.
 * 
 * Once token is created it can not be changed.
 * It has a value and a type. Types are defined in SSTokenType.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public class SSToken {
	/** Value stored in this token. */
	private final String value;
	
	/** Type of element stored in this token. */
	private final SSTokenType type;
	
	/**
	 * Initializes value and type of a new token.
	 * 
	 * @param value		value stored in this token.
	 * @param type		type of this token/element.
	 */
	public SSToken(String value, SSTokenType type) {
		if(type == null) {
			throw new IllegalArgumentException("Token must have a type.");
		}
		this.value = value;
		this.type = type;
	}
	
	/**
	 * Returns value stored in this token.
	 * 
	 * @return token's value.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Return type of this token.
	 * 
	 * @return token's type.
	 */
	public SSTokenType getType() {
		return type;
	}
}
