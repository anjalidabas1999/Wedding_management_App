package com.example.pets.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.Classes.User;
import com.example.pets.R;
import com.example.pets.account.SignUpActivity;
import com.example.pets.handler.AccountsAlertHandler;
import com.example.pets.handler.AlertHandler;
import com.example.pets.interfaces.AlertClickListener;
import com.example.pets.network.NetworkStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountsActivity extends AppCompatActivity {

    final int PROFILE_IMAGE_TAG = 0101;

    int state = 0; // 0: non-editable, 1: editable

    ImageButton backButton;
    ImageButton editButton;

    EditText nameEditText;
    EditText usernameEditText;
    EditText passwordEditText;

    ImageView topProfileImageView;

    CircleImageView profileImage;

    TextView totalPetsTextView;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    User user;

    AlertHandler alertHandler;

    AccountsAlertHandler accountsAlertHandler;

    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        setUp();
    }

    private void setUp() {
        backButton = findViewById(R.id.accountsActivity_backButton_imageButton);
        editButton = findViewById(R.id.accountsActivity_edit_imageButton);
        nameEditText = findViewById(R.id.accountsActivity_name_editText);
        usernameEditText = findViewById(R.id.accountsActivity_username_editText);
        passwordEditText = findViewById(R.id.accountsActivity_password_editText);
        topProfileImageView = findViewById(R.id.accountsActivity_topProfileImage_imageView);
        profileImage = findViewById(R.id.accountsActivity_profile_circularImageView);
        totalPetsTextView = findViewById(R.id.accountsActivity_totalPets_textView);

        alertHandler = new AlertHandler(this, AccountsActivity.this, "", null);
        accountsAlertHandler = new AccountsAlertHandler(this, AccountsActivity.this, "");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState();
            }
        });

        fetchData();

    }

    void fetchData(){
        accountsAlertHandler.show();
        if(!(new NetworkStatus(this)).isNetworkAvailable()){
            accountsAlertHandler.hideProgressWithInfo("It seems that you are not connected to Internet!!", 3000);
            return;
        }
        accountsAlertHandler.setTitle("Loading");

        mFirestore.collection("user").document(mAuth.getUid())
                .collection("data").document("data").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    int size = task.getResult().getLong("size").intValue();
                    totalPetsTextView.setText(""+size);
                }else if(!task.isSuccessful() && task.getException() != null){
                    totalPetsTextView.setText(task.getException().getMessage());
                }
            }
        });

        mFirestore.collection("user").document(mAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    user = task.getResult().toObject(User.class);
                    updateUi();
                    accountsAlertHandler.hideProgressWithInfo("Done", 2000);
                }
            }
        });
    }

    void updateUi(){
        nameEditText.setText(user.getName());
        usernameEditText.setText(user.getUsername());
        passwordEditText.setText(user.getPassword());

        RequestCreator requestCreator = Picasso.get().load(user.getImage()).placeholder(R.drawable.bg6);

        requestCreator.into(topProfileImageView);
        requestCreator.into(profileImage);


    }

    void changeState(){
        state = state==0 ? 1:0;
        setState(state);
    }

    void setState(int st){
        switch (st){
            case 0:
                // settings for state 0
                setButtonState_0();
                setEnabledOfEditTexts(false);
                break;
            case 1:
                // setting for state 1
                setButtonState_1();
                setEnabledOfEditTexts(true);
                break;

        }
    }

    void setButtonState_1(){
        editButton.animate().rotation(360).setDuration(500).start();
        editButton.setImageResource(R.drawable.ic_check_white_24dp);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertHandler.setTitle("Are you sure");
                alertHandler.setAlertClickListener(new AlertClickListener() {
                    @Override
                    public void onNegativeClick() {
                        alertHandler.dismiss();
                    }

                    @Override
                    public void onPositiveClick() {
                        alertHandler.dismiss();
                        finish();
                    }
                });
                alertHandler.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertHandler.setTitle("Save changes");
                alertHandler.setAlertClickListener(new AlertClickListener() {
                    @Override
                    public void onNegativeClick() {
                        toast("keep editing");
                        alertHandler.dismiss();
                    }

                    @Override
                    public void onPositiveClick() {
                        alertHandler.hideProgressWithInfo("Done", 1);
                        toast("Save changes");
                        changeState();
                    }
                });
                alertHandler.show();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentGalleryIntent();
            }
        });
    }

    void setButtonState_0(){
        editButton.animate().rotation(0).setDuration(500).start();
        editButton.setImageResource(R.drawable.ic_edit_white_24dp);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeState();
            }
        });

        profileImage.setOnClickListener(null);

    }


    void setEnabledOfEditTexts(boolean state){
        nameEditText.setEnabled(state);
        usernameEditText.setEnabled(state);
        passwordEditText.setEnabled(state);
    }

    void sentGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PROFILE_IMAGE_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PROFILE_IMAGE_TAG:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    profileImage.setImageURI(selectedImage);
                    topProfileImageView.setImageURI(selectedImage);
                }
        }
    }

    void toast(String message){
        Toast.makeText(AccountsActivity.this, message, Toast.LENGTH_SHORT).show();

    }


}
