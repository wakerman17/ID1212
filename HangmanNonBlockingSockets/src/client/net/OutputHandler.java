package client.net;

/**
 * An observer that make sure the view show information that the server send.
 *
 */
public interface OutputHandler {
	
	/**
	 * Print the current game's state.
	 * 
	 * @param gameState The current game's state.
	 */
	public void handleGameState(String gameState);
	
	/**
	 * Print if an error has accrued.
	 * 
	 */
	public void handleErrorMessage(Exception e);
}
