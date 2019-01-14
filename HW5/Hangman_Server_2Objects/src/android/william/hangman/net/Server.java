package android.william.hangman.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.william.hangman.view.View.ErrorHandler;

/**
 * Receives chat messages and broadcasts them to all chat clients. All communication to/from any
 * chat node pass this server.
 */
public class Server {
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    ErrorHandler errorHandler;
    private int portNo = 8080;
    
    public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.acceptSockets();
	}
    
    /**
     * Waits for sockets/clients to connect and accepts.
     * 
     * @param errorHandler Handles exceptions that may accure.
     */
    public void acceptSockets() {
        try {
        	//this.errorHandler = errorHandler;
        	String ip = InetAddress.getLocalHost().getHostAddress();
			System.out.printf("IP: %s. Port: %s%n", ip, portNo);
        	//this.errorHandler = errorHandler;
        	@SuppressWarnings("resource")
			ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                //System.out.print("clientSocket is accepted " + clientSocket.getPort());
                startHandler(clientSocket);
            }
        } catch (IOException e) {
        	System.err.println("Server failure.");
			e.printStackTrace();
        	//errorHandler.handleExcetion(e);
        }
    }

    private void startHandler(Socket clientSocket) throws IOException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        Thread handlerThread = new Thread(clientHandler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }
}
