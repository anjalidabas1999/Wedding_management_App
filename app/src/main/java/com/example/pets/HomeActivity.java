package com.example.pets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.Classes.Pet;
import com.example.pets.adapter.PetAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    CoordinatorLayout root;
    LinearLayout overLay;

    BottomSheetBehavior bottomSheetBehavior;

    Button slideButton;

    RecyclerView recyclerView;

    PetAdapter adapter;

    ArrayList<Pet> db;

    //bottomsheet
    TextView bottomSheetHeadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        db = new ArrayList<>();

        db.add(new Pet("Tom", "Breed", "20Th Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01Th Jan 2008"));
        db.add(new Pet("Browny", "German", "29Th Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13Th Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20Th Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20Th Mar 2019"));
        db.add(new Pet("Tom", "Breed", "20Th Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01Th Jan 2008"));
        db.add(new Pet("Browny", "German", "29Th Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13Th Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20Th Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20Th Mar 2019"));
        db.add(new Pet("Tom", "Breed", "20Th Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01Th Jan 2008"));
        db.add(new Pet("Browny", "German", "29Th Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13Th Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20Th Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20Th Mar 2019"));

        setUp();




    }

    void setUp(){

        //getting views by id
        root = findViewById(R.id.homeActivity_root_relativeLayout);
        overLay = findViewById(R.id.homeActivity_overlay_linearLayout);
        recyclerView = findViewById(R.id.homeActivity_recyclerView);
        //slideButton = findViewById(R.id.homeActivity_slide_button);

        //getting bottom sheet layout
        bottomSheetHeadingTextView = findViewById(R.id.heading);


        //setting recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerView.setHasFixedSize(false);

        adapter = new PetAdapter(db, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBottomSheet(adapter.get(recyclerView.getChildLayoutPosition(view)));
            }
        });

        recyclerView.setAdapter(adapter);

        //setting bottom sheet
        setUpBottomSheet();

        //setting on click listeners
        /*slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overLay.setVisibility(View.VISIBLE);

                ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(root, "translationX", 700f);
                objectAnimator1.setDuration(500);
                objectAnimator1.start();
            }
        });*/

        overLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overLay.getVisibility() == View.VISIBLE){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });



    }

    void setUpBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.linearLayout));
        bottomSheetBehavior.setPeekHeight(100);
        bottomSheetBehavior.setHideable(false);
//        bottomSheetBehavior.setFitToContents(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        overLay.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        overLay.setVisibility(View.VISIBLE);
                        break;

                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    void updateBottomSheet(Pet pet){
        overLay.setVisibility(View.VISIBLE);
        bottomSheetHeadingTextView.setText(pet.getName());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }



}
