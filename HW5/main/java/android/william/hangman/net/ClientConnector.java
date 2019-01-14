package android.william.hangman.net;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import android.william.hangman.common.MsgType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handles the connection to the server
 *
 */
public class ClientConnector extends Service {
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    Socket socket = null;
    InetSocketAddress serverAddress = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final ClientConnector me = this;
        Thread clientConnectThread = new Thread() {
            public void run() {
                try {
                    serverAddress = new InetSocketAddress(intent.getStringExtra("addr"),
                            intent.getIntExtra("port", 0));
                    socket = new Socket();
                    socket.connect(serverAddress, TIMEOUT_HALF_MINUTE);
                    CurrentSocket.setSocket(socket);
                    Intent newIntent = new Intent("newWindow");
                    LocalBroadcastManager.getInstance(me).sendBroadcast(newIntent);
                    listen();
                } catch (IOException e) {
                    //e.printStackTrace();
                    Intent newIntent = new Intent("IOException");
                    LocalBroadcastManager.getInstance(me).sendBroadcast(newIntent);
                }
            }

            private void listen() {
                try {
                    toServer = CurrentSocket.getOutputStream();
                    fromServer = CurrentSocket.getInputStream();
                    while (true) {
                        android.william.hangman.common.Message msg = (android.william.hangman.common.Message) fromServer.readObject();
                        //Log.d("DEBUGGING:::::::", "msg.getType() = " + msg.getType().toString());
                        //Log.d("DEBUGGING:::::::", "msg.getType() = " + msg.getGameState().toString());
                        if (msg.getType() == MsgType.GAMESTATE) {
                            Intent broadcastIntent = new Intent("gameStateIntent");
                            broadcastIntent.putExtra("message", msg);
                            //broadcastIntent.putExtra("gameStateType", msg.getType().toString());
                            //broadcastIntent.putExtra("gameState", msg.getGameState());
                            LocalBroadcastManager.getInstance(me).sendBroadcast(broadcastIntent);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientConnectThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

