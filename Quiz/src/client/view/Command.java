package client.view;
/**
 * The commands that is allowed by the client.
 *
 */
public enum Command {
	/**
     * Establish a connection to the server. The first parameter is IP address to the server and the
     * second is port number to the server.
     */
    CONNECT,
    /**
     * Leave the game.
     */
    QUIT,
    /**
     * Guess an answer.
     */
    GUESS,
    
    USERNAME,
    /**
     * Called when a new question should be guessed.
     */
    START
}
