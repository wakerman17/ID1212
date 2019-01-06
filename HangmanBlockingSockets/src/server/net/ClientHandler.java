package server.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;

import common.GameState;
import common.Message;
import common.MsgType;
import server.controller.Controller;
import server.net.Server;
import server.view.View.ErrorHandler;


/**
 * Handles all communication with one particular client.
 */
class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private ObjectInputStream fromClient;
    private ObjectOutputStream toClient;
    private boolean connected;
    private Controller serverController;
    ErrorHandler errorHandler;

    /**
     * Creates a new instance of a Client handler, it'll communicate with the client (a socket that've connected to the server) in a new thread for each client.
     *
     * @param clientSocket The socket to which this handler's client is connected.
     * @param errorHandler Handles the errors that may accrue and show them in an .txt file.
     */
    ClientHandler(Socket clientSocket, ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        this.clientSocket = clientSocket;
        connected = true;
        try {
			serverController = new Controller();
		} catch (IOException e) {
			errorHandler.handleExcetion(e);
			disconnectClient();
		}
    }
    
    @Override
    public void run() {
        try {
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
            toClient = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
        	errorHandler.handleExcetion(ioe);
			disconnectClient();
        }
        while (connected) {
        	try {
        		 Message message = (Message) fromClient.readObject();
        		 switch (message.getType()) {
        		 case START: 
        	         sendGameState(serverController.newWord());
        			 break;
        		 case GUESS:
        			 sendGameState(serverController.guess(message.getBody()));
        			 break;
        		 case DISCONNECT:
        			 disconnectClient();
				default:
					break;
        		 }
        		
			} catch (ClassNotFoundException  | IOException e) {
				errorHandler.handleExcetion(e);
				disconnectClient();
			}
        }
    }
    
	private void sendGameState(GameState gameState) {
        try {
        	Message msg = new Message(MsgType.GAMESTATE, gameState);
            toClient.writeObject(msg);
            toClient.flush();
            toClient.reset();
        } catch (IOException ioe) {
        	errorHandler.handleExcetion(ioe);
            throw new UncheckedIOException(ioe); 
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
