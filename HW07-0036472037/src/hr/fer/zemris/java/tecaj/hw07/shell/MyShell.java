package hr.fer.zemris.java.tecaj.hw07.shell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import hr.fer.zemris.java.tecaj.hw07.shell.commands.CatShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.LsShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.ShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.tecaj.hw07.shell.commands.TreeShellCommand;

/**
 * Command-line shell program.
 * To see what commands this shell offers, use command help.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (26.4.2016.)
 */
public class MyShell {
	
	/** Message written when shell is started. */
	private static final String GREETING_MESSAGE = "Welcome to MyShell v 1.0";
	
	/** Map of all shell commands. */
	private static final Map<String, ShellCommand> commands = new HashMap<>();
	static {
		commands.put("exit", new ExitShellCommand());
		commands.put("symbol", new SymbolShellCommand());
		commands.put("help", new HelpShellCommand());
		commands.put("charsets", new CharsetsShellCommand());
		commands.put("cat", new CatShellCommand());
		commands.put("ls", new LsShellCommand());
		commands.put("tree", new TreeShellCommand());
		commands.put("mkdir", new MkdirShellCommand());
		commands.put("copy", new CopyShellCommand());
		commands.put("hexdump", new HexdumpShellCommand());
	}
	
	/**
	 * Entry point.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {
		
		MyEnvironment environment = new MyEnvironment(System.in, System.out, '>', '|', '\\');

		try {
			environment.writeln(GREETING_MESSAGE);
			
			ShellStatus status = ShellStatus.CONTINUE;
			
			while(status != ShellStatus.TERMINATE) {
				environment.write(String.valueOf(environment.getPromptSymbol() + " "));
				
				String input = readInput(environment);
				
				if(input == null) continue;
				
				int index = input.indexOf(' ');
				if(index == -1) index = input.length();
				String commandName = input.substring(0, index).trim();
				String arguments = input.substring(index, input.length()).trim();
	
				ShellCommand command = commands.get(commandName);
				if(command == null) {
					environment.writeln(commandName + " is unsupported command.");
					continue;
				}
				
				status = command.executeCommand(environment, arguments);	
			} 
		} catch (RuntimeException information) {
			System.err.println(information.getMessage());
		} catch (Exception exc) {
			//this will be if some security or whatever
			System.err.println("Shell decided to take a nap.");
		}
		
		try {
			environment.write("bye-bye");
		} catch (IOException ignore) {
		}
		
		environment.close();
	}
	
	/**
	 * Reads whole input as a single line.
	 * 
	 * @param environment	environment of shell.
	 * @return whole user input as a single line.
	 * 
	 * @throws IOException	if unable to read/write in environment.
	 */
	private static String readInput(Environment environment) throws IOException {
		String line = environment.readLine();
		
		if(line.isEmpty()) return null;
	
		//escaping (which makes no sense but was asked for in hw)
		line = line.replace("\\\"", "\"").replace("\\\\", "\\"); // replace \" with " and \\ with \
		
		while(line.charAt(line.length()-1) == environment.getMorelinesSymbol()) {
			environment.write(String.valueOf(environment.getMultilineSymbol() + " "));
			line = line.substring(0, line.length()-1) + environment.readLine();
		}
		return line;
	}

	/**
	 * Environment used in {@code MyShell}.
	 * 
	 * @author Ivica Brebrek
	 * @version 1.0  (26.4.2016.)
	 */
	private static class MyEnvironment implements Environment {
		/** From where to read. */
		private final BufferedReader reader;
		/** Where to write. */
		private final BufferedWriter writer;
		
		/** Prompt symbol. */
		private Character prompt;
		/** Multi-line symbol. */
		private Character multi;
		/** More-lines symbol. */
		private Character more ;

		/**
		 * Initializes this environment.
		 * {@code input} and {@code output} won't be changeable.
		 * Symbols will be changeable.
		 * 
		 * @param input		from where to read.
		 * @param output	where to write.
		 * @param prompt	starting prompt symbol.
		 * @param multi		starting multi-line symbol.
		 * @param more		starting more-lines symbol.
		 */
		private MyEnvironment(InputStream input, OutputStream output,  
					Character prompt, Character multi, Character more) {
			this.prompt = prompt;
			this.multi = multi;
			this.more = more;
			reader = new BufferedReader(new InputStreamReader(input));
			writer = new BufferedWriter(new OutputStreamWriter(output));
		}
		
		/**
		 * Closes environment for further uses.
		 */
		private void close() {
			try {
				reader.close();
			} catch (IOException ignore) {
				
			}
			//they are not in same try-block because if first explodes second won't close
			try {
				writer.close();
			} catch (IOException ignoreAgain) {
				
			}
		}

		@Override
		public String readLine() throws IOException {
			return reader.readLine().trim();
		}

		@Override
		public void write(String text) throws IOException {
			writer.write(text);
			writer.flush();
		}

		@Override
		public void writeln(String text) throws IOException {
			write(text + "\n");
		}

		@Override
		public Iterable<ShellCommand> commands() {
			return commands.values();
		}

		@Override
		public Character getMultilineSymbol() {
			return multi;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multi = symbol;
		}

		@Override
		public Character getPromptSymbol() {
			return prompt;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			prompt = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return more;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			more = symbol;
		}
	}
}
