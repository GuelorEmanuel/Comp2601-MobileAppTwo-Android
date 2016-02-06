package com.example.ldnel.myapplication;

/**
 * Created by ldnel on 2016-01-18.
 */
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

public class SoundManager {

    // Instance
    private static SoundManager instance;

    // Get Instance
    public static SoundManager getInstance(){
        return instance;
    }

    // Handler
    private Handler handler;

    // Get Handler
    public Handler getHandler(){
        return handler;
    }

    // Constants
    public static final int PAN_LEFT = 1;
    public static final int PAN_RIGHT = 2;
    public static final int PAN_BOTH = 3;

    // Audio Manager
    private AudioManager audioManager;
    private SoundPool soundPool;
    private int high;
    private int low;
    private int sub;

    // Variables
    private int panValue;
    private float leftVolume;
    private float rightVolume;

    // Constructor
    public SoundManager(Context context) {
        instance = this;
        handler = new Handler();
        audioManager = (AudioManager) context
                .getSystemService(context.AUDIO_SERVICE);
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        high = soundPool.load(context, R.raw.seikohigh, 1);
        low = soundPool.load(context, R.raw.seikolow, 1);
        sub = soundPool.load(context, R.raw.sub, 1);

        leftVolume = 0.0f;
        rightVolume = 0.0f;
        panValue = PAN_BOTH;

    }

    // Play High Sound
    public void playHigh() {
        initVolume();
        soundPool.play(high, leftVolume, rightVolume, 1, 0, 1);
    }

    // Play Low Sound
    public void playLow() {
        initVolume();
        soundPool.play(low, leftVolume, rightVolume, 1, 0, 1);
    }

    // PLay Subdivision Soung
    public void playSub(){
        soundPool.play(sub, leftVolume, rightVolume, 1, 0, 1);
    }

    // Set Pan Value
    public void setPanValue(int pan){
        this.panValue = pan;
    }

    // Init Volume
    private void initVolume() {
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        switch (panValue) {
            case PAN_LEFT:
                leftVolume = volume;
                rightVolume = 0.0f;
                break;
            case PAN_RIGHT:
                leftVolume = 0.0f;
                rightVolume = volume;
                break;
            default:
                leftVolume = volume;
                rightVolume = volume;
        }

    }

    // Get Audio Manage
    public AudioManager getAudioManager() {
        return audioManager;
    }

    // Get Max Volume
    public int getMaxVolume() {
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    // Get Current Volume
    public int getCurrentVolume() {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    // Set Current Volume
    public void setCurrentVolume(int vol) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0); // AudioManager.FLAG_SHOW_UI
    }

    // Mute
    public void mute() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    // Unmute
    public void unmute() {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

}