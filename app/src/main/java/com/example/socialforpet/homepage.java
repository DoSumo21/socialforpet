package com.example.socialforpet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepage extends AppCompatActivity {
    private ImageButton btnShare ;

    public static BottomNavigationView navigationView;
    private ViewPager2 viewSlideList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        navigationView = findViewById(R.id.bottomAppBar5);
    }
}