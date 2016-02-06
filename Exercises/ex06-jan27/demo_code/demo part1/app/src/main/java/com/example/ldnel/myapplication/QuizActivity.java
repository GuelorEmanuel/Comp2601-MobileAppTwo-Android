package com.example.ldnel.myapplication;

/*
This example opens a hardcoded raw file and parses the XML data
It presents a user interface to scroll through multiple choice test questions
 */

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private  final String TAG = this.getClass().getSimpleName() + " @" + System.identityHashCode(this);
    private static final String STATE_questionIndex = "STATE_questionIndex";

    private static final String fileURLString = "https://www.dropbox.com/s/bf0grfz7kr801pj/comp2601exam.xml?dl=1";
    private static final String downloadedQuestionFileName = "Dropbox";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;

    //model state variables
    private TextView mQuestionTextView;
    private ArrayList<Question> mQuestions;
    private int mQuestionIndex;


    private class FileDownloadTask extends AsyncTask <Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            //excuted on background thread
            //open and parse data file
            //Initialise Data Model objects

            //parse the questions from included raw file resource

            try {
                FileOutputStream fileOutputStream = openFileOutput(downloadedQuestionFileName, Context.MODE_PRIVATE);
                FileDownloader.DownloadFromUrl(fileURLString, fileOutputStream);
            } catch (FileNotFoundException e) {
                Log.i(TAG, "ERROR: file to download not found");
                e.printStackTrace();
            }

            //parse the file.
            //open up InputStream and Reader of our file
            try {
                FileInputStream fInStream = QuizActivity.this.openFileInput(downloadedQuestionFileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fInStream));

                /*
                Log.i(TAG, "Lines from test file");
                Log.i(TAG, "---------------------------------------------");

                String readLine;
                try {
                    while ((readLine = reader.readLine())!= null) {
                        Log.i(TAG, readLine);

                    }
                }catch(java.io.IOException e){

                }
                */

                ArrayList<Question> parsedModel = Exam.pullParseFrom(reader);
                if (parsedModel == null || parsedModel.isEmpty())
                    Log.i(TAG, "ERROR: Question File Not Parsed");
                mQuestions = parsedModel;

                reader.close();

            }
            catch(java.io.FileNotFoundException e){
                Log.i(TAG, "ERROR: downloaded file not found");
            }
            catch(java.io.IOException e){
                Log.i(TAG, "ERROR: unable to close file");
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

        }


        @Override
        protected void onPostExecute(Void result){
            //executed on main UI thread

        }
    }

    private void updateUI(){
        //update UI views based on state of model objects
        if(mQuestions != null && mQuestions.size() > 0) {

            if(mQuestionIndex > mQuestions.size()) mQuestionIndex = 0;

            mQuestionTextView.setText("" + (mQuestionIndex + 1) + ") " +
                    mQuestions.get(mQuestionIndex).toString());
        }

    }

    //Activity Lifecycle Transitions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate(Bundle)");
        //activity exists, is stopped and not visible to user

        setContentView(R.layout.activity_quiz); //set and inflate UI to manage
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

        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mNextButton = (Button) findViewById(R.id.next_button);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setTextColor(Color.BLUE);

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "True Button Clicked"); //print to console for debugging

                Toast t;
                if (mQuestions.get(mQuestionIndex).getAnswer())
                    t = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);

                else
                    t = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);

                t.show();

            }

        });
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("False Button Clicked"); //print to console for debugging

                Toast t;
                if (mQuestions.get(mQuestionIndex).getAnswer())
                    t = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
                else
                    t = Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
                t.show();
            }

        });

        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Prev Button Clicked");

                if(mQuestionIndex > 0) mQuestionIndex--;

                updateUI();

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Prev Button Clicked", Toast.LENGTH_SHORT);
                t.show();
            }

        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Next Button Clicked");

                if (mQuestionIndex < mQuestions.size() - 1) mQuestionIndex++;

                updateUI();

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Next Button Clicked", Toast.LENGTH_SHORT);
                t.show();

            }

        });

        mQuestions = Question.exampleSet1();
        mQuestionIndex = 0;

        if(isNetworkAvailable()){
            Log.i(TAG, "starting file download Task");
            FileDownloadTask download = new FileDownloadTask();
            download.execute();

        }
        //recover state from saved instance state
        if(savedInstanceState != null) mQuestionIndex = savedInstanceState.getInt(STATE_questionIndex);



        updateUI(); //update the UI view components

    }


    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "onStart()");
        //activity paused but visible to user, not in foreground

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume()");
        //activity is visible, running and in foreground

    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
        //activity paused, visible, not in foreground

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop()");
        //activity stopped, not visible
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        //activity and will be destroyed
    }

    //Overide saving and restoring of activity instance state data
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        //called after onPause()
       super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState()");
        //save current question index.
        savedInstanceState.putInt(STATE_questionIndex, mQuestionIndex);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        //Gets called after onStart();
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState()");
        //recover state from saved instance state
        if(savedInstanceState != null) mQuestionIndex = savedInstanceState.getInt(STATE_questionIndex);

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

    //Helper method to determine if Internet connection is available.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
