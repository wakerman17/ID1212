package server.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import common.UserDTO;
import common.FileOperations;
import server.model.User;
import server.model.File;
import server.integration.FileDAO;
import server.model.OperateUser;

public class Controller extends UnicastRemoteObject implements FileOperations {
	private static final long serialVersionUID = -729035211646707620L;

	ArrayList<User> listOfUserThatHaveUploaded = new ArrayList<User>();

	private final FileDAO fileDb;

	private OperateUser operateUser;
	int userID = 0;

	public Controller(OperateUser operateUser) throws RemoteException {
		this.operateUser = operateUser;
		fileDb = new FileDAO();
	}

	@Override
	public synchronized int createAccount(String username, String password, UserDTO clientRemoteObj)
			throws RemoteException {
		try {
			if (fileDb.getPassword(username).equals("")) {
				String hashedPassword = operateUser.generatePassword(password);
				fileDb.createAccount(username, hashedPassword);
				User user = new User(username, ++userID, clientRemoteObj);
				operateUser.insertUser(user);
			} else {
				return 0;
			}

		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return userID;
	}

	@Override
	public synchronized int login(String username, String comparingPassword, UserDTO clientRemoteObj)
			throws RemoteException {
		try {
			String hashedPassword = fileDb.getPassword(username);
			if (!(hashedPassword.equals("")) && operateUser.validatePassword(comparingPassword, hashedPassword)) {
				User user = new User(username, ++userID, clientRemoteObj);
				operateUser.insertUser(user);
				String[] fileNames = fileDb.getUserFiles(user.getUsername());
				operateUser.addWriteFiles(fileNames, user);
			} else {
				return 0;
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return userID;
	}

	@Override
	public synchronized void disconnect(int connectionID) throws RemoteException {
		User user = operateUser.getUserByID(userID);
		operateUser.removeUser(user);

	}

	@Override
	public synchronized File[] getAllFiles() throws RemoteException {
		String[][] allFiles = fileDb.getFiles();
		File[] allFileObject = new File[allFiles.length];
		for (int i = 0; i < allFiles.length; i++) {
			allFileObject[i] = new File(allFiles[i][0], allFiles[i][1], allFiles[i][2], allFiles[i][3]);
		}
		return allFileObject;
	}

	@Override
	public synchronized List<String> getDownloadFiles(int connectionID) throws RemoteException {
		User user = operateUser.getUserByID(userID);
		return user.getWriteFiles();
	}

	@Override
	public synchronized File getFile(String filename, int userID) throws RemoteException {
		User user = operateUser.getUserByID(userID);
		String[] fileInfo = fileDb.getFile(filename);
		noticeUser(user.getUsername(), fileInfo[1], fileInfo[0], "download");
		File file = new File(fileInfo[0], fileInfo[1], fileInfo[2], fileInfo[3]);
		return file;
	}

	@Override
	public synchronized boolean uploadFile(String filename, String size, String permission, int userID)
			throws RemoteException {
		User user = operateUser.getUserByID(userID);
		if (!fileDb.fileExists(filename)
				&& (permission.equalsIgnoreCase("Write") || permission.equalsIgnoreCase("Read"))) {
			fileDb.uploadFile(filename, size, user.getUsername(), permission);
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean updateFileNameAndSize(String orgininalFilename, String newFilename,
			String newSize, int userID) throws RemoteException {
		User user = operateUser.getUserByID(userID);
		if (fileCheck(orgininalFilename, user)) {
			String owner = fileDb.updateFile(orgininalFilename, newFilename, newSize);
			noticeUserNameChange(user.getUsername(), owner, orgininalFilename, newFilename, "nameSize");
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean updateFileName(String orgininalFilename, String newFilename, int userID)
			throws RemoteException {
		User user = operateUser.getUserByID(userID);
		if (fileCheck(orgininalFilename, user)) {
			String owner = fileDb.updateOnlyNameFile(orgininalFilename, newFilename);
			noticeUserNameChange(user.getUsername(), owner, orgininalFilename, newFilename, "size");
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean updateFileSize(String filename, String newSize, int userID)
			throws RemoteException {
		User user = operateUser.getUserByID(userID);
		if (fileCheck(filename, user)) {
			String owner = fileDb.updateFileSize(filename, newSize);
			noticeUser(user.getUsername(), owner, filename, "size");
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean deleteFile(String filename, int userID) throws RemoteException {
		User user = operateUser.getUserByID(userID);
		if (fileCheck(filename, user)) {
			String ownerOfDeletedFile = fileDb.deleteFile(filename);
			noticeUser(user.getUsername(), ownerOfDeletedFile, filename, "delete");
			return true;
		}
		return false;
	}

	private boolean fileCheck(String filename, User user) {
		String[] fileInfo = fileDb.getFile(filename);
		if (fileInfo[1].equals(user.getUsername()) || fileInfo[3].equalsIgnoreCase("Write")) {
			return true;
		}
		return false;
	}

	private void noticeUser(String user, String ownerOfFile, String filename, String operation) {
		if (!user.equals(ownerOfFile)) {
			User owner = operateUser.getUserByUsername(ownerOfFile);
			if (owner != null) {
				owner.send(filename, operation);
			}
		}
	}

	private void noticeUserNameChange(String user, String ownerOfFile, String oldFilename, String newFilename,
			String operation) {
		if (!user.equals(ownerOfFile)) {
			User owner = operateUser.getUserByUsername(ownerOfFile);
			if (owner != null) {
				owner.sendNameChange(oldFilename, newFilename, newFilename);
			}
		}
	}
}
