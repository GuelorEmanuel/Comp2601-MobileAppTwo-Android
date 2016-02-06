package com.android.comp2601.weatherdatajson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
Notes: You need Internet permission set in the Android manifest

To retrieve JSON weather data from openweathermap.org.
you will need an APPID from openweathermap.org.

Use an ASYNCTASK to retrieve JSON -  you cannot make an HTTP connections
on the main UI thread.

*/


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String APPAPI ="f6b6aed7d912676167029255a9e0444c"; //<--TO DO
    private static final String ERROR401 = "{\"cod\":401,\"message\":\"Invalid API key. Please see http://openweathermap.org/faq#error401 for more info.\"}";

    EditText mEditTextLocation;
    Button mButtonSendLocation;
    Button mButtonClearLocation;
    String mLocationName;

    ProgressDialog progress;

    HttpURLConnection mConnection = null;
    BufferedReader mReader = null;
    StringBuffer mStrBuffer = null;
    FileDownloadTask openWeather;


    private class FileDownloadTask extends AsyncTask <Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            String mWeatherString = "http://api.openweathermap.org/data/2.5/weather?q=" + mLocationName + "&units=metric&appid=" + APPAPI;
            URL mWeatherUrl = null;


            try {
                mWeatherUrl = new URL(mWeatherString);
            }
            catch (MalformedURLException e) {
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
                    System.out.println(mStrBuffer.toString());
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

            /*if (mStrBuffer != null){
                Log.i(TAG, mStrBuffer.toString());
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(mStrBuffer.toString());

                    System.out.println("TEST: "+ jsonObj.get("coord"));
                }catch (JSONException e){
                    Log.e("Failed", "Unexpected JSON exception", e);
                }*/



           /* } else {
                Log.i(TAG, "ERROR: not response data received");
            }*/

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected void onPostExecute(Void result){
            //executed on main UI thread
            int unfinishedTasks = 0;

            if(!( openWeather.getStatus() == AsyncTask.Status.FINISHED)){
                    unfinishedTasks++;
                System.out.println("NOT FINISHEDDDDDDDDDDDDDD!");
            }
            if (unfinishedTasks == 1){
                //We are all done. 1 Because its the current one that hasnt finished post execute
                System.out.println("FFFFFFFINISHEDDDDDDDDDD!");
                Bundle weatherBundle = new Bundle();
                Intent weatherIntent = new Intent(MainActivity.this, WeatherActivity.class);
                //obtain data passed to intent
                weatherBundle.putString(States.STATE_WEATHER_BUNDLE, mStrBuffer.toString());
                weatherIntent.putExtras(weatherBundle);
                progress.dismiss();
                startActivity(weatherIntent);


            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextLocation = (EditText) findViewById(R.id.edit_location);
        mButtonSendLocation = (Button) findViewById(R.id.button_send_location);
        mButtonClearLocation = (Button) findViewById(R.id.button_clear);


        mButtonSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationName = mEditTextLocation.getText().toString();


                progress = ProgressDialog.show(MainActivity.this, getString(R.string.dialog_title),
                        getString(R.string.dialog_message), true);

                //TO DO: Launch AsyncTask to get weather data
                if(isNetworkAvailable()){
                    Log.i(TAG, "starting file download Task");
                    openWeather = new FileDownloadTask();
                    openWeather.execute();

                }



            }
        });

        mButtonClearLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditTextLocation.setText("");
            }
        });

    }
    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}


