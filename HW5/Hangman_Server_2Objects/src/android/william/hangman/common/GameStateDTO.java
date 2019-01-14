package android.william.hangman.common;

import java.io.Serializable;
import java.util.Set;

public class GameStateDTO implements Serializable {
	private static final long serialVersionUID = -1415188034720232418L;
	String word;
	int tries;
	int score = 0;
	Set<Character> uniqueLetters;
	int amountOfUniqueLetters;
	CorrectLettersDTO[] correctLetters;
	Set<Character> wrongLetters;
	byte correctLetterIterator = 0;
	boolean changeWord = false;
	boolean lose = false;
	
	public void setNeedToChangeWord(boolean changeWord) {
		this.changeWord = changeWord;
	}
	
	/**
	 * 
	 * @return {@code true} if the client need to change the current word otherwise {@code false}.
	 */
	public boolean needToChangeWord() {
		return changeWord;
	}
	
	public void setWordSize(int amountOfUniqueLetters) {
		this.amountOfUniqueLetters = amountOfUniqueLetters;
	}
	
	/**
	 * 
	 * @return The amount of characters in the word 
	 */
	public int getWordSize() {
		return word.length();
	}
	
	public void setAmountOfUniqueLetters (int amount) {
		correctLetters = new CorrectLettersDTO[amount];
	}
	
	/**
	 * 
	 * @return The amount of correct letters (unique characters).
	 */
	public CorrectLettersDTO[] getCorrectLetters() {
		return correctLetters;
	}
	
	public void setTries (int tries) {
		this.tries = tries;
	}
	
	/**
	 * 
	 * @return How many tries the client has left before losing.
	 */
	public int getTries () {
		return tries;
	}
	
	public void setScore (int score) {
		this.score = score;
	}
	
	/**
	 * 
	 * @return The user's current score the the game session.
	 */
	public int getScore () {
		return score;
	}
	
	public void setdidILose (boolean lose) {
		this.lose = lose;
	}
	
	/**
	 * 
	 * @return {@code boolean} if the user lost it returns {@code true}, else {@code false}.
	 */
	public boolean didILose() {
		return lose;
	}
	
	public void setWord(String word) {
		this.word = word;
		amountOfUniqueLetters = word.length();
		lose = false;
		changeWord = false;
	}
	
	public void setupNewWord(String word, int score, Set<Character> wrongLetters, int amountUniqueLetters) {
		this.word = word;
		tries = word.length();
		lose = false;
		changeWord = false;
		this.score = score;
		this.wrongLetters = wrongLetters;
		correctLetters = new CorrectLettersDTO[amountUniqueLetters];
	}
	
	public void setupDecreeseTries(int tries, int score, boolean changeWord, boolean lose) {
		this.tries = tries;
		this.score = score;
		this.changeWord = changeWord;
		this.lose = lose;
	}
	
	public void setupCharCorrect(int score, boolean changeWord, CorrectLettersDTO correctLetterDTA,
			byte correctLetterIterator) {
		this.score = score;
		this.changeWord = changeWord;
		correctLetters[correctLetterIterator] = correctLetterDTA;
	}
	
	public void setupStringCorrect(int score, boolean changeWord) {
		this.score = score;
		this.changeWord = changeWord;
	}
	
	/**
	 * 
	 * @return Get the current word.
	 */
	public String getWord() {
		return word;
	}
	
	/*public void setWrongLetters(Set<Character> wrongLetters) {
		this.wrongLetters = wrongLetters;
	}*/
	
	public void setWrongLetter(Set<Character> wrongLetters) {
		this.wrongLetters = wrongLetters;
	}
	
	public Set<Character> getWrongLetters() {
		return wrongLetters;
	}

	public void addCorrectLetters(CorrectLettersDTO correctLetterDTA, byte correctLetterIterator) {
		correctLetters[correctLetterIterator] = correctLetterDTA;
	}
}
