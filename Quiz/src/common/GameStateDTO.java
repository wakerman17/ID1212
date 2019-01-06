package common;

import java.io.Serializable;

public interface GameStateDTO extends Serializable {
	
	/**
	 * 
	 * @return {@code true} if the client need to change the current question otherwise {@code false}. 
	 */
	public boolean needToChangeQuestion();
	
	/**
	 * 
	 * @return true if the question should be shown else false.
	 */
	public boolean getFirstTimeShowQuestion();
	
	/**
	 * 
	 * @return The question
	 */
	public String getQuestion();
	
	/**
	 * 
	 * @param id
	 * @return The username based on the id
	 */
	public String usernameOfClient(int id);
	
	/**
	 * 
	 * @return The winner's ID of the current game, or -1 if no winner
	 */
	public int getCurrentWinner();
	
	/**
	 * 
	 * @return Get the answer of the question
	 */
	public String getAnswer();
	
	/**
	 * 
	 * @return How many clients that is playing the game
	 */
	public int getAmountOfClients();
	
	/**
	 * 
	 * @param id
	 * @return The score of the client
	 */
	public int scoreOfClient(int id);
}
