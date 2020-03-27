package com.example.pets.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pets.HomeActivity;
import com.example.pets.R;

import com.example.pets.account.LoginActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;

public class AccountsAlertHandler {

    Context context;
    Activity activity;

    String title;

    Dialog dialog;


    public AccountsAlertHandler(Context context, Activity activity, String title) {
        this.context = context;
        this.activity = activity;
        this.title = title;

        setUpDialog();

    }

    void setUpDialog(){
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        ProgressBar progressBar = dialog.findViewById(R.id.dialog_progressBar);

        Sprite anim = new ChasingDots();
        anim.setColor(context.getResources().getColor(R.color.white));
        progressBar.setIndeterminateDrawable(anim);

    }

    private void updateDialogTitle(){
        ((TextView)dialog.findViewById(R.id.dialog_title_textView)).setText(title);
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


    public void hideProgressWithInfo(String message, int stayDuration){
        setTitle(message);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(message.equals("Success") || message.equals("Welcome!")){
                    dismissWithFinishCall();
                }else{
                    dismiss();
                }

            }
        }, stayDuration);


    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }

    private void dismissWithFinishCall(){
        dialog.dismiss();
        activity.startActivity(new Intent(activity, HomeActivity.class));
        activity.finish();
    }

}
