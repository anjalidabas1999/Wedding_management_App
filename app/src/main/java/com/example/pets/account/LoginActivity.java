package com.example.pets.account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pets.R;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    TextView signUpTextView;

    TextView infoTextView;

    Button logInButton;

    EditText userNameEditTextView;
    EditText passwordEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUp();

    }

    void setUp() {
        //getting views by their ids
        backButton = findViewById(R.id.signUpActivity_backButton_imageView);
        signUpTextView = findViewById(R.id.loginActivity_signUpButton_textView);
        logInButton = findViewById(R.id.loginActivity_loginButton_button);
        infoTextView = findViewById(R.id.loginActivity_introText_textView);
        passwordEditTextView = findViewById(R.id.signUpActivity_userName_textInputLayout);
        userNameEditTextView = findViewById(R.id.signUpActivity_name_textInputLayout);

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
                //calling @sentSignUpIntent
                sentSignUpIntent();
            }
        });

    }

    void sentSignUpIntent(){
        //creating intent instance
        Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);

        //creating options for shared transition
        Pair[] pairs = new Pair[5];
        //getResources().getString(R.string.introSignUp_to_Login_transition_backButton)

        pairs[0] = new Pair(logInButton, getResources().getString(R.string.introSignUp_to_Login_transition_loginButton));
        pairs[1] = new Pair(backButton, getResources().getString(R.string.introSignUp_to_Login_transition_backButton));
        pairs[2] = new Pair(infoTextView, getResources().getString(R.string.login_to_signUp_infoText));
        pairs[3] = new Pair(userNameEditTextView, getResources().getString(R.string.login_to_signUp_userName_editText));
        pairs[4] = new Pair(passwordEditTextView, getResources().getString(R.string.login_to_signUp_password_editText));


        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
        startActivity(signUpIntent, options.toBundle());
    }
}
