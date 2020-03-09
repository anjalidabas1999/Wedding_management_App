package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

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


    }

    public void init(){
        dialog.show();
    }
}
