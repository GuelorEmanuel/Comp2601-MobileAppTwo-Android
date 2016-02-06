package com.android.comp2601.weatherdatajson;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    TextView mCityName;
    TextView mTemperature;
    TextView mMinTemperature;
    TextView mMaxTemperature;
    TextView mDescription;
    Button mButtonReturn;
    String mWeather;

    String mStrCityName;
    String mStrTemperature;
    String mStrMinTemperature;
    String mStrMaxTemperature;
    String mStrDescription;

    JSONObject mJSONWeatherData;

    private static final String TAG = "WeatherActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mCityName = (TextView)findViewById(R.id.text_city_value);
        mTemperature = (TextView)findViewById(R.id.text_temperature_value);
        mMinTemperature = (TextView)findViewById(R.id.text_mintemperature_value);
        mMaxTemperature = (TextView)findViewById(R.id.text_maxtemperature_value);
        mDescription = (TextView)findViewById(R.id.text_description_value);
        mButtonReturn = (Button)findViewById(R.id.button_return_weather);


        if (savedInstanceState == null) {
            Intent mIntent = getIntent();
            if(mIntent != null) {
                Bundle BUNDLE_WEATHER = mIntent.getBundleExtra(States.STATE_WEATHER_BUNDLE);
                mWeather = BUNDLE_WEATHER.getString(States.STATE_WEATHER);

                try {
                    mJSONWeatherData = new JSONObject(mWeather);
                    mStrCityName = mJSONWeatherData.getString("name");
                    mStrTemperature = Integer.toString(mJSONWeatherData.getJSONObject("main").getInt("temp"));
                    mStrMinTemperature = Integer.toString(mJSONWeatherData.getJSONObject("main").getInt("temp_min"));
                    mStrMaxTemperature = Integer.toString(mJSONWeatherData.getJSONObject("main").getInt("temp_max"));

                    JSONArray mWeatherArray = mJSONWeatherData.getJSONArray("weather");
                    JSONObject mDescriptionJSONObject = (JSONObject) mWeatherArray.get(0);
                    mStrDescription = mDescriptionJSONObject.getString("description");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mCityName.setText(mStrCityName);
                mTemperature.setText(mStrTemperature);
                mMinTemperature.setText(mStrMinTemperature);
                mMaxTemperature.setText(mStrMaxTemperature);
                mDescription.setText(mStrDescription);
            }
        } else {
            mStrCityName = savedInstanceState.getString(States.STATE_CITY);
            mStrTemperature = savedInstanceState.getString(States.STATE_TEMP);
            mStrMinTemperature = savedInstanceState.getString(States.STATE_MINTEMP);
            mStrMaxTemperature = savedInstanceState.getString(States.STATE_MAXTEMP);
            mStrDescription = savedInstanceState.getString(States.STATE_DESCRIPTION);

            mCityName.setText(mStrCityName);
            mTemperature.setText(mStrTemperature);
            mMinTemperature.setText(mStrMinTemperature);
            mMaxTemperature.setText(mStrMaxTemperature);
            mDescription.setText(mStrDescription);
        }

        mButtonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        mStrCityName = mCityName.getText().toString();
        mStrTemperature = mTemperature.getText().toString();
        mStrMinTemperature = mMinTemperature.getText().toString();
        mStrMaxTemperature = mMaxTemperature.getText().toString();
        mStrDescription = mDescription.getText().toString();

        savedInstanceState.putString(States.STATE_CITY, mStrCityName);
        savedInstanceState.putString(States.STATE_TEMP, mStrTemperature);
        savedInstanceState.putString(States.STATE_MINTEMP, mStrMinTemperature);
        savedInstanceState.putString(States.STATE_MAXTEMP, mStrMaxTemperature);
        savedInstanceState.putString(States.STATE_DESCRIPTION, mStrDescription);
    }
}
