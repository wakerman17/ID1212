package client.controller;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

import client.view.LineWriter.ConsoleOutput;
import client.net.ClientCommunication;

/**
 * Controller between the view and net on the client
 *
 */
public class Controller {
    private final ClientCommunication clientCommunication;
    ConsoleOutput consoleOutput;
    
    /**
     * Creates a new instance of Controller 
     * 
     * @param clientCommunication A reference to ClientCommunication.
     */
    public Controller(ClientCommunication clientCommunication) {
    	this.clientCommunication = clientCommunication;
	}

	/**
     * Add a client to the server connection.
     * 
     * @param serverAddress The address to the server
     * @param consoleOutput The observer to make the server send information to the view.
     */
	public void addClient(String serverIP, String serverPort, ConsoleOutput consoleOutput) {
        CompletableFuture.runAsync(() -> {
            try {
            	clientCommunication.addClient(serverIP, serverPort, consoleOutput);
            } catch (IOException e) {
            	throw new UncheckedIOException(e);
            }
        }).thenRun(() -> consoleOutput.connectedToServer());
		
	}
	
	public void setUsername(String username) {
		CompletableFuture.runAsync(() -> {
			try {
				clientCommunication.setUsername(username);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} 
		});
	}
	
	/**
	 * Start the game by sending a startmessage.
	 * @param username 
	 * 
	 */
	public void startGame() { 
		CompletableFuture.runAsync(() -> {
			try {
				clientCommunication.startGame();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			} 
		});
	}
	
	/**
	 * Sends the guess.
	 * 
	 * @param guess The word or letter the user guess
	 */
	public void guess(String guess) {
		CompletableFuture.runAsync(() -> {
			try {
				clientCommunication.guess(guess);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}
	
	/**
	 * Send a message to disconnect from the server.
	 * 
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void disconnect() throws IOException {
		clientCommunication.disconnect();
		
	}
}
