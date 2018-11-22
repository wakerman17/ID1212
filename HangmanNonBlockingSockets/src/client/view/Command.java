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
     * Any handshape in the game. Isn't written by client. Timeout 30 seconds to choose. Show all clients {@code HANDSHAPE} and {@code score} after all have chosen or after timeout. 
     */
    GUESS,
    /**
     * Anything besides the other commands.
     */
    NOT_ALLOWED, 
    /**
     * Called when a new word should be guessed.
     */
    START
}
