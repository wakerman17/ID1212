package server.model;

import java.util.ArrayList;
import java.util.StringJoiner;

import common.Constants;

/**
 * Class that provide the current Game's state for this client. It's in the form [Word state]##[amount of tries]##[Score]##[Number of unknown characters]
 * 
 */
public class GameState {
	final String UNKNOWN = "-";
	private String word;
	String wordState;
	int score = 0;
	int wordLength;
	int tries;
	int unknownCharactersInWordState;
	StringBuilder stringBuilder;
	
	/**
	 * Called when a new word should be chosen.
	 * 
	 * @param word The word the use should solve
	 * @return The Current Game's state String
	 */
	public String newWord(String word) {
		this.word = word;
		wordLength = word.length();
		tries = word.length();
		unknownCharactersInWordState = word.length();
		wordState = UNKNOWN.repeat(wordLength);
		stringBuilder = new StringBuilder(wordState);
		boolean lose = false;
		return parseState(lose, false);
	}
	
	/**
	 * Called when the tries should decrees.
	 * 
	 * @return The Current Game's state String
	 */
	public String decreeseTries() {
		tries--;
		if (tries == 0) {
			score--;
			boolean lose = true;
			boolean stringWin = false;
			return parseState(lose, stringWin);
		}
		boolean lose = false;
		boolean stringWin = false;
		return parseState(lose, stringWin);
	}
	
	/**
	 * Called when a word guess is correct guessed
	 *  
	 * @return The Current Game's state String
	 */
	public String stringCorrect() {
		score++;
		boolean lose = false;
		boolean stringWin = true;
		return parseState(lose, stringWin);
	}
	
	/**
	 * 
	 * @param guess
	 * @param indiciesOfRightLetter
	 * @return The Current Game's state String
	 */
	public String guessDone(char guess, ArrayList<Integer> indiciesOfRightLetter) {
		for (int i = 0; i < indiciesOfRightLetter.size(); i++) {
			stringBuilder.setCharAt(indiciesOfRightLetter.get(i).intValue(), guess);
			unknownCharactersInWordState--;
		}
		wordState = stringBuilder.toString();
		if(wordState.equals(word)) {
			score++;
		}
		boolean lose = false;
		boolean stringWin = false;
		return parseState(lose, stringWin);
	}
	
	private String parseState(boolean lost, boolean stringWin) {
		StringJoiner joiner = new StringJoiner(Constants.MSG_SEPARATOR);
		int unknownCharacters;
		if (lost || stringWin) {
			joiner.add(word);
			unknownCharacters = 0;
		} else {
			joiner.add(wordState);
			unknownCharacters = unknownCharactersInWordState;
		}
		joiner.add(String.valueOf(tries));
		joiner.add(String.valueOf(score));
		joiner.add(String.valueOf(unknownCharacters));
		return joiner.toString();
	}
}
