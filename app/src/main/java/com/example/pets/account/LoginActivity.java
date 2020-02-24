package com.example.pets.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pets.R;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    TextView signUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUp();

    }

    void setUp() {
        //getting views by their ids
        backButton = findViewById(R.id.loginActivity_backButton_imageView);
        signUpTextView = findViewById(R.id.loginActivity_signUpButton_textView);

        //setting on click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });




    }
}
