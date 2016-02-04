package me.cubedguelor.midtermapp;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    private Button optOneButton;
    private Button optTwoButton;
    private Button optThreeButton;
    private Button optFourButton;
    private Button optFiveButton;
    private Button nextButton;
    private Button prevButton;
    private Button submitButton;

    private TextView mQuestionTextView;

    private ArrayList<Question> questions;
    StringBuilder submission;


    private static final String TAG       = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_CHOOSEN = "answered";



    private int mCurrentIndex = 0;
    private int[] choosen = new int[20];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            choosen       = savedInstanceState.getIntArray(KEY_CHOOSEN);

            //questions     = savedInstanceState..
            Log.i(TAG, "LOADINGF: " +mCurrentIndex);
        }



        updateQuestion();

        //Initialise Data Model objects
        questions     = null;

        //Try to read resource data file with questions

        ArrayList<Question> parsedModel = null;
        try {
            InputStream iStream = getResources().openRawResource(R.raw.android_exam_questions);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            //ArrayList<Question> parsedModel = Exam.parseFrom(bs);
            Log.i(TAG, "2000: Pulling");
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

        Log.i(TAG, "SAVINGGGGG: " + mCurrentIndex);
        //savedInstanceState.putParcelableArrayList();
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
                //Toast
                optOneButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));

              questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(1);

                Toast.makeText(this,
                        R.string.optionOne,
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.opt_two_button:

                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(2);
                Toast.makeText(this,
                        R.string.optionTwo,
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_three_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
                optThreeButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(3);
                Toast.makeText(this,
                        R.string.optionThree,
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_four_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205,206,207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(179, 220, 233));
                optFiveButton.setBackgroundColor(Color.rgb(205,206,207));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(4);
                Toast.makeText(this,
                        R.string.optionFour,
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_five_button:
                optOneButton.setBackgroundColor(Color.rgb(205,206,207));
                optTwoButton.setBackgroundColor(Color.rgb(205,206,207));
                optThreeButton.setBackgroundColor(Color.rgb(205,206,207));
                optFourButton.setBackgroundColor(Color.rgb(205,206,207));
                optFiveButton.setBackgroundColor(Color.rgb(179, 220, 233));
                questions.get(mCurrentIndex).setDidAnswer(true);
                questions.get(mCurrentIndex).setChoiceID(5);
                Toast.makeText(this,
                        R.string.optionFive,
                        Toast.LENGTH_SHORT).show();
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
                int temp = 1;
                boolean pass = false;

                for( Question i : questions) {
                    submission.append("<answer_text id =\"" + "temp" + "\">d" +
                            i.getQuestionString() + "</answer_text>");
                    if (!i.isDidAnswer()) {
                        Toast.makeText(this,
                                R.string.didNotFinish,
                                Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        pass = true;
                    }
                    temp++;
                }
                if (pass) {
                    submission.append("</answer_key>");
                }



                break;


        }
    }

    private void updateQuestion(){
        int options = 0;
        if(questions != null && questions.size() > 0){
            mQuestionTextView.setText("" + (mCurrentIndex + 1) + ") " +
                    questions.get(mCurrentIndex).getQuestionString().toString());
            optOneButton.setText(questions.get(mCurrentIndex).getChoices(options));
            optTwoButton.setText(questions.get(mCurrentIndex).getChoices(options++));
            optThreeButton.setText(questions.get(mCurrentIndex).getChoices(options++));
            optFourButton.setText(questions.get(mCurrentIndex).getChoices(options++));
            optFiveButton.setText(questions.get(mCurrentIndex).getChoices(options++));

            optOneButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optTwoButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optThreeButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optFourButton.setBackgroundColor(Color.rgb(205, 206, 207));
            optFiveButton.setBackgroundColor(Color.rgb(205, 206, 207));

            int swap = questions.get(mCurrentIndex).getChoiceID();
            if (swap == 1) {
            }
            else if(swap == 2){
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
}
