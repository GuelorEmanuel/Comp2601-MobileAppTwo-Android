package com.example.ldnel.myapplication;

/*
Dr. Beat implements a rudimentary musician's metronome
It is an illustration of using threads.

[Note: this code generates sound that you won't hear if the
volume on your device is turned all the way down]

In this case the time keeping thread is stopped and started from the
main activity UI. The time keeping thread is intended to report back the current beat
count to the main activity for display.

In this code example a "singleton inspired" static variable is used
to provide access to objects via static getInstance() method.
(Not pretty, can you think of a better way?)

Exercise Questions:
1) When you rotate the device it seems to remember the tempo and the metronome keeps
playing even though there is no attempt assign to the savedInstanceState nor any
attempt to override onSaveInstanceState() or onRestoreInstanceState().
Can you explain how the this appears to be working.

2)Although the metronome keeps playing and remembers the tempo when you rotate,
once you rotate you cannot stop it. Can you explain why and fix the problem.
(Similarly if you press play after a rotate another set of metronome beats
 appears to start playing -same problem)

*/

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static MainActivity instance; //globally accessible instance

    private Button mPlayButton;
    private Button mStopButton;
    private Button mUpButton;
    private Button mDownButton;

    private EditText mTempoView;
    private TextView mBeatCountView;


    private int mTempo=Ticker.DEFAULT_BPM_TEMPO; //beats per minute
    private int mBeatsPerMeasure = Ticker.DEFAULT_BEATS_PER_MEASURE;

    private Ticker ticker; //time keeper to generate beat ticks
    private SoundManager soundManager;
    private Handler handler;

    //provide global access to main activity instance
    public static MainActivity getInstance(){return instance;}

    //instance getters and setters
    public int getBPM(){return mTempo;}
    public void setBeatCount(int beatCount){
        Log.i(TAG, "beat count from thread: " + beatCount);
        mBeatCountView.setText("" + beatCount);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this; //set globally accessible instance
        handler = new Handler();

        //if existing ticker instance is running use it
        if(Ticker.getInstance() != null) ticker = Ticker.getInstance();
        else ticker = new Ticker(mBeatsPerMeasure, this);

        soundManager = new SoundManager(this);
        Log.i(TAG, "SOUND MANAGER MAX VOL: " + soundManager.getMaxVolume());

        setContentView(R.layout.activity_main); //set and inflate UI 
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPlayButton = (Button) findViewById(R.id.play_button);
        mStopButton = (Button) findViewById(R.id.stop_button);
        mUpButton = (Button) findViewById(R.id.up_button);
        mDownButton = (Button) findViewById(R.id.down_button);

        mTempoView = (EditText) findViewById(R.id.tempo_text_view);
        mBeatCountView = (TextView) findViewById(R.id.beat_count_text_view);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "Play Button Clicked"); //print to console for debugging
                Toast t = Toast.makeText(MainActivity.this, R.string.play_button_label, Toast.LENGTH_SHORT);
                t.show();

                ticker.start(); //start the time keeper

            }

        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Stop Button Clicked"); //print to console for debugging

                Toast t = Toast.makeText(MainActivity.this, R.string.stop_button_label, Toast.LENGTH_SHORT);
                t.show();
                ticker.stop(); //stop the timer keeper
            }

        });

        mDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Down Button Clicked");

                if(mTempo > Ticker.MIN_BPM_TEMPO) mTempo--;
                mTempoView.setText("" + mTempo);
            }

        });

        mUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Up Button Clicked");

                if(mTempo < Ticker.MAX_BPM_TEMPO) mTempo++;
                mTempoView.setText("" + mTempo);


            }

        });

        mTempoView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newTempoString = s.toString();
                try {
                    int newTempo = Integer.parseInt(newTempoString);
                    if(newTempo >= Ticker.MIN_BPM_TEMPO && newTempo <= Ticker.MAX_BPM_TEMPO)
                        mTempo = newTempo;
                }
                catch(NumberFormatException e){
                    mTempo = Ticker.DEFAULT_BPM_TEMPO;
                }

            }
        });

        //Initialize Data Model objects and update GUI views
        mTempoView.setText(""+ mTempo);
        mBeatCountView.setText("");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Handler getHandler(){
        return this.handler;
    }
}
