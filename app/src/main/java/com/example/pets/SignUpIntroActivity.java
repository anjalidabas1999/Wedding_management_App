package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpIntroActivity extends AppCompatActivity {

    Button loginButton;
    ImageView petIcon;
    TextView bottomSignUpTextView;

    Intent logInIntent;
    Intent signUpIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_intro);

        setUp();

    }

    void setUp(){
        //getting views from their ids
        loginButton= findViewById(R.id.login_intro_loginButton);
        petIcon= findViewById(R.id.pet_icon_imageView);
        bottomSignUpTextView = findViewById(R.id.bottomTextView);

        //setting up intents
        logInIntent = new Intent(SignUpIntroActivity.this, LoginActivity.class);


        //setting onClick callbacks
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating options for shared transition
                Pair[] pairs = new Pair[2];
                //getResources().getString(R.string.introSignUp_to_Login_transition_backButton)

                pairs[0] = new Pair<View, String>(loginButton, getResources().getString(R.string.introSignUp_to_Login_transition_loginButton));
                pairs[1] = new Pair<View, String>(petIcon, "image");

                final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpIntroActivity.this, pairs);

                startActivity(logInIntent, options.toBundle());
            }
        });

        bottomSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
