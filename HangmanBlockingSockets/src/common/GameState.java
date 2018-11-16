package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * The Game's state for this client's game, has information about the current guess game and the whole session.    
 * 
 */
public class GameState implements Serializable{
	private static final long serialVersionUID = 6728193616943214673L;
	String word;
	int tries;
	int score = 0;
	Set<Character> amountOfUniqueLetters;
	CorrectLetters[] correctLetters;
	char[] wrongLetters;
	byte correctLetterIterator = 0;
	boolean changeWord = false;
	boolean lose = false;
	
	/**
	 * Change the old and solved word (by losing or winning) by a new one.
	 * 
	 * @param word The new word in the current game.
	 */
	public void newWord(String word) {
		this.word = word;
		tries = word.length();
		amountOfUniqueLetters = new TreeSet<>();
		for( char c : word.toCharArray() ) {
			amountOfUniqueLetters.add(c);
		}
		lose = false;
		correctLetters = new CorrectLetters[amountOfUniqueLetters.size()];
		changeWord = false;
		correctLetterIterator = 0;
	}
	
	/**
	 * Decrees the amount of tries the user has in the current game.
	 */
	public void decreeseTries () {
		tries--;
		if (tries == 0) {
			score--;
			changeWord = true;
			lose = true;
		}
	}
	
	/**
	 * 
	 * @return How many tries the client has left before losing.
	 */
	public int getTries () {
		return tries;
	}
	
	/**
	 * 
	 * @return The user's current score the the game session.
	 */
	public int getScore () {
		return score;
	}
	
	/**
	 * 
	 * @return The amount of characters in the word 
	 */
	public int getWordSize() {
		return word.length();
	}
	
	/**
	 * 
	 * @return The amount of correct letters (unique characters).
	 */
	public CorrectLetters[] getCorrectLetters() {
		return correctLetters;
	}
	
	/**
	 * 
	 * @return {@code boolean} if the user lost it returns {@code true}, else {@code false}.
	 */
	public boolean didILose() {
		return lose;
	}
	
	/**
	 * Stores all the right indices that've been found on this word
	 * 
	 * @param rightGuess The right guess for a letter in the word
	 * @param amountOfSameLetter The amount of the same letter in the word.
	 * @param indiciesOfRightLetter All of the right indices 
	 */
	public void guessDone(char rightGuess, int amountOfSameLetter, ArrayList<Integer> indiciesOfRightLetter) {
		CorrectLetters correctLetter = new CorrectLetters(amountOfSameLetter, rightGuess);
		correctLetter.addIndices(indiciesOfRightLetter);
		correctLetters[correctLetterIterator] = correctLetter;
		if ((correctLetterIterator + 1) == correctLetters.length) {
			score++;
			changeWord = true;
			correctLetterIterator = 0;
		} else {
			correctLetterIterator++;
		}
	}
	
	/**
	 * 
	 * @return {@code true} if the client need to change the current word oterwise {@code false}.
	 */
	public boolean needToChangeWord() {
		return changeWord;
	}
	
	/**
	 * 
	 * @return Get the current word.
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Call when a full word guess is right.
	 * 
	 */
	public void stringCorrect() {
		changeWord = true;
		score++;
	}
	
}
