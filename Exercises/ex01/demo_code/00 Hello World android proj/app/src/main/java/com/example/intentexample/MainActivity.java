package com.example.intentexample;

/*
Hello World example
Sean's Intent Example stripped of activity lifecyle code for now.

Exercise:
Set Log level to "Info" to see only the info log messages
Launch the app and run the name activity.
Rotate the test device between portrait and landscape. Can you explain the "onCreate()" output in the log?
Can you explain how the "go to main activity" menu item in name activity works (how do you get back to the main activity?
*/


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {


    private  final String TAG = MainActivity.class.getSimpleName() + " @" + System.identityHashCode(this);

    private EditText urlName;
    private EditText firstName;
    private EditText lastName;
    private EditText emailAddressText;
    private EditText emailMessageBodyText;
    private String userFirstName;
    private String userLastName;
    private String userURL;
    private String emailAddress;
    private String emailBody;
    private String emailSubject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, getString(R.string.oncreate));
        Log.i(TAG, getDeviceInfo());

        urlName              = (EditText) findViewById(R.id.webSiteName);
        firstName            = (EditText) findViewById(R.id.firstName);
        lastName             = (EditText) findViewById(R.id.lastName);
        emailAddressText     = (EditText) findViewById(R.id.emailAddressText);
        emailMessageBodyText = (EditText) findViewById(R.id.emailMessageBodyText);

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

        final Button emailActivity = (Button) findViewById(R.id.sendEmail);
        emailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                emailAddress    = emailAddressText.getText().toString();
                emailSubject    = "Tutorial One";
                emailBody       = emailMessageBodyText.getText().toString();
                String tempBody = "device info";

                if (emailBody.toLowerCase().equals(tempBody.toLowerCase()))
                    emailBody = getDeviceInfo();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+ emailAddress));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

                startActivity(Intent.createChooser(emailIntent,"Email Client ..."));

            }
        });

    }

    private String getDeviceInfo(){
        String s = "Device Info:";
        try {
            s += "\n OS Version: "      + System.getProperty("os.version") +
                    "(" + Build.VERSION.INCREMENTAL + ")";
            s += "\n OS API Level: "    + Build.VERSION.SDK_INT;
            s += "\n Model (and Product): " + Build.MODEL +
                    " ("+ Build.PRODUCT + ")";
            s += "\n RELEASE: "         + Build.VERSION.RELEASE;
            s += "\n BRAND: "           + android.os.Build.BRAND;
            s += "\n DISPLAY: "         + android.os.Build.DISPLAY;
            s += "\n HARDWARE: "        + android.os.Build.HARDWARE;
            s += "\n Build ID: "        + android.os.Build.ID;
            s += "\n MANUFACTURER: "    + android.os.Build.MANUFACTURER;
            s += "\n SERIAL: "          + android.os.Build.SERIAL;
            s += "\n USER: "            + android.os.Build.USER;
            s += "\n HOST: "            + android.os.Build.HOST;

        } catch(Exception e) {
            Log.e(TAG, "Error getting Device INFO");
        }
        return s;
    }


}
