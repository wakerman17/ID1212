package common;

/**
 * Defines all messages that can be sent between client and server
 */
public enum MsgType {
    /**
     * A client guess on the word
     */
    GUESS,
    /**
     * When the player want to start the game.
     */
    START,
    /**
     * The current Gamestate 
     */
    GAMESTATE,
    /**
     * Client is about to close, all server recourses related to the sending client should be
     * released.
     */
    DISCONNECT
}
