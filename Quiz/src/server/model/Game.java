package server.model;

import java.io.IOException;

/**
 * Stores the game logic for the Hangman game.
 *
 */
public class Game {
	QuestionAnswerDTO questionAndAnswer;
	GameState gameState;
	RandomQuestionHandler randomQuestionHandler;

	/**
	 * Creates a new instance of game
	 * 
	 * @throws IOException
	 * 
	 */
	public Game() throws IOException {
		randomQuestionHandler = new RandomQuestionHandler();
		gameState = new GameState();

	}

	/**
	 * Chooses a new random question and change the game's state after that.
	 * 
	 * @return The game's state when a new word is chosen.
	 */
	public GameState newQuestion() {
		questionAndAnswer = randomQuestionHandler.getQuestionAndAnswer();
		gameState.newQuestion(questionAndAnswer);
		return gameState;
	}
	
	/**
	 * 
	 * @return The current GameState
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * A guess to the word
	 * 
	 * @param guess The guess the client sent
	 * @param id    The ID of the ClientHandler sending the guess
	 * @return The game's state after the result of the guess
	 */
	synchronized public GameState guess(String guess, int id) {
		gameState.firstGuess();
		if (guess.equals(gameState.getAnswer())) {
			gameState.answerRight(id);
		}
		return gameState;
	}
}
