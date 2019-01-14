package client.view;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import common.UserDTO;
import common.FileDTO;
import common.FileOperations;
import common.LogHandler;

/**
 * Handles the entered line by the client.
 *
 */
public class LineWriter implements Runnable {
	private final String CONNECT_PROMPT = "write connection> ";
	private final String LOGGED_IN_PROMPT = "print the operation you want to make> ";
	private final String LOGIN_PROMPT = "print 'LOGIN ' or 'REGISTER ' then a username and then a password> ";
	private final String UPDATE_SUCCESS = "Updated successfully.";
	private final String COULD_NOT_UPDATE = "Update coudln't be done, either you haven't downloaded the file or someone has changed it's name.";
	private final String CONNECT_FIRST = "You must connect first.";
	private final String LOGIN_FIRST = "You must login first.";
	private final String NAME_OF_FILE = "Write name of the file> ";
	private String prompt = CONNECT_PROMPT;
	private LogHandler logger;
	private boolean okToWrite = false;
	private ErrorMessageHandler errorMessageHandler;
	private FileOperations fileOperations;
	private final UserDTO myRemoteObj;

	public LineWriter(ErrorMessageHandler errorMessageHandler) throws RemoteException {
		this.errorMessageHandler = errorMessageHandler;
		myRemoteObj = new ConsoleOutput();
	}

	/**
	 * Starts the client
	 * 
	 * @throws IOException If the LogHandler don't work.
	 */
	public void start() throws IOException {
		if (okToWrite) {
			return;
		}
		logger = new LogHandler();
		okToWrite = true;
		new Thread(this).start();
	}

