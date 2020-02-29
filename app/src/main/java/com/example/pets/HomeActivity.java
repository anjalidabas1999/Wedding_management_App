package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.Classes.Pet;
import com.example.pets.adapter.PetAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    CoordinatorLayout root;
    LinearLayout overLay;

    BottomSheetBehavior bottomSheetBehavior;

    Button slideButton;

    RecyclerView recyclerView;

    int fabState = 0; // 0: fab closed, 1:fab opened

    FloatingActionButton bottomSheetFab;
    FloatingActionButton addNewEntryFab;
    FloatingActionButton scanQrCodeFab;
    FloatingActionButton manualNewEntryFab;

    PetAdapter adapter;

    ArrayList<Pet> db;

    //bottomsheet
    TextView bottomSheetHeadingTextView;
    ImageView bottomSheetImagView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        db = new ArrayList<>();

        db.add(new Pet("Tom", "Breed", "20 Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01 Jan 2008"));
        db.add(new Pet("Browny", "German", "29 Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13 Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20 Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20 Mar 2019"));
        db.add(new Pet("Tom", "Breed", "20 Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01 Jan 2008"));
        db.add(new Pet("Browny", "German", "29 Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13 Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20 Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20 Mar 2019"));
        db.add(new Pet("Tom", "Breed", "20 Feb 2020"));
        db.add(new Pet("Tiger", "Chihuahua", "01 Jan 2008"));
        db.add(new Pet("Browny", "German", "29 Feb 2018"));
        db.add(new Pet("Tommy", "Anhbdd", "13 Aug 2015"));
        db.add(new Pet("Guchchu", "Pagal", "20 Nov 2020"));
        db.add(new Pet("Tom", "Breed", "20 Mar 2019"));

        setUp();




    }

    void setUp(){

        //getting views by id
        root = findViewById(R.id.homeActivity_root_relativeLayout);
        overLay = findViewById(R.id.homeActivity_overlay_linearLayout);
        recyclerView = findViewById(R.id.homeActivity_recyclerView);

        addNewEntryFab = findViewById(R.id.homeActivity_newEntry_fab);
        bottomSheetFab = findViewById(R.id.homeActivity_bottomSheetEditorButton_fab);
        manualNewEntryFab = findViewById(R.id.homeActivity_newEntry_manual_fab);
        scanQrCodeFab = findViewById(R.id.homeActivity_newEntry_qr_fab);
        //slideButton = findViewById(R.id.homeActivity_slide_button);

        //getting bottom sheet layout
        bottomSheetHeadingTextView = findViewById(R.id.bottomSheet_name_textView);
        bottomSheetImagView = findViewById(R.id.bottomSheet_imageView);


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
                    //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        addNewEntryFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                if(fabState == 0){
                    fabState = 1;
                    overLay.setVisibility(View.VISIBLE);
                    manualNewEntryFab.setVisibility(View.VISIBLE);
                    scanQrCodeFab.setVisibility(View.VISIBLE);
                    manualNewEntryFab.animate().translationX(-200f).setDuration(500).start();
                    scanQrCodeFab.animate().translationY(-200f).setDuration(500).start();
                }else{
                    fabState = 0;
                    overLay.setAlpha(0);
                    overLay.setVisibility(View.INVISIBLE);
                    manualNewEntryFab.setVisibility(View.GONE);
                    scanQrCodeFab.setVisibility(View.GONE);
                    manualNewEntryFab.animate().translationX(0f).setDuration(500).start();
                    scanQrCodeFab.animate().translationY(0f).setDuration(500).start();
                    /*manualNewEntryFab.animate().translationX(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            manualNewEntryFab.setVisibility(View.GONE);
                        }
                    }).start();
                    scanQrCodeFab.animate().translationY(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            scanQrCodeFab.setVisibility(View.GONE);
                        }
                    }).start();*/
                }

            }
        });



        bottomSheetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });



    }

    void setUpBottomSheet(){
        RelativeLayout bottomSheet = findViewById(R.id.linearLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(75);
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
            public void onSlide(@NonNull View view, float slideOffset) {
                overLay.setVisibility(View.VISIBLE);
                overLay.animate().alpha(slideOffset).setDuration(0).start();
                bottomSheetFab.animate().scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                addNewEntryFab.animate().scaleX(1-slideOffset).scaleY(1-slideOffset).setDuration(0).start();
            }
        });
    }

    void updateBottomSheet(Pet pet){
        overLay.setVisibility(View.VISIBLE);
        bottomSheetHeadingTextView.setText(pet.getName());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetImagView.setImageBitmap(generateQR(pet));

    }

    Bitmap generateQR(Pet pet){
        Gson gson = new Gson();
        String data = gson.toJson(pet);

        BarcodeEncoder barcode_content = new BarcodeEncoder();
        try {
            Bitmap bm = barcode_content.encodeBitmap(data, BarcodeFormat.QR_CODE, 15, 15);
            return bm;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

        /*QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 1);
        qrgEncoder.setColorBlack(Color.RED);
        qrgEncoder.setColorWhite(Color.BLUE);
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            return bitmap;
        } catch (Exception e) {
            return null;
        }*/
    }



}
