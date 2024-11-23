package com.example.nicolorossi_assignment2_2024;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EmailComposerActivity extends AppCompatActivity {
    public final static String MESSAGE_TO = "com.example.nicolorossi_assignment2_2024.MESSAGE_TO";
    public final static String MESSAGE_SUBJECT = "com.example.nicolorossi_assignment2_2024.MESSAGE_SUBJECT";
    public final static String MESSAGE_CONTENT = "com.example.nicolorossi_assignment2_2024.MESSAGE_CONTENT";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_composer);
    }

    public void save(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editTextTo = (EditText) findViewById(R.id.editTextTo);
        EditText editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        EditText editTextContent = (EditText) findViewById(R.id.editTextContent);

        String messageTo = editTextTo.getText().toString();
        String messageSubject = editTextSubject.getText().toString();
        String messageContent = editTextContent.getText().toString();
        intent.putExtra(MESSAGE_TO, messageTo);
        intent.putExtra(MESSAGE_SUBJECT, messageSubject);
        intent.putExtra(MESSAGE_CONTENT, messageContent);
        setResult(RESULT_OK, intent);
        finish();


    }
}
