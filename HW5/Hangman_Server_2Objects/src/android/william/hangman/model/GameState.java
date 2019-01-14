package android.william.hangman.model;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import android.william.hangman.common.CorrectLettersDTO;
import android.william.hangman.common.GameStateDTO;

/**
 * The Game's state for this client's game, has information about the current guess game and the whole session.    
 * 
 */
public class GameState {
	String word;
	int tries;
	int score = 0;
	Set<Character> amountOfUniqueLetters;
	CorrectLetters[] correctLetters;
	Set<Character> wrongLetters;
	byte correctLetterIterator = 0;
	boolean changeWord = false;
	boolean lose = false;
	GameStateDTO gameStateDTO;
	
	public GameState(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}
	
	/**
	 * Change the old and solved word (by losing or winning) by a new one.
	 * 
	 * @param word The new word in the current game.
	 */
	public void newWord(String word) {
		this.word = word;
		tries = word.length();
		amountOfUniqueLetters = new TreeSet<>();
		int size = 0;
		wrongLetters = new TreeSet<>();
		for(char character : word.toCharArray() ) {
			for (int i = 0; i < size; i++) {
				size++;
			}
			amountOfUniqueLetters.add(character);
		}
		lose = false;
		correctLetters = new CorrectLetters[amountOfUniqueLetters.size()];
		//Character[] a = new Character[1];
		changeWord = false;
		correctLetterIterator = 0;
		gameStateDTO.setupNewWord(word, score, wrongLetters, amountOfUniqueLetters.size());
		//gameStateDTO.setTries(tries);
		/*gameStateDTO.setScore(score);
		gameStateDTO.setWrongLetter(wrongLetters);
		gameStateDTO.setAmountOfUniqueLetters(amountOfUniqueLetters.size());*/
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
		gameStateDTO.setupDecreeseTries(tries, score, changeWord, lose);
		/*gameStateDTO.setScore(score);
		gameStateDTO.setNeedToChangeWord(changeWord);
		gameStateDTO.setdidILose(lose);*/
	}
	
	public int getTries () {
		return tries;
	}
	
	public int getScore () {
		return score;
	}
	
	public int getWordSize() {
		return word.length();
	}
	
	public CorrectLetters[] getCorrectLetters() {
		return correctLetters;
	}
	
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
	public void charCorrect(char rightGuess, int amountOfSameLetter, ArrayList<Integer> indiciesOfRightLetter) {
		CorrectLetters correctLetter = new CorrectLetters(amountOfSameLetter, rightGuess);
		correctLetter.addIndices(indiciesOfRightLetter);
		CorrectLettersDTO correctLetterDTO = new CorrectLettersDTO(correctLetter.getLetter(), correctLetter.getIndices());
		correctLetters[correctLetterIterator] = correctLetter;
		if ((correctLetterIterator + 1) == correctLetters.length) {
			score++;
			changeWord = true;
			correctLetterIterator = 0;
		} else {
			correctLetterIterator++;
		}
		gameStateDTO.setupCharCorrect(score, changeWord, correctLetterDTO, correctLetterIterator);
		/*gameStateDTO.setScore(score);
		gameStateDTO.setNeedToChangeWord(changeWord);
		gameStateDTO.addCorrectLetters(correctLetterDTA, correctLetterIterator);*/
	}
	
	public boolean needToChangeWord() {
		return changeWord;
	}
	
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
		gameStateDTO.setupStringCorrect(score, changeWord);
		//gameStateDTO.setNeedToChangeWord(changeWord);
	}

	public void addWrongLetter(char guess) {
		wrongLetters.add(guess);
		gameStateDTO.setWrongLetter(wrongLetters);
	}
	
	public Set<Character> getWrongLetters() {
		return wrongLetters;
	}
}
