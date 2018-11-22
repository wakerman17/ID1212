package server.model;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Stores the game logic for the Hangman game.
 *
 */
public class Game {
	String word;
	char[] wordArray; 
	String wordState;
	GameState gameState;
	RandomWordsHandler randomWordsHandler;
	
	/**
	 * Creates a new instance of game
	 * @throws IOException 
	 * 
	 */
	public Game() throws IOException {
		randomWordsHandler = new RandomWordsHandler();
		gameState = new GameState();
		
	}
	
	/**
	 * Chooses a new random word and change the game's state after that.
	 * 
	 * @return The game's state when a new word is chosen.
	 */
	public String newWord() {
		word = randomWordsHandler.getWord();
		wordArray = word.toCharArray();
		return gameState.newWord(word);
	}
	
	/**
	 * 
	 * 
	 * @param guess The guess the user sent
	 * @return The game's state after the result of the guess
	 */
	public String guess(String message) {
		if (message.length() == 1) {
			return charGuess(message.charAt(0));
		} else {
			return stringGuess(message);
		}
	}

	private String charGuess (char guess) {
		boolean charIsRight = false;
		ArrayList<Integer> indiciesOfRightLetter = new ArrayList<Integer>();
		for (int i = 0; i < wordArray.length; i++) {
			if(wordArray[i] == guess) {
				indiciesOfRightLetter.add(i);
				charIsRight = true;
			}
		}
		if (!charIsRight) {
			return gameState.decreeseTries();
		} else {
			return gameState.guessDone(guess, indiciesOfRightLetter);
		}
	}
	
	private String stringGuess(String message) {
		if (message.equals(word)) {
			return gameState.stringCorrect();
		} else {
			return gameState.decreeseTries();
		}
	}
}