	@Override
	public void run() {
		Scanner lineReader = new Scanner(System.in);

		System.out.println(
				"Hi, welcome to a program where you can get and recieve metadata of files. First you need to conect to the server. You need to copy the information from the server.");
		boolean connected = false;
		boolean loggedIn = false;
		int connectionID = 0;
		while (okToWrite) {
			try {
				System.out.print(prompt);
				LineReader enteredLine = new LineReader(lineReader.nextLine());
				switch (enteredLine.getCmd()) {
				case CONNECT:
					if (!connected) {
						if (enteredLine.amountOfWords() == 2) {
							String serverAddress = enteredLine.getWordAtIndex(1);
							if (serverAddress != null) {
								try {
									fileOperations = (FileOperations) Naming.lookup(
											"//" + serverAddress + "/" + FileOperations.SERVER_NAME_IN_REGISTRY);
								} catch (NotBoundException e) {
									errorExecution("Couldn't connect to the server.", e);
								}
								prompt = LOGIN_PROMPT;
								System.out.println("Connection sucessful");
								connected = true;
							} else {
								System.out.println("Copy the information from the server.");
							}
						} else {
							System.out.println("Use: CONNECT [IP]");
						}
					} else {
						System.out.println("You can't connect again.");
					}
					break;
				case REGISTER:
					if (connected && !loggedIn) {
						if (enteredLine.amountOfWords() == 3) {
							connectionID = fileOperations.createAccount(enteredLine.getWordAtIndex(1),
									enteredLine.getWordAtIndex(2), myRemoteObj);
							if (connectionID != 0) {
								prompt = LOGGED_IN_PROMPT;
								loggedIn = true;
								printInfo();
							} else {
								System.out.println("Someone alredy has that username.");
							}
						} else {
							System.out.println("Use: Register [username] [password]");
						}
					} else if (connected && loggedIn) {
						System.out.println("You have already logged in");
					} else if (!connected && !loggedIn) {
						System.out.println(CONNECT_FIRST);
					}
					break;
				case LOGIN:
					if (connected && !loggedIn) {
						if (enteredLine.amountOfWords() == 3) {
							connectionID = fileOperations.login(enteredLine.getWordAtIndex(1),
									enteredLine.getWordAtIndex(2), myRemoteObj);
							if (connectionID != 0) {
								prompt = LOGGED_IN_PROMPT;
								loggedIn = true;
								printInfo();
								break;
							} else {
								System.out.println("The combination of username and password is wrong.");
							}
						} else {
							System.out.println("Use: Login [username] [password]");
						}
					} else if (connected && loggedIn) {
						System.out.println("You have already logged in");
					} else if (!connected && !loggedIn) {
						System.out.println(CONNECT_FIRST);
					}
					break;
				case LIST:
					if (connected && loggedIn) {
						FileDTO[] fileArray = fileOperations.getAllFiles();
						for (int i = 0; i < fileArray.length; i++) {
							System.out.println("Filename: " + fileArray[i].getName() + ", Owner: "
									+ fileArray[i].getOwner() + ", Permission: " + fileArray[i].getPermission()
									+ ", Size: " + fileArray[i].getSize());
						}
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				case UPLOAD:
					if (connected && loggedIn) {
						if (enteredLine.amountOfWords() == 1) {
							System.out.print(NAME_OF_FILE);
							String filename = lineReader.nextLine();
							System.out.print("Write size of the file> ");
							String size = lineReader.nextLine();
							System.out.print("Write permission of the file (Write or Read)> ");
							String permission = lineReader.nextLine();
							if (fileOperations.uploadFile(filename, size, permission, connectionID)) {
								System.out.println("FileDTO uploaded sucessfully.");
							} else {
								System.out.println(
										"The filename already exsits. Or you wrote a permission that isn't allowed.");
							}
						} else {
							System.out.println("Use: UPLOAD");
						}
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				case DOWNLOAD:
					if (connected && loggedIn) {
						if (enteredLine.amountOfWords() == 1) {
							System.out.print(NAME_OF_FILE);
							String filename = lineReader.nextLine();
							FileDTO fileDTO = fileOperations.getFile(filename, connectionID);

							if (!fileDTO.getName().equals("")) {
								System.out.println("You have downloaded the file " + fileDTO.getName());
								if (fileDTO.getPermission().equals("Write")) {
									System.out.println("You can update the file.");
								} else {
									System.out.println("You can not update the file.");
								}
							} else {
								System.out.println("Couldn't find the file");
							}
						} else {
							System.out.println("Use: DOWNLOAD");
						}
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				case UPDATE:
					if (connected && loggedIn) {
						if (enteredLine.amountOfWords() == 2) {
							if (enteredLine.getWordAtIndex(1).equalsIgnoreCase("NAME")) {
								update(lineReader, connectionID, "NAME",
										"Print the file old name, then the new name> ");
							} else if (enteredLine.getWordAtIndex(1).equalsIgnoreCase("SIZE")) {
								update(lineReader, connectionID, "SIZE",
										"Print the file old name, then the new size> ");
							} else if (enteredLine.getWordAtIndex(1).equalsIgnoreCase("NAMESIZE")) {
								update(lineReader, connectionID, "NAMESIZE",
										"Print the file old name, then the new name, then the new size> ");
							} else {
								System.out.println("Use: UPDATE Name or UPDATE Size or UPDATE NameSize");
							}
						}
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				case DELETE:
					if (connected && loggedIn) {
						if (enteredLine.amountOfWords() == 1) {
							System.out.print("Write name of the file> ");
							String filename = lineReader.nextLine();
							if (fileOperations.deleteFile(filename, connectionID)) {
								System.out.println("Delete successful");
							} else {
								System.out.println("Couldn't delete");
							}
						} else {
							System.out.println("Use: DELETE");
						}
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				case QUIT:
					if (connected && loggedIn) {
						fileOperations.disconnect(connectionID);
						lineReader.close();
						boolean forceUnexport = false;
						UnicastRemoteObject.unexportObject(myRemoteObj, forceUnexport);
						okToWrite = false;
					} else {
						couldNotHandleCommand(connected, loggedIn);
					}
					break;
				default:
					System.out.println("You entered a command that isn't allowed at all!");
					break;
				}
			} catch (IOException e) {
				errorExecution("A problem happend.", e);
			}
		}
	}

	private void update(Scanner lineReader, int connectionID, String updateOperation, String prompt)
			throws RemoteException {
		boolean retryWrite = true;
		String[] fileInfo = null;
		if (updateOperation.equals("NAMESIZE")) {
			fileInfo = new String[3];
		} else {
			fileInfo = new String[2];
		}
		while (retryWrite) {
			System.out.print(NAME_OF_FILE);
			fileInfo[0] = lineReader.nextLine();
			if (updateOperation.equals("NAME") || updateOperation.equals("NAMESIZE")) {
				System.out.print("Print new name> ");
				fileInfo[1] = lineReader.nextLine();
			} else if (updateOperation.equals("SIZE")) {
				System.out.print("Print new size> ");
				fileInfo[1] = lineReader.nextLine();
			}
			if (updateOperation.equals("NAMESIZE")) {
				System.out.print("Print new size> ");
				fileInfo[2] = lineReader.nextLine();
			}
			if (updateHelper(updateOperation, fileInfo, connectionID)) {
				System.out.println(UPDATE_SUCCESS);
			} else {
				System.out.println(COULD_NOT_UPDATE);
			}
			retryWrite = false;
		}
	}

	private boolean updateHelper(String updateOperation, String[] fileInfo, int connectionID) throws RemoteException {
		if (updateOperation.equals("NAME")) {
			if (fileOperations.updateFileName(fileInfo[0], fileInfo[1], connectionID)) {
				return true;
			}
		} else if (updateOperation.equals("SIZE")) {
			if (fileOperations.updateFileSize(fileInfo[0], fileInfo[1], connectionID)) {
				return true;
			}
		} else if (updateOperation.equals("NAMESIZE")) {
			if (fileOperations.updateFileNameAndSize(fileInfo[0], fileInfo[1], fileInfo[2], connectionID)) {
				return true;
			}
		}
		return false;
	}

	private void couldNotHandleCommand(boolean connected, boolean loggedIn) {
		if (!connected && !loggedIn) {
			System.out.println(CONNECT_FIRST);
		} else if (!connected && loggedIn) {
			System.out.println(LOGIN_FIRST);
		}
	}

	private void printInfo() {
		System.out.println("Commands:");
		System.out.println("1: Write 'List' to see all files in the filesystem.");
		System.out.println("2: Write 'Upload' to upload a file to the filesystem.");
		System.out.println("3: Write 'Download' tp get information about it.");
		System.out.println("For the 4th and 5th command the file need to have Write permission.");
		System.out.println("4.1: Write 'Update Name' to update a filename on a file.");
		System.out.println("4.2: Write 'Update Size' to update a filesize on a file.");
		System.out.println("4.3: Write 'Update NameSize' to update both filename and filesize on a file.");
		System.out.println("5: Write 'Delete' to delete a file.");
		System.out.println("6: Write 'Quit' to close the program.");
	}

	private void errorExecution(String msg, Exception e) {
		errorMessageHandler.showErrorMsg(msg);
		boolean onClient = true;
		logger.logException(e, onClient);
	}

	private class ConsoleOutput extends UnicastRemoteObject implements UserDTO {
		private static final long serialVersionUID = 5530960563002770279L;

		public ConsoleOutput() throws RemoteException {
		}

		@Override
		public void notice(String filename, String operation) throws RemoteException {
			if (operation.equals("download")) {
				System.out.println("\nThe file " + filename + " was downloaded.");
			} else if (operation.equals("delete")) {
				System.out.println("\nThe file " + filename + " was deleted.");
			} else if (operation.equals("size")) {
				System.out.println("\nThe file " + filename + " had it size changed.");
			}
			System.out.print(prompt);
		}

		@Override
		public void noticeNameChange(String oldFilename, String newFilename, String operation) throws RemoteException {
			if (operation.equals("name")) {
				System.out.println("\nThe file " + oldFilename + " name is now " + newFilename + ".");
			} else if (operation.equals("nameSize")) {
				System.out.println(
						"\nThe file " + oldFilename + " name is now " + newFilename + " and it's size was changed.");
			}
			System.out.print(prompt);
		}
	}
}
