package client.view;

import java.net.InetSocketAddress;

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
	 * Return's the ServerAddress the user printed.
	 * 
	 * @return The NodeAddress the user printed if it's in right format (Command [IP] [PortNo]) else {@code null}.
	 */
	InetSocketAddress getServerAddress() {
		if (enteredWords.length == 3) {
			byte ipAddressIndex = 1;
			byte portNumberIndex = 2;
			if (enteredWords[ipAddressIndex] != null & enteredWords[portNumberIndex] != null) {
				return new InetSocketAddress(enteredWords[ipAddressIndex], Integer.parseInt(enteredWords[portNumberIndex]));
			}
		} 
		return null;
	}
	
	/**
	 * 
	 * 
	 * @return The guess to the word if something was entered.
	 */
	String getGuess() {
		if (enteredWords.length == 2) {
			byte guessIndex = 1;
			if (enteredWords[guessIndex] != null) {
				return enteredWords[guessIndex];
			}
		}
		return null;
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
