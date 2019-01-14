package android.william.hangman.model;

import java.io.IOException;
import java.util.ArrayList;

import android.william.hangman.common.GameStateDTO;

/**
 * Stores the game logic for the Hangman game.
 *
 */
public class Game {
	String word;
	char[] wordArray; 
	String wordState;
	GameState gameState;
	GameStateDTO gameStateDTO;
	RandomWordsHandler randomWordsHandler;
	
	/**
	 * Creates a new instance of game
	 * @throws IOException 
	 * 
	 */
	public Game() throws IOException {
		randomWordsHandler = new RandomWordsHandler();
		gameStateDTO = new GameStateDTO();
		gameState = new GameState(gameStateDTO);
		//gameState.addDTO(gameStateDTO);
	}
	
	/**
	 * Chooses a new random word and change the game's state after that.
	 * 
	 * @return The game's state when a new word is chosen.
	 */
	public GameStateDTO newWord() {
		word = randomWordsHandler.getWord();
		wordArray = word.toCharArray();
		gameState.newWord(word);
		return gameStateDTO;
	}
	
	/**
	 * 
	 * 
	 * @param guess The guess the user sent
	 * @return The game's state after the result of the guess
	 */
	public GameStateDTO guess(String message) {
		if (message.length() == 1) {
			return charGuess(message.charAt(0));
		} else {
			return stringGuess(message);
		}
	}

	private GameStateDTO charGuess (char guess) {
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
			gameState.addWrongLetter(guess);
			gameState.decreeseTries();
		} else {
			gameState.charCorrect(guess, amountOfSameLetter, indiciesOfRightLetter);
		}
		return gameStateDTO;
	}
	
	private GameStateDTO stringGuess(String message) {
		if (message.equals(word)) {
			gameState.stringCorrect();
		} else {
			gameState.decreeseTries();
		}
		return gameStateDTO;
	}
}
