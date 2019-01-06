package client.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import client.view.LineWriter.ConsoleOutput;
import common.Message;
import common.MsgType;

/**
 * The communication to the server.
 * 
 */
public class ClientCommunication{
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private boolean connected;
    private Socket socket;
    
    /**
     * Add a client to the server connection by connecting to it.
     * 
     * @param serverAddress The address to the server
     * @param consoleOutput The observer to make the server send information to the view.
     * @throws IOException If the stream stop working (can't send the message)
     */
	public void addClient (String serverIP, String serverPort,  ConsoleOutput consoleOutput) throws IOException {
		socket = new Socket();
		InetSocketAddress serverAddress = new InetSocketAddress(serverIP, Integer.parseInt(serverPort));
		socket.connect(serverAddress, TIMEOUT_HALF_MINUTE);
		socket.setSoTimeout(TIMEOUT_HALF_HOUR);
		connected = true;
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
        new Thread(new ServerListener(consoleOutput)).start();
	}
	
	public void setUsername(String username) throws IOException {
		sendMessage(new Message(MsgType.USERNAME, username));
	}
	
	/**
	 * Start the game by sending a startmessage.
	 * 
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void startGame() throws IOException { 
		sendMessage(new Message(MsgType.START));
	}
	
	/**
	 * Sends the guess.
	 * 
	 * @param guess The word or letter the user guess
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void guess(String guess) throws IOException {
		sendMessage(new Message(MsgType.GUESS, guess));
	}
	
	/**
	 * Send a message to disconnect from the server.
	 * 
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void disconnect() throws IOException {
		sendMessage(new Message(MsgType.DISCONNECT));
		connected = false;
		socket.close();
	}
	
	private void sendMessage(Message message) throws IOException {
		toServer.writeObject(message);
		toServer.flush();
		toServer.reset();
	}
	
	
    private class ServerListener implements Runnable {
        private final OutputHandler outputHandler;

        private ServerListener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            try {
                while (true) {
                	Message msg = (Message) fromServer.readObject();
                	if (msg.getType() == MsgType.GAMESTATE) {
                		outputHandler.handleGameState(msg.getGameStateDTO());
                	} else if (msg.getType() == MsgType.DISCONNECT) {
                		outputHandler.handleDisconnect();
                	}
                }
            } catch (Exception connectionFailure) {
                if (connected) {
                    outputHandler.handleErrorMessage(connectionFailure);
                }
            }
        }
    }
}