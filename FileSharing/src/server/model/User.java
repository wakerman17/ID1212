package server.model;

import java.util.List;
import java.rmi.RemoteException;
import java.util.ArrayList;

import common.UserDTO;

public class User {
	private String username;
	private int id; 
	private final UserDTO clientRemoteObj;
	List<String> listOfWriteFiles = new ArrayList<String>();
	
	/**
	 * Creates a new instance of User
	 * 
	 * @param username The user's username
	 * @param id The user's ID shared in the client
	 * @param clientRemoteObj The refernece to a object so it can call the client
	 */
	public User(String username, int id, UserDTO clientRemoteObj) {
		this.username = username;
		this.id = id;
		this.clientRemoteObj = clientRemoteObj;
	}
	
	/**
	 * Send a notice that a operation on a file has happened
	 * 
	 * @param filename The filename where the operation happened
	 * @param operation The operation
	 */
	public void send(String filename, String operation) {
		try {
			clientRemoteObj.notice(filename, operation);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a message that someone has changed the name of a file in the user's writelist
	 * 
	 * @param oldFilename The old name
	 * @param newFilename The new name
	 * @param operation what was done.
	 */
	public void sendNameChange(String oldFilename, String newFilename, String operation) {
		try {
			clientRemoteObj.noticeNameChange(oldFilename, newFilename, operation);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 
	 * @return The ID
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * 
	 * @param filename The filename to add to the user
	 */
	public void addWriteFile(String filename) {
		listOfWriteFiles.add(filename);
	}
	
	/**
	 * 
	 * @param filename The filename to remove to the user
	 */
	public void removeWriteFile(String filename) {
		listOfWriteFiles.remove(filename);
	}
	
	/**
	 * Change name of a file in the user's writelist
	 * 
	 * @param oldFilename The old name of a file
	 * @param newFilename The new name of a file
	 */
	public void changeWriteFile(String oldFilename, String newFilename) {
		int index = listOfWriteFiles.indexOf(oldFilename);
		listOfWriteFiles.set(index, newFilename);
	}
	
	/**
	 * 
	 * @return All the writefiles for this user
	 */
	public List<String> getWriteFiles(){
		return listOfWriteFiles;
	}

}
