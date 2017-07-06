package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Command expects two arguments: source file name and destination file name.
 * If destination file exists, user is asked to overwrite it.
 * First argument can not be directory.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.4.2016.)
 */
public class CopyShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] args = CommandsUtility.splitArgument(arguments);
		
		if(args.length != 2) {
			CommandsUtility.writeln(env, "Wrong number of arguments.");
			return ShellStatus.CONTINUE;
		}
		
		File source = new File(args[0]);
		File destination = new File(args[1]);
		
		if(!source.isFile()) {
			CommandsUtility.writeln(env, "First argument must be an existing file.");
			return ShellStatus.CONTINUE;
		}
		if(source.equals(destination)) {
			CommandsUtility.writeln(env, "That is the same file, no need to copy.");
			return ShellStatus.CONTINUE;
		}
		
		if(!destination.getParentFile().isDirectory()) {
			CommandsUtility.writeln(env, "Destination directory tree does not exists.");
			CommandsUtility.writeln(env, "First use command mkdir to create those directories.");
			return ShellStatus.CONTINUE;
		}
		
		if(destination.isFile() && destination.exists()) {
			if(!askToOverwrite(env)) {
				CommandsUtility.writeln(env, "Copying canceled.");
				return ShellStatus.CONTINUE;
			}
		}
		
		if(destination.isDirectory()) {
			destination = new File(destination, source.getName());
		}
		
		try (FileInputStream r = new FileInputStream(source);
			FileOutputStream w = new FileOutputStream(destination)){        
			
			byte[] buffer = new byte[1024];
			int x;
			
			while((x = r.read(buffer)) > 0) {
				w.write(buffer, 0, x);
			}
			w.flush();
			
			CommandsUtility.writeln(env, "Copying successful.");
			
		} catch (IOException exc) {
			CommandsUtility.writeln(env, "Copying failed.");
		}
		
		return ShellStatus.CONTINUE;
	}
	
	/**
	 * Asks user to overwrite file.
	 * If user accept return true.
	 * 
	 * @param env		environment to communicate with user.
	 * @return {@code true} if user accepts to overwrite.
	 */
	private boolean askToOverwrite(Environment env) {
		try {
			CommandsUtility.writeln(env, "Destination file already exists.");
			String answer = "";
			while(!answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
				CommandsUtility.writeln(env, "Overwrite file? [Y/N]");
				answer = env.readLine();
			}
			return answer.equalsIgnoreCase("Y");
		} catch(IOException exc) {
			throw new RuntimeException("Unable to read/write in environment.");
		}
	}

	@Override
	public String getCommandName() {
		return "copy";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Command expects two arguments: source file name and destination file name.");
		lines.add("If destination file exists, user is asked to overwrite it.");
		lines.add("First argument can not be directory.");
		
		return Collections.unmodifiableList(lines);
	}
}
