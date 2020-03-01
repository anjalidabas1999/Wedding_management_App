package com.example.pets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.Classes.Pet;
import com.example.pets.account.LoginActivity;
import com.example.pets.adapter.PetAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    CoordinatorLayout root;
    LinearLayout overLay;

    BottomSheetBehavior bottomSheetBehavior;

    Button slideButton;

    RecyclerView recyclerView;

    int fabState = 0; // 0: fab closed, 1:fab opened
    int drawerState = 0; //0: closed, 1:opened

    FloatingActionButton bottomSheetFab;
    FloatingActionButton addNewEntryFab;
    FloatingActionButton scanQrCodeFab;
    FloatingActionButton manualNewEntryFab;

    CoordinatorLayout petShareButton;

    ImageButton drawerOpener;

    PetAdapter adapter;

    ArrayList<Pet> db;

    //drawer
    LinearLayout accountsMenuItem;
    LinearLayout settingsMenuItem;
    LinearLayout logOutMenuItem;

    //bottomsheet
    TextView bottomSheetHeadingTextView;
    ImageView bottomSheetImageView;


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
        drawerOpener = findViewById(R.id.homeActivity_drawerOpener_imageView);

        addNewEntryFab = findViewById(R.id.homeActivity_newEntry_fab);
        bottomSheetFab = findViewById(R.id.homeActivity_bottomSheetEditorButton_fab);
        manualNewEntryFab = findViewById(R.id.homeActivity_newEntry_manual_fab);
        scanQrCodeFab = findViewById(R.id.homeActivity_newEntry_qr_fab);

        petShareButton = findViewById(R.id.bottomSheet_imageView_container_coordinatorLayout);
        //slideButton = findViewById(R.id.homeActivity_slide_button);



        //getting bottom sheet layout
        bottomSheetHeadingTextView = findViewById(R.id.bottomSheet_name_textView);
        bottomSheetImageView = findViewById(R.id.bottomSheet_imageView);


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

        //setting drawer layout
        setUpDrawer();

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

        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerState == 0){
                    openDrawer();
                }else {
                    closeDrawer();
                }
            }
        });

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
                    overLay.setAlpha(1);
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

        scanQrCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(HomeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
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

        bottomSheetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void updateBottomSheet(final Pet pet){
        overLay.setVisibility(View.VISIBLE);
        bottomSheetHeadingTextView.setText(pet.getName());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);


        petShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(pet);
            }
        });

    }

    void setUpDrawer(){
        //getting views by ids
        accountsMenuItem = findViewById(R.id.menu_accountItem_container_linearLayout);
        settingsMenuItem = findViewById(R.id.menu_settingsItem_container_linearLayout);
        logOutMenuItem = findViewById(R.id.menu_logOutItem_container_linearLayout);

        logOutMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });


    }

    void openDrawer(){
        overLay.setVisibility(View.VISIBLE);
        drawerState = 1;
        root.animate().scaleX(0.7f).scaleY(0.7f).translationX(300).setInterpolator(new AccelerateInterpolator()).setDuration(500).start();
        drawerOpener.animate().rotation(180).setDuration(500).start();
        //animating drawer items
        int travelDist = -400;
        accountsMenuItem.animate().translationX(travelDist).setDuration(0).start();
        settingsMenuItem.animate().translationX(travelDist).setDuration(0).start();
        logOutMenuItem.animate().translationX(travelDist).setDuration(0).start();

        accountsMenuItem.setVisibility(View.VISIBLE);
        settingsMenuItem.setVisibility(View.VISIBLE);
        logOutMenuItem.setVisibility(View.VISIBLE);

        final int animDuration = 200;
        accountsMenuItem.animate().translationX(0).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                settingsMenuItem.animate().translationX(0).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        logOutMenuItem.animate().translationX(0).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();



    }

    void closeDrawer(){
        overLay.setVisibility(View.GONE);
        drawerState = 0;
        root.animate().scaleX(1f).scaleY(1f).translationX(0).setInterpolator(new AccelerateInterpolator()).setDuration(500).start();
        drawerOpener.animate().rotation(0).setDuration(500).start();
        //animating drawer items
        final int animDuration = 100;
        final int travelDist = -400;

        accountsMenuItem.animate().translationX(travelDist).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                settingsMenuItem.animate().translationX(travelDist).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        logOutMenuItem.animate().translationX(travelDist).setDuration(animDuration).setInterpolator(new AccelerateInterpolator()).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();


    }

    void showDialog(Pet pet){
        Dialog dialog = new Dialog(HomeActivity.this);
//        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.qr_code_layout, false);
        dialog.setContentView(R.layout.qr_code_layout);
        ((ImageView)dialog.findViewById(R.id.dialog_imageView)).setImageBitmap(generateQR(pet));
        dialog.show();
    }

    Bitmap generateQR(Pet pet){
        String data = new Gson().toJson(pet);


        BarcodeEncoder barcode_content = new BarcodeEncoder();
        try {
            Bitmap bm = barcode_content.encodeBitmap(data, BarcodeFormat.QR_CODE, 15, 15);
            return bm;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
