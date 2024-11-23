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

    ActivityResultLauncher<Intent> getMessage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result == null) {
                        Log.e("onActivityResult", "The result returned from activity was null");
                        return;
                    }

                    setAllEmailProperties(result);

                    if(areAllPropertiesValid(emailTo, emailContent, emailSubject)){

                        // Writing email in TextView and enabling the button
                        TextView Email = (TextView) findViewById(R.id.textView4);
                        Email.setText(composeEmail(emailTo, emailSubject, emailContent));
                        Button button = (Button) findViewById(R.id.buttonSend);
                        button.setEnabled(true);

                    }
                    else {
                        TextView Email = (TextView) findViewById(R.id.textView4);
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

    public void implicitIntentCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

    public void implicitIntentGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivity(intent);
    }

    public void explicitIntentComposer(View view) {
        Intent intent = new Intent(this, EmailComposerActivity.class);

        getMessage.launch(intent);
    }

    public void implicitIntentDefaultEmail(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:" + emailTo + "?subject=" + emailSubject + "&body=" + emailContent);

        intent.setData(data);
        startActivity(intent);

    }

    private static void setAllEmailProperties(ActivityResult result) {
        setEmailTo(result);
        setEmailContent(result);
        setEmailSubject(result);
    }
    private static void setEmailTo(ActivityResult result){
        emailTo = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_TO);
    }
    private static void setEmailContent(ActivityResult result){
        emailContent = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_CONTENT);
    }
    private static void setEmailSubject(ActivityResult result){
        emailSubject = result.getData().getStringExtra(EmailComposerActivity.MESSAGE_SUBJECT);
    }

    public static String composeEmail(String emailTo, String emailSubject, String emailContent) {
        return String.format("To: %s\nSubject: %s\nContent: %s\n", emailTo, emailSubject, emailContent);
    }

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