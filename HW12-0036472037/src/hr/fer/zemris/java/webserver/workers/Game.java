package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.Random;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Simple web game, guess a number from 1 to 100.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (2.6.2016.)
 */
public class Game implements IWebWorker {
	
	/** How many times can you guess. */
	private static final int START_TRIES = 7;
	
	/** Form used to submit a number */
	private static final String INPUT = 
			"<form action=\"/ext/Game\">"+
			"Guess a number: <input type=\"text\" name=\"number\">"+
			"<input type=\"submit\"></form>";
	
	/** Form used to play again. */
	private static final String AGAIN =
			"<form action=\"/ext/Game\">"+
			"<input type=\"submit\" value=\"Play Again!\">"+
			"</form>";

	@Override
	public void processRequest(RequestContext context) {
		String html = "<html><head><title>Simple game</title></head>"+
			"<body><h1>Guess a number from 1 to 100</h1>";
			
		String number = context.getParameter("number");
		String goal = context.getPersistentParameter("goal");
		
		//start the game
		if(number == null || goal == null) { //goal could be null if you finished and went page back in browser
			int random = new Random().nextInt(100) + 1;
			context.setPersistentParameter("goal", String.valueOf(random));
			context.setPersistentParameter("left", String.valueOf(START_TRIES));
			html += INPUT;
			
		//continue playing
		} else {
			//he won
			if(goal.equals(number)) {
				clearData(context);
				html += "<p>You got it! Number was "+goal+".</p>";
				html += AGAIN;
				
			//didn't win yet
			} else {
				int left = Integer.parseInt(context.getPersistentParameter("left"));
				left--;
				html += "<p>Tries left: "+left+"/"+START_TRIES+"</p>";
				context.setPersistentParameter("left", String.valueOf(left));
				
				//he lost
				if(left < 1) {
					clearData(context);
					html += "<p>Noob! Number was "+goal+"...</p>";
					html += AGAIN;
					
				//let him quess again
				} else {
					try {
						int toGuess = Integer.parseInt(goal);
						int guessed = Integer.parseInt(number);
						if (guessed < toGuess) {
							html += "<p>" + number + " is too low.</p>";
						} else {
							html += "<p>" + number + " is too big.</p>";
						}
					} catch (NumberFormatException exc) {
						html += "<p><font color=\"red\">You guessed: \""+number+"\", nice try.</font></p>";
					}
					html += INPUT;
				}
			}
		}
		html += "</body></html>";
		try {
			context.write(html);
		} catch (IOException e) {
			clearData(context);
			throw new RuntimeException("Game crashed.");
		}
	}

	/**
	 * Removes parameters used by this game.
	 * 
	 * @param context		request context of this game.
	 */
	private void clearData(RequestContext context) {
		context.removePersistentParameter("goal");
		context.removePersistentParameter("left");
	}
}
