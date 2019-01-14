package server.model;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class OperateUser {
	final int KEY_LENGTH = 64;
	List<User> listOfLoggedInUsers = new ArrayList<User>();
	
	/**
	 * Insert a user
	 * 
	 * @param user The user to insert
	 */
	public void insertUser(User user) {
		listOfLoggedInUsers.add(user);
	}
	
	/**
	 * Remove a user
	 * 
	 * @param user The user to remove
	 */
	public void removeUser(User user) {
		listOfLoggedInUsers.remove(user);
	}
	
	/**
	 * 
	 * @param userID The specified ID
	 * @return The user with the specified ID
	 */
	public User getUserByID(int userID) {
		return getUser(user -> user.getID() == userID);
	}
	
	/**
	 * 
	 * @param username The specified username
	 * @return The user with the specified username
	 */
	public User getUserByUsername(String username) {
	    return getUser(user -> user.getUsername().equals(username));
	}
	
	private User getUser(Predicate<User> predicate) {
		ListIterator<User> listIterator = listOfLoggedInUsers.listIterator();
		User user;
		while(listIterator.hasNext()) {
			user = listIterator.next();
			if (predicate.test(user)) {
				return user;
			}
		}
		return null;
	}
	
	/**
	 * Generate a hashed password 
	 * 
	 * @param password the plain text password
	 * @return The hashed password
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public String generatePassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, KEY_LENGTH * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		
		return iterations + ":" + toHex(salt) + ":" + toHex(hash);
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	private static String toHex(byte[] byteArray) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, byteArray);
		String hex = bi.toString(16);
		int paddingLength = (byteArray.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
	
	/**
	 * Validate if the two passwords is equal
	 * 
	 * @param originalPassword A plain text password
	 * @param storedPassword A hashed password
	 * @return true if the password is equal, else false.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
		//System.out.println("storedPassword = " + storedPassword);
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int difference = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			difference |= hash[i] ^ testHash[i];
		}
		boolean noDifference = difference == 0;
		return noDifference;
	}

	private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
	
	/**
	 * Add a file to a user 
	 * 
	 * @param file The file to add
	 * @param user The user that should have the file added 
	 */
	public void addWriteFile(File file, User user) {
		if (file.getPermission().equals("Write")) {
			user.addWriteFile(file.getName());
		}
	}
	
	/**
	 * Add several files to a user
	 * 
	 * @param files The files to add
	 * @param user The user that should have the files added 
	 */
	public void addWriteFiles(String[] files, User user) {
		for (int i = 0; i < files.length; i++) {
			user.addWriteFile(files[i]);
		}
	}
	
	/**
	 * Call when the user want to change or delete a file that has write access or file where the user is the owner.
	 * 
	 * @param fileName The filename asking request
	 * @param user The use to check if it has access
	 * @return 
	 */
	public boolean canAccessFile(String myFileName, User user) {
		ListIterator<String> listIterator = user.getWriteFiles().listIterator();
		String fileName;
		while(listIterator.hasNext()) {
			fileName = listIterator.next();
			if (fileName.equals(myFileName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Update the file in a users writelist
	 * 
	 * @param myFileName The previous name
	 * @param newFileName the new name
	 * @param user The user to change list on
	 * @return
	 */
	public boolean updateFile(String myFileName, String newFileName, User user) {
		ListIterator<String> listIterator = user.getWriteFiles().listIterator();
		String fileName;
		while(listIterator.hasNext()) {
			fileName = listIterator.next();
			if (fileName.equals(myFileName)) {
				listIterator.set(newFileName);
				return true;
			}
		}
		return false;
	}

	/**
	 * Delete a file in a users writelist
	 * 
	 * @param fileName The name of the file to delete
	 * @param user The user to change list on
	 */
	public void deleteFile(String fileName, User user) {
		ListIterator<String> listIterator = user.getWriteFiles().listIterator();
		String fileNameIteration;
		while(listIterator.hasNext()) {
			fileNameIteration = listIterator.next();
			if (fileName.equals(fileNameIteration)) {
				listIterator.remove();
			}
		}
	}
}