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
     * Used when the user want to login to the system.
     */
    LOGIN,
    /**
     * Used when the user want to register a new account to the system.
     */
    REGISTER,
    /**
     * Used when the user want to upload a new file to the system.
     */
    UPLOAD,
    /**
     * Used when the user want to download a file to the system. 
     * If the file have write access the user can change it after this command. If the access is read the file information is shown.  
     */
    DOWNLOAD,
    /**
     * Used when the user want to update a file to the system.
     */
    UPDATE,
    /**
     * Used when the user want to delete a file to the system.
     */
    DELETE,
    /**
     * Used when the user want to see all the files in the system.
     */
    LIST,
    /**
     * Anything besides the other commands.
     */
    NOT_ALLOWED
}
