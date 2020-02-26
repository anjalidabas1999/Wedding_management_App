package com.example.pets.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.MotionEventCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    ImageView backButton;

    Button signUpButton;

    EditText nameAndPasswordEditText;
    EditText userEmailAndConfirmPasswordEditText;

    TextInputLayout userNameTextInputLayout;
    TextInputLayout nameTextInputLayout;

    TextView currentPageTextView;

    int currentPage = 1;

    String mName;
    String mUserName;
    String mPassword;
    String mConfirmPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUp();
    }

    private void setUp() {
        //getting views by their ids
        backButton = findViewById(R.id.signUpActivity_backButton_imageView);
        signUpButton = findViewById(R.id.signUpActivity_signUp_button);
        currentPageTextView = findViewById(R.id.signUpActivity_currentPage_counter_textView);
        nameTextInputLayout = findViewById(R.id.signUpActivity_name_textInputLayout);
        userNameTextInputLayout = findViewById(R.id.signUpActivity_userName_textInputLayout);

        nameAndPasswordEditText = nameTextInputLayout.getEditText();
        userEmailAndConfirmPasswordEditText = userNameTextInputLayout.getEditText();

        
        //adding on click listeners
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCurrentPage();
            }
        });

    }

    void updateCurrentPage(){

        //getting previous text from name and email and storing in a var
        mName = nameAndPasswordEditText.getText().toString();
        mUserName = userEmailAndConfirmPasswordEditText.getText().toString();


        //updating the state of views
        userNameTextInputLayout.setCounterEnabled(true);
        userNameTextInputLayout.setCounterMaxLength(12);

        nameTextInputLayout.setHint("Password");
        userNameTextInputLayout.setHint("Confirm Password");

        nameAndPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userEmailAndConfirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);


        nameAndPasswordEditText.setText(null);
        userEmailAndConfirmPasswordEditText.setText(null);


        nameAndPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);

        userEmailAndConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);




    }


}
