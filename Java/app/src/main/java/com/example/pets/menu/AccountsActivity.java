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
import com.example.pets.interfaces.FirebaseTasksCallbacks;
import com.example.pets.network.NetworkStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    Uri mNewImage;
    User mNewUser;

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
                        updateProfile();
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
                    mNewImage = data.getData();
                    profileImage.setImageURI(mNewImage);
                    topProfileImageView.setImageURI(mNewImage);
                }
        }
    }

    void updateProfile(){
        alertHandler.showProgress();
        mNewUser = new User();
        mNewUser.setName(nameEditText.getText().toString().trim());
        mNewUser.setUsername(usernameEditText.getText().toString().trim());
        mNewUser.setPassword(passwordEditText.getText().toString().trim());

        if(user.equals(mNewUser)){
            alertHandler.hideProgressWithInfo("No change", 1);
            return;
        }



        if(!user.getPassword().equals(mNewUser.getPassword())){
            // In this case password is changed
            // updating password
            updatePassword(mNewUser.getUsername(), mNewUser.getPassword(), new FirebaseTasksCallbacks() {
                @Override
                public void onSuccess(Uri result) {
                    alertHandler.setTitle("Password updated");
                    // checking if profileImage is changed or not
                    if(mNewImage != null){
                        // In this case both password and profileImage is changed
                        // updating profile image
                        updateProfileImage(mNewImage, new FirebaseTasksCallbacks() {
                            @Override
                            public void onSuccess(Uri result) {
                                alertHandler.setTitle("Profile image updated");
                                mNewUser.setImage(result.toString());
                                // updating database
                                updateCompleteDatabase(new FirebaseTasksCallbacks() {
                                    @Override
                                    public void onSuccess(Uri result) {
                                        alertHandler.hideProgressWithInfo("Updated", 1);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Exception e) {
                                alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                            }
                        });
                    }else{
                        // In this case password is changed but not profileImage
                        // updating database
                        mNewUser.setImage(user.getImage());
                        updateCompleteDatabase(new FirebaseTasksCallbacks() {
                            @Override
                            public void onSuccess(Uri result) {
                                alertHandler.hideProgressWithInfo("Updated", 1);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                }
            });
        }else if(mNewImage != null){
            // In this case password is not changed but profileImage is changed
            // uploading profile image after checking if it is changed
            updateProfileImage(mNewImage, new FirebaseTasksCallbacks() {
                @Override
                public void onSuccess(Uri result) {
                    alertHandler.setTitle("Profile image updated");
                    mNewUser.setImage(result.toString());
                    // updating database
                    updateCompleteDatabase(new FirebaseTasksCallbacks() {
                        @Override
                        public void onSuccess(Uri result) {
                            alertHandler.hideProgressWithInfo("Updated", 1);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                }
            });
        }else{
            // In this case neither password nor image is changed
            // updating database
            mNewUser.setImage(user.getImage());
            updateCompleteDatabase(new FirebaseTasksCallbacks() {
                @Override
                public void onSuccess(Uri result) {
                    alertHandler.hideProgressWithInfo("Updated", 1);
                }

                @Override
                public void onFailure(Exception e) {
                    alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                }
            });
            /*updateDatabaseField("name", mNewUser.getName(), new FirebaseTasksCallbacks() {
                @Override
                public void onSuccess(Uri result) {
                    alertHandler.hideProgressWithInfo("Updated", 1);
                }

                @Override
                public void onFailure(Exception e) {
                    alertHandler.hideProgressWithInfo(e.getMessage(), 0);
                }
            });*/
        }


    }

    void updatePassword(String userName, String password, FirebaseTasksCallbacks firebaseTasksCallbacks){
        alertHandler.setTitle("Updating password..");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(userName, password);

        /*firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseTasksCallbacks.onSuccess(null);
                }else{
                    firebaseTasksCallbacks.onFailure(task.getException());
                }
            }
        });*/

        firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseTasksCallbacks.onSuccess(null);
                }else{
                    firebaseTasksCallbacks.onFailure(task.getException());
                }
            }
        });
    }


    void updateProfileImage(Uri imageUri, FirebaseTasksCallbacks firebaseTasksCallbacks){
        alertHandler.setTitle("Updating profile image...");
        StorageReference mRef = FirebaseStorage.getInstance().getReference().child("user/"+mAuth.getUid()+".jpeg");
        mRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebaseTasksCallbacks.onSuccess(uri);
                        }
                    });

                }else{
                    firebaseTasksCallbacks.onFailure(task.getException());
                }
            }
        });
    }

    void updateCompleteDatabase(FirebaseTasksCallbacks firebaseTasksCallbacks){
        mFirestore.collection("user").document(mAuth.getUid()).set(mNewUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseTasksCallbacks.onSuccess(null);
                }else {
                    firebaseTasksCallbacks.onFailure(task.getException());
                }
            }
        });

    }

    void updateDatabaseField(String field, String value, FirebaseTasksCallbacks firebaseTasksCallbacks){
        mFirestore.collection("user").document(mAuth.getUid()).update(field, value).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseTasksCallbacks.onSuccess(null);
                }else{
                    firebaseTasksCallbacks.onFailure(task.getException());
                }
            }
        });
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

    void toast(String message){
        Toast.makeText(AccountsActivity.this, message, Toast.LENGTH_SHORT).show();

    }


}
