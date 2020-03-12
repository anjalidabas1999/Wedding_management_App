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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pets.Classes.Pet;
import com.example.pets.account.LoginActivity;
import com.example.pets.adapter.PetAdapter;
import com.example.pets.handler.AlertHandler;
import com.example.pets.handler.NewPetHandler;
import com.example.pets.interfaces.AlertClickListener;
import com.example.pets.interfaces.ItemListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    CoordinatorLayout root;
    LinearLayout overLay;
    RelativeLayout backgroundMessageContainer;

    BottomSheetBehavior bottomSheetBehavior;

    RecyclerView recyclerView;

    int fabState = 0; // 0: fab closed, 1:fab opened
    int drawerState = 0; //0: closed, 1:opened
    int bottomSheetFabState = 0; // 0: edit, 1: save

    FloatingActionButton bottomSheetFab;
    FloatingActionButton addNewEntryFab;
    FloatingActionButton scanQrCodeFab;
    FloatingActionButton manualNewEntryFab;

    CoordinatorLayout petShareButton;

    ImageButton drawerOpener;

    PetAdapter adapter;

    AlertHandler alertHandler;

    //drawer
    LinearLayout accountsMenuItem;
    LinearLayout settingsMenuItem;
    LinearLayout logOutMenuItem;

    //bottomsheet
    EditText bottomSheetNameEditText;
    EditText bottomSheetDateAddedEditText;
    EditText bottomSheetBreedEditText;
    EditText bottomSheetDescriptionEditText;
    EditText bottomSheetHealthEditText;

    ImageView bottomSheetImageView;
    FloatingActionButton bottomSheetCancelFab;

    Pet currentPetOpenedInBottomSheet = null;



    //firebase objects
    FirebaseFirestore dB;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setUp();

    }

    void setUp(){

        //getting views by id
        root = findViewById(R.id.homeActivity_root_relativeLayout);
        overLay = findViewById(R.id.homeActivity_overlay_linearLayout);
        backgroundMessageContainer = findViewById(R.id.homeActivity_emptyListContainer_relativeLayout);
        recyclerView = findViewById(R.id.homeActivity_recyclerView);
        drawerOpener = findViewById(R.id.homeActivity_drawerOpener_imageView);

        addNewEntryFab = findViewById(R.id.homeActivity_newEntry_fab);
        bottomSheetFab = findViewById(R.id.homeActivity_bottomSheetEditorButton_fab);
        manualNewEntryFab = findViewById(R.id.homeActivity_newEntry_manual_fab);
        scanQrCodeFab = findViewById(R.id.homeActivity_newEntry_qr_fab);

        petShareButton = findViewById(R.id.bottomSheet_imageView_container_coordinatorLayout);
        //slideButton = findViewById(R.id.homeActivity_slide_button);


        //getting bottom sheet layout
        bottomSheetNameEditText = findViewById(R.id.bottomSheet_name_editText);
        bottomSheetDateAddedEditText = findViewById(R.id.bottomSheet_dateAdded_editText);
        bottomSheetImageView = findViewById(R.id.bottomSheet_imageView);
        bottomSheetBreedEditText = findViewById(R.id.bottomSheet_breed_editText);
        bottomSheetDescriptionEditText = findViewById(R.id.bottomSheet_description_editText);
        bottomSheetHealthEditText = findViewById(R.id.bottomSheet_healthStatus_editText);

        bottomSheetCancelFab = findViewById(R.id.bottomSheet_cancelEditing_fab);


        //getting firebase instances
        mAuth = FirebaseAuth.getInstance();
        dB = FirebaseFirestore.getInstance();



        //setting recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recyclerView.setHasFixedSize(false);

        adapter = new PetAdapter(new ArrayList<>(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBottomSheet(adapter.get(recyclerView.getChildLayoutPosition(view)));
            }
        }, new ItemListener() {
            @Override
            public void size(int s) {
                if(s==0){
                    backgroundMessageContainer.setVisibility(View.VISIBLE);
                }else{
                    backgroundMessageContainer.setVisibility(View.GONE);
                }
            }
        });

        recyclerView.setAdapter(adapter);

        //setting bottom sheet
        setUpBottomSheet();

        //setting drawer layout
        setUpDrawer();


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

            @Override
            public void onClick(View view) {
                changeFabState();

            }
        });

        scanQrCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFabState();
                IntentIntegrator integrator = new IntentIntegrator(HomeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a barcode");
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        manualNewEntryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabState0();
                showNewPetDialog();
            }
        });

        fetchDataFromServer();
    }

    void changeFabState(){
        if(fabState == 0){
            setFabState1();
        }else{
            setFabState0();

        }
    }

    @SuppressLint("RestrictedApi")
    void setFabState1(){
        // open fab
        fabState = 1;
        overLay.setAlpha(1);
        overLay.setVisibility(View.VISIBLE);
        manualNewEntryFab.setVisibility(View.VISIBLE);
        scanQrCodeFab.setVisibility(View.VISIBLE);
        addNewEntryFab.animate().rotation(135).setDuration(500).start();
        manualNewEntryFab.animate().translationX(-200f).setDuration(500).start();
        scanQrCodeFab.animate().translationY(-200f).setDuration(500).start();

    }

    @SuppressLint("RestrictedApi")
    void setFabState0(){
        // close fab
        fabState = 0;
        overLay.setAlpha(0);
        overLay.setVisibility(View.INVISIBLE);
        manualNewEntryFab.setVisibility(View.GONE);
        scanQrCodeFab.setVisibility(View.GONE);
        addNewEntryFab.animate().rotation(0).setDuration(500).start();
        manualNewEntryFab.animate().translationX(0f).setDuration(500).start();
        scanQrCodeFab.animate().translationY(0f).setDuration(500).start();
    }



    void setUpBottomSheet(){
        RelativeLayout bottomSheet = findViewById(R.id.linearLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(75);
        bottomSheetBehavior.setHideable(false);
//        bottomSheetBehavior.setFitToContents(false);

        alertHandler = new AlertHandler(this, HomeActivity.this, "",  null);

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
                changeBottomSheetFabState();
            }
        });

        bottomSheetCancelFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForCancelButton();
            }
        });



    }

    void updateBottomSheet(final Pet pet){
        currentPetOpenedInBottomSheet = pet;
        overLay.setVisibility(View.VISIBLE);
        bottomSheetNameEditText.setText(pet.getName());
        bottomSheetDateAddedEditText.setText(pet.getDateAdded());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetHealthEditText.setText(pet.getHealthDesc());
        bottomSheetDescriptionEditText.setText(pet.getDescription());
        bottomSheetBreedEditText.setText(pet.getBreed());


        petShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQrDialog(pet);
            }
        });

    }

    void changeBottomSheetFabState(){

        if(bottomSheetFabState == 0){
            bottomSheetFabState = 1;
            setBottomSheetFabState_1();

        }else{
            bottomSheetFabState = 0;
            setBottomSheetFabState_0();
        }
    }

    @SuppressLint("RestrictedApi")
    void setBottomSheetFabState_1(){
        bottomSheetFab.setImageResource(R.drawable.ic_check_white_24dp);
        bottomSheetFab.animate().rotation(360).setDuration(500).start();
        bottomSheetCancelFab.setVisibility(View.VISIBLE);

        bottomSheetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForUpdateButton();
            }
        });

        makeBottomSheetEditableOrNonEditable(true);
    }

    @SuppressLint("RestrictedApi")
    void setBottomSheetFabState_0(){
        bottomSheetFab.setImageResource(R.drawable.ic_edit_white_24dp);
        bottomSheetFab.animate().rotation(0).setDuration(500).start();
        bottomSheetCancelFab.setVisibility(View.GONE);

        bottomSheetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeBottomSheetFabState();
            }
        });

        makeBottomSheetEditableOrNonEditable(false);
    }

    void makeBottomSheetEditableOrNonEditable(boolean b){
        bottomSheetBreedEditText.setEnabled(b);
        bottomSheetDescriptionEditText.setEnabled(b);
        bottomSheetHealthEditText.setEnabled(b);
        bottomSheetNameEditText.setEnabled(b);
    }

    void showAlertForCancelButton(){
        alertHandler.setTitle("Exit?");
        alertHandler.setAlertClickListener(new AlertClickListener() {
            @Override
            public void onNegativeClick() {
                alertHandler.dismiss();
            }

            @Override
            public void onPositiveClick() {
                alertHandler.dismiss();
                changeBottomSheetFabState();
            }
        });
        alertHandler.show();
    }

    void showAlertForUpdateButton(){
        alertHandler.setTitle("Save changes?");
        alertHandler.setAlertClickListener(new AlertClickListener() {
            @Override
            public void onNegativeClick() {
                alertHandler.dismiss();
            }

            @Override
            public void onPositiveClick() {
                alertHandler.showProgress();
                updateCurrentPet();
            }
        });
        alertHandler.show();
    }

    void updateCurrentPet(){
        if(validateChanges()){
            currentPetOpenedInBottomSheet.setName(bottomSheetNameEditText.getText().toString());
            currentPetOpenedInBottomSheet.setBreed(bottomSheetBreedEditText.getText().toString());
            currentPetOpenedInBottomSheet.setDescription(bottomSheetDescriptionEditText.getText().toString());
            currentPetOpenedInBottomSheet.setHealthDesc(bottomSheetHealthEditText.getText().toString());

            Gson gson = new Gson();
            String updatedPet = gson.toJson(currentPetOpenedInBottomSheet);

            dB.collection("user").document(mAuth.getUid()).collection("data").document("data")
                    .update(""+currentPetOpenedInBottomSheet.getId(), updatedPet).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    adapter.update(currentPetOpenedInBottomSheet.getId(), currentPetOpenedInBottomSheet);
                    alertHandler.hideProgressWithInfo("Success", 1);

                    changeBottomSheetFabState();
                }
            });
        }else{
            alertHandler.hideProgressWithInfo("No change", 1);

            changeBottomSheetFabState();
        }
    }

    boolean validateChanges(){
        if(!bottomSheetNameEditText.getText().toString().equals(currentPetOpenedInBottomSheet.getName()) ||
                !bottomSheetBreedEditText.getText().toString().equals(currentPetOpenedInBottomSheet.getBreed()) ||
                !bottomSheetDescriptionEditText.getText().toString().equals(currentPetOpenedInBottomSheet.getDescription()) ||
                !bottomSheetHealthEditText.getText().toString().equals(currentPetOpenedInBottomSheet.getHealthDesc())
        ){return true;}
        return false;
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

    void showQrDialog(Pet pet){
        Dialog dialog = new Dialog(HomeActivity.this);
//        View v = LayoutInflater.from(HomeActivity.this).inflate(R.layout.qr_code_layout, false);
        dialog.setContentView(R.layout.qr_code_layout);
        ((ImageView)dialog.findViewById(R.id.dialog_imageView)).setImageBitmap(generateQR(pet));
        dialog.show();
    }

    void showNewPetDialog(){
        NewPetHandler petHandler = new NewPetHandler(this, HomeActivity.this);
        petHandler.init();
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
                updateServerDatabase(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void updateServerDatabase(String jsonResponse){
        Gson gson = new Gson();
        Pet pet = gson.fromJson(jsonResponse, Pet.class);
        jsonResponse = gson.toJson(pet);
        DocumentReference reference = dB.collection("user").document(mAuth.getUid());



        reference.collection("data").document("data").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentReference currentDatabase = reference.collection("data").document("data");
                int counter = documentSnapshot.getLong("size").intValue();
                pet.setId(counter);

                String finalJsonResponse = gson.toJson(pet);

                currentDatabase
                        .update(""+counter, finalJsonResponse)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(HomeActivity.this, "Data added", Toast.LENGTH_LONG).show();
                                    currentDatabase.update("size", counter + 1);
                                } else {
                                    Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    void fetchDataFromServer(){
        Gson gson = new Gson();
        dB.collection("user").document(mAuth.getUid()).collection("data").document("data")

               .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                   @Override
                   public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                       int size= documentSnapshot.getLong("size").intValue();
                       int i = 0;
                       while(size>=1 && i<size && size>0){
                           String currentPet = documentSnapshot.getString(""+i);
                           Pet pet = gson.fromJson(currentPet, Pet.class);
                           adapter.add(pet);
                           i++;
                       }


                   }
               });

    }

}
