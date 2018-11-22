package client.startup;

import java.io.IOException;

import client.net.ClientCommunication;
import client.view.ErrorMessageHandler;
import client.view.LineWriter;

/**
 * Starts the client.
 * 
 */
public class Main {
	/**
	 * Starter for the client.
	 * 
	 * @param args Not used
	 */
	public static void main(String[] args) {
		try {
			ClientCommunication clientCommunication = new ClientCommunication();
			ErrorMessageHandler errorMessageHandler = new ErrorMessageHandler();
			new LineWriter(clientCommunication, errorMessageHandler).start();
		}
		catch (IOException ex) {
            System.out.println("Couldn't start the program.");
            ex.printStackTrace();
        }
	}

}
