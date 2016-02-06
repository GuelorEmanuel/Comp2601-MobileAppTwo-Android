package com.example.intentexample;

/*
Exercise: Add another explicit activity
Explicit Activity:
Calculate Tip Activity
Implicit Activity:
Email Activity: launches an email client and sends an email
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {


    private  final String TAG = this.getClass().getSimpleName() + " @" + System.identityHashCode(this);

    private EditText urlName;
    private EditText firstName;
    private EditText lastName;
    private EditText priceText;

    private String userFirstName;
    private String userLastName;
    private String userURL;
    private static final int TIP_CALC_ACTIVITY_CODE = 1;


    protected void onStart() {
        super.onStart();

        Log.i(TAG, getString(R.string.onstart));

    }

    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, getString(R.string.onrestart));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, getString(R.string.oncreate));

        urlName   = (EditText) findViewById(R.id.webSiteName);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName  = (EditText) findViewById(R.id.lastName);
        priceText = (EditText) findViewById(R.id.price);

        final Button webViewBtn = (Button) findViewById(R.id.startWebViewBtn);
        webViewBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userURL = urlName.getText().toString();
                if (userURL.length() != 0) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(userURL));
                    startActivity(webIntent);
                }
            }
        });

        final Button nameActivity = (Button) findViewById(R.id.nameActivity);
        nameActivity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userFirstName = firstName.getText().toString();
                userLastName = lastName.getText().toString();
                Intent nameIntent = new Intent(MainActivity.this, NameActivity.class);
                //or you can use intent.putExtra or parcelable
                Bundle nameInfoBundle = new Bundle();
                nameInfoBundle.putString(States.STATE_FIRSTNAME, userFirstName);
                nameInfoBundle.putString(States.STATE_LASTNAME, userLastName);
                nameIntent.putExtra(States.STATE_BUNDLE, nameInfoBundle);

                startActivity(nameIntent);
            }
        });

        final Button calculatorTipBtn= (Button) findViewById(R.id.calcActivity);
        calculatorTipBtn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v){
              double price;
                try {
                    price = Double.parseDouble(priceText.getText().toString().trim());
                }catch (NumberFormatException e) {
                    price = 0;
                }
                Intent calcIntent = new Intent(MainActivity.this, TipCalcActivity.class);
                Bundle amountsBundle = new Bundle();
                amountsBundle.putDouble(States.STATE_PRICE, price);
                calcIntent.putExtra(States.STATE_BUNDLE, amountsBundle);

                startActivityForResult(calcIntent, TIP_CALC_ACTIVITY_CODE);
            }

        });




        if (savedInstanceState != null) {
            //retrieving save instance state can also be done in the onRestoreInstanceState method
            //refer to http://developer.android.com/training/basics/activity-lifecycle/recreating.html
            //The fact that this is not null indicates the re-creation of a previously destroyed activity

            Log.i(TAG, "OnCreate: Restoring from savedInstanceState");
            userFirstName = savedInstanceState.getString(States.STATE_FIRSTNAME);
            userLastName = savedInstanceState.getString(States.STATE_LASTNAME);
            userURL = savedInstanceState.getString(States.STATE_URL);

            urlName.setText(userURL);
            firstName.setText(userFirstName);
            lastName.setText(userLastName);
        }
        else{
            //This the the creation of a new activity instance with no previously saved state
            Log.i(TAG, "OnCreate: no savedInstanceState");
        }
    }

    protected void onPause() {
        super.onPause();

        Log.i(TAG, getString(R.string.onpause));

    }

    protected void onResume() {
        super.onResume();

        Log.i(TAG, getString(R.string.onresume));

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        /*
        This is called before the activity is stopped i.e. before onStop() is invoked.
         */

        super.onSaveInstanceState(savedInstanceState);

        Log.i(TAG, getString(R.string.onsaveinstancestate));
        savedInstanceState.putString(States.STATE_FIRSTNAME, userFirstName);
        savedInstanceState.putString(States.STATE_LASTNAME, userLastName);
        savedInstanceState.putString(States.STATE_URL, userURL);


    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        /*
        NOTE: this lifecyle method is called only if there actuall is a savedInstanceState bundle.
        This your code does not need to check whether savedInstanceState is null.
        It also means you won't always see this method being called when logging
        the lifecycle methods.
         */

        Log.i(TAG, getString(R.string.onrestoreinstancestate));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData){
        Log.i(TAG, getString(R.string.onActivityResult));

        if(requestCode == TIP_CALC_ACTIVITY_CODE){
            if(resultCode == RESULT_OK){

                Bundle resultBundle = resultData.getBundleExtra(States.STATE_BUNDLE);
                double newTotal = resultBundle.getDouble(States.STATE_TOTAL, 0);
                priceText.setText("" + newTotal);

            }
        }
    }

    protected void onStop() {
        super.onStop();

        Log.i(TAG, getString(R.string.onstop));

    }

    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, getString(R.string.ondestroy));

    }

}
