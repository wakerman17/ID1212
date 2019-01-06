package server.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import common.LogHandler;
import server.net.HandleExcetion;
import server.net.Server;

/**
 * The view of the server. Mostly for handling exceptions.
 * 
 */
public class View{
	private LogHandler logger;
	
	/**
	 * Creates a new instance. 
	 * 
	 * @throws IOException if the LogHandler don't work.
	 */
	public View() throws IOException {
		logger = new LogHandler();
	}
	
	/**
	 * Shows the current IP and Portnumber for the Server and call the server were it'll mostly be in.
	 * @param server 
	 *  
	 */
	public void start(Server server) {
		ErrorHandler errorHandler = new ErrorHandler();
		try {
	        String ip;
			ip = InetAddress.getLocalHost().getHostAddress();
			String portNo = "8080"; 
	        System.out.printf("To connect write: connect %s %s%n", ip, portNo);
	        server.acceptSockets(errorHandler);
		} catch (UnknownHostException e) {
			errorHandler.handleExcetion(e);
		}
        
	}
	
	public class ErrorHandler implements HandleExcetion {
		@Override
		public void handleExcetion(Exception e) {
			boolean onClient = false;
			logger.logException(e, onClient);
		}
	}
}
