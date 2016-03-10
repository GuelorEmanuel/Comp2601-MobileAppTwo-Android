package com.android.comp2601.classchatapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.ConnectException;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private static ChatActivity instance; //globally accessible instance

    private TextView mTextViewMsgOutput;
    private EditText mEditViewMsgSend;
    private Button mButtonSendMsg;
    private Button mButtonConnect;
    private Button mButtonDisconnect;
    private ClientConnection mClientConnection;



    public static ChatActivity getInstance(){return instance;}

    public void enableButtons(Button mButton1, Button mButton2) {
        mButton1.setEnabled(true);
        mButton2.setEnabled(true);
    }

    public void enableButtons(Button mButton) {
        mButton.setEnabled(true);
    }

    public void disableButtons(Button mButton) {
        mButton.setEnabled(false);
    }

    public void disableButtons(Button mButton1, Button mButton2) {
        mButton1.setEnabled(false);
        mButton2.setEnabled(false);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mTextViewMsgOutput = (TextView) findViewById(R.id.msgRecievedView);
        mEditViewMsgSend = (EditText) findViewById(R.id.editTextMsgSend);
        mButtonSendMsg = (Button) findViewById(R.id.buttonSend);
        mButtonConnect = (Button) findViewById(R.id.buttonConnect);
        mButtonDisconnect = (Button) findViewById(R.id.buttonDisconnect);

        disableButtons(mButtonSendMsg, mButtonDisconnect);

        mTextViewMsgOutput.setMovementMethod(new ScrollingMovementMethod());
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableButtons(mButtonSendMsg, mButtonDisconnect);

                disableButtons(mButtonConnect);

                // connect to the server
                new ConnectionTask().execute("");

            }
        });

        mButtonSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = mEditViewMsgSend.getText().toString();


                //sends the message to the server
                if (mClientConnection != null) {
                    mClientConnection.sendMessage(message);
                }


                mEditViewMsgSend.setText("");
            }
        });

        mButtonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enableButtons(mButtonConnect);
                disableButtons(mButtonSendMsg, mButtonDisconnect);
                mClientConnection.stopClient();


            }
        });


    }

    private class ConnectionTask extends AsyncTask<String, String, ClientConnection> {

        @Override
        protected ClientConnection doInBackground(String... message) {


            mClientConnection = new ClientConnection(new ClientConnection.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });

            try {
                mClientConnection.run();
            } catch (ConnectException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.i(TAG, "Message : " + values[0]);
            String mAllText = mTextViewMsgOutput.getText().toString();
            mTextViewMsgOutput.setText("");
            if (!mAllText.isEmpty())
                mTextViewMsgOutput.setText(mAllText + "\n" + values[0].toString());
            else
                mTextViewMsgOutput.setText(values[0].toString());
        }

    }
}



