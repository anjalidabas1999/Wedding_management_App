package com.example.pets;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    RelativeLayout root;
    LinearLayout overLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        overLay = findViewById(R.id.overlay);

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                overLay.setVisibility(View.VISIBLE);


                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(root, "translationX", 700f);
                objectAnimator1.setDuration(500);
                objectAnimator1.start();



            }

        });



        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();


    }



}
