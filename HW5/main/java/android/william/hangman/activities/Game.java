package android.william.hangman.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.william.hangman.common.CorrectLettersDTO;
import android.william.hangman.common.GameStateDTO;
import android.william.hangman.common.Message;
import android.william.hangman.common.MsgType;
import android.william.hangman.net.ClientSender;
import android.william.hangman.R;

import java.util.Set;

/**
 * Handle view logic for the Game view
 *
 */
public class Game extends AppCompatActivity {

    TextView gameState, errors;
    EditText guess;
    Button buttonGuess, newWordButton, buttonQuit, buttonClear;
    boolean noWord = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_game);
        gameState = findViewById(R.id.gameStateTextView);
        errors = findViewById(R.id.responseTextView);
        guess = findViewById(R.id.guessEditText);
        buttonClear = findViewById(R.id.clearButton);
        buttonGuess = findViewById(R.id.guessButton);
        newWordButton = findViewById(R.id.newWordButton);
        buttonQuit = findViewById(R.id.quitButton);

        newWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new ClientSender().execute("START");
            }
        });

        buttonGuess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!guess.getText().toString().equals("")) {
                    errors.setText("");
                    new ClientSender().execute("GUESS", guess.getText().toString());
                } else {
                    errors.setText("You have to write a guess");
                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                errors.setText("");
            }
        });

        buttonQuit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        IntentFilter filter = new IntentFilter(Connect.ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("gameStateIntent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                errorReceiver, new IntentFilter("IOException"));
    }

    private BroadcastReceiver errorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            errors.setText("The server is disconnected");
        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // react to local broadcast message
            Message msg = (Message) intent.getSerializableExtra("message");
            if (msg.getType().equals(MsgType.GAMESTATE)) {
                GameStateDTO gameState = msg.getGameState();
                if (noWord) {
                    guess.setVisibility(View.VISIBLE);
                    buttonGuess.setVisibility(View.VISIBLE);
                    buttonClear.setVisibility(View.VISIBLE);
                    noWord = false;
                }
                new PrintGameState().execute(gameState);
                guess.getText().clear();
            }
        }
    };

    private void endGame() {
        guess.setVisibility(View.INVISIBLE);
        buttonGuess.setVisibility(View.INVISIBLE);
        buttonClear.setVisibility(View.INVISIBLE);
        noWord = true;
    }

    private class PrintGameState extends AsyncTask<GameStateDTO, Void, String> {
        final String UNKNOWN = "-";


        @Override
        protected String doInBackground(GameStateDTO... gameState) {
            StringBuilder resultBuilder = new StringBuilder();
            if (!gameState[0].needToChangeWord()) {
                StringBuilder wordBuilder = new StringBuilder(gameState[0].getWordSize());
                for (int i = 0; i < gameState[0].getWordSize(); i++) {
                    wordBuilder.append(UNKNOWN);
                }
                CorrectLettersDTO[] correctLetters = gameState[0].getCorrectLetters();
                for (CorrectLettersDTO correctLetter : correctLetters) {
                    if (correctLetter != null) {
                        int[] rightLetterIndices = correctLetter.getIndices();
                        for (int index : rightLetterIndices) {
                            wordBuilder.setCharAt(index, correctLetter.getLetter());
                        }
                    }
                }
                resultBuilder.append("Word: " + wordBuilder.toString() + ", Tries left: " + gameState[0].getTries()
                        + ", Score: " + gameState[0].getScore());
                Set<Character> wrongLetters = gameState[0].getWrongLetters();
                if (wrongLetters.size() != 0) {
                    String wrongLettersString = wrongLetters.toString();
                    wrongLettersString = wrongLetters.toString().substring(1, wrongLettersString.length() - 1);
                    resultBuilder.append(", Wrong letters: " + wrongLettersString);
                } else {
                    resultBuilder.append("\n");
                }
            } else {
                if(gameState[0].didILose()) {
                    resultBuilder.append("Sorry, you lost. ");
                } else {
                    resultBuilder.append("Congrats, you won. ");
                }
                resultBuilder.append("Word: " + gameState[0].getWord() + ", Score: " + gameState[0].getScore());
                noWord = true;
            }
            return resultBuilder.toString();
        }

        @Override
        protected void onPostExecute(String gameStateString) {
            gameState.setText("");
            if(noWord) {
                endGame();
            }
            gameState.setText(gameStateString);
        }
    }
}
