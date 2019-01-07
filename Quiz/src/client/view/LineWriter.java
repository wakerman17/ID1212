package client.view;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

import client.controller.Controller;
import client.net.OutputHandler;
import common.GameStateDTO;
import common.LogHandler;

/**
 * Handles the entered line by the client.
 *
 */
public class LineWriter implements Runnable {
	private final String CONNECT_PROMPT = "write connection> ";
	private final String USERNAME_PROMPT = "print 'USERNAME [username]' to choose username> ";
	private final String START_PROMPT = "print 'START' to play> ";
	private final String GUESS_PROMPT = "print your guess> ";
	
	private final String WAITING_OPPONENT_MESSAGE = "You are waiting for an opponent.";
	private final String CONNECTING_MESSAGE = "You must connect first.";
	private String prompt = CONNECT_PROMPT;
	private LogHandler logger;
	private Controller controller;
	private volatile boolean waitForOtherPlayer = false;
	private volatile boolean okToWrite = false;
	private volatile boolean gonnaStart = false;
	private boolean inGame = false;
	private volatile boolean connected = false;
	private ErrorMessageHandler errorMessageHandler;

	/**
	 * Starts the client
	 * 
	 * @param controller
	 * @param errorMessageHandler2
	 * 
	 * @throws IOException If the LogHandler don't work.
	 */
	public void start(Controller controller, ErrorMessageHandler errorMessageHandler) throws IOException {
		this.controller = controller;
		this.errorMessageHandler = errorMessageHandler;
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
				"Hi, welcome to a quiz game. First you need to conect to the server. You need to copy the information from the server. Same prompt again means "
				+ "unsuccessful to connect.");
		//boolean connected = false;
		boolean usernameSet = false;
		
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
						String serverAddress = enteredLine.getWordsAfter(1);
						if (serverAddress != null) {
							String[] serverAddressArray = serverAddress.split(" ");
							controller.addClient(serverAddressArray[0], serverAddressArray[1], new ConsoleOutput());
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
				case USERNAME:
					if (!connected) {
						System.out.println(CONNECTING_MESSAGE);
						break;
					}
					if (!usernameSet) {
						String username = enteredLine.getLastWord(2);
						if (username != null) {
							controller.setUsername(username);
							System.out.println("Username set.");
							prompt = START_PROMPT;
							usernameSet = true;
						} else {
							System.out.println("Don't forget to print your username.");
						}
					} else {
						System.out.println("You have already choosen a username.");
					}
					break;
				case START:
					if (!waitForOtherPlayer) {
						if (connected && !inGame && usernameSet) {
							controller.startGame();
							prompt = "";
							System.out.println("Waiting for an opponent.");
							waitForOtherPlayer = true;
							gonnaStart = true;
						} else {
							if (!connected) {
								System.out.println(CONNECTING_MESSAGE);
							} else if (inGame) {
								System.out.println("You alredy in a game.");
								System.out.print(GUESS_PROMPT);
							} else if (!usernameSet) {
								System.out.println("You need to write your username first");
							}
						}
					} else {
						System.out.println(WAITING_OPPONENT_MESSAGE);
					}

					break;
				case QUIT:
					if (connected) {
						try {
							lineReader.close();
							controller.disconnect();
						} catch (IOException e) {
							errorExecution("A problem happend when it should've closed.", e);
						}
						connected = false;
						okToWrite = false;
						// gameOn = false;
					} else {
						System.out.println(CONNECTING_MESSAGE);
					}
					break;

				case GUESS:

				default:
					if (!waitForOtherPlayer) {
						if (connected && gonnaStart) {
							String guess = enteredLine.getWordsAfter(0);
							if (guess != null) {
								controller.guess(guess);
								prompt = GUESS_PROMPT;
							} else {
								System.out.println("You must enter a guess.");
								System.out.print(GUESS_PROMPT);
							}
						} else if (connected && !inGame) {
							System.out.println(CONNECTING_MESSAGE);
						}
					} else {
						System.out.println(WAITING_OPPONENT_MESSAGE);
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				errorExecution("A problem happend. Press enter to confirm.", e);
				okToWrite = false;
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
		public void handleGameState(GameStateDTO gameState) {
			if (!gameState.getNeedToChangeQuestion()) {
				if (gameState.getFirstTimeShowQuestion()) {
					inGame = true;
					waitForOtherPlayer = false;
					prompt = GUESS_PROMPT;
					System.out.println("\n" + gameState.getQuestion() + "\n");
					System.out.print(prompt);
				} else {
					System.out.println("Your guess was wrong.");
					System.out.print(prompt);
				}
			} else {
				System.out.println("Player " + gameState.getUsernameOfClient(gameState.getCurrentWinner())
						+ " won, the answer was " + gameState.getAnswer() + ".");
				System.out.println("This is the current score:");
				for (int i = 0; i < gameState.getAmountOfClients(); i++) {
					System.out.println("Player " + gameState.getUsernameOfClient(i) + " has " + gameState.getScoreOfClient(i));
				}
				gonnaStart = false;
				prompt = START_PROMPT;
				System.out.print(prompt);
				inGame = false;
			}
		}

		@Override
		public void handleErrorMessage(Exception connectionFailure) {
			okToWrite = false;
			inGame = false;
			errorExecution("\n\nLost connection. Press enter to confirm.", connectionFailure);
		}

		@Override
		public void handleDisconnect() {
			System.out.println("Some other player quited. The game has therefore ended. Press enter to confirm.");
			okToWrite = false;
		}

		@Override
		public void connectedToServer() {
			prompt = USERNAME_PROMPT;
			System.out.println(
					"In the game you and opponents will get a question, the one to first answer right gets a point");
			System.out.println("Commands: To quit print: \"quit\".");
			connected = true;
			System.out.print(prompt);
		}
	}
}
