package android.william.hangman.common;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This class is responsible for the log.
 */
public class LogHandler {
	private static final String LOG_FILE_NAME_SERVER = "src\\android\\william\\hangman\\errors\\hangman-log-server.txt";
	private PrintWriter logFileServer;
	
	
	public LogHandler() throws IOException {
		logFileServer = new PrintWriter(new FileWriter(LOG_FILE_NAME_SERVER), true);
	}
	
	/**
	 * Writes log entries.
	 * 
	 * @param exception The exception that was thrown.
	 * @param errorOnClient If the error was on the client or not (the the server or another external source).
	 */
	public void logException(Exception exception, boolean errorOnClient) {
		StringBuilder logMsgBuilder = new StringBuilder();
		logMsgBuilder.append("Exception was thrown: ");
		logMsgBuilder.append(exception.getMessage());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		logMsgBuilder.append(dateFormat.format(cal.getTime()));
		if (!errorOnClient) {
			printException(exception, logFileServer, logMsgBuilder);
		}
	}
	private void printException(Exception exception, PrintWriter logFile, StringBuilder logMsgBuilder) {
		logFile.println(logMsgBuilder);
		exception.printStackTrace(logFile);
	}
}