package com.example.pets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pets.Classes.Pet;
import com.example.pets.R;
import com.example.pets.interfaces.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    ArrayList<Pet> pets;
    ItemListener itemListener;

    private View.OnClickListener mOnClickListener;


    public PetAdapter(ArrayList<Pet> pets, View.OnClickListener listener, ItemListener itemListener) {
        this.pets = pets;
        this.mOnClickListener = listener;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false);
        return new PetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet currentPet = pets.get(position);

        holder.petName.setText(currentPet.getName());
        holder.petBreed.setText(currentPet.getBreed());
        holder.date.setText(currentPet.getDateAdded());

    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public void add(Pet pet){
        for(Pet p: pets){
            if(p.getId() == pet.getId()){
                return;
            }
        }
        pets.add(pet);
        notifyDataSetChanged();
        itemListener.size(pets.size());
    }

    public void addAll(List<Pet> pets){
        this.pets.addAll(pets);
        notifyDataSetChanged();
    }

    public void update(int id, Pet pet){
        for(int i=0; i<getItemCount(); i++){
            if(pets.get(i).getId() == id){
                pets.set(i, pet);
            }
        }
        notifyDataSetChanged();
    }

    public void remove(int pos){
        pets.remove(pos);
        notifyDataSetChanged();
        itemListener.size(pets.size());
    }

    public Pet get(int pos){
        return pets.get(pos);
    }


    public class PetViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView petName;
        TextView  petBreed;
        TextView date;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(mOnClickListener);
            image = itemView.findViewById(R.id.pet_items_circleImageView);
            petName = itemView.findViewById(R.id.pet_items_petName_textView);
            petBreed = itemView.findViewById(R.id.pet_items_breedName_textView);
            date = itemView.findViewById(R.id.pet_items_date_textView);

        }
    }
}
