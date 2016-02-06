package me.cubedguelor.midtermapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
   By: Guelor Emanuel
   Student ID: 100884107
   Class: Comp2601 Assignment1
 */

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private Button optOneButton;
    private Button optTwoButton;
    private Button optThreeButton;
    private Button optFourButton;
    private Button optFiveButton;
    private Button nextButton;
    private Button prevButton;
    private Button submitButton;

    private EditText firstName;
    private EditText lastName;
    private EditText studentID;

    private TextView mQuestionTextView;



    private ArrayList<Question> questions;
    private StringBuilder submission;
    private static final String TAG          = "QuizActivity";
    private static final String KEY_INDEX    = "index";
    private static final String KEY_CHOOSEN  = "answered";
    private String userFirstName             ="";
    private String userLastName              ="";
    private String userStudentID             = "";
    private int mCurrentIndex                = 0;
    private int[] choosen                    = new int[20];
    private Bundle nameBundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //obtain data passed to intent
        nameBundle           = getIntent().getExtras();
        userFirstName       = nameBundle.getString(States.STATE_FIRSTNAME);
        userLastName        = nameBundle.getString(States.STATE_LASTNAME);
        userStudentID       = nameBundle.getString(States.STATE_STUDENTID);

        mQuestionTextView  = (TextView) findViewById(R.id.question_text_view);

        optOneButton       = (Button) findViewById(R.id.opt_one_button);
        optTwoButton       = (Button) findViewById(R.id.opt_two_button);
        optThreeButton     = (Button) findViewById(R.id.opt_three_button);
        optFourButton      = (Button) findViewById(R.id.opt_four_button);
        optFiveButton      = (Button) findViewById(R.id.opt_five_button);
        nextButton         = (Button) findViewById(R.id.next_button);
        prevButton         = (Button) findViewById(R.id.prev_button);
        submitButton       = (Button) findViewById(R.id.submit_button);


        optOneButton.setOnClickListener(this);
        optTwoButton.setOnClickListener(this);
        optThreeButton.setOnClickListener(this);
        optFourButton.setOnClickListener(this);
        optFiveButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);



        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            choosen       = savedInstanceState.getIntArray(KEY_CHOOSEN);// if user picked an answer
            Log.i(TAG, "LOADING: " +mCurrentIndex);
        }
        updateQuestion(); // update the indexes

        //Initialise Data Model objects
        questions     = null;
        ArrayList<Question> parsedModel = null;

        //Try to read resource data file with questions
        try {
            InputStream iStream = getResources().openRawResource(R.raw.android_exam_questions);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            parsedModel = Exam.pullParseFrom(bReader);
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();

        }
        if(parsedModel == null || parsedModel.isEmpty())
            Log.i(TAG, "ERROR: Questions Not Parsed");

        questions = parsedModel;
        int counter = 0;

        for (Question i: questions){
            i.setChoiceID(choosen[counter]);
            counter++;
        }
        getDeviceInfo();
        updateQuestion();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        int counter = 0;

        for (Question i: questions){
            choosen[counter] = i.getChoiceID();
            counter++;
        }
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putIntArray(KEY_CHOOSEN, choosen);
        Log.i(TAG, "SAVING: " + mCurrentIndex);
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

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.opt_one_button:

                //Change button colors
                optOneButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));

                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(1);
                questions.get(mCurrentIndex).setAnswerChoice(optFourButton.getText().toString());

                //Toast
                Toast.makeText(this, R.string.optionOne, Toast.LENGTH_SHORT).show();

                break;

            // All event listener logic happens here
            case R.id.opt_two_button:
                //Change buttons color
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(2);
                questions.get(mCurrentIndex).setAnswerChoice(optFourButton.getText().toString());

                Toast.makeText(this, R.string.optionTwo, Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_three_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optThreeButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(3);
                questions.get(mCurrentIndex).setAnswerChoice(optFourButton.getText().toString());

                Toast.makeText(this, R.string.optionThree, Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_four_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205,206,207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optFiveButton.setBackgroundColor(Color.rgb(205, 206, 207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(4);
                questions.get(mCurrentIndex).setAnswerChoice(optFourButton.getText().toString());
                Toast.makeText(this, R.string.optionFour, Toast.LENGTH_SHORT).show();

                break;
            case R.id.opt_five_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205,206,207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(179, 220, 233));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(5);
                questions.get(mCurrentIndex).setAnswerChoice(optFourButton.getText().toString());
                Toast.makeText(this, R.string.optionFive, Toast.LENGTH_SHORT).show();

                break;
            case R.id.next_button:
                mCurrentIndex = (mCurrentIndex + 1) % questions.size();
                updateQuestion();
                break;
            case R.id.prev_button:
                mCurrentIndex = (mCurrentIndex - 1) % questions.size();
                if ( mCurrentIndex < 0) {
                    mCurrentIndex = questions.size()-1;
                }
                Log.i(TAG, "NUM: " + questions.size());
                updateQuestion();
                break;
            case R.id.submit_button:
                submission= new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<answer_key> ");

                boolean pass       = false; // check if student answered all the questions

                for( Question i : questions) {
                    submission.append("<answer_text id =\"" + i.getChoiceID() + "\">" +
                            i.getAnswerChoice() + "</answer_text>");
                    if (!i.isDidAnswer()) {
                        Toast.makeText(this,
                                R.string.didNotFinish,
                                Toast.LENGTH_SHORT).show();
                        pass = false;
                        break;
                    }else{
                        pass = true;
                    }
                }
               if (pass) {
                   getDeviceInfo(); // let get the device info
                   submission.append("</answer_key>");
                   String tempBody = submission.toString();
                   Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" +
                            questions.get(mCurrentIndex).getEmail()));
                   emailIntent.putExtra(Intent.EXTRA_SUBJECT, userFirstName + " "+userLastName+"ïd: "+
                            userStudentID );
                   emailIntent.putExtra(Intent.EXTRA_TEXT, tempBody);
                   startActivity(Intent.createChooser(emailIntent, "Email Client ..."));
               }
                break;
        }
    }

    private void updateQuestion(){
        int options = 0;

        if (questions != null && questions.size() > 0){
            mQuestionTextView.setText("" + (mCurrentIndex + 1) + ") " +
                    questions.get(mCurrentIndex).getQuestionString().toString());
            optOneButton.setText(questions.get(mCurrentIndex).getChoices(options));
            optTwoButton.setText(questions.get(mCurrentIndex).getChoices(options+=1));
            optThreeButton.setText(questions.get(mCurrentIndex).getChoices(options+=1));
            optFourButton.setText(questions.get(mCurrentIndex).getChoices(options+=1));
            optFiveButton.setText(questions.get(mCurrentIndex).getChoices(options+=1));

            optOneButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optThreeButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optFourButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optFiveButton.setBackgroundColor(Color.rgb(205, 206, 207));

            int swap = questions.get(mCurrentIndex).getChoiceID();
            if (swap == 1) {
                optOneButton.setBackgroundColor(Color.rgb(179, 220, 233));
            } else if(swap == 2){
                optTwoButton.setBackgroundColor(Color.rgb(179, 220, 233));
            }else if(swap ==3){
                optThreeButton.setBackgroundColor(Color.rgb(179, 220, 233));
            }else if (swap == 4){
                optFourButton.setBackgroundColor(Color.rgb(179, 220, 233));
            }else if (swap == 5) {
                optFiveButton.setBackgroundColor(Color.rgb(179, 220, 233));
            }

        }
    }

    /*
      Method for getting the device Information
     */
    private String getDeviceInfo(){
        String s = "Device Info:";
        try {
            s += "\n OS Version: "      + System.getProperty("os.version") +
                    "(" + Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: "    + Build.VERSION.SDK_INT;
            s += "\n Model (and Product): " + Build.MODEL +
                    " ("+ Build.PRODUCT + ")";
            s += "\n RELEASE: "         + Build.VERSION.RELEASE;
            s += "\n BRAND: "           + android.os.Build.BRAND;
            s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            s += "\n Build ID: "        + android.os.Build.ID;
            s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: "          + android.os.Build.SERIAL;
            s += "\n USER: "            + android.os.Build.USER;
            s += "\n HOST: "            + android.os.Build.HOST;
            System.out.println(s);

        } catch(Exception e) {
            Log.e(TAG, "Error getting Device INFO");
        }
        return s;
    }
}
