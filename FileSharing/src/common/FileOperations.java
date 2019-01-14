package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.List;

public interface FileOperations extends Remote {
	
	/**
	 * Creates a new account with the information in the arguments if possible. If it could be created the user is logged in.
	 * 
	 * @param username The username on the new account, must be unique.
	 * @param password The password on the new account.
	 * @param clientRemoteObj 
	 * @return The ID for the client, unique for this server session
	 * @throws RemoteException If unable to complete the RMI call.
	 */
	public int createAccount(String username, String password, UserDTO clientRemoteObj) throws RemoteException;
	
	public static final String SERVER_NAME_IN_REGISTRY = "file_operator";
	
	/**
	 * Try to login to a already existing account.
	 * 
	 * @param username The username on the account
	 * @param password The password on the account.
	 * @param clientRemoteObj 
	 * @return <code>0</code> if the account was found. <code>1</code> if the username was found, but the password was wrong.  <code>2</code> if the username wasn't found.
	 * @throws RemoteException If unable to complete the RMI call.
	 */
	public int login(String username, String password, UserDTO clientRemoteObj) throws RemoteException;
	
	/**
	 * 
	 * @return All of the files in the database
	 * @throws RemoteException If unable to complete the RMI call.
	 */
	public FileDTO[] getAllFiles() throws RemoteException;
	
	/**
	 * Get the specified file (meaning the user can edit it if have write permission)
	 * 
	 * @param filename The filename the user want
	 * @param userID The ID the client use 
	 * @return The specified File
	 * @throws RemoteException If unable to complete the RMI call.
	 */
	public FileDTO getFile(String filename, int userID) throws RemoteException;
	
	/**
	 * Upload a new file to the database.
	 * 
	 * @param filename The filename of the new file
	 * @param size The size of the new file
	 * @param permission The permission of the new file
	 * @param connectionID The ID the client use
	 * @return true if the file could be uploaded, else false
	 * @throws RemoteException
	 */
	public boolean uploadFile(String filename, String size, String permission, int connectionID) throws RemoteException;
	
	/**
	 * 
	 * @param orgininalFilename The original filename of the file
	 * @param newFilename The new filename of the file
	 * @param newSize The new size of the file
	 * @param connectionID The ID the client use
	 * @return true if the file could be changed, else false
	 * @throws RemoteException
	 */
	public boolean updateFileNameAndSize(String orgininalFilename, String newFilename, String newSize, int connectionID) throws RemoteException;
	
	/**
	 * 
	 * @param orgininalFilename The original filename of the file
	 * @param newFilename The new filename of the file
	 * @param connectionID The ID the client use
	 * @return true if the file could be changed, else false
	 * @throws RemoteException
	 */
	public boolean updateFileName(String orgininalFilename, String newFilename, int connectionID) throws RemoteException;
	
	/**
	 * 
	 * @param orgininalFilename The original filename of the file
	 * @param newFileSize The new filename of the file
	 * @param connectionID The ID the client use
	 * @return true if the file could be changed, else false
	 * @throws RemoteException
	 */
	public boolean updateFileSize(String orgininalFilename, String newFileSize, int connectionID) throws RemoteException;
	
	/**
	 * Delete a file from the database 
	 * 
	 * @param fileName The filename of the file that should be deleted
	 * @param connectionID The ID the client use
	 * @return true if the file could be deleted, else false
	 * @throws RemoteException
	 */
	public boolean deleteFile(String fileName, int connectionID) throws RemoteException;
	
	/**
	 * 
	 * @param connectionID The ID the client use
	 * @return The files this user has uploaded
	 * @throws RemoteException
	 */
	public List<String> getDownloadFiles(int connectionID) throws RemoteException;
	
	/**
	 * Disconnect the client
	 * 
	 * @param connectionID The ID the client use
	 * @throws RemoteException
	 */
	public void disconnect(int connectionID) throws RemoteException;
}
