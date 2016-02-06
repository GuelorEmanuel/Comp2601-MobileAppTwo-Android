package com.comp2601.threads;

import android.util.Log;

import java.util.Random;


public class BankAccount {

    private String TAG = "BankAccount: " + System.identityHashCode(this);
    private int balance;
    private Random mRandom = new Random(); //needed for experiments

    BankAccount(int initialBalance) {
        balance = initialBalance;
    }

    private  int getBalance() {
        return balance;
    }

    private  void setBalance(int i) {
        balance = i;
    }

    public  int balance() {
        return getBalance();
    }


    public synchronized void deposit(int amount) {
        int temp;
        temp = getBalance();
        delay();
        setBalance(temp + amount);
        Log.i(TAG, "DEPOSIT: " + amount);


    }


    public synchronized boolean withdraw(int amount) {
        int temp = getBalance();
        if (temp < amount)
            return false;
        setBalance(temp - amount);
        Log.i(TAG, "WITHDRAW: " + amount);

        return true;
    }

    private void delay() {
        try {
            Thread.sleep(mRandom.nextInt(90)+10);
        }
        catch (java.lang.InterruptedException e) {
        }
    }


}
