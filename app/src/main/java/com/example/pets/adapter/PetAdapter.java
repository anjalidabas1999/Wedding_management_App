package com.example.pets.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pets.Classes.Pet;
import com.example.pets.R;

import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    ArrayList<Pet> pets;

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    void add(Pet pet){
        pets.add(pet);
    }

    void addAll(List<Pet> pets){
        pets.addAll(pets);
    }


    public class PetViewHolder extends RecyclerView.ViewHolder{



        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
