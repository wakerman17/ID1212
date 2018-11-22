package server.startup;

import java.io.IOException;

import server.net.Server;
import server.view.View;

public class Main {

	public static void main(String[] args) {
		try {
			Server server = new Server();
			new View().start(server);
		} catch (IOException e) {
			 System.out.println("Couldn't start the server.");
	         e.printStackTrace();
		}
	}
}
