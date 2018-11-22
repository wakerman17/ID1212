package client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

import client.view.LineWriter.ConsoleOutput;
import common.Constants;
import common.MsgType;

/**
 * The communication to the server.
 * 
 */
public class ClientCommunication implements Runnable {
	private final ByteBuffer msgFromServer = ByteBuffer.allocateDirect(Constants.MAX_MSG_LENGTH);
	private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
	private boolean connected;
	private boolean canWriteAndRead;
	private Selector selector;
	private boolean sendToServer;
	private SocketChannel socketChannel;
	InetSocketAddress serverAddress;
	ConsoleOutput consoleOutput;

	@Override
	public void run() {
		try {
			startConnection();
			initSelector();

			while (canWriteAndRead) {
				if (sendToServer) {
					socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
					sendToServer = false;
				}
				selector.select();
				for (SelectionKey key : selector.selectedKeys()) {
					selector.selectedKeys().remove(key);
					if (!key.isValid()) {
						System.out.println("isValid");
						continue;
					}
					if (key.isConnectable()) {
						completeConnection(key);
					} else if (key.isReadable()) {
						recvFromServer(key);
					} else if (key.isWritable()) {
						sendToServer(key);
					}
				}
			}
		} catch (IOException e) {
			consoleOutput.handleErrorMessage(e);
		}
		try {
			doDisconnect();
		} catch (IOException ex) {
			consoleOutput.handleErrorMessage(ex);
		}
	}

	private void recvFromServer(SelectionKey key) throws IOException {
		msgFromServer.clear();
		int numOfReadBytes = socketChannel.read(msgFromServer);
		if (numOfReadBytes == -1) {
			throw new IOException("Client disconnected");
		}
		String recvdString = getMessageFromBuffer();
		notifyGameStateReceived(recvdString);
	}

	private String getMessageFromBuffer() {
		msgFromServer.flip();
		byte[] bytes = new byte[msgFromServer.remaining()];
		msgFromServer.get(bytes);
		return new String(bytes);
	}

	/**
	 * Add a client to the server connection by connecting to it.
	 * 
	 * @param serverAddress The address to the server
	 * @param consoleOutput The observer to make the server send information to the
	 *                      view.
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void addClient(InetSocketAddress serverAddress, ConsoleOutput consoleOutput) throws IOException {
		this.serverAddress = serverAddress;
		this.consoleOutput = consoleOutput;
		new Thread(this).start();
	}

	private void initSelector() throws IOException {
		selector = Selector.open();
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
	}

	private void completeConnection(SelectionKey key) throws IOException {
		socketChannel.finishConnect();
		key.interestOps(SelectionKey.OP_READ);
	}

	/**
	 * Start the game by sending a startmessage.
	 * 
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void startGame() throws IOException {
		prepareToSendToServer(MsgType.START.toString());
	}

	/**
	 * Sends the guess.
	 * 
	 * @param guess The word or letter the user guess
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void guess(String guess) throws IOException {
		prepareToSendToServer(MsgType.GUESS.toString(), guess);
	}

	/**
	 * Send a message to disconnect from the server.
	 * 
	 * @throws IOException If the stream stop working (can't send the message)
	 */
	public void disconnect() throws IOException {
		connected = false;
		prepareToSendToServer(MsgType.DISCONNECT.toString());
	}

	private void prepareToSendToServer(String... parts) {
		StringJoiner joiner = new StringJoiner(Constants.MSG_SEPARATOR);
		for (String part : parts) { 
			joiner.add(part);
		}
		String message = joiner.toString();
		synchronized (messagesToSend) {
			messagesToSend.add(ByteBuffer.wrap(message.getBytes()));
		}
		sendToServer = true;
		selector.wakeup();
	}

	public void doDisconnect() throws IOException {
		socketChannel.close();
		socketChannel.keyFor(selector).cancel();
	}

	private void sendToServer(SelectionKey key) throws IOException {
		ByteBuffer msg;
		synchronized (messagesToSend) {
			while ((msg = messagesToSend.peek()) != null) {
				if (!connected) {
					canWriteAndRead = false;
				}
				socketChannel.write(msg); 
				if (msg.hasRemaining()) {
					return;
				}
				messagesToSend.remove();
			}
			key.interestOps(SelectionKey.OP_READ);
		}
	}

	private void startConnection() throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(serverAddress);
		connected = true;
		canWriteAndRead = true;
	}

	private void notifyGameStateReceived(String msg) {
		Executor pool = ForkJoinPool.commonPool();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				consoleOutput.handleGameState(msg);
			}
		});
	}
}