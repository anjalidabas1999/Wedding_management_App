package com.example.pets.handler;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.HomeActivity;
import com.example.pets.R;

import java.util.ArrayList;

public class NewPetHandler {

    Context context;
    Activity activity;

    String[] data = {"", "", "", ""};

    int currentPage = 0;

    Dialog dialog;
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

                Dialog d = new Dialog(activity);
                d.setContentView(R.layout.yes_no_dialog_layout);
                d.setCancelable(false);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                (d.findViewById(R.id.yesNo_cancel_imageButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                (d.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        dialog.dismiss();
                    }
                });

                d.show();


            }
        });



    }
    
    public void init(){
        dialog.show();
    }
}
