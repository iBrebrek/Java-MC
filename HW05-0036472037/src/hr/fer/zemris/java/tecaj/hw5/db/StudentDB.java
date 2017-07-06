package hr.fer.zemris.java.tecaj.hw5.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.tecaj.hw5.collections.SimpleHashtable;
import hr.fer.zemris.java.tecaj.hw5.db.commands.CommandResult;
import hr.fer.zemris.java.tecaj.hw5.db.commands.DatabaseCommand;
import hr.fer.zemris.java.tecaj.hw5.db.commands.ExitCommand;
import hr.fer.zemris.java.tecaj.hw5.db.commands.IndexQueryCommand;
import hr.fer.zemris.java.tecaj.hw5.db.commands.QueryCommand;

/**
 * This is a simple database emulator.
 * 
 * Database must be in ./database.txt.
 * Allowed commands are: query, indexquery and exit.
 * 
 * Program stops only when user writes exit.
 * 
 * Query allows multiple conditional expression, while indexquery can have only one.
 * 
 * Indexquery can query only jmbag and can use only =.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public class StudentDB {
	
	/** Database used in this program. */
	private static StudentDatabase database;
	
	/** All commands supported by database. */
	private static SimpleHashtable<String, DatabaseCommand> allCommands;
	
	
	//create database and commands
	static {
		try {
			List<String> lines = Files.readAllLines(
				Paths.get("./database.txt"),
				StandardCharsets.UTF_8
			);
			database = new StudentDatabase(lines);
			
		} catch (IOException fileError) {
			System.err.println("Unable to load database.txt");
			System.exit(-1);
		} catch (IllegalArgumentException invalidData) { //if invalid grade, not 4 elements per row, etc..
			System.err.println(invalidData.getMessage());
			System.exit(-1);
		} catch (Exception unknown) { //maybe if security on file or something
			System.err.println("Unable to create database from database.txt.");
			System.exit(-1);
		}
		
		//store all commands
		allCommands = new SimpleHashtable<>();
		allCommands.put(ExitCommand.NAME, new ExitCommand());
		allCommands.put(QueryCommand.NAME, new QueryCommand(database));
		allCommands.put(IndexQueryCommand.NAME, new IndexQueryCommand(database));
	}
	
	/**
	 * Entry point for this program.
	 * 
	 * @param args	not used.
	 */
	public static void main(String[] args) {		
		Scanner reader = new Scanner(System.in);
		
		while(true) {
			try {
				String line = reader.nextLine().trim();
				int firstSpace = line.indexOf(" ");
				String command = firstSpace == -1 ? line : line.substring(0, firstSpace);
				String argument = firstSpace == -1 ? "" : line.substring(firstSpace);
			
				if(!allCommands.containsKey(command)) {
					System.err.println("Unsupported command.");
					continue;
				}
			
				if(allCommands.get(command).execute(argument) == CommandResult.FINISH) {
					break;
				}
				
			} catch(Exception information) {
				System.err.println(information.getMessage());
			}
		}
		
		reader.close();
	}
}
