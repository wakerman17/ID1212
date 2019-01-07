package server.model;

import java.util.ArrayList;
import java.util.Iterator;

import common.GameStateDTO;

/**
 * The Game's state for this client's game, has information about the current guess game and the whole session.    
 * 
 */
public class GameState {
	String question;
	String answer;
	ArrayList<ClientDTO> listOfClients = new ArrayList<ClientDTO>();
	int amountOfClients;
	boolean changeQuestion = false;
	boolean firstTimeShowQuestion = false;
	int currentWinner = -1;
	GameStateDTO gameStateDTO;
	
	public void addDTO(GameStateDTO gameStateDTO) {
		this.gameStateDTO = gameStateDTO;
	}
	
	/**
	 * Change the old and solved word (by losing or winning) by a new one.
	 * 
	 * @param word The new word in the current game.
	 */
	public void newQuestion(QuestionAnswerDTO questionAndAnswer) {
		this.question = questionAndAnswer.getQuestion();
		this.answer = questionAndAnswer.getAnswer();
		firstTimeShowQuestion = true;
		int currentWinner = -1;
		boolean changeQuestion = false;
		gameStateDTO.setupNewQuestion(question, answer, firstTimeShowQuestion, currentWinner, changeQuestion);
	}
	
	/**
	 * Called when the client have guessed. 
	 * 
	 */
	public void firstGuess() {
		firstTimeShowQuestion = false;
		gameStateDTO.setFirstTimeShowQuestion(firstTimeShowQuestion);
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
		gameStateDTO.setupRightGuess(id, changeQuestion);
		//gameStateDTO.setCurrentWinner(id);
		//gameStateDTO.setNeedToChangeQuestion(changeQuestion);
	}
	
	
	/**
	 * 
	 * @return The current question.
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * 
	 * @return The current answer
	 */
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
		listOfClients.add(new ClientDTO(username, id));
		//gameStateDTO.setListOfPlayers(listOfClients);
		amountOfClients++;
		//gameStateDTO.setAmountOfClients(amountOfClients);
		gameStateDTO.setupAddClient(listOfClients, amountOfClients);
	}
	
	/**
	 * @return true if the user should change question, else false.
	 */
	public boolean needToChangeQuestion() {
		return changeQuestion;
	}
	
	/**
	 * Remove the user specified by the ID
	 * 
	 * @param id The id of the client to remove.
	 */
	public void removeClient(int id) {
		Iterator<ClientDTO> iterator = listOfClients.iterator();
		while (iterator.hasNext()) {
			if(iterator.next().getID() == id) {
				iterator.remove();
				break;
			}
		}
	}
	
	private ClientDTO iteratePlayers(int id) {
		Iterator<ClientDTO> iterator = listOfClients.iterator();
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
