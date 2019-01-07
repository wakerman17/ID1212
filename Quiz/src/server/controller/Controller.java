package server.controller;

import java.io.IOException;

import common.GameStateDTO;
import server.model.Game;
import server.model.GameStart;
import server.model.GameState;

/**
 * Handles all calls from the client handler to the model.
 *
 */
public class Controller {
	Game game;
	GameStart gameStart;

	/**
	 * Creates a new instance of the controller
	 * 
	 * @throws IOException
	 */
	public Controller() throws IOException {
		game = new Game();
		gameStart = new GameStart();
	}

	public void addUsername(String username, int player) {
		GameState gameState = game.getGameState();
		gameState.addClient(username, player);
	}

	/**
	 * 
	 * @return The gamestate when a new question is chosen.
	 */
	public GameStateDTO newQuestion() {
		return game.newQuestion();
	}

	/**
	 * Guess the question.
	 * 
	 * @param guess The guess the user sent
	 * @param id    The id of the ClientHandler
	 * @return The GameStateDTO after the result of the guess
	 */
	public GameStateDTO guess(String guess, int id) {
		return game.guess(guess, id);
	}

	/**
	 * Start the game.
	 * 
	 * @return The new GameState if the client is the last writing start, otherwise
	 *         null
	 */
	public GameStateDTO startGame() {
		gameStart.readyPlayer();
		if (gameStart.lastPlayer()) {
			return game.newQuestion();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return true if the client won, otherwise false
	 */
	public boolean clientWin() {
		return game.getGameState().needToChangeQuestion();
	}

	/**
	 * Remove the user specified by the ID
	 * 
	 * @param id The id of the client to remove.
	 */
	public void removeClient(int id) {
		gameStart.removeClient();
		game.getGameState().removeClient(id);
	}

}
