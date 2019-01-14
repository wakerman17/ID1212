package server.integration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileDAO {
	static protected Connection con;
	private PreparedStatement findAccountStmt;
	private PreparedStatement findFilesByOwnerStmt;
	private PreparedStatement countFilesWithAOwnerStmt;
	private PreparedStatement insertAccountStmt;
	private PreparedStatement countFilesStmt;
	private PreparedStatement allFilesStmt;
	private PreparedStatement updateFileStmt;
	private PreparedStatement updateFileSizeStmt;
	private PreparedStatement updateOnlyNameFileStmt;
	private PreparedStatement insertFileStmt;
	private PreparedStatement findFileStmt;
	private PreparedStatement deleteFileStmt;
	
	/**
	 * Creates a new instance of the FileDAO
	 * 
	 */
	public FileDAO() {
		try {
			String URL = "jdbc:mysql://localhost:3306/id1212_3?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Stockholm&useSSL=false";
			String driver = "com.mysql.cj.jdbc.Driver";
			String[] usernameAndPasswordForDatabase = getDatabaseLogin();
			String user = usernameAndPasswordForDatabase[0];
			String password = usernameAndPasswordForDatabase[1];
			Class.forName(driver);
			con = DriverManager.getConnection(URL, user, password);
			con.setAutoCommit(false);
			prepareStatements(con);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}

	}

	private String[] getDatabaseLogin() throws IOException {
		String[] usernameAndPasswordForDatabase = new String[2];
		String fileName = "D:\\KTH\\Kurser\\Year_3\\P2\\Natverksprogrammering\\Database_secret\\private.txt";
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		int i = 0;
		String line = bufferedReader.readLine();
		while (line != null) {
			if (line.length() != 1) {
				usernameAndPasswordForDatabase[i] = line;
				i++;
			}
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
		return usernameAndPasswordForDatabase;
	}
	
	/**
	 * Creates a new user in the database
	 * 
	 * @param username The new user's username 
	 * @param password The new user's hashed password
	 */
	public void createAccount(String username, String password) {
		try {
			insertAccountStmt.setString(1, username);
			insertAccountStmt.setString(2, password);
			int numberOfRows = insertAccountStmt.executeUpdate();
			con.commit();
            if (numberOfRows == 0) {
                throw new SQLException();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param username The username that
	 * @return The password for the specifeid username
	 */
	public String getPassword(String username) {
		try {
			findAccountStmt.setString(1, username);
			ResultSet result = findAccountStmt.executeQuery();
			if (result.next()) {
				return result.getString(2);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param username The specified user
	 * @return All files the user has uploaded 
	 */
	public String[] getUserFiles(String username)  {
		String[] files = null;
		try {
			countFilesWithAOwnerStmt.setString(1, username);
			ResultSet resultSet = countFilesWithAOwnerStmt.executeQuery();
			resultSet.next();
			int amountOfRows = resultSet.getInt("total");
			files = new String[amountOfRows];
			int i = 0;
			findFilesByOwnerStmt.setString(1, username);
			resultSet = findFilesByOwnerStmt.executeQuery();
			while (resultSet.next()) {
				files[i] = resultSet.getString(1);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return files;
	}
	
	/**
	 * 
	 * @param filename The filename to check..
	 * @return true if the specified filename exsits, else false
	 */
	public boolean fileExists(String filename) {
		try {
			ResultSet resultSet = getFilePrivate(filename);
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param filename The filename of the file that should return
	 * @return The String[] with the info of the file. 
	 * Index: 0 = filename, 1 = owner, 2 = size and 3 = permission
	 */
	public String[] getFile(String filename) {
		String[] fileInfo = {"", "", "", ""};
		try {
			ResultSet result = getFilePrivate(filename);
			while (result.next()) {
				fileInfo[0] = result.getString(1);
				fileInfo[1] = result.getString(2);
				fileInfo[2] = result.getString(3);
				fileInfo[3] = result.getString(4);
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fileInfo;
	}
	
	private ResultSet getFilePrivate(String filename) throws SQLException {
		findFileStmt.setString(1, filename);
		return findFileStmt.executeQuery();
	}
	
	/**
	 * 
	 * @return All files in a String[][] where the first array is the which file and the second have the info based on
	 * Index: 0 = filename, 1 = owner, 2 = size and 3 = permission
	 */
	public String[][] getFiles() {
		String[][] allFiles = null;
		try {
			ResultSet resultSet = countFilesStmt.executeQuery();
			resultSet.next();
			int amountOfRows = resultSet.getInt("total");
			allFiles = new String[amountOfRows][4];
			resultSet = allFilesStmt.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				allFiles[i][0] = resultSet.getString(1);
				allFiles[i][1] = resultSet.getString(2);
				allFiles[i][2] = resultSet.getString(3);
				allFiles[i][3] = resultSet.getString(4);
				i++;
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allFiles;
	}
	
	/**
	 * Upload a new file to the database
	 * 
	 * @param name The name of the new file
	 * @param size The size of the new file 
	 * @param owner The owner of the new file
	 * @param permission The permission of the new file
	 */
	public void uploadFile(String name, String size, String owner, String permission) {
		try {
			insertFileStmt.setString(1, name);
			insertFileStmt.setString(2, owner);
			insertFileStmt.setString(3, size);
			insertFileStmt.setString(4, permission);
			int numberOfRows = insertFileStmt.executeUpdate();
			con.commit();
            if (numberOfRows == 0) {
                throw new SQLException();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param orgininalFilename The original filename of the file that should update
	 * @param newFilename The new filename of the file that should update
	 * @param newSize The new size of the  file that should update
	 * @return
	 */
	public String updateFile(String orgininalFilename, String newFilename, String newSize) {
		try {
			updateFileStmt.setString(1, newFilename);
			updateFileStmt.setString(2, newSize);
			updateFileStmt.setString(3, orgininalFilename);
			updateFileStmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getOwner(newFilename);
	}
	
	/**
	 * 
	 * @param filename The filename of the file that should update
	 * @param newSize The new size of the file that should update
	 * @return
	 */
	public String updateFileSize(String filename, String newSize) {
		try {
			updateFileSizeStmt.setString(1, newSize);
			updateFileSizeStmt.setString(2, filename);
			 updateFileSizeStmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getOwner(filename);
	}
	
	/**
	 * 
	 * @param orgininalFilename The original filename of the file that should update
	 * @param newFilename The new size of the file that should update
	 * @return
	 */
	public String updateOnlyNameFile(String orgininalFilename, String newFilename) {
		try {
			updateOnlyNameFileStmt.setString(1, newFilename);
			updateOnlyNameFileStmt.setString(2, orgininalFilename);
			updateOnlyNameFileStmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getOwner(newFilename);
	}
	
	/**
	 * 
	 * @param filename The file to check owner of.
	 * @return The owner of the specified file
	 */
	public String getOwner(String filename) {
		try {
			findFileStmt.setString(1, filename);
			ResultSet resultSet = findFileStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Delete a specific file
	 * 
	 * @param fileName The file to delete	 
	 * @return The owner of the file that was deleted.
	 */
	public String deleteFile(String fileName) {
		String ownerOfDeletedFile = getOwner(fileName);
		try {
			deleteFileStmt.setString(1, fileName);
			deleteFileStmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ownerOfDeletedFile;
	}
	
	 private void prepareStatements(Connection con) throws SQLException {
		 findAccountStmt = con.prepareStatement("SELECT * FROM person WHERE Username =?");
		 findFilesByOwnerStmt = con.prepareStatement("SELECT name FROM file WHERE Owner =?");
		 countFilesWithAOwnerStmt = con.prepareStatement("SELECT COUNT(*) AS total FROM file WHERE Owner=?");
		 insertAccountStmt = con.prepareStatement("INSERT INTO person VALUE (?, ?)");
		 insertFileStmt = con.prepareStatement("INSERT INTO file VALUE (?, ?, ?, ?)");
		 findFileStmt = con.prepareStatement("SELECT * FROM file WHERE Name = ?");
		 updateFileStmt = con.prepareStatement("UPDATE file SET Name=?, Size=? WHERE Name=?");
		 updateFileSizeStmt = con.prepareStatement("UPDATE file SET Size=? WHERE Name=?");
		 updateOnlyNameFileStmt = con.prepareStatement("UPDATE file SET Name=? WHERE Name=?");
		 countFilesStmt = con.prepareStatement("SELECT COUNT(*) AS total FROM file");
		 allFilesStmt = con.prepareStatement("SELECT * FROM file");
		 deleteFileStmt = con.prepareStatement("DELETE FROM file WHERE Name=?");
	 }
}
