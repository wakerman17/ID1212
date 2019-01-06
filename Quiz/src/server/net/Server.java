package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.controller.Controller;
import server.util.Constants;
import server.view.View.ErrorHandler;

/**
 * Receives messages. All communication to/from any
 * node pass this server.
 */
public class Server {
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    ErrorHandler errorHandler;
    private int portNo = 8080;
    private Controller serverController;
    private AllClientHandlersInAGame allClientHandlers;
    private ClientHandler[] waitingClients = new ClientHandler[Constants.AMOUNT_OF_PLAYERS];
    
    /**
     * Waits for sockets/clients to connect and accepts.
     * 
     * @param errorHandler Handles exceptions that may occur.
     */
    public void acceptSockets(ErrorHandler errorHandler) {
        try {
        	this.errorHandler = errorHandler;
			@SuppressWarnings("resource")
			ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
        	errorHandler.handleExcetion(e);
        }
    }
    
    /**
     * Call when the the first word haven been written. Removes the specified ClientHandler from the waitingClients array.
     * 
     * @param index The index where this ClientHandler is
     */
	public void disconnect(int index) {
		waitingClients[index] = null;
	}

    private void startHandler(Socket clientSocket) throws IOException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        Thread handlerThread = null;
        int i; 
        for (i = 0; i < waitingClients.length; i++) {
        	if (waitingClients[i] == null && i == 0) {
            	serverController = new Controller();
            	allClientHandlers = new AllClientHandlersInAGame();
        	} 
        	if (waitingClients[i] == null) {
        		waitingClients[i] = new ClientHandler(clientSocket, errorHandler, serverController, allClientHandlers, i, this);
            	handlerThread = new Thread(waitingClients[i]);
            	break;
        	} 
        }
        if (i == waitingClients.length - 1) {
        	serverController = null;
        	allClientHandlers = null;
        	for (i = 0; i < waitingClients.length; i ++) {
        		waitingClients[i] = null;
        	}
        }
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
}
