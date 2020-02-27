package com.example.pets.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class SignUpActivity extends AppCompatActivity {

    final int PROFILE_IMAGE_TAG = 100;

    ImageView backButton;

    Button signUpButton;

    ProgressBar progressBar;

    FloatingActionButton profileImageFab;

    EditText nameAndPasswordEditText;
    EditText userEmailAndConfirmPasswordEditText;

    TextInputLayout userNameTextInputLayout;
    TextInputLayout nameTextInputLayout;

    TextView currentPageTextView;
    TextView signUpButtonInfo;

    int currentPage = 1;

    String mName="";
    String mUserName="";
    String mPassword="";
    String mConfirmPassword="";

    FirebaseAuth mAuth;

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
        profileImageFab = findViewById(R.id.signUpActivity_addProfileImage_fab);
        signUpButtonInfo = findViewById(R.id.loginActivity_signUpButtonInfo_textView);

        progressBar = new ProgressBar(this);

        nameAndPasswordEditText = nameTextInputLayout.getEditText();
        userEmailAndConfirmPasswordEditText = userNameTextInputLayout.getEditText();

        //getting instance of firebase object
        mAuth = FirebaseAuth.getInstance();


        //adding on click listeners

        //for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //for signUp button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchDataFromCurrentState();
                currentPage = currentPage==1 ? 2:1;
                updateCurrentPage();

            }
        });

        signUpButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(currentPage == 2){
                    signUp();
                }
                return true;
            }
        });

        //for profile uploading fab
        profileImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("tag", mName+"______"+mUserName+"_________"+mPassword+"________"+mConfirmPassword);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),PROFILE_IMAGE_TAG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PROFILE_IMAGE_TAG:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Bitmap bt = getPath(selectedImage);
                    profileImageFab.setImageBitmap(bt);
                }
        }
    }

    private Bitmap getPath(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        // cursor.close();
        // Convert file path into bitmap image using below line.
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

    void updateCurrentPage(){
        switch (currentPage){
            case 1:
                setPage1();
                break;
            case 2:
                setPage2();
                break;
        }
    }

    void setPage1(){

        //getting previous text from name and email and storing in a var

        Log.d("DATA", "Data from page2 " + mPassword + "_______" +mConfirmPassword);

        //updating the state of views
        currentPageTextView.setText("1");

        signUpButton.setText("Next");

        signUpButtonInfo.setVisibility(View.INVISIBLE);

        userNameTextInputLayout.setCounterEnabled(false);
        //userNameTextInputLayout.setCounterMaxLength(12);

        nameTextInputLayout.setHint("Name");
        userNameTextInputLayout.setHint("Username/Email");

        nameAndPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        userEmailAndConfirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        nameAndPasswordEditText.setText(null);
        userEmailAndConfirmPasswordEditText.setText(null);


        nameAndPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_person_outline_white),
                null, null, null);

        userEmailAndConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_mail),
                null, null, null);
    }

    void setPage2(){
        //getting previous text from name and email and storing in a var


        Log.d("DATA", "Data from page1 " + mName + "_______" +mUserName);

        //updating the state of views
        currentPageTextView.setText("2");

        signUpButton.setText("Previous");

        signUpButtonInfo.setVisibility(View.VISIBLE);

        userNameTextInputLayout.setCounterEnabled(true);
        userNameTextInputLayout.setCounterMaxLength(12);

        nameTextInputLayout.setHint("Password");
        userNameTextInputLayout.setHint("Confirm Password");

        nameAndPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userEmailAndConfirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        nameAndPasswordEditText.setText(null);
        userEmailAndConfirmPasswordEditText.setText(null);


        nameAndPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);

        userEmailAndConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);
    }

    void signUp(){
        fetchDataFromCurrentState();
        if(!(mPassword.equals(mConfirmPassword) ? true:false)){
            nameAndPasswordEditText.requestFocus(1);
            userEmailAndConfirmPasswordEditText.requestFocus();

        }else{
            toast("pass");

            progressBar.setIndeterminate(true);

            mAuth.createUserWithEmailAndPassword(mUserName, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        toast("SignUp successfull: "+task.getResult().getUser().getEmail());
                        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    }

                }
            });


            mAuth.signInWithEmailAndPassword(mUserName, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });
        }
    }

    void fetchDataFromCurrentState(){
        if(currentPage==1){
            mName = nameAndPasswordEditText.getText().toString();
            mUserName = userEmailAndConfirmPasswordEditText.getText().toString();
        }else{
            mPassword = nameAndPasswordEditText.getText().toString();
            mConfirmPassword = userEmailAndConfirmPasswordEditText.getText().toString();
        }
    }

    void toast(String message){
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();

    }


}
