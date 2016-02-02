package com.example.ldnel.myapplication;

/**
 * Created by ldnel on 2016-01-18.
 */
import android.util.Log;

import java.util.Calendar;

public class Ticker {

    private  String TAG = this.getClass().getSimpleName();

    public static final int MIN_BPM_TEMPO = 20;
    public static final int MAX_BPM_TEMPO = 240;
    public static final int DEFAULT_BPM_TEMPO = 88;

    public static final int DEFAULT_BEATS_PER_MEASURE = 4;
    public static final int DOWNBEAT_COUNT = 1;

    // Time Signature Beats per Measure
    private static Ticker instance = null; //globally accessible instance
    private MainActivity mainActivityInstance;
    public static Ticker getInstance(){return instance;}

    private int mBeatsPerMeasure;

    // Running
    private boolean running; //true when ticker is running

    // Constructor
    public Ticker(int aNumberOfBeatsPerMeasure, MainActivity mainActivityInstance) {
        instance = this;
        mBeatsPerMeasure = aNumberOfBeatsPerMeasure;
        running = false;
        this.mainActivityInstance = mainActivityInstance;
    }

    // Tick
    public void playTick() {
        //ticks are played on the non-downbeat of a bar
        SoundManager.getInstance().playLow();
    }

    // Down Beat Accent
    public void playAccent() {
        //accents are played on the downbeat of a bar of music
        SoundManager.getInstance().playHigh();
    }


    // Start the ticker
    public void start() {
        Log.i(TAG, "ticker starting");
        if(!running) {
            running = true;
            new Thread(new Beat()).start();
        }
    }

    // Stop the ticker
    public void stop() {
        Log.i(TAG, "ticker stopping");
        running = false;
    }

    // Beat
    private class Beat implements Runnable {
        // Time References
        Calendar previous; //time of previous beat
        Calendar current;  //current time sample
        // Vars
        int beatCount; //current beat count in measure
        int bpm; //beatsPerMinute tempo
        int delay; //milliseconds per beat
        int elapsed; //elapsed time since last beat

        // Run
        public void run() {
            // Init
            previous = Calendar.getInstance();
            beatCount = DOWNBEAT_COUNT;
             playAccent();


            mainActivityInstance.getHandler().post(new Runnable() {
                public void run() {
                    mainActivityInstance.setBeatCount(beatCount);

                }
            });


            // Thread Loop
            while (running) {
                current = Calendar.getInstance();
                //get current UI beatsPerMinute tempo setting
                bpm = MainActivity.getInstance().getBPM();

                // Check BPM
                if (bpm < MIN_BPM_TEMPO || bpm > MAX_BPM_TEMPO) {
                    continue; //do nothing, tempo is out of range
                }

                delay = 60000 / bpm; //milliseconds per beat

                elapsed = (int) (current.getTimeInMillis() - previous
                        .getTimeInMillis());


                // Time Elapsed
                if (elapsed >= delay) {
                    beatCount = beatCount % mBeatsPerMeasure + 1;
                    previous = current;

                    if(beatCount == DOWNBEAT_COUNT) playAccent();
                    else playTick();
                   MainActivity.getInstance().getHandler().post(new Runnable() {
                       @Override
                       public void run() {
                           mainActivityInstance.setBeatCount(beatCount);

                       }
                   });



                }

            } //end while
        } //end run
    }

}
