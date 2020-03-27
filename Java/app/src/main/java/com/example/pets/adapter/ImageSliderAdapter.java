package com.example.pets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.pets.Classes.SliderItem;
import com.example.pets.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>  {

    List<SliderItem> sliderItemList;

    public ImageSliderAdapter(List<SliderItem> sliderItemList) {
        this.sliderItemList = sliderItemList;

    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageSliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.setImage(sliderItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItemList.size();
    }

    public SliderItem get(int pos){
        return  sliderItemList.get(pos);
    }

    class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }

        public void setImage(SliderItem sliderItem) {
            imageView.setImageResource(sliderItem.getImage());
        }


    }
}
