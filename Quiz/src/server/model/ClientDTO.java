package server.model;

import java.io.Serializable;

/**
 * A DTO with information about a Client in the game.
 *
 */
public class ClientDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private int score = 0; 
	private final int ID;
	
	/**
	 * Create a new instance of the ClientDTO.
	 *  
	 * @param username The username of the current Client
	 * @param id The ID of the current Client
	 */
	public ClientDTO(String username, int id) {
		this.username = username;
		ID = id;
	}
	
	/**
	 * Add one to the score
	 */
	public void addScore() {
		score++;
	}
	
	/**
	 * 
	 * @return The ID of the client
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * 
	 * @return The username of the client.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 
	 * @return The score of the client
	 */
	public int getScore() {
		return score;
	}
	
}
