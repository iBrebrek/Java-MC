package hr.fer.zemris.java.webapp.voting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Offers methods that repeat in voting servlets.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (6.6.2016.)
 */
public final class VotingUtils {
	/** Hidden constructor. */
	private VotingUtils() {
	}

	/**
	 * From given file adds votes to given bands.
	 * 
	 * @param fileName		file with votes.
	 * @param bands			bands to which votes will be added.
	 */
	public static void readVotes(String fileName, List<Band> bands) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (Exception exc) {
			lines = new ArrayList<>();
		}
		for(String line : lines) {
			String[] elements = line.split("\t");
			if(elements.length != 2) continue;
			try {
				int id = Integer.parseInt(elements[0]);
				int votes = Integer.parseInt(elements[1]);
				Band band = new Band();
				band.setId(id);
				bands.get(bands.indexOf(band)).setVotes(votes);
			} catch(Exception exc) {
				continue; // simply ignore that line. Or maybe write error in log file?
			}
		}
	}
	
	/**
	 * Retrieves list of all bands by using given attribute.
	 * If attribute doesn't exists it will read bands from file voting-definition.
	 * If that file doesn't exists writes error message to user.
	 * 
	 * @param req			http request.
	 * @param resp			http response.
	 * @param attribute		attribute used to get bands.
	 * @return list of all bands.
	 * 
	 * @throws ServletException if the target resource throws this exception.
	 * @throws IOException if the target resource throws this exception.
	 */
	@SuppressWarnings("unchecked") //because of casting in line 72
	public static List<Band> getBands(HttpServletRequest req, HttpServletResponse resp, String attribute) 
			throws ServletException, IOException {
		
		List<Band> bands = null;
		
		try {
			bands = (List<Band>)req.getSession().getAttribute(attribute);
		} catch (Exception ignore) {}
		
		if(bands == null) {
			String fileName = req.getServletContext().getRealPath("/WEB-INF/voting-definition.txt");
			List<String> lines = null;
			try {
				lines = Files.readAllLines(Paths.get(fileName));
			} catch(Exception exc) {
				req.setAttribute("errorMsg", 
						"Unable to read/find voting-definition.txt"
				);
				req.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(req, resp);
			}
			
			bands = new ArrayList<>();
			
			for(String line : lines) {
				String[] elements = line.split("\t");
				if(elements.length != 3) continue;
				try {
					bands.add(new Band(
						Integer.parseInt(elements[0]), 
						elements[1], 
						elements[2]
					));
				} catch(Exception exc) {
					continue; // simply ignore that line. Or maybe write error in log file?
				}
			}
			readVotes(req.getServletContext().getRealPath("/WEB-INF/voting-results.txt"), bands);
		}
		
		return bands;
	}	
}
