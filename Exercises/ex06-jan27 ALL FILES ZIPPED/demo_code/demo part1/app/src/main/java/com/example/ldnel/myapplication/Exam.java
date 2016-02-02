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
    static final String XML_EXAM = "exam";
    static final String XML_QUESTION = "question";
    static final String XML_QUESTION_TEXT = "question_text";
    static final String XML_ANSWER = "answer";
    static final String XML_ATTR_CONTRIBUTOR = "contributor";


    public static ArrayList<Question> pullParseFrom(BufferedReader reader){
        ArrayList<Question> questions = new ArrayList<Question>();

        // Get our factory and create a PullParser
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(reader); // set input file for parser
            int eventType = xpp.getEventType(); // get initial eventType

            String currentText = ""; //current text between XML tags being parsed
            Question currentQuestion = null; //current question object being parsed
            String currentQuestionText = null; //text of the question being parsed
            boolean currentQuestionAnswer = false; //answer of current question being parsed
            String currentContributor = null;

            // Loop through pull events until we reach END_DOCUMENT
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // Get the current tag
                String tagname = xpp.getName();

                // handle the various xml tags encounted
                switch (eventType) {
                    case XmlPullParser.START_TAG: //XML opening tags
                        if (tagname.equalsIgnoreCase(XML_QUESTION)) {
                            currentContributor = xpp.getAttributeValue(null, XML_ATTR_CONTRIBUTOR);
                         }
                        break;

                    case XmlPullParser.TEXT:
                        //get text between xml tags
                        currentText = xpp.getText().trim();
                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if (tagname.equalsIgnoreCase(XML_QUESTION_TEXT)) {
                            //end of a question's text </question_text>
                            currentQuestionText = currentText;
                        } else if (tagname.equalsIgnoreCase(XML_ANSWER)) {
                            // </answer>
                            currentQuestionAnswer = Boolean.parseBoolean(currentText);
                        } else if (tagname.equalsIgnoreCase(XML_QUESTION)) {
                            // </question>
                            currentQuestion = new Question(currentQuestionText,
                                                           currentQuestionAnswer,
                                                           currentContributor);
                            questions.add(currentQuestion);
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
