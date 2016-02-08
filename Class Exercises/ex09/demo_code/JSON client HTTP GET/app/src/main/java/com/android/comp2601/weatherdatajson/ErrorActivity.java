package com.android.comp2601.weatherdatajson;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {

    TextView mTextViewErrorMsg;
    Button mButtonReturn;
    String mErrorMessage;

    private static final String TAG = "ErrorActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        mTextViewErrorMsg = (TextView)findViewById(R.id.text_error_message);
        mButtonReturn = (Button)findViewById(R.id.button_return_error);

        if (savedInstanceState == null) {
            Intent mIntent = getIntent();
            if(mIntent != null) {
                Bundle BUNDLE_ERROR = mIntent.getBundleExtra(States.STATE_ERROR_BUNDLE);
                mErrorMessage = BUNDLE_ERROR.getString(States.STATE_ERROR);


                mTextViewErrorMsg.setText(mErrorMessage);
            }
        } else {

            mErrorMessage = savedInstanceState.getString(States.STATE_ERROR);

            mTextViewErrorMsg.setText(mErrorMessage);

        }


        mButtonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);

        mErrorMessage = mTextViewErrorMsg.getText().toString();
        savedInstanceState.putString(States.STATE_ERROR, mErrorMessage);
    }
}
