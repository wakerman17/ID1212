package client.view;

/**
 * Reads the line the client prints.
 *
 */
public class LineReader {
    private static final String PARAM_DELIMETER = " ";
    private Command cmd;
    private final String enteredLine;
    private String[] enteredWords;
	
    /**
     * Create a instance of LineReader 
     * 
     * @param enteredLine The line the user entered. 
     */
	LineReader(String enteredLine) {
        this.enteredLine = enteredLine;
        parseCmd();
	}
	
	/**
	 * Get the command the user entered.
	 * 
	 * @return The command the user entered.
	 */
	Command getCmd() {
		return cmd;
	}
	
	/**
	 * 
	 * 
	 * @param index The location of the word starting from 0.
	 * @return The String at the location of the index
	 */
	String getWordAtIndex(int index) {
		return enteredWords[index];
	} 
	
	int amountOfWords() {
		return enteredWords.length;
	}
	
    private void parseCmd() {
        int indexOfCommand = 0;
        enteredWords = enteredLine.stripLeading().split(PARAM_DELIMETER);
        cmd = getCommandValue(enteredWords[indexOfCommand]);
    }
    
    private Command getCommandValue(String str) {
        for (Command command : Command.values()) {
            if (command.name().equalsIgnoreCase(str))
                return command;
        }
        return Command.NOT_ALLOWED;
    }
}
