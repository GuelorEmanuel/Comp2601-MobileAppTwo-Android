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

/*
Notes: You need Internet permission in the Android manifest

To retrieve JSON weather data from openweathermap.org.
you will need an APIIDD from openweathermap.org.

Use an ASYNCTASK to retrieve JSON -  you cannot make an HTTP connections
on the main UI thread.

*/


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String APPAPI ="YOUR_APP_ID_HERE";
    private static final String ERROR401 = "{\"cod\":401,\"message\":\"Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.\"}";

    EditText mEditTextLocation;
    Button mButtonSendLocation;
    Button mButtonClearLocation;
    Button mButtonUp;
    Button mButtonDown;
    Button mButtonLeft;
    Button mButtonRight;
    String mLocationName;

    ProgressDialog progress;

    HttpURLConnection mConnection = null;
    BufferedReader mReader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextLocation = (EditText) findViewById(R.id.edit_location);
        mButtonSendLocation = (Button) findViewById(R.id.button_send_location);
        mButtonClearLocation = (Button) findViewById(R.id.button_clear);
        mButtonDown  = (Button) findViewById(R.id.buttonDown);
        mButtonUp    = (Button) findViewById(R.id.buttonUp);
        mButtonLeft  = (Button) findViewById(R.id.buttonLeft);
        mButtonRight = (Button) findViewById(R.id.buttonRight);



        mButtonDown.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String mDown     ="http://192.168.10.221:3000/move?word="+mLocationName+"&direction=down";
                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ReadWeatherDataTask().execute(mDown);

            }

        });
        mButtonUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String mUp     ="http://192.168.10.221:3000/move?word="+mLocationName+"&direction=up";
                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ReadWeatherDataTask().execute(mUp);
            }

        });

        mButtonLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String mLeft     ="http://192.168.10.221:3000/move?word="+mLocationName+"&direction"+"=left";
                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ReadWeatherDataTask().execute(mLeft);
            }

        });

        mButtonRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String mRight     ="http://192.168.10.221:3000/move?word="+mLocationName+"&direction=right";
                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ReadWeatherDataTask().execute(mRight);
            }

        });
        mButtonSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationName = mEditTextLocation.getText().toString();

                //String mWeatherString = "http://api.openweathermap.org/data/2.5/weather?q=" + mLocationName + "&units=metric&appid=" + APPAPI;
                String mCanvasApp     ="http://192.168.10.221:3000/add?word="+ mLocationName;
                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);
                new ReadWeatherDataTask().execute(mCanvasApp);

            }
        });

        mButtonClearLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditTextLocation.setText("");
            }
        });

    }

    private class ReadWeatherDataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            URL mWeatherUrl = null;
            StringBuffer mStrBuffer = null;
            try {
                mWeatherUrl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                mConnection = (HttpURLConnection) mWeatherUrl.openConnection();

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

            Log.i(TAG, "Weather URl: " + params[0]);

            if (mStrBuffer != null){
                return mStrBuffer.toString();
            } else {
                return ERROR401;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject mJSONWeatherData = null;
            Intent mIntent;

          try {
                mJSONWeatherData = new JSONObject(result);
                Log.i(TAG, mJSONWeatherData.toString());
                progress.dismiss();
               if (mJSONWeatherData.getInt("cod") != 200){

                   String mError = mJSONWeatherData.getString("message");
                   mIntent = new Intent(MainActivity.this, ErrorActivity.class);
                   Bundle BUNDLE_Message = new Bundle();
                   BUNDLE_Message.putString(States.STATE_ERROR, mError);
                   mIntent.putExtra(States.STATE_ERROR_BUNDLE, BUNDLE_Message);

                   startActivity(mIntent);
               } else {

                   mIntent = new Intent(MainActivity.this, WeatherActivity.class);
                   Bundle BUNDLE_Weather = new Bundle();
                   BUNDLE_Weather.putString(States.STATE_WEATHER, result);
                   mIntent.putExtra(States.STATE_WEATHER_BUNDLE, BUNDLE_Weather);

                   //startActivity(mIntent);
               }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}


