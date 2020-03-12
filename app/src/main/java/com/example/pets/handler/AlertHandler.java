package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.pets.R;
import com.example.pets.interfaces.AlertClickListener;

public class AlertHandler {

    AlertClickListener alertClickListener;

    Context context;
    Activity activity;

    String title;

    Dialog alertDialog;
    ProgressBar progressBar;
    ImageView statusResult;

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

        progressBar = alertDialog.findViewById(R.id.yesNo_progressBar);
        statusResult = alertDialog.findViewById(R.id.yesNo_status_imageView);

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



    public void showProgress(){
        this.setTitle("Updating...");
        progressBar.setScaleX(1);
        progressBar.setScaleY(1);
        progressBar.setVisibility(View.VISIBLE);
        statusResult.setVisibility(View.VISIBLE);

        progressBar.animate().rotation(2000).setDuration(2000).setInterpolator(new DecelerateInterpolator()).start();

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).animate()
                .translationX(-100).scaleY(0).scaleX(0).setDuration(500).start();

        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).animate()
                .translationX(100).scaleY(0).scaleX(0).setDuration(500).start();
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
        statusResult.setScaleX(0);
        statusResult.setScaleY(0);
        statusResult.setVisibility(View.GONE);

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).animate()
                .translationX(0).scaleY(1).scaleX(1).setDuration(0).start();

        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).animate()
                .translationX(0).scaleY(1).scaleX(1).setDuration(0).start();
    }

    public void doAfterSeconds(String message, int status){
        setTitle(message);
        if(status == 0){
            statusResult.setImageResource(R.drawable.ic_close);
        }else{
            statusResult.setImageResource(R.drawable.ic_check);
        }

        statusResult.animate().scaleX(1).scaleY(1).setDuration(500).start();
        progressBar.animate().scaleX(0).scaleY(0).setDuration(500).start();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                dismiss();
            }
        }, 2000);
    }

    public void hideProgressWithInfo(String message, int status){
        // status:0 failure, status:1 success
        if(status == 0){
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doAfterSeconds(message, status);
                }
            }, 3000);
        }else{
            doAfterSeconds(message, status);
        }


    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
