package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.account.LoginActivity;
import com.example.pets.account.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ImageView appLogo;
    RelativeLayout root;

    TextView logInIntroText;
    LinearLayout submitActionContainer;

    Intent logInIntent;
    Intent signUpIntent;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setup();


    }

    private void setup() {
        //getting firebase refrences
        mAuth = FirebaseAuth.getInstance();

        //getting views by their ids
        appLogo = findViewById(R.id.splashActivity_appIcon_ImageView);
        root = findViewById(R.id.root);
        logInIntroText = findViewById(R.id.splashActivity_introText_textView);
        submitActionContainer = findViewById(R.id.splashActivity_bottomActions_linearLayout);

        //setting up intents
        logInIntent = new Intent(SplashActivity.this, LoginActivity.class);
        signUpIntent = new Intent(SplashActivity.this, SignUpActivity.class);

        //handling animations
        handleAnim();

        //setting the onClickListeners

        submitActionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling @sentLoginIntent
                sentLoginIntent();
            }
        });

        submitActionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentSignUpIntent();
            }
        });



    }

    void animateBackgroundColor(int colorFrom, int colorTo){
        ObjectAnimator.ofObject(root, "backgroundColor",
                new ArgbEvaluator(),
                colorFrom, colorTo)
                .setDuration(2500)
                .start();
    }

    void sentSignUpIntent(){
        Pair[] pairs = new Pair[3];
        //getResources().getString(R.string.introSignUp_to_Login_transition_backButton)

        pairs[0] = new Pair(submitActionContainer.getChildAt(0), getResources().getString(R.string.introSignUp_to_Login_transition_loginButton));
        pairs[1] = new Pair(appLogo, getResources().getString(R.string.introSignUp_to_Login_transition_backButton));
        pairs[2] = new Pair(logInIntroText, getResources().getString(R.string.login_to_signUp_infoText));

        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
        startActivity(signUpIntent, options.toBundle());
    }

    void sentLoginIntent(){
        //creating options for shared transition
        Pair[] pairs = new Pair[3];
        //getResources().getString(R.string.introSignUp_to_Login_transition_backButton)

        pairs[0] = new Pair(submitActionContainer.getChildAt(0), getResources().getString(R.string.introSignUp_to_Login_transition_loginButton));
        pairs[1] = new Pair(appLogo, getResources().getString(R.string.introSignUp_to_Login_transition_backButton));
        pairs[2] = new Pair(logInIntroText, getResources().getString(R.string.login_to_signUp_infoText));

        final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
        startActivity(logInIntent, options.toBundle());
    }

    void handleAnim(){
        //animating the background color change from light to dark
        animateBackgroundColor(Color.WHITE, getResources().getColor(R.color.accounts_dark_background));

        //loading animation from anim directory and passing it in imageView setAnimation method
        appLogo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.focus_zoom));
        appLogo.animate();

        //adding listener to the animation on appLogo
        appLogo.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, appLogo, "Image");
                startActivity(new Intent(SplashActivity.this, SignUpIntroActivity.class), options.toBundle());*/

                //getting location matrix of device
                DisplayMetrics metrics = getResources().getDisplayMetrics();

                int y = metrics.heightPixels / 2 - appLogo.getHeight() - 102;
                int x = metrics.widthPixels / 2 - appLogo.getWidth() - 22;

                //creating translate animation fro appLogo moving from center to top corner
                TranslateAnimation anim = new TranslateAnimation(0, -x, 0, -y);

                anim.setInterpolator((new AccelerateDecelerateInterpolator()));
                anim.setDuration(1000);
                anim.setFillAfter(true);
                appLogo.setAnimation(anim);
                appLogo.startAnimation(anim);

                //adding fade_in animation to the intro text and bottom linear layout
                logInIntroText.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));
                submitActionContainer.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fade_in));

                //making the rest of the views visible on screen
                logInIntroText.setVisibility(View.VISIBLE);
                submitActionContainer.setVisibility(View.VISIBLE);

                //animating the background color change from dark to light
                animateBackgroundColor(getResources().getColor(R.color.accounts_dark_background), Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }
}
