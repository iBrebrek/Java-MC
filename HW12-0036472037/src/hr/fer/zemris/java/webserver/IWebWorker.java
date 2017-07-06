package hr.fer.zemris.java.webserver;

/**
 * Interface toward any object that can process current 
 * request: it gets RequestContext as parameter and it 
 * is expected to create a content for client.
 * 
 * @author Ivica Brebrek
 * @version 1.0  (1.6.2016.)
 */
public interface IWebWorker {
	
	/**
	 * Produces content for client.
	 * 
	 * @param context		request context.
	 */
	public void processRequest(RequestContext context);
}