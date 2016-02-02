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

    //XML tags the define Question properties
    public static final String XML_QUESTION = "question";
    public static final String XML_QUESTION_TEXT = "question_text";
    public static final String XML_ANSWER = "answer";
    public static final String XML_ATTR_CONTRIBUTER = "contributor";

    private String mQuestionString; //id of string resource representing the question
    private boolean mAnswer; //boolean representing the question answer
    private String mContributor; //author or contributor of the question

    public Question(String aQuestion, boolean anAnswer){
        mQuestionString = aQuestion;
        mAnswer = anAnswer;
        mContributor = "anonymous";

    }
    public Question(String aQuestion, boolean anAnswer, String contributer){
        mQuestionString = aQuestion;
        mAnswer = anAnswer;
        if(contributer != null && !contributer.isEmpty())
            mContributor = contributer;
        else
            mContributor = "anonymous";

    }


    public String getQuestionString(){return mQuestionString;}
    public boolean getAnswer(){return mAnswer;}
    public String getContributer(){return mContributor;}

    public String toString(){
        String toReturn = "";
        if(mContributor != null && !mContributor.isEmpty())
            toReturn += "[" + mContributor + "] ";
        toReturn += mQuestionString;
        return toReturn;
    }



    public static ArrayList<Question> exampleSet1(){
        ArrayList<Question> questions = new ArrayList<Question>();
        questions.add(new Question(
                "In Java the == operation performs the same function as .equals() method in java",
                false

        ));
        questions.add(new Question(
                "Given java expression z = x.foo(y), in method foo() the variable 'this' refers to y",
                false
        ));
        questions.add(new Question(
                "Subclasses in java can 'see' the private variables of their superclass",
                false
        ));
        questions.add(new Question(
                "An abstract class in java is a class with no method implementations",
                false
        ));
        questions.add(new Question(
                "Constructors in java should be public and have no return type, not even void",
                true
        ));
        return questions;
    }
}
