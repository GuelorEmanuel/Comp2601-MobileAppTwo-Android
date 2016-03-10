package com.android.comp2601.weatherdatajson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.io.OutputStreamWriter;

/*
Add permissions Internet permission to Android manifest

 */


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String ERROR400 = "{\"cod\":400,\"message\":\"Failed to get response data\"}";

    private static String API_DIRECTION_UP = "up";
    private static String API_DIRECTION_DOWN = "down";
    private static String API_DIRECTION_LEFT = "left";
    private static String API_DIRECTION_RIGHT = "right";

    EditText mEditTextName;
    Button mButtonSendLocation;
    Button mButtonClearLocation;
    Button mButtonUp;
    Button mButtonDown;
    Button mButtonLeft;
    Button mButtonRight;

    String mName;

    //private String mServerIpAddress = "http://192.168.1.221:3000"; //localhost for emulator use
    private String mServerIpAddress = "http://10.0.2.2:3001"; //localhost for emulator use
    //see http://developer.android.com/tools/devices/emulator.html#networkaddresses

    ProgressDialog progress;

    HttpURLConnection mConnection = null;
    BufferedReader mReader = null;

    private void requestMove(String aDirection){

        mName = mEditTextName.getText().toString().trim();
        if(mName == null || mName.isEmpty()) return;

        //String mQueryString = mServerIpAddress + "/move?word=" + mName + "&direction=" + aDirection; // JSONString
        String mQueryString = mServerIpAddress + "/move";
        String jsonData = "{\"word\":\"" + mName + "\",\"direction\":\"" +aDirection+ "\"}";
        Log.i(TAG, "SERVER REQUEST: " + mServerIpAddress);


        //

        //progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
        //        getString(R.string.dialog_message), true);

        new ServerRequestTask().execute(mQueryString,jsonData);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextName = (EditText) findViewById(R.id.edit_location);
        mButtonSendLocation = (Button) findViewById(R.id.button_send_location);
        mButtonClearLocation = (Button) findViewById(R.id.button_clear);
        mButtonUp = (Button) findViewById(R.id.buttonUp);
        mButtonDown = (Button) findViewById(R.id.buttonDown);
        mButtonLeft = (Button) findViewById(R.id.buttonLeft);
        mButtonRight = (Button) findViewById(R.id.buttonRight);



        mButtonSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mEditTextName.getText().toString().trim();

                String mQueryString = mServerIpAddress + "/add";
                String jsonData = "{\"word\":\"" + mName + "\"}";

                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ServerRequestTask().execute(mQueryString, jsonData);

            }
        });

        mButtonClearLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditTextName.setText("");
            }
        });

        mButtonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requestMove(API_DIRECTION_UP);}
        });

        mButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requestMove(API_DIRECTION_DOWN);}
        });

        mButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requestMove(API_DIRECTION_RIGHT);}
        });

        mButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requestMove(API_DIRECTION_LEFT);}
        });


    }

    private class ServerRequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            URL mRequestUrl = null;
            StringBuffer mStrBuffer = null;
            try {
                mRequestUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                mConnection = (HttpURLConnection) mRequestUrl.openConnection();
/**/
                mConnection.setRequestMethod("POST");
                mConnection.setRequestProperty("Content-Type", "application/json");
                mConnection.setDoOutput(true);
                mConnection.setChunkedStreamingMode(0);
                OutputStreamWriter out = new   OutputStreamWriter(mConnection.getOutputStream());
                out.write(params[1]);
                out.flush();
                out.close();



                InputStream mInStream = new BufferedInputStream(mConnection.getInputStream());

                mReader = new BufferedReader(new InputStreamReader(mInStream));

                mStrBuffer = new StringBuffer();

                String line = "";

                while ((line = mReader.readLine()) != null) {
                    mStrBuffer.append(line);
                }

            } catch (UnknownHostException e){
                e.printStackTrace();
                Log.i(TAG, getString(R.string.error_unknownhost));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (mConnection != null) {
                    mConnection.disconnect();
                }
                try {
                    if (mReader != null) {
                        mReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.i(TAG, "Request URl: " + params[0]);

            if (mStrBuffer != null){
                return mStrBuffer.toString();
            } else {
                return ERROR400;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject mJSONResponseData = null;
            try {
                 mJSONResponseData = new JSONObject(result);
                Log.i(TAG, mJSONResponseData.toString());
                progress.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

