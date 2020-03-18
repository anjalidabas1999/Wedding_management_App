package com.example.pets.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pets.Classes.User;
import com.example.pets.R;
import com.example.pets.handler.AccountsAlertHandler;
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

        accountsAlertHandler = new AccountsAlertHandler(this, AccountsActivity.this, "");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

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


}
