package server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.GameStateDTO;
import common.Message;
import common.MsgType;
import server.controller.Controller;
import server.view.View.ErrorHandler;

/**
 * Handles all communication with one particular client.
 */
class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private boolean connected;
	private volatile boolean connectedToPlayer = false;
	private AllClientHandlersInAGame allClientHandlers;
	private Controller serverController;
	private final int ID;
	ErrorHandler errorHandler;
	private volatile GameStateDTO gameState;
	private Server server;

	/**
	 * Creates a new instance of a Client handler, it'll communicate with the client
	 * (a socket that've connected to the server) in a new thread for each client.
	 *
	 * @param clientSocket      The socket to which this handler's client is
	 *                          connected.
	 * @param errorHandler      Handles the errors that may accrue and show them in
	 *                          an .txt file.
	 * @param serverController  The reference to the Controller
	 * @param allClientHandlers Reference to AllClientHandlersInAGame
	 * @param id                The ID of this clientHandler.
	 * @param server            Reference to the server.
	 */
	ClientHandler(Socket clientSocket, ErrorHandler errorHandler, Controller serverController,
			AllClientHandlersInAGame allClientHandlers, int id, Server server) {
		this.clientSocket = clientSocket;
		this.errorHandler = errorHandler;
		this.serverController = serverController;
		this.allClientHandlers = allClientHandlers;
		this.server = server;
		ID = id;
		connected = true;
	}

	void clientConnection(ClientHandler otherClientHandler) {

	}

	@Override
	public void run() {
		boolean firstStart = true;
		try {
			fromClient = new ObjectInputStream(clientSocket.getInputStream());
			toClient = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException ioe) {
			errorHandler.handleExcetion(ioe);
			disconnectClient();
		}
		while (connected) {
			try {
				if (firstStart) {
					allClientHandlers.addClientHandler(this);
					firstStart = false;
				}
				Message message = (Message) fromClient.readObject();
				switch (message.getType()) {
				case USERNAME:
					String username = message.getBody();
					serverController.addUsername(username, ID);
					break;
				case START:
					gameState = serverController.startGame();
					if (gameState != null) {
						allClientHandlers.broadcast(gameState);
						
					}
					break;
				case GUESS:
					String guess = message.getBody();
					gameState = serverController.guess(guess, ID);
					if (serverController.clientWin()) {
						allClientHandlers.broadcast(gameState);
					} else {
						sendMessage(new Message(MsgType.GAMESTATE, gameState));
					}
					break;
				case DISCONNECT:
					if (connectedToPlayer) {
						allClientHandlers.sendDisconnectMessage(this);
					} else {
						server.disconnect(ID);
					}
					allClientHandlers.disconnect(this);
					serverController.removeClient(ID);
					disconnectClient();
				default:
					break;
				}

			} catch (ClassNotFoundException | IOException e) {
				errorHandler.handleExcetion(e);
				disconnectClient();
			}
		}
	}
	
	/**
	 * Send the message to the client that is handled.
	 * 
	 * @param msg The message to send
	 */
	void sendMessage(Message msg) {
		try {
			if (msg.getType() == MsgType.GAMESTATE && connectedToPlayer == false) {
				connectedToPlayer = true;
			}
			toClient.writeObject(msg);
			toClient.flush();
			toClient.reset();
		} catch (IOException ioe) {
			errorHandler.handleExcetion(ioe);
		}
	}

	private void disconnectClient() {
		try {
			clientSocket.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		connected = false;
	}
}
