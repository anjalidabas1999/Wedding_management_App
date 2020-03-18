package com.example.pets.account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pets.Classes.User;
import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.example.pets.handler.AccountsAlertHandler;
import com.example.pets.network.NetworkStatus;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    final int PROFILE_IMAGE_TAG = 100;

    ImageView backButton;

    Button signUpButton;

    AccountsAlertHandler accountsAlertHandler;

    CircleImageView profileImageFab;

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
    FirebaseFirestore mFireStore;
    FirebaseStorage mStorage;

    Uri mSelectedImage;

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

        accountsAlertHandler = new AccountsAlertHandler(this, SignUpActivity.this, "");

        nameAndPasswordEditText = nameTextInputLayout.getEditText();
        userEmailAndConfirmPasswordEditText = userNameTextInputLayout.getEditText();

        //getting instance of firebase object
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();


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
                fetchDataFromCurrentState();
                if(currentPage == 2 &&
                        !mName.equals("") && !mPassword.equals("") && !mUserName.equals("") &&
                        mName != null && mPassword != null && mUserName != null
                ){
                    signUp();
                }else{
                    currentPage = 1;
                    updateCurrentPage();
                    nameAndPasswordEditText.setText(mName.equals("") && mName != null? mName: "");
                    userEmailAndConfirmPasswordEditText.setText(mUserName.equals("") && mUserName != null? mUserName: "");
                    nameAndPasswordEditText.requestFocus();
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
                    toast(selectedImage.toString());

                    mSelectedImage = selectedImage;

                    profileImageFab.setImageURI(selectedImage);
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


        //updating the state of views
        currentPageTextView.setText("1");

        nameAndPasswordEditText.setText(mName);
        userEmailAndConfirmPasswordEditText.setText(mUserName);

        signUpButton.setText("Next");

        signUpButtonInfo.setVisibility(View.GONE);

        userNameTextInputLayout.setCounterEnabled(false);

        nameTextInputLayout.setHint("Name");
        userNameTextInputLayout.setHint("Username/Email");

        nameAndPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        userEmailAndConfirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        nameAndPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_person_outline_white),
                null, null, null);

        userEmailAndConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_mail),
                null, null, null);
    }

    void setPage2(){

        //updating the state of views
        currentPageTextView.setText("2");

        nameAndPasswordEditText.setText(mPassword);
        userEmailAndConfirmPasswordEditText.setText(mConfirmPassword);

        signUpButton.setText("Previous");

        signUpButtonInfo.setVisibility(View.VISIBLE);

        userNameTextInputLayout.setCounterEnabled(true);
        userNameTextInputLayout.setCounterMaxLength(12);

        nameTextInputLayout.setHint("Password");
        userNameTextInputLayout.setHint("Confirm Password");

        nameAndPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userEmailAndConfirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        nameAndPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);

        userEmailAndConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.ic_lock_outline_white),
                null, null, null);
    }

    void signUp(){
        accountsAlertHandler.show();
        if(!(new NetworkStatus(this)).isNetworkAvailable()){
            accountsAlertHandler.hideProgressWithInfo("It seems that you are not connected to Internet!!", 3000);
            return;
        }

        accountsAlertHandler.setTitle("Signing you in");

        fetchDataFromCurrentState();
        if(!(mPassword.equals(mConfirmPassword))){
            nameAndPasswordEditText.requestFocus(1);
            userEmailAndConfirmPasswordEditText.requestFocus();
            accountsAlertHandler.hideProgressWithInfo("Password doesn't match", 2000);

        }else{
            accountsAlertHandler.setTitle("Signing you in");

            mAuth.createUserWithEmailAndPassword(mUserName, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       uploadImage(task.getResult().getUser().getUid());
                    }else{
                        accountsAlertHandler.hideProgressWithInfo(task.getException().getMessage(), 5000);
                    }
                }
            });

        }
    }

    void uploadImage(String uid) {
        accountsAlertHandler.setTitle("Setting profile image...");

        if(mSelectedImage==null || mSelectedImage.toString().equals("")){
            StorageReference mRef = mStorage.getReference().child("user/default.jpeg");
            mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    setUpServerDatabase(uid, uri.toString());
                }
            });

        }else{
            StorageReference mRef = mStorage.getReference().child("user/"+uid+".jpeg");
            mRef.putFile(mSelectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                setUpServerDatabase(uid, uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                accountsAlertHandler.hideProgressWithInfo("Error fetching profile image", 3000);
                            }
                        });
                    }else{
                        accountsAlertHandler.hideProgressWithInfo(task.getException().getMessage(), 3000);
                    }

                }
            });
        }

    }

    void setUpServerDatabase(String uid, String imageUrl){
        accountsAlertHandler.setTitle("Setting up database");

        mFireStore.collection("user").document(uid).set(new User(mName, mUserName, mPassword, imageUrl))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        mFireStore.collection("user").document(uid)
                                .collection("data").document("data")
                                .set(new HashMap<String, Integer>(){
                                    {
                                        put("size", 0);
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                accountsAlertHandler.hideProgressWithInfo("Success", 2000);
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                accountsAlertHandler.hideProgressWithInfo(e.getMessage(), 5000);
            }
        });

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
