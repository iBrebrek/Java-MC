package hr.fer.zemris.java.webapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import hr.fer.zemris.java.webapp.dao.DAO;
import hr.fer.zemris.java.webapp.dao.DAOProvider;
import hr.fer.zemris.java.webapp.dao.sql.SQLConnectionProvider;


/**
 * This class is contract on what database with polls will offer.
 * Calling method {@link #prepareTables(DataSource, ServletContext)} 
 * will do all the work that has to be done before database can be used.
 * That method will created needed tables if they do not exists
 * and populate them if they are empty.
 * <br>
 * To change initial poll data see file at {@link #POLLS_PATH}.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public final class PollsContract {
	
	/** Hidden. */
	private PollsContract() {
	}
	
	/**
	 * Path to file where are lists of all polls
	 * that will be added to empty database.
	 */
	private static final String POLLS_PATH = "/WEB-INF/polls.txt";

	/** 
	 * SQL to create table Polls.
	 */
	private static final String CREATE_POLLS = 
			"CREATE TABLE Polls "+
				"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "+
				"title VARCHAR(150) NOT NULL, "+
				"message CLOB(2048) NOT NULL"+
			")";
	
	/** 
	 * SQL to create table PollOptions.
	 */
	private static final String CREATE_OPTIONS = 
			"CREATE TABLE PollOptions "+
				"(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "+
				"optionTitle VARCHAR(100) NOT NULL, "+
				"optionLink VARCHAR(150) NOT NULL, "+
				"pollID BIGINT, "+
				"votesCount BIGINT, "+
				"FOREIGN KEY (pollID) REFERENCES Polls(id)"+
			")";

	/**
	 * Prepares voting database.
	 * If tables didn't exists this method will create them.
	 * If tables are empty this method will fill it with 
	 * initial data from file {@link #POLLS_PATH}.
	 * 
	 * @param dataSource	source needed to connect to database.
	 * @param context		context used to get real file path.
	 * 
	 * @throws SQLException		if unable to create tables.
	 */
	public static void prepareTables(DataSource dataSource, ServletContext context) throws SQLException {		
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Database is not available.", e);
		}
		
		SQLConnectionProvider.setConnection(con);
		
		try {
			createTables();
			populateTables(context);
		} finally {
			SQLConnectionProvider.setConnection(null);
			try { con.close(); } catch(SQLException ignorable) {}
		}
	}

	/**
	 * Crate tables Polls and PollOptions if they do not exist.
	 * 
	 * @throws SQLException if unable to create tables
	 */
	private static void createTables() throws SQLException {
		boolean createPolls = true;
		boolean createOptions = true;

		Connection connection = SQLConnectionProvider.getConnection();
		DatabaseMetaData dbmd = connection.getMetaData();
		
		try (ResultSet rs = dbmd.getTables(null, null, null, new String[] {"TABLE"})) {
			while(rs.next()) {
				switch(rs.getString("TABLE_NAME").toLowerCase()) {
				case "polls": 
					createPolls = false;
					break;
				case "polloptions":
					createOptions = false;
					break;
				}
			}
		}
		try (Statement s = connection.createStatement()){
			if(createPolls) s.executeUpdate(CREATE_POLLS);
			if(createOptions) s.executeUpdate(CREATE_OPTIONS);
		} 
	}
	
	/**
	 * Populates tables if database is considered empty.
	 * 
	 * @param context 		context used to get real file path.
	 */
	private static void populateTables(ServletContext context) {
		DAO db = DAOProvider.getDao();
		if(db.isEmpty()) {
			String pathPolls = context.getRealPath(POLLS_PATH);
			for(List<String> poll : readElements(pathPolls, 3)) {
				long pollID = db.insertPoll(poll.get(0), poll.get(1));
				String pathOptions = context.getRealPath(poll.get(2));
				insertAllOptions(pathOptions, pollID);
			}
		}
	}
	
	/**
	 * From given file path inserts all poll options
	 * and adds them to poll with given id.
	 * Poll option votes are set to 0.
	 * 
	 * @param path		file containing poll options.
	 * @param pollID	poll id to which options belong.
	 */
	private static void insertAllOptions(String path, long pollID) {
		for(List<String> option : readElements(path, 2)) {
			DAOProvider.getDao().insertPollOption(option.get(0), option.get(1), pollID, 0);
		}
	}
	
	/**
	 * From given file retrieves string elements per line.
	 * Elements are split by tab.
	 * If there is line with number of elements 
	 * that is not same as {@code perLine}, that
	 * line is skipped.
	 * 
	 * @param path		file path.
	 * @param perLine 	expected elements per line.
	 * @return list of lines of elements in given file path.
	 */
	private static List<List<String>> readElements(String path, int perLine) {
		List<List<String>> elements = new ArrayList<>();
		try {
			List <String> polls = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
			for(String poll : polls) {
				String[] data = poll.split("\t");
				//just ignore line if it is invalid, because it could be empty line, comment, etc...
				if(data.length != perLine) continue; 
				elements.add(Arrays.asList(data));
			}
		} catch (IOException e) {
			throw new RuntimeException("Unable to read initial data.");
		}
		return elements;
	}	
}
