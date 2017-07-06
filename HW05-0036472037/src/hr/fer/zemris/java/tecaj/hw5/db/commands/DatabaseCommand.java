package hr.fer.zemris.java.tecaj.hw5.db.commands;

import java.util.List;

import hr.fer.zemris.java.tecaj.hw5.db.StudentDatabase;
import hr.fer.zemris.java.tecaj.hw5.db.StudentRecord;

/**
 * Abstraction of all database commands.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (10.4.2016.)
 */
public abstract class DatabaseCommand {
		
	/** Database on which command is used. */
	protected StudentDatabase database;

	/** Name of this command. */
	private final String commandName;
	
	/**
	 * Initializes command's argument and database.
	 * 
	 * @param commandName 	name of this command.
	 * @param database		database on which command is used.
	 */
	public DatabaseCommand(String commandName, StudentDatabase database) {
		this.commandName = commandName;
		this.database = database;
	}
	
	/**
	 * Name of a command.
	 * This name is also used to call the command.
	 * 
	 * @return command name.
	 */
	public String getName() {
		return commandName;
	}
	
	/**
	 * Executes the command.
	 * 
	 * @param argument 	command's argument.
	 * @return result of command.
	 */
	public abstract CommandResult execute(String argument);
	
	/**
	 * Draws database table.
	 * 
	 * @param table		database table.
	 */
	protected void drawTable(List<StudentRecord> table) {
		if(table.isEmpty() || table.contains(null)) {
			System.out.println("Records selected: 0");
			return;
		}
		
		//on following OptionalInts gained from max() we can use getAsInt because we already checked if table is empty
		
		int spaceJMBAG = table.stream().mapToInt(s->s.getJmbag().length()).max().getAsInt();
		int spaceFirstName = table.stream().mapToInt(s->s.getFirstName().length()).max().getAsInt();
		int spaceLastName = table.stream().mapToInt(s->s.getLastName().length()).max().getAsInt();
		int spaceGrade = 1;
		final int numberOfSpaces = 2; //total min number of extra spaces before and after value in drawn table
		
		drawBigLine(spaceJMBAG + numberOfSpaces, spaceLastName + numberOfSpaces, 
					spaceFirstName + numberOfSpaces, spaceGrade + numberOfSpaces);
		
		for(StudentRecord student : table) {
			System.out.format("| %s | %s | %s | %s |\n", 
								addSpaces(student.getJmbag(), spaceJMBAG),
								addSpaces(student.getLastName(), spaceLastName),
								addSpaces(student.getFirstName(), spaceFirstName),
								student.getFinalGrade()
			);
		}
		
		drawBigLine(spaceJMBAG + numberOfSpaces, spaceLastName + numberOfSpaces, 
					spaceFirstName + numberOfSpaces, spaceGrade + numberOfSpaces);
		
		System.out.println("Records selected: " + table.size());
	}

	/**
	 * If needed adds spaces to given string.
	 * 
	 * @param value			adds spaces to this.
	 * @param totalSize		how long newly created string will be long.
	 * @return resizes {@code value}.
	 */
	private String addSpaces(String value, int totalSize) {
		int toAdd = totalSize - value.length();
		
		for(int i = 0; i < toAdd; i++) {
			value += " ";
		}
		
		return value;
	}

	/**
	 * Draws big line, used as upper and lower border lines.
	 * Looks like this: +===+===+==+===+
	 * 
	 * @param first		number of "=" between first and second "+".
	 * @param second	number of "=" between second and third "+".
	 * @param third		number of "=" between third and fourth "+".
	 * @param fourth	number of "=" between fourth and fifth "+".
	 */
	private void drawBigLine(int first, int second, int third, int fourth) {
		System.out.print("+");
		drawDoubleLine(first);
		drawDoubleLine(second);
		drawDoubleLine(third);
		drawDoubleLine(fourth);
		System.out.println();
	}

	/**
	 * Draws double line.
	 * looks like this: ====+
	 * 
	 * @param total		number of "=" in double line.
	 */
	private void drawDoubleLine(int total) {
		for(int i = 0; i < total; i++) {
			System.out.print("=");
		}
		System.out.print("+");
	}
}
