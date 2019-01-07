package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import server.model.ClientDTO;

public class GameStateDTO implements Serializable {
	private static final long serialVersionUID = 8697322934853739527L;
	private String question = null;
	private String answer = null;
	private boolean firstTimeShowQuestion = false;
	private boolean changeQuestion = false;
	private int amountOfClients = 0;
	private int currentWinner = -1;
	private ArrayList<ClientDTO> listOfClients;

	/**
	 * 
	 * @param question              The new value of question.
	 * @param answer                The new value of answer.
	 * @param firstTimeShowQuestion The new value of firstTimeShowQuestion.
	 * @param currentWinner         The new value of currentWinner.
	 * @param changeQuestion        The new value of changeQuestion.
	 */
	public void setupNewQuestion(String question, String answer, boolean firstTimeShowQuestion, int currentWinner,
			boolean changeQuestion) {
		this.question = question;
		this.answer = answer;
		this.firstTimeShowQuestion = firstTimeShowQuestion;
		this.currentWinner = currentWinner;
		this.changeQuestion = changeQuestion;
	}

	/**
	 * 
	 * @param currentWinner  The new value of currentWinner.
	 * @param changeQuestion The new value of changeQuestion.
	 */
	public void setupRightGuess(int currentWinner, boolean changeQuestion) {
		this.currentWinner = currentWinner;
		this.changeQuestion = changeQuestion;
	}

	/**
	 * 
	 * @param listOfClients   The new value of listOfClients.
	 * @param amountOfClients The new value of amountOfClients.
	 */
	public void setupAddClient(ArrayList<ClientDTO> listOfClients, int amountOfClients) {
		this.listOfClients = listOfClients;
		this.amountOfClients = amountOfClients;
	}

	/**
	 * 
	 * @return {@code true} if the client need to change the current question
	 *         otherwise {@code false}.
	 */
	public boolean getNeedToChangeQuestion() {
		return changeQuestion;
	}

	/**
	 * 
	 * @param firstTimeShowQuestion The new value of firstTimeShowQuestion.
	 */
	public void setFirstTimeShowQuestion(boolean firstTimeShowQuestion) {
		this.firstTimeShowQuestion = firstTimeShowQuestion;
	}

	/**
	 * 
	 * @return true if the question should be shown else false.
	 */
	public boolean getFirstTimeShowQuestion() {
		return firstTimeShowQuestion;
	}

	/**
	 * 
	 * @return The question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * 
	 * @return Get the answer of the question
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 
	 * @return The winner's ID of the current game, or -1 if no winner
	 */
	public int getCurrentWinner() {
		return currentWinner;
	}

	/**
	 * 
	 * @return How many clients that is playing the game
	 */
	public int getAmountOfClients() {
		return amountOfClients;
	}

	/**
	 * 
	 * @param id
	 * @return The username of the client with the id
	 */
	public String getUsernameOfClient(int id) {
		ClientDTO client = iteratePlayers(id);
		if (client != null) {
			return client.getUsername();
		}
		return null;
	}

	/**
	 * 
	 * @param id
	 * @return The score of the client with the id
	 */
	public int getScoreOfClient(int id) {
		ClientDTO client = iteratePlayers(id);
		if (client != null) {
			return client.getScore();
		}
		return -1;
	}

	private ClientDTO iteratePlayers(int id) {
		Iterator<ClientDTO> iterator = listOfClients.iterator();
		ClientDTO client;
		while (iterator.hasNext()) {
			client = iterator.next();
			if (client.getID() == id) {
				return client;
			}
		}
		return null;
	}
}
