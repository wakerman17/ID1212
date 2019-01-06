package server.model;

import java.util.ArrayList;
import java.util.Iterator;

import common.GameStateDTO;

/**
 * The Game's state for this client's game, has information about the current guess game and the whole session.    
 * 
 */
public class GameState implements GameStateDTO {
	private static final long serialVersionUID = 6728193616943214673L;
	String question;
	String answer;
	ArrayList<ClientDTO> listOfPlayers = new ArrayList<ClientDTO>();
	int amountOfPlayers;
	boolean changeQuestion = false;
	boolean firstTimeShowQuestion = false;
	int currentWinner = -1;
	
	/**
	 * Change the old and solved word (by losing or winning) by a new one.
	 * 
	 * @param word The new word in the current game.
	 */
	public void newQuestion(QuestionAnswerDTO questionAndAnswer) {
		this.question = questionAndAnswer.getQuestion();
		this.answer = questionAndAnswer.getAnswer();
		firstTimeShowQuestion = true;
	}
	
	/**
	 * Called when the client hav guessed. 
	 * 
	 */
	public void firstGuess() {
		firstTimeShowQuestion = false;
	}
	
	/**
	 * 
	 * @return true if the client should show question, else false
	 */
	@Override
	public boolean getFirstTimeShowQuestion() {
		 return firstTimeShowQuestion;
	}
	
	/**
	 * Called when a client has answered right
	 * 
	 * @param id The ID of the ClientHandler guessing right.
	 */
	public void answerRight(int id) {
		ClientDTO client = iteratePlayers(id);
		if (client != null) {
			client.addScore();
		}
		currentWinner = id;
		changeQuestion = true;
	}
	
	/**
	 * Get the current winner 
	 * 
	 * @return the ID from the ClientDTO current winner 
	 */
	@Override
	public int getCurrentWinner() {
		int currentWinnerReturning = currentWinner;
		currentWinner = -1;
		changeQuestion = false;
		return currentWinnerReturning;
	}
	
	/**
	 * 
	 * @return The current question.
	 */
	@Override
	public String getQuestion() {
		return question;
	}
	
	/**
	 * 
	 * @return The current answer
	 */
	@Override
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Add a client
	 * 
	 * @param username The username of the Client
	 * @param id The ID of the Client
	 */
	synchronized public void addClient(String username, int id) {
		listOfPlayers.add(new ClientDTO(username, id));
		amountOfPlayers++;
	}
	
	/**
	 * 
	 * @return The amount of clients currently
	 */
	@Override
	public int getAmountOfClients() {
		return amountOfPlayers;
	}
	
	/**
	 * 
	 * @param id
	 * @return The username of the client with the ID in the argument.
	 */
	@Override
	public String usernameOfClient(int id) {
		ClientDTO client = iteratePlayers(id);
		if (client != null) {
			return client.getUsername();
		}
		return null;
	}
	
	/**
	 * 
	 * @param id
	 * @return The score of the client with the ID in the argument.
	 */
	@Override
	public int scoreOfClient(int id) {
		ClientDTO client = iteratePlayers(id);
		if (client != null) {
			return client.getScore();
		}
		return -1;
	}
	
	/**
	 * @return true if the user should change question, else false.
	 */
	@Override
	public boolean needToChangeQuestion() {
		return changeQuestion;
	}
	
	/**
	 * Remove the user specified by the ID
	 * 
	 * @param id The id of the client to remove.
	 */
	public void removeClient(int id) {
		Iterator<ClientDTO> iterator = listOfPlayers.iterator();
		while (iterator.hasNext()) {
			if(iterator.next().getID() == id) {
				iterator.remove();
				break;
			}
		}
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
