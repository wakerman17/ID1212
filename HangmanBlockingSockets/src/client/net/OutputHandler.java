package client.net;

import common.GameState;

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
	public void handleGameState(GameState gameState);
	
	/**
	 * Print if an error has accrued.
	 * 
	 */
	public void handleErrorMessage();
}
