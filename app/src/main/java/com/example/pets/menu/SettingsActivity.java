package com.example.pets.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.pets.Classes.SliderItem;
import com.example.pets.R;
import com.example.pets.adapter.ImageSliderAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ViewPager2 imageSliderViewPager;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setUp();

    }

    private void setUp() {
        imageSliderViewPager = findViewById(R.id.viewPager2);
        floatingActionButton = findViewById(R.id.fab);

        List<SliderItem> sliderItemList = new ArrayList<>();
        sliderItemList.add(new SliderItem(R.drawable.bg1));
        sliderItemList.add(new SliderItem(R.drawable.bg2));
        sliderItemList.add(new SliderItem(R.drawable.bg3));
        sliderItemList.add(new SliderItem(R.drawable.bg4));
        sliderItemList.add(new SliderItem(R.drawable.bg5));
        sliderItemList.add(new SliderItem(R.drawable.bg6));

        imageSliderViewPager.setAdapter(new ImageSliderAdapter(sliderItemList));


        imageSliderViewPager.setClipToPadding(false);
        imageSliderViewPager.setClipChildren(false);
        imageSliderViewPager.setOffscreenPageLimit(1);
        imageSliderViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer pageTransformer = new CompositePageTransformer();
        pageTransformer.addTransformer(new MarginPageTransformer(120));
        pageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 -Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
                page.setAlpha(0.43f + r);
            }
        });

        imageSliderViewPager.setPageTransformer(pageTransformer);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = ((ImageSliderAdapter) imageSliderViewPager.getAdapter()).get(imageSliderViewPager.getCurrentItem()).getImage();
                Toast.makeText(SettingsActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                updateTheme(id);
                finish();
            }
        });

    }

    void updateTheme(int themeId){
        SharedPreferences sP = getSharedPreferences("theme", MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putInt("theme", themeId);
        editor.commit();
    }
}
