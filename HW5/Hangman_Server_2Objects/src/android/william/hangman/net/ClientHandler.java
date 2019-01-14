package android.william.hangman.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;

import android.william.hangman.common.GameStateDTO;
import android.william.hangman.common.Message;
import android.william.hangman.common.MsgType;
import android.william.hangman.controller.Controller;
import android.william.hangman.view.View.ErrorHandler;


/**
 * Handles all communication with one particular chat client.
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
     */
    ClientHandler(Socket clientSocket) {
        //this.errorHandler = errorHandler;
        this.clientSocket = clientSocket;
        connected = true;
        try {
			serverController = new Controller();
		} catch (IOException e) {
			//errorHandler.handleExcetion(e);
			disconnectClient();
		}
    }
    
    @Override
    public void run() {
        try {
        	toClient = new ObjectOutputStream(clientSocket.getOutputStream());
            fromClient = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioe) {
        	//errorHandler.handleExcetion(ioe);
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
				//errorHandler.handleExcetion(e);
				disconnectClient();
			}
        }
    }
    
	private void sendGameState(GameStateDTO gameState) {
        try {
        	Message msg = new Message(MsgType.GAMESTATE, gameState);
        	System.out.println("word = " + gameState.getWord());
            toClient.writeObject(msg);
            toClient.flush();
            toClient.reset();
        } catch (IOException ioe) {
        	//errorHandler.handleExcetion(ioe);
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
