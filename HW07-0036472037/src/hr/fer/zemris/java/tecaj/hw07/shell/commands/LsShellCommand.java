package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Takes a single argument – directory – and writes a directory listing.
 * The output consists of 4 columns. 
 * First column indicates if current object is directory (d), readable (r),writable (w) and executable (x). 
 * Second column contains object size in bytes that is right aligned and occupies 10 characters. 
 * Follows file creation date/time and finally file name.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (27.4.2016.)
 */
public class LsShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		File dir = new File(CommandsUtility.splitArgument(argument)[0]);
		
		if(!dir.isDirectory()) {
			CommandsUtility.writeln(env, "Argument for ls must be a directory.");
			return ShellStatus.CONTINUE;
		}
		
		for(File file : dir.listFiles()) {
			StringJoiner sj = new StringJoiner(" ");
			sj.add(firstColumn(file));
			sj.add(String.format("%10s", file.length()));
			sj.add(thirdColumn(file));
			sj.add(file.getName());
			CommandsUtility.writeln(env, sj.toString());
		}
		
		return ShellStatus.CONTINUE;
	}	

	/**
	 * Text for first column.
	 * directory/readable/writable/executable
	 * 
	 * @param file		current file
	 * @return info
	 */
	private String firstColumn(File file) {
		StringBuilder sb = new StringBuilder();
		sb.append(file.isDirectory() ? "d" : "-");
		sb.append(file.canRead() ? "r" : "-");
		sb.append(file.canWrite() ? "w" : "-");
		sb.append(file.canExecute() ? "x" : "-");
		return sb.toString();
	}
	
	/**
	 * Text for third column.
	 * Creation time and date.
	 * 
	 * @param file		current file.
	 * @return file creation time and date.
	 */
	private String thirdColumn(File file) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Path path = file.toPath();
			BasicFileAttributeView faView = Files.getFileAttributeView(
				path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
			);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			return formattedDateTime;
		
		} catch(IOException exc) {
			return "unable to read it";
		}
	}

	@Override
	public String getCommandName() {
		return "ls";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();
		
		lines.add("Takes a single argument – directory – and writes a directory listing.");
		lines.add("First column: directory/readable/writable/executable.");
		lines.add("Second column: size in bytes.");
		lines.add("Third column: file creation date and time.");
		lines.add("Fourth column: file name.");
		
		return Collections.unmodifiableList(lines);
	}
}
