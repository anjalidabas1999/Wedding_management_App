package com.example.pets.handler;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pets.Classes.Pet;
import com.example.pets.R;
import com.example.pets.adapter.PetAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class PetDeleteHelperCallback extends ItemTouchHelper.SimpleCallback {

    Activity activity;
    PetAdapter adapter;

    public PetDeleteHelperCallback(int dragDirs, int swipeDirs, Activity activity, PetAdapter adapter) {
        super(dragDirs, swipeDirs);
        this.activity = activity;
        this.adapter = adapter;
    }

    Drawable background;
    Drawable xMark;
    int xMarkMargin;
    boolean initiated;

    void init(){
        background = new ColorDrawable(Color.RED);
        xMark = ContextCompat.getDrawable(activity, R.drawable.ic_delete_white_32dp);
        //xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        xMarkMargin = 64;
        initiated = true;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        Toast.makeText(activity, ""+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
        initiateDelete(adapter.get(viewHolder.getAdapterPosition()));
        adapter.remove(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;

        if(!initiated){
            init();
        }


        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        // draw x mark
        int itemHeight = itemView.getBottom() - itemView.getTop();
        int intrinsicWidth = xMark.getIntrinsicWidth();
        int intrinsicHeight = xMark.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
        int xMarkBottom = xMarkTop + intrinsicHeight;
        xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

        xMark.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    void initiateDelete(Pet pet){
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        mDb.collection("user").document(FirebaseAuth.getInstance().getUid()).collection("data")
                .document("data").update("size", FieldValue.increment(-1));

        mDb.collection("user").document(FirebaseAuth.getInstance().getUid()).collection("data")
                .document("data").update(""+pet.getId(), FieldValue.delete());
    }


}
