package com.example.pets.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.example.pets.handler.AccountsAlertHandler;
import com.example.pets.network.NetworkStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ImageView backButton;
    TextView signUpTextView;

    TextView infoTextView;

    Button logInButton;

    EditText userNameEditTextView;
    EditText passwordEditTextView;

    AccountsAlertHandler accountsAlertHandler;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_login);

        setUp();

    }

    void setUp() {
        //getting firebase instances
        mAuth = FirebaseAuth.getInstance();


        //getting views by their ids
        backButton = findViewById(R.id.signUpActivity_backButton_imageView);
        signUpTextView = findViewById(R.id.loginActivity_signUpButton_textView);
        logInButton = findViewById(R.id.loginActivity_loginButton_button);
        infoTextView = findViewById(R.id.loginActivity_introText_textView);
        passwordEditTextView = findViewById(R.id.signUpActivity_password_textInputLayout);
        userNameEditTextView = findViewById(R.id.signUpActivity_username_textInputLayout);


        accountsAlertHandler = new AccountsAlertHandler(this, LoginActivity.this, "");

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

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn(userNameEditTextView.getText().toString(), passwordEditTextView.getText().toString());
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

    void logIn(String username, String password){
        accountsAlertHandler.show();
        if(!(new NetworkStatus(this)).isNetworkAvailable()){
            accountsAlertHandler.hideProgressWithInfo("It seems that you are not connected to Internet!!", 3000);
            return;
        }

        accountsAlertHandler.setTitle("Loging you in");


        mAuth.signInWithEmailAndPassword(username, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        accountsAlertHandler.hideProgressWithInfo("Welcome!", 2000);

                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        accountsAlertHandler.hideProgressWithInfo(e.getMessage(), 5000);



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }


}
