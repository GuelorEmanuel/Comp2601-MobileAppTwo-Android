package me.cubedguelor.midtermapp;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * Created by Guelor on 2016-02-03.
 */
public class Exam {
    private static final String TAG = Exam.class.getSimpleName();

    //XML tags used to define an exam of multiple choice questions.
    public static final String XML_EXAM = "exam";


    public static ArrayList pullParseFrom(BufferedReader reader){
        //ArrayList questions = Question.exampleSet1(); //for now
        ArrayList <Question> questions = new ArrayList<Question>();
        String question   ="";
        String choice    = "";
        String email     = "";
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
                        if (isQuestion) {
                            System.out.println("Tetsts:: "+xpp.getAttributeCount() +" "+ xpp.getText().trim());
                            question   = xpp.getText().trim();
                           // questionID = xpp.getAttributeValue(1);
                            isQuestion = false;
                        }else {
                            System.out.println("Tetsts:: "+xpp.getAttributeCount() +" " + xpp.getText().trim());
                            isQuestion = true;
                            choice = xpp.getText().trim();
                            email  = xpp.getText().trim();
                        }

                        break;

                    case XmlPullParser.END_TAG: //XML closing tags
                        if (xpp.getName().trim().equals("question_text")){
                            questions.add(new Question(question, question));
                        }
                        if (xpp.getName().trim().equals("o")){
                            questions.get(questions.size()-1).setChoices(choice);
                        }
                        if (xpp.getName().trim().equals("email")){
                            questions.get(questions.size()-1).setEmail(email);
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
