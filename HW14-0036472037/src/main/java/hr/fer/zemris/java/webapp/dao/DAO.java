package hr.fer.zemris.java.webapp.dao;

import java.util.List;

import hr.fer.zemris.java.webapp.model.Poll;
import hr.fer.zemris.java.webapp.model.PollOption;

/**
 * Interface used to use database.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public interface DAO {
	
	/**
	 * Checks if database is considered empty.
	 * 
	 * @return	<code>true</code> if database is considered empty.
	 * @throws DAOException		in case of an error.
	 */
	public boolean isEmpty() throws DAOException;
	
	/**
	 * Adds new poll to database and returns it's id.
	 * New poll will have given title and message.
	 * 
	 * @param title			title of new poll.
	 * @param message		message of new poll.
	 * @return id of newly inserted poll.
	 * 
	 * @throws DAOException		if unable to insert.
	 */
	public long insertPoll(String title, String message) throws DAOException;
	
	/**
	 * Adds new poll option to database and returns it's id.
	 * New poll option will have given title, link, pollID and votes.
	 * 
	 * @param title			title of new poll option.
	 * @param link			link of new poll option.
	 * @param pollID 		to which poll this option belong.
	 * @param votesCount 	number of votes of new poll option.
	 * @return id of newly inserted poll option.
	 * 
	 * @throws DAOException		if unable to insert.
	 */
	public long insertPollOption(String title, String link, long pollID, long votesCount) throws DAOException;	
	
	/**
	 * From database retrieves poll with given id.
	 * 
	 * @param id		poll id.
	 * @return poll.
	 * 
	 * @throws DAOException		in case of and error.
	 */
	public Poll getPoll(long id) throws DAOException;
	
	/**
	 * From database retrieves poll option with given id.
	 * 
	 * @param id		poll option id.
	 * @return poll option.
	 * 
	 * @throws DAOException		in case of and error.
	 */
	public PollOption getPollOption(long id) throws DAOException;
	
	/**
	 * From database retrieves list of all polls.
	 * 
	 * @return list of all polls.
	 * 
	 * @throws DAOException		in case of an error.
	 */
	public List<Poll> getAllPolls() throws DAOException;
	
	/**
	 * From database retrieves list of all polls
	 * for single poll.
	 * 
	 * @param pollID 	id of a poll which options are retrieved.
	 * @return list of all poll options.
	 * 
	 * @throws DAOException		in case of an error.
	 */
	public List<PollOption> getPollOptions(long pollID) throws DAOException;
	
	/**
	 * Adds a single vote to given poll option.
	 * 
	 * @param option	option to which vote is being added.
	 * 
	 * @throws DAOException		in case of an error.
	 */
	public void submitVote(PollOption option) throws DAOException;
}