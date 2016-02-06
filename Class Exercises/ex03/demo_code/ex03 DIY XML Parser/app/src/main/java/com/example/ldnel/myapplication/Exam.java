package com.example.ldnel.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ldnel on 2015-12-22.
 */
public class Exam {

    private  final String TAG = this.getClass().getSimpleName();

    public final static String questionStart_tag =  "<question_text>";
    public final static String questionEnd_tag   =  "</question_text>";
    public final static String answeerStat_tag   =  "<answer>";
    public final static String answerEnd_tag     =  "</answer>";



    public static ArrayList<Question> parseFrom(BufferedReader iStream) {

        String readLine = null; //input line read from file
        ArrayList<Question> questions = new ArrayList<Question>();
        String question  = "";
        String finalQuestion = "";
        int counter = 0;
        String answer = "";

        boolean isQuestion = false;
        boolean isAnswer   = false;

        try{
            while((readLine = iStream.readLine()) != null){
                if (isQuestion && !readLine.contains(questionEnd_tag)) {
                    question += readLine.trim();


                }
                else if (readLine.contains(questionStart_tag)) { //base case
                    isQuestion = true;

                }
                else if (readLine.contains(questionEnd_tag)){ //alternate
                    isAnswer = true;
                    isQuestion = false;

                }
                if ( isAnswer) {
                    if (!readLine.contains(questionEnd_tag)) {
                        answer = readLine.trim().replace("<answer>", "").replace("</answer>", "");
                        System.out.print("BOLLLEAN VALUE: " + answer);

                        questions.add(new Question(
                                question,
                                Boolean.parseBoolean(answer)
                        ));
                        question = "";
                        isAnswer = false;
                    }
                }





            }
            iStream.close();

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return  questions;
    }
    public static ArrayList<Question> sampleExam1(){
        /*
        Provide a sample set of questions for testing
         */
        ArrayList<Question> questions = new ArrayList<Question>();
        questions.add(new Question(
                "In java private instance variables of a class are \"visible\" to subclasses",
                false
        ));
        questions.add(new Question(
                "In java a variable of a subclass type may be assigned an object of a superclass type",
                false
        ));
        questions.add(new Question(
                "Interfaces in java are created using the \"extends\" keyword",
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
