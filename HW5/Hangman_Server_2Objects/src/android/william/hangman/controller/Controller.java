package android.william.hangman.controller;

import java.io.IOException;

import android.william.hangman.common.GameStateDTO;
import android.william.hangman.model.Game;

/**
 * Handles all calls from the client handler to the android.william.hangman.model.
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
	public GameStateDTO newWord() {
		return game.newWord();
	}
	/**
	 * 
	 * @param guess The guess the user sent
	 * @return The gamestate after the result of the guess
	 */
	public GameStateDTO guess(String guess) {
		return game.guess(guess);
		
	}

}
