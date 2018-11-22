package client.view;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

import client.net.ClientCommunication;
import client.net.OutputHandler;
import common.Constants;
import common.LogHandler;

/**
 * Handles the entered line by the client.
 *
 */
public class LineWriter implements Runnable {
	private final String CONNECT_PROMPT = "write connection> ";
	private final String GUESS_PROMPT = "print 'guess ' then a word or letter> ";
	private final String START_PROMPT = "print 'START' to play> ";
	private String prompt = CONNECT_PROMPT;
	private LogHandler logger;
	private boolean okToWrite = false;
	private boolean inGame = false;
	private ErrorMessageHandler errorMessageHandler;
	private final ClientCommunication clientCommunication;

	public LineWriter(ClientCommunication clientCommunication, ErrorMessageHandler errorMessageHandler) {
		this.errorMessageHandler = errorMessageHandler;
		this.clientCommunication = clientCommunication;
	}

	/**
	 * Starts the client
	 * 
	 * @throws IOException If the LogHandler don't work.
	 */
	public void start() throws IOException {
		if (okToWrite) {
			return;
		}
		logger = new LogHandler();
		okToWrite = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		Scanner lineReader = new Scanner(System.in);

		System.out.println(
				"Hi, welcome to the game Hangman. First you need to conect to the server. You need to copy the information from the server.");
		boolean connected = false;
		inGame = false;
		while (okToWrite) {
			try {
				if (!inGame) {
					System.out.print(prompt);
				}
				LineReader enteredLine = new LineReader(lineReader.nextLine());
				switch (enteredLine.getCmd()) {
				case CONNECT:
					if (!connected) {
						InetSocketAddress serverAddress = enteredLine.getServerAddress();
						if (serverAddress != null) {
							clientCommunication.addClient(serverAddress, new ConsoleOutput());
							prompt = START_PROMPT;
							System.out.println(
									"In the game you you are given a unknown english word that's have the form '---', where each '-' means an unknown letter, you should guess this word.");
							System.out.println(
									"You can either guess a letter or a word, if a letter you guess is in the word you get the positions where the letter are in the word.");
							System.out.println(
									"If you guess a letter that's not in the word you lose a trie, same if you guess a word.");
							System.out.println(
									"If any letter in the word you guess is right but not the full word, the right letters will not show up.");
							System.out.println(
									"When the word is right you gain a point, if you run out of tries you lose a point for this playing session.");
							System.out.println(
									"Commands: To guess print: \"guess [...]\" where [...] is your guess. To quit print: \"quit\".");
							System.out.println(
									"Also, When the message ends with the character \">\" it means you should give input based on the information before it, or a guess if there is no information before it.");
							connected = true;
						} else {
							System.out.println("Copy the information from the server.");
						}
					} else {
						System.out.println("You can't connect again.");
						if (inGame) {
							System.out.println(GUESS_PROMPT);
						}
					}
					break;
				case START:
					if (connected && !inGame) {
						clientCommunication.startGame();
						inGame = true;
					} else {
						if (!connected) {
							System.out.println("You must connect first.");
						} else if (inGame) {
							System.out.println("You alredy in a game.");
							System.out.print(GUESS_PROMPT);
						}
					}
					break;
				case QUIT:
					if (connected) {
						try {
							lineReader.close();
							clientCommunication.disconnect();
						} catch (IOException e) {
							errorExecution("A problem happend when it should've closed.", e);
						}
						connected = false;
						okToWrite = false;
					} else {
						System.out.println("You haven't connected.");
					}
					break;

				case GUESS:
					if (connected) {
						String guess = enteredLine.getGuess();
						if (guess != null) {
							clientCommunication.guess(guess);
							prompt = GUESS_PROMPT;
						} else {
							System.out.println("You must enter a guess.");
							System.out.print(GUESS_PROMPT);
						}
					} else {
						System.out.println("You need to connect before starting.");
					}
					break;

				case NOT_ALLOWED:
					System.out.println("You entered a command that isn't allowed at all!");
					if (inGame) {
						System.out.print(GUESS_PROMPT);
					}
					break;
				}
			} catch (IOException e) {
				errorExecution("A problem happend.", e);
			}
		}
	}

	private void errorExecution(String msg, Exception e) {
		errorMessageHandler.showErrorMsg(msg);
		boolean onClient = true;
		logger.logException(e, onClient);
	}

	/**
	 * Print information from the server.
	 * 
	 */
	public class ConsoleOutput implements OutputHandler {
		final String UNKNOWN = "-";
		
		@Override
		public void handleGameState(String gameState) {
			String[] gameStateParts = gameState.split(Constants.MSG_SEPARATOR);
			int tries = Integer.parseInt(gameStateParts[Constants.TRIES_INDEX]);
			boolean triesIsZero = (tries == 0);
			boolean gameEnded = (triesIsZero || (Integer.parseInt(gameStateParts[Constants.UNKNOWN_CHARACTERS_INDEX]) == 0));
			if (triesIsZero) {
				System.out.print("Sorry, you lost. ");
			}
			System.out.print("Word: " + gameStateParts[Constants.WORDSTATE_INDEX]);
			if (!gameEnded) {
				System.out.print(", Tries left: " + tries);
			}
			System.out.println(", Score: " + gameStateParts[Constants.SCORE_INDEX]);
			if (gameEnded) {
				System.out.print(START_PROMPT);
				inGame = false;
			} else {
				System.out.print(GUESS_PROMPT);
			}
		}

		@Override
		public void handleErrorMessage(Exception e) {
			okToWrite = false;
			inGame = false;
			System.out.println("\n\nERROR: Lost connection.");
			boolean onClient = true;
			logger.logException(e, onClient);
		}
	}
}
