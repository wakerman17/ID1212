package android.william.hangman.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.william.hangman.net.ClientConnector;
import android.william.hangman.R;

/**
 * Responsible to the Connect view logic
 *
 */
public class Connect extends Activity {
    TextView response;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear, buttonQuit;
    Intent nextWindow;
    ResponseReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        editTextAddress = findViewById(R.id.addressEditText);
        editTextPort = findViewById(R.id.portEditText);
        buttonConnect = findViewById(R.id.connectButton);
        buttonClear = findViewById(R.id.clearButton);
        response = findViewById(R.id.responseTextView);
        buttonQuit = findViewById(R.id.quitButton);

        buttonConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nextWindow = new Intent(Connect.this, Game.class);
                final Intent clientConnector = new Intent(Connect.this, ClientConnector.class);
                clientConnector.putExtra("addr", editTextAddress.getText().toString());
                try {
                    clientConnector.putExtra("port", Integer.parseInt(editTextPort.getText().toString()));
                } catch (NumberFormatException e) {
                    response.setText("Only numbers allowed in portnumber field.");
                }
                response.setText("Connecting...");
                startService(clientConnector);
            }
        });

        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });

        buttonQuit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("newWindow"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                errorReceiver, new IntentFilter("IOException"));
    }


    private BroadcastReceiver errorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            response.setText("Couldn't connect to that Server. \nMake sure it's online or if you have written wrong IP or port.");
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(nextWindow);
        }
    };

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.mamlambo.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(nextWindow);
        }
    }
}
