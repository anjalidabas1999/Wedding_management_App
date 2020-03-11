package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pets.Classes.Pet;
import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class NewPetHandler {

    Context context;
    Activity activity;

    String[] label = {"Name", "Breed", "Description", "Health Status"};
    String[] data = {"", "", "", ""};// 0: name, 1: breed, 2: description, 3: health_status

    int currentPage = 0;

    Dialog dialog;
    Dialog alertDialog;

    TextView headingTextView;
    EditText textInputEditText;
    ImageButton previousButton;
    ImageButton cancelButton;
    ImageButton nextButton;

    View confirmDialogLayout;

    FirebaseFirestore mFirestore;

    public NewPetHandler(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        setUpDialog();
    }

    private void setUpDialog(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.new_pet_entry_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);

        headingTextView = dialog.findViewById(R.id.newPetEntry_currentTitle_textView);
        textInputEditText = dialog.findViewById(R.id.newPetEntry_inputText_editText);
        previousButton = dialog.findViewById(R.id.newPetEntry_previous_imageButton);
        cancelButton = dialog.findViewById(R.id.newPetEntry_cancel_imageButton);
        nextButton = dialog.findViewById(R.id.newPetEntry_next_imageButton);
        confirmDialogLayout = dialog.findViewById(R.id.newPetEntry_confirmDialog_include);

        previousButton.setVisibility(View.GONE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });


        setUpAlertDialog();
        setUpNextClickButton();
        setUpPreviousClickButton();

    }

    void setUpAlertDialog(){
        alertDialog = new Dialog(activity);
        alertDialog.setContentView(R.layout.yes_no_dialog_layout);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        setUpExitAlertClickListener();

    }

    void setUpExitAlertClickListener(){

        ((TextView)alertDialog.findViewById(R.id.yesNo_heading_textView)).setText("Exit?");
        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                dialog.dismiss();
            }
        });
    }

    void setUpSubmitAlertClickListener(){

        ((TextView)alertDialog.findViewById(R.id.yesNo_heading_textView)).setText("Submit?");
        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, data[0]+data[1]+data[2]+data[3], Toast.LENGTH_SHORT).show();
                initiateServer();
            }
        });
    }

    void setUpNextClickButton(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage==2){
                    nextButton.setVisibility(View.GONE);
                    cancelButton.setImageResource(R.drawable.ic_check_white_24dp);
                    setUpSubmitAlertClickListener();
                }
                if(currentPage==0){
                    previousButton.setVisibility(View.VISIBLE);
                }

                data[currentPage] = textInputEditText.getText().toString();
                textInputEditText.setText(data[++currentPage]);
                headingTextView.setText(label[currentPage]);

            }
        });
    }

    void setUpPreviousClickButton(){
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage==1){
                    previousButton.setVisibility(View.GONE);
                }
                if(currentPage==3){
                    nextButton.setVisibility(View.VISIBLE);
                    cancelButton.setImageResource(R.drawable.ic_close_white);
                    setUpExitAlertClickListener();
                }

                data[currentPage] = textInputEditText.getText().toString();
                textInputEditText.setText(data[--currentPage]);
                headingTextView.setText(label[currentPage]);

            }
        });
    }

    void initiateServer(){
        mFirestore = FirebaseFirestore.getInstance();
        DocumentReference reference = mFirestore.collection("user").document((FirebaseAuth.getInstance()).getUid());

        Pet pet = getPet();
        Gson gson = new Gson();

        reference.collection("data").document("data").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference currentDatabase = reference.collection("data").document("data");
                int counter = documentSnapshot.getLong("size").intValue();
                pet.setId(counter);

                String finalJsonResponse = gson.toJson(pet);

                currentDatabase
                        .update(""+counter, finalJsonResponse)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(activity, "Data added", Toast.LENGTH_LONG).show();
                                    currentDatabase.update("size", counter + 1);
                                } else {
                                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    Pet getPet(){
        Pet pet = new Pet();
        pet.setName(data[0]);
        pet.setBreed(data[1]);
        pet.setDescription(data[2]);
        pet.setHealthDesc(data[3]);

        return pet;
    }

    public void init(){
        dialog.show();
    }
}
