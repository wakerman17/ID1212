package android.william.hangman.net;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.william.hangman.common.Message;
import android.william.hangman.common.MsgType;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Sends messages to the server
 *
 */
public class ClientSender extends AsyncTask<String, Void, Void>  {

    @Override
    protected Void doInBackground(String... strings) {
        final ClientSender me = this;
        try {
            ObjectOutputStream toServer = CurrentSocket.getOutputStream();
            if (strings[0].equals("START")) {
                toServer.writeObject(new Message(MsgType.START));
            } else if (strings[0].equals("GUESS")) {
                toServer.writeObject(new Message(MsgType.GUESS, strings[1]));
            }
            toServer.flush();
            toServer.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
