package server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ForkJoinPool;

import common.Constants;
import common.MsgType;
import server.controller.Controller;
import server.view.View.ErrorHandler;

/**
 * Handles all communication with one particular chat client.
 */
class ClientHandler implements Runnable {
	private final SocketChannel clientSocketChannel;
	private Controller serverController;
	private String recvdString;
	private String currentWordState;
	ErrorHandler errorHandler;
	Server server;
	private final int ID;
	private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(Constants.MAX_MSG_LENGTH);

	/**
	 * Creates a new instance of a Client handler, it'll communicate with the client
	 * (a socket that've connected to the server) in a new thread for each client.
	 *
	 * @param clientChannel The socket to which this handler's client is connected.
	 * @param errorHandler  Handles the errors that may accrue and show them in an
	 *                      .txt file.
	 * @param clientID 
	 */
	ClientHandler(Server server, SocketChannel clientSocketChannel, ErrorHandler errorHandler, int clientID) {
		ID = clientID;
		this.server = server;
		this.errorHandler = errorHandler;
		this.clientSocketChannel = clientSocketChannel;
		try {
			serverController = new Controller();
		} catch (IOException e) {
			errorHandler.handleExcetion(e);
			disconnectClient();
		}
	}
	
	int getID() {
		return ID;
	}

	@Override
	public void run() {
		String[] messageParts = recvdString.split(Constants.MSG_SEPARATOR);
		String messageType = messageParts[0];
		if (messageType.equals(MsgType.START.toString())) {
			server.sendGameState(serverController.newWord(), ID);
		} else if (messageType.equals(MsgType.GUESS.toString())) {
			server.sendGameState(serverController.guess(messageParts[1]), ID);
		} else if (messageType.equals(MsgType.DISCONNECT.toString())) {
			disconnectClient();
		}
	}

	String getCurrentWordState() {
		return currentWordState;
	}

	int disconnectClient() {
		try {
			clientSocketChannel.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return ID;
	}

	/**
	 * Reads a message from the connected client, then submits a task to the default
	 * <code>ForkJoinPool</code>. That task which will handle the received message.
	 * @param id 
	 *
	 * @throws IOException If failed to read message
	 */
	void recvMsg(int id) throws IOException {
		msgFromClient.clear();
		int numOfReadBytes;
		numOfReadBytes = clientSocketChannel.read(msgFromClient);
		if (numOfReadBytes == -1) {
			throw new IOException("Client disconnected");
		}
		recvdString = extractMessageFromBuffer();
		ForkJoinPool.commonPool().execute(this);
	}

	private String extractMessageFromBuffer() {
		msgFromClient.flip();
		byte[] bytes = new byte[msgFromClient.remaining()];
		msgFromClient.get(bytes);
		return new String(bytes);
	}

	public void sendGameState(ByteBuffer msg) throws Exception {
		clientSocketChannel.write(msg);
		if (msg.hasRemaining()) {
			throw new Exception("Could not send message");
		}
	}
}
