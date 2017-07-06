package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * The tree command expects a single argument: directory name 
 * and prints a tree (each directory level shifts output two characters to the right).
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.4.2016.)
 */
public class TreeShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		Path dir = Paths.get(CommandsUtility.splitArgument(argument)[0]);
		
		if(!Files.isDirectory(dir)) {
			CommandsUtility.writeln(env, "Argument for ls must be a directory.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.walkFileTree(dir, new FileVisitor<Path>() {
				int level = 0;
				
				private void print(Path p) {
					String spaces = "";
					for(int i = 0; i < level; i++) spaces += " ";
					CommandsUtility.writeln(env, spaces + p.getFileName());
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					print(dir);
					level++;
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					print(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					level--;
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			CommandsUtility.writeln(env, "");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "tree";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Command prints a directory tree.");
		lines.add("Needs one argument- directory name.");
		lines.add("Each directory level shifts output two characters to the right.");
		
		return Collections.unmodifiableList(lines);
	}
}
