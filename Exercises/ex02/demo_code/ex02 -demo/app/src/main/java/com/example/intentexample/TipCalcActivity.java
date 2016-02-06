package com.example.intentexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Guelor on 2016-01-14.
 */
public class TipCalcActivity extends Activity {

    private double price;
    private double percent;
    private double total;
    private TextView priceTextView;
    private TextView percentTextView;
    private TextView totalTexView;


    protected void onStart() {
        super.onStart();
    }

    protected void onRestart() {
        super.onRestart();

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        priceTextView =(TextView) findViewById(R.id.price);
        percentTextView =(TextView) findViewById(R.id.percent);
        totalTexView =(TextView) findViewById(R.id.total);

        priceTextView.setText(String.valueOf(getIntent().getExtras().getDouble(States.STATE_PRICE)));

        if (savedInstanceState == null) {
            //Creation of a new activity instance with no previously saved state
            Intent calcIntent = getIntent();
            Bundle calcBundle = calcIntent.getBundleExtra(States.STATE_BUNDLE);
            price   = calcBundle.getDouble(States.STATE_PRICE);
            percent = calcBundle.getDouble(States.STATE_PERCENT);
            total   = calcBundle.getDouble(States.STATE_TOTAL);

            priceTextView.setText(String.valueOf(price));
            percentTextView.setText(String.valueOf(percent));
            totalTexView.setText(String.valueOf(total));

        } else {
            //Recreation of an activity with previously saved state
            price   = savedInstanceState.getDouble(States.STATE_PRICE);
            percent = savedInstanceState.getDouble(States.STATE_PERCENT);
            total   = savedInstanceState.getDouble(States.STATE_TOTAL);

            priceTextView.setText(String.valueOf(price));
            percentTextView.setText(String.valueOf(percent));
            totalTexView.setText(String.valueOf(total));

        }

        final Button calculateBtn= (Button) findViewById(R.id.calculateBtn);
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double temp;

                try {
                    percent = Double.parseDouble(percentTextView.getText().toString().trim());
                    total = ((percent/100)*price)+price;

                } catch (NumberFormatException e) {
                    percent = 1;
                }
                totalTexView.setText(String.valueOf(total));
                Intent resultIntent = new Intent();
                Bundle totalAmountBundle = new Bundle();
                totalAmountBundle.putDouble(States.STATE_TOTAL, total);
                resultIntent.putExtra(States.STATE_BUNDLE, totalAmountBundle);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

        });



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble(States.STATE_PRICE, price);
        savedInstanceState.putDouble(States.STATE_PERCENT, percent);
        savedInstanceState.putDouble(States.STATE_TOTAL, total);


    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        /*
        NOTE: this lifecyle method is called only if there actual is a savedInstanceState bundle.
        This your code does not need to check whether savedInstanceState is null.
        It also means you won't always see this method being called when logging
        the lifecycle methods.
         */

        //Log.i(TAG, getString(R.string.onrestoreinstancestate));

    }

    protected void onPause() {
        super.onPause();

    }

    protected void onResume() {
        super.onResume();

    }


    protected void onStop() {
        super.onStop();


    }

    protected void onDestroy() {
        super.onDestroy();

    }


}
