package server.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import server.view.View.ErrorHandler;

/**
 * Get the calls to the client and let the ClientHandler handle the operation. 
 * 
 */
public class Server {
	private static final int LINGER_TIME = 5000;
	private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
	ErrorHandler errorHandler;
	private int portNo = 8080;
	private Selector selector;
	private ServerSocketChannel listeningSocketChannel;
	int clientID = 0;
	List<Integer> clientIDList = new ArrayList<>();

	void sendGameState(String gameState, int thisClientID) {
        messagesToSend.add(ByteBuffer.wrap(gameState.getBytes()));
        Client client;
        for (SelectionKey key : selector.keys()) {
        	client = (Client) key.attachment();
            if (client.getID() == thisClientID) {
            	if (key.channel() instanceof SocketChannel && key.isValid()) {
            		key.interestOps(SelectionKey.OP_WRITE);
            		break;
            	}
            }
        }
        selector.wakeup();
	}
	
	/**
	 * Waits for sockets/clients to connect or want to write to the server.
	 * 
	 * @param errorHandler Handles exceptions that may accrue.
	 */
	public void acceptSockets(ErrorHandler errorHandler) {
		try {
			selector = Selector.open();
			initListeningSocketChannel();
			while (true) {
				selector.select();
				
				for (SelectionKey key : selector.selectedKeys()) {
					selector.selectedKeys().remove(key);
					if (!key.isValid()) {
						continue;
					}
					if (key.isAcceptable()) { 
						startHandler(key);
					} else if (key.isReadable()) {
						recvFromClient(key);
					} else if (key.isWritable()) {
						sendToClient(key);
					} 
				}
			}
		} catch (Exception e) {
			errorHandler.handleExcetion(e);
			System.err.println("Server failure.");
		}
	}

	private void sendToClient(SelectionKey key) throws Exception {
        Client client = (Client) key.attachment();
        try {
        	client.gameStateToSend(messagesToSend.poll());
            client.sendGameState();
            key.interestOps(SelectionKey.OP_READ);
        } catch (IOException clientHasClosedConnection) {
            removeClient(key);
        }
		
	}

	private void initListeningSocketChannel() throws IOException {
		listeningSocketChannel = ServerSocketChannel.open();
		listeningSocketChannel.configureBlocking(false);
		listeningSocketChannel.bind(new InetSocketAddress(portNo));
		listeningSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
	
    private void startHandler(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        ClientHandler handler = new ClientHandler(this, clientChannel, errorHandler, clientID);
        clientIDList.add(0, clientID);
        clientChannel.register(selector, SelectionKey.OP_READ, new Client(handler, clientID++));
        
        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME); 
    }
    
    private void recvFromClient(SelectionKey key) throws IOException {
        Client client = (Client) key.attachment();
        try {
            client.handler.recvMsg(client.getID());
        } catch (IOException clientHasClosedConnection) {
            removeClient(key);
        }
    }
    
    private void removeClient(SelectionKey clientKey) throws IOException {
        clientKey.cancel();
    }
	
	private class Client {
	    private final ClientHandler handler;
	    private ByteBuffer byteBufferToSend;
	    private final int ID;

	    private Client(ClientHandler handler, int id) {
	        this.handler = handler;
	        ID = id;
	    }

	    private void gameStateToSend(ByteBuffer byteBuffer) {
	    	byteBufferToSend = byteBuffer;
	    }
	    
	    private int getID() {
	    	return ID;
	    } 
	    
	    private void sendGameState() throws Exception {
	    		handler.sendGameState(byteBufferToSend);
	    		byteBufferToSend.clear();
	    	}
	    }
	}