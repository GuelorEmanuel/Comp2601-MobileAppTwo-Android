package com.comp2601.threads;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import java.util.Random;


public class ThreadWithControl extends Thread {

    private boolean mRunning;

    BankAccount    mFromAccount;
    BankAccount    mToAccount;
    Random         mGenerator;
    MainActivity   mainActivity;
    EditText       mFromAccountTextView;
    EditText       mToAccountTextView;
    EditText       mTotalTextView;




    public ThreadWithControl(BankAccount mFromAccount,
                             BankAccount mToAccount, MainActivity mainActivity,
                             EditText mFromAccountTextView, EditText mToAccountTextView,
                             EditText    mTotalTextView)
    {

        this.mRunning              = false;
        this.mFromAccount          = mFromAccount;
        this.mToAccount            = mToAccount;
        mGenerator                 = new Random();
        this.mainActivity          = mainActivity;
        this.mFromAccountTextView  = mFromAccountTextView;
        this.mToAccountTextView    = mToAccountTextView;
        this.mTotalTextView        = mTotalTextView;

    }

    public void quit() {
        mRunning = false;
    }

    public void run() {
        mRunning = true;
        while (mRunning){

            int amount = mGenerator.nextInt(mFromAccount.balance());

            mFromAccount.withdraw(amount);
            mToAccount.deposit(amount);


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFromAccountTextView.setText("" + mFromAccount.balance());
                    mToAccountTextView.setText("" + mToAccount.balance());
                    mTotalTextView.setText("" + (mFromAccount.balance() + mToAccount.balance()));
                }
            });


        }
    }
}
