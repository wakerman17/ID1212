package android.william.hangman.startup;

import java.io.IOException;

import android.william.hangman.net.Server;
import android.william.hangman.view.View;

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
