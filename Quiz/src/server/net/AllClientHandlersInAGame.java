package server.net;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import common.GameStateDTO;
import common.Message;
import common.MsgType;
import server.util.Constants;

/**
 * 
 * 
 */
public class AllClientHandlersInAGame {
	ClientHandler[] clientHandlers = new ClientHandler[Constants.AMOUNT_OF_PLAYERS];

	final Lock lock = new ReentrantLock();
	final Condition waitForOtherPlayer = lock.newCondition();
	
	/**
	 * Add a clientHandler 
	 * 
	 * @param clientHandler The one to add 
	 */
	void addClientHandler(ClientHandler clientHandler) {
		for (int i = 0; i < clientHandlers.length; i++) {
			if (clientHandlers[i] == null) {
				clientHandlers[i] = clientHandler;
				break;
			}
		}
	}
	
	/**
	 * Send a gameState message to all clients
	 * 
	 * @param gameState The current state of the game.
	 */
	void broadcast(GameStateDTO gameState) {
		for (int i = 0; i < clientHandlers.length; i++) {
			clientHandlers[i].sendMessage(new Message(MsgType.GAMESTATE, gameState));
		}
	}
	
	/**
	 * Send a message to all clients beside the one sending
	 * 
	 * @param clientHandler The one not to send.
	 */
	void sendDisconnectMessage(ClientHandler clientHandler) {
		for (int i = 0; i < clientHandlers.length; i++) {
			if(!clientHandlers[i].equals(clientHandler)) {
				clientHandlers[i].sendMessage(new Message(MsgType.DISCONNECT));
			}
		}
	}
	
	/**
	 * Remove the specified ClientHandler from the list
	 * 
	 * @param clientHandler The one to remove.
	 */
	void disconnect(ClientHandler clientHandler) {
		for (int i = 0; i < clientHandlers.length; i++) {
			if (clientHandlers[i] != null && clientHandlers[i].equals(clientHandler)) {
				clientHandlers[i] = null;
			}
		}
	}
}
