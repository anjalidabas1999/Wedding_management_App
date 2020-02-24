package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashActivity extends AppCompatActivity {

    ImageView appLogo;
    RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setup();


    }

    private void setup() {
        //getting views by their ids
        appLogo = findViewById(R.id.splashActivity_appLogo_imageView);
        root = findViewById(R.id.root);

        //handling animations

        //changing background of root from one color to another
        ObjectAnimator.ofObject(root, "backgroundColor",
                new ArgbEvaluator(),
                Color.WHITE, getResources().getColor(R.color.accounts_dark_background))
                .setDuration(5000)
                .start();

        //loading animation from anim directory and passing it in imageView setAnimation method
        appLogo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        appLogo.animate();

        //calling the intent after 5000 seconds
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 6000);



    }
}
