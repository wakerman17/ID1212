package server.controller;

import java.io.IOException;

import server.model.Game;

/**
 * Handles all calls from the client handler to the model.
 *
 */
public class Controller {
	Game game;
	public Controller() throws IOException {
		game = new Game();
	}
	
	/**
	 * 
	 * @return The gamestate when a new word is chosen.
	 */
	public String newWord() {
		return game.newWord();
	}
	/**
	 * 
	 * @param guess The guess the user sent
	 * @return The gamestate after the result of the guess
	 */
	public String guess(String guess) {
		return game.guess(guess);
		
	}

}
