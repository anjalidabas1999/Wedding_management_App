package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pets.HomeActivity;
import com.example.pets.R;
import com.example.pets.interfaces.AlertClickListener;

public class AlertHandler {

    AlertClickListener alertClickListener;

    Context context;
    Activity activity;

    String title;

    Dialog alertDialog;

    public AlertHandler(Context context, Activity activity, String title, AlertClickListener alertClickListener) {
        this.alertClickListener = alertClickListener;
        this.context = context;
        this.activity = activity;
        this.title = title;

        setUpAlertDialog();

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
                alertClickListener.onNegativeClick();
            }
        });


        updateDialogTitle();

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertClickListener.onPositiveClick();
            }
        });



    }

    private void updateDialogTitle(){
        ((TextView)alertDialog.findViewById(R.id.yesNo_heading_textView)).setText(title);
    }

    public AlertClickListener getAlertClickListener() {
        return alertClickListener;
    }

    public void setAlertClickListener(AlertClickListener alertClickListener) {
        this.alertClickListener = alertClickListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateDialogTitle();
    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
