package com.comp2601.threads;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;


public class MainActivity extends Activity {

    private EditText mBank1Text  =  null;
    private EditText mBank2Text  = null;
    private EditText mTotalText  = null;
    private Button mButtonAction = null;

    ThreadWithControl mThread1;
    ThreadWithControl mThread2;

    Random mGenerator = new Random();


    BankAccount mBankAccount1;
    BankAccount mBankAccount2;

    Boolean mIsRunningFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBank1Text    = (EditText) findViewById(R.id.editTextAccount1);
        mBank2Text    = (EditText) findViewById(R.id.editTextAccount2);
        mTotalText    = (EditText) findViewById(R.id.editTextTotal);

        mBankAccount1 = new BankAccount(1000);
        mBankAccount2 = new BankAccount(2000);

        mBank1Text.setText("" + mBankAccount1.balance());
        mBank2Text.setText("" + mBankAccount2.balance());
        mTotalText.setText("" + (mBankAccount1.balance() + mBankAccount2.balance()));

        mButtonAction = (Button) findViewById(R.id.buttonAction);

        mButtonAction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mIsRunningFlag = !mIsRunningFlag;

                if(mIsRunningFlag) mButtonAction.setText("STOP");
                else mButtonAction.setText("START");

                if(mIsRunningFlag) {
                    //transfer 1->2
                    mThread1 = new ThreadWithControl(mBankAccount1, mBankAccount2,
                            MainActivity.this, mBank1Text, mBank2Text,mTotalText );

                    //transfer 2->1
                    mThread2 = new ThreadWithControl(mBankAccount2,mBankAccount1,
                            MainActivity.this, mBank1Text, mBank2Text, mTotalText);

                    mThread1.start();
                    mThread2.start();
                }
                else{
                    mThread1.quit();
                    mThread2.quit();
                    mBank1Text.setText("" + mBankAccount1.balance());
                    mBank2Text.setText(""+ mBankAccount2.balance());
                    mTotalText.setText(""+ (mBankAccount1.balance() + mBankAccount2.balance()));
                }

            }

        });



    }

}
