package server.model;

import java.io.IOException;
import java.util.ArrayList;

import common.GameState;

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
	public GameState newWord() {
		word = randomWordsHandler.getWord();
		wordArray = word.toCharArray();
		gameState.newWord(word);
		return gameState;
	}
	
	/**
	 * 
	 * 
	 * @param guess The guess the user sent
	 * @return The game's state after the result of the guess
	 */
	public GameState guess(String message) {
		if (message.length() == 1) {
			return charGuess(message.charAt(0));
		} else {
			return stringGuess(message);
		}
	}

	private GameState charGuess (char guess) {
		boolean charIsRight = false;
		int amountOfSameLetter = 0;
		ArrayList<Integer> indiciesOfRightLetter = new ArrayList<Integer>();
		for (int i = 0; i < wordArray.length; i++) {
			if(wordArray[i] == guess) {
				amountOfSameLetter++;
				indiciesOfRightLetter.add(i);
				charIsRight = true;
			}
		}
		if (!charIsRight) {
			gameState.decreeseTries();
		} else {
			gameState.guessDone(guess, amountOfSameLetter, indiciesOfRightLetter);
		}
		return gameState;
	}
	
	private GameState stringGuess(String message) {
		if (message.equals(word)) {
			gameState.stringCorrect();
		} else {
			gameState.decreeseTries();
		}
		return gameState;
	}
}
