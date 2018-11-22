package common;

public class Constants {
    /**
     * Separates the instructions (indices) in a message.
     */
    public static final String MSG_SEPARATOR = "##";
    
    /**
     * The index of the the current wordstate.
     */
    public static final int WORDSTATE_INDEX = 0;
    
    /**
     * The index of the the current tries.
     */
    public static final int TRIES_INDEX = 1;
    
    /**
     * The index of the the current score.
     */
    public static final int SCORE_INDEX = 2;
    
    /**
     * The index of the the current amount of unknown Characters in the word.
     */
    public static final int UNKNOWN_CHARACTERS_INDEX = 3;
    
    /**
     * The longest word guess in bytes.
     */
	public static final int MAX_MSG_LENGTH = 8192;
}
