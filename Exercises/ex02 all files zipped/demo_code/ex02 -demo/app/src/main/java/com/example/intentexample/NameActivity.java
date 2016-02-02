package com.example.intentexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class NameActivity extends Activity {

    private  final String TAG = this.getClass().getSimpleName() + "@ " + System.identityHashCode(this);
    private String firstname;
    private String lastname;
    private TextView firstNameTextView;
    private TextView lastNameTextView;

    protected void onStart() {
        super.onStart();

        Log.i(TAG, getString(R.string.onstart));

    }

    protected void onRestart() {
        super.onRestart();

        Log.i(TAG, getString(R.string.onrestart));

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Log.i(TAG, getString(R.string.oncreate));

        firstNameTextView = (TextView) findViewById(R.id.firstName);
        lastNameTextView = (TextView) findViewById(R.id.lastName);


        if (savedInstanceState == null) {
            //Creation of a new activity instance with no previously saved state
            Log.i(TAG, "OnCreate: Creation of new instance (no savedInstanceState)");
            Intent nameIntent = getIntent();
            Bundle nameBundle = nameIntent.getBundleExtra(States.STATE_BUNDLE);
            firstname = nameBundle.getString(States.STATE_FIRSTNAME);
            lastname = nameBundle.getString(States.STATE_LASTNAME);
            firstNameTextView.setText(firstname);

            lastNameTextView.setText(lastname);
        } else {
            //Recreation of an activity with previously saved state
            Log.i(TAG, "OnCreate: Recovering previously saved state information");
            firstname = savedInstanceState.getString(States.STATE_FIRSTNAME);
            lastname = savedInstanceState.getString(States.STATE_LASTNAME);
            firstNameTextView.setText(firstname);
            lastNameTextView.setText(lastname);

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

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, getString(R.string.onsaveinstancestate));
        firstname = firstNameTextView.getText().toString();
        lastname = lastNameTextView.getText().toString();
        savedInstanceState.putString(States.STATE_FIRSTNAME, firstname);
        savedInstanceState.putString(States.STATE_LASTNAME, lastname);


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

        Log.i(TAG, getString(R.string.onrestoreinstancestate));

    }

    protected void onStop() {
        super.onStop();

        Log.i(TAG, getString(R.string.onstop));

    }

    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, getString(R.string.ondestroy));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_main:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
