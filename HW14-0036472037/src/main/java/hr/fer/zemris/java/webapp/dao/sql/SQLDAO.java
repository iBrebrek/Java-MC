package hr.fer.zemris.java.webapp.dao.sql;

import hr.fer.zemris.java.webapp.dao.DAO;
import hr.fer.zemris.java.webapp.dao.DAOException;
import hr.fer.zemris.java.webapp.model.Poll;
import hr.fer.zemris.java.webapp.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of {@link DAO} using SQL.
 * This implementation uses {@link SQLConnectionProvider#getConnection()}
 * to connect to database, therefore, before using any method
 * connection should be set and after connection
 * should be closed.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (14.6.2016.)
 */
public class SQLDAO implements DAO {
	
	@Override
	public boolean isEmpty() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT * FROM Polls");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs != null && rs.next()) {
						return false;
					} else {
						return true;
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while reading Polls.", ex);
		}
	}

	@Override
	public long insertPoll(String title, String message) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
				"INSERT INTO Polls (title, message) values (?,?)",
				Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, title);
			pst.setString(2, message);
			try {
				pst.executeUpdate();
				ResultSet rset = pst.getGeneratedKeys();
				try {
					if(rset != null && rset.next()) {
						return rset.getLong(1);
					} else {
						return -1;
					}
				} finally {
					rset.close();
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while inserting poll.", ex);
		}
	}
	
	@Override
	public long insertPollOption(String title, String link, 
			long pollID, long votesCount) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
				"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, title);
			pst.setString(2, link);
			pst.setLong(3, pollID);
			pst.setLong(4, votesCount);
			try {
				pst.executeUpdate();
				ResultSet rset = pst.getGeneratedKeys();
				try {
					if(rset != null && rset.next()) {
						return rset.getLong(1);
					} else {
						return -1;
					}
				} finally {
					rset.close();
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while inserting poll option.", ex);
		}
	}

	@Override
	public List<Poll> getAllPolls() throws DAOException {
		List<Poll> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"select id, title, message "+
					"from Polls "+
					"order by title");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						polls.add(new Poll(
							rs.getLong(1),
							rs.getString(2),
							rs.getString(3)
						));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while retrieving polls.", ex);
		}
		return polls;
	}

	@Override
	public List<PollOption> getPollOptions(long pollID) {
		List<PollOption> options = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
				"select id, optionTitle, optionLink, votesCount "+
				"from PollOptions "+
				"where pollID=?"+
				"order by optionTitle");
			pst.setLong(1, pollID);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						options.add(new PollOption(
							rs.getLong(1),
							rs.getString(2),
							rs.getString(3),
							rs.getLong(4)
						));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while retrieving poll options.", ex);
		}
		return options;
	}

	@Override
	public void submitVote(PollOption option) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
				"UPDATE PollOptions set votesCount=? WHERE id=?");
			pst.setLong(1, option.getVotesCount()+1);
			pst.setLong(2, option.getId());
			try {
				pst.executeUpdate();
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while submitting vote.", ex);
		}
	}

	@Override
	public Poll getPoll(long id) throws DAOException {
		Poll poll = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"select id, title, message "+
					"from Polls "+
					"where id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						poll = new Poll(
							rs.getLong(1),
							rs.getString(2),
							rs.getString(3)
						);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while retrieving polls.", ex);
		}
		return poll;
	}

	@Override
	public PollOption getPollOption(long id) throws DAOException {
		PollOption option = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(
					"select id, optionTitle, optionLink, votesCount "+
					"from PollOptions "+
					"where id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						option = new PollOption(
							rs.getLong(1),
							rs.getString(2),
							rs.getString(3),
							rs.getLong(4)
						);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while retrieving poll option.", ex);
		}
		return option;
	}
}