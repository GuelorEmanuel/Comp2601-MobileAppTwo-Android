package me.cubedguelor.midtermapp;

import java.util.ArrayList;

/**
 * Created by Guelor on 2016-02-03.
 */
public class Question {
    //XML tags the define Question properties
    /*public static final String XML_QUESTION = "question";
    public static final String XML_QUESTION_TEXT = "question_text";
    public static final String XML_Choices_TEXT   = "choices";
    public static final String XML_ANSWER_CHOOSEN_TEXT = "answer";
    public static final String XML_ATTR_CONTRIBUTER = "contributor";*/

    private String               mQuestionString; //id of string resource representing the question
    private String                mEmail; //author or contributor of the question
    private ArrayList<String>     choices;
    private String                mQuestionID;
    private boolean               mDidAnswer;
    private int                   mChoiceID;

    public Question(String questionID, String questionString){
        mQuestionID      = questionID;
        mQuestionString  = questionString;
        choices          = new ArrayList<String>();
        mDidAnswer       = false;


    }

    public void setQuestionID(String questionID) {
        mQuestionID = questionID;
    }


    public void setDidAnswer(boolean didAnswer) {
        mDidAnswer = didAnswer;
    }

    public void setChoiceID(int choice) {
        mChoiceID = choice;
    }

    public String getQuestionID() {
        return mQuestionID;
    }

    public int getChoiceID() {
        return mChoiceID;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }

    public boolean isDidAnswer() {
        return mDidAnswer;
    }

    public String getChoices(int i) {
        return choices.get(i);
    }

    public void setChoices(String choice) {
        this.choices.add(choice);
    }

    public String getQuestionString() {
        return mQuestionString;
    }

}
