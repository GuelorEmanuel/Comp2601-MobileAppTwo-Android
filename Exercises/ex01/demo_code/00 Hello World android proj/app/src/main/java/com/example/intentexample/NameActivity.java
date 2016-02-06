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

    private  final String TAG = NameActivity.class.getSimpleName() + " @" + System.identityHashCode(this);

    private String firstname;
    private String lastname;
    private TextView firstNameTextView;
    private TextView lastNameTextView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        Log.i(TAG, getString(R.string.oncreate));

        firstNameTextView = (TextView) findViewById(R.id.firstName);
        lastNameTextView = (TextView) findViewById(R.id.lastName);

        //obtain data passed to intent
        Intent nameIntent = getIntent();
        Bundle nameBundle = nameIntent.getBundleExtra(States.STATE_BUNDLE);
        firstname = nameBundle.getString(States.STATE_FIRSTNAME);
        lastname = nameBundle.getString(States.STATE_LASTNAME);
        firstNameTextView.setText(firstname);
        lastNameTextView.setText(lastname);


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
