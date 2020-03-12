package com.example.pets.handler;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;

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
        progressBar.setVisibility(View.VISIBLE);

        progressBar.animate().rotation(3600).setDuration(3000).setInterpolator(new DecelerateInterpolator()).start();

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).animate()
                .translationX(-100).scaleY(0).scaleX(0).setDuration(500).start();

        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).animate()
                .translationX(100).scaleY(0).scaleX(0).setDuration(500).start();
    }

    public void hideProgress(){
       progressBar.setVisibility(View.GONE);

        (alertDialog.findViewById(R.id.yesNo_confirm_imageButton)).animate()
                .translationX(0).scaleY(1).scaleX(1).setDuration(0).start();

        (alertDialog.findViewById(R.id.yesNo_cancel_imageButton)).animate()
                .translationX(0).scaleY(1).scaleX(1).setDuration(0).start();
    }

    public void doAfterSeconds(String message, int status){
        setTitle(message);
        if(status == 0){

            progressBar.animate().scaleX(0).scaleY(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.ic_close));
                    progressBar.animate().scaleX(1).scaleY(1).setDuration(500).start();

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        }else{

            progressBar.setBackground(activity.getResources().getDrawable(R.drawable.ic_check));

            //progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.ic_check));
            //progressBar.requestLayout();
            //progressBar.animate().scaleX(0).scaleY(0).setDuration(500).start();

            /*(new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.ic_check));
                    progressBar.requestLayout();
                    progressBar.animate().scaleX(1).scaleY(1).setDuration(500).start();
                }
            }, 500);*/

        }



        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                dismiss();
            }
        }, 5000);
    }

    public void hideProgressWithInfo(String message, int status){
        // status:0 failure, status:1 success
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doAfterSeconds(message, status);
            }
        }, 5000);



    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
