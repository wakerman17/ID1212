package android.william.hangman.model;

import java.util.ArrayList;

/**
 * Stores the indices of a letter in a specific word.
 * 
 */
public class CorrectLetters {
	final char LETTER;
	int[] indicesOfCorrectLetters;
	int index;
	int iteratior = 0;
	
	
	/**
	 * Creates a new instance of this object.
	 * 
	 * @param thisLetterAmount The amount of characters of this letter in the word
	 * @param letter The letter that is important in this instance. 
	 */
	public CorrectLetters(int thisLetterAmount, char letter) {
		indicesOfCorrectLetters = new int[thisLetterAmount];
		this.LETTER = letter;
	}
	
	public char getLetter () {
		return LETTER;
	}
	
	public int[] getIndices () {
		return indicesOfCorrectLetters;
	}
	
	/**
	 * Add of the indices this letter is in this word to the instance
	 * 
	 * @param allIndices All of the indices this letter is in this word.
	 */
	public void addIndices(ArrayList<Integer> allIndices) {
		for(int i = 0; i < allIndices.size(); i++) {
			indicesOfCorrectLetters[i] = allIndices.get(i).intValue(); 
		}
	} 
}