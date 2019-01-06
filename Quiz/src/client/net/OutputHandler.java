package client.net;

import common.GameStateDTO;

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
	public void handleGameState(GameStateDTO gameState);
	
	/**
	 * Print if an error has accrued.
	 * @param connectionFailure 
	 * 
	 */
	void handleErrorMessage(Exception connectionFailure);
	
	/**
	 * Print a disconnect message.
	 */
	public void handleDisconnect ();
}
