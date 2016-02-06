package com.example.ldnel.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ldnel on 2015-12-21.
 */
public class Question {

    private static final String TAG = Question.class.getSimpleName();

    private String mQuestionString; //id of string resource representing the question
    private boolean mAnswer; //boolean representing the question answer
    private String mContributer; //author or contributer of the question

    public Question(String aQuestion, boolean anAnswer){
        mQuestionString = aQuestion;
        mAnswer = anAnswer;
        mContributer = "anonymous";

    }
    public Question(String aQuestion, boolean anAnswer, String contributer){
        mQuestionString = aQuestion;
        mAnswer = anAnswer;
        if(contributer != null && !contributer.isEmpty())
            mContributer = contributer;
        else
            mContributer = "anonymous";

    }


    public String getQuestionString(){return mQuestionString;}
    public boolean getAnswer(){return mAnswer;}
    public String getContributer(){return mContributer;}

    public String toString(){
        String toReturn = "";
        if(mContributer != null && !mContributer.isEmpty())
            toReturn += "[" + mContributer + "] ";
        toReturn += mQuestionString;
        return toReturn;
    }



    public static ArrayList<Question> exampleSet1(){
        ArrayList<Question> questions = new ArrayList<Question>();
        questions.add(new Question(
                "DEMONSTRATION: THE SKY IS RED",
                false

        ));

        return questions;
    }
}
