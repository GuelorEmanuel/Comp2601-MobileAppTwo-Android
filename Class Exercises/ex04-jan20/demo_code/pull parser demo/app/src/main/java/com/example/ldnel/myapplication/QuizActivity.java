package com.example.ldnel.myapplication;

/*
This example uses exam questions stored in an XML file as a raw resource
The code opens the file when the activity is created and reads and parses the
questions from the XML file.

This example uses the Android specific XmlPullParser (An XML
parser not found in non-android java)
The Exam class has a static method pullParseFrom(BufferedReader) which
parses the xml file representing an exam of true-false questions.

This example shows a parse with XML tags that include attributes.


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

    private static final String TAG = QuizActivity.class.getSimpleName();

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;

    private TextView mQuestionTextView;
    private ArrayList<Question> questions;

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
        //mTrueButton.setBackgroundColor(Color.GREEN);
        mFalseButton = (Button) findViewById(R.id.false_button);
        //mFalseButton.setBackgroundColor(Color.RED);
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
                        questions.get(questionIndex).toString());

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Prev Button Clicked", Toast.LENGTH_SHORT);
                t.show();
            }

        });
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i(TAG, "Next Button Clicked");

                if(questionIndex < questions.size()-1) questionIndex++;

                mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                        questions.get(questionIndex).toString());

                //for debugging
                Toast t = Toast.makeText(QuizActivity.this, "Next Button Clicked", Toast.LENGTH_SHORT);
                t.show();

            }

        });

        //Initialise Data Model objects
        //questions = Question.exampleSet1();
        questions = null;
        questionIndex = 0;



        //Try to read resource data file with questions

        ArrayList<Question> parsedModel = null;
        try {
            InputStream iStream = getResources().openRawResource(R.raw.comp2601exam);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            //ArrayList<Question> parsedModel = Exam.parseFrom(bs);
            parsedModel = Exam.pullParseFrom(bReader);
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();

        }
        if(parsedModel == null || parsedModel.isEmpty())
            Log.i(TAG, "ERROR: Questions Not Parsed");
        questions = parsedModel;

        if(questions != null && questions.size() > 0)
            mQuestionTextView.setText("" + (questionIndex + 1) + ") " +
                    questions.get(questionIndex).toString());


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
