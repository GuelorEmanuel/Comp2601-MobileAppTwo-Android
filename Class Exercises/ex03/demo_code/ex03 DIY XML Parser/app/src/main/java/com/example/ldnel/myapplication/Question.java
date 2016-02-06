package com.example.ldnel.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ldnel on 2015-12-21.
 */
public class Question {
    /*
    Represents a true false question, including its answer
     */

    private  final String TAG = this.getClass().getSimpleName();

    //XML tags of Question object
    public static final String start_tag = "<question>";
    public static final String end_tag = "</question>";
    public static final String question_text_start_tag = "<question_text>";
    public static final String question_text_end_tag = "</question_text>";
    public static final String answer_start_tag = "<answer>";
    public static final String answer_end_tag = "</answer>";

    private String mQuestionString; //question text
    private boolean mAnswer; //boolean representing the question answer

    public Question(String aQuestion, boolean anAnswer){
        mQuestionString = aQuestion;
        mAnswer = anAnswer;

    }

    public String getQuestionString(){return mQuestionString;}
    public boolean getAnswer(){return mAnswer;}



}