package me.cubedguelor.midtermapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/*
   By: Guelor Emanuel
   Student ID: 100884107
   Class: Comp2601 Assignment1
 */

public class SubmitQuiz extends AppCompatActivity {


    private String       firstname;
    private String       lastname;
    private String       studentID;

    private TextView     firstNameTextView;
    private TextView     lastNameTextView;
    private TextView     studentIDView;
    private Button       confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fill in the student information
        firstNameTextView  = (TextView) findViewById(R.id.first_name);
        lastNameTextView   = (TextView) findViewById(R.id.last_name);
        studentIDView      = (TextView) findViewById(R.id.student_id);

        confirmButton      = (Button) findViewById(R.id.confirm_id);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent quizIntent = new Intent(SubmitQuiz.this, QuizActivity.class);
                //obtain data passed to intent
                Bundle nameInfoBundle = new Bundle();
                firstname = firstNameTextView.getText().toString();
                lastname  = lastNameTextView.getText().toString();
                studentID = studentIDView.getText().toString();
                nameInfoBundle.putString(States.STATE_FIRSTNAME, firstname);
                nameInfoBundle.putString(States.STATE_LASTNAME, lastname);
                nameInfoBundle.putString(States.STATE_STUDENTID, studentID);
                quizIntent.putExtras(nameInfoBundle);
                startActivity(quizIntent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
