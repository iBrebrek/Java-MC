package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.IOException;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;

/**
 * Abstraction of every environment that {@code ShellCommand}s will use.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (25.4.2016.)
 */
public interface Environment {
	
	/**
	 * Reads a single line.
	 * 
	 * @return read line.
	 * 
	 * @throws IOException if unable to read line.
	 */
	String readLine() throws IOException;
	
	/**
	 * Writes given text.
	 * 
	 * @param text 	text being written.
	 * 
	 * @throws IOException if unable to write.
	 */
	void write(String text) throws IOException;
	
	/**
	 * Writes given text and then terminates the line.
	 * 
	 * @param text	text being written.
	 * 
	 * @throws IOException if unable to write.
	 */
	void writeln(String text) throws IOException;
	
	/**
	 * All shell command available in this environment.
	 * 
	 * @return all commands.
	 */
	Iterable<ShellCommand> commands();
	
	/**
	 * Returns character used as a beginning of a multi-line command.
	 * Multi-line symbol is used at the beginning of a line.
	 * 
	 * @return multi-line symbol.
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets character used as a beginning of a multi-line command.
	 * Multi-line symbol is used at the beginning of a line.
	 * 
	 * @param symbol 	multi-line symbol.
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns character used as a prompt symbol.
	 * Prompt symbol indicates that user input is expected.
	 * 
	 * @return prompt symbol.
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets character used as a prompt symbol.
	 * Prompt symbol indicates that user input is expected.
	 * 
	 * @param symbol 	prompt symbol.
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns character used to inform shell that more lines as expected.
	 * More lines symbol is used at the end of line if command is not whole.
	 * 
	 * @return prompt symbol.
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets character used to inform shell that more lines as expected.
	 * More lines symbol is used at the end of line if command is not whole.
	 * 
	 * @param symbol 	more-lines symbol
	 */
	void setMorelinesSymbol(Character symbol);

}
