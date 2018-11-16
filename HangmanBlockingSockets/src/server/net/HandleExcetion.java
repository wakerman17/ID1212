package server.net;

/**
 * Makes sure the Exceptions that happen on server is shown for the developer  
 * 
 */
public interface HandleExcetion {
	
	/**
	 * A method that should handle a Exception on the server so the developer knows without showing it on the view.
	 * 
	 * @param e The accrued exception
	 */
	public void handleExcetion(Exception e);
}
