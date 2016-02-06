package com.example.ldnel.myapplication;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ldnel on 2015-12-22.
 */
public class Exam {

    private static final String TAG = Exam.class.getSimpleName();

    //XML tags used to define an exam of multiple choice questions.
    public static final String XML_EXAM = "exam";


    public static ArrayList pullParseFrom(BufferedReader reader){
        //ArrayList questions = Question.exampleSet1(); //for now
        ArrayList <Question> questions = new ArrayList<Question>();
        String question ="";
        String answer = "";
        // Get our factory and create a PullParser
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(reader); // set input file for parser
            int eventType = xpp.getEventType(); // get initial eventType

            // Loop through pull events until we reach END_DOCUMENT
            boolean isQuestion = false;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                // handle the xml tags encountered
                switch (eventType) {
                    case XmlPullParser.START_TAG: //XML opening tags

                        if (xpp.getName().trim().equals("question_text")){
                            isQuestion = true;
                        }else {
                            isQuestion = false;
                        }



                        break;

                    case XmlPullParser.TEXT:
                        //to do
                        //System.out.println("START_TAG: " + xpp.getText().trim())
                        if (isQuestion) {
                            System.out.println("Tetsts:: " + xpp.getText().trim());
                            question = xpp.getText().trim();
                            isQuestion = false;
                        }else {
                            System.out.println("Tetsts:: " + xpp.getText().trim());
                            isQuestion = true;
                            answer = xpp.getText().trim();
                        }

                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if (xpp.getName().trim().equals("answer")){
                            questions.add(new Question(
                                    question,
                                    Boolean.parseBoolean(answer)

                            ));
                        }

                        break;

                    default:
                        break;
                }


                //iterate
                eventType = xpp.next();

            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return questions;

    }

}
