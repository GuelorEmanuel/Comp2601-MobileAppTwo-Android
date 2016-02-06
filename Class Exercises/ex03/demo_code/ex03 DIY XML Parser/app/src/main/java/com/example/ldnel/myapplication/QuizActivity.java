package com.example.ldnel.myapplication;

/*
This example contains exam questions stored in an XML file as a raw resource
The code currently, however, uses sample data supplied by the Exam class
instead of the data in the XML file.

This is a review exercise in programming with java String's, ArrayList's.
The exercise is writing your own simple XML parser.
In future exercises we will use parsing libraries and helper classes available for java and android
but here we write our own.
Our XML file is very "friendly" in that tags are nicely split onto
lines of a text file.

 */

import android.graphics.Color;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private  final String TAG = this.getClass().getSimpleName();

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;

    private TextView mQuestionTextView;
    private ArrayList<Question> questions;
    private static final String KEY_INDEX = "index";

    private int questionIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (questions.get(questionIndex).getAnswer())
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
                if (questions.get(questionIndex).getAnswer())
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

                if(questionIndex > 0) questionIndex--;

                mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                        questions.get(questionIndex).getQuestionString());

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Prev Button Clicked", Toast.LENGTH_SHORT);
                t.show();
            }

        });
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Next Button Clicked");

                questionIndex =  (questionIndex + 1) % questions.size();
                mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                        questions.get(questionIndex).getQuestionString());

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Next Button Clicked", Toast.LENGTH_SHORT);
                t.show();

            }

        });

        //Initialize Data Model objects
        //questions = Question.exampleSet1();
        InputStream iStream = getResources().openRawResource(R.raw.comp2601exam);
        BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
        questions = Exam.parseFrom(bReader);


        questionIndex = 0;


        if(questions != null && questions.size() > 0) {
            if (savedInstanceState != null) {
                questionIndex = savedInstanceState.getInt(KEY_INDEX,0);

            }
            mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                    questions.get(questionIndex).getQuestionString());
        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, questionIndex);
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
}