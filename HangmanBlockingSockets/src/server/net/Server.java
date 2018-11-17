package server.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.view.View.ErrorHandler;

/**
 * Receives chat messages and broadcasts them to all chat clients. All communication to/from any
 * chat node pass this server.
 */
public class Server {
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    ErrorHandler errorHandler;
    private int portNo = 8080;
    
    /**
     * Waits for sockets/clients to connect and accepts.
     * 
     * @param errorHandler Handles exceptions that may accure.
     */
    public void acceptSockets(ErrorHandler errorHandler) {
        try {
        	this.errorHandler = errorHandler;
			ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
        	errorHandler.handleExcetion(e);
        }
    }

    private void startHandler(Socket clientSocket) throws IOException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler clientHandler = new ClientHandler(clientSocket, errorHandler);
        Thread handlerThread = new Thread(clientHandler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
}
