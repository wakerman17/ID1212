package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserDTO extends Remote {
	
	/**
	 * Send a notice that a operation on a file has happened
	 * 
	 * @param filename The filename where the operation happened
	 * @param operation The operation
	 * @throws RemoteException
	 */
	public void notice(String filename, String operation) throws RemoteException;
	
	/**
	 * Send a message that someone has changed the name of a file in the user's writelist
	 * 
	 * @param oldFilename The old name
	 * @param newFilename The new name
	 * @param operation what was done.
	 * @throws RemoteException
	 */
	public void noticeNameChange(String oldFilename, String newFilename, String operation) throws RemoteException;
}
