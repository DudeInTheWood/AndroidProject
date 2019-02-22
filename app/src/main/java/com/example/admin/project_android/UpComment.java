package com.example.admin.project_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpComment extends AppCompatActivity {
    private Button okbtn, cancelbtn;
    private EditText commentField;
    String key;
    private Intent intent;
    //firebase
    DatabaseReference fireRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_comment);
        okbtn = findViewById(R.id.okbtn);
        cancelbtn = findViewById(R.id.cencelbtn);
        commentField = (EditText) findViewById(R.id.commentField);

        intent = getIntent();
        key = intent.getStringExtra("key");

        fireRef = FirebaseDatabase.getInstance().getReference("comment");
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInData checkInData = new CheckInData(key,commentField.getText().toString());
                fireRef.push().setValue(checkInData);
                Toast.makeText(UpComment.this, "posting....", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
