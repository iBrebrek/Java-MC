package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker used to write in table
 * all given parameters.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.6.2016.)
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		StringBuilder sb = new StringBuilder(
			"<html>\r\n" +
			"  <head><title>EchoParams</title></head>" +
			"  <body>\r\n" + 
			"	 <h1>Provided parameters:</h1>" +
			"    <table border='1'>\r\n" +
			"		<tr><th>PARAMETER</th><th>VALUE</th></tr>");
		for(String param : context.getParameterNames()) {
			sb.append("<tr><td>")
				.append(param)
				.append("</td><td>")
				.append(context.getParameter(param))
				.append("</td></tr>\r\n");
		}
		sb.append(
			"    </table>\r\n" + 
			"  </body>\r\n" + 
			"</html>\r\n");
		try {
			context.write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException("EchoParams is unable to create table.");
		}
	}

}
