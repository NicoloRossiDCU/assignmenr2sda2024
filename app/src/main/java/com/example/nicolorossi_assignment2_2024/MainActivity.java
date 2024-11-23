package com.example.nicolorossi_assignment2_2024;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static String emailTo;
    private static String emailContent;
    private static String emailSubject;

    // the activity result launcher will call a secondary activity and return some data from there
    ActivityResultLauncher<Intent> getMessage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // check if result exists
                    if (result == null) {
                        Log.e("onActivityResult", "The result returned from activity was null");
                        return;
                    }
                    // use the result from the activity to set the data for email
                    setAllEmailProperties(result);
                    // validating the property before creating email in textview
                    TextView Email = (TextView) findViewById(R.id.textView4);
                    if(areAllPropertiesValid(emailTo, emailContent, emailSubject)){

                        // Writing email in TextView and enabling the button
                        Email.setText(composeEmail(emailTo, emailSubject, emailContent));
                        Button button = (Button) findViewById(R.id.buttonSend);
                        button.setEnabled(true);

                    }
                    else {
                        // if the properties are not valid log error and show it in text view
                        Email.setText(getResources().getString(R.string.error_data_invalid));
                        Log.e("onActivityResult", "Some values are missing from email");
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // implicit intent that will call the default camera app
    public void implicitIntentCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    // implicit intent that will call image gallery (the type is set to any format)
    public void implicitIntentGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
    }

    // this method will call a secondary activity
    public void explicitIntentComposer(View view) {
        Intent intent = new Intent(this, EmailComposerActivity.class);

        getMessage.launch(intent);
    }

    // this method will open the default email app and compose the message
    public void implicitIntentDefaultEmail(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + emailTo + "?subject=" + emailSubject + "&body=" + emailContent);

        intent.setData(data);
        startActivity(intent);

    }

    // this method is used to set the properties in one place
    private static void setAllEmailProperties(ActivityResult result) {
        setEmailTo(result);
        setEmailContent(result);
        setEmailSubject(result);
    }

    // this are the single set methods
    private static void setEmailTo(ActivityResult result){
        emailTo = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_TO);
    }
    private static void setEmailContent(ActivityResult result){
        emailContent = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_CONTENT);
    }
    private static void setEmailSubject(ActivityResult result){
        emailSubject = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_SUBJECT);
    }

    // Creating email as single string
    public static String composeEmail(String emailTo, String emailSubject, String emailContent) {
        return String.format("To: %s\nSubject: %s\nContent: %s\n", emailTo, emailSubject, emailContent);
    }

    // this method validate the properties (only check if they are empty)
    private static boolean areAllPropertiesValid(String emailTo, String emailContent, String emailSubject){
        boolean isValid = true;
        if(emailTo.isEmpty()) {
            Log.d("onActivityResult", "Email TO is empty.");
            isValid = false;
        }
        if(emailContent.isEmpty()) {
            Log.d("onActivityResult", "Email CONTENT is empty.");
            isValid = false;
        }
        if(emailSubject.isEmpty()) {
            Log.d("onActivityResult", "Email SUBJECT is empty.");
            isValid = false;
        }

        return isValid;
    }
}