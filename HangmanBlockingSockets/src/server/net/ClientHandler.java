/*
 * The MIT License
 *
 * Copyright 2017 Leif Lindbäck <leifl@kth.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
