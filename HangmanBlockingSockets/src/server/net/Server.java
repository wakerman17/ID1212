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
