package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * States in which smart script lexer can be.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.3.2016.)
 */
public enum SSLexerState {
	/** Outside of tags, everything is text until defined symbols. */
	TEXT,
	/** Inside tags, tags are between defined symbols. */
	TAG
}
