package server.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import server.util.Constants;

public class GameStart {
	private int clientsReady = 0;
	
	final Lock lock = new ReentrantLock();
	final Condition waitForOtherPlayer = lock.newCondition();

	/**
	 * Add a client.
	 * 
	 */
	public void readyPlayer() {
		clientsReady++;
	}
	
	/**
	 * Remove a client.
	 * 
	 */
	public void removeClient() {
		clientsReady--;
	}
	
	/**
	 * 
	 * @return true if the player is the last one else false
	 */
	public boolean lastPlayer() {
		if (clientsReady == Constants.AMOUNT_OF_PLAYERS) {
			return true;
		}
		return false;
	}
}
