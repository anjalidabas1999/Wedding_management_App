package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pets.R;

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
                setUpAlertDialog();
            }
        });


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

        alertDialog.show();
    }

    void setUpExitAlertClickListener(){
        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                dialog.dismiss();
            }
        });
    }

    void setUpSubmitAlertClickListener(){
        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                dialog.dismiss();
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
                }

                data[currentPage] = textInputEditText.getText().toString();
                textInputEditText.setText(data[--currentPage]);
                headingTextView.setText(label[currentPage]);

            }
        });
    }

    public void init(){
        dialog.show();
    }
}
