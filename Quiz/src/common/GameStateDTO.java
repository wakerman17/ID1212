package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import server.model.ClientDTO;

public class GameStateDTO implements Serializable{
	private static final long serialVersionUID = 8697322934853739527L;
	private String question = null;
	private String answer = null;
	private boolean firstTimeShowQuestion = false;
	private boolean changeQuestion = false;
	private int amountOfClients = 0;
	private int currentWinner = -1;
	private ArrayList<ClientDTO> listOfPlayers;
	
	public void setNeedToChangeQuestion(boolean changeQuestion) {
		this.changeQuestion = changeQuestion;
	}
	
	/**
	 * 
	 * @return {@code true} if the client need to change the current question otherwise {@code false}. 
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
	 * @param question The new value of question
	 */
	public void setQuestion(String question) {
		this.question = question;
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
	 * @param answer The new value of answer
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
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
	 * @param currentWinner The new value of currentWinner
	 */
	public void setCurrentWinner(int currentWinner) {
		this.currentWinner = currentWinner;
	}
	
	/**
	 * 
	 * @return The winner's ID of the current game, or -1 if no winner
	 */
	public int getCurrentWinner() {
		int currentWinnerReturning = currentWinner;
		currentWinner = -1;
		changeQuestion = false;
		return currentWinnerReturning;
	}
	
	/**
	 * 
	 * @param amountOfClients The new value of amountOfClients
	 */
	public void setAmountOfClients(int amountOfClients) {
		this.amountOfClients = amountOfClients;
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
	 * @param listOfPlayers The new value of listOfPlayers
	 */
	public void setListOfPlayers (ArrayList<ClientDTO> listOfPlayers) {
		this.listOfPlayers = listOfPlayers;
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
		Iterator<ClientDTO> iterator = listOfPlayers.iterator();
		ClientDTO client;
		while (iterator.hasNext()) {
			client = iterator.next();
			if(client.getID() == id) {
				return client;
			}
		}
		return null;
	}
}
