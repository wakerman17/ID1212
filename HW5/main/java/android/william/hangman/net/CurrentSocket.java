package android.william.hangman.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Stores the current Socket
 *
 */
public class CurrentSocket implements Serializable {
    private static Socket socket;
    private static ObjectOutputStream toServer;
    private static ObjectInputStream fromServer;

    /**
     * Set the socket and creates ObjectOutputStream and ObjectInputStream with it.
     *
     * @param socket The socket to set
     * @throws IOException
     */
    public static void setSocket(Socket socket) throws IOException {
        CurrentSocket.socket = socket;
        System.setProperty("http.keepAlive", "false");
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());
    }

    /**
     *
     * @return The socket
     */
    public static Socket getSocket(){
        return socket;
    }

    /**
     *
     * @return The ObjectOutputStream
     */
    public static ObjectOutputStream getOutputStream(){
        return toServer;
    }

    /**
     *
     * @return The ObjectInputStream
     */
    public static ObjectInputStream getInputStream(){
        return fromServer;
    }
}
