package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Produces hex-output. On the right side only a standard subset of characters
 * is shown; for all other characters a '.' is printed instead.
 * 
 * @author Ivica Brebrek
 * @version 1.0 (27.4.2016.)
 */
public class HexdumpShellCommand implements ShellCommand {

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		File file = new File(CommandsUtility.splitArgument(argument)[0]);

		if (!file.isFile()) {
			CommandsUtility.writeln(env, "Hexdump requires file as an argument.");
			return ShellStatus.CONTINUE;
		}

		try (FileInputStream reader = new FileInputStream(file)) {

			final int BYTES_PER_LINE = 16;
			final int HALF = 8;
			int counter = 0;
			int value;
			int left = 0;
			StringBuilder middle = new StringBuilder();
			StringBuilder right = new StringBuilder();

			while ((value = reader.read()) != -1) {

				middle.append(String.format("%02X ", value));

				if (value < 32 || value > 127) {
					right.append('.');
				} else {
					right.append((char)value);
				}

				counter++;

				if (counter == HALF) {
					middle.append('|');
				}
				if (counter == BYTES_PER_LINE) {
					middle.append("| ");
					
					String result = String.format("%010X", left) 
							+ ": " + middle.toString() + right.toString();
					CommandsUtility.writeln(env, result);
					
					left += BYTES_PER_LINE;
					middle = new StringBuilder();
					right = new StringBuilder();
					counter = 0;
				}
			}

			//if whole line is not filled
			if (counter != 0) {

				for (int start = counter; counter < 16; counter++) {
					if (counter == HALF && start != HALF) {
						middle.append('|');
					}
					middle.append("   "); //3 spaces
				}
				middle.append("| ");
				
				String result = String.format("%010X", left)   
						+ ": " + middle.toString() + right.toString();
				CommandsUtility.writeln(env, result);
			}

		} catch (IOException e) {
			CommandsUtility.writeln(env, "Unable to finish hexdump command.");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "hexdump";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> lines = new LinkedList<>();

		lines.add("Produces hex-output.");
		lines.add("On the right side only a standard subset of characters is shown,");
		lines.add("for all other characters a '.' is printed instead.");

		return Collections.unmodifiableList(lines);
	}
}
